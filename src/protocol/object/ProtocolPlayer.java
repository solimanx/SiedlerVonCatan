package protocol.object;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import enums.Color;
/**
 * <b>Spieler (Player)</b>
 * <p>
 * Contains information about player: player ID, color, name , status, score,
 * and resources.
 * </p>
 * 
 */

@Since(0.1)
public class ProtocolPlayer {

	@SerializedName("id")
	private int player_id;

	@SerializedName("Farbe")
	private Color color;

	@SerializedName("Name")
	private String name;

	@SerializedName("Status")
	private String status;

	@SerializedName("Siegpunkte")
	private int victory_points;

	@SerializedName("Rohstoffe")
	private ProtocolResource resources;

	public ProtocolPlayer(int player_id, Color color, String name, String status, int victory_points,
			ProtocolResource resources) {
		this.player_id = player_id;
		this.color = color;
		this.name = name;
		this.status = status;
		this.victory_points = victory_points;
		this.resources = resources;
	}

	public int getPlayer_id() {
		return player_id;
	}

	public Color getColor() {
		return color;
	}

	public String getName() {
		return name;
	}

	public String getStatus() {
		return status;
	}

	public int getVictory_points() {
		return victory_points;
	}

	public ProtocolResource getResources() {
		return resources;
	}

}
