package de.schuette.cobra2DSandbox.texture;

import java.awt.Graphics2D;
import de.schuette.cobra2D.math.Point;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.editing.Editable;
import de.schuette.cobra2D.entity.editing.EditableProperty;
import de.schuette.cobra2D.entity.editing.NumberEditor;
import de.schuette.cobra2D.entity.skills.Renderable;
import de.schuette.cobra2D.rendering.RenderToolkit;

/**
 * This is a renderable entity obtaining a dynamically adjustable layer.
 * 
 * @author Chris
 * 
 */
@Editable
public class LayeredEntity extends Entity implements Renderable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EditableProperty(modifierClass = NumberEditor.KEY)
	protected int layer = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.schuette.cobra2D.entity.skills.Renderable#getLayer()
	 */
	@Override
	public int getLayer() {
		return this.layer;
	}

	/**
	 * Set the new layer for rendering. The layers of the entity are used to
	 * determine their rendering order.
	 * 
	 * @param layer
	 *            The new layer.
	 */
	public void setLayer(final int layer) {
		this.layer = layer;
		if (engine != null) {
			engine.getMap().adjustRenderableLayer(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.schuette.cobra2D.entity.skills.Renderable#render(java.awt.Graphics2D,
	 * java.awt.Point)
	 */
	@Override
	public void render(final Graphics2D graphics, final Point position) {
		RenderToolkit.renderTo(position, graphics, this.getImage());
	}
}
