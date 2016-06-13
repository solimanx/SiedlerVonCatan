package protocol.clientinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

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

	public ProtocolTradeCancel(int tradeID) {

		this.tradeID = tradeID;
	}

	public int getTradeID() {

		return tradeID;
	}
}
