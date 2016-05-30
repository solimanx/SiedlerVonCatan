package protocol.messaging;

import com.google.gson.annotations.SerializedName;

/**
 * <b>5.2 Chat senden</b>
 * <p>
 * Sending a message to other players.
 * </p>
 * 
 * @version 0.1
 * 
 */
public class ProtocolChatSendMessage {

	@SerializedName("Nachricht")
	private String message;

	public ProtocolChatSendMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
