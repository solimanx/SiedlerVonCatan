package protocol.messaging;

import com.google.gson.annotations.SerializedName;

public class ProtocolChatSendMessage {

	@SerializedName("Chatnachricht senden")
	private String sendMessage;
	@SerializedName("Nachricht")
	private String message;

	public ProtocolChatSendMessage(String sendMessage, String message) {

		this.sendMessage = sendMessage;
		this.message = message;
	}

	public String getSendMessage() {
		return sendMessage;
	}

	public String getMessage() {
		return message;
	}
}
