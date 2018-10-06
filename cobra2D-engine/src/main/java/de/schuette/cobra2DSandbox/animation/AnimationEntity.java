package de.schuette.cobra2DSandbox.animation;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.RGBImageFilter;
import java.awt.image.VolatileImage;

import org.apache.log4j.Logger;

import de.schuette.cobra2D.entity.EntityInitializeException;
import de.schuette.cobra2D.entity.editing.Editable;
import de.schuette.cobra2D.entity.editing.EditableProperty;
import de.schuette.cobra2D.entity.editing.NumberEditor;
import de.schuette.cobra2D.entity.editing.StringEditor;
import de.schuette.cobra2D.ressource.Animation;
import de.schuette.cobra2D.ressource.NoTexture;
import de.schuette.cobra2D.ressource.NotFoundAnimation;
import de.schuette.cobra2D.system.Cobra2DEngine;
import de.schuette.cobra2DSandbox.texture.LayeredEntity;

/**
 * @author Chris This entity defines a rendering behaviour to render animations.
 *         The rendering is
 */
@Editable
public class AnimationEntity extends LayeredEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(AnimationEntity.class);

	@EditableProperty(modifierClass = StringEditor.KEY)
	protected String animationKey;

	@EditableProperty(modifierClass = "de.schuette.cobra2DSandbox.texture.editing.TransparencyEditor")
	protected RGBImageFilter filter;

	@EditableProperty(modifierClass = NumberEditor.KEY)
	protected long frameDelay;

	protected int index;

	// This member is private to control access to the animation object
	protected transient Animation animation;
	protected transient long lastIndexUpdate = -1;
	protected transient boolean indexErrorLogged;
	protected transient boolean nullErrorLogged;

	@Override
	public void initialize(final Cobra2DEngine engine)
			throws EntityInitializeException {
		super.initialize(engine);

		// Reset error logging switches
		resetErrorMessageSwitches();

		// Reload animation and engine may be null if this entity was not
		// initialized
		reloadAnimation();

	}

	private void reloadAnimation() {
		if (animationKey != null && engine != null) {
			this.animation = engine.getAnimationMemory().getAnimation(
					this.animationKey);

			if (filter != null) {
				animation.setFilter(filter);
			}

			adjustSize();
		}
	}

	private void resetErrorMessageSwitches() {
		indexErrorLogged = false;
		nullErrorLogged = false;
	}

	@Override
	public void render(final Graphics2D graphics, final Point position) {
		this.image = getFrame(this.index);

		super.render(graphics, position);

		if (System.currentTimeMillis() - lastIndexUpdate >= frameDelay) {
			lastIndexUpdate = System.currentTimeMillis();
			this.index += 1;
		}

		if (this.index >= getAnimation().getPictureCount() - 1) {
			this.index = 0;
			return;
		}

	}

	private VolatileImage getFrame(int index) {

		if (this.animation == null) {
			if (!nullErrorLogged) {
				log.error("Animation frame returned null! Index was "
						+ this.index + "; picture was "
						+ animation.getPictureCount());
				nullErrorLogged = true;
			}
		}

		if (this.index < this.animation.getPictureCount()) {
			VolatileImage frame = animation.getImage(this.index);
			return frame;
		} else {
			if (!indexErrorLogged) {
				log.error("Animation frame index error! Index was "
						+ this.index + "; picture was "
						+ animation.getPictureCount());
				indexErrorLogged = true;
			}
		}

		return NoTexture.getNoTextureImage();
	}

	public Animation getAnimation() {
		if (this.animation == null) {
			return new NotFoundAnimation();
		} else {
			return this.animation;
		}
	}

	@Override
	public void adjustSize() {
		// Overriding this method to adjust size according to one frame of the
		// animation
		if (animation != null && isLinkSizeToTexture()) {
			this.setSize(new Dimension(animation.getWidth(), animation
					.getHeight()));
		}
	}

	public String getAnimationKey() {
		return animationKey;
	}

	public void setAnimationKey(String animationKey) {
		this.animationKey = animationKey;
	}

	public RGBImageFilter getFilter() {
		if (animation != null) {
			return animation.getFilter();
		} else {
			return this.filter;
		}
	}

	public void setFilter(RGBImageFilter filter) {
		if (animation != null) {
			animation.setFilter(filter);
		}
		this.filter = filter;
	}

	public long getFrameDelay() {
		return frameDelay;
	}

	public void setFrameDelay(long frameDelay) {
		this.frameDelay = frameDelay;
	}

}
