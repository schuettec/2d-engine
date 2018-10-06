package de.schuette.cobra2D.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class describes a geometric polygon in a 2D coordinate system. It is
 * basically a list of {@link Line}.
 * 
 * @author Chris
 *
 */
public class Polygon implements Shape, Cloneable {

	/**
	 * Holds the list of {@link Line}s making up the collision hull polygon. Note:
	 * After every modification of the list call
	 * {@link Math2D#sortEntityPoints(List)} to make sure the points are ordered as
	 * polygon with non-crossing lines.
	 */
	protected List<EntityPoint> entityPoints;

	public Polygon() {
		super();
		this.entityPoints = new LinkedList<>();
	}

	public Polygon(EntityPoint... entityPoints) {
		this.entityPoints = new ArrayList<EntityPoint>(entityPoints.length);
		for (EntityPoint p : entityPoints) {
			this.entityPoints.add(p.clone());
		}

		Math2D.sortEntityPoints(this.entityPoints);
	}

	public Polygon(List<EntityPoint> entityPoints) {
		this.entityPoints = new LinkedList<EntityPoint>();
		this.entityPoints.addAll(entityPoints);
		Math2D.sortEntityPoints(this.entityPoints);
	}

	public List<EntityPoint> getEntityPoints() {
		return Collections.unmodifiableList(entityPoints);
	}

	/**
	 * @return Returns the list of {@link Line}s that represents this
	 *         {@link Polygon}. The lines are returned in the correct order.
	 *         Manipulations on the returned {@link Line}s do not modify this
	 *         polygon.
	 */
	public List<Line> getLines() {
		final List<Line> lineList = new ArrayList<Line>();

		if (entityPoints.size() > 0) {
			for (int i = 1; i < entityPoints.size(); i++) {
				EntityPoint start = entityPoints.get(i);
				EntityPoint end = entityPoints.get(i - 1);
				lineList.add(new Line(start.getCoordinates(), end.getCoordinates()));
			}

			EntityPoint start = entityPoints.get(0);
			EntityPoint end = entityPoints.get(entityPoints.size() - 1);
			lineList.add(new Line(start.getCoordinates(), end.getCoordinates()));
		}

		return lineList;

	}

	@Override
	public Polygon clone() {
		List<EntityPoint> clone = new LinkedList<>();
		for (EntityPoint e : entityPoints) {
			EntityPoint n = new EntityPoint(e.getDegrees(), e.getRadius());
			clone.add(n);
		}
		return new Polygon(clone);
	}

	@Override
	public Polygon rotate(double degrees) {
		for (EntityPoint point : entityPoints) {
			point.rotate(degrees);
		}
		return this;
	}

	@Override
	public Polygon translate(Point translation) {
		for (EntityPoint point : entityPoints) {
			point.translate(translation);
		}
		return this;
	}

	@Override
	public Polygon scale(double scaleFactor) {
		for (EntityPoint point : entityPoints) {
			point.scale(scaleFactor);
		}
		return this;
	}
}
