package protocol.messaging;

import com.google.gson.annotations.SerializedName;

public class ProtocolServerConfirmation {

	@SerializedName("Name")
	private String name;
	@SerializedName("Farbe")
	private ColorType color;

	public ProtocolServerConfirmation(String name, ColorType color) {

		this.name = name;
		this.color = color;
	}
}
