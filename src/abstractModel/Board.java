package abstractModel;

import javafx.beans.Observable;

public abstract class Board implements Observable {

	private Edge[] edges;
	private Corner[] corners;
	private Field[] fields;
	//private PlayerModel[] players;
	private Field bandit;
	//private DevDeck devDeck;
	
	
	
	
	
	public Edge[] getEdges() {
		return edges;
	}





	public void setEdges(Edge[] edges) {
		this.edges = edges;
	}





	public Corner[] getCorners() {
		return corners;
	}





	public void setCorners(Corner[] corners) {
		this.corners = corners;
	}





	public Field[] getFields() {
		return fields;
	}





	public void setFields(Field[] fields) {
		this.fields = fields;
	}





	public Field getBandit() {
		return bandit;
	}





	public void setBandit(Field bandit) {
		this.bandit = bandit;
	}





	/**
	 * @author mattmoos
	 * Auxiliary class for navigating hexagonal game board
	 */
	private class HexServices{
		
	}
	
    
}
