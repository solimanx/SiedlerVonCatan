package protocol.clientinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import protocol.object.ProtocolResource;

// TODO: Auto-generated Javadoc
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

	/**
	 * Instantiates a new protocol harbour request.
	 *
	 * @param offer the offer
	 * @param withdrawal the withdrawal
	 */
	public ProtocolHarbourRequest(ProtocolResource offer, ProtocolResource withdrawal) {
		this.offer = offer;
		this.withdrawal = withdrawal;
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
	 * Gets the withdrawal.
	 *
	 * @return the withdrawal
	 */
	public ProtocolResource getWithdrawal() {
		return withdrawal;
	}
}
