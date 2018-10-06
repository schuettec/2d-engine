package de.schuette.cobra2D.rendering;

public class RendererException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4072483268323413599L;

	public RendererException() {
	}

	public RendererException(final String message) {
		super(message);
	}

	public RendererException(final Throwable cause) {
		super(cause);
	}

	public RendererException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
