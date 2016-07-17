package model.board;

import enums.ResourceType;

// TODO: Auto-generated Javadoc
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
	 * Creates a primitive field object.
	 */
	public Field() {
		resourceType = ResourceType.NOTHING;
		diceIndex = null;
	}

	/**
	 * Creates a detailed field object.
	 *
	 * @param id the id
	 * @param resourceType the resource type
	 * @param diceIndex the dice index
	 */
	public Field(String id, ResourceType resourceType, Integer diceIndex) {
		this.id = id;
		this.resourceType = resourceType;
		this.diceIndex = diceIndex;
	}

	// ================================================================================
	// GETTERS
	// ================================================================================

	/**
	 * Gets the field ID.
	 *
	 * @return the field ID
	 */
	public String getFieldID() {
		return id;
	}

	/**
	 * Gets the resource type.
	 *
	 * @return the resource type
	 */
	public ResourceType getResourceType() {
		return resourceType;
	}

	/**
	 * Gets the dice index.
	 *
	 * @return the dice index
	 */
	public Integer getDiceIndex() {
		return diceIndex;
	}

	// ================================================================================
	// SETTERS
	// ================================================================================

	/**
	 * Sets the field ID.
	 *
	 * @param id the new field ID
	 */
	public void setFieldID(String id) {
		this.id = id;
	}

	/**
	 * Sets the resource type.
	 *
	 * @param resourceType the new resource type
	 */
	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	/**
	 * Sets the dice index.
	 *
	 * @param diceIndex the new dice index
	 */
	public void setDiceIndex(Integer diceIndex) {
		this.diceIndex = diceIndex;
	}
}
