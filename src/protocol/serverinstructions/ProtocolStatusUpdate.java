package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;

public class ProtocolStatusUpdate {
	@SerializedName("Spieler")
	private ProtocolPlayer player;

	public ProtocolStatusUpdate(ProtocolPlayer player) {
		this.player = player;
	}
}
