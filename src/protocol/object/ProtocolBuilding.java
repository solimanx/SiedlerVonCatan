package protocol.object;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * <b>Gebäude (Buildings)</b>
 * <p>
 * Contains information about building: owner's ID, building type and location
 * ID.
 * </p>
 * 
 * @since 0.1
 * 
 */

@Since(0.1)
public class ProtocolBuilding {

	@SerializedName("Eigentümer")
	private int playerID;

	@SerializedName("Typ")
	private String building;

	@SerializedName("Ort")
	private String locationID;

	public ProtocolBuilding(int playerID, String building, String locationID) {
		this.playerID = playerID;
		this.building = building;
		this.locationID = locationID;
	}

	public int getPlayerID() {
		return playerID;
	}

	public String getType() {
		return building;
	}

	public String getID() {
		return locationID;
	}

}
