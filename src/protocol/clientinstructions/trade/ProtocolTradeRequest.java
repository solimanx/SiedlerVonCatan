package protocol.clientinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import protocol.object.ProtocolResource;

// TODO: Auto-generated Javadoc
/**
 * <b>Handel anbieten (Request trade/offer)</b>
 * <p>
 * Client sends server the requested resources to trade.
 * </p>
 */

@Since(0.2)
public class ProtocolTradeRequest {

	@SerializedName("Angebot")
	private ProtocolResource offer;

	@SerializedName("Nachfrage")
	private ProtocolResource demand;

	/**
	 * Instantiates a new protocol trade request.
	 *
	 * @param offer the offer
	 * @param demand the demand
	 */
	public ProtocolTradeRequest(ProtocolResource offer, ProtocolResource demand) {

		this.offer = offer;
		this.demand = demand;
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
	 * Gets the demand.
	 *
	 * @return the demand
	 */
	public ProtocolResource getDemand() {

		return demand;
	}

}
