package de.schuette.cobra2D.rendering;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import de.schuette.cobra2D.math.Math2D;

public class RenderToolkit {
	private static GraphicsConfiguration gConfig;

	static {
		final GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		RenderToolkit.gConfig = ge.getDefaultScreenDevice()
				.getDefaultConfiguration();

	}

	/*
	 * 
	 * NEW!!!
	 */
	public static void renderTo(float alpha, double degrees,
			final Point position, final Dimension size,
			final Graphics2D graphics, final VolatileImage image) {

		// TRANSPARENT DRAWING
		AlphaComposite alphaC = null;
		if (alpha != 1f) {
			alphaC = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
			graphics.setComposite(alphaC);
		}

		// doit
		renderTo(degrees, position, size, graphics, image);

		if (alpha != 1f) {
			alphaC = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
			graphics.setComposite(alphaC);
		}
	}

	public static void renderTo(double degrees, final Point position,
			final Dimension size, final Graphics2D graphics,
			final VolatileImage image) {
		// ROTATION
		AffineTransform oldTransform = null;
		AffineTransform rotateTransform = null;
		if (degrees != 0.0d) {
			oldTransform = graphics.getTransform();
			rotateTransform = new AffineTransform();
			rotateTransform.rotate(Math.toRadians(degrees),
					position.x + Math2D.saveRound(size.getWidth() / 2.0),
					position.y + Math2D.saveRound(size.getHeight() / 2.0));
			graphics.setTransform(rotateTransform);
		}

		// DOIT
		renderTo(position, size, graphics, image);

		if (degrees != 0.0d) {
			graphics.setTransform(oldTransform);
		}

	}

	public static void renderTo(final Point position, final Dimension size,
			final Graphics2D graphics, final VolatileImage image) {

		// RESIZED DRAWING
		if (size.width == image.getWidth() && size.height == image.getHeight()) {
			// Draw not resized
			renderTo(position, graphics, image);
		} else {
			// Draw resized
			graphics.drawImage(image, position.x, position.y, position.x
					+ size.width, position.y + size.height, 0, 0,
					image.getWidth(), image.getHeight(), null);
		}
	}

	public static void renderTo(float alpha, double degrees, int blurredFactor,
			final Point position, final Graphics2D graphics,
			final VolatileImage image) {
		// TRANSPARENT DRAWING
		AlphaComposite alphaC = null;
		if (alpha != 1f) {
			alphaC = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
			graphics.setComposite(alphaC);
		}

		// doit
		renderTo(degrees, blurredFactor, position, graphics, image);

		if (alpha != 1f) {
			alphaC = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
			graphics.setComposite(alphaC);
		}
	}

	public static void renderTo(double degrees, int blurredFactor,
			final Point position, final Graphics2D graphics,
			final VolatileImage image) {
		// ROTATION
		AffineTransform oldTransform = null;
		AffineTransform rotateTransform = null;
		if (degrees != 0.0d) {
			oldTransform = graphics.getTransform();
			rotateTransform = new AffineTransform();
			rotateTransform.rotate(Math.toRadians(degrees),
					position.x + (image.getWidth() / 2),
					position.y + (image.getHeight() / 2));
			graphics.setTransform(rotateTransform);
		}

		// DO IT
		renderTo(blurredFactor, position, graphics, image);

		if (degrees != 0.0d) {
			graphics.setTransform(oldTransform);
		}
	}

	public static void renderTo(int blurredFactor, final Point position,
			final Graphics2D graphics, final VolatileImage image) {
		if (blurredFactor != 0) {
			final GaussianFilter blurringOp = new GaussianFilter(blurredFactor);
			graphics.drawImage(image.getSnapshot(), blurringOp, position.x,
					position.y);
		} else {
			renderTo(position, graphics, image);
		}

	}

	public static void renderTo(final Point position,
			final Graphics2D graphics, final VolatileImage image) {

		// Draw not resized
		graphics.drawImage(image, position.x, position.y,
				position.x + image.getWidth(), position.y + image.getHeight(),
				0, 0, image.getWidth(), image.getHeight(), null);
	}

	/*
	 * END NEW!!!
	 */

	public static Color getColorFromHue(final int hue) {
		return Color.getHSBColor(Math.min(hue / 360.0f, 1f), 1f, 1f);
	}

	public static VolatileImage createVolatileImage(final int width,
			final int height) {
		return RenderToolkit.gConfig.createCompatibleVolatileImage(width,
				height, Transparency.TRANSLUCENT);

	}

