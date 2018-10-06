package de.schuette.cobra2D.entity;

import java.awt.Point;

import de.schuette.cobra2D.math.Line;

public class Collision {

	private final Entity entity;
	private final Point collisionPoint;
	private final Line collisionLineOpponent;
	private final Line collisionLineEntity;

	public Collision(final Entity entity, final Point collisionPoint,
			final Line collisionLineOpponent, final Line collisionLineEntity) {
		super();
		this.entity = entity;
		this.collisionPoint = collisionPoint;
		this.collisionLineOpponent = collisionLineOpponent;
		this.collisionLineEntity = collisionLineEntity;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public Point getCollisionPoint() {
		return this.collisionPoint;
	}

	public Line getCollisionLineOpponent() {
		return this.collisionLineOpponent;
	}

	public Line getCollisionLineEntity() {
		return this.collisionLineEntity;
	}

}