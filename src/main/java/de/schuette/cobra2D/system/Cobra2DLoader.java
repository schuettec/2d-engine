package de.schuette.cobra2D.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Cobra2DLoader {

	public Cobra2DLoader() {
	}

	public Cobra2DLevel loadLevel(final Cobra2DEngine engine,
			final File levelFile) throws Exception {
		final Object object = Cobra2DLoader.deserialize(levelFile);
		if (object instanceof Cobra2DLevel) {
			final Cobra2DLevel level = (Cobra2DLevel) object;
			level.afterDeserialization(engine);
			return level;
		} else {
			throw new Exception("Loaded object is not a Cobra2D level.");
		}

	}

	public void saveLevel(final Cobra2DLevel level, final File levelFile)
			throws Exception {
		Cobra2DLoader.serialize(level, levelFile);
	}

	/**
	 * Util method to serialize object to XML file.
	 * 
	 * @param obj
	 *            The object to serialize
	 * @param file
	 *            the file to serialize to
	 * @throws IOException
	 *             If an error occurs while serializing the object.
	 */
	protected static void serialize(final Object obj, final File file)
			throws Exception {
		final FileOutputStream fOut = new FileOutputStream(file);
		final XStream xstream = new XStream(new DomDriver());
		xstream.toXML(obj, fOut);
		fOut.close();
	}

	/**
	 * Util method to deserialize objects from XML file.
	 * 
	 * @param file
	 *            The file to read object from.
	 * @return Returns the object described in xml file.
	 * @throws Exception
	 *             Throws exception if an error occurs while deserializing.
	 */
	protected static Object deserialize(final File file) throws Exception {
		final FileInputStream fIn = new FileInputStream(file);
		final XStream xstream = new XStream(new DomDriver());
		final Object obj = xstream.fromXML(fIn);
		fIn.close();
		return obj;
	}
}
