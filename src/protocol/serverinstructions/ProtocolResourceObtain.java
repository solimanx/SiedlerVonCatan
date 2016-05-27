package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;

public class ProtocolResourceObtain {

	@SerializedName("Spieler")
	private int player;
	@SerializedName("Rohstoffe")
	private ProtocolResource resource;

	public ProtocolResourceObtain(int player, ProtocolResource resource) {

		this.player = player;
		this.resource = resource;
	}
}
