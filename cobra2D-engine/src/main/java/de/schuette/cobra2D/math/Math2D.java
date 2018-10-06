package de.schuette.cobra2D.math;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.List;

import de.schuette.cobra2D.entity.Entity;

public class Math2D {

	/**
	 * Calculates the middlepoint in a picture.
	 * 
	 * @param image
	 *            The picture
	 * @return Returns the middlepoint in the picture
	 */
	public static Point getMiddlepoint(final BufferedImage image) {
		final double middleX = image.getWidth() / 2.0;
		final double middleY = image.getHeight() / 2.0;
		final Point middleInPicture = new Point(middleX, middleY);
		return middleInPicture;
	}

	/**
	 * Calculates a random number between min an max (included).
	 * 
	 * @param min
	 * @param max
	 * @return The random number in the given range.
	 */
	public static double random(double min, double max) {
		final double h = min;
		min = Math.min(min, max);
		max = Math.max(h, max);
		return (Math.random() * (max - min + 1) + min);
	}

	// /**
	// * Shorter command for (int) Math.round(value);
	// *
	// * @param value
	// * @return Returns the rounded int of the value.
	// */
	// public static int saveRound(final float value) {
	// return Math.round(value);
	// }

	/**
	 * Shorter command for (int) Math.round(value);
	 * 
	 * @param value
	 * @return Returns the rounded int of the value.
	 */
	public static int saveRound(final double value) {
		return (int) Math.round(value);
	}

	/**
	 * Calculates real world coordinates into relative coordinates in a viewport.
	 * 
	 * @param realWorld
	 *            Real world coordinates.
	 * @param viewport
	 *            Viewport coordinates.
	 * @return
	 */
	public static Point getRelativePointTranslation(final Point realWorld, final Rectangle viewport) {
		final Point renderPos = new Point(realWorld.x - viewport.x, realWorld.y - viewport.y);
		return renderPos;
	}

	/**
	 * Calculates real world coordinates into relative coordinates in a viewport.
	 * 
	 * @param realWorld
	 *            Real world coordinates.
	 * @param viewport
	 *            Viewport coordinates.
	 * @return
	 */
	public static Point getRelativePointTranslation(final Entity entity, final Rectangle viewport) {
		return Math2D.getRelativePointTranslation(entity.getPosition(), viewport);
	}

	// public static int getDirection(double yourDegrees, double wantedDegrees)
	// {
	// double half = yourDegrees - wantedDegrees;
	// int sign;
	//
	// if (half < 0)
	// half = 360 - half;
	//
	// if (half > 180)
	// sign = 1;
	// else
	// sign = -1;
	//
	// return sign;
	// }

	public static Point getCenterOfScreen(final double width, final double height) {

		final double sx = Toolkit.getDefaultToolkit().getScreenSize().width;
		final double sy = Toolkit.getDefaultToolkit().getScreenSize().height;
		return new Point((sx - width) / 2, (sy - height) / 2);
	}

	public static Point getMittelpunkt(final Point start, final Point ende) {
		return new Point((start.x + ende.x) / 2.0d, (start.y + ende.y) / 2.0d);
	}

