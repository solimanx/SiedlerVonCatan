package protocol.clientinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

// TODO: Auto-generated Javadoc
/**
 * <b>Handel abbrechen(Cancel trade request)</b>
 * <p>
 * Player sends message to Server, in order to cancel trade request.
 * </p>
 */

@Since(0.2)
public class ProtocolTradeCancel {

	@SerializedName("Handel id")
	private int tradeID;

	/**
	 * Instantiates a new protocol trade cancel.
	 *
	 * @param tradeID the trade ID
	 */
	public ProtocolTradeCancel(int tradeID) {

		this.tradeID = tradeID;
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
