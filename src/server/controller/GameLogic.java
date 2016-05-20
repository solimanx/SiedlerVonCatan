package server.controller;

/**
 * @author 
 * Korbinian
 */
import java.util.ArrayList;

import model.Board;
import model.Corner;
import model.Edge;
import model.Field;
import model.PlayerModel;
import enums.ResourceType;


public class GameLogic implements client.controller.GameLogicInterface {
	Board board;

	public GameLogic(Board b) {
		this.board = b;
	}
	
	/**
	 * Checks if the player can build a city at the given position
	 * @param x
	 * @param y
	 * @param dir
	 * @param player
	 * @return boolean true/false
	 */
	
	public boolean checkBuildVillage(int x, int y, int dir, PlayerModel player) {
		if (player.getAmountVillages() <= 0) {
			return false; // no Village left to build
		}
		int[] resources = getPlayerResources(player);
		for (int i = 0; i < 5; i++) {
			if (resources[i] < settings.DefaultSettings.VILLAGE_BUILD_COST[i]) {
				return false; // not enough resources
			}
		}
		Corner c = board.getCornerAt(x, y, dir);
		if (c != null){ //valid corner
		if (c.getStatus() == enums.CornerStatus.EMPTY) { // is the Corner Empty?
			Edge[] e = board.getProjectingEdges(x,y,dir);
			for (int i = 0; i < e.length; i++) {
				if (e[i].getOwnedByPlayer() == player) { // is there an adjusting street
												// with correct player
					return true;
				}
			}
		}
		}
		return false;
	}

	/**
	 * Checks if the player can build a city at the given position
	 * @param x
	 * @param y
	 * @param dir
	 * @param player
	 * @return boolean true/false
	 */
	public boolean checkBuildCity(int x, int y, int dir, PlayerModel player) {
		if (player.getAmountCities() <= 0) {
			return false; // no Cities left to build
		}
		int[] resources = getPlayerResources(player);
		for (int i = 0; i < 5; i++) {
			if (resources[i] < settings.DefaultSettings.CITY_BUILD_COST[i]) {
				return false; // not enough resources
			}
		}
		Corner c = board.getCornerAt(x, y, dir);
		if (c != null){
		if (c.getStatus() == enums.CornerStatus.VILLAGE && c.getOwnedByPlayer() == player) { 
			Edge[] e = board.getProjectingEdges(x,y,dir);
			for (int i = 0; i < e.length; i++) {
				if (e[i].getOwnedByPlayer() == player) { // is there an adjusting streetof this player?
 					return true;
				}
			}
		}
		}
		return false;
	}

	/**
	 * Checks if the player can build a Street at the given position
	 * @param x
	 * @param y
	 * @param dir
	 * @param player
	 * @return boolean true/false
	 */
	public boolean checkBuildStreet(int x, int y, int dir, PlayerModel player) {
		if (player.getAmountStreets() <= 0) { // has this Player a Street left to build?
			return false;
		}
		int[] resources = getPlayerResources(player);
		for (int i = 0; i < 5; i++) {
			if (resources[i] < settings.DefaultSettings.STREET_BUILD_COST[i]) {
				return false;
			}
		}
		Edge e = board.getEdgeAt(x,y,dir);
		if (e != null){  //valid edge
		if (e.isHasStreet() == false) {
			Edge[] neighbors = board.getLinkedEdges(x,y,dir);
			for (int i = 0; i <= neighbors.length; i++) {
				if (neighbors[i].getOwnedByPlayer() == player) {
					return true;
				}
			}
		}
		}
		return false;
	}
	
	/**
	 * checks if bandit can be set at specified position
	 * @param x
	 * @param y
	 * @return true/false
	 */
	public boolean checkSetBandit(int x, int y){
		Field f = board.getFieldAt(x, y);
		if (f != null && f != board.getBandit()){ //valid position and not the same as before
			return true;
		}
		
		return false;
	}

	/**
	 * Gets an array wich contains the player resources in form of
	 * (AmountWood,AmountClay...)
	 * 
	 * @param player
	 * @return resource Array
	 */
	private int[] getPlayerResources(PlayerModel player) {
		ArrayList<ResourceType> resList = player.getResourceCards();
		int[] result = new int[5];
		enums.ResourceType resType;
		for (int i = 0; i < resList.size(); i++) {
			resType = resList.get(i);
			switch (resType) {
			// Build costs: {WOOD, CLAY, ORE, SHEEP, CORN}
			case WOOD:
				result[0]++;
			case CLAY:
				result[1]++;
			case ORE:
				result[2]++;
			case SHEEP:
				result[3]++;
			case CORN:
				result[4]++;
			default:
			}
		}
		return result;
	}

	@Override
	public boolean canTrade() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canPlayCard() {
		// TODO Auto-generated method stub
		return false;
	}

}
