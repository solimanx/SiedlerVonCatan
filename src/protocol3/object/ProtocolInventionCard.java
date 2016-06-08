package protocol3.object;

import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Since;
import protocol.object.ProtocolResource;

@Since(0.3)
public class ProtocolInventionCard {
	@SerializedName("Rohstoffe")
	ProtocolResource resource;

	@SerializedName("Spieler")
	int player_id;

	public ProtocolInventionCard (ProtocolResource resource, int player_id) {
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
