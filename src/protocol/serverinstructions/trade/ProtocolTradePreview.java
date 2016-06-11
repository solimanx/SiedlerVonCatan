package protocol.serverinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import protocol.object.ProtocolResource;

/**
 * not used Protocol
 * <b>Handel angenommen (Trade Confirmation)</b>
 * <p>
 * Server previews the detail of the offered trade.
 * </p>
 * 
 */

@Since(0.2)
public class ProtocolTradePreview {

	@SerializedName("Spieler")
	private int player_id;

	@SerializedName("Handel id")
	private int trade_id;

	@SerializedName("Angebot")
	private ProtocolResource offer;

	@SerializedName("Nachfrage")
	private ProtocolResource withdrawal;

	public ProtocolTradePreview(int player_id, int trade_id, ProtocolResource offer, ProtocolResource withdrawal) {
		this.player_id = player_id;
		this.trade_id = trade_id;
		this.offer = offer;
		this.withdrawal = withdrawal;
	}

	public int getPlayer_id() {
		return player_id;
	}

	public int getTrade_id() {
		return trade_id;
	}

	public ProtocolResource getOffer() {
		return offer;
	}

	public ProtocolResource getWithdrawal() {
		return withdrawal;
	}

}
