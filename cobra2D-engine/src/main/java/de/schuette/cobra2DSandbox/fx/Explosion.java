package de.schuette.cobra2DSandbox.fx;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.VolatileImage;

import de.schuette.cobra2D.entity.EntityInitializeException;
import de.schuette.cobra2D.entity.editing.Editable;
import de.schuette.cobra2D.entity.skills.Moveable;
import de.schuette.cobra2D.entity.skills.Renderable;
import de.schuette.cobra2D.math.Parabel;
import de.schuette.cobra2D.rendering.HueBrightnessTransparencyFilter;
import de.schuette.cobra2D.rendering.RenderToolkit;
import de.schuette.cobra2D.system.Cobra2DEngine;
import de.schuette.cobra2DSandbox.animation.AnimationEntity;

@Editable
public class Explosion extends AnimationEntity implements Renderable, Moveable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected transient Parabel alphaParabel;

	protected boolean dead = false;
	protected int index = 0;

	@Override
	public void render(final Graphics2D graphics, final Point position) {
		VolatileImage aktImage = getAnimation().getImage(this.index);

		final HueBrightnessTransparencyFilter filter = new HueBrightnessTransparencyFilter(
				300, 0.01f);
		aktImage = RenderToolkit.convertSpriteToTransparentSprite(aktImage,
				filter);
		final float alpha = (float) this.alphaParabel.getValue(this.index);

		RenderToolkit.renderTo(alpha, 0.0d, position, this.size, graphics,
				aktImage);

		this.index += 1; // 4

		if (this.index >= getAnimation().getPictureCount() - 1) {
			this.dead = true;
			this.index = 0;
			return;
		}

	}

	@Override
	public void initialize(final Cobra2DEngine engine)
			throws EntityInitializeException {
		super.initialize(engine);

		int maxFrame = getAnimation().getPictureCount();

		this.alphaParabel = new Parabel(new Point.Double(maxFrame, 0),
				new Point.Double(0, 1), true);

		if (this.size == null) {
			this.size = new Dimension(getAnimation().getHeight(),
					getAnimation().getWidth());
		}

		this.setSize(this.size);
	}

	@Override
	public void next() {
		if (this.dead) {
			this.engine.getMap().removeEntity(this);
			return;
		}
	}

}
