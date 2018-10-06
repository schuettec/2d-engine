package de.schuette.cobra2D.ressource;

import java.awt.image.VolatileImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import de.schuette.cobra2D.rendering.RenderToolkit;

public class ImageMemory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(ImageMemory.class);

	private final List<Serializable[]> loadedImages;

	private transient HashMap<String, VolatileImage> images;

	private transient List<String> notFound;

	public ImageMemory() {
		this.images = new HashMap<String, VolatileImage>();
		this.notFound = new ArrayList<String>();
		this.loadedImages = new ArrayList<Serializable[]>();
	}

	public HashMap<String, URL> getKeyToUrlMapping() {
		HashMap<String, URL> mapping = new HashMap<String, URL>();

		for (int i = 0; i < this.loadedImages.size(); i++) {
			final Serializable[] loadItem = this.loadedImages.get(i);
			if (loadItem[0] instanceof String && loadItem[1] instanceof URL) {
				mapping.put((String) loadItem[0], (URL) loadItem[1]);
			} else {
				throw new RuntimeException(
						"Cannot load texture, level file or savegame corrupted!");
			}
		}

		return mapping;
	}

	/**
	 * This method is used to revive every member, after deserialization.
	 */
	public void afterDeserialization() {
		// Recreate transient members:
		this.images = new HashMap<String, VolatileImage>();
		this.notFound = new ArrayList<String>();

		for (int i = 0; i < this.loadedImages.size(); i++) {
			final Serializable[] loadItem = this.loadedImages.get(i);
			if (loadItem[0] instanceof String && loadItem[1] instanceof URL) {
				this.loadImageInternal((String) loadItem[0], (URL) loadItem[1]);
			} else {
				throw new RuntimeException(
						"Cannot load texture, level file or savegame corrupted!");
			}
		}
	}

	public void loadImage(final String address, final URL ressourceURL)
			throws ImageMemoryException {

		final Serializable[] loadItem = new Serializable[] { address,
				ressourceURL };
		this.loadedImages.add(loadItem);

		this.loadImageInternal(address, ressourceURL);
	}

	private void loadImageInternal(final String address, final URL ressourceURL)
			throws ImageMemoryException {

		VolatileImage image;
		try {
			InputStream inStream = RessourceUtil.getRessource(ressourceURL);
			image = RenderToolkit.loadSprite(inStream);
			try {
				inStream.close();
			} catch (Exception e) {
				// Nothing to do!
			}
		} catch (final Exception e) {
			throw new ImageMemoryException(e);
		}
		this.images.put(address, image);
	}

	public void clear() {
		this.images.clear();
	}

	public VolatileImage getImage(final String address) {
		if (!images.containsKey(address)) {
			if (!notFound.contains(address)) {

				log.error("Cannot find texture with key '" + address
						+ "'. Default no-texture returned.");
				notFound.add(address);
			}
			return NoTexture.getNoTextureImage();
		}
		final VolatileImage image = this.images.get(address);
		return image;

	}

	public VolatileImage remove(final String imageAddress) {
		return this.images.remove(imageAddress);
	}

	public HashMap<String, VolatileImage> getImages() {
		return this.images;
	}

	@Override
	public String toString() {
		String string = "ImageMemory has loaded following images:\n";
		final Iterator<String> keys = this.images.keySet().iterator();
		while (keys.hasNext()) {
			string += keys.next() + "\n";
		}

		return string;
	}

	private synchronized void readObject(final java.io.ObjectInputStream s)
			throws IOException, ClassNotFoundException {
		s.defaultReadObject();
		this.afterDeserialization();
	}

}
