package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

// TODO: Auto-generated Javadoc
/**
 * <b>9.10Größte Rittermacht(biggest knight prowess) </b>
 * <p>
 * Server sends to client the Message about the Player who has the biggest
 * knight prowess.
 * </p>
 * Created on 06.06.2016.
 */

@Since(0.3)
public class ProtocolLargestArmy {

	@SerializedName("Spieler")

	int player_id;

	/**
	 * Instantiates a new protocol largest army.
	 *
	 * @param player_id the player id
	 */
	public ProtocolLargestArmy(int player_id) {

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
