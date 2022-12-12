package de.schuette.cobra2D.math;

/**
 * This class describes a geometric line in a 2D coordinate system. It is
 * basically a representation of the formula <code>g(x) = m * x + b</code>
 * 
 * @author Chris
 *
 */
public class Line implements Shape, Cloneable {

	private Point x1, x2;
	private boolean parallelY = false;

	public Line(final Point x1, final Point x2) {
		this.setX1(new Point(x1.x, x1.y));
		this.setX2(new Point(x2.x, x2.y));
	}

	public Polygon toPolygon() {
		return new Polygon(new EntityPoint(x1), new EntityPoint(x2));
	}

	/**
	 * Checks if this line intersects the specified {@link Line}.
	 * 
	 * @param l
	 *            The line.
	 * @return Returns the intersection represented by {@link Point} if this line
	 *         intersects the specified line. Otherwise <code>null</code> is
	 *         returned.
	 */
	public Point intersects(final Line l) {

		// 1. Fall | |
		if (this.parallelY && l.isParallelY()) {
			if (l.isDefined(this.getX1())) {
				return new Point(this.getX1().x, this.getX1().y);
			}
			if (l.isDefined(this.getX2())) {
				return new Point(this.getX2().x, this.getX2().y);
			}

			if (this.isDefined(l.getX1())) {
				return new Point(l.getX1().x, l.getX1().y);
			}
			if (this.isDefined(l.getX2())) {
				return new Point(l.getX2().x, l.getX2().y);
			}

			return null;
		}

		// 2. Fall | --
		if (this.parallelY) {
			final Point schnittPunkt = new Point(this.x1.x, l.getY(this.x1.x));
			if (this.isDefined(schnittPunkt) && l.isDefined(schnittPunkt)) {
				return new Point(schnittPunkt.getX(), schnittPunkt.getY());
			} else {
				return null;
			}

		}

		// 3. Fall -- |
		if (l.isParallelY()) {
			final Point schnittPunkt = new Point(l.getX1().x, this.getY(l.getX1().x));
			if (l.isDefined(schnittPunkt) && this.isDefined(schnittPunkt)) {
				return new Point(schnittPunkt.getX(), schnittPunkt.getY());
			} else {
				return null;
			}
		}

		// 4. Fall === (gleiche Steigung; parallel zueinander)
		if (this.getM() == l.getM()) {
			if (this.isDefined(l.getX1())) {
				return new Point(l.getX1().x, l.getX1().y);
			}
			if (this.isDefined(l.getX2())) {
				return new Point(l.getX2().x, l.getX2().y);
			}
			if (l.isDefined(this.getX1())) {
				return new Point(this.getX1().x, this.getX1().y);
			}
			if (l.isDefined(this.getX2())) {
				return new Point(this.getX2().x, this.getX2().y);
			}

			/*
			 * if(getB() == l.getB()) { return new Point(l.getX1().getX(),
			 * l.getX1().getX()); }
			 */
			return null;
		}

		// 5. Fall /-- Quatsch

		double x = (l.getB() - this.getB()) / (this.getM() - l.getM());

		double y = this.getY(x);

		Point schnittPunkt = new Point(x, y);
		if (this.isDefined(schnittPunkt) && l.isDefined(schnittPunkt)) {
			return new Point(schnittPunkt.getX(), schnittPunkt.getY());
		}

		x = (this.getB() - l.getB()) / (l.getM() - this.getM());

		y = this.getY(x);

		schnittPunkt = new Point(x, y);
		if (this.isDefined(schnittPunkt) && l.isDefined(schnittPunkt)) {
			return new Point(schnittPunkt.getX(), schnittPunkt.getY());
		}

		return null;
	}

	public boolean isDefined(final Point point) {
		if (this.parallelY) {
			if (point.x != this.getX1().x) {
				return false;
			}
			final double ymin = Math.min(this.getX1().y, this.getX2().y);
			final double ymax = Math.max(this.getX1().y, this.getX2().y);

			if (point.y >= ymin && point.y <= ymax) {
				return true;
			} else {
				return false;
			}
		}

		final double xmin = Math.min(this.getX1().x, this.getX2().x);
		final double xmax = Math.max(this.getX1().x, this.getX2().x);
		if (point.x >= xmin && point.x <= xmax) {
			// Beim folgenden Vergleich, kommt es vor, dass die zwei letzten
			// Nachkomma stellen nicht gleich sind. Das liegt an der Errechnung
			// der Werte für y int getY und der Errechnung des punktes:
			// if (getY(point.x)== point.y) {
			// Ergebnisse waren 148.57215836526177!=148.57215836526183
			// Korrekte Aussage, allerdings hatte hier zuvor eine Kollision
			// stattgefunden
			// Lösungsansatz: Auf ungenauen Integer runden und auf Gleichheit
			// prüfen
			if (Math2D.saveRound(this.getY(point.x)) == Math2D.saveRound(point.y)) {
				return true;
			}
		}

		return false;

	}

	public double getY(final double x) {
		if (this.parallelY) {
			return this.x1.y;
		}
		return (this.getM() * x + this.getB());
	}

	public double getB() {
		if (this.parallelY) {
			return 0;
		}
		return (this.x1.y - this.getM() * this.x1.x);
	}

	public double getM() {
		if (this.parallelY) {
			return Double.NaN;
		}

		if (this.x2.x > this.x1.x) {
			return (this.x2.y - this.x1.y) / (this.x2.x - this.x1.x);
		}
		return (this.x1.y - this.x2.y) / (this.x1.x - this.x2.x);

	}

	public Point getX1() {
		return this.x1;
	}

	public Point getX2() {
		return this.x2;
	}

	public Point getStartPoint() {
		return new Point(this.x1.getX(), this.x1.getY());
	}

	public Point getEndPoint() {
		return new Point(this.x2.getX(), this.x2.getY());
	}

	public boolean isParallelY() {
		return this.parallelY;
	}

	public void setX1(final Point x1) {
		this.x1 = x1;

		if (this.x2 == null) {
			return;
		}
		if (x1.x == this.x2.x) {
			this.parallelY = true;
		}
	}

	public void setX2(final Point x2) {
		this.x2 = x2;

		if (this.x1.x == x2.x) {
			this.parallelY = true;
		}
	}

	@Override
	public String toString() {
		if (this.parallelY) {
			return "Parallel zur Y-Achse";
		}

		return "g(x) = " + this.getM() + " * x + " + this.getB();
	}

	@Override
	public Line clone() {
		return new Line(x1.clone(), x2.clone());
	}

	@Override
	public Line rotate(double degrees) {
		Point center = Math2D.getMittelpunkt(x1, x2);
		Point newX1 = Math2D.getCircle(center, Math2D.getEntfernung(x1, center), degrees);
		Point newX2 = Math2D.getCircle(center, Math2D.getEntfernung(x2, center), degrees);
		x1.setLocation(newX1);
		x2.setLocation(newX2);
		return this;
	}

	@Override
	public Line translate(Point translation) {
		x1.translate(translation);
		x2.translate(translation);
		return this;
	}

	@Override
	public Line scale(double scaleFactor) {
		Point center = Math2D.getMittelpunkt(x1, x2);
		double angle = Math2D.getAngle(center, x1);
		Point newX1 = Math2D.getCircle(center, Math2D.getEntfernung(x1, center), angle);
		Point newX2 = Math2D.getCircle(center, Math2D.getEntfernung(x2, center), angle + 180);
		x1.setLocation(newX1);
		x2.setLocation(newX2);
		return this;
	}
}
