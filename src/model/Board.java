package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import enums.CornerStatus;
import enums.HarbourStatus;
import enums.ResourceType;
import model.objects.Corner;
import model.objects.Edge;
import model.objects.Field;
import model.objects.PlayerModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import settings.DefaultSettings;

// TODO: Auto-generated Javadoc
/**
 * Contains all Board methods, retrieving fields, edges and corners.
 *
 * @author Adam
 */
public class Board {
	// ================================================================================
	// CLASS FIELDS
	// ================================================================================

	private Field[][] fields;
	private Corner[][][] corners;
	private Edge[][][] edges;
	private PlayerModel[] players;
	private String banditLocation;
	private DevelopmentCardsStack devCardStack;
	private Corner[] harbourCorners;
	// Field
	private static Map<String, int[]> stringToCoordMap;
	private static Map<Index, String> coordToStringMap;
	// Corner
	private static Map<String, int[]> cMap;
	// Edge
	private static Map<String, int[]> eMap;
	private static Logger logger = LogManager.getLogger(Board.class.getSimpleName());


	// ================================================================================
	// CONSTRUCTORS
	// ================================================================================

	/**
	 * Instantiates a new board.
	 */
	public Board() {
		int r = DefaultSettings.boardSize;
		fields = new Field[r][r];
		initializeFields();
		initializeHashMaps();
		corners = new Corner[r][r][2];
		initializeCorners();
		edges = new Edge[r][r][3];
		initializeEdges();
		devCardStack = new DevelopmentCardsStack();
		initializeID();
		players = new PlayerModel[DefaultSettings.maxPlayersAmount];
		for (int i = 0; i < DefaultSettings.maxPlayersAmount; i++) {
			players[i] = new PlayerModel(i);
		}

	}

	/**
	 * Extend board for 5-6 players.
	 *
	 * @param board the board
	 * @return
	 */
	public static void extendBoard() {
		DefaultSettings.boardSize++;
		DefaultSettings.boardRadius++;
		DefaultSettings.maxPlayersAmount += 2;
		DefaultSettings.diceNumbers = new int[] { 2, 5, 4, 6, 3, 9, 8, 11, 11, 10, 6, 3, 8, 4, 8, 10, 11, 12, 10, 5, 4,
				9, 5, 9, 12, 3, 2, 6 };
		// WOOD
		DefaultSettings.landscapeAmount[0] += 2;
		// CLAY
		DefaultSettings.landscapeAmount[1] += 2;
		// ORE
		DefaultSettings.landscapeAmount[2] += 2;
		// SHEEP
		DefaultSettings.landscapeAmount[3] += 2;
		// CORN
		DefaultSettings.landscapeAmount[4] += 2;
		// DESERT
		DefaultSettings.landscapeAmount[5] += 1;

		//TODO rohstoffkarten
		DefaultSettings.amountResourceCards += 6;
		//TODO harbours
		//Dev Cards
		DefaultSettings.amountDevelopmentCards += 9;
		//Knight
		DefaultSettings.amountKnightCards += 6;
		//Monopoly
		DefaultSettings.amountMonopolyCards += 1;
		//Invention
		DefaultSettings.amountInventionCards+= 1;
		//StreetBuilding
		DefaultSettings.amountStreetBuildingCards += 1;
	}

	// ================================================================================
	// CLASS FIELDS INITIALIZATION
	// ================================================================================

	/**
	 * Initialize used cells axial in the 2d array, unused cells remain null.
	 * <p>
	 * // * @param fields
	 */
	private void initializeFields() {
		int aX; // axial x coordinate
		int aY; // axial y coordinate
		int absoluteValue;
		int radius = DefaultSettings.boardRadius;
		for (int x = 0; x < fields.length; x++) {
			for (int y = 0; y < fields[0].length; y++) {
				aX = x - radius;
				aY = y - radius;
				absoluteValue = Math.abs(aX + aY);
				if (absoluteValue <= radius) {
					fields[x][y] = new Field();
				}
			}
		}
	}

