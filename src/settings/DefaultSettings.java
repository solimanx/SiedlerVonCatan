package settings;

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
	public final static int AMOUNT_DEVELOPMENT_CARDS = 6;

	// Build costs: {WOOD, CLAY, ORE, SHEEP, CORN}
	public final static int[] VILLAGE_BUILD_COST = { 1, 1, 0, 1, 1 };
	// TODO Fix this
	public final static int[] STREET_BUILD_COST = { 1, 1, 0, 0, 0 };
	// TODO Fix this
	public final static int[] CITY_BUILD_COST = { 0, 0, 3, 0, 2 };
	// TODO Fix this
	public final static int[] DEVCARD_BUILD_COST = { 0, 0, 0, 0, 0 };
	// Amount of Landscape Resource Cards: {WOOD, CLAY, ORE, SHEEP, CORN,
	// NOTHING (desert)}
	public final static int[] LANDSCAPE_CARDS = { 4, 3, 3, 4, 4, 1 };

	public final static ResourceType[] RESOURCE_ORDER = { ResourceType.WOOD, ResourceType.CLAY, ResourceType.ORE,
			ResourceType.SHEEP, ResourceType.CORN, ResourceType.NOTHING };

	public final static int[] DICE_NUMBERS = { 2, 5, 10, 8, 4, 11, 12, 9, 10, 8, 3, 6, 4, 9, 11, 3, 6, 5 };

	public final static int MAXIMUM_PLAYERS_AMOUNT = 4;

	public final static String PROTOCOL_VERSION = "0.2";

	public final static String CLIENT_VERSION = "JavaFXClient 0.2 (NiedlichePixel)";

	public static final String SERVER_VERSION = "0.2";
}
