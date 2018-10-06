package de.schuette.cobra2DSandbox.camera.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.awt.image.VolatileImage;
import java.io.Serializable;

import de.schuette.cobra2D.rendering.HueBrightnessTransparencyFilter;
import de.schuette.cobra2D.rendering.RenderToolkit;

public class Meter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Dimension size;
	protected String label;
	protected int min;
	protected int max;
	protected Point position;

	protected static final int backgroundHue = 360;
	protected static final int foregroundHue = 360;

	protected transient VolatileImage backgroundBuffer;
	protected transient VolatileImage foregroundBuffer;

	/**
	 * @param size
	 * @param label
	 * @param min
	 * @param max
	 * @param position
	 */
	public Meter(final Dimension size, final String label, final int min,
			final int max, final Point position) {
		super();
		this.size = size;
		this.label = label;
		this.min = min;
		this.max = max;
		this.position = position;
	}

	public void drawMeter(final Graphics2D graphics, final int value) {

		if (this.backgroundBuffer == null) {
			this.createBackground();
		}
		if (this.foregroundBuffer == null) {
			this.createForeground();
		}

		// Render background
		RenderToolkit.renderTo(this.position, graphics, this.backgroundBuffer);
		{
			// Prepare the gauge
			final Graphics2D g = (Graphics2D) this.foregroundBuffer
					.getGraphics();
			// Draw panel
			g.setColor(RenderToolkit.getColorFromHue(Meter.foregroundHue));
			g.fillRect(0, 0, this.size.width, this.size.height);
			g.setStroke(new BasicStroke(200, BasicStroke.CAP_ROUND,
					BasicStroke.CAP_ROUND));
			g.setPaint(Meter.getBorderPaint(this.size, Meter.foregroundHue));
			final int angle = (int) Math.round((360.0 / (this.max - this.min))
					* (value - this.min));
			g.fillArc(0, 0, this.size.width, this.size.height, 0, -angle);

			final HueBrightnessTransparencyFilter filter = new HueBrightnessTransparencyFilter(
					Meter.foregroundHue, 0.8f);

			this.foregroundBuffer = RenderToolkit
					.convertSpriteToTransparentSprite(this.foregroundBuffer,
							filter);
			// RenderToolkit.renderTo(graphics, position, foregroundBuffer);
			// RenderToolkit.renderGaussedImage(1f, 2, position, graphics,
			// foregroundBuffer);
			RenderToolkit.renderTo(this.position, graphics,
					this.foregroundBuffer);
		}

		{
			// Render on camera viewport
			final int stringWidth = graphics.getFontMetrics().stringWidth(
					this.label);
			final int stringHeight = graphics.getFontMetrics().getHeight();
			final int strX = (int) Math
					.round((this.position.x + (this.size.width / 2.0))
							- (stringWidth / 2.0));
			final int strY = (int) Math
					.round((this.position.y + (this.size.height / 2.0))
							- (stringHeight / 2.0));

			final Font oldFont = graphics.getFont();
			Font newFont = new Font("Courier", Font.BOLD, 12);
			graphics.setFont(newFont);
			graphics.setColor(Color.BLACK);
			graphics.drawString(this.label, strX - 1, strY - 1);
			graphics.drawString(this.label, strX + 1, strY - 1);

			newFont = new Font("Courier", Font.PLAIN, 12);
			graphics.setFont(newFont);
			graphics.setColor(Color.CYAN);
			graphics.drawString(this.label, strX, strY);
			graphics.setFont(oldFont);
		}
		{
			final String label = String.valueOf(value);
			final int stringWidth = graphics.getFontMetrics()
					.stringWidth(label);
			final int stringHeight = graphics.getFontMetrics().getHeight();
			final int strX = (int) Math
					.round((this.position.x + (this.size.width / 2.0))
							- (stringWidth / 2.0));
			final int strY = (int) Math
					.round((this.position.y + (this.size.height / 2.0))
							- (stringHeight / 2.0));

			graphics.setColor(Color.CYAN);
			graphics.drawString(label, strX, strY + stringHeight);
		}
	}

	private void createForeground() {
		// Prepare the transparent background
		this.foregroundBuffer = RenderToolkit.createVolatileImage(
				this.size.width, this.size.height);
		final Graphics2D g = (Graphics2D) this.foregroundBuffer.getGraphics();

		g.setPaint(Meter.getBackgroundPaint(this.size, Meter.foregroundHue));
		g.fillOval(0, 0, this.size.width - 2, this.size.height - 2);

		final HueBrightnessTransparencyFilter filter = new HueBrightnessTransparencyFilter(
				Meter.foregroundHue, 1f);
		this.foregroundBuffer = RenderToolkit.convertSpriteToTransparentSprite(
				this.foregroundBuffer, filter);
	}

	private void createBackground() {
		// Prepare the transparent background
		this.backgroundBuffer = RenderToolkit.createVolatileImage(
				this.size.width, this.size.height);
		final Graphics2D g = (Graphics2D) this.backgroundBuffer.getGraphics();

		g.setPaint(Meter.getBackgroundPaint(this.size, Meter.backgroundHue));
		g.fillOval(0, 0, this.size.width - 2, this.size.height - 2);

		final HueBrightnessTransparencyFilter filter = new HueBrightnessTransparencyFilter(
				Meter.backgroundHue, 1f);
		this.backgroundBuffer = RenderToolkit.convertSpriteToTransparentSprite(
				this.backgroundBuffer, filter);
	}

	protected static RadialGradientPaint getBackgroundPaint(
			final Dimension size, final int transparentHue) {
		final Point2D center = new Point2D.Float(size.width / 2.0f,
				size.height / 2.0f);
		final float radius = size.width;
		final float[] dist = { 0.0f, 0.4f, 0.48f };
		final Color BACKGROUND = RenderToolkit.getColorFromHue(transparentHue);
		final Color[] colors = { BACKGROUND, Color.CYAN, BACKGROUND };
		final RadialGradientPaint p = new RadialGradientPaint(center, radius,
				dist, colors);
		return p;
	}

	protected static RadialGradientPaint getBorderPaint(final Dimension size,
			final int transparentHue) {
		final Point2D center = new Point2D.Float(size.width / 2.0f,
				size.height / 2.0f);
		final float radius = size.width;
		final float[] dist = { 0.23f, 0.3f, 0.35f, 0.47f };
		final Color BLACK = RenderToolkit.getColorFromHue(Meter.backgroundHue);
		final Color[] colors = { BLACK, Color.CYAN, Color.CYAN, BLACK };
		final RadialGradientPaint p = new RadialGradientPaint(center, radius,
				dist, colors);
		return p;
	}

	public Dimension getSize() {
		return this.size;
	}

	public void setSize(final Dimension size) {
		this.size = size;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public int getMin() {
		return this.min;
	}

	public void setMin(final int min) {
		this.min = min;
	}

	public int getMax() {
		return this.max;
	}

	public void setMax(final int max) {
		this.max = max;
	}

	public Point getPosition() {
		return this.position;
	}

	public void setPosition(final Point position) {
		this.position = position;
	}

}
