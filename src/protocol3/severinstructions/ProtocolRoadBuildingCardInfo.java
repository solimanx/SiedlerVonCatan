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
    String road1_id;
    @SerializedName("Straße 2")
    String road2_id;
    @SerializedName("Spieler")
    int player_id;

    public ProtocolRoadBuildingCardInfo(String road1, String road2, int player_id) {
        this.road1_id = road1_id;
        this.road2_id = road2_id;
        this.player_id = player_id;
    }

    public String getRoad1() {
        return road1_id;
    }

    public String getRoad2() {
        return road2_id;
    }

    public int getPlayer_id() {
        return player_id;
    }
}
