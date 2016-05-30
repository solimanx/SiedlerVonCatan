package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;

import protocol.object.ProtocolBuilding;

/**
 * <b>7.4 Bauvorgang </b>
 * <p>
 * Server sends to client the newly created building object.
 * </p>
 * 
 * @version 0.1
 * 
 */
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
