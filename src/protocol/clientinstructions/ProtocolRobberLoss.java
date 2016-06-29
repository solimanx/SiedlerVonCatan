package protocol.clientinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import protocol.object.ProtocolResource;

// TODO: Auto-generated Javadoc
/**
 * <b>Räuber: Rohstoffe abgeben (Losses to Robber)</b>
 * <p>
 * Client sends server the resources that robber stole.
 * </p>
 * 
 */

@Since(0.2)
public class ProtocolRobberLoss {

	@SerializedName("Abgeben")
	private ProtocolResource losses;

	/**
	 * Instantiates a new protocol robber loss.
	 *
	 * @param losses the losses
	 */
	public ProtocolRobberLoss(ProtocolResource losses) {
		this.losses = losses;
	}

	/**
	 * Gets the losses.
	 *
	 * @return the losses
	 */
	public ProtocolResource getLosses() {
		return losses;
	}
}
