package enums;

import com.google.gson.annotations.SerializedName;

public enum CardType {

	@SerializedName("Ritter") KNIGHT,

	@SerializedName("Erfindung") INVENTION,

	@SerializedName("Straßenbau") STREET,

	@SerializedName("Monopol") MONOPOLY,

	@SerializedName("Siegpunkt") VICTORYPOINT

}
