package model;

import enums.CardinalDirection;

public class Edge {
    int offsetX;
    int offsetY;
    CardinalDirection direction;

    boolean hasStreet;
    PlayerModel ownedByPlayer;

    public Edge() {
	hasStreet = false; // TODO constructor
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

    public boolean isHasStreet() {
	return hasStreet;
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

    public void setHasStreet(boolean hasStreet) {
	this.hasStreet = hasStreet;
    }

    public void setOwnedByPlayer(PlayerModel ownedByPlayer) {
	this.ownedByPlayer = ownedByPlayer;
    }
}
