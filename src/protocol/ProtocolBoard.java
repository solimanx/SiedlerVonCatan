package protocol;

import com.google.gson.annotations.SerializedName;

import model.Field;

public class ProtocolBoard {
	
	@SerializedName("Felder")
	Field[] fields;
	
	@SerializedName("Gebäude")
	ProtocolBuilding[] buildings;
	
	@SerializedName("Häfen")
	ProtocolHarbour[] harbours;
	
	@SerializedName("Räuber")
	String robber_location;

	public ProtocolBoard(Field[] fields, ProtocolBuilding[] buildings, ProtocolHarbour[] harbours, String robber_location) {
		this.fields = fields;
		this.buildings = buildings;
		this.harbours = harbours;
		this.robber_location = robber_location;
	}

	public Field[] getFields() {
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

	public void setFields(Field[] fields) {
		this.fields = fields;
	}

	public void setBuildings(ProtocolBuilding[] buildings) {
		this.buildings = buildings;
	}

	public void setHarbours(ProtocolHarbour[] harbours) {
		this.harbours = harbours;
	}

	public void setRobber_location(String robber_location) {
		this.robber_location = robber_location;
	}

}
