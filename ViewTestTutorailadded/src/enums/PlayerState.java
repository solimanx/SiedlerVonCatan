package enums;

public enum PlayerState {
	GAME_SARTING("Spiel starten"), WAITING_FOR_GAMESTART("Warte auf Beginn"), BUILDING_VILLAGE("Dorf bauen"), BUILDING_STREET("Strasse bauen"), DICEROLLING("würfeln"), DISPENDE_CARDS_ROBBER_LOSS(""), MOVE_ROBBER("Räuber bewegen"), TRADING_OR_BUILDING("Handeln/Bauen"), WAITING("Wartet"), CONNECTION_LOST("Verbindung unterbrochen"),

	// OLD enums for debugging only
	TRADING("Handeln"), OFFERING("Bieten"), PLAYING("Spielen");
	
	
	private String value;
	PlayerState(String value){
		this.value = value;
	}
	
	@Override
	public String toString(){
		return this.getValue();
	}

	private String getValue() {
		
		return value;
	}
}
