package protocol3.severinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * <b>Längste Handelsstraße(longest road </b>
 * <p>
 * Server sends to client the message about the Player who has the longest road.
 * </p>
 * Created on 06.06.2016.
 */

@Since(0.3)
public class ProtocolLongestRoad {

    @SerializedName("Spieler")
    Integer player_id;

    public ProtocolLongestRoad(Integer player_id) {
        this.player_id = player_id;
    }

    public ProtocolLongestRoad() {
    }

    public int getPlayer_id() {

        return player_id;
    }

}
