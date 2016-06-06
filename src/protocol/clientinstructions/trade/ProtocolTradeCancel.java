package protocol.clientinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

@Since(0.2)
public class ProtocolTradeCancel {
    @SerializedName("Handel abbrechen")
    private int trade_id;

    public ProtocolTradeCancel(int trade_id) {
        this.trade_id = trade_id;
    }

    public int getTrade_id() {

        return trade_id;
    }
}


