package de.schuette.cobra2DSandbox.camera.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import de.schuette.cobra2D.math.Point;
import java.awt.image.VolatileImage;
import java.io.Serializable;

import de.schuette.cobra2D.math.Math2D;
import de.schuette.cobra2D.rendering.GradientTransparencyFilter;
import de.schuette.cobra2D.rendering.HueBrightnessTransparencyFilter;
import de.schuette.cobra2D.rendering.RenderToolkit;

public class StatusMeter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Point position;
	private Dimension size;

	protected transient VolatileImage backgroundBuffer;
	protected transient VolatileImage foregroundBufferPlay;
	protected transient VolatileImage foregroundBufferPause;

	public StatusMeter(final Dimension size, final Point position) {
		this.size = size;
		this.position = position;
	}

	public void drawMeter(final Graphics2D graphics, final boolean runningStatus) {
		// Render background
		if (this.backgroundBuffer == null) {
			this.createBackground();
		}
		if (this.foregroundBufferPlay == null
				|| this.foregroundBufferPause == null) {
			this.createForeground();
		}

		RenderToolkit.renderTo(this.position, graphics, this.backgroundBuffer);

		if (runningStatus == true) {
			RenderToolkit.renderTo(2, position, graphics,
					this.foregroundBufferPlay);
			// RenderToolkit.renderTo(graphics, position, foregroundBufferPlay);

		} else {
			RenderToolkit.renderTo(2, position, graphics,
					this.foregroundBufferPause);
			// RenderToolkit.renderTo(graphics, position,
			// foregroundBufferPause);
		}

	}

	private void createForeground() {
		{
			// Render status
			// Prepare the transparent background
			this.foregroundBufferPlay = RenderToolkit.createVolatileImage(
					this.size.width, this.size.height);
			final Graphics2D g = (Graphics2D) this.foregroundBufferPlay
					.getGraphics();

			// Render triangle
			final int x1 = Math2D.saveRound(this.size.width / 3.0) + 5;
			final int x2 = x1;
			final int x3 = this.size.width
					- Math2D.saveRound(this.size.width / 3.0) + 5;
			final int y1 = Math2D.saveRound(this.size.height / 4.0);
			final int y2 = this.size.height
					- Math2D.saveRound(this.size.width / 3.0) + 5;
			final int y3 = Math2D.saveRound((y1 + y2) / 2.0);

			g.setColor(Color.CYAN);
			g.fillPolygon(new int[] { x1, x2, x3 }, new int[] { y1, y2, y3 }, 3);
			g.setColor(Color.CYAN.brighter().brighter().brighter());
			g.setStroke(new BasicStroke(2));
			g.drawPolygon(new int[] { x1, x2, x3 }, new int[] { y1, y2, y3 }, 3);
			g.dispose();

			VolatileImage blurred = RenderToolkit.createVolatileImage(
					this.size.width, this.size.height);
			Graphics2D blurredGraph = (Graphics2D) blurred.getGraphics();

			RenderToolkit.renderTo(new Point(0, 0), blurredGraph,
					this.foregroundBufferPlay);
			blurredGraph.dispose();
			this.foregroundBufferPlay = blurred;
			final GradientTransparencyFilter filter = new GradientTransparencyFilter(
					Color.cyan, Color.WHITE);
			this.foregroundBufferPlay = RenderToolkit
					.convertSpriteToTransparentSprite(blurred, filter);
		}
		{
			this.foregroundBufferPause = RenderToolkit.createVolatileImage(
					this.size.width, this.size.height);
			final Graphics2D g = (Graphics2D) this.foregroundBufferPause
					.getGraphics();

			// Render pause symbol
			final int width = Math2D.saveRound(this.size.width / 8.0);

			final int x1 = Math2D.saveRound(this.size.width / 2.5) - width + 3;
			final int y = Math2D.saveRound(this.size.width / 4.0);
			final int height = Math2D.saveRound(this.size.width / 2.0);
			final int x2 = Math2D.saveRound(this.size.width / 2.5) + width + 3;

			g.setColor(Color.CYAN);
			g.fillRect(x1, y, width, height);

			g.setColor(Color.CYAN.brighter().brighter().brighter());
			g.setStroke(new BasicStroke(2));
			g.drawRect(x1, y, width, height);

			g.setColor(Color.CYAN);
			g.fillRect(x2, y, width, height);

			g.setColor(Color.CYAN.brighter().brighter().brighter());
			g.setStroke(new BasicStroke(2));
			g.drawRect(x2, y, width, height);
			g.dispose();

			VolatileImage blurred = RenderToolkit.createVolatileImage(
					this.size.width, this.size.height);
			Graphics2D blurredGraph = (Graphics2D) blurred.getGraphics();

			RenderToolkit.renderTo(2, new Point(0, 0), blurredGraph,
					this.foregroundBufferPause);
			blurredGraph.dispose();
			this.foregroundBufferPause = blurred;
			final GradientTransparencyFilter filter = new GradientTransparencyFilter(
					Color.cyan, Color.WHITE);
			this.foregroundBufferPause = RenderToolkit
					.convertSpriteToTransparentSprite(blurred, filter);
		}
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

	public Point getPosition() {
		return this.position;
	}

	public void setPosition(final Point position) {
		this.position = position;
	}

	public Dimension getSize() {
		return this.size;
	}

	public void setSize(final Dimension size) {
		this.size = size;
	}
}
