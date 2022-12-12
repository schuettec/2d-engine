package de.schuette.cobra2D.system;

import java.io.Serializable;
import java.util.HashMap;

import de.schuette.cobra2D.map.Map;
import de.schuette.cobra2D.ressource.AnimationMemory;
import de.schuette.cobra2D.ressource.ImageMemory;

/**
 * Data container for dynamic resources in a level. Everything should be
 * serializable here, because this object defines a loaded level.
 * 
 * @author Chris
 * 
 */
public class Cobra2DLevel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected HashMap<String, Class<?>> entityClasses;
	protected ImageMemory imageMemory;
	protected AnimationMemory animationMemory;
	protected Map map;

	public Cobra2DLevel() {
		super();
		this.entityClasses = new HashMap<String, Class<?>>();
		this.imageMemory = new ImageMemory();
		this.animationMemory = new AnimationMemory(this.imageMemory);
		this.map = new Map();

	}

	/**
	 * This method is used to revive every member, after deserialization.
	 */
	public void afterDeserialization(final Cobra2DEngine engine) {
		// Get your instance of the engine
		engine.setLevel(this);

		// AnimationMemory: call afterDeserialization with an instance of
		// imageMemory
		this.animationMemory.afterDeserialization(engine);

		this.map.afterDeserialization(engine);

	}

	public HashMap<String, Class<?>> getEntityClasses() {
		return this.entityClasses;
	}

	public void setEntityClasses(final HashMap<String, Class<?>> entityClasses) {
		this.entityClasses = entityClasses;
	}

	public ImageMemory getImageMemory() {
		return this.imageMemory;
	}

	public AnimationMemory getAnimationMemory() {
		return this.animationMemory;
	}

	public Map getMap() {
		return this.map;
	}

	public void setMap(final Map map) {
		this.map = map;
	}

	public void finish() {
		this.map = null;
		this.imageMemory = null;
		this.animationMemory = null;
		this.entityClasses = null;

		if (this.map != null) {
			this.map.finish();
		}
		if (this.imageMemory != null) {
			this.imageMemory.clear();
		}
		if (this.animationMemory != null) {
			this.animationMemory.clear();
		}
		if (this.entityClasses != null) {
			this.entityClasses.clear();
		}

	}

}
