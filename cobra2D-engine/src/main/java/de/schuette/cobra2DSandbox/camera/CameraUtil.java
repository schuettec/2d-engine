package de.schuette.cobra2DSandbox.camera;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.EntityPoint;
import de.schuette.cobra2D.entity.skills.Renderable;
import de.schuette.cobra2D.math.Line;
import de.schuette.cobra2D.math.Math2D;
import de.schuette.cobra2D.system.Cobra2DEngine;

public class CameraUtil {
	/**
	 * Renders an entity to a buffer graphics object. This is the default render
	 * routine of a camera that supports the drawing of entity lines and points.
	 * 
	 * @param entity
	 *            The entity to render
	 * @param bufferGraphics
	 *            The buffer graphics given by the render routine
	 * @param viewport
	 *            The viewport of the camera.
	 * @param engine
	 *            An instanstance of the engine to render to.
	 */
	public static void renderAllDefault(Entity entity,
			Graphics2D bufferGraphics, Rectangle viewport, Cobra2DEngine engine) {
		if (entity instanceof Renderable) {
			final Renderable renderable = (Renderable) entity;
			if (engine.getRenderer().isDrawEntities()) {
				final Point relativePosition = Math2D
						.getRelativePointTranslation(entity, viewport);
				renderable.render(bufferGraphics, relativePosition);
			}
			if (engine.getRenderer().isDrawEntityLines()) {
				CameraUtil.drawEntityLines(entity, viewport.x, viewport.y,
						bufferGraphics);
			}
			if (engine.getRenderer().isDrawEntityPoints()) {
				CameraUtil.drawEntityPoints(entity, viewport.x, viewport.y,
						bufferGraphics);
			}

			if (engine.getRenderer().isDrawEntityCenterPoint()) {
				CameraUtil.drawCenterPoint(entity, viewport.x, viewport.y,
						bufferGraphics);
			}

		}
	}

	/**
	 * Default routine to draw entity lines to a camera's view.
	 * 
	 * @param entity
	 *            The entity to render
	 * @param camPosX
	 *            X coordinate of the camera viewport
	 * @param camPosY
	 *            Y coordinate of the camera viewport
	 * @param graphics
	 *            The graphics object to render to
	 */
	public static void drawEntityLines(final Entity entity, final int camPosX,
			final int camPosY, final Graphics2D graphics) {
		// final Color old = graphics.getColor();
		// graphics.setColor(Color.red);

		for (final Line line : entity.getLineList()) {

			graphics.drawLine((int) line.getX1().x - camPosX,
					(int) line.getX1().y - camPosY, (int) line.getX2().x
							- camPosX, (int) line.getX2().y - camPosY);
		}
		// graphics.setColor(old);

	}

	/**
	 * Default routine to draw entity points to a camera's view.
	 * 
	 * @param entity
	 *            The entity to render
	 * @param camPosX
	 *            X coordinate of the camera viewport
	 * @param camPosY
	 *            Y coordinate of the camera viewport
	 * @param graphics
	 *            The graphics object to render to
	 */
	public static void drawEntityPoints(final Entity entity, final int camPosX,
			final int camPosY, final Graphics2D graphics) {
		// final Color old = graphics.getColor();
		// graphics.setColor(Color.red);
		for (final EntityPoint point : entity.getPointList()) {
			graphics.fillOval(point.getCurrentPosition().x - camPosX - 3,
					point.getCurrentPosition().y - 3 - camPosY, 6, 6);
		}
		// graphics.setColor(old);

	}

	/**
	 * Default routine to draw entity's center point to the camera's view.
	 * 
	 * @param entity
	 *            The entity to render
	 * @param camPosX
	 *            X coordinate of the camera viewport
	 * @param camPosY
	 *            Y coordinate of the camera viewport
	 * @param graphics
	 *            The graphics object to render to
	 */
	public static void drawCenterPoint(final Entity entity, final int camPosX,
			final int camPosY, final Graphics2D graphics) {
		// final Color old = graphics.getColor();
		// graphics.setColor(Color.CYAN);
		Point center = entity.getCenterPoint();
		graphics.fillOval(center.x - camPosX - 3, center.y - 3 - camPosY, 6, 6);
		// graphics.setColor(old);

	}
}
