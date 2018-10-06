package de.schuette.cobra2D.map;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.EntityPoint;
import de.schuette.cobra2D.entity.skills.AlwaysVisible;
import de.schuette.cobra2D.entity.skills.Camera;
import de.schuette.cobra2D.entity.skills.Moveable;
import de.schuette.cobra2D.entity.skills.Obstacle;
import de.schuette.cobra2D.entity.skills.Renderable;
import de.schuette.cobra2D.math.Math2D;
import de.schuette.cobra2D.system.Cobra2DEngine;

public class Map implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Entity> allEntities;
	private final List<Camera> cameraEntities;

	private transient List<Entity> noskilledEntities;
	private transient List<MapListener> listeners;
	private transient List<Moveable> movableEntities;
	private transient List<Obstacle> obstacleEntities;
	private transient List<Renderable> renderableEntities;
	private transient List<Entity> worldObjects;

	public Map() {
		this.allEntities = new ArrayList<Entity>();
		this.cameraEntities = new ArrayList<Camera>();
		this.listeners = new ArrayList<MapListener>();
		this.noskilledEntities = new ArrayList<Entity>();
		this.movableEntities = new ArrayList<Moveable>();
		this.obstacleEntities = new ArrayList<Obstacle>();
		this.renderableEntities = new ArrayList<Renderable>();
		this.worldObjects = new ArrayList<Entity>();
	}

	public void addMapListener(final MapListener listener) {
		this.listeners.add(listener);
	}

	public void removeMapListener(final MapListener listener) {
		this.listeners.remove(listener);
	}

	public void finishList(final List<?> List) {
		for (int i = 0; i < List.size(); i++) {
			final Object o = List.get(i);
			((Entity) o).finish();
		}
	}

	public void finish() {
		this.finishList(this.noskilledEntities);
		this.finishList(this.movableEntities);
		this.finishList(this.obstacleEntities);
		this.finishList(this.renderableEntities);
		this.finishList(this.cameraEntities);

		this.allEntities.clear();
		this.noskilledEntities.clear();
		this.movableEntities.clear();
		this.obstacleEntities.clear();
		this.worldObjects.clear();
		this.renderableEntities.clear();
		this.cameraEntities.clear();
	}

	public void clear() {
		this.allEntities.clear();
		this.noskilledEntities.clear();
		this.movableEntities.clear();
		this.obstacleEntities.clear();
		this.worldObjects.clear();
		this.renderableEntities.clear();
		this.cameraEntities.clear();
	}

	public void addEntity(final Entity entity) {
		boolean skilled = false;

		if (entity instanceof Camera) {
			this.cameraEntities.add((Camera) entity);
			return;
		}

		if (entity instanceof Moveable) {
			this.movableEntities.add((Moveable) entity);
			skilled = true;
		}
		if (entity instanceof Obstacle) {
			this.obstacleEntities.add((Obstacle) entity);
			skilled = true;
		}

		if (entity instanceof Obstacle || entity instanceof Moveable) {
			this.worldObjects.add(entity);
		}

		if (entity instanceof Renderable) {

			if (this.renderableEntities.size() == 0) {
				this.renderableEntities.add((Renderable) entity);
			} else {

				adjustRenderableLayer((Renderable) entity);
			}
			skilled = true;
		}

		if (!skilled) {
			this.noskilledEntities.add(entity);
		}

		this.allEntities.add(entity);

		this.entityAdded(entity);
	}

	public void adjustRenderableLayer(Renderable entity) {
		if (renderableEntities.contains(entity)) {
			renderableEntities.set(entity.getLayer(), entity);
		} else {
			boolean added = false;
			// Search for next index to add element
			for (int i = 0; i < this.renderableEntities.size(); i++) {
				final Renderable renderable = this.renderableEntities.get(i);
				if (((Renderable) entity).getLayer() <= renderable.getLayer()) {
					this.renderableEntities.add(i, (Renderable) entity);
					added = true;
					break;
				}
			}

			if (!added) {
				this.renderableEntities.add((Renderable) entity);
			}
		}
	}

	public void removeEntity(final Entity entity) {
		boolean skilled = false;

		if (entity instanceof Camera) {
			if (this.cameraEntities.contains(entity)) {
				this.cameraEntities.remove(entity);
			}
			return;
		}

		if (entity instanceof Moveable) {
			if (this.movableEntities.contains(entity)) {
				this.movableEntities.remove(entity);
				skilled = true;
			}

		}
		if (entity instanceof Obstacle) {
			if (this.obstacleEntities.contains(entity)) {
				this.obstacleEntities.remove(entity);
				skilled = true;
			}
		}

		if (entity instanceof Obstacle || entity instanceof Moveable) {
			if (this.worldObjects.contains(entity)) {
				this.worldObjects.remove(entity);
			}
			if (this.allEntities.contains(entity)) {
				this.allEntities.remove(entity);
			}
		}

		if (entity instanceof Renderable) {
			if (this.renderableEntities.contains(entity)) {
				this.renderableEntities.remove(entity);
				skilled = true;
			}
			if (this.allEntities.contains(entity)) {
				this.allEntities.remove(entity);
				skilled = true;
			}
		}

		if (!skilled) {
			if (noskilledEntities.contains(entity)) {
				this.noskilledEntities.remove(entity);
			}
		}

		this.entityRemoved(entity);
	}

	public void removeEntity(final int fingerprint) {
		final Entity toRemove = this.allEntities.get(fingerprint);
		this.removeEntity(toRemove);

	}

	public List<Renderable> getVisibleRenderable(final int camPosX,
			final int camPosY, final int camWidth, final int camHeight) {

		final List<Renderable> visibles = new ArrayList<Renderable>();

		for (int i = 0; i < this.renderableEntities.size(); i++) {

			final Entity aktEntity = (Entity) this.renderableEntities.get(i);

			if (aktEntity instanceof AlwaysVisible) {
				visibles.add((Renderable) aktEntity);
			} else {

				for (int a = 0; a < aktEntity.getPointList().size(); a++) {
					final EntityPoint entityPoint = aktEntity.getPointList()
							.get(a);
					final Point ol = new Point(camPosX, camPosY);

					final Point ur = new Point(ol.x + camWidth, ol.y
							+ camHeight);
					if (Math2D.isInRect(entityPoint.getCurrentPosition(), ol,
							ur)) {

						visibles.add((Renderable) aktEntity);
						break;
					}
				}
			}

		}

		return visibles;
	}

	/**
	 * Returns a list of entities that are hit by the given point.
	 * 
	 * @param point
	 *            The point to hit entities.
	 * @return The list of entities hit by this point.
	 */
	public List<Entity> getEntityAt(Point point) {
		List<Entity> retList = new ArrayList<Entity>();
		for (int i = 0; i < this.renderableEntities.size(); i++) {
			final Entity aktEntity = (Entity) this.renderableEntities.get(i);
			if (aktEntity.isInMyRect(point)) {
				retList.add(aktEntity);
			}
		}
		return retList;
	}

	/**
	 * Checks whether an entity hits a viewport. The viewport is specified by a
	 * x, y, width and height value.
	 * 
	 * @param entity
	 *            The entity to test.
	 * @param camPosX
	 *            X position of the viewport.
	 * @param camPosY
	 *            Y position of the viewport.
	 * @param camWidth
	 *            Width of the viewport.
	 * @param camHeight
	 *            Height of the viewport.
	 * @return True if the entity hits the viewport. False otherwise.
	 */
	public boolean isVisibleViewport(final Entity entity, final int camPosX,
			final int camPosY, final int camWidth, final int camHeight) {

		if (entity instanceof AlwaysVisible) {
			return true;
		} else {
			for (int a = 0; a < entity.getPointList().size(); a++) {
				final EntityPoint entityPoint = entity.getPointList().get(a);
				final Point ol = new Point(camPosX, camPosY);
				final Point ur = new Point(ol.x + camWidth, ol.y + camHeight);
				if (Math2D.isInRect(entityPoint.getCurrentPosition(), ol, ur)) {
					return true;
				}
			}
		}

		return false;
	}

	public void entityAdded(final Entity entity) {
		final List<MapListener> ls = new ArrayList<MapListener>(this.listeners);
		for (int i = 0; i < ls.size(); i++) {
			final MapListener l = ls.get(i);
			l.entityAdded(entity);
		}
	}

	public void entityRemoved(final Entity entity) {
		final List<MapListener> ls = new ArrayList<MapListener>(this.listeners);
		for (int i = 0; i < ls.size(); i++) {
			final MapListener l = ls.get(i);
			l.entityRemoved(entity);
		}
	}

	public void beforeUpdate() {
		final List<MapListener> ls = new ArrayList<MapListener>(this.listeners);
		for (int i = 0; i < ls.size(); i++) {
			final MapListener l = ls.get(i);
			l.beforeUpdate();
		}
	}

	public void afterUpdate() {

		final List<MapListener> ls = new ArrayList<MapListener>(this.listeners);
		for (int i = 0; i < ls.size(); i++) {
			final MapListener l = ls.get(i);
			l.afterUpdate();
		}
	}

	public void updateWorld() {
		// Fire listener event
		this.beforeUpdate();

		// Build up the camera cache:
		final HashMap<Camera, List<Entity>> cameraCache = new HashMap<Camera, List<Entity>>();
		for (int i = 0; i < this.cameraEntities.size(); i++) {
			final Camera cam = this.cameraEntities.get(i);
			final List<Entity> renderables = new ArrayList<Entity>();
			cameraCache.put(cam, renderables);
		}

		// We go through ALL entities
		for (int i = 0; i < this.allEntities.size(); i++) {
			final Entity entity = this.allEntities.get(i);

			// Calculate the next frame
			if (entity instanceof Moveable) {
				((Moveable) entity).next();
			}

			// Check entity for visibility of cameras:
			if (entity instanceof Renderable) {
				// Check visibility for every camera:
				for (int c = 0; c < this.cameraEntities.size(); c++) {
					// Get the cam
					final Camera cam = this.cameraEntities.get(c);
					// Get the viewport
					final Rectangle view = cam.getViewportRectangle();
					// If there is a viewport, check visibility
					// Do nothing, if no viewport is specified.
					if (view != null) {
						// If visible
						if (this.isVisibleViewport(entity, view.x, view.y,
								view.width, view.height)) {
							// Add to camera cache
							final List<Entity> cache = cameraCache.get(cam);
							cache.add(entity);
						}
					}
				}
			}

		}

		// Fire listener event
		this.afterUpdate();

		// Set cameras entity-subsets:
		for (int i = 0; i < this.cameraEntities.size(); i++) {
			final Camera cam = this.cameraEntities.get(i);
			final List<Entity> capturedObjects = cameraCache.get(cam);
			cam.setCapturedObjects(capturedObjects);
		}

	}

	/**
	 * Use this method to calculate all visible object for every camera without
	 * doing a map update. After calling this method the cameras are able to
	 * render their objects. If you use map updates in your engine, do not call
	 * this method. The map update will do the job instead. Calling this method
	 * while using map updates may cause inefficient map behavior.
	 */
	public void sendVisibleObjectToCameras() {
		// Fire listener event
		this.beforeUpdate();

		// Build up the camera cache:
		final HashMap<Camera, List<Entity>> cameraCache = new HashMap<Camera, List<Entity>>();
		for (int i = 0; i < this.cameraEntities.size(); i++) {
			final Camera cam = this.cameraEntities.get(i);
			final List<Entity> renderables = new ArrayList<Entity>();
			cameraCache.put(cam, renderables);
		}

		// We go through ALL entities
		for (int i = 0; i < this.allEntities.size(); i++) {
			final Entity entity = this.allEntities.get(i);

			// Check entity for visibility of cameras:
			if (entity instanceof Renderable) {
				// Check visibility for every camera:
				for (int c = 0; c < this.cameraEntities.size(); c++) {
					// Get the cam
					final Camera cam = this.cameraEntities.get(c);
					// Get the viewport
					final Rectangle view = cam.getViewportRectangle();
					// If there is a viewport, check visibility
					// Do nothing, if no viewport is specified.
					if (view != null) {
						// If visible
						if (this.isVisibleViewport(entity, view.x, view.y,
								view.width, view.height)) {
							// Add to camera cache
							final List<Entity> cache = cameraCache.get(cam);
							cache.add(entity);
						}
					}
				}
			}

		}

		// Fire listener event
		this.afterUpdate();

		// Set cameras entity-subsets:
		for (int i = 0; i < this.cameraEntities.size(); i++) {
			final Camera cam = this.cameraEntities.get(i);
			final List<Entity> capturedObjects = cameraCache.get(cam);
			cam.setCapturedObjects(capturedObjects);
		}
	}

	public List<Entity> getNoSkilledEntities() {
		return new ArrayList<Entity>(this.noskilledEntities);
	}

	public List<Moveable> getMoveable() {
		return new ArrayList<Moveable>(this.movableEntities);
	}

	public List<Obstacle> getObstacle() {
		return new ArrayList<Obstacle>(this.obstacleEntities);
	}

	public List<Renderable> getRenderable() {
		return new ArrayList<Renderable>(this.renderableEntities);
	}

	public List<Camera> getCameras() {
		return new ArrayList<Camera>(this.cameraEntities);
	}

	public List<Entity> getWorldObjects() {
		return new ArrayList<Entity>(this.worldObjects);
	}

	public void setWorldObjects(final List<Entity> worldObjects) {
		this.worldObjects = worldObjects;
	}

	public List<Entity> getAllEntities() {
		return new ArrayList<Entity>(this.allEntities);
	}

	public void afterDeserialization(final Cobra2DEngine engine) {
		// Save all entities.
		final List<Entity> allEntitiesCopy = this.allEntities;

		// Overwrite allEntites with new clean instance to avoid endless adding
		// of serialized entities.
		// This is important! Imagine what will happen in the Re-add loop if we
		// do not do that.
		this.allEntities = new ArrayList<Entity>();

		// Create all transient instances
		this.listeners = new ArrayList<MapListener>();
		this.noskilledEntities = new ArrayList<Entity>();
		this.movableEntities = new ArrayList<Moveable>();
		this.obstacleEntities = new ArrayList<Obstacle>();
		this.renderableEntities = new ArrayList<Renderable>();
		this.worldObjects = new ArrayList<Entity>();

		// Re-add all entities in new allEntities instance
		for (int i = 0; i < allEntitiesCopy.size(); i++) {
			final Entity e = allEntitiesCopy.get(i);
			this.addEntity(e);
			e.initialize(engine);
		}

		// // Reinitialize all entities:
		// for (int i = 0; i < allEntities.size(); i++) {
		// Entity entity = allEntities.get(i);
		//
		// }

		// Initialize cameras seperately
		for (int i = 0; i < this.cameraEntities.size(); i++) {
			final Camera c = this.cameraEntities.get(i);
			final Entity e = (Entity) c;
			e.initialize(engine);
		}
	}

}
