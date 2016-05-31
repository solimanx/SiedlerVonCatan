package protocol.configuration;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

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

	public ProtocolError(String notice) {
		this.notice = notice;
	}

	public String getNotice() {
		return notice;
	}

}
