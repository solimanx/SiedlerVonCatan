package protocol.clientinstructions;

import com.google.gson.annotations.SerializedName;

public class ProtocolBuildRequest {

	@SerializedName("Bauen")
	private String build;
	@SerializedName("Typ")
	private String type;
	@SerializedName("Ort")
	private String location;

	public ProtocolBuildRequest(String sendMessage, String message) {

		this.build = build;
		this.type = type;
		this.location = location;
	}

	public String getBuild() {
		return build;
	}

	public String getType() {
		return type;
	}

	public String getLocation() {
		return location;
	}

}
