package model.protocol;

import com.google.gson.annotations.SerializedName;

import model.Field;

public class Board {
	
	@SerializedName("Felder")
	Field[] fields;
	
	@SerializedName("Gebäude")
	Building[] buildings;
	
	@SerializedName("Häfen")
	Harbour[] harbours;
	
	@SerializedName("Räuber")
	String robber_location;

	public Board(Field[] fields, Building[] buildings, Harbour[] harbours, String robber_location) {
		this.fields = fields;
		this.buildings = buildings;
		this.harbours = harbours;
		this.robber_location = robber_location;
	}

	public Field[] getFields() {
		return fields;
	}

	public Building[] getBuildings() {
		return buildings;
	}

	public Harbour[] getHarbours() {
		return harbours;
	}

	public String getRobber_location() {
		return robber_location;
	}

	public void setFields(Field[] fields) {
		this.fields = fields;
	}

	public void setBuildings(Building[] buildings) {
		this.buildings = buildings;
	}

	public void setHarbours(Harbour[] harbours) {
		this.harbours = harbours;
	}

	public void setRobber_location(String robber_location) {
		this.robber_location = robber_location;
	}

}
