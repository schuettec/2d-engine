package de.schuette.cobra2DSandbox.camera;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;

import de.schuette.cobra2D.controller.ControllerListener;
import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.skills.Camera;
import de.schuette.cobra2D.system.Cobra2DEngine;

/**
 * @author Chris This camera defines a simple camera with a variable viewport
 *         size. Set the viewport size dynamically on this camera. If no
 *         viewport was set the camera is using the default viewport size from
 *         the engine. This camera can also capture an existing entity on the
 *         map.
 */
public class ViewportCamera extends Entity implements Camera,
		ControllerListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Entity> capturedObjects;
	private Rectangle viewport;
	private Entity capture;
	private boolean fitToResolution;
	private boolean drawCenterPoint;

	@Override
	public void initialize(final Cobra2DEngine engine) {
		super.initialize(engine);
		if (engine.getController() != null) {
			engine.getController().addKeyListener(this);
		}

		// Initialize with engine's default viewport size if nothing was set
		// before
		if (viewport == null) {
			final int viewWidth = engine.getRenderer().getResolutionX();
			final int viewHeight = engine.getRenderer().getResolutionY();
			this.viewport = new Rectangle(0, 0, viewWidth, viewHeight);
		}
	}

	@Override
	public void render(final Graphics2D bufferGraphics) {

		if (this.capture != null) {
			this.viewport.x = (int) Math.round(this.capture.getCenterPoint().x
					- (viewport.width / 2.0));
			this.viewport.y = (int) Math.round(this.capture.getCenterPoint().y
					- (viewport.height / 2.0));
		}

		if (this.capturedObjects != null) {
			for (final Entity entity : this.capturedObjects) {
				CameraUtil.renderAllDefault(entity, bufferGraphics, viewport,
						engine);
			}

		}

	}

	@Override
	public boolean fitToResolution() {
		return fitToResolution;
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

	public void setViewportRectangle(Rectangle viewport) {
		this.viewport = viewport;
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

	public void setCapture(Entity capture) {
		this.capture = capture;
	}

	public Entity getCapture() {
		return capture;
	}

	public boolean isFitToResolution() {
		return fitToResolution;
	}

	public void setFitToResolution(boolean fitToResolution) {
		this.fitToResolution = fitToResolution;
	}

	public boolean isDrawCenterPoint() {
		return drawCenterPoint;
	}

	public void setDrawCenterPoint(boolean drawCenterPoint) {
		this.drawCenterPoint = drawCenterPoint;
	}

}