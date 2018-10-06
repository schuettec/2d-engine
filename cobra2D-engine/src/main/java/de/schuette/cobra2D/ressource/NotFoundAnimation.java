package de.schuette.cobra2D.ressource;

import java.awt.Rectangle;
import java.awt.image.RGBImageFilter;
import java.awt.image.VolatileImage;

public class NotFoundAnimation extends Animation {

	private Rectangle rectangle;

	public NotFoundAnimation() {
		super(NoTexture.getNoTextureImage(), NoTexture.getNoTextureWidth(),
				NoTexture.getNoTextureHeight());
		this.rectangle = new Rectangle(0, 0, NoTexture.getNoTextureWidth(),
				NoTexture.getNoTextureHeight());

	}

	protected Rectangle calculateRect(final int index) {

		return rectangle;
	}

	public int getIndex() {
		return 0;
	}

	public int getPictureCount() {
		return 100;
	}

	/**
	 * Returns the current image drawn on a new created backbuffer image.
	 * 
	 * @param index
	 *            The index of the requested frame of the animation.
	 * @return Returns a new VolatileImage with the requested frame.
	 */
	public VolatileImage getImage(final int index) {
		return NoTexture.getNoTextureImage();
	}

	public RGBImageFilter getFilter() {
		return null;
	}

	public void setFilter(final RGBImageFilter filter) {
	}

	public int getWidth() {
		return NoTexture.getNoTextureWidth();
	}

	public int getHeight() {
		return NoTexture.getNoTextureHeight();
	}

	public int getCols() {
		return 1;
	}

	public int getRows() {
		return 1;
	}

	public Rectangle getRect() {
		return this.rectangle;
	}

	public int getCurrentIndex() {
		return 0;
	}
}
