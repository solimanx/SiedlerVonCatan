package protocol.dualinstructions;

import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Since;

/**
 * <b>Straßenbaukarte ausspielen(play the knight card) 12.2</b>
 * <p>
 * Server sends to all the clients, the message about the Player who played this
 * card.
 * </p>
 * Created on 06.06.2016.
 */
@Since(0.3)
public class ProtocolPlayRoadCard {

	@SerializedName("Spieler")
	private Integer playerID;
	
	@SerializedName("Straße 1")
	private String roadID1;

	@SerializedName("Straße 2")
	private String roadID2;

	// Client to Server
	public ProtocolPlayRoadCard(String roadID1, String roadID2) {
		this.roadID1 = roadID1;
		this.roadID2 = roadID2;
	}

	// Server to Client
	public ProtocolPlayRoadCard(Integer playerID, String roadID1, String roadID2) {
		this.playerID = playerID;
		this.roadID1 = roadID1;
		this.roadID2 = roadID2;
		
	}

	public String getRoadID1() {
		return roadID1;
	}

	public String getRoadID2() {
		return roadID2;
	}

	public Integer getPlayerID() {
		return playerID;
	}
}
