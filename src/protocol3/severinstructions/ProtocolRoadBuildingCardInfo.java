package protocol3.severinstructions;

import com.google.gson.annotations.SerializedName;

/**
 * <b>Straßenbaukarte ausspielen(play the knight card)</b>
 * <p>
 * Server sends to all the clients, the message about the Player who played this
 * card.
 * </p>
 * Created on 06.06.2016.
 */
public class ProtocolRoadBuildingCardInfo {

	@SerializedName("Straße 1")
	String road1;
	@SerializedName("Straße 2")
	String road2;
	@SerializedName("Spieler")
	int player_id;

	public ProtocolRoadBuildingCardInfo(String road1, String road2, int player_id) {
		this.road1 = road1;
		this.road2 = road2;
		this.player_id = player_id;
	}

	public String getRoad1() {
		return road1;
	}

	public String getRoad2() {
		return road2;
	}

	public int getPlayer_id() {
		return player_id;
	}
}
