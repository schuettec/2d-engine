package de.schuette.cobra2D.system;

/**
 * Constants used by the engine to read values from the config file, or
 * determine the timeouts of reading and connection to ressources. You can use
 * the maven replace mechanism to replace those values by an external
 * configuration.
 * 
 * @author Chris
 * 
 */
public class Cobra2DConstants {

	public enum RessourceType {
		CLASSPATH("classpath"), INSTALL_DIR("install-dir");

		private String protocol;

		private RessourceType(String protocol) {
			this.protocol = protocol;
		}

		@Override
		public String toString() {
			return protocol;
		}
	}

	/**
	 * This constant determines the read timeout of all ressources accessed by
	 * URLs.
	 */
	public static final int READ_TIMEOUT = 3000;
	/**
	 * This constant determines the timeout of creating a connection to the
	 * ressources accessed by URLs.
	 */
	public static final int CONNECTION_TIMEOUT = 3000;

	/**
	 * This constant defines the property key, used to read the x-resolution
	 * from the configuration file or properties.
	 */
	public static final String RESOLUTION_X = "resolution-x";
	/**
	 * This constant defines the property key, used to read the y-resolution
	 * from the configuration file or properties.
	 */
	public static final String RESOLUTION_Y = "resolution-y";
	/**
	 * This constant defines the property key, used to read the bit-depht of the
	 * renderer from the configuration file or properties.
	 */
	public static final String BIT_DEPHT = "bit-depht";
	/**
	 * This constant defines the property key, used to read the refresh rate of
	 * the screen from the configuration file or properties.
	 */
	public static final String REFRESH_REATE = "refresh-rate";
	/**
	 * This constant defines the property key, used to read the requested frames
	 * per second from the configuration file or properties.
	 */
	public static final String REQUESTED_FPS = "fps";
	/**
	 * This constant defines the property key, used to read the fullscreen flag
	 * from the configuration file or properties.
	 */
	public static final String FULLSCREEN = "fullscreen";
	/**
	 * This constant defines the property key, used to read the map-update flag
	 * from the configuration file or properties.
	 */
	public static final String MAP_UPDATE = "map-update";
	/**
	 * This constant defines the property key, used to read the use-renderer
	 * flag from the configuration file or properties.
	 */
	public static final String USE_RENDERER = "use-renderer";
	/**
	 * This constant defines the property key, used to read the keyboard
	 * controller from the configuration file or properties.
	 */
	public static final String DEFAULT_CONTROLLER = "keyboard-controller";

}
