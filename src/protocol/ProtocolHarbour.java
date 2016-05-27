package protocol;

import com.google.gson.annotations.SerializedName;

import enums.protocol.HarbourType;

public class ProtocolHarbour {

	@SerializedName("Ort")
	String ID;
	@SerializedName("Typ")
	HarbourType type;

	public ProtocolHarbour(String iD, HarbourType type) {
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
