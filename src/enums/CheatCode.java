package enums;

import com.google.gson.annotations.SerializedName;

// TODO: Auto-generated Javadoc
public enum CheatCode {

	@SerializedName("ROLLTWO") DICEROLL_2("ROLLTWO"),

	@SerializedName("ROLLTHREE") DICEROLL_3("ROLLTHREE"),

	@SerializedName("ROLLFOUR") DICEROLL_4("ROLLFOUR"),

	@SerializedName("ROLLFIVE") DICEROLL_5("ROLLFIVE"),

	@SerializedName("ROLLSIX") DICEROLL_6("ROLLSIX"),

	@SerializedName("ROLLSEVEN") DICEROLL_7("ROLLSEVEN"),

	@SerializedName("ROLLEIGHT") DICEROLL_8("ROLLEIGHT"),

	@SerializedName("ROLLNINE") DICEROLL_9("ROLLNINE"),

	@SerializedName("ROLLTEN") DICEROLL_10("ROLLTEN"),

	@SerializedName("ROLLELEVEN") DICEROLL_11("ROLLELEVEN"),

	@SerializedName("ROLLTWELVE") DICEROLL_12("ROLLTWELVE"),

	@SerializedName("KNIGHT") KNIGHT_CARD("KNIGHT"),

	@SerializedName("INVENTION") INVENTION_CARD("INVENTION"),

	@SerializedName("MONOPOLY") MONOPOLY_CARD("MONOPOLY"),

	@SerializedName("STREETBUILD") STREET_BUILD_CARD("STREETBUILD"),
	
	@SerializedName("VICTORY") VICTORY ("VICTORY"),

	@SerializedName("WIN") INSTANT_WIN("WIN"),

	@SerializedName("VICTORYPLUS") VICTORYPLUS("VICTORYPLUS"),

	@SerializedName("VICTORYMINUS") VICTORYMINUS("VICTORYMINUS"),

	@SerializedName("ELEMENTONE") INCREASE_ELEMENTONE("ELEMENTONE"),

	@SerializedName("ELEMENTTWO") INCREASE_ELEMENTTWO("ELEMENTTWO"),

	@SerializedName("ELEMENTTHREE") INCREASE_ELEMENTTHREE("ELEMENTTHREE"),

	@SerializedName("ELEMENTFOUR") INCREASE_ELEMENTFOUR("ELEMENTFOUR"),

	@SerializedName("ELEMENTFIVE") INCREASE_ELEMENTFIVE("ELEMENTFIVE"),

	@SerializedName("OTHERHAND") OTHER_HAND("OTHERHAND"),
	
	@SerializedName("ACTIVATELT") ACTIVATE_LT("ACTIVATELT"),
	
	@SerializedName("DEACTIVATELT") DEACTIVATE_LT("DEACTIVATELT"),
	
	@SerializedName("SOUNDTRACKONE") SOUNDTRACK_ONE("SOUNDTRACKONE"),
	
	@SerializedName("SOUNDTRACKTWO") SOUNDTRACK_TWO ("SOUNDTRACKTWO");
	

	private String value;

	/**
	 * Instantiates a new Cheatcode.
	 *
	 * @param value
	 *            the value
	 */
	CheatCode(String value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
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

	/**
	 * From string.
	 *
	 * @param value2 the value 2
	 * @return the cheat code
	 */
	public static CheatCode fromString(String value2) {
		if (value2 != null) {
			for (CheatCode c : CheatCode.values()) {
				if (value2.equalsIgnoreCase(c.value)) {
					return c;
				}
			}
		}
		return null;
	}
}
