package de.schuette.cobra2D.ressource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import de.schuette.cobra2D.system.Cobra2DConstants;

public class RessourceUtil {

	/**
	 * Returns an InputStream to a given URL using the timeout constants of
	 * Cobra2DConstants.
	 * 
	 * @param ressourceURL
	 * @throws IOException
	 */
	public static InputStream getRessource(URL ressourceURL) throws IOException {
		URLConnection connection = ressourceURL.openConnection();
		connection.setReadTimeout(Cobra2DConstants.READ_TIMEOUT);
		connection.setConnectTimeout(Cobra2DConstants.CONNECTION_TIMEOUT);
		InputStream inStream = connection.getInputStream();
		return inStream;
	}
}
