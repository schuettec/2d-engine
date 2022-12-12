package de.schuette.cobra2D.math;

/**
 * This interface defines a clonable object. Implementations should make sure to
 * return deep copies.
 * 
 * @author Chris
 *
 */
public interface Cloneable {

	/**
	 * @return Returns an exact clone of this object. The clone is assumed to be
	 *         a deep copy of the object.
	 */
	public Cloneable clone();

}
