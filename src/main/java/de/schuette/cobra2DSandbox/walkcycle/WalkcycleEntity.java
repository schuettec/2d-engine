package de.schuette.cobra2DSandbox.walkcycle;

import java.awt.Color;
import java.awt.Graphics2D;

import org.apache.log4j.Logger;

import de.schuette.cobra2D.entity.EntityInitializeException;
import de.schuette.cobra2D.entity.editing.Editable;
import de.schuette.cobra2D.entity.skills.Moveable;
import de.schuette.cobra2D.math.Point;
import de.schuette.cobra2D.system.Cobra2DEngine;
import de.schuette.cobra2DSandbox.texture.LayeredEntity;

/**
 * @author Chris This entity defines a rendering behaviour to render animations.
 *         The rendering is
 */
@Editable
public class WalkcycleEntity extends LayeredEntity implements Moveable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(WalkcycleEntity.class);

	public WalkcycleEntity() {
		super();
	}

	@Override
	public void initialize(Cobra2DEngine engine) throws EntityInitializeException {
		super.initialize(engine);

//		this.body = new DebugEntity(engine, position);
//		this.body.setSize(new Dimension(100, 200));
//		this.body.setColor(Color.YELLOW);
//		engine.getMap().addEntity(body);

	}

	@Override
	public void finish() {
//		this.body.finish();
		super.finish();
	}

	@Override
	public void render(Graphics2D graphics, Point position) {
		graphics.setColor(Color.YELLOW);
		graphics.drawLine(position.getRoundX(), position.getRoundY(), position.getRoundX() + getSize().width,
				position.getRoundY() + getSize().height);
	}

	@Override
	public void next() {
		this.setPosition(this.getPosition());
	}

}
