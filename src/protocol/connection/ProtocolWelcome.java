package protocol.connection;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

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

	public ProtocolWelcome(int playerID) {
		this.playerID = playerID;
	}

	public int getPlayerID() {
		return playerID;
	}

}
