package de.schuette.cobra2D.entity;

public class EntityInitializeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EntityInitializeException() {
		super();
	}

	public EntityInitializeException(final String message, final Throwable e) {
		super(message, e);
	}

	public EntityInitializeException(final String message) {
		super(message);
	}

	public EntityInitializeException(final Throwable e) {
		super(e);
	}

}
