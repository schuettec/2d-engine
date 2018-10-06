package de.schuette.cobra2DSandbox.wall;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.VolatileImage;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.EntityInitializeException;
import de.schuette.cobra2D.entity.skills.Obstacle;
import de.schuette.cobra2D.rendering.HueBrightnessTransparencyFilter;
import de.schuette.cobra2D.rendering.RenderToolkit;
import de.schuette.cobra2D.system.Cobra2DEngine;
import de.schuette.cobra2DSandbox.texture.TextureEntity;

public class WallEntity extends TextureEntity implements Obstacle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WallEntity() {

	}

	@Override
	public void initialize(final Cobra2DEngine engine)
			throws EntityInitializeException {
		super.initialize(engine);
		final HueBrightnessTransparencyFilter filter = new HueBrightnessTransparencyFilter(
				300, 0.9f);
		this.setImage(RenderToolkit.convertSpriteToTransparentSprite(
				this.getImage(), filter));
	}

	@Override
	public void render(final Graphics2D graphics, final Point position) {
		VolatileImage img = getImage();
		RenderToolkit.renderTo(this.getDegrees(), 0, position, graphics, img);
	}

	@Override
	public boolean isCollision(final Entity entity) {
		return this.isInMyRect(entity);
	}

	@Override
	public boolean isCollision(final Point point) {
		return this.isInMyRect(point);
	}

	@Override
	public void collision(Entity entity) {
	}
}
