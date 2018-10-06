package de.schuette.cobra2DSandbox.texture;

import java.awt.image.RGBImageFilter;

import de.schuette.cobra2D.entity.EntityInitializeException;
import de.schuette.cobra2D.entity.editing.Editable;
import de.schuette.cobra2D.entity.editing.EditableProperty;
import de.schuette.cobra2D.rendering.RenderToolkit;
import de.schuette.cobra2D.system.Cobra2DEngine;

/**
 * This class takes advantage of the layered entity and adds the ability to set
 * an image modifier for the managed texture of the entity. This image modifier
 * is used to prepare the transparency and other filtering mechanisms.
 * 
 * @author Chris
 * 
 */
@Editable
public class ImageModifierEntity extends LayeredEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EditableProperty(modifierClass = "de.schuette.cobra2DSandbox.texture.editing.TransparencyEditor")
	protected RGBImageFilter filter;

	@Override
	public void initialize(final Cobra2DEngine engine)
			throws EntityInitializeException {
		super.initialize(engine);

		updateImage();

	}

	public RGBImageFilter getFilter() {
		return this.filter;
	}

	public void setFilter(final RGBImageFilter filter) {
		resetFilter();

		if (filter != null) {
			this.filter = filter;
			updateImage();
		}
	}

	public void resetFilter() {
		this.changeTexture(this.textureKey);
	}

	protected void updateImage() {
		// This can only be done successfully if there is an existing filter on
		// an existing texture within an initialized entity.
		if (filter != null && this.textureKey != null && engine != null) {
			this.changeTexture(this.textureKey);
			this.setImage(RenderToolkit.convertSpriteToTransparentSprite(
					this.getImage(), filter));
		}
	}

}
