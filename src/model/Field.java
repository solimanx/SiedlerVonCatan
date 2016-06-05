package model;

import enums.ResourceType;

public class Field {

	private String id;

	private ResourceType resourceType;

	private Integer diceIndex;

	public Field() {
		resourceType = ResourceType.NOTHING;
		diceIndex = null;
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

	public Integer getDiceIndex() {
		return diceIndex;
	}

	public void setDiceIndex(Integer diceIndex) {
		this.diceIndex = diceIndex;
	}

}
