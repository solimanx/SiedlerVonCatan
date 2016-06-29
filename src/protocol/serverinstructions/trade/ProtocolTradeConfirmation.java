package protocol.serverinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

// TODO: Auto-generated Javadoc
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

	/**
	 * Instantiates a new protocol trade confirmation.
	 *
	 * @param playerID the player ID
	 * @param tradeID the trade ID
	 * @param accepted the accepted
	 */
	public ProtocolTradeConfirmation(int playerID, int tradeID, boolean accepted) {
		this.playerID = playerID;
		this.tradeID = tradeID;
		this.accepted = accepted;
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
	 * Gets the accepted.
	 *
	 * @return the accepted
	 */
	public boolean getAccepted() {
		return accepted;
	}

}
