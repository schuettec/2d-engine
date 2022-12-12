package de.schuette.cobra2D.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import de.schuette.cobra2D.rendering.Renderer;

public class SinglePlayerController extends Controller implements KeyListener {
	private final Renderer renderer;

	private final boolean[] pressedKeys = new boolean[1000];

	public SinglePlayerController(final Renderer renderer) throws IllegalArgumentException {
		if (renderer == null) {
			throw new IllegalArgumentException("Need a renderer to control keyboard.");
		}
		this.renderer = renderer;

		this.renderer.addKeyListener(this);

	}

	public void addMouseListener(MouseListener listener) {
		renderer.addMouseListener(listener);
	}

	public void addMouseMotionListener(MouseMotionListener listener) {
		renderer.addMouseMotionListener(listener);
	}

	@Override
	public void finish() {
		this.renderer.removeKeyListener(this);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {

		for (int i = 0; i < this.listeners.size(); i++) {
			final ControllerListener listener = this.listeners.get(i);
			listener.keyPressed(arg0.getKeyCode());

		}

		this.pressedKeys[arg0.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(final KeyEvent arg0) {

		for (int i = 0; i < this.listeners.size(); i++) {
			final ControllerListener listener = this.listeners.get(i);
			listener.keyReleased(arg0.getKeyCode());
		}
		this.pressedKeys[arg0.getKeyCode()] = false;
	}

	@Override
	public boolean isKeyPressed(final int keyCode) {
		return this.pressedKeys[keyCode];
	}

	@Override
	public void keyTyped(final KeyEvent arg0) {

		for (int i = 0; i < this.listeners.size(); i++) {
			final ControllerListener listener = this.listeners.get(i);
			listener.keyTyped(arg0.getKeyCode());
			listener.keyChar(arg0.getKeyChar());
		}

	}

}
