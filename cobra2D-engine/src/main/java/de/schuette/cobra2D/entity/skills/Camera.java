package de.schuette.cobra2D.entity.skills;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.List;

import de.schuette.cobra2D.entity.Entity;

public interface Camera extends Serializable {

	/**
	 * This method is used by the map engine to set the list of objects
	 * currently located in the viewport.
	 * 
	 * @param capturedObjects
	 *            The set of objects currently captured by this camera.
	 */
	public void setCapturedObjects(final List<Entity> capturedObjects);

	/**
	 * Reads the current position and dimension of the viewport. This method is
	 * used to pre-calculate the set of objects, that are captured by this
	 * camera object.
	 * 
	 * @return Return the viewport rectangle of this camera, by specifying the
	 *         current position and dimension of the viewport.
	 */
	public Rectangle getViewportRectangle();

	/**
	 * Renders the current scene in the viewport to the graphics object.
	 * 
	 * @param graphics
	 *            The graphics object to render to.
	 */
	public void render(final Graphics2D graphics);

	/**
	 * Specifies whether the rendered image is stretched to fit to the given
	 * resolution of the screen. By returning false it is possible to avoid
	 * stretching of the picture. Important information: If you want to use
	 * buttons with mouse activity it is necessary to turn "fitToResolution" to
	 * false The problem is: If the renderer stretches the image of the menue,
	 * the position of the buttons cannot be calculated.
	 * 
	 * Note: Stretching images is time expensive!
	 * 
	 * @return Return true if camera viewport should be streched to resolution.
	 *         False otherwise.
	 */
	public boolean fitToResolution();

}
