package protocol.configuration;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import protocol.object.ProtocolBoard;

/**
 * <b>Konfiguration und Spielstart (Game Can Start)</b>
 * <p>
 * Confirmation, if ProtocolClientReady goes through.
 * </p>
 * 
 */

@Since(0.1)
public class ProtocolGameStarted {

	@SerializedName("Karte")
	private ProtocolBoard board;

	public ProtocolGameStarted(ProtocolBoard board) {
		this.board = board;
	}

	public ProtocolBoard getBoard() {
		return board;
	}

}