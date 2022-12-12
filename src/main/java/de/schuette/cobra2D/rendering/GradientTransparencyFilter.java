package de.schuette.cobra2D.rendering;

import java.awt.Color;
import java.awt.image.RGBImageFilter;

import de.schuette.cobra2D.math.Math2D;

public class GradientTransparencyFilter extends RGBImageFilter {
	protected double maxDistance;

	protected Color start;
	protected int rgbEnd[];

	public GradientTransparencyFilter(final Color start, final Color end) {

		this.rgbEnd = new int[] { end.getRed(), end.getGreen(), end.getBlue() };
		this.maxDistance = Math.sqrt(Math.pow(start.getRed() - this.rgbEnd[0],
				2)
				+ Math.pow(start.getGreen() - this.rgbEnd[1], 2)
				+ Math.pow(start.getBlue() - this.rgbEnd[2], 2));

		this.start = start;

		if (this.maxDistance == 0.0) {
			this.maxDistance = 1.0;
		}

	}

	protected double getDistance(final Color testColor) {
		return Math.sqrt(Math.pow(testColor.getRed() - this.rgbEnd[0], 2)
				+ Math.pow(testColor.getGreen() - this.rgbEnd[1], 2)
				+ Math.pow(testColor.getBlue() - this.rgbEnd[2], 2));
	}

	@Override
	public int filterRGB(final int x, final int y, final int rgb) {
		final Color srcC = new Color(rgb);

		// float[] hsbSrc = Color.RGBtoHSB(srcC.getRed(), srcC.getGreen(),
		// srcC.getBlue(), null);
		//
		// float hueSrc = hsbSrc[0];
		// float satSrc = hsbSrc[1];
		// float brgSrc = hsbSrc[2];
		//
		// if (hueSrc >= (hueTrans - 0.13f) && hueSrc <= (hueTrans + 0.1f)) {
		// // If the transparency color occurs.
		// return new Color(srcC.getRed(), srcC.getGreen(), srcC.getBlue(), 0)
		// .getRGB();
		// }
		//
		final int alpha = Math2D.saveRound(this.getDistance(srcC)
				* (255.0 / this.maxDistance));
		if (alpha > 255) {
			return new Color(srcC.getRed(), srcC.getGreen(), srcC.getBlue(), 0)
					.getRGB();
		}

		// System.out.println(getDistance(srcC) + " of -> " + maxDistance);
		// int alpha = (int) Math.round(lambda * 2.55f);
		// System.out.println(lambda);
		return new Color(this.start.getRed(), this.start.getGreen(),
				this.start.getBlue(), alpha).getRGB();
		// return new Color(srcC.getRed(), srcC.getGreen(), srcC.getBlue(),
		// alpha)
		// .getRGB();
	}
}
