package de.schuette.cobra2DSandbox.modifier;

import java.awt.Dimension;
import java.awt.Point;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.editing.Editable;
import de.schuette.cobra2D.entity.editing.EditableProperty;
import de.schuette.cobra2D.entity.editing.NumberEditor;
import de.schuette.cobra2D.system.Cobra2DEngine;

@Editable
public class ArrayModifier extends Modifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EditableProperty(modifierClass = NumberEditor.KEY)
	protected int xMultiplier;
	@EditableProperty(modifierClass = NumberEditor.KEY)
	protected int yMultiplier;

	public ArrayModifier() {
		this.xMultiplier = 1;
		this.yMultiplier = 1;
	}

	public void execute(final Cobra2DEngine engine) {

		// Create all the entities side by side. Start at the point
		// getPosition().

		final Point newPosition = new Point(this.position);
		for (int y = 0; y < this.yMultiplier; y++) {
			Entity newEntity = null;
			Dimension eDim = null;
			for (int x = 0; x < this.xMultiplier; x++) {
				newEntity = this.creator.createEntity();
				newEntity.initialize(engine);
				creator.postInitializeEntity(newEntity);
				newEntity.setPosition(newPosition);
				eDim = newEntity.getSize();
				newPosition.x += eDim.width;
				this.entities.add(newEntity);
				engine.getMap().addEntity(newEntity);
			}
			if (newPosition != null && eDim != null) {
				newPosition.x = this.position.x;
				newPosition.y += eDim.height;
			}

		}

	}

	public int getxMultiplier() {
		return xMultiplier;
	}

	public void setxMultiplier(int xMultiplier) {
		this.xMultiplier = xMultiplier;
	}

	public int getyMultiplier() {
		return yMultiplier;
	}

	public void setyMultiplier(int yMultiplier) {
		this.yMultiplier = yMultiplier;
	}

}
