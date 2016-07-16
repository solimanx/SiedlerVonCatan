package model;

import java.util.ArrayList;

import model.objects.Edge;

public class StreetSet {
	private int playerID;
	private ArrayList<Edge> edges;
	private boolean hasCircle;

	/**
	 * Instantiates a new street set.
	 *
	 * @param playerID the player ID
	 * @param edges the edges
	 */
	public StreetSet(int playerID, ArrayList<Edge> edges) {
		this.playerID = playerID;
		this.edges = edges;
		hasCircle = false;

	}

	/**
	 * Adds the edge to the list.
	 *
	 * @param e the e
	 */
	public void addEdge(Edge e) {
		edges.add(e);
	}

	/**
	 * Gets the player ID.
	 *
	 * @return the player ID
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * Gets the edges.
	 *
	 * @return the edges
	 */
	public ArrayList<Edge> getEdges() {
		return edges;
	}

	/**
	 * Checks if the edges make a circle.
	 *
	 * @return the checks for circle
	 */
	public boolean getHasCircle() {
		return hasCircle;
	}

	/**
	 * Sets that there is a circle made by the edges.
	 *
	 * @param flag the new checks for circle
	 */
	public void setHasCircle(boolean flag) {
		hasCircle = flag;
	}

	/**
	 * Gets the edge at the given index.
	 *
	 * @param index the index
	 * @return the edge at
	 */
	public Edge getEdgeAt(int index) {
		return edges.get(index);
	}

	/**
	 * Get size of edge.
	 *
	 * @return the int
	 */
	public int size() {
		if (edges != null) {
			return edges.size();
		}
		return 0;
	}

}
