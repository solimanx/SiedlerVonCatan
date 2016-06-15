package protocol.serverinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * used Protocol
 * <b>Handelsangebot abgebrochen (Request trade/offer was canceled)</b>
 * <p>
 * Server sends Information, that the trade request was canceled to players.
 * </p>
 */

@Since(0.2)
public class ProtocolTradeCancellation {

	@SerializedName("Spieler")
	private int playerID;

	@SerializedName("Handel id")
	private int tradeID;

	public ProtocolTradeCancellation(int playerID, int tradeID) {

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