	/**
	 * Initialize String-to-axial coordinate HashMap and it's reverse, and
	 * systematically assigns IDs to each field by it's coordinate.
	 */
	/*
	 * Warning: Works as intended ONLY if amount of inner fields is not greater
	 * than 26 fields!
	 *
	 */
	private void initializeHashMaps() {
		// initialize HashMaps
		stringToCoordMap = new HashMap<String, int[]>();
		coordToStringMap = new HashMap<Index, String>();
		cMap = new HashMap<String, int[]>();
		eMap = new HashMap<String, int[]>();
		// temp to make code look clearer
		int radius = DefaultSettings.boardRadius;

		// Starting indices.
		char outerFieldsBegin = 'a';
		char innerFieldsBegin = 'A';

		// iterate through all fields row by row from top to bottom
		for (int j = -radius; j <= radius; j++) {
			for (int i = -radius; i <= radius; i++) {
				// to skip null/undefined fields
				if (getFieldAt(i, j) != null) {
					if (HexService.sumAbsCubeXYZ(HexService.convertAxialToCube(new int[] { i, j })) == radius * 2) {
						stringToCoordMap.put(String.valueOf(outerFieldsBegin), new int[] { i, j });
						coordToStringMap.put(new Index(i, j), String.valueOf(outerFieldsBegin));
						// go to next char : a->b->c->...
						outerFieldsBegin++;
					} else if (HexService.sumAbsCubeXYZ(HexService.convertAxialToCube(new int[] { i, j })) < radius
							* 2) {
						stringToCoordMap.put(String.valueOf(innerFieldsBegin), new int[] { i, j });
						coordToStringMap.put(new Index(i, j), String.valueOf(innerFieldsBegin));
						// go to next char : A->B->C->...
						innerFieldsBegin++;
					}
				}
			}
		}
	}

	/**
	 * Initialize the corners systematically
	 * <p>
	 * //* @param corners.
	 */
	private void initializeCorners() {
		// temporary variables to make code appear clearer
		int[] temp;
		int radius = DefaultSettings.boardRadius;
		// Go through HashMap<String,int[]>
		for (Map.Entry<String, int[]> entry : stringToCoordMap.entrySet()) {
			// entry of key
			temp = entry.getValue();
			// lowercase keys only (outer ring)
			if (entry.getKey().matches("[a-z]")) {
				// North only will be set
				if (HexService.sumOfCubeXY(HexService.convertAxialToCube(temp)) < 0) {
					corners[temp[0] + radius][temp[1] + radius][0] = new Corner();
				}
				// south only will be set
				else if (HexService.sumOfCubeXY(HexService.convertAxialToCube(temp)) > 0) {
					// convert axial back to array coords
					corners[temp[0] + radius][temp[1] + radius][1] = new Corner();
				}
			}
			// outercase keys only (inner rings/fields)
			else {
				// North set
				corners[temp[0] + radius][temp[1] + radius][0] = new Corner();

				corners[temp[0] + radius][temp[1] + radius][1] = new Corner();

			}

		}
	}

	/**
	 * Initialize the edges systematically;
	 * <p>
	 * //* @param edges.
	 */
	/*
	 * NW = north west, NE = north east, E = east
	 */
	private void initializeEdges() {
		// temporary variables to make code appear clearer
		int[] temp;
		int radius = DefaultSettings.boardRadius;
		// Go through HashMap<String,int[]>
		for (Map.Entry<String, int[]> entry : stringToCoordMap.entrySet()) {
			// entry of key
			temp = entry.getValue();
			// lowercase keys only (outer ring)
			if (entry.getKey().matches("[a-z]")) {
				// NW only is defined
				if (temp[0] >= 0 && temp[1] > 0) {
					edges[temp[0] + radius][temp[1] + radius][0] = new Edge();
				}
				// NE only is defined
				else if (temp[0] == -radius && temp[1] == radius) {
					edges[temp[0] + radius][temp[1] + radius][1] = new Edge();
				}

				// E only is defined
				else if (temp[0] < 0 && temp[1] <= 0) {
					edges[temp[0] + radius][temp[1] + radius][2] = new Edge();
				}
				// only NW + NE are defined
				else if (temp[0] > -radius && temp[0] < 0 && temp[1] == radius) {
					edges[temp[0] + radius][temp[1] + radius][0] = new Edge();

					edges[temp[0] + radius][temp[1] + radius][1] = new Edge();
				}
				// only NE + E are defined
				else if (temp[0] == -radius && temp[1] < radius && temp[1] > 0) {
					edges[temp[0] + radius][temp[1] + radius][1] = new Edge();
					edges[temp[0] + radius][temp[1] + radius][2] = new Edge();

				}
			}
			// outercase keys only (inner rings/fields)
			else {
				// NE, NW, E are set
				edges[temp[0] + radius][temp[1] + radius][0] = new Edge();

				edges[temp[0] + radius][temp[1] + radius][1] = new Edge();

				edges[temp[0] + radius][temp[1] + radius][2] = new Edge();
			}

		}
	}

