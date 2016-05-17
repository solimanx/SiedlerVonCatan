package model;

import java.util.Observable;

import enums.CardinalDirection;
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
	// TODO bandit (find desert through HexServices and place it on top of it)

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
	int absoluteValue;
	for (int y = 0; y < fields.length; y++) {
	    for (int x = 0; x < fields[0].length; x++) {
		fields[y][x].setOffsetY(-3 + y); // TODO soft code
		fields[y][x].setOffsetX(-3 + x); // TODO soft code

		absoluteValue = Math.abs(fields[y][x].getOffsetY() + fields[y][x].getOffsetX());

		if (absoluteValue > 3) // TODO soft code
		    fields[y][x] = null;

	    }
	}

    }

    /**
     * Sync the offset x and y coordinates of field with corners , and attach
     * cardinal directions to them. Null fields are neglected.
     * 
     * @param corners
     */
    private void initializeCorners(Corner[][][] corners) {
	for (int y = 0; y < fields.length; y++) {
	    for (int x = 0; x < fields[0].length; x++) {
		if (fields[y][x] != null) {
		    corners[y][x][0].setOffsetY(fields[y][x].getOffsetY());
		    corners[y][x][0].setOffsetX(fields[y][x].getOffsetX());
		    corners[y][x][0].setDirection(CardinalDirection.NORTH);

		    corners[y][x][1].setOffsetY(fields[y][x].getOffsetY());
		    corners[y][x][1].setOffsetX(fields[y][x].getOffsetX());
		    corners[y][x][1].setDirection(CardinalDirection.SOUTH);
		}
	    }
	}

    }

    /**
     * Sync the offset x and y coordinates of field with edges, and attach
     * cardinal directions to them. Null fields are neglected.
     * 
     * @param edges
     */
    private void initializeEdges(Edge[][][] edges) {
	for (int y = 0; y < fields.length; y++) {
	    for (int x = 0; x < fields[0].length; x++) {
		if (fields[y][x] != null) {
		    edges[y][x][0].setOffsetY(fields[y][x].getOffsetY());
		    edges[y][x][0].setOffsetX(fields[y][x].getOffsetX());
		    edges[y][x][0].setDirection(CardinalDirection.NORTH_WEST);

		    edges[y][x][1].setOffsetY(fields[y][x].getOffsetY());
		    edges[y][x][1].setOffsetX(fields[y][x].getOffsetX());
		    edges[y][x][1].setDirection(CardinalDirection.NORTH_EAST);

		    edges[y][x][2].setOffsetY(fields[y][x].getOffsetY());
		    edges[y][x][2].setOffsetX(fields[y][x].getOffsetX());
		    edges[y][x][2].setDirection(CardinalDirection.EAST);

		}
	    }
	}
    }

    /**
     * @author mattmoos Auxiliary class for navigating hexagonal game board
     */
    private class HexServices {

    }

}
