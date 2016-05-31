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
	private int winner_id;

	public ProtocolVictory(String message, int winner_id) {
		this.message = message;
		this.winner_id = winner_id;
	}

	public String getMessage() {
		return message;
	}

	public int getWinner_id() {
		return winner_id;
	}

}
