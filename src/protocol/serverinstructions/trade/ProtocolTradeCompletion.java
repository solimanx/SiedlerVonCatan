package protocol.serverinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

// TODO: Auto-generated Javadoc
/**
 * <b>Handel ausgeführt (trade was completed)</b>
 * <p>
 * Server confirms that trade was completed and sends information to players
 * <p/>
 */

@Since(0.2)
public class ProtocolTradeCompletion {

	@SerializedName("Spieler")
	private int playerID;

	@SerializedName("Mitspieler")
	private int tradePartnerID;

	/**
	 * Instantiates a new protocol trade completion.
	 *
	 * @param playerID the player ID
	 * @param tradePartnerID the trade partner ID
	 */
	public ProtocolTradeCompletion(int playerID, int tradePartnerID) {

		this.playerID = playerID;
		this.tradePartnerID = tradePartnerID;

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
	 * Gets the trade partner ID.
	 *
	 * @return the trade partner ID
	 */
	public int getTradePartnerID() {

		return tradePartnerID;
	}
}
