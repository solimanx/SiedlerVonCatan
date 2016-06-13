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

	@SerializedName("Spieler")
	private int playerID;

	@SerializedName("Handel id")
	private int tradeID;

	public ProtocolTradeConfirmation(int playerID, int tradeID) {
		this.playerID = playerID;
		this.tradeID = tradeID;
	}

	public int getPlayerID() {
		return playerID;
	}

	public int getTradeID() {
		return tradeID;
	}

}
