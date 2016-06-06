package protocol.clientinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

@Since(0.2)
public class ProtocolTradeComplete {
    @SerializedName("Handel abschließen")
    private int trade_id;

    private int tradingPlayer_id;

    public ProtocolTradeComplete(int trade_id, int tradingPlayer_id) {
        this.trade_id = trade_id;
        this.tradingPlayer_id = tradingPlayer_id;
    }

    public int getTrade_id() {

        return trade_id;
    }

    public int getTradingPlayer_id() {

        return tradingPlayer_id;
    }
}


