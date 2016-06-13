package protocol.clientinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import protocol.object.ProtocolResource;

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

	public ProtocolTradeRequest(ProtocolResource offer, ProtocolResource demand) {

		this.offer = offer;
		this.demand = demand;
	}

	public ProtocolResource getOffer() {

		return offer;
	}

	public ProtocolResource getDemand() {

		return demand;
	}

}
