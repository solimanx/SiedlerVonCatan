package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;

public class ProtocolStatusUpdate {
	@SerializedName("Spieler")
	public ProtocolPlayer player;
}
