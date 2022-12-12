package de.schuette.cobra2DSandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.skills.Renderable;
import de.schuette.cobra2D.math.EntityPoint;
import de.schuette.cobra2D.math.Point;
import de.schuette.cobra2D.system.Cobra2DEngine;

public class DebugEntity extends Entity implements Renderable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Color color = Color.RED;

	public DebugEntity(final Cobra2DEngine engine, final Point position) {
		this.setPosition(position);
		this.setSize(new Dimension(50, 50));
		pointList.add(new EntityPoint(0, 0));
		this.initialize(engine);
	}

	@Override
	public void render(final Graphics2D graphics, final Point position) {

		final Point x1 = new Point(position.getRoundX() - (int) (this.getSize().width / 2.0),
				position.getRoundY() - (int) (this.getSize().height / 2.0));

		final Point x2 = new Point(position.getRoundX() + (int) (this.getSize().width / 2.0),
				position.getRoundY() + (int) (this.getSize().height / 2.0));

		final Point x3 = new Point(position.getRoundX() - (int) (this.getSize().width / 2.0),
				position.getRoundY() + (int) (this.getSize().height / 2.0));

		final Point x4 = new Point(position.getRoundX() + (int) (this.getSize().width / 2.0),
				position.getRoundY() - (int) (this.getSize().height / 2.0));

		graphics.setColor(this.color);
		graphics.drawLine(x1.getRoundX(), x1.getRoundY(), x2.getRoundX(), x2.getRoundY());
		graphics.drawLine(x3.getRoundX(), x3.getRoundY(), x4.getRoundX(), x4.getRoundY());
	}

	public Color getColor() {
		return this.color;
	}

	public void setColor(final Color color) {
		this.color = color;
	}

	@Override
	public int getLayer() {
		return 255;
	}

}
