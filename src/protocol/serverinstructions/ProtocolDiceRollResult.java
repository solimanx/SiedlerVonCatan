package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;

public class ProtocolDiceRollResult {
	@SerializedName("Spieler")
	public int player;
	@SerializedName("Wurf")
	public int roll;

}
