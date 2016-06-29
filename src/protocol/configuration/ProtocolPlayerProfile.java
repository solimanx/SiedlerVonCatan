package protocol.configuration;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
import enums.Color;

// TODO: Auto-generated Javadoc
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

	/**
	 * Instantiates a new protocol player profile.
	 *
	 * @param name the name
	 * @param color the color
	 */
	public ProtocolPlayerProfile(String name, enums.Color color) {

		this.name = name;
		this.color = color;

	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

}
