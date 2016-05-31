package protocol.clientinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import protocol.object.ProtocolResource;

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

	public ProtocolRobberLoss(ProtocolResource losses) {
		this.losses = losses;
	}

	public ProtocolResource getLosses() {
		return losses;
	}
}
