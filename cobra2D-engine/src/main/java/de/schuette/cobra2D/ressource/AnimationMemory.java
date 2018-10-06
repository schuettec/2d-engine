package de.schuette.cobra2D.ressource;

import java.awt.image.VolatileImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import de.schuette.cobra2D.system.Cobra2DEngine;

public class AnimationMemory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient HashMap<String, Animation> images = new HashMap<String, Animation>();

	private transient List<String> notFound;

	private transient ImageMemory imageMemory;

	private final List<Serializable[]> loadedAnimations;

	private static final Logger log = Logger.getLogger(AnimationMemory.class);

	public HashMap<String, Animation> getAnimations() {
		return new HashMap<String, Animation>(images);
	}

	public AnimationMemory(final ImageMemory imageMemory) {
		this.imageMemory = imageMemory;
		this.notFound = new ArrayList<String>();

		this.loadedAnimations = new ArrayList<Serializable[]>();
	}

	/**
	 * This method is used to revive every member, after deserialization.
	 */
	public void afterDeserialization(final Cobra2DEngine engine) {
		// Recreate transient members:
		this.imageMemory = engine.getImageMemory();
		this.notFound = new ArrayList<String>();
		this.images = new HashMap<String, Animation>();

		for (int i = 0; i < loadedAnimations.size(); i++) {
			Serializable[] loadItem = loadedAnimations.get(i);
			if (loadItem[0] instanceof String && loadItem[1] instanceof String) {

				try {
					int width = Integer.parseInt(((Integer) loadItem[2])
							.toString());
					int height = Integer.parseInt(((Integer) loadItem[3])
							.toString());
					loadAnimationInternal((String) loadItem[0],
							(String) loadItem[1], width, height);
				} catch (Exception e) {
					throw new RuntimeException(
							"Cannot load animation, level file or savegame corrupted!",
							e);
				}

			} else {
				throw new RuntimeException(
						"Cannot load texture, level file or savegame corrupted!");
			}
		}
	}

	public void clear() {
		images.clear();
	}

	public void loadAnimation(String address, String texturAddress, int width,
			int height) {
		loadedAnimations.add(new Serializable[] { address, texturAddress,
				width, height });

		loadAnimationInternal(address, texturAddress, width, height);
	}

	private void loadAnimationInternal(String address, String texturAddress,
			int width, int height) {

		VolatileImage image = imageMemory.getImage(texturAddress);

		Animation animation = new Animation(image, width, height);
		images.put(address, animation);
	}

	public Animation getAnimation(String address) {
		// Vorgeladene Animationen Klonen ist KEINE GUTE IDEE
		if (!images.containsKey(address)) {
			if (!notFound.contains(address)) {
				log.error("Cannot find animation with key '" + address
						+ "'. Default no-texture animation returned.");
				notFound.add(address);
			}
			return new NotFoundAnimation();

		} else {
			return images.get(address);
		}
		// return new Animation(images.get(address).getTexturAddress(),
		// images.get(address).getWidth(), images.get(address).getHeight(),
		// images.get(address).getTransparency());
	}

	public Animation remove(String animationAddress) {
		return images.remove(animationAddress);
	}

}
