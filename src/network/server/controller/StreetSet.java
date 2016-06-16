package network.server.controller;

import java.util.ArrayList;

import model.objects.Edge;

public class StreetSet {
	private int playerID;
	private ArrayList<Edge> edges;
	
	
	public StreetSet(int playerID, ArrayList<Edge> edges) {
		this.playerID = playerID;
		this.edges = edges;
	}
	
	protected int getPlayerID() {
		return playerID;
	}
	
	protected ArrayList<Edge> getEdges() {
		return edges;
	}


}
