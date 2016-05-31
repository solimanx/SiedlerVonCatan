package protocol.clientinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import protocol.object.ProtocolResource;

/**
 * <b>Seehandel (Sea trade)</b>
 * <p>
 * Client sends server the requested resources to trade with harbour.
 * </p>
 * 
 */

@Since(0.2)
public class ProtocolHarbourRequest {

	@SerializedName("Angebot")
	private ProtocolResource offer;

	@SerializedName("Nachfrage")
	private ProtocolResource withdrawal;

	public ProtocolHarbourRequest(ProtocolResource offer, ProtocolResource withdrawal) {
		this.offer = offer;
		this.withdrawal = withdrawal;
	}

	public ProtocolResource getOffer() {
		return offer;
	}

	public ProtocolResource getWithdrawal() {
		return withdrawal;
	}
}
