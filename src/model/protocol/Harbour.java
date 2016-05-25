package model.protocol;

import enums.protocol.HarbourType;

public class Harbour {
	String ID;
	HarbourType type;

	public Harbour(String iD, HarbourType type) {
		ID = iD;
		this.type = type;
	}

	public String getID() {
		return ID;
	}

	public HarbourType getType() {
		return type;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public void setType(HarbourType type) {
		this.type = type;
	}
}
