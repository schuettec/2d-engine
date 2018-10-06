package de.schuette.cobra2D.math;

public class Point implements Shape, Cloneable {

	public Object userObject;

	public double x;
	public double y;

	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public Point(Point p) {
		this(p.x, p.y);
	}

	public int getRoundX() {
		return (int) Math.round(x);
	}

	public int getRoundY() {
		return (int) Math.round(y);
	}

	public void translate(double x, double y) {
		this.x += x;
		this.y += y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Sets the location of this {@link Point}.
	 * 
	 * @param x
	 *            The new x coordinate of this {@link Point}.
	 * @param y
	 *            The new y coordinate of this {@link Point}.
	 */
	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Sets the location of this {@link Point}.
	 * 
	 * @param p
	 *            The new location of this {@link Point}.
	 */
	public void setLocation(Point p) {
		this.x = p.x;
		this.y = p.y;
	}

	@Override
	public String toString() {
		return "[x" + x + ", " + y + "]";
	}

	public java.awt.Point getPoint() {
		return new java.awt.Point(getRoundX(), getRoundY());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	@Override
	public Point clone() {
		return new Point(x, y);
	}

	/**
	 * This implementation does nothing, because a {@link Point} cannot be rotated.
	 */
	@Override
	public Point rotate(double degrees) {
		return this;
	}

	@Override
	public Point translate(Point translation) {
		this.x += translation.x;
		this.y += translation.y;
		return this;
	}

	/**
	 * This implementation does nothing, because a {@link Point} cannot be scaled.
	 */
	@Override
	public Point scale(double scaleFactor) {
		return this;
	}

	public java.awt.Point toAwtPoint() {
		return new java.awt.Point(Math2D.saveRound(x), Math2D.saveRound(y));
	}

	public static Point ofAWT(java.awt.Point point) {
		return new Point(point.getX(), point.getY());
	}

}
