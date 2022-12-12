package de.schuette.cobra2DSample.smoke;

import de.schuette.cobra2D.math.Point;
import java.net.URL;
import java.util.Properties;

import de.schuette.cobra2D.map.Map;
import de.schuette.cobra2D.ressource.ImageMemory;
import de.schuette.cobra2D.system.Cobra2DConstants;
import de.schuette.cobra2D.system.Cobra2DConstants.RessourceType;
import de.schuette.cobra2D.system.Cobra2DEngine;
import de.schuette.cobra2DSandbox.camera.AdvancedMapCamera;
import de.schuette.cobra2DSandbox.camera.InfoOverlayCamera;
import de.schuette.cobra2DSandbox.fx.Smoke;
import de.schuette.cobra2DSandbox.texture.TextureEntity;

public class SmokeExample {
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String... args) throws Exception {
		// Build configuration
		final Properties engineConfiguration = new Properties();
		engineConfiguration.put(Cobra2DConstants.RESOLUTION_X, "1024"); // 1920
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
		engine.getRenderer().setDrawEntityLines(false);
		engine.getRenderer().setDrawEntityPoints(false);

		// Load textures and animations
		final ImageMemory imgMemory = engine.getImageMemory();
		imgMemory.loadImage("sky", new URL("resource:sky.jpeg"));
		imgMemory.loadImage("smoke", new URL("resource:smoke.png"));
		// No images needed

		// Build map, entities and cameras
		final Map map = engine.getMap();

		final TextureEntity texture = new TextureEntity();
		texture.setPosition(new Point(100, 100));
		texture.setTextureKey("sky");
		texture.initialize(engine);
		texture.createRectangleEntityPoints();
		map.addEntity(texture);

		Smoke smoke = new Smoke();
		smoke.setTextureKey("smoke");
		smoke.setPosition(new Point(200, 200));
		smoke.setParticleCount(4);
		smoke.setMaxRadius(120);
		smoke.initialize(engine);
		smoke.createRectangleEntityPoints();
		map.addEntity(smoke);

		// smoke = new Smoke();
		// smoke.setTextureKey("smoke");
		// smoke.setPosition(new Point(300, 200));
		// smoke.setParticleCount(5);
		// smoke.setMaxRadius(150);
		// smoke.initialize(engine);
		// texture.createRectangleEntityPoints();
		// map.addEntity(smoke);
		//
		// smoke = new Smoke();
		// smoke.setTextureKey("smoke");
		// smoke.setPosition(new Point(400, 200));
		// smoke.setParticleCount(5);
		// smoke.setMaxRadius(150);
		// smoke.initialize(engine);
		// texture.createRectangleEntityPoints();
		// map.addEntity(smoke);

		final AdvancedMapCamera camera = new AdvancedMapCamera();
		// camera.setBenchmarkEntities(true);
		camera.setPosition(new Point(0, 0));
		camera.setFitToResolution(false);
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
}
