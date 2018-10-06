package de.schuette.cobra2DSandbox.camera.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import de.schuette.cobra2D.controller.ControllerListener;
import de.schuette.cobra2D.system.Cobra2DEngine;

public class TextFieldButton implements Button, ControllerListener {

	protected List<ActionListener> listeners = new ArrayList<ActionListener>();

	protected boolean active = false;

	protected String title;
	protected String text;
	protected int maxLength = 30;
	protected int fixedWidth = 0;

	protected Color brighter;
	protected Color darker;

	protected Point position = new Point(0, 0); // Set by the ButtonController

	protected Cobra2DEngine engine;

	// to verify the buttons
	// position for mouse actions

	public TextFieldButton(final Cobra2DEngine engine, final String title,
			final String startText, final int maxLength) {
		this(engine, title, startText, maxLength, 0);

	}

	public TextFieldButton(final Cobra2DEngine engine, final String title,
			final String startText, final int maxLength, final int fixedWidth) {
		this(engine, title, startText, maxLength, fixedWidth, new Color(128, 0,
				0), new Color(45, 0, 6));

	}

	public TextFieldButton(final Cobra2DEngine engine, final String title,
			final String startText, final int maxLength, final int fixedWidth,
			final Color brighter, final Color darker) {
		this.engine = engine;
		this.title = title;
		this.text = startText;
		this.maxLength = maxLength;
		this.fixedWidth = fixedWidth;
		this.brighter = brighter;
		this.darker = darker;

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
		this.engine.getController().addKeyListener(this);

	}

	@Override
	public void deactivate() {
		this.active = false;
		this.engine.getController().removeKeyListener(this);

	}

	@Override
	public Dimension getSize() {
		final Font font = this.engine.getRenderer().getFont();
		final FontMetrics metrics = this.engine.getRenderer().getFontMetrics(
				font);

		int textWidth = metrics.charsWidth(this.title.toCharArray(), 0,
				this.title.length())
				+ metrics.getMaxAdvance()
				* (this.maxLength / 3) + 20;
		if (this.fixedWidth > 0) {
			textWidth = this.fixedWidth;
		}

		final int textHeight = metrics.getHeight();

		return new Dimension(textWidth, textHeight + 10);

	}

	@Override
	public boolean isActive() {
		return this.active;
	}

	@Override
	public BufferedImage render() {
		final BufferedImage image = new BufferedImage(this.getSize().width,
				this.getSize().height, BufferedImage.TYPE_INT_RGB);

		final Graphics2D g = (Graphics2D) image.getGraphics();

		final int titleWidth = g.getFontMetrics().charsWidth(
				this.title.toCharArray(), 0, this.title.length());
		final int titleHeight = g.getFontMetrics().getHeight();
		int textWidth = g.getFontMetrics().charsWidth(this.text.toCharArray(),
				0, this.text.length());

		if (this.active) {
			g.setColor(this.darker.brighter());
		} else {
			g.setColor(this.darker);
		}
		g.fillRect(0, 0, this.getSize().width - 1, this.getSize().height - 1);
		g.setColor(this.brighter);
		g.drawRect(0, 0, this.getSize().width - 1, this.getSize().height - 1);
		g.setColor(this.brighter);
		g.drawString(this.title, 0, titleHeight);

		// RenderToolkit.renderTo(g, new Point(titleWidth, 10), new Dimension(
		// getSize().width, getSize().height), underline);

		g.setColor(Color.GRAY);
		// g.setFont(new Font("Arial", Font.BOLD, 24));
		String toRender = this.text;
		while ((titleWidth + textWidth + 10) >= this.getSize().width) {
			if (toRender.length() <= 0) {
				break;
			}
			toRender = toRender.substring(1, toRender.length());
			textWidth = g.getFontMetrics().charsWidth(toRender.toCharArray(),
					0, toRender.length());
		}
		g.drawString(toRender, titleWidth, titleHeight);
		g.setColor(Color.RED);
		g.drawLine(titleWidth + textWidth, titleHeight, titleWidth + textWidth
				+ 10, titleHeight);

		g.dispose();
		return image;

	}

	@Override
	public void performAction(final ActionEvent event) {
		for (final ActionListener l : this.listeners) {
			l.actionPerformed(event);
		}
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	@Override
	public Object getValue() {
		return this.text;
	}

	@Override
	public void setValue(final Object value) {
		if (value instanceof String) {
			this.text = (String) value;
		}
	}

	public int getMaxLength() {
		return this.maxLength;
	}

	public void setMaxLength(final int maxLength) {
		this.maxLength = maxLength;
	}

	@Override
	public void keyPressed(final int e) {

		if (e == KeyEvent.VK_BACK_SPACE) {
			if (this.text == "" || this.text.length() == 0) {
				return;
			}
			this.text = this.text.substring(0, this.text.length() - 1);
			return;
		}

	}

	@Override
	public void keyChar(final char c) {
		this.text += c;

	}

	@Override
	public void keyReleased(final int e) {

	}

	@Override
	public void keyTyped(final int e) {

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

}
