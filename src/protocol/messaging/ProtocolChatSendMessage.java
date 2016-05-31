package protocol.messaging;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * <b>Chat senden (Send message)</b>
 * <p>
 * Sending a message to other players.
 * </p>
 * 
 */

@Since(0.1)
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
