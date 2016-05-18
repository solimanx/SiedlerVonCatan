package model;

import enums.CornerStatus;
import enums.HarbourStatus;

public class Corner {

    private CornerStatus status;
    private HarbourStatus harbourStatus;
    private PlayerModel ownedByPlayer;

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

    public PlayerModel getOwnedByPlayer() {
	return ownedByPlayer;
    }

    public void setStatus(CornerStatus status) {
	this.status = status;
    }

    public void setHarbourStatus(HarbourStatus harbourStatus) {
	this.harbourStatus = harbourStatus;
    }

    public void setOwnedByPlayer(PlayerModel ownedByPlayer) {
	this.ownedByPlayer = ownedByPlayer;
    }

}
