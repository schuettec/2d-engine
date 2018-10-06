package de.schuette.cobra2DSandbox.modifier;

import de.schuette.cobra2D.math.Point;
import java.util.ArrayList;
import java.util.List;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.EntityInitializeException;
import de.schuette.cobra2D.system.Cobra2DEngine;

public abstract class Modifier extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected List<Entity> entities;

	// @EditableProperty(modifierClass = )
	protected EntityCreator creator;

	public Modifier() {
		this.position = new Point(0, 0);
		this.entities = new ArrayList<Entity>();
	}

	@Override
	public void initialize(Cobra2DEngine engine)
			throws EntityInitializeException {
		super.initialize(engine);

		if (creator == null) {
			throw new IllegalStateException(
					"Entity creator cannot be null. Set entity creator first, before initializing.");
		}

		execute(engine);
	}

	public abstract void execute(final Cobra2DEngine engine);

	/**
	 * Removes all created entities from the map and finishes them.
	 */
	@Override
	public void finish() {
		super.finish();
		for (int i = 0; i < this.entities.size(); i++) {
			final Entity e = this.entities.get(i);
			engine.getMap().removeEntity(e);
			e.finish();
		}
	}

	public EntityCreator getCreator() {
		return creator;
	}

	public void setCreator(EntityCreator creator) {
		this.creator = creator;
	}

}
