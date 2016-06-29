package protocol.configuration;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

// TODO: Auto-generated Javadoc
/**
 * <b>Konfiguration und Spielstart (Victory)</b>
 * <p>
 * End game results.
 * </p>
 * 
 */

@Since(0.2)
public class ProtocolVictory {

	@SerializedName("Nachricht")
	private String message;

	@SerializedName("Sieger")
	private int winnerID;

	/**
	 * Instantiates a new protocol victory.
	 *
	 * @param message the message
	 * @param winnerID the winner ID
	 */
	public ProtocolVictory(String message, int winnerID) {
		this.message = message;
		this.winnerID = winnerID;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets the winner ID.
	 *
	 * @return the winner ID
	 */
	public int getWinnerID() {
		return winnerID;
	}

}
