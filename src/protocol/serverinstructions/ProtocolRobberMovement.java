package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import model.Index;

/**
 *
 * <b>Räuber versetzt (Robber repositioned)</b>
 * <p>
 * After robber's location has been changed.
 * </p>
 *
 */

@Since(1.0)
public class ProtocolRobberMovement {

	@SerializedName("Spieler")
	private int playerID;

	@SerializedName("Ort")
	private Index locationID;

	@SerializedName("Ziel")
	private int victimID;

	public ProtocolRobberMovement(int playerID, Index locationID, int victimID) {
		this.playerID = playerID;
		this.locationID = locationID;
		this.victimID = victimID;
	}

	public int getPlayerID() {
		return playerID;
	}

	public Index getLocationID() {
		return locationID;
	}

	public int getVictimID() {
		return victimID;
	}

}
