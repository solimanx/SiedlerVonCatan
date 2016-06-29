package protocol.serverinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

// TODO: Auto-generated Javadoc
/**
 * used Protocol <b>Handelsangebot abgebrochen (Request trade/offer was
 * canceled)</b>
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

	/**
	 * Instantiates a new protocol trade cancellation.
	 *
	 * @param playerID the player ID
	 * @param tradeID the trade ID
	 */
	public ProtocolTradeCancellation(int playerID, int tradeID) {

		this.playerID = playerID;
		this.tradeID = tradeID;
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
	 * Gets the trade ID.
	 *
	 * @return the trade ID
	 */
	public int getTradeID() {

		return tradeID;
	}
}
