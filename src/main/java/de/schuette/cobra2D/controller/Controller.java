package de.schuette.cobra2D.controller;

import java.util.ArrayList;
import java.util.List;

public abstract class Controller {

	protected List<ControllerListener> listeners = new ArrayList<ControllerListener>();

	public abstract void finish();

	public void addKeyListener(final ControllerListener listener) {
		this.listeners.add(listener);
	}

	public void removeKeyListener(final ControllerListener listener) {
		this.listeners.remove(listener);
	}

	public abstract boolean isKeyPressed(final int keyCode);

}
