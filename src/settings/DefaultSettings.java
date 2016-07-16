package settings;

import java.util.HashMap;
import java.util.Map;

import enums.ResourceType;

/**
 * Specifies all the settings for the board.
 */
public final class DefaultSettings {

	// 7 equates to 37 hexagons
	public static int boardSize = 7;
	public static int boardRadius = 3;

	public final static int MAX_VICTORY_POINTS = 10;

	public final static int START_AMOUNT_VILLAGES = 5;
	public final static int START_AMOUNT_CITIES = 4;
	public final static int START_AMOUNT_STREETS = 15;

	public static int amountKnightCards = 14;
	public static int amountVictoryCards = 5;

	public static int amountInventionCards = 2;
	public static int amountMonopolyCards = 2;
	public static int amountStreetBuildingCards = 2;
	public static int amountDevelopmentCards = amountKnightCards + amountVictoryCards + amountInventionCards
			+ amountMonopolyCards + amountStreetBuildingCards;

	// Costs related:
	// Build costs: {WOOD, CLAY, ORE, SHEEP, CORN}
	
	public final static int[] VILLAGE_BUILD_COST = { 1, 1, 0, 1, 1 };

	public final static int[] STREET_BUILD_COST = { 1, 1, 0, 0, 0 };

	public final static int[] CITY_BUILD_COST = { 0, 0, 3, 0, 2 };

	public final static int[] DEVCARD_BUILD_COST = { 0, 0, 1, 1, 1 };
	/**
	 * Amount of Landscape Field: {WOOD, CLAY, ORE, SHEEP, CORN, DESERT}
	 */
	public static int[] landscapeAmount = { 4, 3, 3, 4, 4, 1 };

	public final static ResourceType[] RESOURCE_ORDER = { ResourceType.WOOD, ResourceType.CLAY, ResourceType.ORE,
			ResourceType.SHEEP, ResourceType.CORN, ResourceType.NOTHING };

	@SuppressWarnings("serial")
	public final static Map<ResourceType, Integer> RESOURCE_VALUES = new HashMap<ResourceType, Integer>() {
		{
			put(ResourceType.WOOD, 0);
			put(ResourceType.CLAY, 1);
			put(ResourceType.ORE, 2);
			put(ResourceType.SHEEP, 3);
			put(ResourceType.CORN, 4);
			put(ResourceType.NOTHING, 5);
		}
	};

	public static int[] diceNumbers = { 5, 2, 6, 3, 8, 10, 9, 12, 11, 4, 8, 10, 9, 4, 5, 6, 3, 11 };

	public static int maxPlayersAmount = 4;
	public static int amountResourceCards = 19;

	//Server related
	
	public final static String PROTOCOL_VERSION = "1.0";

	public final static String CLIENT_VERSION = "JavaFXClient 1.0 (NiedlichePixel)";

	public final static String AI_VERSION = "NiedlichePixel (KI)";

	public final static String SERVER_VERSION = "1.0";

	public final static String SERVER_OK = "OK";

}
