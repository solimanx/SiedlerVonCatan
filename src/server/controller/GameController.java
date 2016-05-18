package server.controller;

import java.util.ArrayList;

import model.Corner;
import model.Field;

/**
 * @author NiedlichePixel
 * Controls the game flow
 */
public class GameController {
	
	public void gainBoardResources(int diceNum){
		ArrayList<model.Field> correspondingFields = new ArrayList<model.Field>();
		int offset = settings.DefaultSettings.BOARD_SIZE/2;
		model.Field currField;
		for (int i = -offset;i<= offset;i++){
			for (int j = -offset;j<= offset;j++){
				currField = board.getFieldAt(i,j);
				if (currField != null){
					if (currField.getDiceIndex() == diceNum){
						correspondingFields.add(currField);
					}
				}
			}
		}
		Corner[][][] neighborCorners;
		enums.CornerStatus status;
		enums.ResourceType resType;
		Field bandit = board.getBandit();
		for (Field p : correspondingFields){
			if (p != bandit){
			neighborCorners = board.hexService.getNeighborCorners(p);
			resType = p.getResourceType();
			for (Corner o : neighborCorners){
				status = o.getStatus();
				switch(status){
				case VILLAGE:
					addToPlayersResource(o.getPlayer(),resType,1);
				case CITY:
					addToPlayersResource(o.getPlayer(),resType,2);
				default:	
				}
			}
			}
		}
		
	}

}
