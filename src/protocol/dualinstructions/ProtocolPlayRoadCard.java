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
	private Integer player_id;
	
	@SerializedName("Straße 1")
	private String road1_id;

	@SerializedName("Straße 2")
	private String road2_id;

	// Client to Server
	public ProtocolPlayRoadCard(String road1_id, String road2_id) {
		this.road1_id = road1_id;
		this.road2_id = road2_id;
	}

	// Server to Client
	public ProtocolPlayRoadCard(Integer player_id, String road1_id, String road2_id) {
		this.player_id = player_id;
		this.road1_id = road1_id;
		this.road2_id = road2_id;
		
	}

	public String getRoad1_id() {
		return road1_id;
	}

	public String getRoad2_id() {
		return road2_id;
	}

	public int getPlayer_id() {
		return player_id;
	}
}
