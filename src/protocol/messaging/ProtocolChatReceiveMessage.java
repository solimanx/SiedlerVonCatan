package protocol.messaging;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

// TODO: Auto-generated Javadoc
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
	private Integer senderID;

	@SerializedName("Nachricht")
	private String message;

	/**
	 * Instantiates a new protocol chat receive message.
	 *
	 * @param senderID the sender ID
	 * @param message the message
	 */
	public ProtocolChatReceiveMessage(Integer senderID, String message) {
		this.senderID = senderID;
		this.message = message;
	}

	/**
	 * Gets the sender.
	 *
	 * @return the sender
	 */
	public Integer getSender() {
		return senderID;
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
