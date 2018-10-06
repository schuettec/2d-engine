package de.schuette.cobra2D.system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import de.schuette.cobra2D.benchmark.Benchmarker;
import de.schuette.cobra2D.controller.Controller;
import de.schuette.cobra2D.controller.SinglePlayerController;
import de.schuette.cobra2D.map.Map;
import de.schuette.cobra2D.rendering.RenderPausedAction;
import de.schuette.cobra2D.rendering.Renderer;
import de.schuette.cobra2D.rendering.RendererException;
import de.schuette.cobra2D.rendering.WindowRenderer;
import de.schuette.cobra2D.ressource.AnimationMemory;
import de.schuette.cobra2D.ressource.ImageMemory;
import de.schuette.cobra2D.ressource.URLClasspathHandler;
import de.schuette.cobra2D.ressource.URLInstallDirectoryHandler;
import de.schuette.cobra2D.ressource.URLResourceTypeHandler;
import de.schuette.cobra2D.ressource.URLStreamHandlerRegistry;
import de.schuette.cobra2D.system.Cobra2DConstants.RessourceType;

public class Cobra2DEngine {

	private static final String LOG4J_XML = "log4j.xml";

	private static Logger log;

	protected static final String[] PROPERTIES_KEYS = new String[] {
			Cobra2DConstants.RESOLUTION_X, Cobra2DConstants.RESOLUTION_Y,
			Cobra2DConstants.BIT_DEPHT, Cobra2DConstants.REFRESH_REATE,
			Cobra2DConstants.REQUESTED_FPS, Cobra2DConstants.FULLSCREEN,
			Cobra2DConstants.MAP_UPDATE, Cobra2DConstants.USE_RENDERER,
			Cobra2DConstants.DEFAULT_CONTROLLER };

	protected int fps;
	protected int actualFPS;
	protected int actualUPS;
	protected boolean running = false;
	protected int noDelaysPerYield = 5;
	protected int maxFrameSkips = 10;

	protected int ignoreSkipsAtStartUp = 100;

	protected boolean useRenderer;
	protected boolean mapUpdate;

	protected Renderer renderer;
	protected List<RenderPausedAction> actionsAfterRenderCycle = new ArrayList<RenderPausedAction>();

	protected Controller controller;

	protected Cobra2DLevel level;

	protected double updateTime;
	protected double renderTime;

	protected Thread rendererThread;
	protected Runnable rendererProcess = new Runnable() {

		@Override
		public void run() {
			long startTime, stopTime, neededTime, sleepTime, oversleepTime = 0;
			long excess = 0L;
			int noDelays = 0;

			final long period = (1000 / Cobra2DEngine.this.fps) * 1000000L;

			while (Cobra2DEngine.this.running) {

				// some statistics in every loop
				int mapUpdates = 0;

				// Start
				startTime = System.nanoTime();

				// Update world
				if (Cobra2DEngine.this.mapUpdate) {
					Cobra2DEngine.this.getMap().updateWorld();
					mapUpdates++;
				}

				Cobra2DEngine.this.updateTime = (System.nanoTime() - startTime) / 1000000.0;
				final long renderStart = System.nanoTime();

				if (Cobra2DEngine.this.useRenderer) {
					Cobra2DEngine.this.renderer.render();
				}

				Cobra2DEngine.this.renderTime = (System.nanoTime() - renderStart) / 1000000.0;

				// FLIPPING

				// Stop
				stopTime = System.nanoTime();
				neededTime = stopTime - startTime;
				sleepTime = (period - neededTime) - oversleepTime;

				if (sleepTime > 0) {
					// some time left in this cycle
					try {
						Thread.sleep(sleepTime / 1000000L); // nano ->
						// ms
					} catch (final InterruptedException ex) {
					}
					oversleepTime = (System.nanoTime() - stopTime) - sleepTime;
				} else {
					// Frame took longer than period
					excess -= sleepTime;
					oversleepTime = 0;

					if (++noDelays >= Cobra2DEngine.this.noDelaysPerYield) {
						Thread.yield(); // give another thread a chance
						// to
						// run
						noDelays = 0;
					}

				}

				// startTime = System.nanoTime();
				/*
				 * If frame animation is taking too long, update the game state
				 * without rendering it, to get the updates/sec nearer to the
				 * required FPS.
				 */

				if (Cobra2DEngine.this.mapUpdate) {

					int skips = 0;
					while ((excess > period)
							&& (skips < Cobra2DEngine.this.maxFrameSkips)) {
						excess -= period;
						if (!Cobra2DEngine.this.running) {
							break;
						}
						// Update world, test again for loop
						if (Cobra2DEngine.this.mapUpdate) {

							if (Cobra2DEngine.this.ignoreSkipsAtStartUp > 0) {
								Cobra2DEngine.this.ignoreSkipsAtStartUp--;
							}

							if (Cobra2DEngine.this.ignoreSkipsAtStartUp <= 0) {
								Cobra2DEngine.this.getMap().updateWorld(); // update
							}

							mapUpdates++;
						}
						// state
						// but
						// don't render
						skips++;
					}

				}
				stopTime = System.nanoTime();
				neededTime = stopTime - startTime;
				final int runningTime = (int) Math
						.round(neededTime / 1000000.0);

				Cobra2DEngine.this.actualFPS = (int) Math
						.round(1000.0 / runningTime);

				// Hochrechnung auf eine Sekunde
				final double factor = 1000.0 / runningTime;

				Cobra2DEngine.this.actualUPS = (int) Math.round(mapUpdates
						* factor);

				if (Cobra2DEngine.this.actionsAfterRenderCycle.size() != 0) {
					final RenderPausedAction action = Cobra2DEngine.this.actionsAfterRenderCycle
							.get(0);
					Cobra2DEngine.this.actionsAfterRenderCycle.remove(action);
					action.performAction();
				}
			}
		}

	};

