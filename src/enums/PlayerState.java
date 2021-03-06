package enums;

import com.google.gson.annotations.SerializedName;

// TODO: Auto-generated Javadoc
public enum PlayerState {
	@SerializedName("Spiel starten") GAME_STARTING("Setting up"),

	@SerializedName("Wartet auf Spielbeginn") WAITING_FOR_GAMESTART("Waiting for game begin"),

	@SerializedName("Dorf bauen") BUILDING_VILLAGE("Building settlement"),

	@SerializedName("Straße bauen") BUILDING_STREET("Building road"),

	@SerializedName("Würfeln") DICEROLLING("Rolling dice"),

	@SerializedName("Karten wegen Räuber abgeben") DISPENSE_CARDS_ROBBER_LOSS("Losing resources to robber"),

	@SerializedName("Räuber versetzen") MOVE_ROBBER("Moving robber"),

	@SerializedName("Handeln oder Bauen") TRADING_OR_BUILDING("Trading/Building"),

	@SerializedName("Bauen") BUILDING("BUILDING"), //WARNING: 5-6 PLAYERS ONLY

	@SerializedName("Warten") WAITING("Waiting"),

	@SerializedName("Verbindung verloren") CONNECTION_LOST("Disconnected"),

	// OLD enums for debugging only
	TRADING(""), OFFERING(""), PLAYING("");

	private String value;

	/**
	 * Instantiates a new player state.
	 *
	 * @param value the value
	 */
	PlayerState(String value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return this.getValue();
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	private String getValue() {

		return value;
	}
}
