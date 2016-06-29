package protocol.connection;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

// TODO: Auto-generated Javadoc
/**
 * <b>Verbindungsaufbau (Logging in : Hello)</b>
 * <p>
 * Version and protocol information.
 * </p>
 * 
 */

@Since(0.1)
public class ProtocolHello {

	@SerializedName("Version")
	private String version;

	@SerializedName("Protokoll")
	private String protocol;

	/**
	 * Instantiates a new protocol hello.
	 *
	 * @param version the version
	 */
	public ProtocolHello(String version) {
		this.version = version;
	}

	/**
	 * Instantiates a new protocol hello.
	 *
	 * @param version the version
	 * @param protocol the protocol
	 */
	public ProtocolHello(String version, String protocol) {
		this.version = version;
		this.protocol = protocol;
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Gets the protocol.
	 *
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

}