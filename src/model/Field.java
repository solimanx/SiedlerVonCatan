package model;

import enums.ResourceType;

public class Field {

	private String id;
	private ResourceType resourceType;
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

	public int getDiceIndex() {
		return diceIndex;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public void setDiceIndex(int diceIndex) {
		this.diceIndex = diceIndex;
	}

}
