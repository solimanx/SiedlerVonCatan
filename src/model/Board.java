package model;

import java.util.Observable;

import settings.DefaultSettings;

public class Board extends Observable {

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

	public Edge[][][] getEdges() {
		return edges;
	}

	public void setEdges(Edge[][][] edges) {
		this.edges = edges;
	}

	public Corner[][][] getCorners() {
		return corners;
	}

	public void setCorners(Corner[][][] corners) {
		this.corners = corners;
	}

	public Field[][] getFields() {
		return fields;
	}

	public void setFields(Field[][] fields) {
		this.fields = fields;
	}

	public PlayerModel[] getPlayers() {
		return players;
	}

	public void setPlayers(PlayerModel[] players) {
		this.players = players;
	}

	public Field getBandit() {
		return bandit;
	}

	public void setBandit(Field bandit) {
		this.bandit = bandit;
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
					corners[y][x][0] = new Corner(); //North
					corners[y][x][1] = new Corner(); //South
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
					edges[y][x][0] = new Edge(); //Northwest
					edges[y][x][1] = new Edge(); //Northeast
					edges[y][x][2] = new Edge(); //East

				}
			}
		}
	}

	/**
	 * @author mattmoos Auxiliary class for navigating hexagonal game board
	 */
	private class HexServices {

		// Field, Corner, Edge getters
		public Field getFieldAt(int offsetX, int offsetY) {
			/*
			 * TODO if (offsetX<4 && offsetX >-4 && offsetY<4 && offsetY >-4) {
			 * int y = offsetY + 3; int x = offsetX + 3; return fields[y][x]; }
			 * else return null;
			 */

			return null;
		}

		public Corner getCornerAt(int offsetX, int offsetY, int i) {
			// TODO similar to getFieldAt
			return null;
		}

		public Edge getEdgeAt(int offsetX, int offsetY, int i) {
			// TODO similar to getFieldAt
			return null;
		}

		public Field[] getNeighboursOfFieldAt(int offsetX, int offsetY) {
			// TODO adapt with hexservice
			// NorthWest
			Field nw;// =getFieldAt(offsetX)(offsetY - 1);

			// NorthEast
			Field ne;// =getFieldAt(offsetX + 1)(offsetY - 1);

			// East
			Field e;// =getFieldAt(offsetX - 1)(offsetY);

			// West
			Field w;// =getFieldAt(offsetX + 1)(offsetY);

			// SouthWest
			Field sw;// =getFieldAt(offsetX - 1)(offsetY + 1)

			// SouthEast
			Field se;// =getFieldAt(offsetX)(offsetY + 1);
			Field[] neighbours = null; // = {nw, ne, e, w, sw, se}; TODO 12 Uhr uhrzeiger

			return neighbours;
		}

	}

}
