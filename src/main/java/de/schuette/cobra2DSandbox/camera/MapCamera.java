package de.schuette.cobra2DSandbox.camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;

import de.schuette.cobra2D.benchmark.Benchmark;
import de.schuette.cobra2D.benchmark.Benchmarker;
import de.schuette.cobra2D.controller.Controller;
import de.schuette.cobra2D.controller.ControllerListener;
import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.skills.Camera;
import de.schuette.cobra2D.entity.skills.Renderable;
import de.schuette.cobra2D.math.EntityPoint;
import de.schuette.cobra2D.math.Line;
import de.schuette.cobra2D.math.Math2D;
import de.schuette.cobra2D.math.Point;
import de.schuette.cobra2D.system.Cobra2DEngine;

public class MapCamera extends Entity implements Camera, ControllerListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected List<Entity> capturedObjects;
	protected Rectangle viewport;
	protected boolean fitToResolution;
	private boolean benchmarkEntities;

	@Override
	public void initialize(final Cobra2DEngine engine) {
		super.initialize(engine);
		if (engine.getController() != null) {
			engine.getController().addKeyListener(this);
		}

		final int viewWidth = engine.getRenderer().getResolutionX();
		final int viewHeight = engine.getRenderer().getResolutionY();
		this.viewport = new Rectangle(this.position.getRoundX(), this.position.getRoundY(), viewWidth, viewHeight);

	}

	@Override
	public void render(final Graphics2D bufferGraphics) {

		this.viewport.x = this.position.getRoundX();
		this.viewport.y = this.position.getRoundY();

		final Controller controller = this.engine.getController();
		if (controller != null) {
			if (controller.isKeyPressed(KeyEvent.VK_LEFT)) {
				this.moveLeft();
			} else if (controller.isKeyPressed(KeyEvent.VK_RIGHT)) {
				this.moveRight();
			} else if (controller.isKeyPressed(KeyEvent.VK_UP)) {
				this.moveUp();
			} else if (controller.isKeyPressed(KeyEvent.VK_DOWN)) {
				this.moveDown();
			}
		}

		if (this.capturedObjects != null) {
			final boolean drawEntityLines = this.engine.getRenderer().isDrawEntityLines();
			final boolean drawEntities = this.engine.getRenderer().isDrawEntities();
			final boolean drawEntityPoints = this.engine.getRenderer().isDrawEntityPoints();
			final Benchmarker benchmark = Benchmarker.getInstance();

			Benchmark bench = benchmark.createBenchmarkAndStart("Entities", "Rendern aller Entities der Map Camera");

			for (final Entity entity : this.capturedObjects) {
				if (entity instanceof Renderable) {
					final Renderable renderable = (Renderable) entity;

					if (drawEntities) {
						Benchmark entityBenchmark = null;
						if (isBenchmarkEntities()) {
							/*
							 * Start benchmarking rendering of an entity.
							 */
							StringBuilder sb = new StringBuilder(80);
							sb.append("Render Entity: ");
							sb.append(renderable.getClass().getSimpleName());
							sb.append(" [Texture: ");
							sb.append(entity.getTextureKey());
							sb.append(']');

							final String title = sb.toString();

							sb = new StringBuilder(40);
							sb.append("Rendering of the entity ");
							sb.append(renderable.getClass().getSimpleName());

							final String description = sb.toString();

							entityBenchmark = benchmark.createBenchmarkAndStart(title, description);
						}

						final Point relativePosition = Math2D.getRelativePointTranslation(entity, this.viewport);
						renderable.render(bufferGraphics, relativePosition);

						/*
						 * Stop benchmarking.
						 */
						if (isBenchmarkEntities()) {
							entityBenchmark.stopBenchmark();
						}
					}

					if (drawEntityLines) {
						this.drawEntityLines(entity, this.viewport.x, this.viewport.y, bufferGraphics);
					}
					if (drawEntityPoints) {
						this.drawEntityPoints(entity, this.viewport.x, this.viewport.y, bufferGraphics);
					}

				}
			}

			bench.stopBenchmark();

		}

	}

	public void drawEntityLines(final Entity entity, final int camPosX, final int camPosY, final Graphics2D graphics) {
		final Color old = graphics.getColor();
		graphics.setColor(Color.red);

		for (final Line line : entity.getLineList()) {

			graphics.drawLine((int) line.getX1().x - camPosX, (int) line.getX1().y - camPosY,
					(int) line.getX2().x - camPosX, (int) line.getX2().y - camPosY);
		}
		graphics.setColor(old);

	}

	public void drawEntityPoints(final Entity entity, final int camPosX, final int camPosY, final Graphics2D graphics) {
		final Color old = graphics.getColor();
		graphics.setColor(Color.red);
		final List<EntityPoint> list = entity.getPointList();
		for (int i = 0; i < list.size(); i++) {
			final EntityPoint point = list.get(i);
			graphics.drawOval(point.getCoordinates().getRoundX() - camPosX,
					point.getCoordinates().getRoundY() - camPosY, 5, 5);
		}
		graphics.setColor(old);

	}

	@Override
	public boolean fitToResolution() {
		return this.fitToResolution;
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
		default:
			break;
		}

	}

	public void moveLeft() {
		this.position.translate(-20, 0);
	}

	public void moveRight() {
		this.position.translate(20, 0);
	}

	public void moveUp() {
		this.position.translate(0, -20);
	}

	public void moveDown() {
		this.position.translate(0, 20);
	}

	@Override
	public void keyChar(final char c) {

	}

	public boolean isFitToResolution() {
		return this.fitToResolution;
	}

	public void setFitToResolution(final boolean fitToResolution) {
		this.fitToResolution = fitToResolution;
	}

	public boolean isBenchmarkEntities() {
		return benchmarkEntities;
	}

	public void setBenchmarkEntities(boolean benchmarkEntities) {
		this.benchmarkEntities = benchmarkEntities;
	}

}
