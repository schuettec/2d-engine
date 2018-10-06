package de.schuette.cobra2D.controller;

public interface ControllerListener {
	public void keyTyped(final int keyCode);

	public void keyReleased(final int keyCode);

	public void keyPressed(final int keyCode);

	public void keyChar(final char c);

}
