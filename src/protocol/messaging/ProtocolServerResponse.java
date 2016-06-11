package protocol.messaging;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * <b>Bestätigungen und Fehler (Confirmations and Errors)</b>
 * <p>
 * Server confirmation of client's actions.
 * </p>
 * 
 */

@Since(0.1)
public class ProtocolServerResponse {

	@SerializedName("Serverantwort")
	private String serverResponse;

	public ProtocolServerResponse(String serverResponse) {
		this.serverResponse = serverResponse;
	}

	public String getServerResponse() {
		return serverResponse;
	}

}