	/**
	 * Initialize ID for field, corners and edges.
	 */
	private void initializeID() {
		// fields
		int radius = DefaultSettings.boardRadius;
		for (int i = -radius; i <= radius; i++) {
			for (int j = -radius; j <= radius; j++) {
				if (getFieldAt(j, i) != null) {
					Index index = new Index(j, i);
					String ID = coordToStringMap.get(index);
					getFieldAt(j, i).setFieldID(ID);
				}
			}
		}

		for (int i = -radius; i <= radius; i++) {
			for (int j = -radius; j <= radius; j++) {
				for (int k = 0; k < 3; k++) {
					if (getCornerAt(j, i, k) != null) {
						// touching fields
						Field[] tF = getTouchingFields(j, i, k);
						String ID1 = tF[0] == null ? "" : tF[0].getFieldID();
						String ID2 = tF[1] == null ? "" : tF[1].getFieldID();
						String ID3 = tF[1] == null ? "" : tF[2].getFieldID();
						String ID = ID1 + ID2 + ID3;
						getCornerAt(j, i, k).setCornerID(ID);
						cMap.put(ID, new int[] { j, i, k });
					}

					if (getEdgeAt(j, i, k) != null) {
						// connected fields
						Field[] cF = getConnectedFields(j, i, k);
						String ID1 = cF[0] == null ? "" : cF[0].getFieldID();
						String ID2 = cF[1] == null ? "" : cF[1].getFieldID();
						String ID = ID1 + ID2;
						getEdgeAt(j, i, k).setEdgeID(ID);
						eMap.put(ID, new int[] { j, i, k });
					}
				}
			}
		}

	}

	// ================================================================================
	// GETTERS AND SETTERS
	// ================================================================================

	/**
	 * Returns field given axial coordinates, if field doesn't exist then null.
	 *
	 * @param aX
	 *            Field axial x-coordinate
	 * @param aY
	 *            Field axial y-coordinate
	 * @return the field at
	 */

	public Field getFieldAt(int aX, int aY) {
		// temp to make code clearer
		int radius = DefaultSettings.boardRadius;
		// if out of range
		if (aX < -radius || aX > radius || aY < -radius || aY > radius) {
			return null;
		} else {
			return fields[aX + radius][aY + radius];
		}
	}

	/**
	 * Returns corner given axial coordinates and index(direction), if corner
	 * doesn't exist then null.
	 *
	 * @param aX
	 *            Corner axial x-coordinate
	 * @param aY
	 *            Corner axial y-coordinate
	 * @param dir
	 *            Corner direction (0 = North, 1 = South)
	 * @return the corner at
	 */

	public Corner getCornerAt(int aX, int aY, int dir) {
		// temp to make code clearer
		int radius = DefaultSettings.boardRadius;
		// if out of range
		if (aX < -radius || aX > radius || aY < -radius || aY > radius || dir < 0 || dir > 1) {
			return null;
		} else {
			return this.corners[aX + radius][aY + radius][dir];
		}
	}

	/**
	 * Returns edge given axial coordinates and index(direction), if edge
	 * doesn't exist then null.
	 *
	 * @param aX
	 *            Edge axial x-coordinate
	 * @param aY
	 *            Edge axial y-coordinate
	 * @param dir
	 *            Edge index/direction (0 = NorthWest, 1 = NorthEast, 2 = East)
	 * @return the edge at
	 */

	public Edge getEdgeAt(int aX, int aY, int dir) {
		// temp to make code clearer
		int radius = DefaultSettings.boardRadius;
		// if out of range
		if (aX < -radius || aX > radius || aY < -radius || aY > radius || dir < 0 || dir > 2) {
			return null;
		} else {
			return this.edges[aX + radius][aY + radius][dir];
		}
	}

	/**
	 * Returns fields by normal array index.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return the field
	 */

	@Deprecated
	public Field getField(int x, int y) {
		return this.fields[x][y];
	}

	/**
	 * Returns corners by normal array index and direction.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param dir
	 *            the dir
	 * @return the corner
	 */

	@Deprecated
	public Corner getCorner(int x, int y, int dir) {
		return this.corners[x][y][dir];
	}

	/**
	 * Returns edges array by normal array index and direction.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param dir
	 *            the dir
	 * @return the edge
	 */
	@Deprecated
	public Edge getEdge(int x, int y, int dir) {
		return this.edges[x][y][dir];
	}

