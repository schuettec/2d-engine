package de.schuette.cobra2D.ressource;

import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import de.schuette.cobra2D.rendering.RenderToolkit;

public class NoTexture {
	/**
	 * Amount of bytes that can be maximal read in one step.
	 */
	private static final int BUFFER_SIZE = 2048;

	public static void main(String... args) throws Exception {
		String filename = args[0];

		if (filename == null || filename.trim().isEmpty()) {
			System.out
					.println("Please start this application with a filename of a picture.");
			System.exit(1);
		}

		FileInputStream in = new FileInputStream(new File(filename));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		boolean run = true;
		while (run) {
			byte[] data = new byte[BUFFER_SIZE];
			int readAmount = in.read(data, 0, data.length);
			if (readAmount == -1) {
				run = false;
				continue;
			}
			try {
				bos.write(data, 0, readAmount);
			} catch (Exception e) {
				throw new Exception("Error occurred in reader callback.", e);
			}
		}
		try {
			in.close();
		} catch (Exception e) {
			// Nothing to do.
		}

		byte[] bytes = bos.toByteArray();
		String base64Binary = DatatypeConverter.printBase64Binary(bytes);

		System.out.println(base64Binary);

	}

	public static int getNoTextureWidth() {
		return 400;
	}

	public static int getNoTextureHeight() {
		return 400;
	}

	public static VolatileImage getNoTextureImage() {
		createVolatileImageOnce();
		return NO_IMAGE_TEXTURE;
	}

	private static void createVolatileImageOnce() {
		if (NO_IMAGE_TEXTURE == null) {
			byte[] base64 = DatatypeConverter
					.parseBase64Binary(NO_IMAGE_BASE64);
			ByteArrayInputStream in = new ByteArrayInputStream(base64);
			try {
				BufferedImage read = ImageIO.read(in);
				VolatileImage copyImage = RenderToolkit.copyImage(read);
				NO_IMAGE_TEXTURE = copyImage;
			} catch (IOException e) {
				// Will never happen
			}
		}
	}

	public static VolatileImage NO_IMAGE_TEXTURE;
	public static final String NO_IMAGE_BASE64 = "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";
}
