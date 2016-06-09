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
import settings.DefaultSettings;

/**
 * Contains all Board methods, retrieving fields, edges and corners.
 *
 * @author Adam
 */
public class Board {
	private Field[][] fields;
	private Corner[][][] corners;
	private Edge[][][] edges;
	private PlayerModel[] players;
	private String banditLocation;
	private static Map<String, int[]> stringToCoordMap;
	private static Map<Index, String> coordToStringMap;
	// TODO private DevDeck devDeck;

	public Board(ArrayList<PlayerModel> tempPlayers) {
		int r = DefaultSettings.BOARD_SIZE;
		fields = new Field[r][r];
		initializeHashMaps();
		initializeFields();
		corners = new Corner[r][r][2];
		initializeCorners();
		edges = new Edge[r][r][3];
		initializeEdges();
		players = new PlayerModel[DefaultSettings.MAXIMUM_PLAYERS_AMOUNT];
		initializePlayers(tempPlayers);
		initializeBandit();
	}

	public Board() {
		int r = DefaultSettings.BOARD_SIZE;
		fields = new Field[r][r];
		initializeFields();
		initializeHashMaps();
		corners = new Corner[r][r][2];
		initializeCorners();
		edges = new Edge[r][r][3];
		initializeEdges();
		players = new PlayerModel[DefaultSettings.MAXIMUM_PLAYERS_AMOUNT];
		initializeBandit();
	}