	private boolean useKeyboardController;

	/**
	 * Use this static method at the beginning of your application to setup the
	 * JVM Properties used by this engine. Use the resource type to determine
	 * where resources are loaded from.
	 */
	public static void setupEnvironment(RessourceType resourceType) {
		// Prepare VM configurations:
		URLStreamHandlerRegistry registry = new URLStreamHandlerRegistry();
		registry.addHandler("resource",
				new URLResourceTypeHandler(resourceType.toString()));
		registry.addHandler("classpath", new URLClasspathHandler());
		registry.addHandler("install-dir", new URLInstallDirectoryHandler());
		URL.setURLStreamHandlerFactory(registry);

		// Prepare logging information
		DOMConfigurator.configureAndWatch(LOG4J_XML, 60 * 1000);
		log = Logger.getLogger(Cobra2DEngine.class);
		log.info("Cobra2DEngine - Environment initialized.");
	}

	public static void main(final String[] args) throws Exception {
		// Setup JVM properties
		setupEnvironment(RessourceType.INSTALL_DIR);

		// Check arguments!
		if (args.length != 1) {
			log.error("Start Cobra2DEngine with the path to configfile.");
			System.exit(1);
		}

		final String configString = args[0];
		URL configURL = null;
		try {
			configURL = new URL(configString);
		} catch (MalformedURLException e) {
			log.error(
					"URL to config file is malformed. Please specify protocol and location correctly",
					e);
			e.printStackTrace();
			System.exit(1);
		}

		new Cobra2DEngine(configURL, new WindowRenderer());

	}

	/**
	 * Constructs an instance of the engine by using the configURL to read the
	 * configurations from a file. The engine will use the default window
	 * renderer.
	 * 
	 * @param configURL
	 *            The URL to the config file.
	 * @throws Exception
	 *             Thrown if any exception occurs.
	 */
	public Cobra2DEngine(final URL configURL) throws Exception {
		this.loadConfiguration(configURL, null);
	}

	/**
	 * Constructs an instance of the engine by using the given properties to
	 * configure the engines runtime. The engine will use the default window
	 * renderer.
	 * 
	 * @param properties
	 *            The properties used for configuration of the engine.
	 * @throws Exception
	 *             Thrown if any exception occurs.
	 */
	public Cobra2DEngine(final Properties properties) throws Exception {
		this.loadConfiguration(properties, null);
	}

	public void setRenderer(Renderer renderer) {
		pauseEngine();
		if (this.renderer != null) {
			this.renderer.finish();
		}
		this.renderer = renderer;
		if (this.useKeyboardController) {
			this.controller.finish();
			this.controller = new SinglePlayerController(this.renderer);
		}
		resumeEngine();
	}

