package de.schuette.cobra2D.math;

import java.awt.Point;
import java.awt.geom.Point2D;

public class Line {

	private Point2D.Double x1, x2;
	private boolean parallelY = false;

	public Line(final Point x1, final Point x2) {
		this.setX1(new Point2D.Double(x1.x, x1.y));
		this.setX2(new Point2D.Double(x2.x, x2.y));
	}

	/*
	 * public boolean schneidetLinie(Line l) { //1. Fall | | if(parallelY &&
	 * l.isParallelY()) return false;
	 * 
	 * 
	 * //2. Fall | -- if(parallelY) { Point2D.Double schnittPunkt = new
	 * Point2D.Double(x1.x, l.getY(x1.x)); if(isDefined(schnittPunkt) &&
	 * l.isDefined(schnittPunkt)) { return true; } else { return false; }
	 * 
	 * }
	 * 
	 * 
	 * //3. Fall -- | if(l.isParallelY()) { Point2D.Double schnittPunkt = new
	 * Point2D.Double(l.getX1().x, getY(l.getX1().x));
	 * if(isDefined(schnittPunkt) && l.isDefined(schnittPunkt)) { return true; }
	 * else { return false; } }
	 * 
	 * //4. Fall === (gleiche Steigung; parallel zueinander) if(getM() ==
	 * l.getM()) { if(getB() == l.getB()) return true; return false; }
	 * 
	 * 
	 * //5. Fall /-- double x = (l.getB() - getB()) / (getM() - l.getM());
	 * 
	 * double y = getY(x);
	 * 
	 * Point2D.Double schnittPunkt = new Point2D.Double(x, y);
	 * if(isDefined(schnittPunkt) && l.isDefined(schnittPunkt)) { return true; }
	 * 
	 * return false; }
	 */

	public Point schneidetLinie(final Line l) {

		// 1. Fall | |
		if (this.parallelY && l.isParallelY()) {
			if (l.isDefined(this.getX1())) {
				return new Point((int) this.getX1().x, (int) this.getX1().y);
			}
			if (l.isDefined(this.getX2())) {
				return new Point((int) this.getX2().x, (int) this.getX2().y);
			}

			if (this.isDefined(l.getX1())) {
				return new Point((int) l.getX1().x, (int) l.getX1().y);
			}
			if (this.isDefined(l.getX2())) {
				return new Point((int) l.getX2().x, (int) l.getX2().y);
			}

			return null;
		}

		// 2. Fall | --
		if (this.parallelY) {
			final Point2D.Double schnittPunkt = new Point2D.Double(this.x1.x,
					l.getY(this.x1.x));
			if (this.isDefined(schnittPunkt) && l.isDefined(schnittPunkt)) {
				return new Point((int) schnittPunkt.getX(),
						(int) schnittPunkt.getY());
			} else {
				return null;
			}

		}

		// 3. Fall -- |
		if (l.isParallelY()) {
			final Point2D.Double schnittPunkt = new Point2D.Double(l.getX1().x,
					this.getY(l.getX1().x));
			if (l.isDefined(schnittPunkt) && this.isDefined(schnittPunkt)) {
				return new Point((int) schnittPunkt.getX(),
						(int) schnittPunkt.getY());
			} else {
				return null;
			}
		}

		// 4. Fall === (gleiche Steigung; parallel zueinander)
		if (this.getM() == l.getM()) {
			if (this.isDefined(l.getX1())) {
				return new Point((int) l.getX1().x, (int) l.getX1().y);
			}
			if (this.isDefined(l.getX2())) {
				return new Point((int) l.getX2().x, (int) l.getX2().y);
			}
			if (l.isDefined(this.getX1())) {
				return new Point((int) this.getX1().x, (int) this.getX1().y);
			}
			if (l.isDefined(this.getX2())) {
				return new Point((int) this.getX2().x, (int) this.getX2().y);
			}

			/*
			 * if(getB() == l.getB()) { return new Point((int)l.getX1().getX(),
			 * (int)l.getX1().getX()); }
			 */
			return null;
		}

		// 5. Fall /-- Quatsch

		double x = (l.getB() - this.getB()) / (this.getM() - l.getM());

		double y = this.getY(x);

		Point2D.Double schnittPunkt = new Point2D.Double(x, y);
		if (this.isDefined(schnittPunkt) && l.isDefined(schnittPunkt)) {
			return new Point((int) schnittPunkt.getX(),
					(int) schnittPunkt.getY());
		}

		x = (this.getB() - l.getB()) / (l.getM() - this.getM());

		y = this.getY(x);

		schnittPunkt = new Point2D.Double(x, y);
		if (this.isDefined(schnittPunkt) && l.isDefined(schnittPunkt)) {
			return new Point((int) schnittPunkt.getX(),
					(int) schnittPunkt.getY());
		}

		return null;
	}

	public boolean isDefined(final Point2D.Double point) {
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
			// der Werte f�r y in getY und der Errechnung des punktes:
			// if (getY(point.x)== point.y) {
			// Ergebnisse waren 148.57215836526177!=148.57215836526183
			// Korrekte Aussage, allerdings hatte hier zuvor eine Kollision
			// stattgefunden
			// L�sungsansatz: Auf ungenauen Integer runden und auf Gleichheit
			// pr�fen
			if (((int) this.getY(point.x)) == ((int) point.y)) {
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

	public Point2D.Double getX1() {
		return this.x1;
	}

	public Point2D.Double getX2() {
		return this.x2;
	}

	public Point getStartPoint() {
		return new Point((int) Math.round(this.x1.getX()),
				(int) Math.round(this.x1.getY()));
	}

	public Point getEndPoint() {
		return new Point((int) Math.round(this.x2.getX()),
				(int) Math.round(this.x2.getY()));
	}

	public boolean isParallelY() {
		return this.parallelY;
	}

	public void setX1(final Point2D.Double x1) {
		this.x1 = x1;

		if (this.x2 == null) {
			return;
		}
		if (x1.x == this.x2.x) {
			this.parallelY = true;
		}
	}

	public void setX2(final Point2D.Double x2) {
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
}
