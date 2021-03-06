/*
 *
 */
package ai.agents;

import java.util.ArrayList;
import enums.CardType;
import enums.ResourceType;
import model.board.PlayerModel;

// TODO: Auto-generated Javadoc
/**
 * Tracks other opponents resources/cards.
 *
 */
public class OpponentAgent {

	private ArrayList<PlayerModel> opponents = new ArrayList<PlayerModel>();

	private int[][] opponentsRessources;
	// {WOOD, CLAY, ORE, SHEEP, CORN, unknown, missing} missing is positive>0
	// after robber loss
	private int[] initialRessources = { 0, 0, 0, 0, 0, 0, 0 };
	private int amountPlayer;
	// DevCards
	private int[] playedDevCards = { 0, 0, 0, 0, 0 }; // {KNIGHT,INVENTION,STREET,MONOPOLY,VICTORYPOINT}
	private int[] playerDevCards = { 0, 0, 0, 0 }; // 4 player, amount cards by
													// player

	/**
													 * Instantiates a new opponent agent.
													 *
													 * @param pm the pm
													 */
	public OpponentAgent(PlayerModel[] pm) {
		amountPlayer = pm.length;
		opponentsRessources = new int[amountPlayer][7];
		for (int i = 0; i < amountPlayer; i++) {
			opponentsRessources[i] = initialRessources;
			opponents.add(pm[i]);
		}
	}

	/**
	 * Instantiates a new opponent agent.
	 */
	public OpponentAgent() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * converts the internal playerID.
	 *
	 * @param boardPlayerID
	 *            the board player ID
	 * @return the opponent model
	 */
	public PlayerModel getOpponentModel(Integer boardPlayerID) {
		for (int i = 0; i < opponents.size(); i++) {
			if (opponents.get(i).getID() == boardPlayerID) {
				return opponents.get(i);
			}
		} 
		throw new IllegalArgumentException("ID " + boardPlayerID + " doesn't exist");
	}

	/**
	 * Gets the internal player ID.
	 *
	 * @param pm
	 *            the pm
	 * @return the internal player ID
	 */
	public int getInternalPlayerID(PlayerModel pm) {
		for (int i = 0; i < opponents.size(); i++) {
			if (opponents.get(i).equals(pm)) {
				return i;
			}
		}
		throw new IllegalArgumentException("there is no such playerModel in opponents");
	}

	/**
	 * Ressource obtain enemy.
	 *
	 * @param boardPlayerID the board player ID
	 * @param ressources the ressources
	 */
	// inputhandler an diese methode
	public void ressourceObtainEnemy(int boardPlayerID, int[] ressources) {
		int playerID = getInternalPlayerID(getOpponentModel(boardPlayerID));
		if (ressources.length == 5) {
			for (int i = 0; i < 5; i++) {
				opponentsRessources[playerID][i] = opponentsRessources[playerID][i] + ressources[i];
			}
		} else if (ressources.length == 1) {
			opponentsRessources[playerID][6]++;
		}
		if (ressources.length != 5 && ressources.length != 1) {
			throw new IllegalArgumentException("Illegal Argument in opponetAgent.ressourceObtainEnemy");
		}
	}

	/**
	 * Building cost enemy.
	 *
	 * @param boardPlayerID the board player ID
	 * @param costs            the costs
	 */
	public void costsEnemy(int boardPlayerID, int[] costs) {
		int playerID = getInternalPlayerID(getOpponentModel(boardPlayerID));
		if (costs.length == 5 || costs.length == 1) {
			decrementOpponentsResources(playerID, costs);
		} else {
		throw new IllegalArgumentException("Illegal Argument in opponentAgent.buildingCostEnemy.");
		}
		}

	/**
	 * use this method only with internal player ID's.
	 *
	 * @param playerID
	 *            the player ID
	 * @param costs
	 *            the costs
	 */
	private void decrementOpponentsResources(int playerID, int[] costs) {
		if (costs.length == 5) {
			for (int i = 0; i < 5; i++) {
				if (costs[i] != 0) {
					if (opponentsRessources[playerID][i] < costs[i]) {
						for (int j = 0; j < costs[i]; j++) {
							if (opponentsRessources[playerID][i] != 0) {
								opponentsRessources[playerID][i]--;
							} else {
								if (opponentsRessources[playerID][4] != 0) {
									opponentsRessources[playerID][4]--;
								} else {
									//throw new IllegalStateException("Error in calculation of enemy resources");
								}
							}
						}
					} else {
						opponentsRessources[playerID][i] = opponentsRessources[playerID][i] - costs[i];
					}
				}
			}
		} else if (costs.length == 1) {
			opponentsRessources[playerID][6]++;
		}

		if (ammountResourceCard(playerID) == 0) {
			opponentsRessources[playerID] = initialRessources;
		}
		if (costs.length != 5 && costs.length != 1) {
			throw new IllegalArgumentException("Illegal Argument in opponetAgent.decrementOpponentsResources");
		}
	}

