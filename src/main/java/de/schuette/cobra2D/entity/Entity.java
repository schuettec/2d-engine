package de.schuette.cobra2D.entity;

import java.awt.Dimension;
import java.awt.image.VolatileImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.schuette.cobra2D.entity.editing.BooleanEditor;
import de.schuette.cobra2D.entity.editing.DegreesEditor;
import de.schuette.cobra2D.entity.editing.DimensionEditor;
import de.schuette.cobra2D.entity.editing.Editable;
import de.schuette.cobra2D.entity.editing.EditableProperty;
import de.schuette.cobra2D.entity.editing.PointEditor;
import de.schuette.cobra2D.entity.editing.StringEditor;
import de.schuette.cobra2D.math.EntityPoint;
import de.schuette.cobra2D.math.Line;
import de.schuette.cobra2D.math.Math2D;
import de.schuette.cobra2D.math.Point;
import de.schuette.cobra2D.system.Cobra2DEngine;

@Editable
public class Entity implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected List<EntityPoint> hull = new ArrayList<EntityPoint>();

	@EditableProperty(modifierClass = StringEditor.KEY)
	protected String textureKey;

	@EditableProperty(modifierClass = StringEditor.KEY)
	protected String entityName = "unnamed";

	@EditableProperty(modifierClass = PointEditor.KEY)
	protected Point position = new Point(0, 0);

	@EditableProperty(modifierClass = DimensionEditor.KEY)
	protected Dimension size = new Dimension(0, 0);

	@EditableProperty(modifierClass = DegreesEditor.KEY)
	protected double degrees = 0;

	@EditableProperty(modifierClass = BooleanEditor.KEY)
	protected boolean linkSizeToTexture = true;

	protected transient VolatileImage image;
	protected transient Cobra2DEngine engine;

	public Entity() {
	}

	/**
	 * This method is used to initialize an entity an engage it with an instance of
	 * the engine. Feel free to overwrite this method for your initializations. This
	 * method is used if some initializations depend on the instance of the engine.
	 *
	 * @param engine The engine, injected by the framework.
	 * @throws EntityInitializeException
	 */
	public void initialize(final Cobra2DEngine engine) throws EntityInitializeException {
		this.engine = engine;

		if (this.textureKey != null) {
			this.changeTexture(this.textureKey);
		}
	}

	/**
	 * This method frees any ressources and disengages this entity from the engine.
	 * Note: This method invalidates the instance of this entity. Be sure that this
	 * reference is no longe used. A finished entity cannot be reused!
	 */
	public void finish() {
		this.hull = null;
		this.entityName = null;
		this.textureKey = null;
		this.image = null;
		this.position = null;
		this.size = null;
		this.engine = null;
	}

	public void addHullPoint(final EntityPoint entityPoint) {
		this.hull.add(entityPoint);
		this.resetPoints();
	}

	public void removeHullPoint(final EntityPoint entityPoint) {
		this.hull.remove(entityPoint);
		this.resetPoints();
	}

	/**
	 * @return Returns a copy of the point list of this entity.
	 */
	public List<EntityPoint> getPointList() {
		return new ArrayList<EntityPoint>(this.hull);
	}

	public String getTextureKey() {
		return this.textureKey;
	}

	/**
	 * This method sets a texture for this entity. Note: This method is used to
	 * define a texture before initializing the entity. To change the texture of an
	 * initialized entity, use changeTexture(String)-Method.
	 *
	 * @param textureKey
	 */
	public void setTextureKey(final String textureKey) {
		changeTexture(textureKey);
	}

	/**
	 * This method loads a new texture from the texture memory of the engine. This
	 * method recalculates the size of this entity if no size was specified before.
	 * Note that this method can only be used on an entity that was initialized
	 * before. Otherwise there will be errors while loading the texture from a non
	 * existing engine. This method is mainly used WITHIN the initialize()-Method to
	 * bind this entity to an engine's texture storage.
	 *
	 * @param textureKey The new texture to use.
	 */
	public void changeTexture(String textureKey) {
		this.textureKey = textureKey;
		if (textureKey == null || textureKey.trim().length() == 0) {
			return;
		}
		if (engine != null) {
			this.image = this.engine.getImageMemory().getImage(textureKey);
			adjustSize();
		}
	}

	/**
	 * @return Returns true if this entity changes its size according to texture
	 *         changes, false otherwise.
	 */
	public boolean isLinkSizeToTexture() {
		return linkSizeToTexture;
	}

	/**
	 * This method is used to adjust the size of this entity by using the dimensions
	 * of the texture. Note that this is only possible if the entity was initialized
	 * and therefore has a texture.
	 */
	public void adjustSize() {
		if (isLinkSizeToTexture()) {
			if (this.image != null) {
				this.setSize(new Dimension(this.image.getWidth(), this.image.getHeight()));
			}
		}
	}

	/**
	 * Returns the currently texture image referenced by this entity.
	 *
	 * @return Volatile Image representing the current texture.
	 */
	public VolatileImage getImage() {
		if (this.image == null && engine != null) {
			this.image = this.engine.getImageMemory().getImage(this.textureKey);
		}
		return this.image;
	}

	/**
	 * Sets the current texture of this entity. This method does not recalculate the
	 * size of the object. The size is only adjusted if using changeTexture();
	 *
	 * @param image The image to set
	 */
	public void setImage(final VolatileImage image) {
		// this.setSize(new Dimension(image.getWidth(), image.getHeight()));
		this.image = image;
	}

	/**
	 *
	 * @return Returns the current location of this entity in world coordinates.
	 */
	public Point getPosition() {
		return this.position;
	}

	/**
	 * Sets the current location of this entity in world coordinates.
	 *
	 * @param position The current location of this entity in world coordinates.
	 */
	public void setPosition(final Point position) {
		this.position.x = position.x;
		this.position.y = position.y;
		this.resetPoints();

	}

	/**
	 * @return Returns the size of this entity in world coordinates.
	 */
	public Dimension getSize() {
		return this.size;
	}

	/**
	 * @param size Sets the size of this entity in world coordinates.
	 */
	public void setSize(final Dimension size) {
		this.size = size;
	}

	/**
	 * @return Returns the current degrees of this entity.
	 */
	public double getDegrees() {
		return this.degrees;
	}

	/**
	 * @param degrees Sets the degrees of this entity.
	 */
	public void setDegrees(final double degrees) {
		this.degrees = degrees;
		this.rotatePoints((int) Math.round(degrees));
	}

	/**
	 * @return Returns the list of lines calculated by the current entity points.
	 */
	public List<Line> getLineList() {
		final List<Line> lineList = new ArrayList<Line>();

		if (this.hull.size() == 0) {
			return lineList;
		}

		for (int i = 1; i < this.hull.size(); i++) {
			lineList.add(new Line(this.hull.get(i).getCoordinates(), this.hull.get(i - 1).getCoordinates()));
		}

		lineList.add(new Line(this.hull.get(0).getCoordinates(), this.hull.get(this.hull.size() - 1).getCoordinates()));

		return lineList;
	}

	/**
	 * Recalculates the rotation of the entity points relative to the given degrees.
	 * This method does not modify the degrees of the entity. Note: It is
	 * recommended to use this method only for internal purposes.
	 *
	 * @param degrees The degrees of rotation of the entity points
	 */
	public void rotatePoints(final double degrees) {

		for (int i = 0; i < hull.size(); i++) {
			final EntityPoint point = hull.get(i);
			point.rotate(degrees);

		}

		// this.degrees = degrees;
	}

	/**
	 * Reset any deviant rotation of the entity points.
	 */
	public void resetPoints() {

		this.setDegrees(this.degrees);
	}

	/**
	 * @param entity Another entity
	 * @return True if the other entity hits the rectangle enclosing this entity.
	 */
	public boolean isInMyRect(final Entity entity) {

		final Point x1 = this.getPosition();
		final Point x2 = new Point(this.getPosition().x + this.getSize().width,
				this.getPosition().y + this.getSize().height);

		for (int i = 0; i < hull.size(); i++) {
			final EntityPoint point = hull.get(i);
			if (Math2D.isInRect(point.getCoordinates(), x1, x2)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks if a given point hits the rectangle enclosing this entity. See
	 * {@link #isInMyRect(Entity)}.
	 *
	 * @param point Another point
	 * @return True if it hits the rectangle, False if not.
	 */
	public boolean isInMyRect(final Point point) {

		final Point x1 = this.getPosition();
		final Point x2 = new Point(this.getPosition().x + this.getSize().width,
				this.getPosition().y + this.getSize().height);

		if (Math2D.isInRect(point, x1, x2)) {
			return true;
		}

		return false;
	}

	/**
	 * Searches for a collision of this entity with an entity from the list.
	 *
	 * @param entityList The list of entities to check.
	 * @return Returns a list of collision objects. Every object specifies a
	 *         collision with another entity only if there is currently a collision
	 *         detectable.
	 */
	public List<Collision> collisionWith(final List<?> entityList) {
		// Wenn eine Gerade der Entity einen schnittpunkt mit einer Geraden
		// einer anderen Entity liefert, so liegt eine Kollision vor.
		final List<Collision> entities = new ArrayList<Collision>();

		final List<?> toCheck = entityList;
		toCheck.remove(this);

		for (int i = 0; i < entityList.size(); i++) {
			Entity anEntity;
			try {
				anEntity = (Entity) entityList.get(i);
			} catch (final Exception e) {
				throw new RuntimeException("Cannot analyse collision, because object is not an entity.");
			}

			if (anEntity == null) {
				continue;
				// Entity Filter (Entities herausfiltern, mit denen nicht
				// kollidiert
				// wird)
				// if((anEntity instanceof DebugEntity) ||(anEntity instanceof
				// Floor) ||(anEntity instanceof ExplosionSmall) || (anEntity
				// instanceof Shot)
				// || (anEntity.equals(this))) continue;
			}

			final List<Line> lineList = anEntity.getLineList();

			for (final Line myLine : this.getLineList()) {
				for (final Line l : lineList) {
					Point schnittPunkt;
					if ((schnittPunkt = myLine.intersects(l)) != null) {
						entities.add(new Collision(anEntity, schnittPunkt, l, myLine));
					}
				}
			}

		}

		return entities;
	}

	/**
	 * {@link #collisionWithFirst(List)}
	 *
	 * @param entityList The list of entities.
	 * @return The first collision detected.
	 */
	public Collision collisionWithFirst(final List<?> entityList) {
		// Wenn eine Gerade der Entity einen schnittpunkt mit einer Geraden
		// einer anderen Entity liefert, so liegt eine Kollision vor.

		final List<?> toCheck = entityList;
		toCheck.remove(this);

		for (int i = 0; i < toCheck.size(); i++) {

			Entity anEntity;
			try {
				anEntity = (Entity) entityList.get(i);
			} catch (final Exception e) {
				throw new RuntimeException("Cannot analyse collision, because object is not an entity.");
			}

			if (anEntity == null) {
				continue;
			}

			final List<Line> lineList = anEntity.getLineList();

			for (final Line myLine : this.getLineList()) {
				for (final Line l : lineList) {
					Point schnittPunkt;
					if ((schnittPunkt = myLine.intersects(l)) != null) {

						return new Collision(anEntity, schnittPunkt, l, myLine);

					}
				}
			}
		}

		return null;
	}

	/**
	 * @return The entity name of this entity.
	 */
	public String getEntityName() {
		return this.entityName;
	}

	/**
	 * @param entityName The entity name to set.
	 */
	public void setEntityName(final String entityName) {
		this.entityName = entityName;
	}

	/**
	 * Calculates the center point for this entity, based on the current location
	 * and its dimension.
	 *
	 * @return The center point of this entity.
	 */
	public Point getCenterPoint() {
		if (size == null) {
			return new Point(this.getPosition().x, this.getPosition().y);
		} else {
			return new Point(this.getPosition().x + (int) (this.getSize().width / 2.0),
					this.getPosition().y + (int) (this.getSize().height / 2.0));
		}
	}

	/**
	 * Creates a rectangle of entity points and adds it to this entity.
	 */
	public void createRectangleEntityPoints() {
		final int emX = (int) (this.getSize().width / 2.0);
		final int emY = (int) (this.getSize().height / 2.0);
		final Point eMiddle = new Point(emX, emY);

		Point newPoint;
		hull.clear();

		newPoint = new Point(1, 1);
		hull.add(new EntityPoint(Math2D.getAngle(eMiddle, newPoint), (int) Math2D.getEntfernung(eMiddle, newPoint)));

		newPoint = new Point(this.getSize().width, 1);
		hull.add(new EntityPoint(Math2D.getAngle(eMiddle, newPoint), (int) Math2D.getEntfernung(eMiddle, newPoint)));

		newPoint = new Point(this.getSize().width, this.getSize().height);
		hull.add(new EntityPoint(Math2D.getAngle(eMiddle, newPoint), (int) Math2D.getEntfernung(eMiddle, newPoint)));

		newPoint = new Point(1, this.getSize().height);
		hull.add(new EntityPoint(Math2D.getAngle(eMiddle, newPoint), (int) Math2D.getEntfernung(eMiddle, newPoint)));

		resetPoints();
	}

	/**
	 * Creates a rectangle of entity points and adds it to this entity.
	 */
	public void createRectangleEntityPointsWithPositionInCenter() {
		hull.clear();

		final int widthHalf = Math2D.saveRound(this.getSize().width / 2.0);
		final int heightHalf = Math2D.saveRound(this.getSize().height / 2.0);

		final Point center = new Point(widthHalf, heightHalf);

		Point newPoint = new Point(-widthHalf, -heightHalf);
		double distance = Math2D.getEntfernung(center, newPoint);
		double angle = Math2D.getAngle(center, newPoint);
		hull.add(new EntityPoint(angle, Math2D.saveRound(distance)));

		newPoint = new Point(+widthHalf, -heightHalf);
		distance = Math2D.getEntfernung(center, newPoint);
		angle = Math2D.getAngle(center, newPoint);
		hull.add(new EntityPoint(angle, Math2D.saveRound(distance)));

		newPoint = new Point(+widthHalf, +heightHalf);
		distance = Math2D.getEntfernung(center, newPoint);
		angle = Math2D.getAngle(center, newPoint);
		hull.add(new EntityPoint(angle, Math2D.saveRound(distance)));

		newPoint = new Point(-widthHalf, +heightHalf);
		distance = Math2D.getEntfernung(center, newPoint);
		angle = Math2D.getAngle(center, newPoint);
		hull.add(new EntityPoint(angle, Math2D.saveRound(distance)));

		resetPoints();

	}

	/**
	 * Creates one entity point and adds it to this entity. The entity point is the
	 * center point of this entity.
	 */
	public void createMiddleHullPoint() {
		hull.clear();
		hull.add(new EntityPoint(0, 0));
		resetPoints();
	}

	/**
	 * Checks if there is a collision with a specified line and this entity.
	 *
	 * @param line The line to check
	 * @return The collision object, or null if there is no collision.
	 */
	public Collision isCollision(final Line line) {

		for (final Line myLine : this.getLineList()) {
			Point schnittPunkt;
			if ((schnittPunkt = myLine.intersects(line)) != null) {
				return new Collision(this, schnittPunkt, line, myLine);
			}

		}
		return null;
	}

	/**
	 * Checks if there is a collision with a specified entity and this entity.
	 *
	 * @param entity The entity to check
	 * @return The collision object, or null if there is no collision.
	 */
	public Collision collisionWith(final Entity entity) {

		for (final Line myLine : this.getLineList()) {
			for (final Line line : entity.getLineList()) {
				Point schnittPunkt;
				if ((schnittPunkt = myLine.intersects(line)) != null) {
					return new Collision(entity, schnittPunkt, line, myLine);
				}
			}
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(this.degrees);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((this.entityName == null) ? 0 : this.entityName.hashCode());
		result = prime * result + ((this.hull == null) ? 0 : this.hull.hashCode());
		result = prime * result + ((this.position == null) ? 0 : this.position.hashCode());
		result = prime * result + ((this.size == null) ? 0 : this.size.hashCode());
		result = prime * result + ((this.textureKey == null) ? 0 : this.textureKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final Entity other = (Entity) obj;
		if (Double.doubleToLongBits(this.degrees) != Double.doubleToLongBits(other.degrees)) {
			return false;
		}
		if (this.entityName == null) {
			if (other.entityName != null) {
				return false;
			}
		} else if (!this.entityName.equals(other.entityName)) {
			return false;
		}
		if (this.hull == null) {
			if (other.hull != null) {
				return false;
			}
		} else if (!this.hull.equals(other.hull)) {
			return false;
		}
		if (this.position == null) {
			if (other.position != null) {
				return false;
			}
		} else if (!this.position.equals(other.position)) {
			return false;
		}
		if (this.size == null) {
			if (other.size != null) {
				return false;
			}
		} else if (!this.size.equals(other.size)) {
			return false;
		}
		if (this.textureKey == null) {
			if (other.textureKey != null) {
				return false;
			}
		} else if (!this.textureKey.equals(other.textureKey)) {
			return false;
		}
		return true;
	}

}
