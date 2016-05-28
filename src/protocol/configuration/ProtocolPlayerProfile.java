package protocol.configuration;

import com.google.gson.annotations.SerializedName;

public class ProtocolPlayerProfile {

	@SerializedName("Name")
	private String name;
	@SerializedName("Farbe")
	private String color;

	public ProtocolPlayerProfile(String name, String color) {

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
