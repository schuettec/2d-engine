package de.schuette.cobra2D.ressource;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.RGBImageFilter;
import java.awt.image.VolatileImage;

import de.schuette.cobra2D.rendering.RenderToolkit;

public class Animation {

	private final int width, height;
	private final int cols, rows;

	private transient VolatileImage image;
	private Rectangle rect;

	private int currentIndex = 0;

	protected RGBImageFilter filter;

	public Animation(final VolatileImage image, final int width,
			final int height) {
		this.width = width;
		this.height = height;

		this.image = image;

		this.rect = new Rectangle(new Dimension(width, height));

		this.cols = (int) Math.round((image.getWidth() / (double) width));
		this.rows = (int) Math.round((image.getHeight() / (double) height));

	}

	private void setIndex(final int index) throws IllegalArgumentException {

		if (index < 0 || index >= (this.getPictureCount())) {
			throw new IllegalArgumentException(
					"Picture-Index out of bounds. \n Min-Index: 0 \n Max-Index: "
							+ (this.getPictureCount() - 1) + "\n"
							+ "Requested Index: " + index);
		}

		this.rect = this.calculateRect(index);
		this.currentIndex = index;
	}

	protected Rectangle calculateRect(final int index) {
		final int x = (index % this.cols) * this.width;
		final int y = (index / this.cols) * this.height;

		return new Rectangle(x, y, this.width, this.height);
	}

	public int getIndex() {
		return this.currentIndex;
	}

	public int getPictureCount() {
		return (this.cols * this.rows);
	}

	/**
	 * Returns the current image drawn on a new created backbuffer image.
	 * 
	 * @param index
	 *            The index of the requested frame of the animation.
	 * @return Returns a new VolatileImage with the requested frame.
	 */
	public VolatileImage getImage(final int index) {
		this.setIndex(index);

		final VolatileImage tmpImage = RenderToolkit.createVolatileImage(
				this.width, this.height);
		final Graphics2D g = (Graphics2D) tmpImage.getGraphics();
		g.drawImage(this.image, 0, 0, this.width, this.height, this.rect.x,
				this.rect.y, this.rect.x + this.width, this.rect.y
						+ this.height, null);

		if (this.filter != null) {
			return RenderToolkit.convertSpriteToTransparentSprite(tmpImage,
					this.filter);
		}

		return tmpImage;
	}

	public RGBImageFilter getFilter() {
		return this.filter;
	}

	public void setFilter(final RGBImageFilter filter) {
		this.filter = filter;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getCols() {
		return this.cols;
	}

	public int getRows() {
		return this.rows;
	}

	public Rectangle getRect() {
		return this.rect;
	}

	public int getCurrentIndex() {
		return this.currentIndex;
	}

}
