package de.schuette.cobra2D.rendering;

import java.awt.Color;
import java.awt.image.RGBImageFilter;

public class HueBrightnessTransparencyFilter extends RGBImageFilter {

	private final float hueTrans;
	private final float maxBrightness;

	public HueBrightnessTransparencyFilter(final int hue,
			final float maxBrightness) {
		this.hueTrans = Math.min((hue % 360) / 360.0f, 1f);

		this.maxBrightness = maxBrightness;
	}

	@Override
	public int filterRGB(final int x, final int y, final int rgb) {
		final Color srcC = new Color(rgb);

		final float[] hsbSrc = Color.RGBtoHSB(srcC.getRed(), srcC.getGreen(),
				srcC.getBlue(), null);

		final float hueSrc = hsbSrc[0];
		final float brgSrc = hsbSrc[2];

		if (hueSrc >= (this.hueTrans - 0.13f)
				&& hueSrc <= (this.hueTrans + 0.1f)) {
			// If the transparency color occurs.
			return new Color(srcC.getRed(), srcC.getGreen(), srcC.getBlue(), 0)
					.getRGB();
		}

		int alpha = 255;
		if (brgSrc <= this.maxBrightness) {
			alpha = Math.round((255f / this.maxBrightness) * brgSrc);
		}
		return new Color(srcC.getRed(), srcC.getGreen(), srcC.getBlue(), alpha)
				.getRGB();

	}
}
