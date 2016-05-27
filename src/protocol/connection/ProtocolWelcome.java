package protocol.connection;

import com.google.gson.annotations.SerializedName;

public class ProtocolWelcome {

	@SerializedName("id")
	private int player_id;

	public ProtocolWelcome(int player_id) {
		this.player_id = player_id; //TODO 31-bit-integer
	}

	public int getPlayer_id() {
		return player_id;
	}

}