	public static boolean isInCircle(final Point point, final Point mcircle, final double radius) {

		if (Math.pow(point.x - mcircle.x, 2.0d) + Math.pow(point.y - mcircle.y, 2.0d) <= Math.pow(radius, 2)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isInRect(final Point point, Point rectX1, Point rectX2) {

		if ((point.x >= rectX1.x && point.x <= rectX2.x) && (point.y >= rectX1.y && point.y <= rectX2.y)) {
			return true;
		}

		final Point tmp = rectX1;
		rectX1 = rectX2;
		rectX2 = tmp;

		if ((point.x >= rectX1.x && point.x <= rectX2.x) && (point.y >= rectX1.y && point.y <= rectX2.y)) {
			return true;
		}

		return false;
	}

	public static double getSteigung(final Point start, final Point ende) {

		double result;
		result = (ende.y - start.y) / (ende.x - start.x);
		return result;
	}

	public static Point getCircle(final Point start, final double radius, final double winkel) {
		// winkel -= 90;

		final double w = winkel * (Math.PI / 180.d);

		final double x = (start.getX() + (Math.cos(w) * radius));
		final double y = (start.getY() + (Math.sin(w) * radius));

		return new Point(x, y);

	}

	public static double getAngle(final Point start, final Point end) {

		double w = (Math.atan2((end.y - start.y), (end.x - start.x))) * (180.d / Math.PI);

		// w = w + 90;
		if (w <= 0.0) {
			w = 360.0 + w;
		}

		// //BEDENKLICH
		if (w >= 360.0) {
			w = w - 360.0;
		}

		return w;

	}

	public static double getEntfernung(final Point start, final Point ende) {
		final double e = Math.sqrt(Math.pow((start.x - ende.x), 2) + Math.pow((start.y - ende.y), 2));
		return e;
	}

	public static Point getPointNextToY(final List<Point> points) {
		double minX = Double.MAX_VALUE;
		Point anchorNextToX = points.get(0);

		for (int i = 0; i < points.size(); i++) {
			final Point aktPoint = points.get(i);
			final double x = aktPoint.x;

			if (x < minX) {
				minX = x;
				anchorNextToX = aktPoint;
			}
		}

		return new Point(anchorNextToX.x, anchorNextToX.y);
	}

	public static EntityPoint getPointNextTo(final EntityPoint nextTo, final List<EntityPoint> points) {
		double laenge = Double.MAX_VALUE;
		EntityPoint closest = null;

		for (int i = 0; i < points.size(); i++) {
			final EntityPoint point = points.get(i);
			final double newLaenge = Math2D.getEntfernung(nextTo.getCoordinates(), point.getCoordinates());
			if (newLaenge < laenge) {
				laenge = newLaenge;
				closest = point;
			}
		}

		return closest;
	}

	public static Point getPointNextTo(final Point nextTo, final List<Point> points) {
		double laenge = Double.MAX_VALUE;
		Point closest = null;

		for (int i = 0; i < points.size(); i++) {
			final Point point = points.get(i);
			final double newLaenge = Math2D.getEntfernung(nextTo, point);
			if (newLaenge < laenge) {
				laenge = newLaenge;
				closest = point;
			}
		}

		return closest;
	}

	public static Point getPointNextToEntityPoints(final Point nextTo, final List<EntityPoint> points) {
		double laenge = Double.MAX_VALUE;
		Point closest = null;

		for (int i = 0; i < points.size(); i++) {
			final EntityPoint ePoint = points.get(i);
			final Point point = ePoint.getCoordinates();
			final double newLaenge = Math2D.getEntfernung(nextTo, point);
			if (newLaenge < laenge) {
				laenge = newLaenge;
				closest = point;
			}
		}

		return closest;
	}

	public static Point getPointNextToX(final List<Point> points) {
		double minY = Double.MAX_VALUE;
		Point anchorNextToY = points.get(0);

		for (int i = 0; i < points.size(); i++) {
			final Point aktPoint = points.get(i);
			final double y = aktPoint.y;

			if (y < minY) {
				minY = y;
				anchorNextToY = aktPoint;
			}
		}
		return new Point(anchorNextToY.x, anchorNextToY.y);
	}

	public static Point getPointMaxDistToX(final List<Point> points) {
		double maxY = 0;
		Point anchorMaxDistToY = points.get(0);

		for (int i = 0; i < points.size(); i++) {
			final Point aktPoint = points.get(i);
			final double y = aktPoint.y;

			if (y > maxY) {
				maxY = y;
				anchorMaxDistToY = aktPoint;
			}
		}

		return new Point(anchorMaxDistToY.x, anchorMaxDistToY.y);
	}

	public static Point getPointMaxDistToY(final List<Point> points) {
		double maxX = 0;
		Point anchorMaxDistToX = points.get(0);

		for (int i = 0; i < points.size(); i++) {
			final Point aktPoint = points.get(i);
			final double x = aktPoint.x;

			if (x > maxX) {
				maxX = x;
				anchorMaxDistToX = aktPoint;
			}
		}

		return new Point(anchorMaxDistToX.x, anchorMaxDistToX.y);
	}

	public static Rectangle getHuellRect(final List<Point> punktliste) {
		final Point x1 = new Point(Math2D.getPointNextToY(punktliste).x - 10d,
				Math2D.getPointNextToX(punktliste).y - 10d);
		// Point x2 = new Point(getPointMaxDistToY(punktliste).x + 10,
		// getPointNextToX(punktliste).y - 10);

		final Point x3 = new Point(Math2D.getPointMaxDistToY(punktliste).x + 10d,
				Math2D.getPointMaxDistToX(punktliste).y + 10d);
		// Point x4 = new Point(getPointNextToY(punktliste).x - 10,
		// getPointMaxDistToX(punktliste).y + 10);

		final Rectangle rect = new Rectangle(x1.getPoint(),
				new Dimension(saveRound(x3.x - x1.x), saveRound(x3.y - x1.y)));

		return rect;
	}

	/**
	 * Sorts the specified list of points so that the points connected with lines in
	 * the order the list specified result in a polygon with non crossing lines.
	 * 
	 * @param points
	 */
	public static void sortEntityPoints(List<EntityPoint> points) {
		if (points.size() == 0)
			return;

		for (int i = 1; i < points.size(); i++) {
			EntityPoint current = points.get(i - 1);
			List<EntityPoint> subListView = points.subList(i, points.size());
			EntityPoint nextPoint = getPointNextTo(current, subListView);
			points.remove(nextPoint);
			points.add(i, nextPoint);
		}
	}

	public static double[] pqFormula(double pP, double pQ) {
		double diskriminante;
		diskriminante = (pP / 2.0) * (pP / 2.0) - pQ;
		if (diskriminante >= 0) {
			double x1, x2;
			x1 = -(pP / 2) + Math.sqrt(diskriminante);
			x2 = -(pP / 2) - Math.sqrt(diskriminante);
			if (x1 == x2) {
				return new double[] { x2 };
			} else {
				return new double[] { x1, x2 };// Was x1, x2
			}
		} else {
			return new double[] {};
		}
	}

}
