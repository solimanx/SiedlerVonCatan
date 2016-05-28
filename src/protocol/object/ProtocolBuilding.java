package protocol.object;

import com.google.gson.annotations.SerializedName;

public class ProtocolBuilding {

	@SerializedName("Eigentümer")
	int player_id;

	@SerializedName("Typ")
	String building;

	@SerializedName("Ort")
	String id;

	public ProtocolBuilding(int player_id, String building, String id) {
		this.player_id = player_id;
		this.building = building;
		this.id = id;
	}

	public int getPlayer_id() {
		return player_id;
	}

	public String getBuilding() {
		return building;
	}

	public String getId() {
		return id;
	}

}
