package protocol.messaging;
import com.google.gson.annotations.SerializedName;

public class ProtocolChatSendMessage {
	 @SerializedName("Chatnachricht senden")
		public String sendMessage;
		@SerializedName("Nachricht")
		public String message ;
}
