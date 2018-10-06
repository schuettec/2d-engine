package de.schuette.cobra2D.ressource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class URLInstallDirectoryHandler extends URLStreamHandler {

	protected File currentDirectory;

	public URLInstallDirectoryHandler() {
		currentDirectory = new File(".");
	}

	@Override
	protected URLConnection openConnection(URL u) throws IOException {

		File file = new File(currentDirectory.getAbsolutePath()
				+ File.separator + u.getFile());

		URL absoluteURL = new URL("file", "", file.getAbsolutePath());

		return absoluteURL.openConnection();
	}
}