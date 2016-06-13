package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * 
 * <b>Räuber versetzt (Robber repositioned)</b>
 * <p>
 * After robber's location has been changed.
 * </p>
 * 
 */

@Since(0.2)
public class ProtocolRobberMovement {

	@SerializedName("Spieler")
	private int playerID;

	@SerializedName("Ort")
	private String locationID;

	@SerializedName("Ziel")
	private int victimID;

	public ProtocolRobberMovement(int playerID, String locationID, int victimID) {
		this.playerID = playerID;
		this.locationID = locationID;
		this.victimID = victimID;
	}

	public int getPlayerID() {
		return playerID;
	}

	public String getLocationID() {
		return locationID;
	}

	public int getVictimID() {
		return victimID;
	}

}
