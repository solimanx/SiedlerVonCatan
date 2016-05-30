package protocol.connection;

import com.google.gson.annotations.SerializedName;

/**
 * <b>3: Verbindungsaufbau</b>
 * <p>
 * Version and protocol information.
 * </p>
 * 
 * @version 0.1
 * 
 */
public class ProtocolHello {

	@SerializedName("Version")
	private String version;

	@SerializedName("Protokoll")
	private String protocol;

	public ProtocolHello(String version, String protocol) {
		this.version = version;
		this.protocol = protocol;
	}

	public String getVersion() {
		return version;
	}

	public String getProtocol() {
		return protocol;
	}

}