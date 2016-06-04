package model;

import com.google.gson.annotations.SerializedName;

import enums.ResourceType;

public class Field {

	@SerializedName("Ort")
	private String id;

	@SerializedName("Typ")
	private ResourceType resourceType;

	@SerializedName("Zahl")
	private int diceIndex;

	public Field() {
		resourceType = ResourceType.NOTHING; // TODO Constructor
	}

	public String getFieldID() {
		return id;
	}

	public void setFieldID(String id) {
		this.id = id;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public int getDiceIndex() {
		return diceIndex;
	}

	public void setDiceIndex(int diceIndex) {
		this.diceIndex = diceIndex;
	}

}