	/**
	 * Returns a Player object.
	 *
	 * @param i
	 *            the i
	 * @return the player
	 */

	public PlayerModel getPlayer(int i) {
		return this.players[i] != null ? this.players[i] : null;
	}

	/**
	 * Gets the player models.
	 *
	 * @return the player models
	 */
	public PlayerModel[] getPlayerModels() {
		return this.players;
	}

	/**
	 * Returns the amount of players.
	 *
	 * @return the amount players
	 */
	public int getAmountPlayers() {
		return this.players.length;
	}

	/**
	 * Returns bandit.
	 *
	 * @return the bandit
	 */

	public String getBandit() {
		return this.banditLocation;
	}

	/**
	 * Sets bandit location.
	 *
	 * @param banditLocation
	 *            the new bandit
	 */

	public void setBandit(String banditLocation) {
		this.banditLocation = banditLocation;

	}

	/**
	 * Sets the field.
	 *
	 * @param i
	 *            the i
	 * @param j
	 *            the j
	 * @param resourceType
	 *            the resource type
	 * @param diceIndex
	 *            the dice index
	 */
	@Deprecated
	public void setField(int i, int j, ResourceType resourceType, Integer diceIndex) {
		fields[i][j].setResourceType(resourceType);
		fields[i][j].setDiceIndex(diceIndex);

	}

	/**
	 * Sets the field at.
	 *
	 * @param i
	 *            axial X
	 * @param j
	 *            axial Y
	 * @param resourceType
	 *            the resource type
	 * @param diceIndex
	 *            the dice index
	 */
	public void setFieldAt(int i, int j, ResourceType resourceType, Integer diceIndex) {
		int radius = DefaultSettings.boardRadius;
		if (fields[i + radius][j + radius] != null) {
			fields[i + radius][j + radius].setResourceType(resourceType);
			fields[i + radius][j + radius].setDiceIndex(diceIndex);
		} else {
			logger.warn("Throws new Illegal Argument Exception\"Field doesn't exist\"");
			throw new IllegalArgumentException("Field doesn't exist");
		}

	}

	/**
	 * Sets corner.
	 *
	 * @param i
	 *            corner axial x
	 * @param j
	 *            corner axial y
	 * @param k
	 *            corner direction
	 * @param status
	 *            corner status
	 * @param hstatus
	 *            corner harbourstatus
	 * @param ownerID
	 *            corner owner
	 */
	@Deprecated
	public void setCorner(int i, int j, int k, CornerStatus status, HarbourStatus hstatus, int ownerID) {
		corners[i][j][k].setHarbourStatus(hstatus);
		corners[i][j][k].setStatus(status);
		corners[i][j][k].setOwnerID(ownerID);

	}

	/**
	 * Sets edge.
	 *
	 * @param i
	 *            edge axial x
	 * @param j
	 *            edge axial y
	 * @param k
	 *            edge direction
	 * @param hasStreet
	 *            if edge has street
	 * @param ownedByPlayer
	 *            edge owner
	 */
	@Deprecated
	public void setEdge(int i, int j, int k, boolean hasStreet, int ownedByPlayer) {
		edges[i][j][k].setHasStreet(hasStreet);
		edges[i][j][k].setOwnedByPlayer(ownedByPlayer);

	}

	/**
	 * Return ID to Coordinate map.
	 *
	 * @return the stringToCoordMap
	 */
	public static Map<String, int[]> getStringToCoordMap() {
		return stringToCoordMap;
	}

	/**
	 * Return Coordinate to ID map.
	 *
	 * @return the coordToStringMap
	 */
	public static Map<Index, String> getCoordToStringMap() {
		return coordToStringMap;
	}

	/**
	 * Gets the c map.
	 *
	 * @return the c map
	 */
	public static Map<String, int[]> getCMap() {
		return cMap;

	}

	/**
	 * Gets the e map.
	 *
	 * @return the e map
	 */
	public static Map<String, int[]> getEMap() {
		return eMap;
	}

	/**
	 * Gets the dev card stack.
	 *
	 * @return returns the DevelopmentCardStack
	 */
	public DevelopmentCardsStack getDevCardStack() {
		return devCardStack;
	}

	// ================================================================================
	// HEXAGONAL RELATIONS (TO FIELD)
	// ================================================================================

