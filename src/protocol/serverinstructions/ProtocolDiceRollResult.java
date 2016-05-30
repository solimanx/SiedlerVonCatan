package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;

/**
 * <b>7.2 Würfeln </b>
 * <p>
 * Server sends to client the dice roll result.
 * </p>
 * 
 * @version 0.1
 * 
 */
public class ProtocolDiceRollResult {

	@SerializedName("Spieler")
	private int player_id;

	@SerializedName("Wurf")
	private int roll;

	public ProtocolDiceRollResult(int player, int roll) {
		this.player_id = player;
		this.roll = roll;
	}

	public int getPlayer() {
		return player_id;
	}

	public int getRoll() {
		return roll;
	}
}
