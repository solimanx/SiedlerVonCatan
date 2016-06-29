package protocol.clientinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

// TODO: Auto-generated Javadoc
/**
 * <b>Handel annehmen (Accept trade offered)</b>
 * <p>
 * When client accepts/decline the terms of the given trade.
 * </p>
 */

@Since(1.0)
public class ProtocolTradeAccept {

	@SerializedName("Handel id")
	private int tradeID;

	@SerializedName("Annehmen")
	private boolean accepted;

	/**
	 * Instantiates a new protocol trade accept.
	 *
	 * @param tradeID the trade ID
	 * @param accepted the accepted
	 */
	public ProtocolTradeAccept(int tradeID, boolean accepted) {

		this.tradeID = tradeID;
		this.accepted = accepted;
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
	public Boolean getAccepted() {
		return accepted;
	}
}
