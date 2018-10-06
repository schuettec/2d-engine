package de.schuette.cobra2DSandbox.camera.widgets;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;

public class AverageMeter extends Meter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected transient long startTimestamp;
	protected long lastUpdateCount;
	protected long updateCount;

	public AverageMeter(final Dimension size, final String label,
			final int min, final int max, final Point position) {
		super(size, label, min, max, position);
		this.startTimestamp = -1;
	}

	@Override
	public void drawMeter(final Graphics2D graphics, final int value) {

		// init start timestamp
		if (this.startTimestamp == -1) {
			this.startTimestamp = System.currentTimeMillis();
		}

		int avergareFPS = 0;

		// Reset average fps all 5 seconds
		if (System.currentTimeMillis() - this.startTimestamp >= 20000) {
			this.startTimestamp = System.currentTimeMillis() - 1000;
			this.updateCount = value;
			avergareFPS = value;
		} else {

			// Only count update every second
			if (System.currentTimeMillis() - this.lastUpdateCount >= 1000) {
				this.updateCount += value;
				this.lastUpdateCount = System.currentTimeMillis();
			}

			// Calculate average fps
			final long time = System.currentTimeMillis() - this.startTimestamp;
			avergareFPS = (int) Math.round(this.updateCount / (time / 1000.0));
		}

		// Draw the average stuff
		super.drawMeter(graphics, avergareFPS);

	}

	// Reset timestamp after deserialization
	private synchronized void readObject(final java.io.ObjectInputStream s)
			throws IOException, ClassNotFoundException {
		this.startTimestamp = -1;
	}

}
