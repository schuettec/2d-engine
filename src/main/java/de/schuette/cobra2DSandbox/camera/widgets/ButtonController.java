package de.schuette.cobra2DSandbox.camera.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.List;

import de.schuette.cobra2D.controller.ControllerListener;
import de.schuette.cobra2D.math.Math2D;
import de.schuette.cobra2D.math.Point;
import de.schuette.cobra2D.rendering.RenderToolkit;
import de.schuette.cobra2D.system.Cobra2DEngine;

public class ButtonController implements ControllerListener, MouseListener, MouseMotionListener {

	public enum ButtonLayout {
		HORIZONTAL, VERTICAL;
	}

	protected ButtonLayout layout;

	protected List<Button> buttons = new ArrayList<Button>();

	protected int activeButton = 0;

	protected boolean running = false;
	protected boolean paused = false;

	protected int spacing = 0;

	protected Color background;

	private final Cobra2DEngine engine;

	public ButtonController(final Cobra2DEngine engine, final ButtonLayout layout) {
		this(engine, layout, 0, Color.BLACK);
	}

	public ButtonController(final Cobra2DEngine engine, final ButtonLayout layout, final int spacing,
			final Color background) {
		this.layout = layout;
		this.spacing = spacing;
		this.background = background;
		this.engine = engine;
	}

	public void addButton(final Button button) {
		this.buttons.add(button);

	}

	public void removeButton(final Button button) {
		this.buttons.remove(button);
	}

	public void deactivateButtons() {
		for (final Button b : this.buttons) {
			b.deactivate();
		}
	}

	public void removeAll() {
		this.buttons.clear();
	}

	public void activateButton(final Button button) {

		this.deactivateButtons();
		button.activate();
	}

	public void activateButton(final int index) {
		for (final Button b : this.buttons) {
			b.deactivate();
		}

		final Button button = this.buttons.get(index);
		if (button != null) {
			button.activate();
		}
	}

	public void activateNextButton() {
		this.activeButton++;
		if (this.activeButton >= this.buttons.size()) {
			this.activeButton = 0;
		}
		this.activateButton(this.activeButton);
	}

	public void activatePreviousButton() {
		this.activeButton--;
		if (this.activeButton < 0) {
			this.activeButton = this.buttons.size() - 1;
		}
		this.activateButton(this.activeButton);
	}

	public Button getActiveButton() {
		for (final Button b : this.buttons) {
			if (b.isActive()) {
				return b;
			}
		}

		return null;
	}

	public Dimension getSize() {
		int width = 0;
		int height = 0;
		if (this.layout == ButtonLayout.HORIZONTAL) {
			// Creating Size
			for (int i = 0; i < this.buttons.size(); i++) {
				final Button button = this.buttons.get(i);
				if (button.getSize().width > width) {
					width = button.getSize().width;
				}

				height += button.getSize().height + this.spacing;
			}

		} else {
			// Creating Size
			for (int i = 0; i < this.buttons.size(); i++) {
				final Button button = this.buttons.get(i);
				if (button.getSize().height > height) {
					height = button.getSize().height;
				}

				width += button.getSize().width + this.spacing;
			}

		}
		return new Dimension(width, height);
	}

