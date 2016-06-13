package protocol.serverinstructions;

import com.google.gson.annotations.Since;

import protocol.object.ProtocolResource;

/**
 * <b>Kosten (Costs) </b>
 * <p>
 * Server sends to client the cost required (Decrease resources).
 * </p>
 * 
 * @see protocol.serverinstructions.ProtocolResourceObtain
 */

@Since(0.2)
public class ProtocolCosts extends ProtocolResourceObtain {

	public ProtocolCosts(int playerID, ProtocolResource resource) {
		super(playerID, resource);
	}

}
