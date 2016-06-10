package protocol3.severinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * <b>Größte Rittermacht(biggest knight prowess) </b>
 * <p>
 * Server sends to client the Message about the Player who has the biggest
 * knight prowess.
 * </p>
 * Created on 06.06.2016.
 */

@Since(0.3)
public class ProtocolBiggestKnightProwess {

    @SerializedName("Spieler")

    int player_id;

    public ProtocolBiggestKnightProwess(int player_id) {

        this.player_id = player_id;
    }

    public int getPlayer_id() {

        return player_id;
    }
}
