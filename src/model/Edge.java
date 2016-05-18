package model;

public class Edge {

	private boolean hasStreet;
	private PlayerModel ownedByPlayer;

	public Edge() {
		hasStreet = false; // TODO constructor
	}

	public boolean isHasStreet() {
		return hasStreet;
	}

	public PlayerModel getOwnedByPlayer() {
		return ownedByPlayer;
	}

	public void setHasStreet(boolean hasStreet) {
		this.hasStreet = hasStreet;
	}

	public void setOwnedByPlayer(PlayerModel ownedByPlayer) {
		this.ownedByPlayer = ownedByPlayer;
	}
}
