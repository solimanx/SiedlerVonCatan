package protocol.clientinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * <b>Handelsangebot annehmen (Accept trade offered)</b>
 * <p>
 * When client accepts the terms of the given trade.
 * </p>
 */

@Since(0.2)
public class ProtocolTradeAccept {

    @SerializedName("Handel id")
    private int trade_id;

    public ProtocolTradeAccept(int trade_id) {

        this.trade_id = trade_id;
    }

    public int getTrade_id() {

        return trade_id;
    }
}
