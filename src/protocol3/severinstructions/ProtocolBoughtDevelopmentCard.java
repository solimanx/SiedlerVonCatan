package protocol3.severinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
import enums.CardType;
import protocol3.clientinstructions.ProtocolDevelopmentCards;

/**
 * 9.7
 * Created on 09.06.2016.
 */
@Since(0.3)
public class ProtocolBoughtDevelopmentCard {
    @SerializedName("Spieler")

    private int player_id;

    @SerializedName("Entwicklungskarte")

    private CardType developmentCard;

    public ProtocolBoughtDevelopmentCard(int player_id, CardType developmentCard) {
        this.player_id = player_id;
        this.developmentCard = developmentCard;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public CardType getDevelopmentCard() {
        return developmentCard;
    }
}