	/**
	 * Trading enemy.
	 *
	 * @param boardPlayerID
	 *            the board player ID
	 */
	public void tradingEnemy(int boardPlayerID) {

	}

	/**
	 * Draw card robber.
	 *
	 * @param boardVictimID
	 *            the board victim ID
	 * @param boardPlayerID
	 *            the board player ID
	 */
	public void drawCardRobber(int boardVictimID, int boardPlayerID) {
		int playerID = getInternalPlayerID(getOpponentModel(boardPlayerID));
		int victimID = getInternalPlayerID(getOpponentModel(boardVictimID));
		int[] loss = { 0, 0, 0, 0, 0, 0, 1 };
		decrementOpponentsResources(victimID, loss);
		opponentsRessources[playerID][6]++;
	}

	/**
	 * Monopoly card enemy.
	 *
	 * @param boardPlayerID
	 *            the board player ID
	 * @param ammountCardsRecievd
	 *            the amount cards received
	 * @param resType
	 *            the res type
	 */
	public void monopolyCardEnemy(int boardPlayerID, int ammountCardsRecievd, ResourceType resType) {
		int playerID = getInternalPlayerID(getOpponentModel(boardPlayerID));
		switch (resType) {
		case WOOD:
			opponentsRessources[playerID][0] = opponentsRessources[playerID][0] + ammountCardsRecievd;
			break;
		case CLAY:
			opponentsRessources[playerID][1] = opponentsRessources[playerID][1] + ammountCardsRecievd;
			break;
		case ORE:
			opponentsRessources[playerID][2] = opponentsRessources[playerID][2] + ammountCardsRecievd;
			break;
		case SHEEP:
			opponentsRessources[playerID][3] = opponentsRessources[playerID][3] + ammountCardsRecievd;
			break;
		case CORN:
			opponentsRessources[playerID][4] = opponentsRessources[playerID][4] + ammountCardsRecievd;
			break;
		default:
			throw new IllegalArgumentException("Illegal ResourceType");
		}
		// TODO andere spieler, die dadurch karten verlieren...
	}

	/**
	 * Player strength.
	 *
	 * @param boardPlayerID
	 *            the board player ID
	 * @return strength of a player as int
	 */
	public int playerStrength(int boardPlayerID) {
		int playerID = getInternalPlayerID(getOpponentModel(boardPlayerID));
		int result = 0;
		result = 100 * opponents.get(playerID).getVictoryPoints();
		result = result + 10 * ammountResourceCard(playerID);
		for (int i = 0; i < playerDevCards[playerID]; i++) {
			switch (i) {
			case 0:
				result = result + 30;
				break;
			case 1:
				result = result + 55;
				break;
			default:
				result = result + 95;
				break;
			}
		}

		return result;
	}

	/**
	 * Ammount resource card.
	 *
	 * @param playerID
	 *            the player ID
	 * @return the int
	 */
	public int ammountResourceCard(int playerID) {
		int result = 0;
		for (int i = 0; i < 6; i++) {
			result = result + opponentsRessources[playerID][i];
		}
		result = result - opponentsRessources[playerID][6];
		return result;
	}

	/**
	 * Bought dev card.
	 *
	 * @param boardPlayerID the board player ID
	 */
	public void boughtDevCard(int boardPlayerID) {
		int playerID = getInternalPlayerID(getOpponentModel(boardPlayerID));
		playerDevCards[playerID]++;
	}

	/**
	 * {KNIGHT,INVENTION,STREET,MONOPOLY,VICTORYPOINT}.
	 *
	 * @param type the type
	 * @param boardPlayerID the board player ID
	 */
	public void devCardPlayed(CardType type, int boardPlayerID) {
		int playerID = getInternalPlayerID(getOpponentModel(boardPlayerID));
		if (playerDevCards[playerID] != 0) {
			playerDevCards[playerID]--;
		} else {
			throw new IllegalStateException("more development cards played, than the player has");
		}
		switch (type) {
		case KNIGHT:
			playedDevCards[0]++;
			break;
		case INVENTION:
			playedDevCards[1]++;
			break;
		case STREET:
			playedDevCards[2]++;
			break;
		case MONOPOLY:
			playedDevCards[3]++;
			break;
		case VICTORYPOINT:
			playedDevCards[4]++;
			break;
		default:
			throw new IllegalArgumentException("no such card implemented");
		}
	}

	/**
	 * Gets the amount player.
	 *
	 * @return the amount player
	 */
	public int getAmountPlayer() {
		return amountPlayer;
	}
	
	

}
