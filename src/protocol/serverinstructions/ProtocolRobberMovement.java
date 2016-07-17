package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import model.unit.Index;

// TODO: Auto-generated Javadoc
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
	private Integer victimID;

	/**
	 * Instantiates a new protocol robber movement.
	 *
	 * @param playerID the player ID
	 * @param locationID the location ID
	 * @param victimID the victim ID
	 */
	public ProtocolRobberMovement(int playerID, Index locationID, Integer victimID) {
		this.playerID = playerID;
		this.locationID = locationID;
		this.victimID = victimID;
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
	 * Gets the location ID.
	 *
	 * @return the location ID
	 */
	public Index getLocationID() {
		return locationID;
	}

	/**
	 * Gets the victim ID.
	 *
	 * @return the victim ID
	 */
	public Integer getVictimID() {
		return victimID;
	}

}
