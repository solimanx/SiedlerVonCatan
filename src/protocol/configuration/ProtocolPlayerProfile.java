package protocol.configuration;

import com.google.gson.annotations.SerializedName;

/**
 * <b>6 Konfiguration und Spielstart</b>
 * <p>
 * Setting up name and color for each player.
 * </p>
 * 
 * @version 0.1
 * 
 */
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
