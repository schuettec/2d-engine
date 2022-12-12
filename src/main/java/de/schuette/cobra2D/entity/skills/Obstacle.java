package de.schuette.cobra2D.entity.skills;

import de.schuette.cobra2D.math.Point;
import java.io.Serializable;

import de.schuette.cobra2D.entity.Entity;

public interface Obstacle extends Serializable {

	/**
	 * As an obstacle the entity must return a boolean value if there is a
	 * collision with another point or entity
	 * 
	 * @param entity
	 *            The entity to perform a collision test with.
	 * @return True if there is a collision, Fals if not.
	 */
	public abstract boolean isCollision(final Entity entity);

	/**
	 * As an obstacle the entity must return a boolean value if there is a
	 * collision with another point or entity
	 * 
	 * @param point
	 *            The point to perform a collision test with.
	 * @return True if there is a collision, Fals if not.
	 */
	public abstract boolean isCollision(final Point point);

	/**
	 * This method can be called by entities, to message a collision to this
	 * entity.
	 * 
	 * @param entity
	 *            The other entity that collides with this.
	 */
	public abstract void collision(final Entity entity);
}
