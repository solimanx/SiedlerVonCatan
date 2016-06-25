package ai.agents;

import java.util.ArrayList;
import java.util.Map;

import ai.AdvancedAI;
import enums.ResourceType;
import model.Board;
import model.objects.Corner;
import model.objects.Field;
import model.objects.PlayerModel;
import settings.DefaultSettings;

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

	
	public OpponentAgent(AdvancedAI aai) {
		board = aai.getGl().getBoard();
		amountPlayer = board.getAmountPlayers();
		opponentsRessources = new int[amountPlayer][7];
		for (int i = 0; i < amountPlayer; i++) {
			opponentsRessources[i] = initialRessources;
		}
	}
	
	/**
	 * converts the internal playerID
	 * @param playerID
	 * @return
	 */
	public PlayerModel getOpponentModel(Integer boardPlayerID){
		for(int i=0; i<opponents.size(); i++){
			if(opponents.get(i).getID() == boardPlayerID){
				return opponents.get(i);
			}
		}
		throw new IllegalArgumentException("ID "+boardPlayerID+" doesn't exist");
	}
	
	public int getInternalPlayerID(PlayerModel pm){
		for(int i = 0; i<opponents.size(); i++){
			if(opponents.get(i).equals(pm)){
				return i;
			}
		}
		throw new IllegalArgumentException("there is no such playerModel in opponents");
	}

	// inputhandler an diese methode
	public void ressourceObtainEnemy() {
		
	}

	public void buildingCostEnemy(int playerID, int[] costs){
		if(costs.length != 5){
			throw new IllegalArgumentException("Illegal Argument in opponentAgent.buildingCostEnemy.");
		}
		decrementOpponentsResources(playerID, costs);
	}
	
	
	/**
	 * use this method only with internal player ID's
	 * @param playerID
	 * @param costs
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
	}

	public void tradingEnemy(int boardPlayerID){
		
	}

	public void drawCardRobber(int boardVictimID, int boardPlayerID){
		int playerID = getInternalPlayerID(getOpponentModel(boardPlayerID));
		int victimID = getInternalPlayerID(getOpponentModel(boardVictimID));
		int[] loss = {0,0,0,0,0,0,1};
		decrementOpponentsResources(victimID, loss);
		opponentsRessources[playerID][6]++;
	}

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
	 * 
	 * @param boardPlayerID
	 * @return strength of a player as int
	 */
	public int playerStrength(int boardPlayerID){
		int playerID = getInternalPlayerID(getOpponentModel(boardPlayerID));
		int result = 0;
		result = 100 * opponents.get(playerID).getVictoryPoints();
		result = result + 10 * ammountResourceCard(playerID);
		//TODO devCard
		
		
		return result;
	}
	
	public int ammountResourceCard(int playerID){
		int result = 0;
		for(int i = 0; i<6; i++){
			result = result + opponentsRessources[playerID][i];
		}
		result = result - opponentsRessources[playerID][6];
		return result;
	}


}
