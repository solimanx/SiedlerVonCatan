package model.objects;

// TODO: Auto-generated Javadoc
/**
 * All properties of an edge object, each edge represents a road/street in the
 * board.
 */
public class Edge {

	// ================================================================================
	// CLASS FIELDS
	// ================================================================================

	// Whether the edge is occupied or not
	private boolean hasStreet;
	// The 31-bit-integer ID of an owner
	private Integer ownerID;
	// A 2-character long ID for the edge, (Two fields make an edge)
	private String edgeID;

	// ================================================================================
	// CONSTRUCTOR
	// ================================================================================

	/**
	 * Creates a primitive constructor for edge.
	 */
	public Edge() {
		hasStreet = false;
	}

	// ================================================================================
	// GETTERS
	// ================================================================================

	/**
	 * Checks if is checks for street.
	 *
	 * @return true, if is checks for street
	 */
	public boolean isHasStreet() {
		return hasStreet;
	}

	/**
	 * Gets the owner ID.
	 *
	 * @return the owner ID
	 */
	public Integer getOwnerID() {
		return ownerID;
	}

	/**
	 * Gets the edge ID.
	 *
	 * @return the edge ID
	 */
	public String getEdgeID() {
		return edgeID;
	}

	// ================================================================================
	// SETTERS
	// ================================================================================

	/**
	 * Sets the checks for street.
	 *
	 * @param hasStreet the new checks for street
	 */
	public void setHasStreet(boolean hasStreet) {
		this.hasStreet = hasStreet;
	}

	/**
	 * Sets the owned by player.
	 *
	 * @param ownerID the new owned by player
	 */
	public void setOwnedByPlayer(int ownerID) {
		this.ownerID = ownerID;
	}

	/**
	 * Sets the edge ID.
	 *
	 * @param edgeID the new edge ID
	 */
	public void setEdgeID(String edgeID) {
		this.edgeID = edgeID;
	}
}
