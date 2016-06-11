package model;

import java.util.ArrayList;

import enums.PlayerState;
import enums.ResourceType;
import model.objects.Corner;
import model.objects.Edge;
import model.objects.Field;

/**
 * @author Implements rules of the game
 */
public class GameLogic {
	private Board board;

	public GameLogic(Board b) {
		this.board = b;
	}

	/**
	 * Checks if the player can build a city at the given position
	 * 
	 * @param x
	 * @param y
	 * @param dir
	 * @param player
	 * @return boolean true/false
	 */

	public boolean checkBuildVillage(int x, int y, int dir, int playerID) {
		if (board.getPlayer(playerID).getAmountVillages() <= 0) {
			return false; // no Village left to build
		}
		if (checkPlayerResources(playerID, settings.DefaultSettings.VILLAGE_BUILD_COST) == false) {
			return false;
		}
		/*
		 * int[] resources = getPlayerResources(playerId); for (int i = 0; i <
		 * 5; i++) { if (resources[i] <
		 * settings.DefaultSettings.VILLAGE_BUILD_COST[i]) { return false; //
		 * not enough resources } }
		 */
		Corner c = board.getCornerAt(x, y, dir);
		if (c != null) { // valid corner
			if (c.getStatus() == enums.CornerStatus.EMPTY) { // is the Corner
																// Empty?
				Edge[] e = board.getProjectingEdges(x, y, dir);
				for (int i = 0; i < e.length; i++) {
					if (e[i] != null) {
						if (e[i].getOwnerID() == playerID) { // is
																// there
																// an
																// adjusting
																// street
							// with correct player
							return true;

						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks if the player can build a city at the given position
	 * 
	 * @param x
	 * @param y
	 * @param dir
	 * @param player
	 * @return boolean true/false
	 */
	public boolean checkBuildCity(int x, int y, int dir, int playerID) {
		if (board.getPlayer(playerID).getAmountCities() <= 0) {
			System.out.println("no cities left");
			return false; // no Cities left to build
		}
		if (checkPlayerResources(playerID, settings.DefaultSettings.CITY_BUILD_COST) == false) {
			return false;
		}

		Corner c = board.getCornerAt(x, y, dir);
		if (c != null) {
			if (c.getStatus() == enums.CornerStatus.VILLAGE && c.getOwnerID() == playerID) {
				Edge[] e = board.getProjectingEdges(x, y, dir);
				for (int i = 0; i < e.length; i++) {
					if (e[i] != null) {
						if (e[i].getOwnerID() == playerID) {
							return true;
						}
					}
				}

			}
		}
		return false;

	}

	/**
	 * Checks if the player can build a Street at the given position
	 * 
	 * @param x
	 * @param y
	 * @param dir
	 * @param player
	 * @return boolean true/false
	 */
	public boolean checkBuildStreet(int x, int y, int dir, int playerID) {
		if (board.getPlayer(playerID).getAmountStreets() <= 0) { // has this
																	// Player
																	// a Street
																	// left
																	// to build?
			return false;
		}
		if (checkPlayerResources(playerID, settings.DefaultSettings.STREET_BUILD_COST) == false) {
			return false;
		}
		/*
		 * int[] resources = getPlayerResources(playerId); for (int i = 0; i <
		 * 5; i++) { if (resources[i] <
		 * settings.DefaultSettings.STREET_BUILD_COST[i]) { return false; } }
		 */
		Edge e = board.getEdgeAt(x, y, dir);
		if (e != null) { // valid edge
			if (e.isHasStreet() == false) {
				Edge[] neighbors = board.getLinkedEdges(x, y, dir);
				for (int i = 0; i < neighbors.length; i++) {
					if (neighbors[i] != null) {
						if (neighbors[i].getOwnerID() == playerID) {
							return true;

						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * checks if bandit can be set at specified position
	 * 
	 * @param x
	 * @param y
	 * @param playerId
	 * @return true/false
	 */
	public boolean checkSetBandit(int x, int y, int playerID) {
		Field f = board.getFieldAt(x, y);
		if (f != null && !f.getFieldID().equals(board.getBandit())) { // valid
			// position and
			// not the
			// same as before
			if (playerID == 0) { // check if specified player has a corner at
									// this field
				return true;
			} else {
				Corner[] corners = board.getSurroundingCorners(x, y);
				for (Corner c : corners) {
					if (c.getOwnerID() == playerID) {
						return true;
					}
				}
				return false;
			}
		}

		return false;
	}

	public boolean checkPlayerResources(int playerID, int[] resources) {
		int[] playerResources = getPlayerResources(playerID);
		for (int i = 0; i < 5; i++) {
			if (playerResources[i] < resources[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets an array which contains the player resources in form of
	 * (AmountWood,AmountClay...)
	 * 
	 * @param playerID
	 * @return resource Array
	 */
	public int[] getPlayerResources(int playerID) {
		int[] result = { 0, 0, 0, 0, 0 };
		ArrayList<ResourceType> resList = board.getPlayer(playerID).getResourceCards();
		if (resList == null) {
			return result;
		} else {
			enums.ResourceType resType;
			for (int i = 0; i < resList.size(); i++) {
				resType = resList.get(i);
				switch (resType) {
				// Build costs: {WOOD, CLAY, ORE, SHEEP, CORN}
				case WOOD:
					result[0]++;
					break;
				case CLAY:
					result[1]++;
					break;
				case ORE:
					result[2]++;
					break;
				case SHEEP:
					result[3]++;
					break;
				case CORN:
					result[4]++;
					break;
				default:
					break;
				}
			}
			return result;
		}
	}

	/**
	 * Checks if the player can build a Street at the given position at the
	 * beginning of the game
	 * 
	 * @param x
	 * @param y
	 * @param dir
	 * @param playerID
	 * @return boolean true/false
	 */
	public boolean checkBuildInitialStreet(int x, int y, int dir, int playerID) {
		Edge e = board.getEdgeAt(x, y, dir);
		if (e != null) { // valid edge
			if (e.isHasStreet() == false) {
				Corner[] neighbors = board.getAttachedCorners(x, y, dir);
				for (int i = 0; i < neighbors.length; i++) {
					if (neighbors[i] != null) {
						if (neighbors[i].getOwnerID() == playerID) {
							return true;

						}
					}
				}
			}
		}
		return false;

	}

	/**
	 * Checks if the player can build a Street at the given position at the
	 * beginning of the game
	 * 
	 * @param x
	 * @param y
	 * @param dir
	 * @return boolean true/false
	 */
	public boolean checkBuildInitialVillage(int x, int y, int dir) {
		Corner c = board.getCornerAt(x, y, dir);
		if (c != null) { // valid edge
			if (c.getStatus() == enums.CornerStatus.EMPTY) {
				return true;
			}
		}
		return false;
	}

	public boolean canTrade() {

		// TODO Auto-generated method stub
		return false;
	}

	public boolean canPlayCard() {
		// TODO Auto-generated method stub
		return false;
	}

	public Board getBoard() {
		return board;
	}

	public boolean checkIfActionIsAllowed(int playerID,int currentPlayer,PlayerState state){
		if (playerID != currentPlayer || board.getPlayer(currentPlayer).getPlayerState() != state){
			return false;
		} else {
			return true;			
		}		
	}

}