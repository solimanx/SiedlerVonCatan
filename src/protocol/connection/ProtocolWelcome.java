package protocol.connection;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

// TODO: Auto-generated Javadoc
/**
 * <b>Verbindungsaufbau (Logging in : Welcome)</b>
 * <p>
 * Server assigned user-id.
 * </p>
 * 
 */

@Since(0.1)
public class ProtocolWelcome {

	@SerializedName("id")
	private int playerID;

	/**
	 * Instantiates a new protocol welcome.
	 *
	 * @param playerID the player ID
	 */
	public ProtocolWelcome(int playerID) {
		this.playerID = playerID;
	}

	/**
	 * Gets the player ID.
	 *
	 * @return the player ID
	 */
	public int getPlayerID() {
		return playerID;
	}

}
