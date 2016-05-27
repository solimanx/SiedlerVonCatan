package protocol.object;

import com.google.gson.annotations.SerializedName;

import model.Field;

public class ProtocolBoard {

	@SerializedName("Felder")
	ProtocolField[] fields;

	@SerializedName("Gebäude")
	ProtocolBuilding[] buildings;

	@SerializedName("Häfen")
	ProtocolHarbour[] harbours;

	@SerializedName("Räuber")
	String robber_location;

	public ProtocolBoard(ProtocolField[] fields, ProtocolBuilding[] buildings, ProtocolHarbour[] harbours,
			String robber_location) {
		this.fields = fields;
		this.buildings = buildings;
		this.harbours = harbours;
		this.robber_location = robber_location;
	}

	public ProtocolField[] getFields() {
		return fields;
	}

	public ProtocolBuilding[] getBuildings() {
		return buildings;
	}

	public ProtocolHarbour[] getHarbours() {
		return harbours;
	}

	public String getRobber_location() {
		return robber_location;
	}

}
