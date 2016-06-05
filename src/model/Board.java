package model;

import settings.DefaultSettings;
import java.util.ArrayList;

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
	private String bandit;
	// TODO private DevDeck devDeck;

	public Board(ArrayList<PlayerModel> tempPlayers) {
		int r = DefaultSettings.BOARD_SIZE;
		fields = new Field[r][r];
		initializeFields(fields);
		corners = new Corner[r][r][2];
		initializeCorners(corners);
		edges = new Edge[r][r][3];
		initializeEdges(edges);
		players = new PlayerModel[tempPlayers.size()]; // 0 = noPlayer; 1 =
														// player1...
		initializePlayers(tempPlayers);
		// TODO players
		// TODO bandit
	}

	private void initializePlayers(ArrayList<PlayerModel> tempPlayers) {
		for (int i = 0; i < tempPlayers.size(); i++) {
			players[i] = tempPlayers.get(i);
		}

	}

	/**
	 * Initialize used cells axial in the 2d array, unused cells remain null.
	 *
	 * @param fields
	 */
	private void initializeFields(Field[][] fields) {
		int aX; // axial x coordinate
		int aY; // axial y coordinate
		int absoluteValue;
		for (int x = 0; x < fields.length; x++) {
			for (int y = 0; y < fields[0].length; y++) {
				aX = x - DefaultSettings.BOARD_SIZE / 2;
				aY = y - DefaultSettings.BOARD_SIZE / 2;
				absoluteValue = Math.abs(aX + aY);
				if (absoluteValue <= DefaultSettings.BOARD_SIZE / 2) {
					fields[x][y] = new Field();
				}
			}
		}

		// TODO: better solution

		getFieldAt(0, -2).setFieldID("A");
		getFieldAt(1, -2).setFieldID("B");
		getFieldAt(2, -2).setFieldID("C");
		getFieldAt(-1, -1).setFieldID("D");
		getFieldAt(0, -1).setFieldID("E");
		getFieldAt(1, -1).setFieldID("F");
		getFieldAt(2, -1).setFieldID("G");
		getFieldAt(-2, 0).setFieldID("H");
		getFieldAt(-1, 0).setFieldID("I");
		getFieldAt(0, 0).setFieldID("J");
		getFieldAt(1, 0).setFieldID("K");
		getFieldAt(2, 0).setFieldID("L");
		getFieldAt(-2, 1).setFieldID("M");
		getFieldAt(-1, 1).setFieldID("N");
		getFieldAt(0, 1).setFieldID("O");
		getFieldAt(1, 1).setFieldID("P");
		getFieldAt(-2, 2).setFieldID("Q");
		getFieldAt(-1, 2).setFieldID("R");
		getFieldAt(0, 2).setFieldID("S");

		// water

	}

	/**
	 * Initialize the corners.
	 *
	 * @param corners
	 */
	private void initializeCorners(Corner[][][] corners) {
		for (int x = 0; x < fields.length; x++) {
			for (int y = 0; y < fields[0].length; y++) {
				if (fields[x][y] != null) {
					corners[x][y][0] = new Corner(); // North
					corners[x][y][1] = new Corner(); // South
				}
			}
			// TODO: better solution
			filterUnusedCorners();
		}
	}

	/**
	 * Set unused corners to null
	 */
	private void filterUnusedCorners() {

		for (int i = 0; i < DefaultSettings.BOARD_SIZE; i++) {

		}

		// row 0
		corners[3][0][0] = null;
		corners[4][0][0] = null;
		corners[5][0][0] = null;
		corners[6][0][0] = null;
		// row 1
		corners[2][1][0] = null;
		corners[6][1][0] = null;
		// row 2
		corners[1][2][0] = null;
		corners[6][2][0] = null;
		// row 3
		corners[0][3][0] = null;
		corners[0][3][1] = null;
		corners[6][3][0] = null;
		corners[6][3][1] = null;
		// row 4
		corners[0][4][1] = null;
		corners[5][4][1] = null;
		// row 5
		corners[0][5][1] = null;
		corners[4][5][1] = null;
		// row 6
		corners[0][6][1] = null;
		corners[1][6][1] = null;
		corners[2][6][1] = null;
		corners[3][6][1] = null;
	}

	/**
	 * Initialize the edges.
	 *
	 * @param edges
	 */
	private void initializeEdges(Edge[][][] edges) {
		for (int x = 0; x < fields.length; x++) {
			for (int y = 0; y < fields[0].length; y++) {
				if (fields[x][y] != null) {
					edges[x][y][0] = new Edge(); // Northwest
					edges[x][y][1] = new Edge(); // Northeast
					edges[x][y][2] = new Edge(); // East
				}
			}
		}
		// TODO: better solution
		filterUnusedEdges();
	}

	/**
	 * Set unused edges to null.
	 */
	private void filterUnusedEdges() {
		// row 0
		edges[3][0][0] = null;
		edges[3][0][1] = null;
		edges[3][0][2] = null;

		edges[4][0][0] = null;
		edges[4][0][1] = null;
		edges[4][0][2] = null;

		edges[5][0][0] = null;
		edges[5][0][1] = null;
		edges[5][0][2] = null;

		edges[6][0][0] = null;
		edges[6][0][1] = null;
		edges[6][0][2] = null;

		// row 1
		edges[2][1][0] = null;
		edges[2][1][1] = null;

		edges[6][1][0] = null;
		edges[6][1][1] = null;
		edges[6][1][2] = null;

		// row 2
		edges[1][2][0] = null;
		edges[1][2][1] = null;

		edges[6][2][0] = null;
		edges[6][2][1] = null;
		edges[6][2][2] = null;

		// row 3
		edges[0][3][0] = null;
		edges[0][3][1] = null;

		edges[6][3][0] = null;
		edges[6][3][1] = null;
		edges[6][3][2] = null;

		// row 4
		edges[0][4][0] = null;

		edges[5][4][1] = null;
		edges[5][4][2] = null;

		// row 5
		edges[0][5][0] = null;

		edges[4][5][1] = null;
		edges[4][5][2] = null;

		// row 6
		edges[0][6][0] = null;
		edges[0][6][2] = null;

		edges[1][6][2] = null;

		edges[2][6][2] = null;

		edges[3][6][1] = null;
		edges[3][6][2] = null;

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
		if (aX < -DefaultSettings.BOARD_SIZE / 2 || aX > DefaultSettings.BOARD_SIZE / 2
				|| aY < -DefaultSettings.BOARD_SIZE / 2 || aY > DefaultSettings.BOARD_SIZE / 2) {
			return null;
		} else {
			return this.fields[aX + DefaultSettings.BOARD_SIZE / 2][aY + DefaultSettings.BOARD_SIZE / 2];
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
		if (aX < -DefaultSettings.BOARD_SIZE / 2 || aX > DefaultSettings.BOARD_SIZE / 2
				|| aY < -DefaultSettings.BOARD_SIZE / 2 || aX > DefaultSettings.BOARD_SIZE / 2 || dir < 0 || dir > 1) {
			return null;
		} else {
			return this.corners[aX + DefaultSettings.BOARD_SIZE / 2][aY + DefaultSettings.BOARD_SIZE / 2][dir];
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
		if (aX < -DefaultSettings.BOARD_SIZE / 2 || aX > DefaultSettings.BOARD_SIZE / 2
				|| aY < -DefaultSettings.BOARD_SIZE / 2 || aX > DefaultSettings.BOARD_SIZE / 2 || dir < 0 || dir > 2) {
			return null;
		} else {
			return this.edges[aX + DefaultSettings.BOARD_SIZE / 2][aY + DefaultSettings.BOARD_SIZE / 2][dir];
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

	public int[] getFieldCoordinates(Field f) {
		String field_id = f.getFieldID();

		// filter bad input
		if (field_id.length() == 1) {
			// water coordinates
			if (field_id.matches("[a-r]")) {
				switch (field_id) {
				case "a":
					return new int[] { 0, -3 };
				case "b":
					return new int[] { 1, -3 };
				case "c":
					return new int[] { 2, -3 };
				case "d":
					return new int[] { 3, -3 };
				case "e":
					return new int[] { -1, -2 };
				case "f":
					return new int[] { 3, -2 };
				case "g":
					return new int[] { -2, -1 };
				case "h":
					return new int[] { 3, -1 };
				case "i":
					return new int[] { -3, 0 };
				case "j":
					return new int[] { 3, 0 };
				case "k":
					return new int[] { -3, 1 };
				case "l":
					return new int[] { 2, 1 };
				case "m":
					return new int[] { -3, 2 };
				case "n":
					return new int[] { 1, 2 };
				case "o":
					return new int[] { -3, 3 };
				case "p":
					return new int[] { -2, 3 };
				case "q":
					return new int[] { -1, 3 };
				case "r":
					return new int[] { 0, 3 };
				}
			}
			// field coordinates
			else if (field_id.matches("[A-S]")) {
				switch (field_id) {
				case "A":
					return new int[] { 0, -2 };
				case "B":
					return new int[] { 1, -2 };
				case "C":
					return new int[] { 2, -2 };
				case "D":
					return new int[] { -1, -1 };
				case "E":
					return new int[] { 0, -1 };
				case "F":
					return new int[] { 1, -1 };
				case "G":
					return new int[] { 2, -1 };
				case "H":
					return new int[] { -2, 0 };
				case "I":
					return new int[] { -1, 0 };
				case "J":
					return new int[] { 0, 0 };
				case "K":
					return new int[] { 1, 0 };
				case "L":
					return new int[] { 2, 0 };
				case "M":
					return new int[] { -2, 1 };
				case "N":
					return new int[] { -1, 1 };
				case "O":
					return new int[] { 0, 1 };
				case "P":
					return new int[] { 1, 1 };
				case "Q":
					return new int[] { -2, 2 };
				case "R":
					return new int[] { -1, 2 };
				case "S":
					return new int[] { 0, 2 };
				}
			}
		}
		// wrong id
		return null;
	}

	public ArrayList<Field> getSpiral(Field f) {
		Field firstField = f;
		Field nextField = f;
		Field plannedNextField = f;
		int r = ((DefaultSettings.BOARD_SIZE - 1) / 2) - 1;
		ArrayList<Field> result = new ArrayList<Field>();
		int dir = getDirektion(f);
		int[] coord = new int[2];

		for (int length = r; length > 0; length--) {
			for (int j = 0; j < 7; j++) {
				for (int i = 0; i < length; i++) {
					if (nextField != null) {
						switch (j) {
						case 0:
							if (getRing(length).contains(plannedNextField)) {
								nextField = plannedNextField;
								result.add(nextField);
								// sysoutArrayList(result); //debug
								coord[0] = getFieldCoordinates(nextField)[0];
								coord[1] = getFieldCoordinates(nextField)[1];
								plannedNextField = getNextField(coord[0], coord[1], getDirektion(nextField));
							} else {
								i = r;
							}
							break;
						case 6:
							if (plannedNextField != firstField) {
								nextField = plannedNextField;
								result.add(nextField);
								// sysoutArrayList(result); //debug
								coord[0] = getFieldCoordinates(nextField)[0];
								coord[1] = getFieldCoordinates(nextField)[1];
								plannedNextField = getNextField(coord[0], coord[1], getDirektion(nextField));
							} else {
								coord[0] = getFieldCoordinates(nextField)[0];
								coord[1] = getFieldCoordinates(nextField)[1];
								i = r;
								j = 7;
								plannedNextField = getNextField(coord[0], coord[1],
										nextDir(getDirektion(nextField), 0));
								if (result.contains(plannedNextField)) {
									plannedNextField = getNextField(coord[0], coord[1],
											nextDir(getDirektion(nextField), 1));
								}
							}
							break;
						default:
							nextField = plannedNextField;
							result.add(nextField);
							// sysoutArrayList(result); //debug
							coord[0] = getFieldCoordinates(nextField)[0];
							coord[1] = getFieldCoordinates(nextField)[1];
							plannedNextField = getNextField(coord[0], coord[1], getDirektion(nextField));
							break;
						}
					}
				}

			}
			firstField = nextField;
		}
		result.add(getFieldAt(0, 0));
		return result;
	}

	// TODO remove debug Method
	public void sysoutArrayList(ArrayList<Field> a) {
		String s = "";
		for (int i = 0; i < a.size(); i++) {
			s = s + a.get(i).getFieldID();
		}
		System.out.println(s);
	}

	public int nextDir(int dir, int i) {
		int nextDir = (dir + i) % 6;
		switch (nextDir) {
		case 0:
			return 1;
		case 1:
			return 2;
		case 2:
			return 3;
		case 3:
			return 4;
		case 4:
			return 5;
		case 5:
			return 0;
		default:
			System.out.println("Error in board.nextDir.");
			return 0;
		}
	}

	public int getDirektion(Field f) {
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

	public ArrayList<Field> getRing(int rad) {
		ArrayList<Field> result = new ArrayList<Field>();
		int[] coord = new int[2];
		Field nextField = getFieldAt(-2, 1);
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < rad; j++) {
				result.add(nextField);
				coord[0] = getFieldCoordinates(nextField)[0];
				coord[1] = getFieldCoordinates(nextField)[1];
				nextField = getNextField(coord[0], coord[1], getDirektion(nextField));
			}
		}
		return result;
	}

	public Field getNextField(int aX, int aY, int dir) {
		Field[] result = getNeighbouringFields(aX, aY); // To Do field to
														// coordinates
		switch (dir) {
		case 0:
			return result[0];
		case 1:
			return result[1];
		case 2:
			return result[2];
		case 3:
			return result[3];
		case 4:
			return result[4];
		case 5:
			return result[5];
		default:
			return null;
		}
	}

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
	 * Converts Axial (x,y) to Cube (x,y,z)
	 *
	 * @param a
	 *            (x,y)
	 */

	public int[] convertAxialToCube(int[] a) {

		if (a.length != 2) {
			// TODO: logging
			System.out.println("Unable to convert: " + a.toString() + " to cube.");
			return null;

		} else {

			int x = a[0];
			int z = a[1];
			int y = -x - z;

			return new int[] { x, y, z };
		}
	}

	/**
	 * Converts Cube (x,y,z) to Axial (x,y)
	 *
	 * @param c
	 *            (x,y,z)
	 */

	public int[] convertCubeToAxial(int[] c) {
		if (c.length != 3) {
			// TODO: logging
			System.out.println("Unable to convert: " + c.toString() + " to axial.");
			return null;
		} else {
			int x = c[0];
			int y = c[2];
			return new int[] { x, y };
		}
	}

	/**
	 * Returns fields array
	 */

	public Field[][] getFields() {
		return this.fields;
	}

	/**
	 * Returns corners array
	 */

	public Corner[][][] getCorners() {
		return this.corners;
	}

	/**
	 * Returns edges array
	 */

	public Edge[][][] getEdges() {
		return this.edges;
	}

	/**
	 * Returns PlayerModel array
	 */

	public PlayerModel[] getPlayerModels() {
		return this.players;
	}

	/**
	 * Returns bandit.
	 */

	public String getBandit() {
		return this.bandit;
	}

	/**
	 * Sets bandit
	 */

	public void setBandit(String f) {
		this.bandit = f;

	}

}
