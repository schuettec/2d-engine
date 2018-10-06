package de.schuette.cobra2DSample.explosion;

import java.awt.Dimension;
import java.awt.Point;
import java.net.URL;
import java.util.Properties;

import de.schuette.cobra2D.map.Map;
import de.schuette.cobra2D.ressource.AnimationMemory;
import de.schuette.cobra2D.ressource.ImageMemory;
import de.schuette.cobra2D.system.Cobra2DConstants;
import de.schuette.cobra2D.system.Cobra2DConstants.RessourceType;
import de.schuette.cobra2D.system.Cobra2DEngine;
import de.schuette.cobra2DSandbox.camera.AdvancedMapCamera;
import de.schuette.cobra2DSandbox.camera.InfoOverlayCamera;
import de.schuette.cobra2DSandbox.fx.Explosion;
import de.schuette.cobra2DSandbox.texture.TextureEntity;

public class ExplosionExample {
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String... args) throws Exception {
		// Build configuration
		final Properties engineConfiguration = new Properties();
		engineConfiguration.put(Cobra2DConstants.RESOLUTION_X, "640"); // 1920
		engineConfiguration.put(Cobra2DConstants.RESOLUTION_Y, "480"); // 1080
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
		imgMemory.loadImage("explosion", new URL("resource:explosion.png"));
		imgMemory.loadImage("smoke", new URL("resource:smoke.png"));
		imgMemory.loadImage("fire0", new URL("resource:fire0.png"));
		imgMemory.loadImage("fire1", new URL("resource:fire1.png"));
		imgMemory.loadImage("fire2", new URL("resource:fire2.png"));

		final AnimationMemory aniMemory = engine.getAnimationMemory();
		aniMemory.loadAnimation("explosion", "explosion", 256, 256);

		// Build map, entities and cameras
		final Map map = engine.getMap();

		final TextureEntity texture = new TextureEntity();
		texture.setPosition(new Point(100, 100));
		texture.setTextureKey("sky");
		texture.initialize(engine);
		texture.createRectangleEntityPoints();
		map.addEntity(texture);

		final Explosion explosion = new Explosion();
		explosion.setAnimationKey("explosion");
		explosion.setPosition(new Point(200, 200));
		explosion.setSize(new Dimension(250, 250));
		explosion.initialize(engine);
		explosion.createRectangleEntityPoints();
		map.addEntity(explosion);

		final AdvancedMapCamera camera = new AdvancedMapCamera();
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
