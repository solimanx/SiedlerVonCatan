package protocol.configuration;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
import enums.Color;

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
	private Color color;

	public ProtocolPlayerProfile(String name, enums.Color color) {

		this.name = name;
		this.color = color;

	}

	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}

}
