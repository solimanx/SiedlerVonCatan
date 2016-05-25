package enums.protocol;

import com.google.gson.annotations.SerializedName;

public enum PlayerState {
	
	@SerializedName("Spiel starten") STARTING_GAME,

	@SerializedName("Wartet auf Spielbeginn") INITIAL_WAITING,

	@SerializedName("Dorf bauen") BUILDING_VILLAGE,

	@SerializedName("Straße bauen") BUILDING_ROAD,

	@SerializedName("Würfeln") ROLLING_DICE,

	@SerializedName("Handeln oder Bauen") CURRENT_TURN,

	@SerializedName("Warten") WAITING,

	@SerializedName("Verbindung verloren") DISCONNECTED

}
