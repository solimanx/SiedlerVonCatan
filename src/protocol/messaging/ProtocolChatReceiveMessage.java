package protocol.messaging;

import com.google.gson.annotations.SerializedName;

public class ProtocolChatReceiveMessage {

	@SerializedName("Absender")
	private int sender;
	@SerializedName("Nachricht")
	private String message;

	public ProtocolChatReceiveMessage(int sender, String message) {
		this.sender = sender;
		this.message = message;
	}
}
