package model;

import enums.CardinalDirection;

public class Edge {
    private CardinalDirection direction;

    private boolean hasStreet;
    private PlayerModel ownedByPlayer;

    public Edge() {
	hasStreet = false; // TODO constructor
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
