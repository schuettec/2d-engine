package de.schuette.cobra2D.math;

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * @author Chris Defines a parabel that can be initialized with three arguments:
 *         A point where the peak of the parabel
 */
public class Parabel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Point2D.Double expectedValue;

	protected Point2D.Double peak;
	protected double a;
	protected int parabelType;

	/**
	 * @param expectedValue
	 * @param peak
	 * @param parabelType
	 */
	public Parabel(final Point2D.Double expectedValue,
			final Point2D.Double peak, final boolean parabelBottomType) {
		super();

		if (expectedValue.x - peak.x == 0)
			throw new IllegalStateException(
					"ExpectedValue.x - Peak.x cannot be 0.");

		this.peak = peak;
		this.setExpectedValue(expectedValue);
		this.setBottomType(parabelBottomType);
	}

	public double getValue(final double x) {
		if (this.isBottomType()) {
			return -this.a * Math.pow(x - this.peak.x, 2) + this.peak.y;
		} else {
			return this.a * Math.pow(x - this.peak.x, 2) + this.peak.y;
		}
	}

	public Point2D.Double getPeak() {
		return this.peak;
	}

	public void setPeak(final Point2D.Double peak) {
		this.peak = peak;
		this.calculateA();
	}

	private void calculateA() {
		this.a = -(this.expectedValue.y - this.peak.y)
				/ Math.pow(this.expectedValue.x - this.peak.x, 2);
	}

	public Point2D.Double getExpectedValue() {
		return this.expectedValue;
	}

	public void setExpectedValue(final Point2D.Double expectedValue) {
		this.expectedValue = expectedValue;
		this.calculateA();
	}

	public boolean isBottomType() {
		return this.parabelType < 0;
	}

	public void setBottomType(final boolean bottomType) {
		if (bottomType) {
			this.parabelType = -1;
		} else {
			this.parabelType = 1;
		}
	}

	@Override
	public String toString() {
		return "Parabel: f(x) = " + this.a + "* (x - " + this.peak.x + ")² + "
				+ this.peak.y + ")";
	}
}