	/**
	 * Constructs an instance of the engine by using the configURL to read the
	 * configurations from a file. You can specify a special renderer instance
	 * to determine the rendering target.
	 * 
	 * @param configURL
	 *            The URL to the config file.
	 * @param renderer
	 *            The special renderer instance or NULL to use the default
	 *            renderer
	 * @throws Exception
	 *             Thrown if any exception occurs.
	 */
	public Cobra2DEngine(final URL configURL, Renderer renderer)
			throws Exception {
		this.loadConfiguration(configURL, renderer);
	}

	/**
	 * Constructs an instance of the engine by using the given properties to
	 * configure the engines runtime. You can specify a special renderer
	 * instance to determine the rendering target.
	 * 
	 * @param properties
	 *            The properties used for configuration of the engine.
	 * @param renderer
	 *            The special renderer instance or NULL to use the default
	 *            renderer
	 * @throws Exception
	 *             Thrown if any exception occurs.
	 */
	public Cobra2DEngine(final Properties properties, Renderer renderer)
			throws Exception {
		this.loadConfiguration(properties, renderer);
	}

	private void loadConfiguration(final URL configURL, Renderer renderer)
			throws Exception {

		// Load configfile
		try {
			// URL
			URLConnection connection = configURL.openConnection();
			connection.setReadTimeout(Cobra2DConstants.READ_TIMEOUT);
			connection.setConnectTimeout(Cobra2DConstants.CONNECTION_TIMEOUT);
			InputStream inStream = connection.getInputStream();
			final Properties preloadedProperties = new Properties();
			preloadedProperties.load(inStream);
			inStream.close();
			this.loadConfiguration(preloadedProperties, renderer);
			log.info("Engine configuration done. Using renderer: "
					+ renderer.getClass().getSimpleName());
		} catch (final FileNotFoundException e) {
			throw e;
		} catch (final IOException e) {
			throw e;
		}

	}

	private void loadConfiguration(final Properties properties,
			Renderer renderer) throws Exception {
		// Checking config
		for (final String key : Cobra2DEngine.PROPERTIES_KEYS) {
			if (!properties.containsKey(key)) {
				log.error("Configuration error. See details below.");
				throw new Exception(
						"Incorrect configfile: Missing configuration for '"
								+ key + "'");
			}
		}

		// Start engineprocesses such as renderloop, map-Calculating other stuff
		// Add the renderer to a JFrame
		final boolean fullscreen = properties.getProperty(
				Cobra2DConstants.FULLSCREEN).equalsIgnoreCase("true");
		this.useRenderer = properties
				.getProperty(Cobra2DConstants.USE_RENDERER).equalsIgnoreCase(
						"true");
		this.renderer = renderer;
		if (useRenderer) {
			log.info("Configured using renderer.");
		} else {
			log.info("Configured using no renderer, working dedicated instead.");
		}

		this.mapUpdate = properties.getProperty(Cobra2DConstants.MAP_UPDATE)
				.equalsIgnoreCase("true");
		log.info("Configured performing map updates.");

		this.useKeyboardController = Boolean.parseBoolean(properties
				.getProperty(Cobra2DConstants.DEFAULT_CONTROLLER));
		log.info("Configured using keyboard controller.");

		final int resX = Integer.parseInt(properties
				.getProperty(Cobra2DConstants.RESOLUTION_X));
		final int resY = Integer.parseInt(properties
				.getProperty(Cobra2DConstants.RESOLUTION_Y));
		log.info("Configured using " + resX + "x" + resY
				+ " for rendering dimensions.");
		final int bitDepth = Integer.parseInt(properties
				.getProperty(Cobra2DConstants.BIT_DEPHT));
		log.info("Configured using " + bitDepth + " bit depth.");
		final int refreshRate = Integer.parseInt(properties
				.getProperty(Cobra2DConstants.REFRESH_REATE));
		log.info(refreshRate + " Hz refresh rate requested by configuration.");
		this.fps = Integer.parseInt(properties
				.getProperty(Cobra2DConstants.REQUESTED_FPS));
		log.info(fps + " frames per second requested by configuration.");

		// Create renderer base on the configuration.
		try {

			this.level = new Cobra2DLevel();

			if (this.useRenderer) {
				if (renderer == null) {
					this.renderer = new WindowRenderer();
				}
				this.renderer.initializeRenderer(this, resX, resY, bitDepth,
						refreshRate, fullscreen);
				log.info("Renderer initialized.");

				if (this.useKeyboardController) {
					this.controller = new SinglePlayerController(this.renderer);
				}
			}

		} catch (final RendererException e) {
			throw e;
		}

	}

