package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import protocol.object.ProtocolResource;

// TODO: Auto-generated Javadoc
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

	/**
	 * Instantiates a new protocol resource obtain.
	 *
	 * @param playerID the player ID
	 * @param resource the resource
	 */
	public ProtocolResourceObtain(int playerID, ProtocolResource resource) {
		this.playerID = playerID;
		this.resource = resource;
	}

	/**
	 * Gets the player ID.
	 *
	 * @return the player ID
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * Gets the resource.
	 *
	 * @return the resource
	 */
	public ProtocolResource getResource() {
		return resource;
	}
}
