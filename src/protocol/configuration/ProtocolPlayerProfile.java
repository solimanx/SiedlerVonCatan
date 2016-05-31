package protocol.configuration;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * <b>Konfiguration und Spielstart (Profile)</b>
 * <p>
 * Setting up name and color for each player.
 * </p>
 * 
 */

@Since(0.1)
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
