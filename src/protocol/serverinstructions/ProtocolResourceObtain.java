package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;

public class ProtocolResourceObtain {

	@SerializedName("Spieler")
	public int player;
	@SerializedName("Rohstoffe")
	public ProtocolResource resource;

}
