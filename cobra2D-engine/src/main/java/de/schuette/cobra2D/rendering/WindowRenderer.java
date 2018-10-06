package de.schuette.cobra2D.rendering;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import de.schuette.cobra2D.benchmark.Benchmark;
import de.schuette.cobra2D.benchmark.Benchmarker;
import de.schuette.cobra2D.entity.skills.Camera;
import de.schuette.cobra2D.math.Point;
import de.schuette.cobra2D.system.Cobra2DEngine;

public class WindowRenderer extends JFrame implements Renderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2790433653469935308L;
	private final int CORRECTION_X = 0;
	private final int CORRECTION_Y = 0;

	private final int NUM_BUFFERS = 2;

	private GraphicsDevice gd;
	private BufferStrategy bufferStrategy;

	private VolatileImage worldView;

	// private boolean initialized = false;

	private int resolutionX, resolutionY, bitDepth, refreshRate;

	private boolean fullscreen;

	private boolean drawEntityLines = false;
	private boolean drawEntityPoints = false;
	private boolean drawEntities = true;

	private boolean cursorVisible = true;
	private Cobra2DEngine engine;

	protected Benchmarker benchmarkAfterRendering;

	protected Graphics2D bufferGraphics;
	protected Graphics2D worldViewGraphics;
	private boolean drawEntityCenterPoint;

	@Override
	public void render() {

		final Benchmarker benchmark = Benchmarker.getInstance();

		/*
		 * Start benchmarking the whole rendering.
		 */
		final Benchmark rendercycle = benchmark.createBenchmarkAndStart("Render cycle",
				"Rendering of all elements that will be visible through cameras on the screen.");

		/*
		 * Start benchmarking fitting cameras.
		 */
		final Benchmark fittingCameras = benchmark.createBenchmarkAndStart("Fitting cameras",
				"Rendering the cameras view resized to worldview buffer.");

		// Try to avoid senseless
		boolean worldViewChanged = false;
		boolean worldViewClear = false;
		final List<Camera> cameras = this.engine.getMap().getCameras();
		final int camerasSize = cameras.size();
		for (int i = 0; i < camerasSize; i++) {
			final Camera camera = cameras.get(i);
			if (camera.fitToResolution()) {
				final Rectangle viewport = camera.getViewportRectangle();
				// Prepare backbuffer for camera view
				final VolatileImage cameraView = RenderToolkit.createVolatileImage(viewport.width, viewport.height);
				final Graphics2D camGraphis = (Graphics2D) cameraView.getGraphics();
				camera.render(camGraphis);
				camGraphis.dispose();

				// Clear only if the first camera want to render to worldview
				if (!worldViewClear) {
					worldViewGraphics.setBackground(Color.BLACK);
					worldViewGraphics.setColor(Color.BLACK);
					// HINTERGRUND ZEICHNEN
					worldViewGraphics.fillRect(0, 0, this.worldView.getWidth(), this.worldView.getHeight());
					worldViewClear = true;
				}

				// Render cameraview to worldView
				RenderToolkit.renderTo(

						new Point(0, 0), new Dimension(this.worldView.getWidth(), this.worldView.getHeight()),
						worldViewGraphics, cameraView);
				worldViewChanged = true;

			}
		}
		/*
		 * Stop benchmarking fitting cameras.
		 */
		fittingCameras.stopBenchmark();

		// Clear now the screen buffer
		bufferGraphics.setBackground(Color.BLACK);
		bufferGraphics.setColor(Color.BLACK);
		bufferGraphics.fillRect(0, 0, this.resolutionX, this.resolutionY);

		/*
		 * Start benchmarking Render the worldview (that is used to resize to screen
		 * resolution).
		 */
		final Benchmark wordView = benchmark.createBenchmarkAndStart("Render world view",
				"Render the world view to backbuffer.");
		// Render worldview to backbuffer only if someone was drawing to it
		if (worldViewChanged) {
			if (this.fullscreen) {
				bufferGraphics.drawImage(this.worldView, 0, 0, this.resolutionX, this.resolutionY, 0, 0,
						this.worldView.getWidth(), this.worldView.getHeight(), null);
			} else {
				bufferGraphics.drawImage(this.worldView, this.CORRECTION_X, this.CORRECTION_Y,
						this.CORRECTION_X + this.resolutionX, this.CORRECTION_Y + this.resolutionY, 0, 0,
						this.worldView.getWidth(), this.worldView.getHeight(), null);
			}
		}
		/*
		 * Stop benchmarking the worldview
		 */
		wordView.stopBenchmark();

		/*
		 * Start benchmarking Non-Fitting cameras.
		 */
		final Benchmark nonFittingCameras = benchmark.createBenchmarkAndStart("Non-Fitting cameras",
				"Rendering the cameras that will not fit the screen resolution.");
		for (int i = 0; i < cameras.size(); i++) {
			final Camera camera = cameras.get(i);
			if (!camera.fitToResolution()) {
				/*
				 * Start benchmarking rendering of a non-fitting camera.
				 */
				final Benchmark cameraBenchmark = benchmark.createBenchmarkAndStart(
						"Camera: " + camera.getClass().getSimpleName(),
						"Rendering the view camera " + camera.getClass().getSimpleName());
				camera.render(bufferGraphics);
				/*
				 * Stop benchmarking
				 */
				cameraBenchmark.stopBenchmark();
			}
		}
		/*
		 * Stop benchmarking Non-Fitting cameras.
		 */
		nonFittingCameras.stopBenchmark();

		/*
		 * Start benchmarking Non-Fitting cameras.
		 */
		final Benchmark backbuffer = benchmark.createBenchmarkAndStart("Screen buffer flip",
				"Flipping the backbuffer to screen");
		this.bufferStrategy.show();
		/*
		 * Stop benchmarking the backbuffer flipping.
		 */
		backbuffer.stopBenchmark();

		/*
		 * Stop benchmarking the rendercycle
		 */
		rendercycle.stopBenchmark();

		// Save copy of benchmark
		this.benchmarkAfterRendering = benchmark.getSnapshot();
		benchmark.clear();
	}

	public WindowRenderer() {
	}

	@Override
	public void initializeRenderer(final Cobra2DEngine engine, final int resolutionX, final int resolutionY,
			final int bitDepth, final int refreshRate, final boolean fullscreen) throws RendererException {
		this.engine = engine;
		this.resolutionX = resolutionX;
		this.resolutionY = resolutionY;
		this.bitDepth = bitDepth;
		this.refreshRate = refreshRate;
		this.fullscreen = fullscreen;

		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(final WindowEvent e) {

			}

			@Override
			public void windowIconified(final WindowEvent e) {

			}

			@Override
			public void windowDeiconified(final WindowEvent e) {

			}

			@Override
			public void windowDeactivated(final WindowEvent e) {

			}

			@Override
			public void windowClosing(final WindowEvent e) {
				WindowRenderer.this.engine.shutdownEngine();
			}

			@Override
			public void windowClosed(final WindowEvent e) {

			}

			@Override
			public void windowActivated(final WindowEvent e) {

			}
		});

		this.worldView = RenderToolkit.createVolatileImage(resolutionX, resolutionY);
		this.worldViewGraphics = (Graphics2D) this.worldView.getGraphics();

		if (fullscreen) {
			this.setUndecorated(true);
		}

		this.pack();
		this.getContentPane().getGraphics().setColor(Color.BLACK);
		this.getContentPane().getGraphics().fillRect(0, 0, this.getContentPane().getWidth(),
				this.getContentPane().getHeight());
		this.setBackground(Color.BLACK);
		this.setForeground(Color.BLACK);

		this.setTitle("Cobra2DEngine");
		this.setFocusable(true);
		this.requestFocus();

		if (fullscreen) {
			final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			this.gd = ge.getDefaultScreenDevice();
			// setUndecorated(true); // no menu bar, borders, etc.
			this.setIgnoreRepaint(true);
			// turn off paint events since doing active rendering
			this.setResizable(false);
			if (!this.gd.isFullScreenSupported()) {
				throw new RendererException("Fullscreenmode is not supported by vido card.");
			}
			this.gd.setFullScreenWindow(this); // switch on FSEM
			// we can now adjust the display modes, if we wish
			try {
				this.gd.setDisplayMode(
						new DisplayMode(this.resolutionX, this.resolutionY, this.bitDepth, this.refreshRate));

			} catch (final Exception e) {
				this.finish();
				throw new RendererException("Cannot establish display mode! " + resolutionX + "x" + resolutionY);
			}

		} else {

			// this.setIgnoreRepaint(true);
			// GraphicsDevice graphicsDevice = getGraphicsConfiguration()
			// .getDevice();
			// graphicsDevice.setFullScreenWindow(this);
			// graphicsDevice.setDisplayMode(new DisplayMode(xResolution,
			// yResolution, bitDepht , refreshRate));
			this.setResizable(false);
			this.setSize(resolutionX + this.CORRECTION_X + 5, resolutionY + this.CORRECTION_Y + 5);
			this.setVisible(true);

		}

		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					WindowRenderer.this.createBufferStrategy(WindowRenderer.this.NUM_BUFFERS);
				}
			});
		} catch (final Exception e) {

		}

		try {
			Thread.sleep(500);
		} catch (final InterruptedException ex) {
		}
		this.bufferStrategy = this.getBufferStrategy(); // store for later

		// initialized = true;

		this.bufferGraphics = (Graphics2D) this.bufferStrategy.getDrawGraphics();
	}

	public void setWorldViewSize(final Dimension size) {
		this.worldView = RenderToolkit.createVolatileImage(size.width, size.height);

	}

	@Override
	public Dimension getWorldViewSize() {
		return new Dimension(this.worldView.getWidth(), this.worldView.getHeight());
	}

	@Override
	public void finish() {

		final Thread finisher = new Thread(new Runnable() {

			@Override
			public void run() {
				worldViewGraphics.dispose();
				bufferGraphics.dispose();
				// UNINITIALIZE RENDERING MODE
				if (WindowRenderer.this.fullscreen) {
					final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
					WindowRenderer.this.gd = ge.getDefaultScreenDevice();
					WindowRenderer.this.gd.setFullScreenWindow(null);
				}

				WindowRenderer.this.setVisible(false);

				WindowRenderer.this.dispose();

			}

		});

		SwingUtilities.invokeLater(finisher);

	}

	public Dimension getScreenSize() {
		return new Dimension(this.resolutionX, this.resolutionY);
	}

	@Override
	public int getResolutionX() {
		return this.resolutionX;
	}

	@Override
	public int getResolutionY() {
		return this.resolutionY;
	}

	public int getBitDepth() {
		return this.bitDepth;
	}

	public int getRefreshRate() {
		return this.refreshRate;
	}

	public boolean isFullscreen() {
		return this.fullscreen;
	}

	@Override
	public boolean isDrawEntityLines() {
		return this.drawEntityLines;
	}

	@Override
	public void setDrawEntityLines(final boolean drawEntityLines) {
		this.drawEntityLines = drawEntityLines;
	}

	@Override
	public boolean isDrawEntityPoints() {
		return this.drawEntityPoints;
	}

	@Override
	public void setDrawEntityPoints(final boolean drawEntityPoints) {
		this.drawEntityPoints = drawEntityPoints;
	}

	@Override
	public boolean isDrawEntities() {
		return this.drawEntities;
	}

	public void setDrawEntities(final boolean drawEntities) {
		this.drawEntities = drawEntities;

	}

	public void showCursor(final boolean visible) {
		this.cursorVisible = visible;
		if (visible) {
			this.setCursor(Cursor.getDefaultCursor());
		} else {
			this.setCursor(this.getToolkit().createCustomCursor(new ImageIcon("").getImage(), new java.awt.Point(0, 0),
					"No Cursor"));
		}

	}

	public void setCursorImage(final String texturAddress) {
		final VolatileImage image = this.engine.getImageMemory().getImage(texturAddress);
		if (image == null) {
			System.out.println("Cannot set cursor to image with texture address '" + texturAddress + "'.");
			return;
		}
		this.cursorVisible = true;
		this.setCursor(
				this.getToolkit().createCustomCursor(image.getSnapshot(), new java.awt.Point(0, 0), "CustomCursor"));
	}

	@Override
	public boolean isCursorVisible() {
		return this.cursorVisible;
	}

	@Override
	public Benchmarker getBenchmarker() {
		return this.benchmarkAfterRendering;
	}

	@Override
	public boolean isDrawEntityCenterPoint() {
		return this.drawEntityCenterPoint;
	}

	@Override
	public void setDrawEntityCenterPoint(boolean b) {
		this.drawEntityCenterPoint = b;
	}

}
