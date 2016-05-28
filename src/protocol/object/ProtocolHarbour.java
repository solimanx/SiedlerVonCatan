package protocol.object;

import com.google.gson.annotations.SerializedName;


public class ProtocolHarbour {

	@SerializedName("Ort")
	String ID;

	@SerializedName("Typ")
	String type;

	public ProtocolHarbour(String iD, String type) {
		ID = iD;
		this.type = type;
	}

	public String getID() {
		return ID;
	}

	public String getType() {
		return type;
	}

}
