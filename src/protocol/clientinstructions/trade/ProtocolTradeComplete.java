package protocol.clientinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * <b>Handel abschließen(Complete trading)</b>
 * <p>
 * Player can choose, which trade he wants to complete.
 * </p>
 */

@Since(0.2)
public class ProtocolTradeComplete {

	@SerializedName("Handel id")

	private int tradeID;

	@SerializedName("Mitspieler")

	private int tradePartnerID;

	public ProtocolTradeComplete(int tradeID, int tradePartnerID) {

		this.tradeID = tradeID;
		this.tradePartnerID = tradePartnerID;
	}

	public int getTradeID() {

		return tradeID;
	}

	public int getTradePartnerID() {

		return tradePartnerID;
	}
}