	public void drawButtonField(final Graphics2D graphics, final Point position) {
		if (!this.running || this.buttons.size() <= 0) {
			return;
		}
		VolatileImage image;
		final int width = this.getSize().width;
		final int height = this.getSize().height;

		if (this.layout == ButtonLayout.HORIZONTAL) {

			image = RenderToolkit.createVolatileImage(width, height);

			final Graphics2D g = (Graphics2D) image.getGraphics();
			g.setColor(this.background);
			g.fillRect(0, 0, width, height);
			int lastHeight = 0;
			for (int i = 0; i < this.buttons.size(); i++) {
				final Button button = this.buttons.get(i);
				final BufferedImage buttonImage = button.render();
				final int mX = (width - button.getSize().width) / 2;
				g.drawImage(buttonImage, mX, lastHeight, null);
				button.setPosition(new Point(mX + position.getRoundX(), lastHeight + position.getRoundY()));
				lastHeight += button.getSize().height + this.spacing;

			}
		} else {

			image = RenderToolkit.createVolatileImage(width, height);

			final Graphics2D g = (Graphics2D) image.getGraphics();
			g.setColor(this.background);
			g.fillRect(0, 0, width, height);
			int lastWidth = 0;
			for (int i = 0; i < this.buttons.size(); i++) {
				final Button button = this.buttons.get(i);
				final BufferedImage buttonImage = button.render();
				g.drawImage(buttonImage, lastWidth, 0, null);
				button.setPosition(new Point(lastWidth + position.getRoundX(), position.getRoundY()));
				lastWidth += button.getSize().width + this.spacing;

			}
		}

		RenderToolkit.renderTo(position, graphics, image); // size, image);
	}

	public void start(final boolean keyboardActions, final boolean mouseActions) {

		if (keyboardActions) {
			this.activateButton(0);
		}
		this.paused = false;
		this.running = true;
		if (keyboardActions) {
			this.engine.getController().addKeyListener(this);
		}
		if (mouseActions) {
			this.engine.getRenderer().addMouseListener(this);
			this.engine.getRenderer().addMouseMotionListener(this);
		}
	}

	public void pause() {
		this.paused = true;
		this.engine.getController().removeKeyListener(this);
		this.engine.getRenderer().removeMouseListener(this);
		this.engine.getRenderer().removeMouseMotionListener(this);
	}

	public void finish() {
		this.pause();
		this.running = false;

	}

	public ButtonLayout getLayout() {
		return this.layout;
	}

	public boolean isRunning() {
		return this.running;
	}

	public boolean isPaused() {
		return this.paused;
	}

	@Override
	public void keyPressed(final int e) {

		if (!this.running) {
			return;
		}

		if (e == KeyEvent.VK_UP || e == KeyEvent.VK_LEFT) {
			this.activatePreviousButton();
		}

		if (e == KeyEvent.VK_DOWN || e == KeyEvent.VK_RIGHT) {
			this.activateNextButton();
		}

		if (e == KeyEvent.VK_ENTER) {
			final Button button = this.getActiveButton();
			if (button == null) {
				return;
			}
			button.performAction(new ActionEvent(this.getActiveButton(), this.activeButton, "buttonClicked"));
		}

	}

	@Override
	public void keyReleased(final int e) {
	}

	@Override
	public void keyTyped(final int e) {
	}

	@Override
	public void mouseClicked(final MouseEvent e) {

		if (!this.engine.getRenderer().isCursorVisible()) {
			return;
		}
		final Button button = this.getActiveButton();
		if (button == null) {
			return;
		}
		button.performAction(new ActionEvent(this.getActiveButton(), this.activeButton, "buttonClicked"));

	}

	@Override
	public void mouseMoved(final MouseEvent e) {

		if (!this.engine.getRenderer().isCursorVisible()) {
			return;
		}

		this.deactivateButtons();

		for (int i = 0; i < this.buttons.size(); i++) {
			final Button button = this.buttons.get(i);
			if (button.getPosition() == null) {
				continue;
			}
			final Point a = button.getPosition();
			final Point b = new Point(button.getPosition().x + button.getSize().width,
					button.getPosition().y + button.getSize().height);
			if (Math2D.isInRect(Point.ofAWT(e.getPoint()), a, b)) {
				this.activateButton(button);
			} else {

			}
		}
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
	}

	@Override
	public void mouseExited(final MouseEvent e) {
	}

	@Override
	public void mousePressed(final MouseEvent e) {
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
	}

	@Override
	public void mouseDragged(final MouseEvent e) {

	}

	@Override
	public void keyChar(final char c) {

	}

}
