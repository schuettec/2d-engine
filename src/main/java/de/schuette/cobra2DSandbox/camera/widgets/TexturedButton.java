package de.schuette.cobra2DSandbox.camera.widgets;

import java.awt.Dimension;
import de.schuette.cobra2D.math.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.List;

import de.schuette.cobra2D.system.Cobra2DEngine;

public class TexturedButton implements Button {

	protected List<ActionListener> listeners = new ArrayList<ActionListener>();

	protected boolean active = false;

	protected String imageAddressActive;
	protected String imageAddressDeactivated;

	protected Point position = new Point(0, 0); // Set by the ButtonController
	// to
	// verify the buttons position
	// for mouse actions

	protected int fixedWidth = 0, fixedHeight = 0;

	protected Cobra2DEngine engine;

	public TexturedButton(final Cobra2DEngine engine,
			final String imageAddressActive,
			final String imageAddressDeactivated) {
		this(engine, imageAddressActive, imageAddressDeactivated, 0, 0);
	}

	public TexturedButton(final Cobra2DEngine engine,
			final String imageAddressActive,
			final String imageAddressDeactivated, final int fixedWidth,
			final int fixedHeight) {
		this.engine = engine;
		this.imageAddressActive = imageAddressActive;
		this.imageAddressDeactivated = imageAddressDeactivated;
		this.fixedWidth = fixedWidth;
		this.fixedHeight = fixedHeight;
	}

	public Object value = null;

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public void setValue(final Object value) {
		this.value = value;
	}

	public void addActionListener(final ActionListener listener) {
		this.listeners.add(listener);
	}

	public void removeActionListener(final ActionListener listener) {
		this.listeners.remove(listener);
	}

	@Override
	public void activate() {
		this.active = true;

	}

	@Override
	public void deactivate() {
		this.active = false;

	}

	@Override
	public Dimension getSize() {
		int width, height;

		VolatileImage image;
		if (this.active) {
			image = this.engine.getImageMemory().getImage(
					this.imageAddressActive);
		} else {
			image = this.engine.getImageMemory().getImage(
					this.imageAddressDeactivated);
		}

		if (image == null) {
			return new Dimension(0, 0);
		}

		width = image.getWidth();
		height = image.getHeight();

		if (this.fixedWidth > 0) {
			width = this.fixedWidth;
		}
		if (this.fixedHeight > 0) {
			height = this.fixedHeight;
		}

		return new Dimension(width, height);

	}

	@Override
	public boolean isActive() {
		return this.active;
	}

	@Override
	public BufferedImage render() {
		BufferedImage preImage;

		if (this.active) {
			preImage = this.engine.getImageMemory()
					.getImage(this.imageAddressActive).getSnapshot();

		} else {
			preImage = this.engine.getImageMemory()
					.getImage(this.imageAddressDeactivated).getSnapshot();
		}

		if (this.fixedWidth > 0 && this.fixedHeight > 0) {
			final BufferedImage image = new BufferedImage(this.fixedWidth,
					this.fixedHeight, BufferedImage.TYPE_INT_RGB);
			image.getGraphics().drawImage(preImage, 0, 0, this.fixedWidth,
					this.fixedHeight, 0, 0, preImage.getWidth(),
					preImage.getHeight(), null);
			return image;
		}

		if (this.fixedWidth > 0) {
			final BufferedImage image = new BufferedImage(this.fixedWidth,
					preImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			image.getGraphics().drawImage(preImage, 0, 0, this.fixedWidth,
					preImage.getHeight(), 0, 0, preImage.getWidth(),
					preImage.getHeight(), null);
			return image;
		}
		if (this.fixedHeight > 0) {
			final BufferedImage image = new BufferedImage(preImage.getWidth(),
					this.fixedHeight, BufferedImage.TYPE_INT_RGB);
			image.getGraphics().drawImage(preImage, 0, 0, preImage.getWidth(),
					this.fixedHeight, 0, 0, preImage.getWidth(),
					preImage.getHeight(), null);
			return image;
		}

		return preImage;
	}

	@Override
	public void performAction(final ActionEvent event) {
		for (final ActionListener l : this.listeners) {
			l.actionPerformed(event);
		}
	}

	@Override
	public Point getPosition() {

		return this.position;
	}

	@Override
	public void setPosition(final Point position) {
		this.position = position;

	}

	public int getFixedWidth() {
		return this.fixedWidth;
	}

	public void setFixedWidth(final int fixedWidth) {
		this.fixedWidth = fixedWidth;
	}

	public int getFixedHeight() {
		return this.fixedHeight;
	}

	public void setFixedHeight(final int fixedHeight) {
		this.fixedHeight = fixedHeight;
	}

	public String getImageAddressActive() {
		return this.imageAddressActive;
	}

	public String getImageAddressDeactivated() {
		return this.imageAddressDeactivated;
	}

}
