package protocol.object;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import model.unit.Index;

// TODO: Auto-generated Javadoc
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

	/**
	 * Instantiates a new protocol building.
	 *
	 * @param playerID the player ID
	 * @param building the building
	 * @param locationID the location ID
	 */
	public ProtocolBuilding(int playerID, String building, Index[] locationID) {
		this.playerID = playerID;
		this.building = building;
		this.locationID = locationID;
	}

	/**
	 * Gets the player ID.
	 *
	 * @return the player ID
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return building;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Index[] getID() {
		return locationID;
	}

}