	/**
	 * This method is used to load a level into the engine.
	 * 
	 * @param level
	 *            The level to load.
	 */
	public void loadLevel(final Cobra2DLevel level) {
		// Externalize in thread:
		final Thread externAction = new Thread() {
			@Override
			public void run() {
				super.run();
				Cobra2DEngine.this.running = false;

				waitForRenderCycle();
				Cobra2DEngine.this.resetEngine();

				level.afterDeserialization(Cobra2DEngine.this);

				resumeEngine();

				log.info("Level file loaded, resuming engine.");

			}
		};
		externAction.start();
		try {
			externAction.join();
		} catch (InterruptedException e) {
		}
		log.info("Engine paused for loading level...");
	}

	/**
	 * Loads a serialized level from file system and replaces the current engine
	 * state. If loading was successfull, the engine continues running the
	 * loaded level.
	 * 
	 * @param levelFile
	 *            The levelfile of the serialized cobra2D level.
	 */
	public void loadLevel(final File levelFile) {
		// Externalize in thread:
		final Thread externAction = new Thread() {
			@Override
			public void run() {
				super.run();
				Cobra2DEngine.this.running = false;

				waitForRenderCycle();
				Cobra2DEngine.this.resetEngine();

				Cobra2DEngine.this.loadLevelInternal(levelFile);

				log.info("Level file loaded, resuming engine.");

			}
		};
		externAction.start();
		log.info("Engine paused for loading level...");
	}

	private void loadLevelInternal(final File levelFile) {
		// Uncomment to load with old engine level loader
		// Cobra2DEngineLoader.loadLevel(engine, levelFile);

		// Uncomment to load with serialization framework
		final Cobra2DLoader loader = new Cobra2DLoader();
		try {
			this.level = loader.loadLevel(this, levelFile);
		} catch (final Exception e) {
			log.info("Failed to load levelfile.", e);
		}

		// use setLevel
		this.resumeEngine();
	}

	/**
	 * Saves the current state of the cobra2D level to the file. The engine is
	 * stopped to take a snapshot of the current state of the level.
	 * 
	 * @param levelFile
	 *            The file to save engine level.
	 */
	public void saveLevel(final File levelFile) {
		log.info("Pausing engine to save level state.");
		this.pauseEngine();

		// Uncomment to load with old engine level loader
		// Cobra2DEngineLoader.loadLevel(engine, levelFile);

		// Uncomment to load with serialization framework
		final Cobra2DLoader loader = new Cobra2DLoader();
		try {
			loader.saveLevel(this.level, levelFile);
			log.info("Engine state saved to levelfile.");
		} catch (final Exception e) {
			log.info("Failed to save levelfile.", e);
		}

		// use setLevel
		this.resumeEngine();
		log.info("Engine resumed.");
	}

	/**
	 * Resumes the render&update engine after exchanging or modifying resource
	 * storages and renderer.
	 */
	public void resumeEngine() {
		this.running = true;
		if (useRenderer) {
			this.rendererThread = new Thread(this.rendererProcess);
			this.rendererThread.start();
		}
	}

	/**
	 * Pauses the render&update engine, so that resource storages and renderer
	 * can be exchanged or modified.
	 */
	public void pauseEngine() {
		final RenderPausedAction action = new RenderPausedAction() {

			@Override
			public void performAction() {
				Cobra2DEngine.this.running = false;

			}
		};
		this.addActionAfterRenderCycle(action);

		this.waitForRenderCycle();
	}

	private void waitForRenderCycle() {
		try {
			if (this.rendererThread != null) {
				this.rendererThread.join();
				this.rendererThread = null;
			}
		} catch (final InterruptedException e) {
		}
	}

