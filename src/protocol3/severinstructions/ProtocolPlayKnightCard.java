package protocol3.severinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * <b>Ritter ausspielen(play the knight card)</b>
 * <p>
 * Server sends to all the clients, the message about the Player who played this
 * card.
 * </p>
 * Created on 06.06.2016.
 */

@Since(0.3)
public class ProtocolPlayKnightCard {

    @SerializedName("Straße 1")
    private String road1_id;

    @SerializedName("Ziel")
    private int target;
    @SerializedName("Spieler")
    private int player_id;

    //Client to Server
    public ProtocolPlayKnightCard(String road1_id, int target) {
        this.road1_id = road1_id;
        this.target = target;
    }

    //Server to Client
    public ProtocolPlayKnightCard(String road1_id, int target, int player_id) {
        this.road1_id = road1_id;
        this.target = target;
        this.player_id = player_id;
    }

    public String getRoad1_id() {
        return road1_id;
    }

    public int getTarget() {
        return target;
    }

    public int getPlayer_id() {
        return player_id;
    }
}