package protocol.object;

import com.google.gson.annotations.SerializedName;

/**
 * <b>4.2 Gebäude</b>
 * <p>
 * Contains information about building: owner's ID, building type and location
 * ID.
 * </p>
 * 
 * @version 0.1
 * 
 */
public class ProtocolBuilding {

	@SerializedName("Eigentümer")
	int player_id;

	@SerializedName("Typ")
	String building;

	@SerializedName("Ort")
	String location_id;

	public ProtocolBuilding(int player_id, String building, String location_id) {
		this.player_id = player_id;
		this.building = building;
		this.location_id = location_id;
	}

	public int getPlayer_id() {
		return player_id;
	}

	public String getBuilding() {
		return building;
	}

	public String getId() {
		return location_id;
	}

}
