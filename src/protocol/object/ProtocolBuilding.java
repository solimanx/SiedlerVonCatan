package protocol.object;

import com.google.gson.annotations.SerializedName;

import enums.protocol.BuildingType;

public class ProtocolBuilding {

	@SerializedName("Eigentümer")
	int player_id;
	
	@SerializedName("Typ")
	BuildingType building;
	
	@SerializedName("Ort")
	String id;

	public ProtocolBuilding(int player_id, BuildingType building, String id) {
		this.player_id = player_id;
		this.building = building;
		this.id = id;
	}

	public int getPlayer_id() {
		return player_id;
	}

	public BuildingType getBuilding() {
		return building;
	}

	public String getId() {
		return id;
	}

	public void setPlayer_id(int player_id) {
		this.player_id = player_id;
	}

	public void setBuilding(BuildingType building) {
		this.building = building;
	}

	public void setId(String id) {
		this.id = id;
	}

	
}
