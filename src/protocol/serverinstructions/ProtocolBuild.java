package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;

import protocol.object.ProtocolBuilding;

public class ProtocolBuild {

	@SerializedName("Gebäude")

	private ProtocolBuilding building;

	public ProtocolBuild(ProtocolBuilding building) {

		this.building = building;
	}

	public ProtocolBuilding getBuilding() {

		return building;
	}
}
