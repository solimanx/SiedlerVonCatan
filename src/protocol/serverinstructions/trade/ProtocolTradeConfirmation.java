package protocol.serverinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * <b>Handelsangebot angenommen (Trade Confirmation)</b>
 * <p>
 * Server shows completion of trade.
 * </p>
 * 
 */

@Since(0.2)
public class ProtocolTradeConfirmation {

	@SerializedName("Mitspieler")
	private int playerID;

	@SerializedName("Handel id")
	private int tradeID;

	@SerializedName("Annehmen")
	private boolean accepted;

	public ProtocolTradeConfirmation(int playerID, int tradeID, boolean accepted) {
		this.playerID = playerID;
		this.tradeID = tradeID;
		this.accepted = accepted;
	}

	public int getPlayerID() {
		return playerID;
	}

	public int getTradeID() {
		return tradeID;
	}

	public boolean getAccepted() {
		return accepted;
	}

}
