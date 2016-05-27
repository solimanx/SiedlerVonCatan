package protocol.configuration;

import com.google.gson.annotations.SerializedName;

import protocol.object.ProtocolResource;

public class ProtocolGameStarted {

	@SerializedName("Karte")
	private ProtocolResource resources;

	public ProtocolGameStarted() {

		this.resources = resources;

	}

	public ProtocolResource getResources() {
		return resources;
	}

}
