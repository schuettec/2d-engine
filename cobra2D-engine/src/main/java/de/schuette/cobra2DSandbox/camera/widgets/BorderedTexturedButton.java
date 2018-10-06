package de.schuette.cobra2DSandbox.camera.widgets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import de.schuette.cobra2D.system.Cobra2DEngine;

public class BorderedTexturedButton extends TexturedButton {

	public Object value = null;

	public BorderedTexturedButton(final Cobra2DEngine engine,
			final String imageAddressActive,
			final String imageAddressDeactivated) {
		super(engine, imageAddressActive, imageAddressDeactivated, 0, 0);
	}

	public BorderedTexturedButton(final Cobra2DEngine engine,
			final String imageAddressActive,
			final String imageAddressDeactivated, final int fixedWidth,
			final int fixedHeight) {
		super(engine, imageAddressActive, imageAddressDeactivated, fixedWidth,
				fixedHeight);
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public void setValue(final Object value) {
		this.value = value;
	}

	@Override
	public BufferedImage render() {
		BufferedImage preImage;

		if (this.active) {
			if (this.engine.getImageMemory().getImage(this.imageAddressActive) == null) {

			}

			preImage = this.engine.getImageMemory()
					.getImage(this.imageAddressActive).getSnapshot();

		} else {
			preImage = this.engine.getImageMemory()
					.getImage(this.imageAddressDeactivated).getSnapshot();
		}

		if (this.fixedWidth > 0 && this.fixedHeight > 0) {
			final BufferedImage image = new BufferedImage(this.fixedWidth,
					this.fixedHeight, BufferedImage.TYPE_INT_RGB);
			final Graphics2D g = (Graphics2D) image.getGraphics();
			g.drawImage(preImage, 0, 0, this.fixedWidth, this.fixedHeight, 0,
					0, preImage.getWidth(), preImage.getHeight(), null);
			g.dispose();
			this.drawBorder(image);
			return image;
		}

		if (this.fixedWidth > 0) {
			final BufferedImage image = new BufferedImage(this.fixedWidth,
					preImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			final Graphics2D g = (Graphics2D) image.getGraphics();
			g.drawImage(preImage, 0, 0, this.fixedWidth, preImage.getHeight(),
					0, 0, preImage.getWidth(), preImage.getHeight(), null);
			g.dispose();
			this.drawBorder(image);
			return image;
		}
		if (this.fixedHeight > 0) {
			final BufferedImage image = new BufferedImage(preImage.getWidth(),
					this.fixedHeight, BufferedImage.TYPE_INT_RGB);
			final Graphics2D g = (Graphics2D) image.getGraphics();
			g.drawImage(preImage, 0, 0, preImage.getWidth(), this.fixedHeight,
					0, 0, preImage.getWidth(), preImage.getHeight(), null);
			g.dispose();
			this.drawBorder(image);

			return image;
		}

		this.drawBorder(preImage);

		return preImage;
	}

	protected void drawBorder(final BufferedImage image) {
		final Graphics2D g = (Graphics2D) image.getGraphics();
		final Color brighter = new Color(128, 0, 0);
		final Color darker = new Color(45, 0, 6);

		if (this.active) {
			g.setColor(brighter);
		} else {
			g.setColor(darker);
		}
		g.drawRect(0, 0, image.getWidth() - 1, image.getHeight() - 1);
		g.dispose();
	}

}