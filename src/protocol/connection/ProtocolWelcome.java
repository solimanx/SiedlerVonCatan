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
	private int player_id;

	public ProtocolWelcome(int player_id) {
		this.player_id = player_id;
	}

	public int getPlayer_id() {
		return player_id;
	}

}
