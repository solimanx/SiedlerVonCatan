package protocol3.object;

import com.google.gson.annotations.SerializedName;

import protocol.object.ProtocolResource;


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
