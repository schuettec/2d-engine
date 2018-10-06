package de.schuette.cobra2DSandbox.fx;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.RGBImageFilter;
import java.awt.image.VolatileImage;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.EntityInitializeException;
import de.schuette.cobra2D.entity.skills.Renderable;
import de.schuette.cobra2D.math.Math2D;
import de.schuette.cobra2D.math.Parabel;
import de.schuette.cobra2D.rendering.GradientTransparencyFilter;
import de.schuette.cobra2D.rendering.RenderToolkit;
import de.schuette.cobra2D.system.Cobra2DEngine;

public class Smoke extends Entity implements Renderable {
	/**
	 * 
	 */
	protected static final long serialVersionUID = 1L;

	protected static final Color SMOKE_COLOR = Color.BLACK;
	protected static final Color TRANSPARENCY_COLOR = Color.MAGENTA;

	protected int particleCount; // Cannot be modified after creation
	protected int maxPositionDistortion = 5;

	protected transient int[] distance; // Cannot be modified after creation
	protected transient double[] angle; // Cannot be modified after creation
	protected transient int[] distortionOffset; // Cannot be modified after
												// creation

	protected transient Parabel alphaParabel;// Cannot be modified after
												// creation
	protected int maxRadius = 150;// Cannot be modified after creation

	protected RGBImageFilter filter;

	// public Smoke(final String texturekey, final Point position) {
	// this(texturekey, position, 5, 150);
	// }
	//
	// public Smoke(final String texturekey, final Point position,
	// final int particelCount, final int radius) {
	// this(texturekey, position, particelCount, radius, null);
	// }

	// public Smoke(final String texturekey, final Point position,
	// final int particelCount, final int radius, RGBImageFilter filter) {
	// this.setPosition(position);
	// this.textureKey = texturekey;
	// this.particleCount = particelCount;
	// this.maxRadius = radius;
	// this.filter = filter;
	// }

	public int getParticleCount() {
		return particleCount;
	}

	public void setParticleCount(int particleCount) {
		this.particleCount = particleCount;
	}

	public int getMaxRadius() {
		return maxRadius;
	}

	public void setMaxRadius(int maxRadius) {
		this.maxRadius = maxRadius;
	}

	@Override
	public void render(final Graphics2D graphics, final Point position) {
		// graphics.setColor(Color.BLUE);
		// graphics.fillOval(position.x, position.y, 5, 5);

		for (int i = particleCount - 1; i >= 0; i--) {

			final Point distoredPosition = new Point(position.x
					- distortionOffset[i], position.y - distortionOffset[i]);

			final float alpha = (float) this.alphaParabel
					.getValue(this.distance[i]);

			// final BufferedImage img = RenderToolkit.rotateImage(
			// this.getImage(), this.angle[i]);
			VolatileImage img = getImage();

			final Point renderPoint = Math2D.getCircle(distoredPosition,
					this.distance[i], 225);
			// graphics.setColor(Color.RED);
			// graphics.fillOval(renderPoint.x, renderPoint.y, 5, 5);
			//
			final Point sizePoint = Math2D.getCircle(distoredPosition,
					this.distance[i], 45);
			// graphics.setColor(Color.GREEN);
			// graphics.fillOval(sizePoint.x, sizePoint.y, 5, 5);

			// Dimension newDim = new Dimension(distance[i] * 2, distance[i] *
			// 2);
			final Dimension newDim = new Dimension(sizePoint.x - renderPoint.x,
					sizePoint.y - renderPoint.y);

			RenderToolkit.renderTo(alpha, angle[i], renderPoint, newDim,
					graphics, img);
		}

		this.moveCloud();
	}

	protected void moveCloud() {
		for (int i = 0; i < this.particleCount; i++) {
			// angle[i] = angle[i] + 1;
			this.distance[i]++;
			if (this.distance[i] >= this.maxRadius) {
				this.angle[i] = Math2D.random(180, 270);
				this.distance[i] = 0;
			}
		}
	}

	protected void calculateCloud() {
		this.distance = new int[this.particleCount];
		this.angle = new double[this.particleCount];
		this.distortionOffset = new int[this.particleCount];
		for (int i = 0; i < this.particleCount; i++) {
			// Initialize randomly
			this.calculateParticle(i);
		}
	}

	protected void calculateParticle(final int index) {
		this.angle[index] = Math2D.random(180, 270);
		this.distance[index] = Math2D.saveRound(index
				* (this.maxRadius / (double) this.particleCount));
		this.distortionOffset[index] = Math2D.random(0, maxPositionDistortion);
	}

	@Override
	public void initialize(final Cobra2DEngine engine)
			throws EntityInitializeException {
		super.initialize(engine);
		this.engine = engine;
		this.createRectangleEntityPointsWithPositionInCenter();

		VolatileImage copyImage = RenderToolkit.createVolatileImage(getImage()
				.getWidth(), getImage().getHeight());
		// Render the image to the copy but blurred
		RenderToolkit.renderTo(5, new Point(0, 0),
				(Graphics2D) copyImage.getGraphics(), getImage());

		if (filter != null) {
			this.setImage(RenderToolkit.convertSpriteToTransparentSprite(
					copyImage, filter));
		} else {
			this.setImage(RenderToolkit.convertSpriteToTransparentSprite(
					copyImage, new GradientTransparencyFilter(
							Smoke.SMOKE_COLOR, Smoke.TRANSPARENCY_COLOR)));
		}

		this.alphaParabel = new Parabel(new Point2D.Double(0, 0),
				new Point2D.Double(this.maxRadius / 2.0, 1), true);

		this.calculateCloud();
	}

	@Override
	public int getLayer() {
		return 103;
	}

	public void setFilter(GradientTransparencyFilter filter) {
		this.filter = filter;
	}

	public RGBImageFilter getFilter() {
		return filter;
	}

	public int getMaxPositionDistortion() {
		return maxPositionDistortion;
	}

	public void setMaxPositionDistortion(int maxPositionDistortion) {
		this.maxPositionDistortion = maxPositionDistortion;
	}

}
