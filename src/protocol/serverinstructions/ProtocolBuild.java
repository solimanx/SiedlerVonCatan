package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;

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
