package protocol.configuration;

import com.google.gson.annotations.SerializedName;

import protocol.object.ProtocolBoard;

/**
 * <b>6 Konfiguration und Spielstart</b>
 * <p>
 * Confirmation, if ProtocolClientReady goes through.
 * </p>
 * 
 * @version 0.1
 * 
 */
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