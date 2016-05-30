package protocol.messaging;

import com.google.gson.annotations.SerializedName;

/**
 * <b>5.3 Chat empfangen</b>
 * <p>
 * Receiving a message from other players.
 * </p>
 * 
 * @version 0.1
 * 
 */
public class ProtocolChatReceiveMessage {

	@SerializedName("Absender")
	private int sender_id;
	@SerializedName("Nachricht")
	private String message;

	public ProtocolChatReceiveMessage(int sender_id, String message) {
		this.sender_id = sender_id;
		this.message = message;
	}

	public int getSender() {
		return sender_id;
	}

	public String getMessage() {
		return message;
	}
}