	/**
	 * Get neighbouring fields of the specified field with the following order:
	 * <p>
	 * {NorthEast, East, SouthEast, SouthWest, West, NorthWest}.
	 *
	 * @param aX
	 *            Field axial x-coordinate
	 * @param aY
	 *            Field axial y-coordinate
	 * @return the neighbouring fields
	 */

	public Field[] getNeighbouringFields(int aX, int aY) {
		Field ne = getFieldAt(aX + 1, aY - 1);
		Field e = getFieldAt(aX + 1, aY);
		Field se = getFieldAt(aX, aY + 1);
		Field sw = getFieldAt(aX - 1, aY + 1);
		Field w = getFieldAt(aX - 1, aY);
		Field nw = getFieldAt(aX, aY - 1);
		Field[] neighbours = { ne, e, se, sw, w, nw };
		return neighbours;
	}

	/**
	 * Get fields that touch the specified corner with the following order:
	 * <p>
	 * For Northern corner (i = 0): {NorthEast, South, NorthWest}
	 * <p>
	 * For Southern corner (i = 1): {North, SouthEast, SouthWest}.
	 *
	 * @param aX
	 *            Corner axial x-coordinate
	 * @param aY
	 *            Corner axial y-coordinate
	 * @param dir
	 *            Corner index/direction (0 = North, 1 = South)
	 * @return the touching fields
	 */

	public Field[] getTouchingFields(int aX, int aY, int dir) {
		Field[] touchedFields;
		if (dir == 0) {
			Field ne = getFieldAt(aX + 1, aY - 1);
			Field s = getFieldAt(aX, aY);
			Field nw = getFieldAt(aX, aY - 1);
			touchedFields = new Field[] { ne, s, nw };
		} else if (dir == 1) {
			Field n = getFieldAt(aX, aY);
			Field se = getFieldAt(aX, aY + 1);
			Field sw = getFieldAt(aX - 1, aY + 1);
			touchedFields = new Field[] { n, se, sw };
		} else {
			return null;
		}
		return touchedFields;
	}

	/**
	 * Get fields that are connected to the specified edge with the following
	 * order:
	 * <p>
	 * For NorthWestern edge (i = 0): {NorthWest, SouthEast}
	 * <p>
	 * For NorthEastern edge (i = 1): {NorthEast, SouthWest}
	 * <p>
	 * for Eastern edge (i = 2): {West, East}.
	 *
	 * @param aX
	 *            Edge axial x-coordinate
	 * @param aY
	 *            Edge axial y-coordinate
	 * @param dir
	 *            Edge index/direction (0 = NorthWest, 1 = NorthEast, 2 = East)
	 * @return the connected fields
	 */

	public Field[] getConnectedFields(int aX, int aY, int dir) {
		Field[] connectedFields;
		if (dir == 0) {
			Field nw = getFieldAt(aX, aY - 1);
			Field se = getFieldAt(aX, aY);
			connectedFields = new Field[] { nw, se };
		} else if (dir == 1) {
			Field ne = getFieldAt(aX + 1, aY - 1);
			Field sw = getFieldAt(aX, aY);
			connectedFields = new Field[] { ne, sw };
		} else if (dir == 2) {
			Field w = getFieldAt(aX, aY);
			Field e = getFieldAt(aX + 1, aY);
			connectedFields = new Field[] { w, e };
		} else {
			return null;
		}
		return connectedFields;
	}

	// ================================================================================
	// HEXAGONAL RELATIONS (TO CORNER)
	// ================================================================================

	/**
	 * Get all surrounding corners of a field with the following order:
	 * <p>
	 * <p>
	 * {North, NorthEast, SouthEast, South, SouthWest, NorthWest}.
	 *
	 * @param aX
	 *            Field axial x-coordinate
	 * @param aY
	 *            Field axial y-coordinate
	 * @return the surrounding corners
	 */

	public Corner[] getSurroundingCorners(int aX, int aY) {
		Corner n = getCornerAt(aX, aY, 0);
		Corner ne = getCornerAt(aX + 1, aY - 1, 1);
		Corner se = getCornerAt(aX, aY + 1, 0);
		Corner s = getCornerAt(aX, aY, 1);
		Corner sw = getCornerAt(aX - 1, aY + 1, 0);
		Corner nw = getCornerAt(aX, aY - 1, 1);
		Corner[] surroundingCorners = { n, ne, se, s, sw, nw };
		return surroundingCorners;
	}

