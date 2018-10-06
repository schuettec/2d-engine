package de.schuette.cobra2D.ressource;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class URLClasspathHandler extends URLStreamHandler {
	/** The classloader to find resources from. */
	private final ClassLoader classLoader;

	public URLClasspathHandler() {
		this.classLoader = getClass().getClassLoader();
	}

	public URLClasspathHandler(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	@Override
	protected URLConnection openConnection(URL u) throws IOException {
		final URL resourceUrl = classLoader.getResource(u.getPath());

		if (resourceUrl == null) {
			throw new IOException("Ressource not found: " + u.toString());
		}
		return resourceUrl.openConnection();
	}
}