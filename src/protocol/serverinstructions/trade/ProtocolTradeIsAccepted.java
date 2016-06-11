package protocol.serverinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * not used Protocol
 * <b>Handelsangebot angenommen(Trade request was accepted)</b>
 * <p>
 * Server sends action information to players.
 * </p>
 */

@Since(0.2)
public class ProtocolTradeIsAccepted {

	@SerializedName("Spieler")
	private int player_id;

	@SerializedName("Handel id")
	private int trade_id;

	public int getPlayer_id() {

		return player_id;
	}

	public int getTrade_id() {

		return trade_id;
	}

	public ProtocolTradeIsAccepted(int player_id, int trade_id) {

		this.player_id = player_id;
		this.trade_id = trade_id;

	}
}
