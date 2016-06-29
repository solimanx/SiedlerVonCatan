package protocol.serverinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import protocol.object.ProtocolResource;

// TODO: Auto-generated Javadoc
/**
 * 
 * <b>Handelsangebot (Trade Confirmation)</b>
 * <p>
 * Server previews the detail of the offered trade.
 * </p>
 * 
 */

@Since(0.2)
public class ProtocolTradePreview {

	@SerializedName("Spieler")
	private int playerID;

	@SerializedName("Handel id")
	private int tradeID;

	@SerializedName("Angebot")
	private ProtocolResource offer;

	@SerializedName("Nachfrage")
	private ProtocolResource withdrawal;

	/**
	 * Instantiates a new protocol trade preview.
	 *
	 * @param playerID the player ID
	 * @param tradeID the trade ID
	 * @param offer the offer
	 * @param withdrawal the withdrawal
	 */
	public ProtocolTradePreview(int playerID, int tradeID, ProtocolResource offer, ProtocolResource withdrawal) {
		this.playerID = playerID;
		this.tradeID = tradeID;
		this.offer = offer;
		this.withdrawal = withdrawal;
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

	/**
	 * Gets the offer.
	 *
	 * @return the offer
	 */
	public ProtocolResource getOffer() {
		return offer;
	}

	/**
	 * Gets the withdrawal.
	 *
	 * @return the withdrawal
	 */
	public ProtocolResource getWithdrawal() {
		return withdrawal;
	}

}
