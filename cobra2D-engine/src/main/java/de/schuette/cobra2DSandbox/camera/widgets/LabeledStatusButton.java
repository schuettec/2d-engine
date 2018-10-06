package de.schuette.cobra2DSandbox.camera.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import de.schuette.cobra2D.math.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import de.schuette.cobra2D.system.Cobra2DEngine;

public class LabeledStatusButton implements Button {

	protected static final int ADD_WIDTH = 20;
	protected static final int ADD_HEIGHT = 50;

	List<ActionListener> listeners = new ArrayList<ActionListener>();

	protected BufferedImage activeImage;
	protected BufferedImage deactiveImage;

	protected BufferedImage checkedActiveImage;
	protected BufferedImage checkedImage;

	protected Font font;
	protected String text = "No Caption";
	protected Color foreColor;
	protected Color backColor;

	protected Color checkedForeColor;
	protected Color checkedBackColor;

	protected boolean active = false;
	protected int fixedWidth = 0;
	protected int fixedHeight = 0;

	protected boolean checked = false;

	public Object value = null;

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public void setValue(final Object value) {
		this.value = value;
	}

	public boolean isChecked() {
		return this.checked;
	}

	public void setChecked(final boolean checked) {
		this.checked = checked;
	}

	private Point position = new Point(0, 0); // Set by the ButtonController to

	private final Cobra2DEngine engine;

	// verify the buttons position
	// for mouse actions

	public LabeledStatusButton(final Cobra2DEngine engine, final String text) {
		this(engine, text, 0, 0);
	}

	public LabeledStatusButton(final Cobra2DEngine engine, final String text,
			final int fixedWidth, final int fixedHeight) {
		this(engine, text, fixedWidth, fixedHeight, engine.getRenderer()
				.getFont(), new Color(128, 0, 0), new Color(45, 0, 6),
				new Color(40, 180, 37), new Color(15, 77, 33));
	}

	public LabeledStatusButton(final Cobra2DEngine engine, String text,
			final int fixedWidth, final int fixedHeight, final Font font,
			final Color foreColor, final Color backColor,
			final Color checkedForeColor, final Color checkedBackColor) {
		this.engine = engine;
		this.text = text;
		this.font = font;
		this.foreColor = foreColor;
		this.backColor = backColor;
		this.checkedForeColor = checkedForeColor;
		this.checkedBackColor = checkedBackColor;

		this.fixedWidth = fixedWidth;
		this.fixedHeight = fixedHeight;

		final FontMetrics metrics = engine.getRenderer().getFontMetrics(font);
		int textWidth = metrics
				.charsWidth(text.toCharArray(), 0, text.length());
		final int textHeight = metrics.getHeight();

		if (fixedWidth > 0) {
			// boolean shorter = false;
			// Textstring resizing
			while (textWidth > fixedWidth) {
				text = text.substring(0, text.length() - 6);
				text += "...";
				textWidth = metrics.charsWidth(text.toCharArray(), 0,
						text.length());
			}

		}

		this.activeImage = new BufferedImage(this.getSize().width,
				this.getSize().height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) this.activeImage.getGraphics();
		g.setColor(backColor.brighter());
		g.fillRect(0, 0, this.getSize().width, this.getSize().height);
		g.setColor(foreColor);
		g.drawRect(0, 0, this.getSize().width - 1, this.getSize().height - 1);
		g.drawString(text, (int) ((this.getSize().width - textWidth) / 2.0),
				(int) (((this.getSize().height) / 2.0) + (textHeight / 4.0)));
		g.dispose();

		this.deactiveImage = new BufferedImage(this.getSize().width,
				this.getSize().height, BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D) this.deactiveImage.getGraphics();
		g.setColor(backColor);
		g.fillRect(0, 0, this.getSize().width, this.getSize().height);
		g.setColor(foreColor);
		g.drawRect(0, 0, this.getSize().width - 1, this.getSize().height - 1);
		g.drawString(text, (int) ((this.getSize().width - textWidth) / 2.0),
				(int) (((this.getSize().height) / 2.0) + (textHeight / 4.0)));
		g.dispose();

		this.checkedImage = new BufferedImage(this.getSize().width,
				this.getSize().height, BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D) this.checkedImage.getGraphics();
		g.setColor(checkedBackColor);
		g.fillRect(0, 0, this.getSize().width, this.getSize().height);
		g.setColor(checkedForeColor);
		g.drawRect(0, 0, this.getSize().width - 1, this.getSize().height - 1);
		g.drawString(text, (int) ((this.getSize().width - textWidth) / 2.0),
				(int) (((this.getSize().height) / 2.0) + (textHeight / 4.0)));
		g.dispose();

		this.checkedActiveImage = new BufferedImage(this.getSize().width,
				this.getSize().height, BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D) this.checkedActiveImage.getGraphics();
		g.setColor(backColor);
		g.fillRect(0, 0, this.getSize().width, this.getSize().height);
		g.setColor(checkedForeColor);
		g.drawRect(0, 0, this.getSize().width - 1, this.getSize().height - 1);
		g.drawString(text, (int) ((this.getSize().width - textWidth) / 2.0),
				(int) (((this.getSize().height) / 2.0) + (textHeight / 4.0)));
		g.dispose();
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

		final FontMetrics metrics = this.engine.getRenderer().getFontMetrics(
				this.font);
		int width, height;

		width = metrics.charsWidth(this.text.toCharArray(), 0,
				this.text.length())
				+ LabeledStatusButton.ADD_WIDTH;
		height = LabeledStatusButton.ADD_HEIGHT;

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
		if (this.active) {
			if (this.checked) {
				return this.checkedActiveImage;
			} else {
				return this.activeImage;
			}

		} else {

			if (this.checked) {
				return this.checkedImage;
			} else {
				return this.deactiveImage;
			}
		}
	}

	@Override
	public void performAction(final ActionEvent event) {
		this.checked = !this.checked;
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

}
