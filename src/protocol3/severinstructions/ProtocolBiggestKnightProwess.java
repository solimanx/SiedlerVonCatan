package protocol3.severinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * Created  on 06.06.2016.
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
