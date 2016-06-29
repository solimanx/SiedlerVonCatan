package protocol.messaging;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

// TODO: Auto-generated Javadoc
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

	/**
	 * Instantiates a new protocol chat send message.
	 *
	 * @param message the message
	 */
	public ProtocolChatSendMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

}
