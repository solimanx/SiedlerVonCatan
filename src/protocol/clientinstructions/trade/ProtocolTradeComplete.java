package protocol.clientinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

// TODO: Auto-generated Javadoc
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

	/**
	 * Instantiates a new protocol trade complete.
	 *
	 * @param tradeID the trade ID
	 * @param tradePartnerID the trade partner ID
	 */
	public ProtocolTradeComplete(int tradeID, int tradePartnerID) {

		this.tradeID = tradeID;
		this.tradePartnerID = tradePartnerID;
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
	 * Gets the trade partner ID.
	 *
	 * @return the trade partner ID
	 */
	public int getTradePartnerID() {

		return tradePartnerID;
	}
}
