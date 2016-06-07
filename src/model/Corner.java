package model;

import enums.CornerStatus;
import enums.HarbourStatus;

public class Corner {

	private CornerStatus status;
	private HarbourStatus harbourStatus;
	private int ownerID;

	public Corner() { // TODO Constructor
		status = CornerStatus.EMPTY;
		harbourStatus = HarbourStatus.NULL;
	}

	public CornerStatus getStatus() {
		return status;
	}

	public HarbourStatus getHarbourStatus() {
		return harbourStatus;
	}

	public int getOwnerID() {
		return ownerID;
	}

	public void setStatus(CornerStatus status) {
		this.status = status;
	}

	public void setHarbourStatus(HarbourStatus harbourStatus) {
		this.harbourStatus = harbourStatus;
	}

	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

}
