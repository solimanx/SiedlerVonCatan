package protocol.connection;

import com.google.gson.annotations.SerializedName;

/**
 * <b>3: Verbindungsaufbau</b>
 * <p>
 * Server assigned user-id.
 * </p>
 * 
 * @version 0.1
 * 
 */
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
