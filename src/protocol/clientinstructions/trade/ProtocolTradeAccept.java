package protocol.clientinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * <b>Handelsangebot annehmen (Accept trade offered)</b>
 * <p>
 * When client accepts the terms of the given trade.
 * </p>
 */

@Since(0.2)
public class ProtocolTradeAccept {

	@SerializedName("Handel id")
	private int tradeID;

	public ProtocolTradeAccept(int tradeID) {

		this.tradeID = tradeID;
	}

	public int getTradeID() {

		return tradeID;
	}
}
