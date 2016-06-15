package protocol.dualinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * <b>Ritter ausspielen(play the knight card)12.1</b>
 * <p>
 * Server sends to all the clients, the message about the Player who played this
 * card.
 * </p>
 * Created on 06.06.2016.
 */

@Since(0.3)
public class ProtocolPlayKnightCard {
	@SerializedName("Spieler")
	private Integer playerID;

	@SerializedName("Ort")
	private String fieldID;

	@SerializedName("Ziel")
	private int targetID;

	// Client to Server
	public ProtocolPlayKnightCard(String road1_id, int target) {
		this.fieldID = road1_id;
		this.targetID = target;
	}

	// Server to Client
	public ProtocolPlayKnightCard(Integer playerID, String fieldID, int targetID) {
		this.playerID = playerID;
		this.fieldID = fieldID;
		this.targetID = targetID;

	}

	public String getRoad1_id() {
		return fieldID;
	}

	public int getTarget() {
		return targetID;
	}

	public Integer getPlayer_id() {
		return playerID;
	}
}