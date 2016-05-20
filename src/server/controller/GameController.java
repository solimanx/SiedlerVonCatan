package server.controller;

import java.util.ArrayList;
import java.util.Random;

import client.controller.GameLogic;
import client.controller.ViewController;
import enums.PlayerState;
import enums.ResourceType;
import javafx.stage.Stage;
import model.Board;
import model.Corner;
import model.Edge;
import model.Field;
import model.PlayerModel;
import settings.DefaultSettings;

/**
 * @author NiedlichePixel
 * Controls the game flow
 */
public class GameController implements GameControllerInterface {
	Board board;
	GameLogic gameLogic;
	PlayerModel[] playerModels;
	Field[][] fields;
	ViewController viewController;
	
	public GameController(Stage primaryStage){
		init();
		viewController = new ViewController(primaryStage,board);
	}

	@Override
	public void init() {
		this.board = Board.getInstance();
		this.gameLogic = new GameLogic(board);
		this.playerModels = board.getPlayerModels();
		this.fields = board.getFields();
		generateBoard(fields[-2][-2],true);
		// TODO Auto-generated method stub
		
	}	
	
	/**
	 * Generates the resource and the dice index of each field
	 * calls gui via setField to set the correct graphics
	 * if randomDesert is set then the desert will be placed random at the board, else it will be set in the middle 
	 * @param initialField
	 * @param randomDesert
	 */
	private void generateBoard(Field initialField,boolean randomDesert){
		Field[] fields = getSpiral(initialField);
		int[] cards = DefaultSettings.LANDSCAPE_CARDS;
		int currNum;
		if (randomDesert){
			int diceInd = 0;
			for (int i = 0;i < fields.length;i++){ 
				Random r = new Random();
				boolean notFound = true;
				do {
					currNum = r.nextInt(5); //desert allowed
					if (cards[currNum] > 0){
						notFound = false;
					}
				} while (notFound);
				cards[currNum] --;
				fields[i].setResourceType(DefaultSettings.RESOURCE_ORDER[currNum]);
				if (currNum != 5){
				    fields[i].setDiceIndex(DefaultSettings.DICE_NUMBERS[diceInd]);
				    diceInd ++;
				}
			}
		} else {
		for (int i = 0;i < fields.length-1;i++){ 
			Random r = new Random();
			boolean notFound = true;
			do {
				currNum = r.nextInt(4);
				if (cards[currNum] > 0){
					notFound = false;
				}
			} while (notFound);
			cards[currNum] --;
			fields[i].setResourceType(DefaultSettings.RESOURCE_ORDER[currNum]);
			fields[i].setDiceIndex(DefaultSettings.DICE_NUMBERS[currNum]);
		}
		fields[18].setResourceType(ResourceType.NOTHING);
		}
	}
	
