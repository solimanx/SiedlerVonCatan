package protocol.messaging;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * <b>Chat empfangen (Receive Message)</b>
 * <p>
 * Receiving a message from other players.
 * </p>
 * 
 */

@Since(0.1)
public class ProtocolChatReceiveMessage {

	@SerializedName("Absender")
	private int senderID;

	@SerializedName("Nachricht")
	private String message;

	public ProtocolChatReceiveMessage(int senderID, String message) {
		this.senderID = senderID;
		this.message = message;
	}

	public int getSender() {
		return senderID;
	}

	public String getMessage() {
		return message;
	}
}
