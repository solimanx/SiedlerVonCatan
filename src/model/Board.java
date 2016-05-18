package model;

import java.util.Observable;

import settings.DefaultSettings;

public class Board extends Observable implements BoardInterface {

	private Field[][] fields;
	private Corner[][][] corners;
	private Edge[][][] edges;

	private PlayerModel[] players;

	private Field bandit;
	// TODO private DevDeck devDeck;

	private static Board instance;

	private Board() {

		int r = DefaultSettings.BOARD_SIZE;
		fields = new Field[r][r];
		initializeFields(fields);

		corners = new Corner[r][r][2];
		initializeCorners(corners);

		edges = new Edge[r][r][3];
		initializeEdges(edges);

		// TODO players
		// TODO bandit

	}

	public static synchronized Board getInstance() {
		if (Board.instance == null) {
			Board.instance = new Board();
		}
		return Board.instance;
	}

	/**
	 *
	 * Initialize used cells axial in the 2d array, unused cells remain null.
	 *
	 * @param fields
	 *
	 */

	private void initializeFields(Field[][] fields) {

		int aX; // axial x coordinate
		int aY; // axial y coordinate
		int absoluteValue;

		for (int x = 0; x < fields.length; x++) {
			for (int y = 0; y < fields[0].length; y++) {

				aX = x - DefaultSettings.BOARD_SIZE / 2;
				aY = y - DefaultSettings.BOARD_SIZE / 2;

				absoluteValue = Math.abs(aY + aX);

				if (absoluteValue <= DefaultSettings.BOARD_SIZE / 2) {
					fields[x][y] = new Field();
				}
			}
		}
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
					// TODO unused corners / undefined edges
				}
			}
		}
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
					// TODO unused edges / undefined edges

				}
			}
		}
	}

	/**
	 * Returns field given axial coordinates, if field doesn't exist then null
	 * 
	 * @param aX
	 *            Field axial x-coordinate
	 * @param aY
	 *            Field axial y-coordinate
	 */

	@Override
	public Field getFieldAt(int aX, int aY) {
		try {

			return this.fields[aX + DefaultSettings.BOARD_SIZE / 2][aY + DefaultSettings.BOARD_SIZE / 2];

		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO: logging
			System.out.println("Field doesn't exist " + e);
			return null;
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
	 * @param i
	 *            Corner index/direction (0 = North, 1 = South)
	 */

	@Override
	public Corner getCornerAt(int aX, int aY, int i) {
		try {

			return this.corners[aX + DefaultSettings.BOARD_SIZE / 2][aY + DefaultSettings.BOARD_SIZE / 2][i];

		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO: logging
			System.out.println("Corner doesn't exist " + e);
			return null;
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
	 * @param i
	 *            Edge index/direction (0 = NorthWest, 1 = NorthEast, 2 = East)
	 */

	@Override
	public Edge getEdgeAt(int aX, int aY, int i) {
		try {

			return this.edges[aX + DefaultSettings.BOARD_SIZE / 2][aY + DefaultSettings.BOARD_SIZE / 2][i];

		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO: logging
			System.out.println("Edge array is out of bounds " + e);
			return null;
		}
	}

	// Refer to hexagonrelations.png in trello for this part
	/**
	 * Get neighbouring fields of the specified field with the following order:
	 * <p>
	 * {NorthEast, East, SouthEast, SouthWest, West, NorthWest}
	 * 
	 * @param aX
	 *            Field axial x-coordinate
	 * @param aY
	 *            Field axial y-coordinate
	 */

	@Override
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
	 * @param i
	 *            Corner index/direction (0 = North, 1 = South)
	 */

	@Override
	public Field[] getTouchingFields(int aX, int aY, int i) {

		Field[] touchedFields;

		if (i == 0) {

			Field ne = getFieldAt(aX + 1, aY - 1);
			Field s = getFieldAt(aX, aY);
			Field nw = getFieldAt(aX, aY - 1);

			touchedFields = new Field[] { ne, s, nw };

		} else if (i == 1) {

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
	 * @param i
	 *            Edge index/direction (0 = NorthWest, 1 = NorthEast, 2 = East)
	 */

	@Override
	public Field[] getConnectedFields(int aX, int aY, int i) {

		Field[] connectedFields;

		if (i == 0) {

			Field nw = getFieldAt(aX, aY - 1);
			Field se = getFieldAt(aX, aY);

			connectedFields = new Field[] { nw, se };

		} else if (i == 1) {

			Field ne = getFieldAt(aX + 1, aY - 1);
			Field sw = getFieldAt(aX, aY);

			connectedFields = new Field[] { ne, sw };

		} else if (i == 2) {

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

	@Override
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
	 * @param i
	 *            Corner index/direction (0 = North, 1 = South)
	 */
	
	@Override
	public Corner[] getAdjacentCorners(int aX, int aY, int i) {

		Corner[] adjacentCorners;

		if (i == 0) {

			Corner n = getCornerAt(aX + 1, aY - 2, 1);
			Corner se = getCornerAt(aX + 1, aY - 1, 1);
			Corner sw = getCornerAt(aX, aY - 1, 1);

			adjacentCorners = new Corner[] { n, se, sw };

		} else if (i == 1) {

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
	 * @param i
	 *            Edge index/direction (0 = NorthWest, 1 = NorthEast, 2 = East)
	 */

	@Override
	public Corner[] getAttachedCorners(int aX, int aY, int i) {

		Corner[] attachedCorners;

		if (i == 0) {

			Corner ne = getCornerAt(aX, aY, 0);
			Corner sw = getCornerAt(aX, aY - 1, 1);

			attachedCorners = new Corner[] { ne, sw };

		} else if (i == 1) {

			Corner nw = getCornerAt(aX, aY, 0);
			Corner se = getCornerAt(aX + 1, aY - 1, 1);

			attachedCorners = new Corner[] { nw, se };

		} else if (i == 2) {

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
	
	@Override
	public Edge[] getBorderingEdges(int aX, int aY) {

		Edge ne = getEdgeAt(aX, aY, 1);
		Edge e = getEdgeAt(aX, aY, 2);
		Edge se = getEdgeAt(aX, aY - 1, 0);
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
	 * @param i
	 *            Corner index/direction (0 = North, 1 = South)
	 */

	@Override
	public Edge[] getProjectingEdges(int aX, int aY, int i) {
		Edge[] projectedEdges;

		if (i == 0) {

			Edge n = getEdgeAt(aX, aY - 1, 2);
			Edge se = getEdgeAt(aX, aY, 1);
			Edge sw = getEdgeAt(aX, aY, 0);

			projectedEdges = new Edge[] { n, se, sw };

		} else if (i == 1) {

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
	 * @param i
	 *            Edge index/direction (0 = NorthWest, 1 = NorthEast, 2 = East)
	 */

	@Override
	public Edge[] getLinkedEdges(int aX, int aY, int i) {

		Edge[] linkedEdges;

		if (i == 0) {

			Edge n = getEdgeAt(aX, aY - 1, 2);
			Edge se = getEdgeAt(aX, aY, 1);
			Edge s = getEdgeAt(aX - 1, aY, 2);
			Edge nw = getEdgeAt(aX - 1, aY, 1);

			linkedEdges = new Edge[] { n, se, s, nw };

		} else if (i == 1) {

			Edge n = getEdgeAt(aX, aY - 1, 2);
			Edge ne = getEdgeAt(aX + 1, aY, 0);
			Edge s = getEdgeAt(aX, aY, 2);
			Edge sw = getEdgeAt(aX, aY, 0);

			linkedEdges = new Edge[] { n, ne, s, sw };

		} else if (i == 2) {

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

	/**
	 *
	 */

	@Override
	public int[] convertToCube(int[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *
	 */

	@Override
	public int[] convertToAxial(int[] c) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *
	 */

	@Override
	public Field[] getSpiral(Field f) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *
	 */

	@Override
	public Field[][] getFields() {
		return this.fields;
	}

	@Override
	public Corner[][][] getCorners() {
		return this.corners;
	}

	@Override
	public Edge[][][] getEdges() {
		return this.edges;
	}

	@Override
	public PlayerModel[] getPlayerModels() {
		return this.players;
	}

	@Override
	public Field getBandit() {
		return this.bandit;
	}

	@Override
	public void setBandit(Field f) {
		this.bandit = f;

	}
	
	//TODO test the relation methods

}
