package de.schuette.cobra2DSandbox.fx;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.VolatileImage;
import java.util.Arrays;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.EntityInitializeException;
import de.schuette.cobra2D.entity.skills.Renderable;
import de.schuette.cobra2D.math.EntityPoint;
import de.schuette.cobra2D.math.Math2D;
import de.schuette.cobra2D.math.Point;
import de.schuette.cobra2D.rendering.GradientTransparencyFilter;
import de.schuette.cobra2D.rendering.RenderToolkit;
import de.schuette.cobra2D.system.Cobra2DEngine;

/**
 * @author Chris Defines a flash entity that renders a flash between a start and
 *         end point. Because rendering a single flash with blurring and glow
 *         effects is to time expensive, this implementation pre-renders a
 *         number of images. In the render method only one flash is displayed
 *         and a second flash is fading out in the background. After iterating
 *         over all pre-rendered images, a thread is spawn outside the render
 *         thread to pre-render one image and replace it in the backbuffers.
 *         This makes the flash look a bit more randomly. Another strategy for
 *         more randomness is to mirror the backbuffers randomly before
 *         rendering them to screen.
 */
public class Flash extends Entity implements Renderable {

	private static final int DEFAULT_WIDTH = 400;

	public static final int DEFAULT_SPLITS = 5;

	private static final int DEFAULT_DISTORTION = 30;

	private static final Color LIGHTNING_COLOR = Color.WHITE;

	private static final Color GLOW_COLOR = Color.CYAN;

	private static final Color TRANSPARENCY_COLOR = Color.MAGENTA;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Point start;
	protected Point end;
	protected int precalcCount = 15;
	protected int maxSplits = Flash.DEFAULT_SPLITS;
	protected int maxDistortion = Flash.DEFAULT_DISTORTION;

	protected int splits;
	protected int distortion;

	boolean reflect = false;
	protected float alpha = 1f;
	protected float lastAlpha = 0f;

	protected int currentBuffer = 0;
	protected int toReplaceBuffer = 0;
	protected int lastBufferIndex = 0;

	protected transient VolatileImage[] glowBackbuffer;
	protected transient VolatileImage[] lightBackbuffer;

	private final int width = DEFAULT_WIDTH;

	/**
	 * Constructs a flash between start and end point. Please specify the maximal
	 * distance between start and end point to improve the quality of the graphics.
	 * 
	 * @param start
	 * @param end
	 * @param maxwidth
	 */
	// public Flash(final Point start, final Point end, final int maxwidth) {
	// this.start = start;
	// this.end = end;
	// this.width = maxwidth;
	// }

	protected void randomizeParameters() {
		this.splits = Math2D.saveRound(Math2D.random(5, this.maxSplits));
		this.distortion = Math2D.saveRound(Math2D.random(10, this.maxDistortion));
	}

	@Override
	public void render(final Graphics2D graphics, final Point position) {
		this.renderToPoint(graphics, this.glowBackbuffer[this.currentBuffer], this.alpha, this.start, this.end,
				this.reflect);
		this.renderToPoint(graphics, this.lightBackbuffer[this.currentBuffer], this.alpha, this.start, this.end,
				this.reflect);

		if (this.lastAlpha < 1f && this.lastAlpha >= 0) {
			this.renderToPoint(graphics, this.glowBackbuffer[this.lastBufferIndex], this.lastAlpha, this.start,
					this.end, this.reflect);
			this.renderToPoint(graphics, this.lightBackbuffer[this.lastBufferIndex], this.lastAlpha, this.start,
					this.end, this.reflect);
		}

		this.alpha -= 0.2f;
		this.lastAlpha -= 0.1f;

		if (this.alpha < 0.5) {
			this.lastBufferIndex = this.currentBuffer;
			this.lastAlpha = this.alpha;
			this.alpha = 1f;
			this.currentBuffer++;
			// Define reflect attribute this frame cycle
			final int random = Math2D.saveRound(Math2D.random(0, 1000));
			if (random % 2 == 0) {
				this.reflect = true;
			}
		}

		if (this.currentBuffer >= this.precalcCount) {
			this.currentBuffer = 0;
			this.createFlashInThread();
		}

	}

	protected void createFlashInThread() {
		final Thread background = new Thread(new Runnable() {

			@Override
			public void run() {
				// int random = Math2D.random(0, 100) % precalcCount;
				final Point[] points = Flash.this.calcPoints();
				Flash.this.glowBackbuffer[Flash.this.toReplaceBuffer] = Flash.this.createGlowBackbuffer(points);
				Flash.this.lightBackbuffer[Flash.this.toReplaceBuffer] = Flash.this.createLightningBackbuffer(points);
				Flash.this.toReplaceBuffer++;
				if (Flash.this.toReplaceBuffer >= Flash.this.precalcCount) {
					Flash.this.toReplaceBuffer = 0;
				}
			}
		});
		background.start();
	}

