package model;

import com.google.gson.annotations.Since;

import enums.ResourceType;

public class Field {
	
	@Since(0.1)
	private String id;
	
	@Since(0.1)
	private ResourceType resourceType;
	
	@Since(0.1)
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
