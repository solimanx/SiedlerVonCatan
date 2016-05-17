package settings;

public final class DefaultSettings {

    public final static int BOARD_SIZE = 37;

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
    public final static int[] STREET_BUILD_COST = { 0, 0, 0, 0, 0 };
    // TODO Fix this
    public final static int[] CITY_BUILD_COST = { 0, 0, 0, 0, 0 };
    // TODO Fix this
    public final static int[] DEVCARD_BUILD_COST = { 0, 0, 0, 0, 0 };

}
