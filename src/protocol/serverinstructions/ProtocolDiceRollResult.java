package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

// TODO: Auto-generated Javadoc
/**
 * <b>Würfeln (Dice Roll Result) </b>
 * <p>
 * Server sends to client the dice roll result.
 * </p>
 * 
 */

@Since(0.1)
public class ProtocolDiceRollResult {

	@SerializedName("Spieler")
	private int playerID;

	@SerializedName("Wurf")
	private int[] roll;

	/**
	 * Instantiates a new protocol dice roll result.
	 *
	 * @param playerID the player ID
	 * @param result the result
	 */
	public ProtocolDiceRollResult(int playerID, int[] result) {
		this.playerID = playerID;
		this.roll = result;
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
	 * Gets the roll.
	 *
	 * @return the roll
	 */
	public int[] getRoll() {
		return roll;
	}
}
