package de.schuette.cobra2D.entity;

import java.awt.Point;
import java.io.Serializable;

import de.schuette.cobra2D.math.Math2D;

public class EntityPoint implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Point currentPosition;

	private final Entity entity;

	private double radius = 0;

	private double degrees = 0.0;

	public EntityPoint(final double degrees, final double radius,
			final Entity entity) {
		this.entity = entity;
		setCurrentPosition(degrees, radius);
	}

	public void setCurrentPosition(double degrees, double radius) {
		this.degrees = degrees;
		this.radius = radius;

		final Point mitte = getReferencePoint();
		this.currentPosition = Math2D.getCircle(mitte, radius, degrees);
	}

	public void rotate(final double degrees) {
		final Point mitte = getReferencePoint();
		this.currentPosition = Math2D.getCircle(mitte, this.radius, degrees
				- this.degrees);
	}

	public Point getReferencePoint() {
		final Point mitte = new Point(entity.getPosition().x
				+ (entity.getSize().width / 2), entity.getPosition().y
				+ (entity.getSize().height / 2));
		return mitte;
	}

	public void setCurrentPosition(final Point currentPosition) {
		this.degrees = entity.getDegrees()
				- Math2D.getAngle(getReferencePoint(), currentPosition);
		this.radius = Math2D
				.getEntfernung(getReferencePoint(), currentPosition);
		setCurrentPosition(degrees, radius);
	}

	public double getDegrees() {
		return this.degrees;
	}

	public double getRadius() {
		return this.radius;

	}

	public Point getCurrentPosition() {
		return this.currentPosition;
	}

	public Entity getEntity() {
		return entity;
	}
}
