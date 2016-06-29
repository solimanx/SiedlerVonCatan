/*
 * 
 */
package settings;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import enums.ResourceType;

public final class DefaultSettings {

	// 7 equates to 37 hexagons
	public final static int BOARD_SIZE = 7;
	public final static int BOARD_RADIUS = BOARD_SIZE / 2;

	public final static int MAX_VICTORY_POINTS = 10;

	public final static int START_AMOUNT_VILLAGES = 5;
	public final static int START_AMOUNT_CITIES = 4;
	public final static int START_AMOUNT_STREETS = 15;

	public final static int AMOUNT_KNIGHT_CARDS = 14;
	public final static int AMOUNT_VICTORY_CARDS = 5;

	public final static int AMOUNT_INVENTION_CARDS = 2;
	public final static int AMOUNT_MONOPOLY_CARDS = 2;
	public final static int AMOUNT_STREETBUILDING_CARDS = 2;
	public final static int AMOUNT_DEVELOPMENT_CARDS = AMOUNT_KNIGHT_CARDS + AMOUNT_VICTORY_CARDS
			+ AMOUNT_INVENTION_CARDS + AMOUNT_MONOPOLY_CARDS + AMOUNT_STREETBUILDING_CARDS;

	// Build costs: {WOOD, CLAY, ORE, SHEEP, CORN}
	public final static int[] VILLAGE_BUILD_COST = { 1, 1, 0, 1, 1 };
	// TODO Fix this
	public final static int[] STREET_BUILD_COST = { 1, 1, 0, 0, 0 };
	// TODO Fix this
	public final static int[] CITY_BUILD_COST = { 0, 0, 3, 0, 2 };
	// TODO Fix this
	public final static int[] DEVCARD_BUILD_COST = { 0, 0, 1, 1, 1 };
	// Amount of Landscape Resource Cards: {WOOD, CLAY, ORE, SHEEP, CORN}
	// NOTHING (desert)}
	public final static int[] LANDSCAPE_CARDS = { 4, 3, 3, 4, 4, 1 };

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

	public final static int[] DICE_NUMBERS = { 5, 2, 6, 3, 8, 10, 9, 12, 11, 4, 8, 10, 9, 4, 5, 6, 3, 11 };

	public final static int MAXIMUM_PLAYERS_AMOUNT = 4;

	public final static String PROTOCOL_VERSION = "1.0";

	public final static String CLIENT_VERSION = "JavaFXClient 1.0 (NiedlichePixel)";

	public final static String AI_VERSION = "NiedlichePixel (KI)";

	public static final String SERVER_VERSION = "1.0";

	/**
	 * Gets the current time.
	 *
	 * @return the current time
	 */
	// Get current time as string
	public static String getCurrentTime() {
		return "[" + LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")) + "] ";
	}

	public final static String SERVER_OK = "OK";
}
