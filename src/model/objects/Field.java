package model.objects;

import enums.ResourceType;

/**
 * All properties of a field object, each field represents a hexagon in the
 * board.
 */
public class Field {

	// ================================================================================
	// CLASS FIELDS
	// ================================================================================

	// The ID for the field (one character)
	private String id;

	// The type of field
	private ResourceType resourceType;

	// The dice number displayed on a field
	private Integer diceIndex;

	// ================================================================================
	// CONSTRUCTORS
	// ================================================================================

	/**
	 * Creates a primitive field object
	 */
	public Field() {
		resourceType = ResourceType.NOTHING;
		diceIndex = null;
	}

	/**
	 * Creates a detailed field object
	 * 
	 * @param id
	 * @param resourceType
	 * @param diceIndex
	 */
	public Field(String id, ResourceType resourceType, Integer diceIndex) {
		this.id = id;
		this.resourceType = resourceType;
		this.diceIndex = diceIndex;
	}

	// ================================================================================
	// GETTERS
	// ================================================================================

	public String getFieldID() {
		return id;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public Integer getDiceIndex() {
		return diceIndex;
	}

	// ================================================================================
	// SETTERS
	// ================================================================================

	public void setFieldID(String id) {
		this.id = id;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public void setDiceIndex(Integer diceIndex) {
		this.diceIndex = diceIndex;
	}
}
