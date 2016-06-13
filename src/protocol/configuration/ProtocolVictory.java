package protocol.configuration;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

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

	public ProtocolVictory(String message, int winnerID) {
		this.message = message;
		this.winnerID = winnerID;
	}

	public String getMessage() {
		return message;
	}

	public int getWinnerID() {
		return winnerID;
	}

}
