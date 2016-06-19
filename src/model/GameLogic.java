package model;

import java.util.ArrayList;

import enums.HarbourStatus;
import enums.PlayerState;
import enums.ResourceType;
import model.objects.Corner;
import model.objects.Edge;
import model.objects.Field;
import network.ProtocolToModel;
import settings.DefaultSettings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Implements rules of the game
 */
public class GameLogic {
	private Board board;

	public GameLogic(Board b) {
		this.board = b;
	}

	private static Logger logger = LogManager.getLogger(GameLogic.class.getSimpleName());

	/**
	 * Checks if the player can build a city at the given position
	 *
	 * @param x
	 * @param y
	 * @param dir
	 *            //* @param player
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
						if (e[i].getOwnerID() != null && e[i].getOwnerID() == playerID) { // is
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
	 *            //* @param player
	 * @return boolean true/false
	 */
	public boolean checkBuildCity(int x, int y, int dir, int playerID) {
		if (board.getPlayer(playerID).getAmountCities() <= 0) {
			System.out.println("no cities left");
			logger.info("no cities left");
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
	 *            // * @param player
	 * @return boolean true/false
	 */
	public boolean checkBuildStreet(int x, int y, int dir, int playerID) {
		// enough streets
		if (board.getPlayer(playerID).getAmountStreets() <= 0) {
			return false;
		}
		// can he afford
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
			// unoccupied
			if (e.isHasStreet() == false) {
				Edge[] neighbors = board.getLinkedEdges(x, y, dir);
				for (int i = 0; i < neighbors.length; i++) {
					if (neighbors[i] != null) {
						if (neighbors[i].getOwnerID() != null && neighbors[i].getOwnerID() == playerID) {
							// check if there is a hostile village in between
							Corner[] thisAdjoiningVillages = board.getAttachedCorners(x, y, dir);
							String id = e.getEdgeID();
							int[] nCs = ProtocolToModel.getEdgeCoordinates(id);
							Corner[] neighbourAdjoiningVillages = board.getAttachedCorners(nCs[0], nCs[1], nCs[2]);
							Corner schnitt = null; // schnittmenge aus Villages;
							for (int j = 0; j < thisAdjoiningVillages.length; j++) {
								for (int k = 0; k < neighbourAdjoiningVillages.length; k++) {
									if (thisAdjoiningVillages[j].equals(neighbourAdjoiningVillages[k])) {
										schnitt = thisAdjoiningVillages[j];
									}
								}
							}
							if (schnitt.getOwnerID() != null && schnitt.getOwnerID() != playerID) {
								return false;
							} else {
								return true;
							}
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
	 *            // * @param playerId
	 * @return true/false
	 */
	public boolean checkSetBandit(int x, int y, Integer playerID) {
		Field f = board.getFieldAt(x, y);
		if (f != null && !f.getFieldID().equals(board.getBandit())) { // valid
			// position and
			// not the
			// same as before
			if (playerID == null) { // check if specified player has a corner at
				// this field
				return true;
			} else {
				Corner[] corners = board.getSurroundingCorners(x, y);
				for (Corner c : corners) {
					if (c.getOwnerID() != null && c.getOwnerID() == playerID) {
						return true;
					}
				}
				return false;
			}
		}

		return false;
	}

	public boolean checkPlayerResources(int playerID, int[] cost) {
		// int[] playerResources = getPlayerResources(playerID);
		int[] playerResources = board.getPlayer(playerID).getResources();
		for (int i = 0; i < 5; i++) {
			if (playerResources[i] < cost[i]) {
				return false;
			}
		}
		return true;
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
						if (neighbors[i].getOwnerID() != null && neighbors[i].getOwnerID() == playerID) {
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

	/**
	 * @param playerID
	 *            ID of the player, who wants to buy a DevelopmentCard
	 * @return returns true, if it is possible for the player to buy a
	 *         Developmentcard else false
	 */
	public boolean checkBuyDevCard(int playerID) {
		if (!board.getDevCardStack().buyable()) {
			return false;
		}
		if (!checkPlayerResources(playerID, settings.DefaultSettings.DEVCARD_BUILD_COST)) {
			return false;
		}
		return true;
	}

	/**
	 * Check if player has a settlement on a wood 2:1 harbour
	 *
	 * @return
	 */
	public boolean hasWoodHarbour(int playerID) {
		int radius = DefaultSettings.BOARD_RADIUS;
		for (int i = -radius; i <= radius; i++) {
			for (int j = -radius; j <= radius; j++) {
				for (int k = 0; k < 2; k++) {
					if (board.getCornerAt(j, i, k) != null) {
						Corner c = board.getCornerAt(j, i, k);
						if (c.getHarbourStatus().equals(HarbourStatus.WOOD)) {
							if (c.getOwnerID()!= null && c.getOwnerID() == playerID) {
								return true;
							}
						}

					}
				}
			}
		}
		return false;
	}

	/**
	 * Check if player has a settlement on a clay 2:1 harbour
	 *
	 * @return
	 */
	public boolean hasClayHarbour(int playerID) {
		int radius = DefaultSettings.BOARD_RADIUS;
		for (int i = -radius; i <= radius; i++) {
			for (int j = -radius; j <= radius; j++) {
				for (int k = 0; k < 2; k++) {
					if (board.getCornerAt(j, i, k) != null) {
						Corner c = board.getCornerAt(j, i, k);
						if (c.getHarbourStatus().equals(HarbourStatus.CLAY)) {
							if (c.getOwnerID() != null && c.getOwnerID() == playerID) {
								return true;
							}
						}

					}
				}
			}
		}
		return false;
	}

	/**
	 * Check if player has a settlement on a wool 2:1 harbour
	 *
	 * @return
	 */
	public boolean hasWoolHarbour(int playerID) {
		int radius = DefaultSettings.BOARD_RADIUS;
		for (int i = -radius; i <= radius; i++) {
			for (int j = -radius; j <= radius; j++) {
				for (int k = 0; k < 2; k++) {
					if (board.getCornerAt(j, i, k) != null) {
						Corner c = board.getCornerAt(j, i, k);
						if (c.getHarbourStatus().equals(HarbourStatus.SHEEP)) {
							if (c.getOwnerID() != null && c.getOwnerID() == playerID) {
								return true;
							}
						}

					}
				}
			}
		}
		return false;
	}

	/**
	 * Check if player has a settlement on a corn 2:1 harbour
	 *
	 * @return
	 */
	public boolean hasCornHarbour(int playerID) {
		int radius = DefaultSettings.BOARD_RADIUS;
		for (int i = -radius; i <= radius; i++) {
			for (int j = -radius; j <= radius; j++) {
				for (int k = 0; k < 2; k++) {
					if (board.getCornerAt(j, i, k) != null) {
						Corner c = board.getCornerAt(j, i, k);
						if (c.getHarbourStatus().equals(HarbourStatus.CORN)) {
							if (c.getOwnerID()!=null && c.getOwnerID() == playerID) {
								return true;
							}
						}

					}
				}
			}
		}
		return false;
	}

	/**
	 * Check if player has a settlement on a ore 2:1 harbour
	 *
	 * @return
	 */
	public boolean hasOreHarbour(int playerID) {
		int radius = DefaultSettings.BOARD_RADIUS;
		for (int i = -radius; i <= radius; i++) {
			for (int j = -radius; j <= radius; j++) {
				for (int k = 0; k < 2; k++) {
					if (board.getCornerAt(j, i, k) != null) {
						Corner c = board.getCornerAt(j, i, k);
						if (c.getHarbourStatus().equals(HarbourStatus.ORE)) {
							if (c.getOwnerID() != null && c.getOwnerID() == playerID) {
								return true;
							}
						}

					}
				}
			}
		}
		return false;
	}

	/**
	 * Check if player has a settlement on a 3:1 harbour.
	 *
	 * @return
	 */
	public boolean hasThreeOneHarbour(int playerID) {
		int radius = DefaultSettings.BOARD_RADIUS;
		for (int i = -radius; i <= radius; i++) {
			for (int j = -radius; j <= radius; j++) {
				for (int k = 0; k < 2; k++) {
					if (board.getCornerAt(j, i, k) != null) {
						Corner c = board.getCornerAt(j, i, k);
						if (c.getHarbourStatus().equals(HarbourStatus.THREE_TO_ONE)) {
							if (c.getOwnerID()!= null && c.getOwnerID() == playerID) {
								return true;
							}
						}

					}
				}
			}
		}
		return false;
	}

	public Board getBoard() {
		return board;
	}

	/**
	 *
	 * @param playerID
	 * @param currentPlayer
	 * @param state
	 * @return true if it is forbidden false if it is not
	 */
	public boolean isActionForbidden(int playerID, int currentPlayer, PlayerState state) {
		if (playerID != currentPlayer || board.getPlayer(currentPlayer).getPlayerState() != state) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkPlayDevCard(int modelID, int currentPlayer) {
		if (isActionForbidden(modelID, currentPlayer, PlayerState.TRADING_OR_BUILDING)
				|| board.getPlayer(modelID).hasPlayedDevCard()) {
			return false;
		}
		return true;
	}

}