package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * 
 * <b>Handelsangebot angenommen (Trade Confirmation)</b>
 * <p>
 * Server shows completion of trade.
 * </p>
 * 
 */

@Since(0.2)
public class ProtocolTradeConfirmation {

	@SerializedName("Spieler")
	private int player_id;

	@SerializedName("Handel id")
	private int trade_id;

	public ProtocolTradeConfirmation(int player_id, int trade_id) {
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
