package de.schuette.cobra2DSample.flash;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.net.URL;
import java.util.Properties;

import de.schuette.cobra2D.map.Map;
import de.schuette.cobra2D.ressource.ImageMemory;
import de.schuette.cobra2D.system.Cobra2DConstants;
import de.schuette.cobra2D.system.Cobra2DConstants.RessourceType;
import de.schuette.cobra2D.system.Cobra2DEngine;
import de.schuette.cobra2DSandbox.camera.AdvancedMapCamera;
import de.schuette.cobra2DSandbox.camera.InfoOverlayCamera;
import de.schuette.cobra2DSandbox.fx.Flash;

public class FlashExample implements MouseMotionListener {
	protected Flash flash;

	public FlashExample() throws Exception {

	}

	public void start() throws Exception {
		// Build configuration
		final Properties engineConfiguration = new Properties();
		engineConfiguration.put(Cobra2DConstants.RESOLUTION_X, "1366"); // 1920
		engineConfiguration.put(Cobra2DConstants.RESOLUTION_Y, "768"); // 1080
		engineConfiguration.put(Cobra2DConstants.BIT_DEPHT, "32");
		engineConfiguration.put(Cobra2DConstants.REFRESH_REATE, "60");
		engineConfiguration.put(Cobra2DConstants.REQUESTED_FPS, "100");
		engineConfiguration.put(Cobra2DConstants.FULLSCREEN, "false");
		engineConfiguration.put(Cobra2DConstants.MAP_UPDATE, "true");
		engineConfiguration.put(Cobra2DConstants.USE_RENDERER, "true");
		engineConfiguration.put(Cobra2DConstants.DEFAULT_CONTROLLER, "true");

		// Construct engine
		Cobra2DEngine.setupEnvironment(RessourceType.CLASSPATH);
		final Cobra2DEngine engine = new Cobra2DEngine(engineConfiguration);
		// Set debug option: Renderer makes now entity lines visible
		// engine.getRenderer().setDrawEntityLines(true);
		// engine.getRenderer().setDrawEntityPoints(true);

		// Load textures and animations
		final ImageMemory imgMemory = engine.getImageMemory();
		imgMemory.loadImage("sky", new URL("resource:sky.jpeg"));
		imgMemory.loadImage("flash", new URL("resource:flash.png"));
		// No images needed

		// Build map, entities and cameras
		final Map map = engine.getMap();

		this.flash = new Flash();
		this.flash.setStart(new Point(100, 100));
		this.flash.setEnd(new Point(400, 400));
		this.flash.setMaxDistortion(20);
		this.flash.initialize(engine);

		map.addEntity(this.flash);

		engine.getRenderer().addMouseMotionListener(this);

		final AdvancedMapCamera camera = new AdvancedMapCamera();
		camera.setPosition(new Point(0, 0));
		camera.initialize(engine);
		map.addEntity(camera);

		final InfoOverlayCamera infoCam = new InfoOverlayCamera();
		infoCam.initialize(engine);
		map.addEntity(infoCam);

		// Start level by resuming engines update and render thread.
		// This must be done manually if there is level loaded by
		// engine.loadLevel(levelFile)
		engine.resumeEngine();
	}

	@Override
	public void mouseDragged(final MouseEvent e) {

	}

	@Override
	public void mouseMoved(final MouseEvent e) {
		this.flash.setEnd(e.getPoint());
	}

	public static void main(final String... args) throws Exception {
		final FlashExample starter = new FlashExample();
		starter.start();
	}

}
