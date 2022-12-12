package de.schuette.cobra2D.rendering;

import java.awt.Color;
import java.awt.image.RGBImageFilter;

public class HSBTransparencyFilter extends RGBImageFilter {

	private final float hueTrans;
	private final float maxHueDistance;

	private final float minSaturation;
	private final float minBrightness;

	public HSBTransparencyFilter(final int hue, final int maxHueDistance) {
		this(hue, maxHueDistance, 40, 20);
	}

	public HSBTransparencyFilter(final int hue, final int maxHueDistance,
			final int minSaturation, final int minBrightness) {
		this.hueTrans = Math.min((hue % 360) / 360.0f, 1f);

		this.maxHueDistance = Math.min((maxHueDistance % 360) / 360.0f, 1f);
		this.minSaturation = minSaturation / 100.0f;
		this.minBrightness = minBrightness / 100.0f;
	}

	@Override
	public int filterRGB(final int x, final int y, final int rgb) {

		final Color pixel = new Color(rgb);

		final float[] hsbSrc = Color.RGBtoHSB(pixel.getRed(), pixel.getGreen(),
				pixel.getBlue(), null);

		final float hueSrc = hsbSrc[0];
		final float satSrc = hsbSrc[1];
		final float brgSrc = hsbSrc[2];

		// If color hue is not in the transparency area take full opacity
		if (hueSrc < (this.hueTrans - this.maxHueDistance)
				|| hueSrc > (this.hueTrans + this.maxHueDistance)) {
			return rgb;
		} else if (satSrc < this.minSaturation) {
			return rgb;

		} else if (brgSrc < this.minBrightness) {
			return rgb;

		} else {

			final float distance = this.hueTrans - hueSrc;

			int alpha = 255;
			alpha = Math.round((255f / this.maxHueDistance)
					* Math.abs(distance));

			// Choose the color that is the nearest to allowed color to hue
			// trans
			float targetHue;
			if (distance < 0) {
				targetHue = this.hueTrans - this.maxHueDistance;
			} else {
				targetHue = this.hueTrans + this.maxHueDistance;
			}

			final Color newColor = new Color(Color.HSBtoRGB(targetHue, satSrc,
					brgSrc));

			return new Color(newColor.getRed(), newColor.getGreen(),
					newColor.getBlue(), alpha).getRGB();
		}
	}
}
