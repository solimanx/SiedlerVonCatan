/*
 *
 */
package ai.agents;

import java.util.ArrayList;
import java.util.Map;

import ai.AdvancedAI;
import enums.CardType;
import enums.ResourceType;
import model.Board;
import model.objects.Corner;
import model.objects.Field;
import model.objects.PlayerModel;
import settings.DefaultSettings;

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
	private Board board;
	private int[] playedDevCards = {0,0,0,0,0}; //{KNIGHT,INVENTION,STREET,MONOPOLY,VICTORYPOINT}
	private int[] playerDevCards = {0,0,0,0}; // 4 player, amount cards by player


	/**
	 * Instantiates a new opponent agent.
	 *
	 * @param aai the aai
	 */
	public OpponentAgent(AdvancedAI aai) {
		board = aai.getGl().getBoard();
		amountPlayer = board.getAmountPlayers();
		opponentsRessources = new int[amountPlayer][7];
		for (int i = 0; i < amountPlayer; i++) {
			opponentsRessources[i] = initialRessources;
			opponents.add(aai.getGl().getBoard().getPlayer(i));
		}
	}

	/**
	 * converts the internal playerID.
	 *
	 * @param boardPlayerID the board player ID
	 * @return the opponent model
	 */
	public PlayerModel getOpponentModel(Integer boardPlayerID){
		for(int i=0; i<opponents.size(); i++){
			if(opponents.get(i).getID() == boardPlayerID){
				return opponents.get(i);
			}
		}
		throw new IllegalArgumentException("ID "+boardPlayerID+" doesn't exist");
	}

	/**
	 * Gets the internal player ID.
	 *
	 * @param pm the pm
	 * @return the internal player ID
	 */
	public int getInternalPlayerID(PlayerModel pm){
		for(int i = 0; i<opponents.size(); i++){
			if(opponents.get(i).equals(pm)){
				return i;
			}
		}
		throw new IllegalArgumentException("there is no such playerModel in opponents");
	}

	/**
	 * Ressource obtain enemy.
	 */
	// inputhandler an diese methode
	public void ressourceObtainEnemy(int boardPlayerID, int[] ressources) {
		int playerID = getInternalPlayerID(getOpponentModel(boardPlayerID));
		for(int i = 0; i<5; i++){
			opponentsRessources[playerID][i] = opponentsRessources[playerID][i] + ressources[i];
		}
	}

	/**
	 * Building cost enemy.
	 *
	 * @param playerID the player ID
	 * @param costs the costs
	 */
	public void CostsEnemy(int boardPlayerID, int[] costs){
		int playerID = getInternalPlayerID(getOpponentModel(boardPlayerID));
		if(costs.length != 5){
			throw new IllegalArgumentException("Illegal Argument in opponentAgent.buildingCostEnemy.");
		}
		decrementOpponentsResources(playerID, costs);
	}


	/**
	 * use this method only with internal player ID's.
	 *
	 * @param playerID the player ID
	 * @param costs the costs
	 */
	private void decrementOpponentsResources(int playerID, int[] costs){
		for(int i = 0; i<5; i++){
			if(costs[i] != 0){
				if(opponentsRessources[playerID][i]<costs[i]){
					for(int j = 0; j<costs[i]; j++){
						if(opponentsRessources[playerID][i] != 0){
							opponentsRessources[playerID][i]--;
						}
						else{
							if(opponentsRessources[playerID][5] != 0){
								opponentsRessources[playerID][5]--;
							}
							else{
								throw new IllegalStateException("Error in calculation of enemy resources");
							}
						}
					}
				} else {
					opponentsRessources[playerID][i] = opponentsRessources[playerID][i] - costs[i];
				}
			}
		}
		if (ammountResourceCard(playerID) == 0){
			opponentsRessources[playerID] = initialRessources;
		}
	}

	/**
	 * Trading enemy.
	 *
	 * @param boardPlayerID the board player ID
	 */
	public void tradingEnemy(int boardPlayerID){

	}

	/**
	 * Draw card robber.
	 *
	 * @param boardVictimID the board victim ID
	 * @param boardPlayerID the board player ID
	 */
	public void drawCardRobber(int boardVictimID, int boardPlayerID){
		int playerID = getInternalPlayerID(getOpponentModel(boardPlayerID));
		int victimID = getInternalPlayerID(getOpponentModel(boardVictimID));
		int[] loss = {0,0,0,0,0,0,1};
		decrementOpponentsResources(victimID, loss);
		opponentsRessources[playerID][6]++;
	}

	/**
	 * Invention card enemy.
	 *
	 * @param boardPlayerID the board player ID
	 * @param resType the res type
	 */
	public void inventionCardEnemy(int boardPlayerID, ResourceType resType){
		int playerID = getInternalPlayerID(getOpponentModel(boardPlayerID));
		switch (resType) {
		case WOOD:
			opponentsRessources[playerID][0] = opponentsRessources[playerID][0] +2;
			break;
		case CLAY:
			opponentsRessources[playerID][1] = opponentsRessources[playerID][1] +2;
			break;
		case ORE:
			opponentsRessources[playerID][2] = opponentsRessources[playerID][2] +2;
			break;
		case SHEEP:
			opponentsRessources[playerID][3] = opponentsRessources[playerID][3] +2;
			break;
		case CORN:
			opponentsRessources[playerID][4] = opponentsRessources[playerID][4] +2;
			break;
		default:
			throw new IllegalArgumentException("Illegal ResourceType");
		}
	}

	/**
	 * Monopoly card enemy.
	 *
	 * @param boardPlayerID the board player ID
	 * @param ammountCardsRecievd the ammount cards recievd
	 * @param resType the res type
	 */
	public void monopolyCardEnemy(int boardPlayerID, int ammountCardsRecievd, ResourceType resType){
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
		//TODO andere spieler, die dadurch karten verlieren...
	}

	/**
	 * Player strength.
	 *
	 * @param boardPlayerID the board player ID
	 * @return strength of a player as int
	 */
	public int playerStrength(int boardPlayerID){
		int playerID = getInternalPlayerID(getOpponentModel(boardPlayerID));
		int result = 0;
		result = 100 * opponents.get(playerID).getVictoryPoints();
		result = result + 10 * ammountResourceCard(playerID);
		for(int i = 0; i<playerDevCards[playerID]; i++){
			switch (i) {
			case 0: result = result + 30;
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
	 * @param playerID the player ID
	 * @return the int
	 */
	public int ammountResourceCard(int playerID){
		int result = 0;
		for(int i = 0; i<6; i++){
			result = result + opponentsRessources[playerID][i];
		}
		result = result - opponentsRessources[playerID][6];
		return result;
	}

	public void boughtDevCard(int boardPlayerID){
		int playerID = getInternalPlayerID(getOpponentModel(boardPlayerID));
		playerDevCards[playerID]++;
	}


	/** {KNIGHT,INVENTION,STREET,MONOPOLY,VICTORYPOINT}
	 *
	 * @param type
	 */
	public void devCardPlayed(CardType type, int boardPlayerID){
		int playerID = getInternalPlayerID(getOpponentModel(boardPlayerID));
		if(playerDevCards[playerID] != 0){
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


}
