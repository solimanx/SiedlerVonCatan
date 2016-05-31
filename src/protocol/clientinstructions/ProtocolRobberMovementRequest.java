package protocol.clientinstructions;

import com.google.gson.annotations.Since;

import protocol.serverinstructions.ProtocolRobberMovement;

/**
 * <b>Räuber versetzen (Request moving Robber)</b>
 * <p>
 * Client sends server the location of robber after rolling 7.
 * </p>
 * 
 */

@Since(0.2)
public class ProtocolRobberMovementRequest extends ProtocolRobberMovement{

	//ID is optional
	public ProtocolRobberMovementRequest(int player_id, String location_id, int victim_id) {
		super(player_id, location_id, victim_id);
	}
	

}
