package protocol.dualinstructions;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import model.unit.Index;

// TODO: Auto-generated Javadoc
/**
 * <b>Ritter ausspielen(play the knight card)12.1</b>
 * <p>
 * Server sends to all the clients, the message about the Player who played this
 * card.
 * </p>
 * Created on 06.06.2016.
 */

@Since(1.0)
public class ProtocolPlayKnightCard {
	@SerializedName("Spieler")
	private Integer playerID;

	@SerializedName("Ort")
	private Index fieldID;

	@SerializedName("Ziel")
	private Integer victimID;

	/**
	 * Instantiates a new protocol play knight card.
	 *
	 * @param fieldID the field ID
	 * @param target the target
	 */
	// Client to Server
	public ProtocolPlayKnightCard(Index fieldID, Integer target) {
		this.fieldID = fieldID;
		this.victimID = target;
	}

	/**
	 * Instantiates a new protocol play knight card.
	 *
	 * @param playerID the player ID
	 * @param fieldID the field ID
	 * @param victimID the victim ID
	 */
	// Server to Client
	public ProtocolPlayKnightCard(Integer playerID, Index fieldID, Integer victimID) {
		this.playerID = playerID;
		this.fieldID = fieldID;
		this.victimID = victimID;

	}

	/**
	 * Gets the location ID.
	 *
	 * @return the location ID
	 */
	public Index getLocationID() {
		return fieldID;
	}

	/**
	 * Gets the victim ID.
	 *
	 * @return the victim ID
	 */
	public Integer getVictimID() {
		return victimID;
	}

	/**
	 * Gets the player ID.
	 *
	 * @return the player ID
	 */
	public Integer getPlayerID() {
		return playerID;
	}
}