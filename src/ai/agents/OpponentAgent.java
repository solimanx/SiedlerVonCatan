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
	
//	private int[][] opponentsRessources;
	// {WOOD, CLAY, ORE, SHEEP, CORN, unknown, missing} missing is positive>0
	// after robber loss
//	private int[] initialRessources = { 0, 0, 0, 0, 0, 0, 0 };
//	private int amountPlayer;
	private Board board;

	public PlayerModel getOpponent(Integer ID){
		for(int i=0; i<opponents.size(); i++){
			if(opponents.get(i).getID() == ID){
				return opponents.get(i);
			}
		}
		throw new IllegalArgumentException("ID "+ID+" doesn't exist");
	}
	public OpponentAgent(AdvancedAI aai) {
//		board = aai.getGl().getBoard();
//		amountPlayer = board.getAmountPlayers();
//		opponentsRessources = new int[amountPlayer][7];
//		for (int i = 0; i < amountPlayer; i++) {
//			opponentsRessources[i] = initialRessources;
//		}
	}

	// inputhandler iunpur an diese methode
	public void ressourceObtainEnemy(int diceRollNum) {
		
	}

	public void robberLossEnemy(int playerID){
		getOpponent(playerID).decrementHiddenResources(1);
		//		opponentsRessources[playerID][7] = opponentsRessources[playerID][7] -1;
	}

	public void buildingCostEnemy(int playerID, int[] costs){
		if(costs.length != 5){
			throw new IllegalArgumentException("Illegal Argument in opponentAgent.buildingCostEnemy.");
		}
		decrementOpponentsRessources(playerID, costs);
	}
	
	private void decrementOpponentsRessources(int playerID, int[] costs){
		getOpponent(playerID).decrementResources(costs);
//		for(int i = 0; i<5; i++){
//			if(costs[i] != 0){
//				if(opponentsRessources[playerID][i]<costs[i]){
//					for(int j = 0; j<costs[i]; j++){
//						if(opponentsRessources[playerID][i] != 0){
//							opponentsRessources[playerID][i]--;
//						}
//						else{
//							if(opponentsRessources[playerID][6] != 0){
//								opponentsRessources[playerID][6]--;
//							}
//							else{
//								throw new IllegalStateException("Error in calculation of enemy resources");
//							}
//						}
//					}
//				} else {
//					opponentsRessources[playerID][i] = opponentsRessources[playerID][i] - costs[i];
//				}
//			}
//		}
	}

	public void tradingEnemy(int playerID){

	}

	public void drawCardRobber(int victimID, int playerID){

	}

	public void inventionCardEnemy(int playerID, ResourceType resType){

	}

	public void monopolyCardEnemy(int playerID, int ammountCardsRecievd){

	}
	
	/**
	 * 
	 * @param playerID
	 * @return strength of a player as int
	 */
	public int playerStrength(int playerID){
		return 0;
	}


}
