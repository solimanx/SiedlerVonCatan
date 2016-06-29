package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import protocol.object.ProtocolBuilding;

// TODO: Auto-generated Javadoc
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

	/**
	 * Instantiates a new protocol build.
	 *
	 * @param building the building
	 */
	public ProtocolBuild(ProtocolBuilding building) {
		this.building = building;
	}

	/**
	 * Gets the building.
	 *
	 * @return the building
	 */
	public ProtocolBuilding getBuilding() {
		return building;
	}
}
