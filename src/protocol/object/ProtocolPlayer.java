package protocol.object;

import com.google.gson.annotations.SerializedName;

public class ProtocolPlayer {

	@SerializedName("id")
	private int player_id;

	@SerializedName("Farbe")
	private ColorType color;

	@SerializedName("Name")
	private String name;

	@SerializedName("Status")
	private StatusType status;

	@SerializedName("Siegpunkte")
	private int victory_points;

	@SerializedName("Rohstoffe")
	private ProtocolResource resources;

	public ProtocolPlayer(int player_id, ColorType color, String name, StatusType status, int victory_points,
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

	public ColorType getColor() {
		return color;
	}

	public String getName() {
		return name;
	}

	public StatusType getStatus() {
		return status;
	}

	public int getVictory_points() {
		return victory_points;
	}

	public ProtocolResource getResources() {
		return resources;
	}

	public enum ColorType {

		@SerializedName("ROT") RED,

		@SerializedName("ORANGE") ORANGE,

		@SerializedName("BLAU") BLUE,

		@SerializedName("WHITE") WHITE

	}

	private enum StatusType {
		@SerializedName("Spiel starten") INITIAL_START,

		@SerializedName("Wartet auf Spielbeginn") INTIAL_WAITING,

		@SerializedName("Dorf bauen") BUILD_VILLAGE,

		@SerializedName("Straße bauen") BUILD_ROAD,

		@SerializedName("Würfeln") ROLLING_DICE,

		@SerializedName("Handeln oder Bauen") TRADING_OR_BUILDING,

		@SerializedName("Warten") WAITING,

		@SerializedName("Verbindung verloren") DISCONNECTED

	}
}