	/**
	 * This method shuts down the engine and frees all resources. This method
	 * proceeds with high NPE security, to avoid unneccessary NPEs during
	 * destruction. NOTE: THIS METHOD INVALIDATES THIS OBJECT AND SHOULD BE USED
	 * IF ENGINE IS NOT USED ANYMORE.
	 */
	public void shutdownEngine() {
		final RenderPausedAction shutdownAction = new RenderPausedAction() {

			@Override
			public void performAction() {
				// Do not call pauseEninge() because this method creates a
				// renderPausedAction that will wait endless for the render
				// loop.
				Cobra2DEngine.this.running = false;
				Cobra2DEngine.this.level.finish();

				if (Cobra2DEngine.this.renderer != null) {
					Cobra2DEngine.this.renderer.finish();
				}

				Cobra2DEngine.this.rendererThread = null;
				Cobra2DEngine.this.renderer = null;
			}
		};
		this.addActionAfterRenderCycle(shutdownAction);
	}

	/**
	 * Clears the resource storages and data structures to switch levels by
	 * using the same engine instance.
	 */
	public void resetEngine() {
		if (this.useKeyboardController) {
			this.controller.finish();
			this.controller = new SinglePlayerController(this.renderer);
		}
		this.level.finish();
	}

	public void addActionAfterRenderCycle(final RenderPausedAction action) {
		this.actionsAfterRenderCycle.add(action);
	}

	public Renderer getRenderer() {
		return this.renderer;
	}

	public Controller getController() {
		return this.controller;
	}

	public int getActualFPS() {
		return this.actualFPS;
	}

	public void setActualFPS(final int actualFPS) {
		this.actualFPS = actualFPS;
	}

	public boolean isUseRenderer() {
		return this.useRenderer;
	}

	public void setUseRenderer(final boolean useRenderer) {
		this.useRenderer = useRenderer;
	}

	public boolean isMapUpdate() {
		return this.mapUpdate;
	}

	public void setMapUpdate(final boolean mapUpdate) {
		this.mapUpdate = mapUpdate;
	}

	public void setController(final Controller controller) {
		this.controller = controller;
	}

	public HashMap<String, Class<?>> getEntityClasses() {
		return this.level.getEntityClasses();
	}

	public Map getMap() {
		return this.level.getMap();
	}

	public ImageMemory getImageMemory() {
		return this.level.getImageMemory();
	}

	public AnimationMemory getAnimationMemory() {
		return this.level.getAnimationMemory();
	}

	public Cobra2DLevel getLevel() {
		return this.level;
	}

	/**
	 * This method is only used internally by this engine. To bind a level to
	 * the engine use level.afterDeserialization(engine);
	 * 
	 * @param level
	 *            The level to set internally.
	 */
	protected void setLevel(final Cobra2DLevel level) {
		this.level = level;

	}

	public boolean isRunning() {
		return this.running;
	}

	public int getActualUPS() {
		// if (this.actualUPS == 0) {
		// this.actualUPS = this.fps;
		// }
		return this.actualUPS;
	}

	public double getUpdateTime() {
		return this.updateTime;
	}

	public double getRenderTime() {
		return this.renderTime;
	}

	/**
	 * Returns a copy of the benchmark from the last render cycle.
	 * 
	 * @return
	 */
	public Benchmarker getBenchmarker() {
		return this.renderer.getBenchmarker();
	}

	public void setDrawEntityLines(boolean b) {
		if (isUseRenderer())
			renderer.setDrawEntityLines(b);
	}

	public void setDrawEntityPoints(boolean b) {
		if (isUseRenderer())
			renderer.setDrawEntityPoints(b);
	}

	public boolean isDrawEntityLines() {
		if (isUseRenderer())
			return renderer.isDrawEntityLines();
		else
			return false;
	}

	public boolean isDrawEntities() {
		if (isUseRenderer())
			return renderer.isDrawEntities();
		else
			return false;
	}

	public boolean isDrawEntityPoints() {
		if (isUseRenderer())
			return renderer.isDrawEntityPoints();
		else
			return false;
	}

	public boolean isDrawEntityCenterPoint() {
		if (isUseRenderer())
			return renderer.isDrawEntityCenterPoint();
		else
			return false;
	}

	public void setDrawEntityCenterPoint(boolean b) {
		if (isUseRenderer())
			renderer.setDrawEntityCenterPoint(b);
	}

}
