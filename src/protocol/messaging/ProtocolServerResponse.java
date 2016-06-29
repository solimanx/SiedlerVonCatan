package protocol.messaging;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

// TODO: Auto-generated Javadoc
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

	/**
	 * Instantiates a new protocol server response.
	 *
	 * @param serverResponse the server response
	 */
	public ProtocolServerResponse(String serverResponse) {
		this.serverResponse = serverResponse;
	}

	/**
	 * Gets the server response.
	 *
	 * @return the server response
	 */
	public String getServerResponse() {
		return serverResponse;
	}

}
