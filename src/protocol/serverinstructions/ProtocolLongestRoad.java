package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

// TODO: Auto-generated Javadoc
/**
 * <b>9.10Längste Handelsstraße(longest road </b>
 * <p>
 * Server sends to client the message about the Player who has the longest road.
 * </p>
 * Created on 06.06.2016.
 */

@Since(0.3)
public class ProtocolLongestRoad {

	@SerializedName("Spieler")
	Integer player_id;

	/**
	 * Instantiates a new protocol longest road.
	 *
	 * @param player_id the player id
	 */
	// normal case
	public ProtocolLongestRoad(Integer player_id) {
		this.player_id = player_id;
	}

	/**
	 * Instantiates a new protocol longest road.
	 */
	// Special Case
	public ProtocolLongestRoad() {
		this.player_id = null;
	}

	/**
	 * Gets the player ID.
	 *
	 * @return the player ID
	 */
	public Integer getPlayerID() {

		return player_id;
	}

}
