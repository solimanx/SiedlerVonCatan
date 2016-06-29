package ai.agents;

import java.util.ArrayList;

import model.objects.Edge;

// TODO: Auto-generated Javadoc
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
	 * Adds the edge.
	 *
	 * @param e the e
	 */
	protected void addEdge(Edge e) {
		edges.add(e);
	}

	/**
	 * Gets the player ID.
	 *
	 * @return the player ID
	 */
	protected int getPlayerID() {
		return playerID;
	}

	/**
	 * Gets the edges.
	 *
	 * @return the edges
	 */
	protected ArrayList<Edge> getEdges() {
		return edges;
	}

	/**
	 * Gets the checks for circle.
	 *
	 * @return the checks for circle
	 */
	protected boolean getHasCircle() {
		return hasCircle;
	}

	/**
	 * Sets the checks for circle.
	 *
	 * @param flag the new checks for circle
	 */
	protected void setHasCircle(boolean flag) {
		hasCircle = flag;
	}

	/**
	 * Gets the edge at.
	 *
	 * @param index the index
	 * @return the edge at
	 */
	protected Edge getEdgeAt(int index) {
		return edges.get(index);
	}

	/**
	 * Size.
	 *
	 * @return the int
	 */
	protected int size() {
		if (edges != null) {
			return edges.size();
		}
		return 0;
	}

}
