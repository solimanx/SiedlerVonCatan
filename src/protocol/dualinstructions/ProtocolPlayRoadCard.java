package protocol.dualinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import model.Index;

/**
 * <b>Straßenbaukarte ausspielen(play the knight card) 12.2</b>
 * <p>
 * Server sends to all the clients, the message about the Player who played this
 * card.
 * </p>
 * Created on 06.06.2016.
 */
@Since(1.0)
public class ProtocolPlayRoadCard {

	@SerializedName("Spieler")
	private Integer playerID;

	@SerializedName("Straße 1")
	private Index[] roadID1; //length 2

	@SerializedName("Straße 2")
	private Index[] roadID2; // length 2

	// Client to Server
	public ProtocolPlayRoadCard(Index[] roadID1, Index[] roadID2) {
		this.roadID1 = roadID1;
		this.roadID2 = roadID2;
	}

	// Server to Client
	public ProtocolPlayRoadCard(Integer playerID, Index[] roadID1, Index[] roadID2) {
		this.playerID = playerID;
		this.roadID1 = roadID1;
		this.roadID2 = roadID2;

	}

	public Index[] getRoadID1() {
		return roadID1;
	}

	public Index[] getRoadID2() {
		return roadID2;
	}

	public Integer getPlayerID() {
		return playerID;
	}
}
