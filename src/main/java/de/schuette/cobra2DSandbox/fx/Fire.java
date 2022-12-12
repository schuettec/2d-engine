package de.schuette.cobra2DSandbox.fx;

import java.awt.Color;
import java.awt.Dimension;
import de.schuette.cobra2D.math.Point;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.EntityInitializeException;
import de.schuette.cobra2D.map.Map;
import de.schuette.cobra2D.math.Math2D;
import de.schuette.cobra2D.rendering.GradientTransparencyFilter;
import de.schuette.cobra2D.system.Cobra2DEngine;

public class Fire extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String smokeTexture = "smoke";
	protected String redTexture = "fire0";
	protected String yellowTexture = "fire1";
	protected String whiteTexture = "fire2";

	protected int radius = 150;

	protected Smoke smoke;
	protected Smoke redFire;
	protected Smoke yellowFire;
	protected Smoke whiteFire;

	@Override
	public void initialize(final Cobra2DEngine engine)
			throws EntityInitializeException {
		super.initialize(engine);

		final Map map = engine.getMap();

		// if (this.smokeTexture != null) {
		if (this.smoke == null) {
			this.smoke = new Smoke();
			this.smoke.setTextureKey(this.smokeTexture);
			this.smoke.setPosition(this.position);
			this.smoke.setParticleCount(5);
			this.smoke.setMaxRadius(4);
			this.smoke.initialize(engine);
			map.addEntity(this.smoke);
		}

		if (this.redFire == null) {
			final int redRadius = Math2D.saveRound(this.radius
					* (100.0 / 150.0));
			final GradientTransparencyFilter filter = new GradientTransparencyFilter(
					Color.RED, Color.MAGENTA);
			this.redFire = new Smoke();
			this.redFire.setTextureKey(this.redTexture);
			this.redFire.setPosition(this.position);
			this.redFire.setParticleCount(5);
			this.redFire.setMaxRadius(redRadius);
			this.redFire.setFilter(filter);
			this.redFire.initialize(engine);

			map.addEntity(this.redFire);
		}

		if (this.yellowFire == null) {
			final int redRadius = Math2D.saveRound(this.radius
					* (100.0 / 150.0));
			final GradientTransparencyFilter filter = new GradientTransparencyFilter(
					Color.YELLOW, Color.MAGENTA);
			this.yellowFire = new Smoke();
			this.yellowFire.setTextureKey(this.yellowTexture);
			this.yellowFire.setPosition(this.position);
			this.yellowFire.setParticleCount(5);
			this.yellowFire.setMaxRadius(redRadius);
			this.yellowFire.setFilter(filter);
			this.yellowFire.initialize(engine);
			map.addEntity(this.yellowFire);
		}

		if (this.whiteFire == null) {
			final int yellowRadius = Math2D.saveRound(this.radius
					* (70.0 / 150.0));
			final GradientTransparencyFilter filter = new GradientTransparencyFilter(
					Color.WHITE, Color.MAGENTA);
			this.whiteFire = new Smoke();
			this.whiteFire.setTextureKey(this.whiteTexture);
			this.whiteFire.setPosition(this.position);
			this.whiteFire.setFilter(filter);
			this.whiteFire.setParticleCount(5);
			this.whiteFire.setMaxRadius(yellowRadius);
			this.whiteFire.initialize(engine);
			map.addEntity(this.whiteFire);
		}

		// Set your size to have a valid information about this entity
		this.setSize(new Dimension(radius, radius));
		map.removeEntity(this);
	}

	public String getSmokeTexture() {
		return smokeTexture;
	}

	public void setSmokeTexture(String smokeTexture) {
		this.smokeTexture = smokeTexture;
	}

	public String getRedTexture() {
		return redTexture;
	}

	public void setRedTexture(String redTexture) {
		this.redTexture = redTexture;
	}

	public String getYellowTexture() {
		return yellowTexture;
	}

	public void setYellowTexture(String yellowTexture) {
		this.yellowTexture = yellowTexture;
	}

	public String getWhiteTexture() {
		return whiteTexture;
	}

	public void setWhiteTexture(String whiteTexture) {
		this.whiteTexture = whiteTexture;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	@Override
	public void setPosition(Point position) {
		super.setPosition(position);
		if (smoke != null && redFire != null && yellowFire != null
				&& whiteFire != null) {
			smoke.setPosition(position);
			redFire.setPosition(position);
			yellowFire.setPosition(position);
			whiteFire.setPosition(position);
		}

	}

	@Override
	public void finish() {
		final Map map = this.engine.getMap();
		map.removeEntity(this.smoke);
		this.smoke.finish();
		map.removeEntity(this.redFire);
		this.redFire.finish();
		map.removeEntity(this.yellowFire);
		this.yellowFire.finish();
		map.removeEntity(this.whiteFire);
		this.whiteFire.finish();

		super.finish();
	}
}