	/**
	 * Get corners that are adjacent to the specified corner with the following
	 * order:
	 * <p>
	 * For Northern corner (i = 0): {North, SouthEast, SouthWest}
	 * <p>
	 * For Southern corner (i = 1): {NorthEast, South, NorthWest}.
	 *
	 * @param aX
	 *            Corner axial x-coordinate
	 * @param aY
	 *            Corner axial y-coordinate
	 * @param dir
	 *            Corner index/direction (0 = North, 1 = South)
	 * @return the adjacent corners
	 */

	public Corner[] getAdjacentCorners(int aX, int aY, int dir) {

		Corner[] adjacentCorners;

		if (dir == 0) {

			Corner n = getCornerAt(aX + 1, aY - 2, 1);
			Corner se = getCornerAt(aX + 1, aY - 1, 1);
			Corner sw = getCornerAt(aX, aY - 1, 1);

			adjacentCorners = new Corner[] { n, se, sw };

		} else if (dir == 1) {

			Corner ne = getCornerAt(aX, aY + 1, 0);
			Corner s = getCornerAt(aX - 1, aY + 2, 0);
			Corner nw = getCornerAt(aX - 1, aY + 1, 0);

			adjacentCorners = new Corner[] { ne, s, nw };

		} else {

			return null;
		}

		return adjacentCorners;
	}

	/**
	 * Get corners that are attached to the specified edge with the following
	 * order:
	 * <p>
	 * For NorthWestern edge (i = 0): {NorthEast, SouthWest}
	 * <p>
	 * For NorthEastern edge (i = 1): {NorthWest, SouthEast}
	 * <p>
	 * for Eastern edge (i = 2): {North, South}.
	 *
	 * @param aX
	 *            Edge axial x-coordinate
	 * @param aY
	 *            Edge axial y-coordinate
	 * @param dir
	 *            Edge index/direction (0 = NorthWest, 1 = NorthEast, 2 = East)
	 * @return the attached corners
	 */

	public Corner[] getAttachedCorners(int aX, int aY, int dir) {

		Corner[] attachedCorners;

		if (dir == 0) {

			Corner ne = getCornerAt(aX, aY, 0);
			Corner sw = getCornerAt(aX, aY - 1, 1);

			attachedCorners = new Corner[] { ne, sw };

		} else if (dir == 1) {

			Corner nw = getCornerAt(aX, aY, 0);
			Corner se = getCornerAt(aX + 1, aY - 1, 1);

			attachedCorners = new Corner[] { nw, se };

		} else if (dir == 2) {

			Corner n = getCornerAt(aX + 1, aY - 1, 1);
			Corner s = getCornerAt(aX, aY + 1, 0);

			attachedCorners = new Corner[] { n, s };

		} else {

			return null;
		}

		return attachedCorners;
	}

	// ================================================================================
	// HEXAGONAL RELATIONS (TO EDGE)
	// ================================================================================

	/**
	 * Get bordering edges of a field with the following order:
	 * <p>
	 * <p>
	 * {NorthEast, East, SouthEast, SouthWest, West, NorthWest}.
	 *
	 * @param aX
	 *            Field axial x-coordinate
	 * @param aY
	 *            Field axial y-coordinate
	 * @return the bordering edges
	 */

	public Edge[] getBorderingEdges(int aX, int aY) {
		Edge ne = getEdgeAt(aX, aY, 1);
		Edge e = getEdgeAt(aX, aY, 2);
		Edge se = getEdgeAt(aX, aY + 1, 0);
		Edge sw = getEdgeAt(aX - 1, aY + 1, 1);
		Edge w = getEdgeAt(aX - 1, aY, 2);
		Edge nw = getEdgeAt(aX, aY, 0);
		Edge[] borders = { ne, e, se, sw, w, nw };
		return borders;
	}

	/**
	 * Get edges that are projected to the specified corner with the following
	 * order:
	 * <p>
	 * For Northern corner (i = 0): {North, SouthEast, SouthWest}
	 * <p>
	 * For Southern corner (i = 1): {NorthEast, South, NorthWest}.
	 *
	 * @param aX
	 *            Corner axial x-coordinate
	 * @param aY
	 *            Corner axial y-coordinate
	 * @param dir
	 *            Corner index/direction (0 = North, 1 = South)
	 * @return the projecting edges
	 */

