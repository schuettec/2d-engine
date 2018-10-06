package de.schuette.cobra2DSandbox.camera;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.File;

import de.schuette.cobra2D.controller.Controller;
import de.schuette.cobra2D.system.Cobra2DEngine;
import de.schuette.cobra2DSandbox.camera.widgets.TextDisplay;

public class AdvancedMapCamera extends MapCamera {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected boolean drawBenchmark;
	protected transient TextDisplay benchmarkDisplay;
	protected transient String benchmarkSummary;

	@Override
	public void initialize(final Cobra2DEngine engine) {
		super.initialize(engine);
		this.drawBenchmark = false;
		this.benchmarkDisplay = new TextDisplay(new Point(0, 30));
	}

	@Override
	public void render(final Graphics2D bufferGraphics) {
		super.render(bufferGraphics);

		/*
		 * Draw Benchmark to screen
		 */
		if (this.benchmarkSummary == null) {
			this.drawBenchmark = false;
		}
		if (this.drawBenchmark) {
			this.benchmarkDisplay.drawTextdisplay(this.benchmarkSummary,
					bufferGraphics);
		}

		final Controller controller = this.engine.getController();
		if (controller != null) {
			if (controller.isKeyPressed(KeyEvent.VK_ADD)) {
				this.increaseViewport();
			} else if (controller.isKeyPressed(KeyEvent.VK_SUBTRACT)) {
				this.decreaseViewport();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.schuette.cobra2DSandbox.entities.camera.MapCamera#keyPressed(int)
	 */
	@Override
	public void keyPressed(final int keyCode) {
		super.keyPressed(keyCode);
		switch (keyCode) {
		case KeyEvent.VK_P:
			if (this.engine.isRunning()) {
				this.engine.pauseEngine();
				// Render one frame to display status
				this.engine.getRenderer().render();
				// engine.setMapUpdate(false);
			} else {
				this.engine.resumeEngine();
				// engine.setMapUpdate(true);
			}
			break;
		case KeyEvent.VK_S:
			this.saveLevelSnapshot();
			break;
		case KeyEvent.VK_L:
			this.loadLevelSnapshot();
			break;
		case KeyEvent.VK_F1:
			this.drawBenchmark = !this.drawBenchmark;
			this.benchmarkSummary = this.engine.getBenchmarker()
					.getBenchmarkSummary();
			System.out.println(this.benchmarkSummary);
			break;
		default:
			break;
		}

	}

	private void loadLevelSnapshot() {
		final String home = System.getProperty("user.home");
		this.engine.loadLevel(new File(home + File.separator + "level.xml"));
	}

	private void saveLevelSnapshot() {
		final String home = System.getProperty("user.home");
		this.engine.saveLevel(new File(home + File.separator + "level.xml"));
	}

	public void increaseViewport() {
		final double factor = 1.02d;
		this.viewport.width = (int) Math.round(factor * this.viewport.width);
		this.viewport.height = (int) Math.round(factor * this.viewport.height);
	}

	public void decreaseViewport() {
		final double factor = 0.98d;
		this.viewport.width = (int) Math.round(factor * this.viewport.width);
		this.viewport.height = (int) Math.round(factor * this.viewport.height);
	}

}
