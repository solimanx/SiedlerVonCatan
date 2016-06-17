package protocol.clientinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import model.Index;

/**
 * <b>Räuber versetzen (Request moving Robber)</b>
 * <p>
 * Client sends server the location of robber after rolling 7.
 * </p>
 *
 */

@Since(1.0)
public class ProtocolRobberMovementRequest {

	@SerializedName("Ort")
	Index locationID;

	// optional
	@SerializedName("Ziel")
	Integer victimID;

	//the standard constructor
	public ProtocolRobberMovementRequest(Index locationID) {
		this.locationID = locationID;
	}
	//the optional one
	public ProtocolRobberMovementRequest(Index locationID, Integer victimID) {
		this.locationID = locationID;
		this.victimID = victimID;
	}

	public Index getLocationID() {
		return locationID;
	}

	public Integer getVictimID() {
		return victimID;
	}
}
