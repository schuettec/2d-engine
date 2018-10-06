package de.schuette.cobra2DSandbox.camera.widgets;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public interface Button {

	public void activate();

	public void deactivate();

	public Dimension getSize();

	public BufferedImage render();

	public void performAction(final ActionEvent event);

	public boolean isActive();

	public Point getPosition();

	public void setPosition(final Point position);

	public Object getValue();

	public void setValue(final Object object);
}
