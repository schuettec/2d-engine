package de.schuette.cobra2DSample.texture;

import java.net.URL;
import java.util.Properties;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.map.Map;
import de.schuette.cobra2D.math.Point;
import de.schuette.cobra2D.ressource.ImageMemory;
import de.schuette.cobra2D.system.Cobra2DConstants;
import de.schuette.cobra2D.system.Cobra2DConstants.RessourceType;
import de.schuette.cobra2D.system.Cobra2DEngine;
import de.schuette.cobra2DSandbox.camera.AdvancedMapCamera;
import de.schuette.cobra2DSandbox.camera.InfoOverlayCamera;
import de.schuette.cobra2DSandbox.modifier.EntityCreator;
import de.schuette.cobra2DSandbox.texture.TextureEntity;

public class TextureExample {
	public static void main(final String... args) throws Exception {
		// Build configuration
		final Properties engineConfiguration = new Properties();
		engineConfiguration.put(Cobra2DConstants.RESOLUTION_X, "1280"); // 1920
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
		engine.getRenderer().setDrawEntityLines(true);
		engine.getRenderer().setDrawEntityPoints(true);
		engine.getRenderer().setDrawEntityCenterPoint(true);

		// Load textures and animations
		final ImageMemory imgMemory = engine.getImageMemory();
		imgMemory.loadImage("floor", new URL("resource:floor.png"));

		// final AnimationMemory aniMemory = engine.getAnimationMemory();
		// aniMemory.loadAnimation("walkcyle_harvey", "walkcyle_harvey", 120,
		// 120);

		// Build map, entities and cameras
		final Map map = engine.getMap();

		final EntityCreator creator = new EntityCreator() {

			@Override
			public Entity createEntity() {
				final TextureEntity texture = new TextureEntity();
				texture.setPosition(new Point(0, 0));
				texture.setTextureKey("floor");
				return texture;
			}

			@Override
			public void postInitializeEntity(Entity entity) {
				entity.createRectangleEntityPoints();
			}

		};
//		ArrayModifier modifier = new ArrayModifier();
//		modifier.setPosition(new Point(100, 100));
//		modifier.setxMultiplier(10);
//		modifier.setyMultiplier(10);
//		modifier.setCreator(creator);
//		modifier.initialize(engine);
//		map.addEntity(modifier);

		final TextureEntity texture = new TextureEntity();
		texture.initialize(engine);
		texture.setPosition(new Point(0, 0));
		texture.setTextureKey("floor");
		texture.createRectangleEntityPointsWithPositionInCenter();
		map.addEntity(texture);

		final AdvancedMapCamera camera = new AdvancedMapCamera();
		camera.setBenchmarkEntities(true);
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
