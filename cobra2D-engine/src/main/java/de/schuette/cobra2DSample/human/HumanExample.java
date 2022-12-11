package de.schuette.cobra2DSample.human;

import java.awt.Dimension;
import java.net.URL;
import java.util.Properties;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.map.Map;
import de.schuette.cobra2D.math.Point;
import de.schuette.cobra2D.rendering.HSBTransparencyFilter;
import de.schuette.cobra2D.rendering.RenderToolkit;
import de.schuette.cobra2D.ressource.AnimationMemory;
import de.schuette.cobra2D.ressource.ImageMemory;
import de.schuette.cobra2D.system.Cobra2DConstants;
import de.schuette.cobra2D.system.Cobra2DConstants.RessourceType;
import de.schuette.cobra2D.system.Cobra2DEngine;
import de.schuette.cobra2DSandbox.camera.AdvancedMapCamera;
import de.schuette.cobra2DSandbox.camera.InfoOverlayCamera;
import de.schuette.cobra2DSandbox.human.HeadEntity;
import de.schuette.cobra2DSandbox.human.WalkcycleEntity;
import de.schuette.cobra2DSandbox.modifier.ArrayModifier;
import de.schuette.cobra2DSandbox.modifier.EntityCreator;
import de.schuette.cobra2DSandbox.texture.TextureEntity;

public class HumanExample {
	/**
	 * @param args
	 * @throws Exception
	 */
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String... args) throws Exception {
		// Build configuration
		final Properties engineConfiguration = new Properties();
		engineConfiguration.put(Cobra2DConstants.RESOLUTION_X, "1920"); // 1920
		engineConfiguration.put(Cobra2DConstants.RESOLUTION_Y, "1080"); // 1080
		engineConfiguration.put(Cobra2DConstants.BIT_DEPHT, "32");
		engineConfiguration.put(Cobra2DConstants.REFRESH_REATE, "60");
		engineConfiguration.put(Cobra2DConstants.REQUESTED_FPS, "60");
		engineConfiguration.put(Cobra2DConstants.FULLSCREEN, "true");
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
		imgMemory.loadImage("walkcyle_harvey", new URL("resource:walkcyle_harvey.png"));
		imgMemory.loadImage("head", new URL("resource:head.png"));
		imgMemory.loadImage("floor", new URL("resource:floor.png"));
		imgMemory.loadImage("chair", new URL("resource:chair.png"));

		final AnimationMemory aniMemory = engine.getAnimationMemory();
		aniMemory.loadAnimation("walkcyle_harvey", "walkcyle_harvey", 120, 120);

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
		ArrayModifier modifier = new ArrayModifier();
		modifier.setPosition(new Point(100, 100));
		modifier.setxMultiplier(2);
		modifier.setyMultiplier(2);
		modifier.setCreator(creator);
		modifier.initialize(engine);
		map.addEntity(modifier);

		final TextureEntity chair = new TextureEntity();
		chair.setPosition(new Point(120, 120));
		chair.setTextureKey("chair");
		chair.setFilter(new HSBTransparencyFilter(300, 35));
		chair.initialize(engine);
		chair.createRectangleEntityPoints();
		// chair.createRectangleEntityPoints();
		map.addEntity(chair);

		final WalkcycleEntity walking = new WalkcycleEntity();
		walking.setTextureKey("walkcyle_harvey");
		walking.setPosition(new Point(1000, 200));
		walking.setFilter(RenderToolkit.getDefaultHSBFilter());
		walking.setDegrees(180);
		walking.initialize(engine);
		walking.createRectangleEntityPoints();
		map.addEntity(walking);

		final HeadEntity head = new HeadEntity();
		head.setWalkcycle(walking);
		head.setTextureKey("head");
		head.setFilter(RenderToolkit.getDefaultHSBFilter());
		head.initialize(engine);
		head.setSize(new Dimension(100, 100));
		head.createRectangleEntityPoints();
		map.addEntity(head);

		final AdvancedMapCamera camera = new AdvancedMapCamera();
		camera.setPosition(new Point(-100, -100));
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
