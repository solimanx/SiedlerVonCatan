package protocol3.severinstructions;

import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Since;
import protocol3.object.ProtocolRoadBuildingCard;

/**
 * <b>Straßenbaukarte ausspielen(play the knight card)</b>
 * <p>
 * Server sends to all the clients, the message about the Player who played this
 * card.
 * </p>
 * Created on 06.06.2016.
 */
@Since(0.3)
public class ProtocolRoadBuildingCardInfo {

    @SerializedName("Straße 1")
    private ProtocolRoadBuildingCard road1_id;

    @SerializedName("Straße 2")
    private ProtocolRoadBuildingCard road2_id;

@SerializedName("Spieler")
    private int player_id;

    public ProtocolRoadBuildingCardInfo(ProtocolRoadBuildingCard road1_id, ProtocolRoadBuildingCard road2_id, int player_id) {
        this.road1_id = road1_id;
        this.road2_id = road2_id;
        this.player_id = player_id;
    }

    public ProtocolRoadBuildingCard getRoad1_id() {
        return road1_id;
    }

    public ProtocolRoadBuildingCard getRoad2_id() {
        return road2_id;
    }

    public int getPlayer_id() {
        return player_id;
    }
}
