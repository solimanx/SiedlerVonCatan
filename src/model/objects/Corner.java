package model.objects;

import enums.CornerStatus;
import enums.HarbourStatus;

/**
 * All properties of a corner object, each corner represents a building or a
 * harbour in the board.
 */
public class Corner {
	// ================================================================================
	// CLASS FIELDS
	// ================================================================================

	// Whether the corner is occupied, blocked, and type of building it has.
	private CornerStatus status;
	// Whether the corner is a harbour, and the type of the harbour
	private HarbourStatus harbourStatus;
	// The 31-bit-integer ID of an owner
	private int ownerID;
	// A 3-character long ID for the corner, (Three fields make a corner)
	private String cornerID;

	// ================================================================================
	// CONSTRUCTOR
	// ================================================================================

	/**
	 * Creates a corner object, initially no corner status or harbour status
	 * type
	 */
	public Corner() {
		status = CornerStatus.EMPTY;
		harbourStatus = HarbourStatus.NULL;
	}

	// ================================================================================
	// GETTERS
	// ================================================================================

	public CornerStatus getStatus() {
		return status;
	}

	public HarbourStatus getHarbourStatus() {
		return harbourStatus;
	}

	public int getOwnerID() {
		return ownerID;
	}

	public String getCornerID() {
		return this.cornerID;
	}

	// ================================================================================
	// SETTERS
	// ================================================================================

	public void setStatus(CornerStatus status) {
		this.status = status;
	}

	public void setHarbourStatus(HarbourStatus harbourStatus) {
		this.harbourStatus = harbourStatus;
	}

	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

	public void setCornerID(String cornerID) {
		this.cornerID = cornerID;
	}

}
