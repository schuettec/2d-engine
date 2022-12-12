package de.schuette.cobra2D.ressource;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chris Registry for StreamHandlers to extend the supported URL
 *         protocols of the JVM by custom protocols.
 */
public class URLStreamHandlerRegistry implements URLStreamHandlerFactory {

	private final Map<String, URLStreamHandler> protocolHandlers;

	public URLStreamHandlerRegistry() {
		protocolHandlers = new HashMap<String, URLStreamHandler>();
	}

	public void addHandler(String protocol, URLStreamHandler urlHandler) {
		protocolHandlers.put(protocol, urlHandler);
	}

	public URLStreamHandler createURLStreamHandler(String protocol) {
		return protocolHandlers.get(protocol);
	}
}
