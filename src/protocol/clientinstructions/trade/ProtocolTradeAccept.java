package protocol.clientinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

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

	public ProtocolTradeAccept(int tradeID, boolean accepted) {

		this.tradeID = tradeID;
		this.accepted = accepted;
	}

	public int getTradeID() {

		return tradeID;
	}

	public Boolean getAccepted() {
		return accepted;
	}
}
