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
		// TODO bandit (find desert through HexServices and place it on top of
		// it)

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
		for (int y = 0; y < fields.length; y++) {
			for (int x = 0; x < fields[0].length; x++) {

				offsetY = -3 + y; // TODO soft code
				offsetX = -3 + x; // TODO soft code

				absoluteValue = Math.abs(offsetY + offsetX);

				if (absoluteValue != 3) // TODO soft code
					fields[y][x] = new Field();

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
		for (int y = 0; y < fields.length; y++) {
			for (int x = 0; x < fields[0].length; x++) {
				if (fields[y][x] != null) {
					corners[y][x][0] = new Corner(); // North
					corners[y][x][1] = new Corner(); // South
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
		for (int y = 0; y < fields.length; y++) {
			for (int x = 0; x < fields[0].length; x++) {
				if (fields[y][x] != null) {
					edges[y][x][0] = new Edge(); // Northwest
					edges[y][x][1] = new Edge(); // Northeast
					edges[y][x][2] = new Edge(); // East

				}
			}
		}
	}

	/**
	 *
	 */
	@Override
	public Field getFieldAt(int offsetX, int offsetY) {
		return this.fields[offsetY + DefaultSettings.BOARD_SIZE / 2][offsetX + DefaultSettings.BOARD_SIZE / 2];
	}

	/**
	 *
	 */

	@Override
	public Corner getCornerAt(int offsetX, int offsetY, int i) {
		return this.corners[offsetY + DefaultSettings.BOARD_SIZE / 2][offsetX + DefaultSettings.BOARD_SIZE / 2][i];
	}

	/**
	 *
	 */

	@Override
	public Edge getEdgeAt(int offsetX, int offsetY, int i) {
		return this.edges[offsetY + DefaultSettings.BOARD_SIZE / 2][offsetX + DefaultSettings.BOARD_SIZE / 2][i];
	}

	/**
	 *
	 */

	@Override
	public Field[] getSurroundingF(Field f) {
		// TODO Auto-generated method stub
		// NorthWest Field nw;//=getFieldAt(offsetX)(offsetY - 1);

		// NorthEast Field ne;// =getFieldAt(offsetX + 1)(offsetY - 1);

		// East Field e;// =getFieldAt(offsetX - 1)(offsetY);

		// West Field w;// =getFieldAt(offsetX + 1)(offsetY);

		// SouthWest Field sw;// =getFieldAt(offsetX - 1)(offsetY + 1)

		// SouthEast Field se;// =getFieldAt(offsetX)(offsetY + 1); Field[]
		// neighbours = null; // = {nw, ne, e, w, sw, se}; TODO 12 Uhr //
		// uhrzeiger

		// return neighbours;
		return null;
	}

	/**
	 *
	 */

	@Override
	public Field[] getSurroundingF(Corner c) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *
	 */

	@Override
	public Field[] getSurroundingF(Edge e) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *
	 */

	@Override
	public Corner[] getSurroundingC(Field f) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *
	 */

	@Override
	public Corner[] getSurroundingC(Corner c) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 *
	 */

	@Override
	public Corner[] getSurroundingC(Edge e) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 *
	 */

	@Override
	public Edge[] getSurroundingE(Field f) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 *
	 */

	@Override
	public Edge[] getSurroundingE(Corner c) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 *
	 */

	@Override
	public Edge[] getSurroundingE(Edge e) {
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
	public Field[][][] getFields() {
		// TODO Auto-generated method stub
		return null;
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
		return null;
	}



	@Override
	public void setBandit(Field f) {
		this.bandit=f;

	}

}
