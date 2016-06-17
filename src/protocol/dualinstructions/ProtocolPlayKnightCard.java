package protocol.dualinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import model.Index;

/**
 * <b>Ritter ausspielen(play the knight card)12.1</b>
 * <p>
 * Server sends to all the clients, the message about the Player who played this
 * card.
 * </p>
 * Created on 06.06.2016.
 */

@Since(1.0)
public class ProtocolPlayKnightCard {
	@SerializedName("Spieler")
	private Integer playerID;

	@SerializedName("Ort")
	private Index fieldID;

	@SerializedName("Ziel")
	private int victimID;

	// Client to Server
	public ProtocolPlayKnightCard(Index fieldID, int target) {
		this.fieldID = fieldID;
		this.victimID = target;
	}

	// Server to Client
	public ProtocolPlayKnightCard(Integer playerID, Index fieldID, int victimID) {
		this.playerID = playerID;
		this.fieldID = fieldID;
		this.victimID = victimID;

	}

	public Index getLocationID() {
		return fieldID;
	}

	public int getVictimID() {
		return victimID;
	}

	public Integer getPlayerID() {
		return playerID;
	}
}