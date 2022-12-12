package de.schuette.cobra2D.entity.skills;

import java.awt.Graphics2D;
import de.schuette.cobra2D.math.Point;
import java.io.Serializable;

public interface Renderable extends Serializable {

	// If the entity should be visible in the scene, it should give a image
	// that will be rendered to the camera
	public abstract void render(final Graphics2D graphics, final Point position);

	// Layer
	public abstract int getLayer();
}
