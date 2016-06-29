package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import protocol.object.ProtocolPlayer;

// TODO: Auto-generated Javadoc
/**
 * 
 * <b>Statusupdate eines Spielers (Player's status update)</b>
 * <p>
 * After status of player changes.
 * </p>
 * 
 */

@Since(0.1)
public class ProtocolStatusUpdate {

	@SerializedName("Spieler")
	private ProtocolPlayer player;

	/**
	 * Instantiates a new protocol status update.
	 *
	 * @param player the player
	 */
	public ProtocolStatusUpdate(ProtocolPlayer player) {
		this.player = player;
	}

	/**
	 * Gets the player.
	 *
	 * @return the player
	 */
	public ProtocolPlayer getPlayer() {
		return player;
	}
}
