package protocol.clientinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import model.Index;

// TODO: Auto-generated Javadoc
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

	/**
	 * Instantiates a new protocol robber movement request.
	 *
	 * @param locationID the location ID
	 */
	// the standard constructor
	public ProtocolRobberMovementRequest(Index locationID) {
		this.locationID = locationID;
	}

	/**
	 * Instantiates a new protocol robber movement request.
	 *
	 * @param locationID the location ID
	 * @param victimID the victim ID
	 */
	// the optional one
	public ProtocolRobberMovementRequest(Index locationID, Integer victimID) {
		this.locationID = locationID;
		this.victimID = victimID;
	}

	/**
	 * Gets the location ID.
	 *
	 * @return the location ID
	 */
	public Index getLocationID() {
		return locationID;
	}

	/**
	 * Gets the victim ID.
	 *
	 * @return the victim ID
	 */
	public Integer getVictimID() {
		return victimID;
	}
}
