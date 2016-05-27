package protocol.clientinstructions;

import com.google.gson.annotations.SerializedName;

import enums.protocol.BuildingType;

public class ProtocolBuildRequest {

	@SerializedName("Bauen")
	private String build;
	@SerializedName("Typ")
	private BuildingType building;
	@SerializedName("Ort")
	private String location;

	public ProtocolBuildRequest(String sendMessage, String message) {

		this.build = build;
		this.building = building;
		this.location = location;
	}

	public String getBuild() {
		return build;
	}

	public BuildingType getBuilding() {
		return building;
	}

	public String getLocation() {
		return location;
	}

}
