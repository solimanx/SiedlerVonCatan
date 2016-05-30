package protocol.configuration;

import com.google.gson.annotations.SerializedName;

/**
 * <b>6 Konfiguration und Spielstart</b>
 * <p>
 * Error, in case ProtocolClientReady didn't go through.
 * </p>
 * 
 * @version 0.1
 * 
 */
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
