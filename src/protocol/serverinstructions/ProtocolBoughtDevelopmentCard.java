package protocol.serverinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import enums.CardType;

// TODO: Auto-generated Javadoc
/**
 * <b>9,7 Entwicklungskarte gekauft (bought development card) </b>
 * <p>
 * If player buys a development card, Server sends messade to Client (it does
 * not distinguish between Knight cards and development cards) The
 * "development card" has to be hidden for other players as Unknown.
 * </p>
 * Created on 06.06.2016.
 */

@Since(0.3)
public class ProtocolBoughtDevelopmentCard {
	@SerializedName("Spieler")

	private int playerID;

	@SerializedName("Entwicklungskarte")

	private CardType developmentCard;

	/**
	 * Instantiates a new protocol bought development card.
	 *
	 * @param player_id the player id
	 * @param developmentCard the development card
	 */
	public ProtocolBoughtDevelopmentCard(int player_id, CardType developmentCard) {
		this.playerID = player_id;
		this.developmentCard = developmentCard;
	}

	/**
	 * Gets the player ID.
	 *
	 * @return the player ID
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * Gets the development card.
	 *
	 * @return the development card
	 */
	public CardType getDevelopmentCard() {
		return developmentCard;
	}
}