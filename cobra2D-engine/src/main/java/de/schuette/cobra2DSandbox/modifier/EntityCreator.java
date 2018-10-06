package de.schuette.cobra2DSandbox.modifier;

import de.schuette.cobra2D.entity.Entity;

public abstract class EntityCreator {
	/**
	 * This method is used to create an entity, so that it can be arranged by a
	 * modifier. This method should always return new instances of an entity. It
	 * is allowed to setup all entity properties as needed, but note that a
	 * modifier can override some of these properties, especially it calls the
	 * initialize-method itself.
	 * 
	 * A minimal implementation of this method is to call a constructor of an
	 * entity and return the instance.
	 * 
	 * @return Return a new instance of an entity.
	 */
	public abstract Entity createEntity();

	/**
	 * This method is used to modify the given entity after initializing. This
	 * is useful to add entity points after initialization has been completed.
	 * 
	 * Note: This method is called for every entity created by the modifier.
	 */
	public abstract void postInitializeEntity(Entity entity);
}