	public Edge[] getProjectingEdges(int aX, int aY, int dir) {
		Edge[] projectedEdges;
		if (dir == 0) {
			Edge n = getEdgeAt(aX, aY - 1, 2);
			Edge se = getEdgeAt(aX, aY, 1);
			Edge sw = getEdgeAt(aX, aY, 0);
			projectedEdges = new Edge[] { n, se, sw };
		} else if (dir == 1) {
			Edge ne = getEdgeAt(aX, aY + 1, 0);
			Edge s = getEdgeAt(aX - 1, aY + 1, 2);
			Edge nw = getEdgeAt(aX - 1, aY + 1, 1);
			projectedEdges = new Edge[] { ne, s, nw };
		} else {
			return null;
		}
		return projectedEdges;
	}

	/**
	 * Get edges that are linked to the specified edge with the following order:
	 * <p>
	 * For NorthWestern edge (i = 0): {North, SouthEast, South, NorthWest}
	 * <p>
	 * For NorthEastern edge (i = 1): {North, NorthEast, South, SouthWest}
	 * <p>
	 * for Eastern edge (i = 2): {NorthEast, SouthEast, SouthWest, NorthWest}.
	 *
	 * @param aX
	 *            Edge axial x-coordinate
	 * @param aY
	 *            Edge axial y-coordinate
	 * @param dir
	 *            Edge index/direction (0 = NorthWest, 1 = NorthEast, 2 = East)
	 * @return the linked edges
	 */

	public Edge[] getLinkedEdges(int aX, int aY, int dir) {
		Edge[] linkedEdges;
		if (dir == 0) {
			Edge n = getEdgeAt(aX, aY - 1, 2);
			Edge se = getEdgeAt(aX, aY, 1);
			Edge s = getEdgeAt(aX - 1, aY, 2);
			Edge nw = getEdgeAt(aX - 1, aY, 1);
			linkedEdges = new Edge[] { n, se, s, nw };
		} else if (dir == 1) {
			Edge n = getEdgeAt(aX, aY - 1, 2);
			Edge ne = getEdgeAt(aX + 1, aY, 0);
			Edge s = getEdgeAt(aX, aY, 2);
			Edge sw = getEdgeAt(aX, aY, 0);
			linkedEdges = new Edge[] { n, ne, s, sw };
		} else if (dir == 2) {
			Edge ne = getEdgeAt(aX + 1, aY, 0);
			Edge se = getEdgeAt(aX, aY + 1, 1);
			Edge sw = getEdgeAt(aX, aY + 1, 0);
			Edge nw = getEdgeAt(aX, aY, 1);
			linkedEdges = new Edge[] { ne, se, sw, nw };
		} else {
			return null;
		}
		return linkedEdges;
	}

	// ================================================================================
	// HASHMAP RELATED
	// ================================================================================

	/**
	 * Gets the field coordinates.
	 *
	 * @param f
	 *            the f
	 * @return the field coordinates
	 */
	public int[] getFieldCoordinates(String f) {

		// filter bad input
		if (f.length() == 1) {
			return stringToCoordMap.get(f);
		}
		// wrong id
		return null;
	}

	/**
	 * Get sequence of sea field ID's.
	 *
	 * @return "ABCDEFGHIJKLMNOPQR"
	 */
	public String getOuterRing() {
		StringBuffer result = new StringBuffer();
		for (String key : stringToCoordMap.keySet()) {
			if (key.matches("[a-z]")) {
				result.append(key);
			}
		}
		return result.toString();
	}

	/**
	 * Get sequence of non-sea field IDs.
	 *
	 * @return "ABCDEFGHIJKLMNOPQRS"
	 */
	public String getInnerFields() {
		StringBuffer result = new StringBuffer();
		for (String key : stringToCoordMap.keySet()) {
			if (key.matches("[A-^]")) {
				result.append(key);
			}
		}
		return result.toString();

	}

	/**
	 * Gets the direction.
	 *
	 * @param f
	 *            the f
	 * @return the direction
	 */
	@Deprecated
	// MOVED TO HEXSERVICE
	public int getDirection(String f) {

		int x = getFieldCoordinates(f)[0];
		int y = getFieldCoordinates(f)[1];
		int sum = x + y;
		if (x < 0 && y <= 0 && sum < 0) {
			return 0;
		}
		if (x >= 0 && y < 0 && sum < 0) {
			return 1;
		}
		if (x > 0 && y < 0 && sum >= 0) {
			return 2;
		}
		if (x > 0 && y >= 0 && sum > 0) {
			return 3;
		}
		if (x <= 0 && y > 0 && sum > 0) {
			return 4;
		}
		if (x < 0 && y > 0 && sum <= 0) {
			return 5;
		}

		logger.error("Error in Board.getDirektion");
		return 0;
	}