	/**
	 * Renders the given image to a calculated position so that the content of the
	 * texture, hits the from-point with its start-point and the to-point with is
	 * end-point. The start-point is a virtual point within the texture and is
	 * located at the point (0, height/2). The endpoint is a virtual point within
	 * the texture located at (width, height/2). Every content between start and end
	 * will be rotated and resized to match the given from and to point.
	 * 
	 * @param g
	 *            The graphics object to render to
	 * @param img
	 *            The texture.
	 * @param from
	 *            The from point in the target image.
	 * @param to
	 *            The to point in the target image.
	 * @param reflectRandomly
	 *            Makes the method render the image in the buffer reflected, so the
	 *            flash looks more randomly
	 */

	protected void renderToPoint(final Graphics2D g, final VolatileImage img, final float alpha, final Point from,
			final Point to, final boolean reflect) {
		// Rotate correctly
		final double angle = Math2D.getAngle(from, to);

		final int distance = Math2D.saveRound(Math2D.getEntfernung(from, to));

		// g.setColor(Color.BLUE);
		// g.drawRect(from.x, from.y, distance, distance);

		final Point flashMiddlePoint = new Point(Math2D.saveRound(from.x + distance / 2.0),
				Math2D.saveRound(from.y + distance / 2.0));
		final Point flashStart = Math2D.getCircle(flashMiddlePoint, Math2D.saveRound(distance / 2.0), angle + 180);

		final double xCorrected = from.x - (flashStart.x - from.x);
		final double yCorrected = from.y - (flashStart.y - from.y);
		final Point corrected = new Point(xCorrected, yCorrected);

		// g.fillOval(flashMiddlePoint.x, flashMiddlePoint.y, 5, 5);
		// g.fillOval(flashStart.x, flashStart.y, 5, 5);
		//
		// g.setColor(Color.GREEN);
		// g.fillOval(corrected.x, corrected.y, 5, 5);

		// Render to correct position resized
		// BufferedImage rotated;
		if (reflect) {
			// Turn it with 180 degrees to make it look more randomly
			// rotated = RenderToolkit.rotateImage(img, angle + 180);
			RenderToolkit.renderTo(alpha, angle + 180, corrected, new Dimension(distance, distance), g, img);
		} else {
			RenderToolkit.renderTo(alpha, angle, corrected, new Dimension(distance, distance), g, img);
		}

	}

	public VolatileImage createLightningBackbuffer(final Point[] points) {
		// Lightning Part
		final VolatileImage lightBuffer = RenderToolkit.createVolatileImage(this.size.width + 20, this.size.width + 20);
		final Graphics2D g = (Graphics2D) lightBuffer.getGraphics();
		g.setColor(Flash.TRANSPARENCY_COLOR);
		g.fillRect(0, 0, lightBuffer.getWidth(), lightBuffer.getHeight());

		this.drawPoints(g, points, Flash.LIGHTNING_COLOR, 2);

		// Target buffer used to render the blurre glowBuffer
		VolatileImage targetBuffer = RenderToolkit.createVolatileImage(this.size.width + 20, this.size.width + 20);
		final Graphics2D target = (Graphics2D) targetBuffer.getGraphics();
		target.setColor(Flash.TRANSPARENCY_COLOR);
		target.fillRect(0, 0, targetBuffer.getWidth(), targetBuffer.getHeight());
		RenderToolkit.renderTo(2, new Point(0, 0), target, lightBuffer);

		// Backbuffer
		targetBuffer = RenderToolkit.convertSpriteToTransparentSprite(targetBuffer,
				new GradientTransparencyFilter(Flash.LIGHTNING_COLOR, Flash.TRANSPARENCY_COLOR));
		return targetBuffer;
	}

	public VolatileImage createGlowBackbuffer(final Point[] points) {

		// Glowpart
		final VolatileImage glowBuffer = RenderToolkit.createVolatileImage(this.size.width + 20, this.size.width + 20);
		final Graphics2D g = (Graphics2D) glowBuffer.getGraphics();
		g.setColor(Flash.TRANSPARENCY_COLOR);
		g.fillRect(0, 0, glowBuffer.getWidth(), glowBuffer.getHeight());

		this.drawPoints(g, points, Flash.GLOW_COLOR, 8);

		// Target buffer used to render the blurre glowBuffer
		VolatileImage targetBuffer = RenderToolkit.createVolatileImage(this.size.width + 20, this.size.width + 20);
		final Graphics2D target = (Graphics2D) targetBuffer.getGraphics();
		target.setColor(Flash.TRANSPARENCY_COLOR);
		target.fillRect(0, 0, targetBuffer.getWidth(), targetBuffer.getHeight());
		RenderToolkit.renderTo(20, new Point(0, 0), target, glowBuffer);

		// Backbuffer
		targetBuffer = RenderToolkit.convertSpriteToTransparentSprite(targetBuffer,
				new GradientTransparencyFilter(Flash.GLOW_COLOR, Flash.TRANSPARENCY_COLOR));
		return targetBuffer;
	}