	/* (non-Javadoc)
	 * @see server.controller.GameControllerInterface#gainBoardResources(int)
	 */
	public void gainBoardResources(int diceNum){
		ArrayList<model.Field> correspondingFields = new ArrayList<model.Field>();
		for (int i = 0; i< fields.length;i++){
			for (int j = 0; j < fields[i].length;j++){
				if (fields[i][j] != null){
					if (fields[i][j].getDiceIndex() == diceNum){
						correspondingFields.add(fields[i][j]);
					}
				}
			}
		}

		Corner[] neighborCorners;
		enums.CornerStatus status;
		enums.ResourceType resType;
		Field bandit = board.getBandit();
		for (Field p : correspondingFields){
			if (p != bandit){
			neighborCorners = board.getSurroundingCorners(p);
			resType = p.getResourceType();
			for (Corner o : neighborCorners){
				status = o.getStatus();
				switch(status){
				case VILLAGE:
					addToPlayersResource(o.getOwnedByPlayer().getId(),resType,1);
				case CITY:
					addToPlayersResource(o.getOwnedByPlayer().getId(),resType,2);
				default:	
				}
			}
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see server.controller.GameControllerInterface#buildVillage(int, int, int, int)
	 */
	@Override 
	public void buildVillage(int x,int y,int dir,int playerId) {
		if (gameLogic.checkBuildVillage(x, y, dir, playerId)){
			Corner c = board.getCornerAt(x, y, dir);
			c.setStatus(enums.CornerStatus.VILLAGE);
			c.setOwnedByPlayer(playerModels[playerId]);
			playerModels[playerId].decreaseAmountVillages();
			Corner[] neighbors = board.getAdjacentCorners(x, y, dir);
			for (int i = 0;i<neighbors.length;i++){
				neighbors[i].setStatus(enums.CornerStatus.BLOCKED);
			}
			int[] costs = DefaultSettings.VILLAGE_BUILD_COST;
			subFromPlayersResources(playerId,costs);
			
			viewController.setCorner(x,y,dir,enums.CornerStatus.VILLAGE,playerId);
		}
		
	}

	@Override
	public void buildStreet(int x,int y,int dir,int playerId) {
		if (gameLogic.checkBuildStreet(x, y, dir, playerId)){
			Edge e = board.getEdgeAt(x, y, dir);
			e.setHasStreet(true);
			e.setOwnedByPlayer(playerModels[playerId]);
			playerModels[playerId].decreaseAmountStreets();
			int[] costs = DefaultSettings.STREET_BUILD_COST;
			subFromPlayersResources(playerId,costs);
			
			viewController.setStreet(x,y,dir,playerId);
		}
		
	}

	@Override
	public void buildCity(int x,int y,int dir,int playerId) {
		if (gameLogic.checkBuildCity(x, y, dir, playerId)){
			Corner c = board.getCornerAt(x, y, dir);
			c.setStatus(enums.CornerStatus.CITY);
			c.setOwnedByPlayer(playerModels[playerId]);
			playerModels[playerId].increaseAmountVillages();
			playerModels[playerId].decreaseAmountCities();
			int[] costs = DefaultSettings.CITY_BUILD_COST;
			subFromPlayersResources(playerId,costs);
			
			viewController.setCorner(x,y,dir,playerId,enums.CornerStatus.CITY);
		}
		
	}
	
	public void buildInitialStreet(int x, int y,int dir,int playerId){
		if (gameLogic.checkBuildInitialStreet(x, y, dir, playerId)){
			Edge e = board.getEdgeAt(x, y, dir);
			e.setHasStreet(true);
			e.setOwnedByPlayer(playerModels[playerId]);
			playerModels[playerId].decreaseAmountStreets();	
			
			viewController.setStreet(x,y,dir,playerId);
		}
	}
	
	public void buildInitialVillage(int x, int y,int dir,int playerId){
		if (gameLogic.checkBuildInitialVillage(x, y, dir)){
			Corner c = board.getCornerAt(x, y, dir);
			c.setStatus(enums.CornerStatus.VILLAGE);
			c.setOwnedByPlayer(playerModels[playerId]);
			playerModels[playerId].decreaseAmountVillages();
			
			viewController.setCorner(x,y,dir,playerId,enums.CornerStatus.VILLAGE);
		}
	}
	
	private void addToPlayersResource(int playerId, ResourceType resType, int amount) {
		ArrayList<ResourceType> resourceCards = playerModels[playerId].getResourceCards();
		for (int i = 0;i < amount;i++){
			resourceCards.add(resType);
		}
		playerModels[playerId].setResourceCards(resourceCards);
	}
	
	private void subFromPlayersResources(int playerId, int[] costs) {
		ResourceType currResType;
	    ArrayList<ResourceType> list = playerModels[playerId].getResourceCards();			
		for (int i = 0;i< costs.length;i++){
			for (int j = list.size() - 1;j >= 0 ;j--){ //umkehren wegen remove
				currResType = list.get(j);
				switch(currResType){
				// Build costs: {WOOD, CLAY, ORE, SHEEP, CORN}
				case WOOD:
					if (costs[0] > 0) {
						list.remove(j);
						costs[0] --;
					}
				case CLAY:
					if (costs[1] > 0){
						list.remove(j);
						costs[1] --;
					}
				case ORE:
					if (costs[2] > 0){
						list.remove(j);
						costs[2] --;
					}
				case SHEEP:
					if (costs[3] > 0){
						list.remove(j);
						costs[3] --;
					}
				case CORN:
					if (costs[4] > 0){
						list.remove(j);
						costs[4] --;
					}
				default:
				}
			}
			
		}
		playerModels[playerId].setResourceCards(list);		
	}

	@Override
	public void setBandit(int x,int y) {
		if (gameLogic.checkSetBandit(x, y)){
			board.setBandit(board.getFieldAt(x,y));
			//view.setBandit ?
		}
		
	}

	@Override
	public void setPlayerState(PlayerState state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGameState() {
		// TODO Auto-generated method stub
		
	}

}
