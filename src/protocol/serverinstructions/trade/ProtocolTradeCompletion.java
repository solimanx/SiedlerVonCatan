package protocol.serverinstructions.trade;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * <b>Handel ausgeführt (trade was completed)</b>
 * <p>
 * Server confirms that trade was completed and sends information to players
 * <p/>
 */

@Since(0.2)
public class ProtocolTradeCompletion {

	@SerializedName("Spieler")
	private int playerID;

	@SerializedName("Mitspieler")
	private int tradePartnerID;

	public ProtocolTradeCompletion(int playerID, int tradePartnerID) {

		this.playerID = playerID;
		this.tradePartnerID = tradePartnerID;

	}

	public int getPlayerID() {

		return playerID;
	}

	public int getTradePartnerID() {

		return tradePartnerID;
	}
}
