package protocol3.severinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
import protocol.object.ProtocolResource;
import protocol3.object.ProtocolInventionCard;

/**
 * <b>Erfindung(invention card) </b>
 * <p>
 * Server sends to all clients the information, in order to make the process
 * understandable. (player can get two different resource types)
 * </p>
 * Created on 06.06.2016.
 */

@Since(0.3)
public class ProtocolInventionCardInfo {

	@SerializedName("Rohstoffe")
	private ProtocolInventionCard invention;
	
	

	public ProtocolInventionCardInfo(ProtocolInventionCard invention) {
		this.invention = invention;
		
	}

	public ProtocolInventionCard getInventionCard() {
		return invention;
	}

}
