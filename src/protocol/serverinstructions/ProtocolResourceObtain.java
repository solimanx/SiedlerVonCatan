package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;

import protocol.object.ProtocolResource;

/**
 * <b>7.3 Ertrag </b>
 * <p>
 * Server sends to client the resources obtained.
 * </p>
 * 
 * @version 0.1
 * 
 */
public class ProtocolResourceObtain {

	@SerializedName("Spieler")
	private int player_id;
	
	@SerializedName("Rohstoffe")
	private ProtocolResource resource;

	public ProtocolResourceObtain(int player_id, ProtocolResource resource) {

		this.player_id = player_id;
		this.resource = resource;
	}

	public int getPlayer() {
		return player_id;
	}

	public ProtocolResource getResource() {
		return resource;
	}
}