	/**
	 * Initialize used cells axial in the 2d array, unused cells remain null.
	 *
	 * @param fields
	 */
	private void initializeFields() {
		int aX; // axial x coordinate
		int aY; // axial y coordinate
		int absoluteValue;
		int radius = DefaultSettings.BOARD_RADIUS;
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
	 * systematically assigns IDs to each field by it's coordinate
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
		// temp to make code look clearer
		int radius = DefaultSettings.BOARD_RADIUS;

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
	 *
	 * @param corners
	 */
	private void initializeCorners() {
		// temporary variables to make code appear clearer
		int[] temp;
		int radius = DefaultSettings.BOARD_RADIUS;
		// Go through HashMap<String,int[]>
		for (Map.Entry<String, int[]> entry : stringToCoordMap.entrySet()) {
			// entry of key
			temp = entry.getValue();
			// lowercase keys only (outer ring)
			if (entry.getKey().matches("[a-z]")) {
				// North only will be set
				if (HexService.sumOfCubeXY(HexService.convertAxialToCube(temp)) < 0) {
					// convert axial back to array coords
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
				// North and south are set
				corners[temp[0] + radius][temp[1] + radius][0] = new Corner();
				corners[temp[0] + radius][temp[1] + radius][1] = new Corner();
			}

		}
	}

	/**
	 * Initialize the edges systematically;
	 *
	 * @param edges
	 */
	/*
	 * NW = north west, NE = north east, E = east
	 */
	private void initializeEdges() {
		// temporary variables to make code appear clearer
		int[] temp;
		int radius = DefaultSettings.BOARD_RADIUS;
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

	private void initializePlayers(ArrayList<PlayerModel> tempPlayers) {
		for (int i = 0; i < tempPlayers.size(); i++) {
			players[i] = tempPlayers.get(i);
		}
	}

	private void initializeBandit() {
		// TODO Auto-generated method stub

	}

	/**
	 * Returns field given axial coordinates, if field doesn't exist then null
	 *
	 * @param aX
	 *            Field axial x-coordinate
	 * @param aY
	 *            Field axial y-coordinate
	 */

	public Field getFieldAt(int aX, int aY) {
		// temp to make code clearer
		int radius = DefaultSettings.BOARD_RADIUS;
		// if out of range
		if (aX < -radius || aX > radius || aY < -radius || aY > radius) {
			return null;
		} else {
			return fields[aX + radius][aY + radius];
		}
	}

	/**
	 * Returns corner given axial coordinates and index(direction), if corner
	 * doesn't exist then null
	 *
	 * @param aX
	 *            Corner axial x-coordinate
	 * @param aY
	 *            Corner axial y-coordinate
	 * @param dir
	 *            Corner direction (0 = North, 1 = South)
	 */

	public Corner getCornerAt(int aX, int aY, int dir) {
		// temp to make code clearer
		int radius = DefaultSettings.BOARD_RADIUS;
		// if out of range
		if (aX < -radius || aX > radius || aY < -radius || aY > radius || dir < 0 || dir > 1) {
			return null;
		} else {
			return this.corners[aX + radius][aY + radius][dir];
		}
	}

	/**
	 * Returns edge given axial coordinates and index(direction), if edge
	 * doesn't exist then null
	 *
	 * @param aX
	 *            Edge axial x-coordinate
	 * @param aY
	 *            Edge axial y-coordinate
	 * @param dir
	 *            Edge index/direction (0 = NorthWest, 1 = NorthEast, 2 = East)
	 */

	public Edge getEdgeAt(int aX, int aY, int dir) {
		// temp to make code clearer
		int radius = DefaultSettings.BOARD_RADIUS;
		// if out of range
		if (aX < -radius || aX > radius || aY < -radius || aY > radius || dir < 0 || dir > 2) {
			return null;
		} else {
			return this.edges[aX + radius][aY + radius][dir];
		}
	}

	/**
	 * Get neighbouring fields of the specified field with the following order:
	 * <p>
	 * {NorthEast, East, SouthEast, SouthWest, West, NorthWest}
	 *
	 * @param aX
	 *            Field axial x-coordinate
	 * @param aY
	 *            Field axial y-coordinate
	 *
	 */

	public static String getNeighbouringFields(int aX, int aY) {
		Index ne = new Index(aX + 1, aY - 1);
		Index e = new Index(aX + 1, aY);
		Index se = new Index(aX, aY + 1);
		Index sw = new Index(aX - 1, aY + 1);
		Index w = new Index(aX - 1, aY);
		Index nw = new Index(aX, aY - 1);
//		Field[] neighbours = { ne, e, se, sw, w, nw };

		String northEast = getCoordToStringMap().get(ne);
		String east = getCoordToStringMap().get(e);
		String southEast = getCoordToStringMap().get(se);
		String southWest = getCoordToStringMap().get(sw);
		String west = getCoordToStringMap().get(w);
		String northWest = getCoordToStringMap().get(nw);


		String neighbours = northEast+east+southEast+southWest+west+northWest;
		return neighbours;
	}

	/**
	 *
	 * Get fields that touch the specified corner with the following order:
	 * <p>
	 * For Northern corner (i = 0): {NorthEast, South, NorthWest}
	 * <p>
	 * For Southern corner (i = 1): {North, SouthEast, SouthWest}
	 *
	 * @param aX
	 *            Corner axial x-coordinate
	 * @param aY
	 *            Corner axial y-coordinate
	 * @param dir
	 *            Corner index/direction (0 = North, 1 = South)
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
	 *
	 * Get fields that are connected to the specified edge with the following
	 * order:
	 * <p>
	 * For NorthWestern edge (i = 0): {NorthWest, SouthEast}
	 * <p>
	 * For NorthEastern edge (i = 1): {NorthEast, SouthWest}
	 * <p>
	 * for Eastern edge (i = 2): {West, East}
	 *
	 * @param aX
	 *            Edge axial x-coordinate
	 * @param aY
	 *            Edge axial y-coordinate
	 * @param dir
	 *            Edge index/direction (0 = NorthWest, 1 = NorthEast, 2 = East)
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

	/**
	 * Get all surrounding corners of a field with the following order:
	 * <p>
	 *
	 * {North, NorthEast, SouthEast, South, SouthWest, NorthWest}
	 *
	 * @param aX
	 *            Field axial x-coordinate
	 * @param aY
	 *            Field axial y-coordinate
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
	 *
	 * Get corners that are adjacent to the specified corner with the following
	 * order:
	 * <p>
	 * For Northern corner (i = 0): {North, SouthEast, SouthWest}
	 * <p>
	 * For Southern corner (i = 1): {NorthEast, South, NorthWest}
	 *
	 * @param aX
	 *            Corner axial x-coordinate
	 * @param aY
	 *            Corner axial y-coordinate
	 * @param dir
	 *            Corner index/direction (0 = North, 1 = South)
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
	 *
	 * Get corners that are attached to the specified edge with the following
	 * order:
	 * <p>
	 * For NorthWestern edge (i = 0): {NorthEast, SouthWest}
	 * <p>
	 * For NorthEastern edge (i = 1): {NorthWest, SouthEast}
	 * <p>
	 * for Eastern edge (i = 2): {North, South}
	 *
	 * @param aX
	 *            Edge axial x-coordinate
	 * @param aY
	 *            Edge axial y-coordinate
	 * @param dir
	 *            Edge index/direction (0 = NorthWest, 1 = NorthEast, 2 = East)
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

	/**
	 * Get bordering edges of a field with the following order:
	 *
	 * <p>
	 * {NorthEast, East, SouthEast, SouthWest, West, NorthWest}
	 *
	 * @param aX
	 *            Field axial x-coordinate
	 * @param aY
	 *            Field axial y-coordinate
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
	 *
	 * Get edges that are projected to the specified corner with the following
	 * order:
	 * <p>
	 * For Northern corner (i = 0): {North, SouthEast, SouthWest}
	 * <p>
	 * For Southern corner (i = 1): {NorthEast, South, NorthWest}
	 *
	 * @param aX
	 *            Corner axial x-coordinate
	 * @param aY
	 *            Corner axial y-coordinate
	 * @param dir
	 *            Corner index/direction (0 = North, 1 = South)
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
	 *
	 * Get edges that are linked to the specified edge with the following order:
	 * <p>
	 * For NorthWestern edge (i = 0): {North, SouthEast, South, NorthWest}
	 * <p>
	 * For NorthEastern edge (i = 1): {North, NorthEast, South, SouthWest}
	 * <p>
	 * for Eastern edge (i = 2): {NorthEast, SouthEast, SouthWest, NorthWest}
	 *
	 * @param aX
	 *            Edge axial x-coordinate
	 * @param aY
	 *            Edge axial y-coordinate
	 * @param dir
	 *            Edge index/direction (0 = NorthWest, 1 = NorthEast, 2 = East)
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

	public int[] getFieldCoordinates(String f) {

		// filter bad input
		if (f.length() == 1) {
			return stringToCoordMap.get(f);
		}
		// wrong id
		return null;
	}

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

		System.out.println("Error in Board.getDirektion");
		return 0;
	}

//	public String getNextField(int aX, int aY, int dir) {
//		char[] result = getNeighbouringFields(aX, aY).toCharArray(); // To Do field to
//														// coordinates
//		switch (dir) {
//		case 0:
//			return result[0];
//		case 1:
//			return result[1];
//		case 2:
//			return result[2];
//		case 3:
//			return result[3];
//		case 4:
//			return result[4];
//		case 5:
//			return result[5];
//		default:
//			return null;
//		}
//	}

	// TODO FOR DEBUGGING ONLY
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
	 * Get outer ring
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

	public String getInnerFields() {
		StringBuffer result = new StringBuffer();
		for (String key : stringToCoordMap.keySet()) {
			if (key.matches("[A-Z]")) {
				result.append(key);
			}
		}
		return result.toString();

	}

	/**
	 * Returns fields by normal array index
	 */

	public Field getField(int x, int y) {
		return this.fields[x][y];
	}

	/**
	 * Returns corners by normal array index and direction
	 */

	public Corner getCorner(int x, int y, int dir) {
		return this.corners[x][y][dir];
	}

	/**
	 * Returns edges array by normal array index and direction
	 */

	public Edge getEdge(int x, int y, int dir) {
		return this.edges[x][y][dir];
	}

	/**
	 * Returns a Player object
	 */

	public PlayerModel getPlayer(int i) {
		return this.players[i] != null ? this.players[i] : null;
	}

	/**
	 * Returns the amount of players
	 */
	public int getAmountPlayers() {
		return this.players.length;
	}

	/**
	 * Returns bandit.
	 */

	public String getBandit() {
		return this.banditLocation;
	}

	/**
	 * Sets bandit
	 */

	public void setBandit(String banditLocation) {
		this.banditLocation = banditLocation;

	}

	/**
	 * @return the fieldIDMap
	 */
	public static Map<String, int[]> getStringToCoordMap() {
		return stringToCoordMap;
	}

	public static Map<Index, String> getCoordToStringMap() {
		return coordToStringMap;
	}

	public void setField(int i, int j, ResourceType resourceType, Integer diceIndex) {
		fields[i][j].setResourceType(resourceType);
		fields[i][j].setDiceIndex(diceIndex);

	}

	public void setEdge(int i, int j, int k, boolean hasStreet, int ownedByPlayer) {
		edges[i][j][k].setHasStreet(hasStreet);
		edges[i][j][k].setOwnedByPlayer(ownedByPlayer);

	}

	public void setCorner(int i, int j, int k, CornerStatus status, HarbourStatus hstatus, int ownerID) {
		corners[i][j][k].setHarbourStatus(hstatus);
		corners[i][j][k].setStatus(status);
		corners[i][j][k].setOwnerID(ownerID);

	}

}
