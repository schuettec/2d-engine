package de.schuette.cobra2D.ressource;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * @author Chris This URLStreamHandler supports the pseudo-protocol "resource:".
 *         This protocol is used as the switch between "resource:" and
 *         "classpath:" protocol. It returns URLs with the target protocol given
 *         by the constructor.
 */
public class URLResourceTypeHandler extends URLStreamHandler {

	protected String targetProtocol;

	public URLResourceTypeHandler(String targetProtocol) {
		this.targetProtocol = targetProtocol;
	}

	@Override
	protected URLConnection openConnection(URL u) throws IOException {
		URL targetURL = new URL(targetProtocol, u.getHost(), u.getFile());
		return targetURL.openConnection();
	}
}