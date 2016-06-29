package protocol.configuration;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import protocol.object.ProtocolBoard;

// TODO: Auto-generated Javadoc
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

	/**
	 * Instantiates a new protocol game started.
	 *
	 * @param board the board
	 */
	public ProtocolGameStarted(ProtocolBoard board) {
		this.board = board;
	}

	/**
	 * Gets the board.
	 *
	 * @return the board
	 */
	public ProtocolBoard getBoard() {
		return board;
	}

}