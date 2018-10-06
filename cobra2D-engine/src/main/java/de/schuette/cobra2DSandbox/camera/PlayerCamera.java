package de.schuette.cobra2DSandbox.camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;

import de.schuette.cobra2D.controller.ControllerListener;
import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.EntityPoint;
import de.schuette.cobra2D.entity.skills.Camera;
import de.schuette.cobra2D.entity.skills.Renderable;
import de.schuette.cobra2D.math.Line;
import de.schuette.cobra2D.math.Math2D;
import de.schuette.cobra2D.system.Cobra2DEngine;

public class PlayerCamera extends Entity implements Camera, ControllerListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Entity> capturedObjects;
	private Rectangle viewport;
	private Entity player;

	@Override
	public void initialize(final Cobra2DEngine engine) {
		super.initialize(engine);
		if (engine.getController() != null) {
			engine.getController().addKeyListener(this);
		}

		final int viewWidth = engine.getRenderer().getResolutionX();
		final int viewHeight = engine.getRenderer().getResolutionY();

		this.viewport = new Rectangle(0, 0, viewWidth, viewHeight);
	}

	@Override
	public void render(final Graphics2D bufferGraphics) {

		if (this.player != null) {
			this.viewport.x = (int) Math
					.round(this.player.getCenterPoint().x
							- (this.engine.getRenderer().getWorldViewSize().width / 2.0));
			this.viewport.y = (int) Math
					.round(this.player.getCenterPoint().y
							- (this.engine.getRenderer().getWorldViewSize().height / 2.0));
		}

		if (this.capturedObjects != null) {
			for (final Entity entity : this.capturedObjects) {

				if (entity instanceof Renderable) {
					final Renderable renderable = (Renderable) entity;
					if (this.engine.getRenderer().isDrawEntities()) {
						final Point relativePosition = Math2D
								.getRelativePointTranslation(entity,
										this.viewport);
						renderable.render(bufferGraphics, relativePosition);
					}
					if (this.engine.getRenderer().isDrawEntityLines()) {
						this.drawEntityLines(entity, this.viewport.x,
								this.viewport.y, bufferGraphics);
					}
					if (this.engine.getRenderer().isDrawEntityPoints()) {
						this.drawEntityPoints(entity, this.viewport.x,
								this.viewport.y, bufferGraphics);
					}

				}
			}

		}

	}

	public void drawEntityLines(final Entity entity, final int camPosX,
			final int camPosY, final Graphics2D graphics) {
		final Color old = graphics.getColor();
		graphics.setColor(Color.red);

		for (final Line line : entity.getLineList()) {

			graphics.drawLine((int) line.getX1().x - camPosX,
					(int) line.getX1().y - camPosY, (int) line.getX2().x
							- camPosX, (int) line.getX2().y - camPosY);
		}
		graphics.setColor(old);

	}

	public void drawEntityPoints(final Entity entity, final int camPosX,
			final int camPosY, final Graphics2D graphics) {
		final Color old = graphics.getColor();
		graphics.setColor(Color.red);
		for (final EntityPoint point : entity.getPointList()) {
			graphics.drawOval(point.getCurrentPosition().x - camPosX,
					point.getCurrentPosition().y - camPosY, 5, 5);
		}
		graphics.setColor(old);

	}

	@Override
	public boolean fitToResolution() {
		return true;
	}

	@Override
	public void finish() {

	}

	@Override
	public void setCapturedObjects(final List<Entity> capturedObjects) {
		this.capturedObjects = capturedObjects;
	}

	@Override
	public Rectangle getViewportRectangle() {
		return this.viewport;
	}

	@Override
	public void keyTyped(final int keyCode) {

	}

	@Override
	public void keyReleased(final int keyCode) {

	}

	@Override
	public void keyPressed(final int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_ESCAPE:
			this.engine.shutdownEngine();
			break;
		case KeyEvent.VK_P:
			if (this.engine.isMapUpdate()) {
				this.engine.setMapUpdate(false);
			} else {
				this.engine.setMapUpdate(true);
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void keyChar(final char c) {

	}

	public void setPlayer(Entity player) {
		this.player = player;
	}

	public Entity getPlayer() {
		return player;
	}
}
