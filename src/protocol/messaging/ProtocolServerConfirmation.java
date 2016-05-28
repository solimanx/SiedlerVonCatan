package protocol.messaging;

import com.google.gson.annotations.SerializedName;

public class ProtocolServerConfirmation {

	@SerializedName("Name")
	private String name;
	@SerializedName("Farbe")
	private String color;

	public ProtocolServerConfirmation(String name, String color) {

		this.name = name;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}
}
