package protocol.connection;

import com.google.gson.annotations.SerializedName;

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