	// ================================================================================
	// VIEW
	// ================================================================================

	/**
	 * Gets the all fields.
	 *
	 * @return the all fields
	 */
	// DEBUGGING
	@Deprecated
	public ArrayList<Field> getAllFields() {
		ArrayList<Field> result = new ArrayList<Field>();
		for (int i = 0; i < fields.length; i++) {
			for (int j = 0; j < fields[i].length; j++) {
				if (fields[i][j] != null) {
					if (fields[i][j].getFieldID() != null) {
						result.add(fields[i][j]);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Intended to check which of the two fields is sea, not intended for two
	 * land fields.
	 * <p>
	 * // * @param u Field 1 x-coordinate // * @param v Field 1 y-coordinate
	 * //* @param x Field 2 x-coordinate //* @param y Field 2 y-coordinate
	 *
	 * @param field1
	 *            the field 1
	 * @param field2
	 *            the field 2
	 * @return the water Field
	 */

	private int[] getHarbourField(int[] field1, int[] field2) {
		if (field1.length == 2 && field2.length == 2) {
			if (getFieldAt(field1[0], field1[1]).getResourceType() == enums.ResourceType.SEA) {
				if (getFieldAt(field2[0], field2[1]).getResourceType() == enums.ResourceType.SEA) {
					logger.warn("Throw new Illegal Argument Exception\"Both fields are sea\"");
					throw new IllegalArgumentException("Both fields are sea");
				} else
					return field1;
			} else if (getFieldAt(field2[0], field2[1]).getResourceType() == enums.ResourceType.SEA) {
				return field2;
			} else
				logger.warn("Throw new Illegal Argument Exception\"None are SEAT\"");
			throw new IllegalArgumentException("None are SEAT");
		}
		logger.warn("Throw new Illegal Argument Exception\"False input\"");
		throw new IllegalArgumentException("False input");
	}

	/**
	 * Get Sea Field coordinates through two corners.
	 *
	 * @param corner1
	 *            {i,j,k}
	 * @param corner2
	 *            {i,j,k}
	 * @return the harbour middlepoint
	 */
	public int[] getHarbourMiddlepoint(int[] corner1, int[] corner2) {
		if (corner1.length == 3 && corner2.length == 3) {
			Field[] tF1 = getTouchingFields(corner1[0], corner1[1], corner1[2]);
			Field[] tF2 = getTouchingFields(corner2[0], corner2[1], corner2[2]);
			String ID1 = null;
			String ID2 = null;
			int count = 0;
			for (int i = 0; i < tF1.length; i++) {
				for (int j = 0; j < tF2.length; j++) {
					// if the IDs match
					if (tF1[i].getFieldID().equals(tF2[j].getFieldID())) {
						// if its the first one
						if (count == 0) {
							ID1 = tF1[i].getFieldID();
							count++;
						} else if (count == 1) {
							ID2 = tF1[i].getFieldID();
						} else {
							logger.warn("Throw new Illegal Argument Exception\"Corners don 't connect\"");
							throw new IllegalArgumentException("Corners don't connect");
						}

					}

				}
			}
			int[] firstField = getFieldCoordinates(ID1);
			int[] secondField = getFieldCoordinates(ID2);
			return getHarbourField(firstField, secondField);
		} else {
			logger.warn("Throw new Illegal Argument Exception\"False input\"");
			throw new IllegalArgumentException("False input");
		}

	}

	/**
	 * Sets the harbour corner.
	 *
	 * @param harbourCorners
	 *            the new harbour corner
	 */
	public void setHarbourCorner(Corner[] harbourCorners) {
		this.harbourCorners = harbourCorners;
	}

	/**
	 * Gets the harbour corners.
	 *
	 * @return the harbour corners
	 */
	public Corner[] getHarbourCorners() {
		return this.harbourCorners;
	}

	/**
	 * For ai.
	 */
	public void deletePlayers() {
		players = null;
	}

	/**
	 * Insert players.
	 *
	 * @param n
	 *            the n
	 */
	public void insertPlayers(int n) {
		players = new PlayerModel[n];
		for (int i = 0; i < players.length; i++) {
			players[i] = new PlayerModel();
		}
	}

	public void extendPlayers(PlayerModel[] oldPM) {
		for(int i=0; i<oldPM.length; i++){
			players[i] = oldPM[i];
		}
		
	}

}
