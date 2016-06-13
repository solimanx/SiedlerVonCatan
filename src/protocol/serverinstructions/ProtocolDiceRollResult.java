package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

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

	public ProtocolDiceRollResult(int playerID, int[] result) {
		this.playerID = playerID;
		this.roll = result;
	}

	public int getPlayerID() {
		return playerID;
	}

	public int[] getRoll() {
		return roll;
	}
}
