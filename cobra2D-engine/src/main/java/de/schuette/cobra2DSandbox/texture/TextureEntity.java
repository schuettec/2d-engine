package de.schuette.cobra2DSandbox.texture;

import java.awt.Dimension;
import java.awt.Graphics2D;
import de.schuette.cobra2D.math.Point;
import java.awt.image.VolatileImage;

import de.schuette.cobra2D.entity.EntityInitializeException;
import de.schuette.cobra2D.entity.editing.Editable;
import de.schuette.cobra2D.rendering.RenderToolkit;
import de.schuette.cobra2D.system.Cobra2DEngine;

/**
 * This entity takes advantage of the image modifyable entity, and adds the
 * ability of setting a target size. That means, if the size is adjusted, the
 * rendering will resize the texture and renders it in the given dimension.
 * 
 * @author Chris
 * 
 */
@Editable
public class TextureEntity extends ImageModifierEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void render(final Graphics2D graphics, final Point position) {
		this.renderTexture(getDegrees(), this.getImage(), graphics, position);
	}

	/**
	 * Renders the texture by resizing with the entity's size. This method is
	 * used to render a modified image with the resize function.
	 * 
	 * @param texture
	 *            The modified image to render resized.
	 * @param graphics
	 *            The graphics from renderer.
	 * @param position
	 *            The screen position given by the renderer.
	 */
	public void renderTexture(double degrees, final VolatileImage texture,
			final Graphics2D graphics, final Point position) {
		final Dimension imageSize = new Dimension(texture.getWidth(),
				texture.getHeight());

		if (this.size == null || imageSize.equals(this.size)) {
			RenderToolkit.renderTo(degrees, 0, position, graphics, texture);
		} else {
			RenderToolkit.renderTo(degrees, position, this.size, graphics,
					texture);

		}
	}

	@Override
	public void initialize(final Cobra2DEngine engine)
			throws EntityInitializeException {
		super.initialize(engine);
	}

}
