package ai.agents;

import java.util.ArrayList;
import java.util.Map;

import enums.ResourceType;
import model.Board;
import model.objects.Corner;
import model.objects.Field;
import settings.DefaultSettings;

/**
 * Tracks other opponents resources/cards.
 *
 */
public class OpponentAgent {

	private int[][] opponentsRessources;
	// {WOOD, CLAY, ORE, SHEEP, CORN, unknown, missing} missing is positive>0
	// after robber loss
	private int[] initialRessources = { 0, 0, 0, 0, 0, 0, 0 };
	private int amountPlayer;
	private Board board;

	public OpponentAgent() {
		amountPlayer = board.getAmountPlayers(); // TODO is ammount enemy
													// player
		opponentsRessources = new int[amountPlayer][7];
		board = new Board();
		for (int i = 0; i < amountPlayer; i++) {
			opponentsRessources[i] = initialRessources;
		}

	}

	// errechnet aus dem würfelergebniss die ressourcen, die ein gegner erhalten
	// würde und speichert dies.
	public void ressourceObtainEnemy(int diceRollNum) {
		ArrayList<Field> diceFields = new ArrayList<Field>();
		for (Map.Entry<String, int[]> entry : Board.getStringToCoordMap().entrySet()) {
			int[] coord = entry.getValue();
			Field f = board.getFieldAt(coord[0], coord[1]);
			Integer diceInd = f.getDiceIndex();
			if (diceInd != null) {
				if (diceInd == diceRollNum) {
					diceFields.add(f);
				}
			}
		}
		int[] coords;
		Corner[] neighbors;
		ResourceType currResType;
		for (Field f : diceFields) {
			if (!f.getFieldID().equals(board.getBandit())) {
				coords = board.getFieldCoordinates(f.getFieldID());
				neighbors = board.getSurroundingCorners(coords[0], coords[1]);
				for (int i = 0; i < neighbors.length; i++) {
					if (neighbors[i] != null) {
						switch (neighbors[i].getStatus()) {
						case VILLAGE:
							currResType = f.getResourceType();
							opponentsRessources[neighbors[i].getOwnerID()][DefaultSettings.RESOURCE_VALUES
									.get(currResType)]++;
							break;
						case CITY:
							currResType = f.getResourceType();
							for (int j = 0; j < 2; j++) {
								opponentsRessources[neighbors[i].getOwnerID()][DefaultSettings.RESOURCE_VALUES
										.get(currResType)]++;
							}
							break;
						default:
							break;
						}
					}
				}
			}
		}
	}

	public void robberLossEnemy(int playerID){

	}

	public void buildingCostEnemy(int playerID){

	}

	public void tradingEnemy(int playerID){

	}

	public void drawCardRobber(int victimID, int playerID){

	}

	public void inventionCardEnemy(int playerID, ResourceType resType){

	}

	public void monopolyCardEnemy(int playerID, int ammountCardsRecievd){

	}



}
