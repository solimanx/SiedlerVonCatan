package enums;

import com.google.gson.annotations.SerializedName;

public enum CardType {

	@SerializedName("Ritter") KNIGHT("Knight"),

	@SerializedName("Erfindung") INVENTION("Invention"),

	@SerializedName("Straßenbau") STREET("Build Streets"),

	@SerializedName("Monopol") MONOPOLY("Monopoly"),

	@SerializedName("Siegpunkt") VICTORYPOINT("Victorypoint"),

	@SerializedName("Unbekannt") UNKNOWN("Unknown");

	private String value;
	
	CardType(String value){
		this.value = value;
	}
	
	@Override
	public String toString(){
		return this.getValue();
	}

	public String getValue() {
		
		return value;
	}
}
