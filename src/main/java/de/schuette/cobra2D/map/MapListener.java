package de.schuette.cobra2D.map;

import de.schuette.cobra2D.entity.Entity;

public interface MapListener {

	public void entityAdded(final Entity entity);

	public void entityRemoved(final Entity entity);

	public void beforeUpdate();

	public void afterUpdate();

}
