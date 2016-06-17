package model.objects;

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
	 * Creates a primitive constructor for edge
	 */
	public Edge() {
		hasStreet = false; 
	}

	// ================================================================================
	// GETTERS
	// ================================================================================

	public boolean isHasStreet() {
		return hasStreet;
	}

	public Integer getOwnerID() {
		return ownerID;
	}

	public String getEdgeID() {
		return edgeID;
	}

	// ================================================================================
	// SETTERS
	// ================================================================================

	public void setHasStreet(boolean hasStreet) {
		this.hasStreet = hasStreet;
	}

	public void setOwnedByPlayer(int ownerID) {
		this.ownerID = ownerID;
	}

	public void setEdgeID(String edgeID) {
		this.edgeID = edgeID;
	}
}
