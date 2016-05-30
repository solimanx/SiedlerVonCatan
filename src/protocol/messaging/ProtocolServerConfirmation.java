package protocol.messaging;

import com.google.gson.annotations.SerializedName;

/**
 * <b>5.1 Bestätigungen und Fehler</b>
 * <p>
 * Server confirmation of client's actions.
 * </p>
 * 
 * @version 0.1
 * 
 */
public class ProtocolServerConfirmation {

	@SerializedName("Serverantwort")
	private String server_response;

	public ProtocolServerConfirmation(String server_response) {
		this.server_response = server_response;
	}

	public String getServer_response() {
		return server_response;
	}

}
