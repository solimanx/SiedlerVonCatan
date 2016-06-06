package protocol.clientinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

@Since(0.2)
public class ProtocolTradeIsCanceled {
    @SerializedName("Spieler")
    private int player_id;
    @SerializedName("Handel id")
    private int trade_id;


    public ProtocolTradeIsCanceled(int player_id, int trade_id) {
        this.player_id = player_id;
        this.trade_id = trade_id;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public int getTrade_id() {
        return trade_id;
    }
}
