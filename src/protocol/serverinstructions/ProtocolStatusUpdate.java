package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;

import protocol.object.ProtocolPlayer;

/**
 * <b>7.1 Statusupdate eines Spielers </b>
 * <p>
 * After status of player changes.
 * </p>
 * 
 * @version 0.1
 * 
 */
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
