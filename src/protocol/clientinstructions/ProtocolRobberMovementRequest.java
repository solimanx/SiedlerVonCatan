package protocol.clientinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * <b>Räuber versetzen (Request moving Robber)</b>
 * <p>
 * Client sends server the location of robber after rolling 7.
 * </p>
 *
 */

@Since(0.2)
public class ProtocolRobberMovementRequest {

	@SerializedName("Ort")
	String location_id;

	// optional
	@SerializedName("Ziel")
	int victim_id;

	public ProtocolRobberMovementRequest(String location_id, int victim_id) {
		this.location_id = location_id;
		this.victim_id = victim_id;
	}

	public String getLocation_id() {
		return location_id;
	}

	public int getVictim_id() {
		return victim_id;
	}
}
