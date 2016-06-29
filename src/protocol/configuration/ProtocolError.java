package protocol.configuration;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

// TODO: Auto-generated Javadoc
/**
 * <b>Konfiguration und Spielstart (Error)</b>
 * <p>
 * Error, in case ProtocolClientReady didn't go through.
 * </p>
 * 
 */

@Since(0.1)
public class ProtocolError {

	@SerializedName("Meldung")
	private String notice;

	/**
	 * Instantiates a new protocol error.
	 *
	 * @param notice the notice
	 */
	public ProtocolError(String notice) {
		this.notice = notice;
	}

	/**
	 * Gets the notice.
	 *
	 * @return the notice
	 */
	public String getNotice() {
		return notice;
	}

}
