package protocol.configuration;

import com.google.gson.annotations.SerializedName;

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