	public static VolatileImage copyImage(final VolatileImage img) {
		final VolatileImage newImg = RenderToolkit.createVolatileImage(
				img.getWidth(), img.getHeight());
		final Graphics2D g = (Graphics2D) newImg.getGraphics();

		RenderToolkit.renderTo(new Point(0, 0), g, img);
		return newImg;
	}

	// public static BufferedImage copyImage(final BufferedImage img) {
	// final BufferedImage newImg = RenderToolkit.createBufferedImage(
	// img.getWidth(), img.getHeight());
	// final Graphics2D g = (Graphics2D) newImg.getGraphics();
	// RenderToolkit.renderTo(g, new Point(0, 0), img);
	// return newImg;
	// }

	// public static void renderGaussedImage(final float alpha,
	// final int blurredFactor, final Point position, double degrees,
	// final Graphics2D graphics, final BufferedImage image) {
	//
	//
	// AffineTransform oldTransform = graphics.getTransform();
	//
	// AffineTransform rotateTransform = new AffineTransform();
	// rotateTransform.rotate(degrees, image.getWidth() / 2,
	// image.getHeight() / 2);
	// graphics.setTransform(rotateTransform);
	//
	// renderGaussedImage(transparency, blurredFactor, position, graphics,
	// image);
	//
	// graphics.setTransform(oldTransform);
	//
	//
	//
	// }

	// public static void renderGaussedImage(final float transparency,
	// final int blurredFactor, final Point position,
	// final Graphics2D graphics, final BufferedImage image) {
	//
	// final BufferedImage imgIn = image;
	//
	// AlphaComposite alpha = AlphaComposite.getInstance(
	// AlphaComposite.SRC_OVER, transparency);
	// graphics.setComposite(alpha);
	//
	// final GaussianFilter blurringOp = new GaussianFilter(blurredFactor);
	// graphics.drawImage(imgIn, blurringOp, position.x, position.y);
	//
	// alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
	// graphics.setComposite(alpha);
	//
	// }

	// public static BufferedImage rotateImage(final VolatileImage src,
	// final double degrees) {
	// if (src == null) {
	// return null;
	// }
	//
	// if (degrees == 0) {
	// return src.getSnapshot();
	// }
	//
	// final BufferedImage rotatedImage = new BufferedImage(src.getWidth(),
	// src.getHeight(), BufferedImage.TYPE_INT_ARGB);
	//
	// final AffineTransform affineTransform = AffineTransform
	// .getRotateInstance(Math.toRadians(degrees), src.getWidth() / 2,
	// src.getHeight() / 2);
	//
	// final Graphics2D g = (Graphics2D) rotatedImage.getGraphics();
	//
	// g.setTransform(affineTransform);
	//
	// g.drawImage(src, 0, 0, null);
	//
	// return rotatedImage;
	// }
	//
	// public static BufferedImage rotateImage(final BufferedImage src,
	// final double degrees) {
	// if (src == null) {
	// return null;
	// }
	//
	// final AffineTransform affineTransform = AffineTransform
	// .getRotateInstance(Math.toRadians(degrees), src.getWidth() / 2,
	// src.getHeight() / 2);
	//
	// final BufferedImage rotatedImage = new BufferedImage(src.getWidth(),
	// src.getHeight(), BufferedImage.TYPE_INT_ARGB);
	//
	// final Graphics2D g = (Graphics2D) rotatedImage.getGraphics();
	//
	// g.setTransform(affineTransform);
	//
	// g.drawImage(src, 0, 0, null);
	//
	// g.dispose();
	//
	// return rotatedImage;
	// }

	// public static void renderTo(float alpha, final Graphics2D graphics,
	// final Point position, double degrees, final VolatileImage image) {
	// AffineTransform oldTransform = graphics.getTransform();
	//
	// AffineTransform rotateTransform = new AffineTransform();
	// rotateTransform.rotate(degrees, image.getWidth() / 2,
	// image.getHeight() / 2);
	// graphics.setTransform(rotateTransform);
	//
	// renderTo(graphics, position, image);
	//
	// graphics.setTransform(oldTransform);
	// }
	//
	// public static void renderTo(final Graphics2D graphics,
	// final Point position, final VolatileImage image) {
	// if (image == null) {
	// return;
	// }
	// graphics.drawImage(image, position.x, position.y,
	// position.x + image.getWidth(), position.y + image.getHeight(),
	// 0, 0, image.getWidth(), image.getHeight(), null);
	//
	// }

	// public static void renderTo(final Graphics2D graphics,
	// final Point position, final BufferedImage src) {
	// if (src == null) {
	// return;
	// }
	// graphics.drawImage(src, position.x, position.y,
	// position.x + src.getWidth(), position.y + src.getHeight(), 0,
	// 0, src.getWidth(), src.getHeight(), null);
	//
	// }

