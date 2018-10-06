package de.schuette.cobra2D.rendering;

import java.awt.Color;
import java.util.Random;

/**
 * Some more useful math functions for image processing. These are becoming
 * obsolete as we move to Java2D. Use MiscComposite instead.
 */
public class PixelUtils {

	public final static int REPLACE = 0;
	public final static int NORMAL = 1;
	public final static int MIN = 2;
	public final static int MAX = 3;
	public final static int ADD = 4;
	public final static int SUBTRACT = 5;
	public final static int DIFFERENCE = 6;
	public final static int MULTIPLY = 7;
	public final static int HUE = 8;
	public final static int SATURATION = 9;
	public final static int VALUE = 10;
	public final static int COLOR = 11;
	public final static int SCREEN = 12;
	public final static int AVERAGE = 13;
	public final static int OVERLAY = 14;
	public final static int CLEAR = 15;
	public final static int EXCHANGE = 16;
	public final static int DISSOLVE = 17;
	public final static int DST_IN = 18;
	public final static int ALPHA = 19;
	public final static int ALPHA_TO_GRAY = 20;

	private static Random randomGenerator = new Random();

	/**
	 * Clamp a value to the range 0..255
	 */
	public static int clamp(final int c) {
		if (c < 0) {
			return 0;
		}
		if (c > 255) {
			return 255;
		}
		return c;
	}

	public static int interpolate(final int v1, final int v2, final float f) {
		return PixelUtils.clamp((int) (v1 + f * (v2 - v1)));
	}

	public static int brightness(final int rgb) {
		final int r = (rgb >> 16) & 0xff;
		final int g = (rgb >> 8) & 0xff;
		final int b = rgb & 0xff;
		return (r + g + b) / 3;
	}

	public static boolean nearColors(final int rgb1, final int rgb2,
			final int tolerance) {
		final int r1 = (rgb1 >> 16) & 0xff;
		final int g1 = (rgb1 >> 8) & 0xff;
		final int b1 = rgb1 & 0xff;
		final int r2 = (rgb2 >> 16) & 0xff;
		final int g2 = (rgb2 >> 8) & 0xff;
		final int b2 = rgb2 & 0xff;
		return Math.abs(r1 - r2) <= tolerance && Math.abs(g1 - g2) <= tolerance
				&& Math.abs(b1 - b2) <= tolerance;
	}

	private final static float hsb1[] = new float[3];
	private final static float hsb2[] = new float[3];

	// Return rgb1 painted onto rgb2
	public static int combinePixels(final int rgb1, final int rgb2, final int op) {
		return PixelUtils.combinePixels(rgb1, rgb2, op, 0xff);
	}

	public static int combinePixels(final int rgb1, final int rgb2,
			final int op, final int extraAlpha, final int channelMask) {
		return (rgb2 & ~channelMask)
				| PixelUtils.combinePixels(rgb1 & channelMask, rgb2, op,
						extraAlpha);
	}

