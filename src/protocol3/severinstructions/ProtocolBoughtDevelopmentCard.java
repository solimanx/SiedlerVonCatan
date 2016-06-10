package protocol3.severinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
import protocol3.clientinstructions.ProtocolDevelopmentCards;

/**
 * Created on 09.06.2016.
 */
@Since(0.3)
public class ProtocolBoughtDevelopmentCard {
    @SerializedName("Spieler")

    private int player_id;

    @SerializedName("Entwicklungskarte")

    private ProtocolDevelopmentCards developmentCards;

    public ProtocolBoughtDevelopmentCard(int player_id, ProtocolDevelopmentCards developmentCards) {

        this.player_id = player_id;
        this.developmentCards = developmentCards;
    }

    public int getPlayer_id() {

        return player_id;
    }

    public ProtocolDevelopmentCards getDevelopmentCards() {
        return developmentCards;
    }
}
