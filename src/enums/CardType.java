package enums;

import com.google.gson.annotations.SerializedName;

// TODO: Auto-generated Javadoc
public enum CardType {

	@SerializedName("Ritter") KNIGHT("Knight"),

	@SerializedName("Erfindung") INVENTION("Invention"),

	@SerializedName("Straßenbau") STREET("Build Streets"),

	@SerializedName("Monopol") MONOPOLY("Monopoly"),

	@SerializedName("Siegpunkt") VICTORYPOINT("Victorypoint"),

	@SerializedName("Unbekannt") UNKNOWN("Unknown");

	private String value;

	/**
	 * Instantiates a new card type.
	 *
	 * @param value the value
	 */
	CardType(String value) {
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
	public String getValue() {

		return value;
	}
}
