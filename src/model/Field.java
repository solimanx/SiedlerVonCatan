package model;

import enums.ResourceType;

public class Field {

    int offsetX;
    int offsetY;

    ResourceType resourceType;
    int diceIndex;

    public Field() {
	resourceType = ResourceType.NOTHING; // TODO Constructor
    }

    public int getOffsetX() {
	return offsetX;
    }

    public int getOffsetY() {
	return offsetY;
    }

    public ResourceType getResourceType() {
	return resourceType;
    }

    public int getDiceIndex() {
	return diceIndex;
    }

    public void setOffsetX(int offsetX) {
	this.offsetX = offsetX;
    }

    public void setOffsetY(int offsetY) {
	this.offsetY = offsetY;
    }

    public void setResourceType(ResourceType resourceType) {
	this.resourceType = resourceType;
    }

    public void setDiceIndex(int diceIndex) {
	this.diceIndex = diceIndex;
    }

}
