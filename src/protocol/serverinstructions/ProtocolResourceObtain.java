package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import protocol.object.ProtocolResource;

/**
 * <b>Ertrag (Resource Gain) </b>
 * <p>
 * Server sends to client the resources obtained. (Increase resources)
 * </p>
 * 
 * @see protocol.serverinstructions.ProtocolCosts
 */

@Since(0.1)
public class ProtocolResourceObtain {

	@SerializedName("Spieler")
	private int playerID;

	@SerializedName("Rohstoffe")
	private ProtocolResource resource;

	public ProtocolResourceObtain(int playerID, ProtocolResource resource) {
		this.playerID = playerID;
		this.resource = resource;
	}

	public int getPlayerID() {
		return playerID;
	}

	public ProtocolResource getResource() {
		return resource;
	}
}
