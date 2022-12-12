package de.schuette.cobra2D.entity;

import de.schuette.cobra2D.entity.skills.Obstacle;
import de.schuette.cobra2D.math.Math2D;
import de.schuette.cobra2D.math.Point;

public class MovementUtils {

	/**
	 * Moves the entity in the direction specified by degrees. The speed is a radius
	 * of one step.
	 * 
	 * @param entity
	 *            The entity to move.
	 * @param speed
	 *            The step width or speed of movement.
	 */
	public static void move(final Entity entity, final double speed) {
		final Point neuePosition = Math2D.getCircle(entity.getPosition(), speed, entity.getDegrees());
		entity.setPosition(neuePosition);
	}

	/**
	 * Performs a default movement and collision detection. The movement is based on
	 * the entities degrees and the speed given by parameter. In case of a collision
	 * the movement is undone by calculating the old position. The method also
	 * calculates the speed of this entity that is given by parameter.
	 * 
	 * @param speed
	 *            Returns the speed after the collision, or the unmodified speed if
	 *            there is no collision.
	 * @return
	 */
	public static double defaultCollisionMovement(double speed, final Entity entity) {
		final Point neuePosition = Math2D.getCircle(entity.getPosition(), speed, entity.getDegrees());

		// Endg�ltig Position setzen
		entity.setPosition(neuePosition);

		final Collision collision = entity.collisionWithFirst(entity.engine.getMap().getObstacle());

		if (collision == null) {
			return speed;
		} else {

			if (collision.getEntity() instanceof Obstacle) {
				((Obstacle) collision.getEntity()).collision(entity);
			}

			if (entity instanceof Obstacle) {
				((Obstacle) entity).collision(collision.getEntity());
			}

			if (Math.abs(speed) < 10) {
				speed = Math.signum(speed) * 10;
			}

			// EXPERIMENTAL BUT GOOD
			final Point colMitte = collision.getCollisionPoint();

			// Richtung in die abgesto�en werden muss
			final double newDegrees = Math2D.getAngle(colMitte, entity.getCenterPoint());

			// Der naechste Punkt der Entity zum Collisionspunkt
			final Point closest = Math2D.getPointNextToEntityPoints(colMitte, entity.getPointList());

			// Die L�nge dieses Punktes zum eigenen Mittelpunkt
			final double laenge = Math2D.getEntfernung(entity.getCenterPoint(), closest);

			// Spieler muss vom punkt colMitte aus, in Richtung newDegrees um
			// laenge + PUFFER
			// verschoben werden
			Point newPoint = Math2D.getCircle(colMitte, laenge + 20, newDegrees);

			// ACHTUNG: Hier ist es wichtig, die Position vom Zentrum aus, neu
			// zu setzen. Daher wird width/2 und height/2 abgezogen.
			newPoint = new Point((int) (newPoint.x - (entity.getSize().width / 2.0)),
					(int) (newPoint.y - (entity.getSize().height / 2.0)));

			entity.setPosition(newPoint);

			return speed = -Math.round(speed / 3);

		}
	}
}
