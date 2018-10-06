package de.schuette.cobra2DSandbox.ball;

import static de.schuette.cobra2D.math.Math2D.getCircle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import de.schuette.cobra2D.entity.Collision;
import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.EntityInitializeException;
import de.schuette.cobra2D.entity.editing.Editable;
import de.schuette.cobra2D.entity.editing.EditableProperty;
import de.schuette.cobra2D.entity.editing.NumberEditor;
import de.schuette.cobra2D.entity.skills.Moveable;
import de.schuette.cobra2D.entity.skills.Obstacle;
import de.schuette.cobra2D.entity.skills.Renderable;
import de.schuette.cobra2D.math.Line;
import de.schuette.cobra2D.math.Math2D;
import de.schuette.cobra2D.system.Cobra2DEngine;

@Editable
public class Ball extends Entity implements Renderable, Obstacle, Moveable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EditableProperty(modifierClass = NumberEditor.KEY)
	protected int thickness = 20;

	@EditableProperty(modifierClass = NumberEditor.KEY)
	protected double speed;

	public Ball() {

	}

	@Override
	public void render(final Graphics2D g, final Point position) {

		final Color oldColor = g.getColor();
		g.setColor(Color.YELLOW);
		g.fillOval(position.x, position.y, this.thickness, this.thickness);
		g.setColor(oldColor);
	}

	@Override
	public void next() {
		final Point oldPosition = this.getPosition();

		Point newPosition = getCircle(oldPosition, this.speed, this.degrees);

		List<Obstacle> entitiesWithoutBalls = new ArrayList<Obstacle>();

		for (int i = 0; i < this.engine.getMap().getObstacle().size(); i++) {
			if (!(this.engine.getMap().getObstacle().get(i) instanceof Ball)) {
				entitiesWithoutBalls.add(this.engine.getMap().getObstacle().get(i));
			}
		}

		final Collision collision = this.collisionWithFirst(entitiesWithoutBalls);

		if (collision != null) {
			// DebugEntity debug = new DebugEntity(engine,
			// collision.getCollisionPoint());
			// engine.getMap().addEntity(debug);

			final Line line = collision.getCollisionLineOpponent();
			final Point colLineStart = line.getStartPoint();
			final Point colLineEnd = line.getEndPoint();
			final int colLineDegrees = Math2D.getAngle(colLineStart, colLineEnd);

			// Einfallswinkel und Objektwinkel sind gleich: Resultat
			// newAngle += 180 (Gegenrichtung)
			int newAngle = 0;
			if ((int) Math.round(this.getDegrees() - colLineDegrees) == 0) {
				newAngle = (int) Math.round((this.getDegrees() + 180) % 360);
			} else {
				final int angle = (int) Math.round(this.getDegrees() - colLineDegrees) % 360;
				newAngle = Math.round(360 - angle + colLineDegrees) % 360;

			}

			newPosition = Math2D.getCircle(oldPosition, Math.max(this.getSize().width, this.getSize().height),
					newAngle);
			this.setDegrees(newAngle);
			newPosition = Math2D.getCircle(oldPosition, this.speed, newAngle);
			this.setPosition(newPosition);
		}

		this.setPosition(newPosition);
	}

	@Override
	public void changeTexture(String textureKey) {
		throw new UnsupportedOperationException(
				"A ball can have no texture because it is rendered with it own graphical operations.");
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public void initialize(final Cobra2DEngine engine) throws EntityInitializeException {
		this.engine = engine;

		this.setSize(new Dimension(this.thickness, this.thickness));
		this.createRectangleEntityPoints();

	}

	@Override
	public boolean isCollision(final Entity entity) {
		return this.isInMyRect(entity);
	}

	@Override
	public boolean isCollision(final Point point) {
		return this.isInMyRect(point);
	}

	@Override
	public void collision(final Entity entity) {

	}

	public double getSpeed() {
		return this.speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public int getThickness() {
		return thickness;
	}

	public void setThickness(int thickness) {
		this.thickness = thickness;
	}
}
