package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * 
 * <b>Räuber versetzt (Robber repositioned)</b>
 * <p>
 * After robber's location has been changed.
 * </p>
 * 
 */

@Since(0.2)
public class ProtocolRobberMovement {

	@SerializedName("Spieler")
	private int player_id;

	@SerializedName("Ort")
	private String location_id;

	@SerializedName("Ziel")
	private int victim_id;

	public ProtocolRobberMovement(int player_id, String location_id, int victim_id) {
		this.player_id = player_id;
		this.location_id = location_id;
		this.victim_id = victim_id;
	}

	public int getPlayer_id() {
		return player_id;
	}

	public String getLocation_id() {
		return location_id;
	}

	public int getVictim_id() {
		return victim_id;
	}

}
