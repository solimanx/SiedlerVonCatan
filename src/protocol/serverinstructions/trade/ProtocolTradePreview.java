package protocol.serverinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import protocol.object.ProtocolResource;

/**
 * 
 * <b>Handelsangebot (Trade Confirmation)</b>
 * <p>
 * Server previews the detail of the offered trade.
 * </p>
 * 
 */

@Since(0.2)
public class ProtocolTradePreview {

	@SerializedName("Spieler")
	private int playerID;

	@SerializedName("Handel id")
	private int tradeID;

	@SerializedName("Angebot")
	private ProtocolResource offer;

	@SerializedName("Nachfrage")
	private ProtocolResource withdrawal;

	public ProtocolTradePreview(int playerID, int tradeID, ProtocolResource offer, ProtocolResource withdrawal) {
		this.playerID = playerID;
		this.tradeID = tradeID;
		this.offer = offer;
		this.withdrawal = withdrawal;
	}

	public int getPlayerID() {
		return playerID;
	}

	public int getTradeID() {
		return tradeID;
	}

	public ProtocolResource getOffer() {
		return offer;
	}

	public ProtocolResource getWithdrawal() {
		return withdrawal;
	}

}
