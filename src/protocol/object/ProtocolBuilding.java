package protocol.object;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import model.Index;

/**
 * <b>Gebäude (Buildings)</b>
 * <p>
 * Contains information about building: owner's ID, building type and location
 * ID.
 * </p>
 *
 */

@Since(1.0)
public class ProtocolBuilding {

	@SerializedName("Eigentümer")
	private int playerID;

	@SerializedName("Typ")
	private String building;

	@SerializedName("Ort")
	private Index[] locationID; // can be 2 or 3

	public ProtocolBuilding(int playerID, String building, Index[] locationID) {
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

	public Index[] getID() {
		return locationID;
	}

}
