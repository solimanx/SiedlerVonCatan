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
	 * Offset x and y coordinates by mid point and set unused cells to null.
	 *
	 * @param fields
	 *
	 */
	private void initializeFields(Field[][] fields) {
		int offsetX;
		int offsetY;
		int absoluteValue;
		for (int x = 0; x < fields.length; x++) {
			for (int y = 0; y < fields[0].length; y++) {

				offsetX = x - DefaultSettings.BOARD_SIZE;
				offsetY = y - DefaultSettings.BOARD_SIZE;
				absoluteValue = Math.abs(offsetY + offsetX);

				if (absoluteValue != DefaultSettings.BOARD_SIZE)
					fields[x][y] = new Field();

			}
		}

	}

	/**
	 * Sync the x and y coordinates of field with corners , and attach cardinal
	 * directions to them. Null fields are neglected.
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
		}

	}

	/**
	 * Sync the x and y coordinates of field with edges, and attach cardinal
	 * directions to them. Null fields are neglected.
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
	}

	@Override
	public Field getFieldAt(int offsetX, int offsetY) {
		if (offsetX + DefaultSettings.BOARD_SIZE / 2 < 0 || offsetY + DefaultSettings.BOARD_SIZE / 2 < 0)
			return null;
		else
			return this.fields[offsetX + DefaultSettings.BOARD_SIZE / 2][offsetY + DefaultSettings.BOARD_SIZE / 2];
	}

	@Override
	public Corner getCornerAt(int offsetX, int offsetY, int i) {
		if (offsetX + DefaultSettings.BOARD_SIZE / 2 < 0 || offsetY + DefaultSettings.BOARD_SIZE / 2 < 0)
			return null;
		else
			return this.corners[offsetX + DefaultSettings.BOARD_SIZE / 2][offsetY + DefaultSettings.BOARD_SIZE / 2][i];
	}

	@Override
	public Edge getEdgeAt(int offsetX, int offsetY, int i) {
		if (offsetX + DefaultSettings.BOARD_SIZE / 2 < 0 || offsetY + DefaultSettings.BOARD_SIZE / 2 < 0)
			return null;
		else
			return this.edges[offsetX + DefaultSettings.BOARD_SIZE / 2][offsetY + DefaultSettings.BOARD_SIZE / 2][i];
	}

	// Refer to relationships.pdf

	/**
	 * Get neighbouring fields of a field with the following order:
	 * <p>
	 * {NorthEast, East, SouthEast, SouthWest, West, NorthWest}
	 */

	@Override
	public Field[] getNeighbouringFields(int offsetX, int offsetY) {

		Field ne = getFieldAt(offsetX + 1, offsetY - 1);
		Field e = getFieldAt(offsetX + 1, offsetY);
		Field se = getFieldAt(offsetX, offsetY + 1);
		Field sw = getFieldAt(offsetX - 1, offsetY + 1);
		Field w = getFieldAt(offsetX - 1, offsetY);
		Field nw = getFieldAt(offsetX, offsetY - 1);

		Field[] neighbours = { ne, e, se, sw, w, nw };

		return neighbours;
	}

	/**
	 *
	 */

	@Override
	public Field[] getTouchingFields(int offsetX, int offsetY, int i) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *
	 */

	@Override
	public Field[] getConnectedFields(int offsetX, int offsetY, int i) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Get all corners of a field with the following order:
	 * <p>
	 * {North, NorthEast, SouthEast, South, SouthWest, NorthWest}
	 */

	@Override
	public Corner[] getSurroundingCorners(int offsetX, int offsetY) {
		Corner n = getCornerAt(offsetX, offsetY, 0);
		Corner ne = getCornerAt(offsetX + 1, offsetY - 1, 1);
		Corner se = getCornerAt(offsetX, offsetY + 1, 0);
		Corner s = getCornerAt(offsetX, offsetY, 1);
		Corner sw = getCornerAt(offsetX - 1, offsetY + 1, 0);
		Corner nw = getCornerAt(offsetX, offsetY - 1, 1);
		Corner[] surrCorners = { n, ne, se, s, sw, nw };
		return surrCorners;
	}

	/**
	 *
	 */

	@Override
	public Corner[] getAdjacentCorners(int offsetX, int offsetY, int i) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *
	 */

	@Override
	public Corner[] getAttachedCorners(int offsetX, int offsetY, int i) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Get bordering edges of a field with the following order:
	 * <p>
	 * {NorthEast, East, SouthEast, SouthWest, West, NorthWest}
	 */

	@Override
	public Edge[] getBorderingEdges(int offsetX, int offsetY) {
		Edge ne = getEdgeAt(offsetX, offsetY, 1);
		Edge e = getEdgeAt(offsetX, offsetY, 2);
		Edge se = getEdgeAt(offsetX, offsetY - 1, 0);
		Edge sw = getEdgeAt(offsetX - 1, offsetY + 1, 1);
		Edge w = getEdgeAt(offsetX - 1, offsetY, 2);
		Edge nw = getEdgeAt(offsetX, offsetY, 0);

		Edge[] borders = { ne, e, se, sw, w, nw };
		return borders;
	}

	/**
	 *
	 */

	@Override
	public Edge[] getProjectingEdges(int offsetX, int offsetY, int i) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *
	 */

	@Override
	public Edge[] getLinkedEdges(int offsetX, int offsetY, int i) {
		// TODO Auto-generated method stub
		return null;
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

}
