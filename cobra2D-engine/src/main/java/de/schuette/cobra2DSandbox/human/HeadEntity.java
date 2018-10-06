package de.schuette.cobra2DSandbox.human;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

import de.schuette.cobra2D.entity.EntityInitializeException;
import de.schuette.cobra2D.entity.editing.Editable;
import de.schuette.cobra2D.entity.editing.EditableProperty;
import de.schuette.cobra2D.entity.editing.NumberEditor;
import de.schuette.cobra2D.entity.skills.Moveable;
import de.schuette.cobra2D.entity.skills.Renderable;
import de.schuette.cobra2D.math.Math2D;
import de.schuette.cobra2D.system.Cobra2DEngine;
import de.schuette.cobra2DSandbox.texture.TextureEntity;

@Editable
public class HeadEntity extends TextureEntity implements Moveable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EditableProperty(modifierClass = NumberEditor.KEY)
	protected int maxTorsoRotationDegrees = 70;

	protected WalkcycleEntity walkcycle;

	protected transient Point lastMousePosition;

	public WalkcycleEntity getWalkcycle() {
		return walkcycle;
	}

	public void setWalkcycle(WalkcycleEntity walkcycle) {
		this.walkcycle = walkcycle;
	}

	@Override
	public void initialize(Cobra2DEngine engine)
			throws EntityInitializeException {
		if (walkcycle == null) {
			throw new EntityInitializeException(
					"Walkcycle attribute cannot be empty.");
		}

		super.initialize(engine);
	}

	/**
	 * Constructs a head entity an connects it to a walkcycle entity.
	 * 
	 * @param walkcycle
	 *            The walkcycle that is prepared and initialized.
	 * @param textureKey
	 *            The texture for this head.
	 */
	// public HeadEntity(final WalkcycleEntity walkcycle, final String
	// textureKey) {
	// this(walkcycle, textureKey, null);
	// }

	/**
	 * Constructs a head entity an connects it to a walkcycle entity.
	 * 
	 * @param walkcycle
	 *            The walkcycle that is prepared and initialized.
	 * @param textureKey
	 *            The texture for this head.
	 */
	// public HeadEntity(final WalkcycleEntity walkcycle, final String
	// textureKey,
	// final Dimension targetSize) {
	// this(walkcycle, textureKey, targetSize, RenderToolkit
	// .getDefaultTransparencyFilter());
	// }

	/**
	 * Constructs a head entity an connects it to a walkcycle entity.
	 * 
	 * @param walkcycle
	 *            The walkcycle that is prepared and initialized.
	 * @param textureKey
	 *            The texture for this head.
	 * @param filter
	 *            The texture filter for this head.
	 */
	// public HeadEntity(final WalkcycleEntity walkcycle, final String
	// textureKey,
	// final Dimension targetSize, final RGBImageFilter filter) {
	// super(walkcycle.getCenterPoint(), targetSize, textureKey, filter);
	// this.walkcycle = walkcycle;
	// }

	@Override
	public void next() {
		if (walkcycle != null) {
			final Point newPosition = this.walkcycle.getCenterPoint();
			newPosition.x = newPosition.x
					- Math2D.saveRound(this.getSize().width / 2.0);
			newPosition.y = newPosition.y
					- Math2D.saveRound(this.getSize().height / 2.0);
			this.setPosition(newPosition);
		}
	}

	@Override
	public void render(final Graphics2D graphics, final Point position) {
		System.out.println(getDegrees());
		if (walkcycle != null) {
			final PointerInfo a = MouseInfo.getPointerInfo();
			final Point mousePosition = a.getLocation();

			final Point screenPosition = this.engine.getRenderer()
					.getLocation();
			final Dimension screenSize = this.getSize();
			final Point middleScreenPoint = new Point(screenSize.width,
					screenSize.height);
			middleScreenPoint.x = screenPosition.x + position.x
					+ Math2D.saveRound(middleScreenPoint.x / 2.0);
			middleScreenPoint.y = screenPosition.y + position.y
					+ Math2D.saveRound(middleScreenPoint.y / 2.0);

			final double degrees = Math2D.getAngle(middleScreenPoint,
					mousePosition);

			this.setDegrees(this.walkcycle.getDegrees());

			if (!mousePosition.equals(this.lastMousePosition)) {
				this.setDegrees(degrees);
				this.walkcycle.setDegrees(degrees);
			}
			this.lastMousePosition = mousePosition;
		}
		// Call inherited method from texture entity to rendere resized!
		this.renderTexture(getDegrees(), getImage(), graphics, position);
	}

	@Override
	public int getLayer() {
		if (walkcycle != null) {
			final Renderable walkcycle = this.walkcycle;
			return walkcycle.getLayer() + 1;
		}
		return Integer.MAX_VALUE;
	}

}
