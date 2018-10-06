package de.schuette.cobra2DSandbox.human;

import java.awt.Dimension;
import java.awt.Graphics2D;
import de.schuette.cobra2D.math.Point;
import java.awt.event.KeyEvent;
import java.awt.image.VolatileImage;

import de.schuette.cobra2D.benchmark.Benchmark;
import de.schuette.cobra2D.benchmark.Benchmarker;
import de.schuette.cobra2D.controller.Controller;
import de.schuette.cobra2D.entity.EntityInitializeException;
import de.schuette.cobra2D.entity.skills.Moveable;
import de.schuette.cobra2D.math.Math2D;
import de.schuette.cobra2D.ressource.Animation;
import de.schuette.cobra2D.ressource.AnimationMemory;
import de.schuette.cobra2D.system.Cobra2DEngine;
import de.schuette.cobra2DSandbox.texture.TextureEntity;

public class WalkcycleEntity extends TextureEntity implements Moveable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected transient Animation walkcycle;

	protected int index;

	protected Benchmarker benchmarker = new Benchmarker();
	protected Benchmark stepBench;

	protected int framesProStep = 2;
	protected int stepSpeed = 3;
	protected int turningSpeed = 3;
	protected int currentFrame = 0;

	protected boolean running;
	protected boolean runningLocked;

	// public WalkcycleEntity(final String animationKey, final Point position) {
	// this(animationKey, position, null);
	// }
	//
	// public WalkcycleEntity(final String animationKey, final Point position,
	// final Dimension targetSize) {
	// this(animationKey, position, targetSize, null);
	// }

	// public WalkcycleEntity(final String animationKey, final Point position,
	// final Dimension targetSize, final RGBImageFilter filter) {
	// super(position, targetSize, null, filter);
	// this.position = position;
	// this.animationKey = animationKey;
	// }

	@Override
	public void render(final Graphics2D graphics, final Point position) {

		this.currentFrame++;

		if (this.currentFrame >= this.framesProStep) {
			this.currentFrame = 0;
			this.index++;
		}

		if (this.index >= 16 || !this.running || this.runningLocked) {
			this.index = 0;
		}

		// final BufferedImage img = RenderToolkit.rotateImage(
		// this.walkcycle.getImage(this.index), ;
		VolatileImage img = this.walkcycle.getImage(this.index);
		this.renderTexture(this.getDegrees(), img, graphics, position);

	}

	@Override
	public void next() {
		final Controller controller = this.engine.getController();

		this.running = controller.isKeyPressed(KeyEvent.VK_W)
				|| controller.isKeyPressed(KeyEvent.VK_S);

		if (controller.isKeyPressed(KeyEvent.VK_W)) {
			this.stepSpeed = Math.abs(this.stepSpeed);
		}

		if (controller.isKeyPressed(KeyEvent.VK_S)) {
			this.stepSpeed = -Math.abs(this.stepSpeed);
		}

		if (controller.isKeyPressed(KeyEvent.VK_A)) {
			this.setDegrees(this.getDegrees() - this.turningSpeed);
		}

		if (controller.isKeyPressed(KeyEvent.VK_D)) {
			this.setDegrees(this.getDegrees() + this.turningSpeed);
		}

		if (this.running && !this.runningLocked) {
			final Point newPos = Math2D.getCircle(this.getPosition(),
					this.stepSpeed, this.getDegrees());
			this.setPosition(newPos);
		}

	}

	/**
	 * @return Returns true if this walkcycle is allowed to move.
	 */
	public boolean isRunningLocked() {
		return this.runningLocked;
	}

	/**
	 * @param run
	 *            Sets if this walkcycle is allowed to move.
	 */
	public void setRunningLocked(final boolean lock) {
		this.runningLocked = lock;
	}

	@Override
	public void initialize(final Cobra2DEngine engine)
			throws EntityInitializeException {
		super.initialize(engine);

		final AnimationMemory animations = engine.getAnimationMemory();
		this.walkcycle = animations.getAnimation(this.textureKey);

		// Convert animation memory
		this.walkcycle.setFilter(this.filter);
		this.setSize(new Dimension(this.walkcycle.getWidth(), this.walkcycle
				.getHeight()));

		this.index = 0;

		this.runningLocked = false;

	}

}
