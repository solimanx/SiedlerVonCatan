package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;

import protocol.object.ProtocolPlayer;

public class ProtocolStatusUpdate {
	
	@SerializedName("Spieler")
	private ProtocolPlayer player;

	public ProtocolStatusUpdate(ProtocolPlayer player) {
		this.player = player;
	}

	public ProtocolPlayer getPlayer() {
		return player;
	}
}
