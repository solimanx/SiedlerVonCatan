package protocol.clientinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

@Since(0.2)
public class ProtocolTradeIsCompleted {
    @SerializedName("Spieler")
    private int player_id;
    @SerializedName("Mitspieler")
    private int tradePartner_id;


    public ProtocolTradeIsCompleted(int player_id, int tradePartner_id) {
        this.player_id = player_id;
        this.tradePartner_id = tradePartner_id;

    }

    public int getPlayer_id() {
        return player_id;
    }

    public int getTradePartner_id() {
        return tradePartner_id;
    }
}
