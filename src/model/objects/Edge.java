package model.objects;

public class Edge {

	private boolean hasStreet;
	private int ownerID;

	public Edge() {
		hasStreet = false; // TODO constructor
	}

	public boolean isHasStreet() {
		return hasStreet;
	}

	public int getOwnerID() {
		return ownerID;
	}

	public void setHasStreet(boolean hasStreet) {
		this.hasStreet = hasStreet;
	}

	public void setOwnedByPlayer(int ownerID) {
		this.ownerID = ownerID;
	}
}
