package enums;

import com.google.gson.annotations.SerializedName;

public enum PlayerState {
	@SerializedName("Spiel starten") GAME_STARTING,

	@SerializedName("Wartet auf Spielbeginn") WAITING_FOR_GAMESTART,

	@SerializedName("Dorf bauen") BUILDING_VILLAGE,

	@SerializedName("Straße bauen") BUILDING_STREET,

	@SerializedName("Würfeln") DICEROLLING,

	@SerializedName("Karten wegen Räuber abgeben") DISPENSE_CARDS_ROBBER_LOSS,

	@SerializedName("Räuber versetzen") MOVE_ROBBER,

	@SerializedName("Handeln oder Bauen") TRADING_OR_BUILDING,

	@SerializedName("Warten") WAITING,

	@SerializedName("Verbindung verloren") CONNECTION_LOST,

	// OLD enums for debugging only
	TRADING, OFFERING, PLAYING
}
