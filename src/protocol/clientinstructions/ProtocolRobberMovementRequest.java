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
	String locationID;

	// optional
	@SerializedName("Ziel")
	Integer victimID;

	//the standard constructor
	public ProtocolRobberMovementRequest(String locationID) {
		this.locationID = locationID;
	}
	//the optional one
	public ProtocolRobberMovementRequest(String locationID, Integer victimID) {
		this.locationID = locationID;
		this.victimID = victimID;
	}

	public String getLocationID() {
		return locationID;
	}

	public Integer getVictimID() {
		return victimID;
	}
}
