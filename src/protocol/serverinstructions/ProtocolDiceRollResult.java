package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;

public class ProtocolDiceRollResult {
	@SerializedName("Spieler")
	private int player;
	@SerializedName("Wurf")
	private int roll;

	public ProtocolDiceRollResult(int player, int roll) {

		this.player = player;
		this.roll = roll;
	}
}
