package de.schuette.cobra2D.rendering;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import de.schuette.cobra2D.math.Point;
import java.awt.Rectangle;
import java.awt.image.VolatileImage;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.schuette.cobra2D.benchmark.Benchmark;
import de.schuette.cobra2D.benchmark.Benchmarker;
import de.schuette.cobra2D.entity.skills.Camera;
import de.schuette.cobra2D.system.Cobra2DEngine;

public class PanelRenderer extends JPanel implements Renderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2790433653469935308L;

	protected VolatileImage worldView;

	protected boolean drawEntityLines = false;
	protected boolean drawEntityPoints = false;
	protected boolean drawEntities = true;
	protected boolean drawEntityCenterPoint = true;

	protected Cobra2DEngine engine;

	protected Benchmarker benchmarkAfterRendering;

	public PanelRenderer() {
		this.setBackground(Color.BLACK);
		this.setDoubleBuffered(true);
		this.setLayout(null);
	}

	@Override
	protected void paintComponent(Graphics gr) {

		// boolean bright = true;
		// boolean startLineBright = true;
		// for (int y = 0; y < this.getSize().height; y += SQUARE_WIDTH) {
		// bright = !startLineBright;
		// startLineBright = !startLineBright;
		//
		// for (int x = 0; x < this.getSize().width; x += SQUARE_WIDTH) {
		//
		// if (bright) {
		// gr.setColor(Color.LIGHT_GRAY);
		// } else {
		// gr.setColor(Color.GRAY);
		// }
		//
		// gr.fillRect(x, y, SQUARE_WIDTH, SQUARE_WIDTH);
		// bright = !bright;
		// }
		// }

		super.paintComponent(gr);

		if (engine != null) {
			Graphics2D worldViewGraphics = (Graphics2D) worldView.getGraphics();
			// Try to avoid senseless
			boolean worldViewChanged = false;

			Benchmarker benchmark = Benchmarker.getInstance();

			/*
			 * Start benchmarking the whole rendering.
			 */
			Benchmark rendercycle = benchmark.createBenchmarkAndStart("Render cycle",
					"Rendering of all elements that will be visible through cameras on the screen.");

			boolean worldViewClear = false;
			/*
			 * Start benchmarking fitting cameras.
			 */
			Benchmark fittingCameras = benchmark.createBenchmarkAndStart("Fitting cameras",
					"Rendering the cameras view resized to worldview buffer.");

			for (int i = 0; i < engine.getMap().getCameras().size(); i++) {
				Camera camera = engine.getMap().getCameras().get(i);
				if (camera.fitToResolution()) {
					Rectangle viewport = camera.getViewportRectangle();
					// Prepare backbuffer for camera view
					final VolatileImage cameraView = RenderToolkit.createVolatileImage(viewport.width, viewport.height);
					Graphics2D camGraphis = (Graphics2D) cameraView.getGraphics();
					camGraphis.setColor(Color.BLACK);
					camGraphis.fillRect(0, 0, viewport.width, viewport.height);

					camera.render(camGraphis);
					camGraphis.dispose();

					// Clear only if the first camera want to render to
					// worldview
					if (!worldViewClear) {
						worldViewGraphics.setColor(Color.BLACK);
						// HINTERGRUND ZEICHNEN
						worldViewGraphics.fillRect(0, 0, worldView.getWidth(), worldView.getHeight());
						worldViewClear = true;
					}

					// Render cameraview to worldView
					RenderToolkit.renderTo(new Point(0, 0), worldViewGraphics, cameraView);
					worldViewChanged = true;

				}
			}
			worldViewGraphics.dispose();

			/*
			 * Stop benchmarking fitting cameras.
			 */
			fittingCameras.stopBenchmark();

			Graphics2D graphics = (Graphics2D) gr;

			// Clear now the screen buffer
			graphics.setColor(Color.BLACK);
			graphics.fillRect(0, 0, getWidth(), getHeight());

			/*
			 * Start benchmarking Render the worldview (that is used to resize to screen
			 * resolution).
			 */
			Benchmark wordView = benchmark.createBenchmarkAndStart("Render world view",
					"Render the world view to backbuffer.");
			// Render worldview to backbuffer only if someone was drawing to it
			if (worldViewChanged) {
				graphics.drawImage(worldView, 0, 0, getWidth(), getHeight(), 0, 0, worldView.getWidth(),
						worldView.getHeight(), null);
			}
			/*
			 * Stop benchmarking the worldview
			 */
			wordView.stopBenchmark();

			/*
			 * Start benchmarking Non-Fitting cameras.
			 */
			Benchmark nonFittingCameras = benchmark.createBenchmarkAndStart("Non-Fitting cameras",
					"Rendering the cameras that will not fit the screen resolution.");
			for (int i = 0; i < engine.getMap().getCameras().size(); i++) {
				Camera camera = engine.getMap().getCameras().get(i);
				if (!camera.fitToResolution()) {
					/*
					 * Start benchmarking rendering of a non-fitting camera.
					 */
					Benchmark cameraBenchmark = benchmark.createBenchmarkAndStart(
							"Camera: " + camera.getClass().getSimpleName(),
							"Rendering the view camera " + camera.getClass().getSimpleName());
					camera.render(graphics);
					/*
					 * Stop benchmarking
					 */
					cameraBenchmark.stopBenchmark();
				}
			}
			/*
			 * Stop benchmarking Non-Fitting cameras.
			 */
			nonFittingCameras.stopBenchmark();

			/*
			 * Start benchmarking Non-Fitting cameras.
			 */
			Benchmark backbuffer = benchmark.createBenchmarkAndStart("Screen buffer flip",
					"Flipping the backbuffer to screen");
			/*
			 * Stop benchmarking the backbuffer flipping.
			 */
			backbuffer.stopBenchmark();

			/*
			 * Stop benchmarking the rendercycle
			 */
			rendercycle.stopBenchmark();

			// Save copy of benchmark
			this.benchmarkAfterRendering = benchmark.getSnapshot();
			benchmark.clear();
		}
	}

	public void setWorldViewSize(Dimension size) {
		worldView = RenderToolkit.createVolatileImage(size.width, size.height);

	}

	@Override
	public Dimension getWorldViewSize() {
		return new Dimension(worldView.getWidth(), worldView.getHeight());
	}

	@Override
	public boolean isDrawEntityLines() {
		return drawEntityLines;
	}

	@Override
	public void setDrawEntityLines(boolean drawEntityLines) {
		this.drawEntityLines = drawEntityLines;
	}

	@Override
	public boolean isDrawEntityPoints() {
		return drawEntityPoints;
	}

	@Override
	public void setDrawEntityPoints(boolean drawEntityPoints) {
		this.drawEntityPoints = drawEntityPoints;
	}

	@Override
	public boolean isDrawEntities() {
		return drawEntities;
	}

	public void setDrawEntities(boolean drawEntities) {
		this.drawEntities = drawEntities;

	}

	@Override
	public Benchmarker getBenchmarker() {
		return benchmarkAfterRendering;
	}

	@Override
	public void initializeRenderer(Cobra2DEngine engine, int resolutionX, int resolutionY, int bitDepth,
			int refreshRate, boolean fullscreen) throws RendererException {
		worldView = RenderToolkit.createVolatileImage(resolutionX, resolutionY);
		this.setSize(new Dimension(resolutionX, resolutionY));
		this.engine = engine;
	}

	@Override
	public boolean isCursorVisible() {
		return true;
	}

	@Override
	public void finish() {

	}

	@Override
	public int getResolutionX() {
		return getSize().width;
	}

	@Override
	public int getResolutionY() {
		return getSize().height;
	}

	@Override
	public void render() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				repaint();
			}
		});

	}

	@Override
	public boolean isDrawEntityCenterPoint() {
		return this.drawEntityCenterPoint;
	}

	@Override
	public void setDrawEntityCenterPoint(boolean b) {
		this.drawEntityCenterPoint = b;
	}
}
