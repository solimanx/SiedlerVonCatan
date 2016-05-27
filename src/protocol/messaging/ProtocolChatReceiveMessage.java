package protocol.messaging;

public class ProtocolChatReceiveMessage {
	
	 @SerializedName("Absender")
		public int sender;
		@SerializedName("Nachricht")
		public String message;

}
