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
	private ProtocolResource resource;

	@SerializedName("Spieler")
	private int player_id;

	public ProtocolInventionCardInfo(ProtocolResource resource, int player_id) {

		this.resource = resource;
		this.player_id = player_id;
	}

	public ProtocolResource getResource() {
		return resource;
	}

	public int getPlayer_id() {
		return player_id;
	}
}
