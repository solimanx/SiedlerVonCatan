package model.board;

import enums.CornerStatus;
import enums.HarbourStatus;

// TODO: Auto-generated Javadoc
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
	private Integer ownerID;
	// A 3-character long ID for the corner, (Three fields make a corner)
	private String cornerID;

	// ================================================================================
	// CONSTRUCTOR
	// ================================================================================

	/**
	 * Creates a corner object, initially no corner status or harbour status
	 * type.
	 */
	public Corner() {
		status = CornerStatus.EMPTY;
		harbourStatus = HarbourStatus.NULL;
	}

	// ================================================================================
	// GETTERS
	// ================================================================================

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public CornerStatus getStatus() {
		return status;
	}

	/**
	 * Gets the harbour status.
	 *
	 * @return the harbour status
	 */
	public HarbourStatus getHarbourStatus() {
		return harbourStatus;
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
	 * Gets the corner ID.
	 *
	 * @return the corner ID
	 */
	public String getCornerID() {
		return this.cornerID;
	}

	// ================================================================================
	// SETTERS
	// ================================================================================

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(CornerStatus status) {
		this.status = status;
	}

	/**
	 * Sets the harbour status.
	 *
	 * @param harbourStatus the new harbour status
	 */
	public void setHarbourStatus(HarbourStatus harbourStatus) {
		this.harbourStatus = harbourStatus;
	}

	/**
	 * Sets the owner ID.
	 *
	 * @param ownerID the new owner ID
	 */
	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

	/**
	 * Sets the corner ID.
	 *
	 * @param cornerID the new corner ID
	 */
	public void setCornerID(String cornerID) {
		this.cornerID = cornerID;
	}

}