	protected void drawPoints(final Graphics2D g, final Point[] points, final Color color, final int strokeWidth) {
		for (int i = 1; i < points.length; i++) {
			this.drawSegment(g, color, points[i - 1], points[i], strokeWidth);
		}
	}

	protected Point[] calcPoints() {
		this.randomizeParameters();

		final double degrees = 0;// Math2D.getAngle(start, end);
		// WorldCoordinates to bufferCoordinates
		final Point start = new Point(10, Math2D.saveRound(this.size.width / 2.0));
		final Point end = new Point(this.size.width, Math2D.saveRound(this.size.width / 2.0));
		// Direction
		final double distance = Math2D.getEntfernung(start, end);

		final int pointCount = Math2D.saveRound(distance / 100 * this.splits);

		final Point[] points = new Point[pointCount];
		final double[] distances = this.getRandomSplitting(0, Math2D.saveRound(distance), pointCount);

		points[0] = start;
		points[pointCount - 1] = end;

		for (int i = 1; i < points.length - 1; i++) {
			points[i] = Math2D.getCircle(start, distances[i], degrees);
			final double distort = Math2D.random(-this.distortion, +this.distortion);
			points[i] = Math2D.getCircle(points[i], distort, degrees + 90);
		}

		return points;
	}

	protected double[] getRandomSplitting(final int min, final int max, final int splits) {
		final double[] numbers = new double[splits];
		numbers[0] = min;
		for (int i = 1; i < numbers.length; i++) {
			numbers[i] = Math2D.random(min, max);
		}
		Arrays.sort(numbers);
		return numbers;

	}

	private void drawSegment(final Graphics2D graphics, final Color color, final Point lastPoint, final Point next,
			final int strokeWidth) {
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(strokeWidth));
		graphics.drawLine(lastPoint.getRoundX(), lastPoint.getRoundY(), next.getRoundX(), next.getRoundY());
	}

	@Override
	public int getLayer() {
		return 500;
	}

	@Override
	public void initialize(final Cobra2DEngine engine) throws EntityInitializeException {

		if (start == null || end == null) {
			throw new EntityInitializeException("Start and end attributs cannot be empty.");
		}

		super.initialize(engine);

		this.setPosition(this.start);

		// it is important that the size is quadratic
		this.setSize(new Dimension(this.width, this.width));

		// Create entity points to make flash visible
		hull.clear();
		final EntityPoint p1 = new EntityPoint(0, 0);
		p1.setByPosition(this.start);
		hull.add(p1);

		final EntityPoint p2 = new EntityPoint(0, 0);
		p2.setByPosition(this.end);
		hull.add(p2);

		// Prerender backbuffers
		if (this.glowBackbuffer == null || this.lightBackbuffer == null) {
			this.glowBackbuffer = new VolatileImage[this.precalcCount];
			this.lightBackbuffer = new VolatileImage[this.precalcCount];
			for (int i = 0; i < this.precalcCount; i++) {
				final Point[] points = this.calcPoints();
				this.glowBackbuffer[i] = this.createGlowBackbuffer(points);
				this.lightBackbuffer[i] = this.createLightningBackbuffer(points);
			}
		}

	}

	public Point getStart() {
		return this.start;
	}

	public void setStart(final Point start) {
		this.start = start;
	}

	public Point getEnd() {
		return this.end;
	}

	public void setEnd(final Point end) {
		this.end = end;
	}

	@Override
	public void setPosition(Point position) {
		double diffX = end.x - start.x;
		double diffY = end.y - start.y;

		super.setPosition(position);
		this.start = position;
		this.end = new Point(position.getRoundX() + diffX, position.getRoundY() + diffY);
	}

	public int getMaxSplits() {
		return this.maxSplits;
	}

	public void setMaxSplits(final int maxSplits) {
		this.maxSplits = maxSplits;
	}

	public int getMaxDistortion() {
		return this.maxDistortion;
	}

	public void setMaxDistortion(final int maxDistortion) {
		this.maxDistortion = maxDistortion;
	}

}
