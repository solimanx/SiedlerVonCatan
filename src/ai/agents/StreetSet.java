package ai.agents;

import java.util.ArrayList;

import model.objects.Edge;

public class StreetSet {
	private int playerID;
	private ArrayList<Edge> edges;
	private boolean hasCircle;

	public StreetSet(int playerID, ArrayList<Edge> edges) {
		this.playerID = playerID;
		this.edges = edges;
		hasCircle = false;

	}

	protected void addEdge(Edge e) {
		edges.add(e);
	}

	protected int getPlayerID() {
		return playerID;
	}

	protected ArrayList<Edge> getEdges() {
		return edges;
	}

	protected boolean getHasCircle() {
		return hasCircle;
	}

	protected void setHasCircle(boolean flag) {
		hasCircle = flag;
	}

	protected Edge getEdgeAt(int index) {
		return edges.get(index);
	}

	protected int size() {
		if (edges != null) {
			return edges.size();
		}
		return 0;
	}

}
