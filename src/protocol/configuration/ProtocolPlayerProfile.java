package protocol.configuration;

import com.google.gson.annotations.SerializedName;

import enums.protocol.ColorType;

public class ProtocolPlayerProfile {

	@SerializedName("Name")
	private String name;
	@SerializedName("Farbe")
	private ColorType color;

	public ProtocolPlayerProfile(String name, ColorType color) {

		this.name = name;
		this.color = color;
		
	}

	public String getName() {
		return name;
	}

	public ColorType getColor() {
		return color;
	}

}
