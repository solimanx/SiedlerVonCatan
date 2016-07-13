package protocol.cheats;

import com.google.gson.annotations.SerializedName;

public class ProtocolLongestTurn {
	@SerializedName("Player")

	int player_id;

	/**
	 * Instantiates a new protocol longest Turn.
	 *
	 * @param player_id the player id
	 */
	public ProtocolLongestTurn(int player_id) {

		this.player_id = player_id;
	}

	/**
	 * Gets the player ID.
	 *
	 * @return the player ID
	 */
	public int getPlayerID() {

		return player_id;
	}
}