	// public static void renderTo(final Graphics2D graphics,
	// final Point position, final Dimension size, double degrees,
	// final VolatileImage image) {
	// AffineTransform oldTransform = graphics.getTransform();
	//
	// AffineTransform rotateTransform = new AffineTransform();
	// rotateTransform.rotate(degrees, image.getWidth() / 2,
	// image.getHeight() / 2);
	// graphics.setTransform(rotateTransform);
	//
	// renderTo(graphics, position, size, image);
	//
	// graphics.setTransform(oldTransform);
	// }
	//
	// public static void renderTo(final Graphics2D graphics,
	// final Point position, final Dimension size,
	// final VolatileImage image) {
	// if (image == null) {
	// return;
	// }
	//
	// graphics.drawImage(image, position.x, position.y, position.x
	// + size.width, position.y + size.height, 0, 0, image.getWidth(),
	// image.getHeight(), null);
	//
	// }

	// public static void renderTo(final Graphics2D graphics,
	// final Point position, final Dimension size, final BufferedImage src) {
	// RenderToolkit.renderTo(1f, graphics, position, size, src);
	// }

	// public static void renderTo(final float alpha, final Graphics2D graphics,
	// final Point position, double degrees, final BufferedImage src) {
	// if (src == null) {
	// return;
	// }
	// AlphaComposite alphaC = AlphaComposite.getInstance(
	// AlphaComposite.SRC_OVER, alpha);
	// graphics.setComposite(alphaC);
	// graphics.drawImage(src, position.x, position.y,
	// position.x + src.getWidth(), position.y + src.getHeight(), 0,
	// 0, src.getWidth(), src.getHeight(), null);
	// alphaC = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
	// graphics.setComposite(alphaC);
	// }

	// public static void renderTo(final float alpha, final Graphics2D graphics,
	// final Point position, final Dimension size, final BufferedImage src) {
	//
	// AlphaComposite alphaC = AlphaComposite.getInstance(
	// AlphaComposite.SRC_OVER, alpha);
	// graphics.setComposite(alphaC);
	// graphics.drawImage(src, position.x, position.y,
	// position.x + size.width, position.y + size.height, 0, 0,
	// src.getWidth(), src.getHeight(), null);
	// alphaC = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
	// graphics.setComposite(alphaC);
	//
	// }
	//
	// public static void renderTo(final float alpha, final Graphics2D graphics,
	// final Point position, final VolatileImage src) {
	// if (src == null) {
	// return;
	// }
	// AlphaComposite alphaC = AlphaComposite.getInstance(
	// AlphaComposite.SRC_OVER, alpha);
	// graphics.setComposite(alphaC);

	// graphics.drawImage(src, position.x, position.y,
	// position.x + src.getWidth(), position.y + src.getHeight(), 0,
	// 0, src.getWidth(), src.getHeight(), null);

	// alphaC = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
	// graphics.setComposite(alphaC);
	// }
	//
	// public static void renderTo(final float alpha, final Graphics2D graphics,
	// final Point position, final Dimension size, final VolatileImage src) {
	//
	// AlphaComposite alphaC = AlphaComposite.getInstance(
	// AlphaComposite.SRC_OVER, alpha);
	// graphics.setComposite(alphaC);
	// graphics.drawImage(src, position.x, position.y,
	// position.x + size.width, position.y + size.height, 0, 0,
	// src.getWidth(), src.getHeight(), null);
	// alphaC = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
	// graphics.setComposite(alphaC);
	//
	// }

	public static VolatileImage loadSprite(final File imageFile) {
		Image src = null;
		try {
			src = ImageIO.read(imageFile);
		} catch (final IOException e) {
			throw new RuntimeException("Cannot load image "
					+ imageFile.getAbsolutePath());
		}
		return copyImage(src);
	}

	public static VolatileImage loadSprite(final InputStream inputstream) {
		Image src = null;
		try {
			src = ImageIO.read(inputstream);
		} catch (final IOException e) {
			throw new RuntimeException("Cannot load image from stream.");
		}
		return copyImage(src);
	}

	public static VolatileImage loadSprite(final URL url) {
		Image src = null;
		try {
			src = ImageIO.read(url);
		} catch (final IOException e) {
			throw new RuntimeException("Cannot load image from url " + url.toString());
		}
		return copyImage(src);
	}

	public static VolatileImage copyImage(Image src) {

		final VolatileImage image = RenderToolkit.createVolatileImage(
				src.getWidth(null), src.getHeight(null));
		image.validate(RenderToolkit.gConfig);

		image.getGraphics().drawImage(src, 0, 0, null);
		return image;
	}

