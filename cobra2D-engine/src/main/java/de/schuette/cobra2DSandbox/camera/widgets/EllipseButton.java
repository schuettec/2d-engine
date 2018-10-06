package de.schuette.cobra2DSandbox.camera.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.io.Serializable;

import de.schuette.cobra2D.math.Math2D;

public class EllipseButton implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Point position;
	protected int maxRadius = 70;
	protected int minRadius = 40;
	protected int maxRings = 4;

	protected transient int[] radius;
	protected transient int[] angle;
	protected transient int[] angleSpeed;
	protected transient int[] range;

	public EllipseButton(final Point position) {
		this.position = position;
		this.createButton();

	}

	protected void createButton() {
		this.radius = new int[this.maxRings];
		this.angle = new int[this.maxRings];
		this.angleSpeed = new int[this.maxRings];
		this.range = new int[this.maxRings];

		for (int i = 0; i < this.maxRings; i++) {
			this.radius[i] = Math2D.random(this.minRadius, this.maxRadius);
			this.angle[i] = Math2D.random(0, 360);
			this.angleSpeed[i] = Math2D.random(1, 3);
			this.range[i] = Math2D.random(90, 180);
		}

	}

	public void drawButton(final Graphics2D g) {
		if (this.radius == null || this.angle == null
				|| this.angleSpeed == null || this.range == null) {
			this.createButton();
		}

		for (int i = 0; i < this.maxRings; i++) {
			this.angle[i] = (this.angle[i] + this.angleSpeed[i]) % 360;

			final Point renderPoint = Math2D.getCircle(this.position,
					this.radius[i], 225);
			// g.setColor(Color.RED);
			// g.fillRect(renderPoint.x, renderPoint.y, 5, 5);

			final Point renderEndPoint = Math2D.getCircle(this.position,
					this.radius[i], 45);
			// g.setColor(Color.GREEN);
			// g.fillRect(renderEndPoint.x, renderEndPoint.y, 5, 5);

			g.setColor(Color.CYAN);
			final Dimension rect = new Dimension(renderEndPoint.x
					- renderPoint.x, renderEndPoint.y - renderPoint.y);

			final Stroke oldStroke = g.getStroke();
			g.setStroke(new BasicStroke(2));
			g.drawArc(renderPoint.x, renderPoint.y, rect.width, rect.height,
					this.angle[i], this.range[i]);
			g.setStroke(oldStroke);

			// Button surface
			final Point renderPointButton = Math2D.getCircle(this.position,
					this.minRadius, 225);
			final Point renderEndPointButton = Math2D.getCircle(this.position,
					this.minRadius, 45);
			final Dimension rectButton = new Dimension(renderEndPointButton.x
					- renderPointButton.x, renderEndPointButton.y
					- renderPointButton.y);

			g.fillOval(renderPointButton.x, renderPointButton.y,
					rectButton.width, rectButton.height);
		}
	}

	protected static RadialGradientPaint getBackgroundPaint(
			final Dimension size, final Color transColor) {
		final Point2D center = new Point2D.Float(size.width / 2.0f,
				size.height / 2.0f);
		final float radius = size.width;
		final float[] dist = { 0.0f, 0.4f, 0.48f };
		final Color[] colors = { transColor, Color.CYAN, transColor };
		final RadialGradientPaint p = new RadialGradientPaint(center, radius,
				dist, colors);
		return p;
	}
}