	public static int combinePixels(int rgb1, final int rgb2, final int op,
			final int extraAlpha) {
		if (op == PixelUtils.REPLACE) {
			return rgb1;
		}
		int a1 = (rgb1 >> 24) & 0xff;
		int r1 = (rgb1 >> 16) & 0xff;
		int g1 = (rgb1 >> 8) & 0xff;
		int b1 = rgb1 & 0xff;
		final int a2 = (rgb2 >> 24) & 0xff;
		final int r2 = (rgb2 >> 16) & 0xff;
		final int g2 = (rgb2 >> 8) & 0xff;
		final int b2 = rgb2 & 0xff;

		switch (op) {
		case NORMAL:
			break;
		case MIN:
			r1 = Math.min(r1, r2);
			g1 = Math.min(g1, g2);
			b1 = Math.min(b1, b2);
			break;
		case MAX:
			r1 = Math.max(r1, r2);
			g1 = Math.max(g1, g2);
			b1 = Math.max(b1, b2);
			break;
		case ADD:
			r1 = PixelUtils.clamp(r1 + r2);
			g1 = PixelUtils.clamp(g1 + g2);
			b1 = PixelUtils.clamp(b1 + b2);
			break;
		case SUBTRACT:
			r1 = PixelUtils.clamp(r2 - r1);
			g1 = PixelUtils.clamp(g2 - g1);
			b1 = PixelUtils.clamp(b2 - b1);
			break;
		case DIFFERENCE:
			r1 = PixelUtils.clamp(Math.abs(r1 - r2));
			g1 = PixelUtils.clamp(Math.abs(g1 - g2));
			b1 = PixelUtils.clamp(Math.abs(b1 - b2));
			break;
		case MULTIPLY:
			r1 = PixelUtils.clamp(r1 * r2 / 255);
			g1 = PixelUtils.clamp(g1 * g2 / 255);
			b1 = PixelUtils.clamp(b1 * b2 / 255);
			break;
		case DISSOLVE:
			if ((PixelUtils.randomGenerator.nextInt() & 0xff) <= a1) {
				r1 = r2;
				g1 = g2;
				b1 = b2;
			}
			break;
		case AVERAGE:
			r1 = (r1 + r2) / 2;
			g1 = (g1 + g2) / 2;
			b1 = (b1 + b2) / 2;
			break;
		case HUE:
		case SATURATION:
		case VALUE:
		case COLOR:
			Color.RGBtoHSB(r1, g1, b1, PixelUtils.hsb1);
			Color.RGBtoHSB(r2, g2, b2, PixelUtils.hsb2);
			switch (op) {
			case HUE:
				PixelUtils.hsb2[0] = PixelUtils.hsb1[0];
				break;
			case SATURATION:
				PixelUtils.hsb2[1] = PixelUtils.hsb1[1];
				break;
			case VALUE:
				PixelUtils.hsb2[2] = PixelUtils.hsb1[2];
				break;
			case COLOR:
				PixelUtils.hsb2[0] = PixelUtils.hsb1[0];
				PixelUtils.hsb2[1] = PixelUtils.hsb1[1];
				break;
			}
			rgb1 = Color.HSBtoRGB(PixelUtils.hsb2[0], PixelUtils.hsb2[1],
					PixelUtils.hsb2[2]);
			r1 = (rgb1 >> 16) & 0xff;
			g1 = (rgb1 >> 8) & 0xff;
			b1 = rgb1 & 0xff;
			break;
		case SCREEN:
			r1 = 255 - ((255 - r1) * (255 - r2)) / 255;
			g1 = 255 - ((255 - g1) * (255 - g2)) / 255;
			b1 = 255 - ((255 - b1) * (255 - b2)) / 255;
			break;
		case OVERLAY:
			int m,
			s;
			s = 255 - ((255 - r1) * (255 - r2)) / 255;
			m = r1 * r2 / 255;
			r1 = (s * r1 + m * (255 - r1)) / 255;
			s = 255 - ((255 - g1) * (255 - g2)) / 255;
			m = g1 * g2 / 255;
			g1 = (s * g1 + m * (255 - g1)) / 255;
			s = 255 - ((255 - b1) * (255 - b2)) / 255;
			m = b1 * b2 / 255;
			b1 = (s * b1 + m * (255 - b1)) / 255;
			break;
		case CLEAR:
			r1 = g1 = b1 = 0xff;
			break;
		case DST_IN:
			r1 = PixelUtils.clamp((r2 * a1) / 255);
			g1 = PixelUtils.clamp((g2 * a1) / 255);
			b1 = PixelUtils.clamp((b2 * a1) / 255);
			a1 = PixelUtils.clamp((a2 * a1) / 255);
			return (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
		case ALPHA:
			a1 = a1 * a2 / 255;
			return (a1 << 24) | (r2 << 16) | (g2 << 8) | b2;
		case ALPHA_TO_GRAY:
			final int na = 255 - a1;
			return (a1 << 24) | (na << 16) | (na << 8) | na;
		}
		if (extraAlpha != 0xff || a1 != 0xff) {
			a1 = a1 * extraAlpha / 255;
			final int a3 = (255 - a1) * a2 / 255;
			r1 = PixelUtils.clamp((r1 * a1 + r2 * a3) / 255);
			g1 = PixelUtils.clamp((g1 * a1 + g2 * a3) / 255);
			b1 = PixelUtils.clamp((b1 * a1 + b2 * a3) / 255);
			a1 = PixelUtils.clamp(a1 + a3);
		}
		return (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
	}

}
