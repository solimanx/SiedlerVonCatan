package model;

import enums.CardinalDirection;
import enums.CornerStatus;
import enums.HarbourStatus;

public class Corner {

    int offsetX;
    int offsetY;
    CardinalDirection direction;

    CornerStatus status;
    HarbourStatus harbourStatus;
    PlayerModel ownedByPlayer;

    public Corner() { // TODO Constructor
	status = CornerStatus.EMPTY;
	harbourStatus = HarbourStatus.NULL;
    }

    public int getOffsetX() {
	return offsetX;
    }

    public int getOffsetY() {
	return offsetY;
    }

    public CardinalDirection getDirection() {
	return direction;
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

    public void setOffsetX(int offsetX) {
	this.offsetX = offsetX;
    }

    public void setOffsetY(int offsetY) {
	this.offsetY = offsetY;
    }

    public void setDirection(CardinalDirection direction) {
	this.direction = direction;
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
