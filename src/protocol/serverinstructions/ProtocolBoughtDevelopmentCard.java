package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
import protocol.object.ProtocolDevCard;

/**
 * <b>9,7 Entwicklungskarte gekauft (bought development card) </b>
 * <p>
 * If player buys a development card, Server sends messade to Client
 * (it does not distinguish between Knight cards and development cards)
 * The "development card" has to be hidden for other players as Unknown.
 * </p>
 * Created on 06.06.2016.
 */

@Since(0.3)
public class ProtocolBoughtDevelopmentCard {
    @SerializedName("Spieler")

    private int playerID;

    @SerializedName("Entwicklungskarte")

    private ProtocolDevCard developmentCard;

    public ProtocolBoughtDevelopmentCard(int player_id, ProtocolDevCard developmentCard) {
        this.playerID = player_id;
        this.developmentCard = developmentCard;
    }

    public int getPlayerID() {
        return playerID;
    }

    public ProtocolDevCard getDevelopmentCard() {
        return developmentCard;
    }
}