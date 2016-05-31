package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import protocol.object.ProtocolBuilding;

/**
 * <b>Bauvorgang (Build Process)</b>
 * <p>
 * Server sends to client the newly created building object.
 * </p>
 * 
 */

@Since(0.1)
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
