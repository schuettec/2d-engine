package de.schuette.cobra2DSandbox.camera;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import de.schuette.cobra2D.math.Point;
import java.awt.Rectangle;
import java.util.List;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.EntityInitializeException;
import de.schuette.cobra2D.entity.skills.Camera;
import de.schuette.cobra2D.math.Math2D;
import de.schuette.cobra2D.rendering.Renderer;
import de.schuette.cobra2D.system.Cobra2DEngine;
import de.schuette.cobra2DSandbox.camera.widgets.AverageMeter;
import de.schuette.cobra2DSandbox.camera.widgets.EllipseButton;
import de.schuette.cobra2DSandbox.camera.widgets.Meter;
import de.schuette.cobra2DSandbox.camera.widgets.StatusMeter;

public class InfoOverlayCamera extends Entity implements Camera {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final int meterDefaultWidth = 70;
	private final int meterDefaultHeight = 70;

	protected Meter fpsMeter;
	protected Meter upsMeter;
	protected StatusMeter statusMeter;
	protected EllipseButton button;

	@Override
	public void initialize(final Cobra2DEngine engine)
			throws EntityInitializeException {
		super.initialize(engine);
		this.fpsMeter = new AverageMeter(new Dimension(this.meterDefaultWidth,
				this.meterDefaultHeight), "FPS", 0, 100, new Point(0, 0));
		this.upsMeter = new AverageMeter(new Dimension(this.meterDefaultWidth,
				this.meterDefaultHeight), "UPS", 0, 100, new Point(0, 0));
		this.statusMeter = new StatusMeter(new Dimension(
				this.meterDefaultWidth, this.meterDefaultHeight), new Point(0,
				0));

		this.button = new EllipseButton(new Point(300, 300));

	}

	@Override
	public void setCapturedObjects(final List<Entity> capturedObjects) {
	}

	@Override
	public Rectangle getViewportRectangle() {
		return null;
	}

	@Override
	public void render(final Graphics2D graphics) {

		// Calc sizes
		final Renderer renderer = this.engine.getRenderer();
		final int scrWidth = renderer.getWorldViewSize().width;

		// Size of the infopanel
		final int width = this.meterDefaultWidth;
		int x = scrWidth - width - 10;
		int y = 25;

		this.statusMeter.setPosition(new Point(x, y));
		this.statusMeter.drawMeter(graphics, this.engine.isRunning());

		x = scrWidth - 2 * width - 10;
		y = 25;
		this.fpsMeter.setPosition(new Point(x, y));
		this.fpsMeter.drawMeter(graphics, this.engine.getActualFPS());

		x = scrWidth - 3 * width - 10;
		y = 25;
		this.upsMeter.setPosition(new Point(x, y));
		this.upsMeter.drawMeter(graphics, this.engine.getActualUPS());

		final int updateTime = Math2D.saveRound(this.engine.getUpdateTime());
		final int renderTime = Math2D.saveRound(this.engine.getRenderTime());
		this.drawText("Update time: " + updateTime + "ms", graphics, 10, 35);
		this.drawText("Render time: " + renderTime + "ms", graphics, 140, 35);
		this.drawText("FPS: " + this.engine.getActualFPS() + "fps", graphics,
				280, 35);

		// this.button.drawButton(graphics);
	}

	protected void drawText(final String text, final Graphics2D g, final int x,
			final int y) {
		g.setColor(Color.cyan);
		g.drawString(text, x, y);
	}

	@Override
	public boolean fitToResolution() {
		return false;
	}

}