	public static VolatileImage convertSpriteToTransparentSprite(
			final VolatileImage src, final RGBImageFilter rgbFilter) {

		final ImageProducer imageprod = new FilteredImageSource(
				src.getSource(), rgbFilter);
		final Image transparentImage = Toolkit.getDefaultToolkit().createImage(
				imageprod);
		// BufferedImage image = new
		// BufferedImage(src.getWidth(null),src.getHeight(null),
		// BufferedImage.TYPE_INT_ARGB);

		final VolatileImage image = RenderToolkit.createVolatileImage(
				src.getWidth(null), src.getHeight(null));
		image.validate(RenderToolkit.gConfig);

		final Graphics2D g = image.createGraphics();

		g.setComposite(AlphaComposite.Src);

		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, image.getWidth(), image.getHeight()); // Clears the
		// image.

		g.drawImage(transparentImage, 0, 0, null);
		g.dispose();

		// image.getGraphics().drawImage(transparentImage, 0, 0, null);
		return image;
	}

	// public static BufferedImage convertSpriteToTransparentSprite(
	// final BufferedImage src, final RGBImageFilter rgbFilter) {
	//
	// final ImageProducer imageprod = new FilteredImageSource(
	// src.getSource(), rgbFilter);
	// final Image transparentImage = Toolkit.getDefaultToolkit().createImage(
	// imageprod);
	// // BufferedImage image = new
	// // BufferedImage(src.getWidth(null),src.getHeight(null),
	// // BufferedImage.TYPE_INT_ARGB);
	//
	// final BufferedImage image = RenderToolkit.createBufferedImage(
	// src.getWidth(null), src.getHeight(null));
	//
	// final Graphics2D g = image.createGraphics();
	//
	// g.setComposite(AlphaComposite.Src);
	//
	// g.setColor(new Color(0, 0, 0, 0));
	// g.fillRect(0, 0, image.getWidth(), image.getHeight()); // Clears the
	// // image.
	//
	// g.drawImage(transparentImage, 0, 0, null);
	// g.dispose();
	//
	// // image.getGraphics().drawImage(transparentImage, 0, 0, null);
	// return image;
	// }

	// public static BufferedImage createBufferedImage(final int width,
	// final int height) {
	// return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	//
	// }

	// public static BufferedImage resizeBufferedImage(final BufferedImage src,
	// final Dimension newSize) {
	// final BufferedImage image = new BufferedImage(newSize.width,
	// newSize.height, Transparency.BITMASK);
	// final Graphics2D graphics = image.createGraphics();
	// RenderToolkit.renderTo(graphics, new Point(0, 0), newSize, src);
	// graphics.dispose();
	// return image;
	// }

	public static VolatileImage resize(final VolatileImage src,
			final Dimension newSize) {
		final VolatileImage image = RenderToolkit.createVolatileImage(
				newSize.width, newSize.height);
		final Graphics2D graphics = image.createGraphics();
		RenderToolkit.renderTo(new Point(0, 0), newSize, graphics, src);
		graphics.dispose();
		return image;
	}

	public static int getHue(final Color transparencyColor) {
		final float[] hsbSrc = Color
				.RGBtoHSB(transparencyColor.getRed(),
						transparencyColor.getGreen(),
						transparencyColor.getBlue(), null);
		return Math2D.saveRound(hsbSrc[0] * 360);
	}

	/**
	 * Returns an instance of a default magenta filter with a hue range of 35
	 * degrees in HSB hue. and the default filter values.
	 * 
	 * @return
	 */
	public static HSBTransparencyFilter getDefaultHSBFilter() {
		final HSBTransparencyFilter filter = new HSBTransparencyFilter(300, 35);
		return filter;
	}

	/**
	 * @return Returns the default gradient filter from black (fully opaque) to
	 *         magenta (fully transluent).
	 */
	public static GradientTransparencyFilter getDefaultGradientFilter() {
		final GradientTransparencyFilter filter = new GradientTransparencyFilter(
				Color.BLACK, Color.MAGENTA);
		return filter;
	}

	/**
	 * @return Returns the default hue-brightness filter with a hue value of 300
	 *         and a brightness of 0.3f.
	 */
	public static HueBrightnessTransparencyFilter getDefaultHueBrightnessFilter() {
		final HueBrightnessTransparencyFilter filter = new HueBrightnessTransparencyFilter(
				300, 0.3f);
		return filter;
	}

	/**
	 * @return Returns the default transparency filter on the magenta color.
	 */
	public static TransparencyFilter getDefaultTransparencyFilter() {
		final TransparencyFilter filter = new TransparencyFilter(Color.MAGENTA);
		return filter;
	}

}
