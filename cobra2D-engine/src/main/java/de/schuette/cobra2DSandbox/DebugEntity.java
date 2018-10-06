package de.schuette.cobra2DSandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.EntityPoint;
import de.schuette.cobra2D.entity.skills.Renderable;
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
		pointList.add(new EntityPoint(0, 0, this));
		this.initialize(engine);
	}

	@Override
	public void render(final Graphics2D graphics, final Point position) {

		final Point x1 = new Point(position.x
				- (int) (this.getSize().width / 2.0), position.y
				- (int) (this.getSize().height / 2.0));

		final Point x2 = new Point(position.x
				+ (int) (this.getSize().width / 2.0), position.y
				+ (int) (this.getSize().height / 2.0));

		final Point x3 = new Point(position.x
				- (int) (this.getSize().width / 2.0), position.y
				+ (int) (this.getSize().height / 2.0));

		final Point x4 = new Point(position.x
				+ (int) (this.getSize().width / 2.0), position.y
				- (int) (this.getSize().height / 2.0));

		graphics.setColor(this.color);
		graphics.drawLine(x1.x, x1.y, x2.x, x2.y);
		graphics.drawLine(x3.x, x3.y, x4.x, x4.y);
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
