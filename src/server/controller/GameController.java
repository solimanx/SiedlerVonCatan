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
 * Controls the game flow
 * 
 * @author NiedlichePixel
 * 
 * 
 */
public class GameController implements GameControllerInterface {
	Board board;
	GameLogic gameLogic;
	PlayerModel[] playerModels;
	Field[][] fields;
	ViewController viewController;

	public GameController(Stage primaryStage,int amountPlayers) {
		this.board = Board.getInstance(amountPlayers);
		this.gameLogic = new GameLogic(board);
		this.playerModels = board.getPlayerModels();
		this.fields = board.getFields();
		viewController = new ViewController(primaryStage,board,this); //DEBUG ONLY!!
		init();		
	}

	@Override
	public void init() { 

		generateBoard(fields[2][2],true);	
		/*
		  for (int i = 1; i <= amountPlayers;i++){
		      networkController.initClients(i,board)...
		  end
		 */
		addToPlayersResource(1,ResourceType.WOOD,3); //All DEBUG!!
		addToPlayersResource(1,ResourceType.CLAY,3);
		addToPlayersResource(1,ResourceType.ORE,3);
		addToPlayersResource(1,ResourceType.SHEEP,3);
		addToPlayersResource(1,ResourceType.CORN,3);
		setPlayerState(1,PlayerState.PLAYING); // player 1 begins

	}

	/**
	 * Generates the resource and the dice index of each field calls gui via
	 * setField to set the correct graphics if randomDesert is set then the
	 * desert will be placed random at the board, else it will be set in the
	 * middle
	 * 
	 * @param initialField
	 * @param randomDesert
	 */
	private void generateBoard(Field initialField, boolean randomDesert) {
		ArrayList<Field> fields = board.getAllFields(); //spiral implementieren
		System.out.println("Size" +fields.size());
		int[] cards = DefaultSettings.LANDSCAPE_CARDS;
		int currNum;
		if (randomDesert) {
			int diceInd = 0;
			for (int i = 0; i < fields.size(); i++) {

				Random r = new Random();
				boolean notFound = true;
				do {
					currNum = r.nextInt(6); // desert allowed
					System.out.println(currNum);
					if (cards[currNum] > 0) {
						notFound = false;
					}
				} while (notFound);
				cards[currNum]--;
				fields.get(i).setResourceType(DefaultSettings.RESOURCE_ORDER[currNum]);
				if (currNum != 5) {
					fields.get(i).setDiceIndex(DefaultSettings.DICE_NUMBERS[diceInd]);
					diceInd++;
				} else {
					fields.get(i).setDiceIndex(0);
				}
			}
		} else {
			for (int i = 0; i < fields.size() - 1; i++) {
				Random r = new Random();
				boolean notFound = true;
				do {
					currNum = r.nextInt(4);
					if (cards[currNum] > 0) {
						notFound = false;
					}
				} while (notFound);
				cards[currNum]--;
				fields.get(i).setResourceType(DefaultSettings.RESOURCE_ORDER[currNum]);
				fields.get(i).setDiceIndex(DefaultSettings.DICE_NUMBERS[currNum]);
			}
			fields.get(fields.size()-1).setResourceType(ResourceType.NOTHING); //inner field = desert;
			fields.get(fields.size()-1).setDiceIndex(0);
		}
		int[] viewCoord = new int[2];
		for (int i = 0;i <fields.size();i++){
			viewCoord = board.getFieldCoordinates(fields.get(i));
			viewController.setField(viewCoord[0], viewCoord[1], fields.get(i).getResourceType(), fields.get(i).getDiceIndex());
			System.out.println(i+" set field "+ viewCoord[0] + "  " + viewCoord[1]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.controller.GameControllerInterface#gainBoardResources(int)
	 */
	public void gainBoardResources(int diceNum) {
		ArrayList<model.Field> correspondingFields = new ArrayList<model.Field>();
		for (int i = 0; i < fields.length; i++) {
			for (int j = 0; j < fields[i].length; j++) {
				if (fields[i][j] != null) {
					if (fields[i][j].getDiceIndex() == diceNum) {
						correspondingFields.add(fields[i][j]);
					}
				}
			}
		}

		Corner[] neighborCorners;
		enums.CornerStatus status;
		enums.ResourceType resType;
		Field bandit = board.getBandit();
		int[] fieldCoordinates = new int[2];
		for (Field p : correspondingFields) {
			if (p != bandit) {
				fieldCoordinates = board.getFieldCoordinates(p);
				neighborCorners = board.getSurroundingCorners(fieldCoordinates[0],fieldCoordinates[1]);
				resType = p.getResourceType();
				for (Corner o : neighborCorners) {
					status = o.getStatus();
					switch (status) {
					case VILLAGE:
						addToPlayersResource(o.getOwnedByPlayer().getId(), resType, 1);
						break;
					case CITY:
						addToPlayersResource(o.getOwnedByPlayer().getId(), resType, 2);
						break;
					default:
						break;
					}
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.controller.GameControllerInterface#buildVillage(int, int,
	 * int, int)
	 */
	@Override
	public void buildVillage(int x, int y, int dir, int playerId) {
		if (gameLogic.checkBuildVillage(x, y, dir, playerId)) {
			Corner c = board.getCornerAt(x, y, dir);
			c.setStatus(enums.CornerStatus.VILLAGE);
			c.setOwnedByPlayer(playerModels[playerId]);
			playerModels[playerId].decreaseAmountVillages();
			Corner[] neighbors = board.getAdjacentCorners(x, y, dir);
			for (int i = 0; i < neighbors.length; i++) {
				if (neighbors[i] != null){
				neighbors[i].setStatus(enums.CornerStatus.BLOCKED);
				}
			}
			int[] costs = new int[5];
			for (int i = 0; i < costs.length; i++) {
				costs[i] = settings.DefaultSettings.VILLAGE_BUILD_COST[i];
			}
			subFromPlayersResources(playerId, costs);

			viewController.setCorner(x, y, dir, enums.CornerStatus.VILLAGE, playerId);
		}

	}

	@Override
	public void buildStreet(int x, int y, int dir, int playerId) {
		if (gameLogic.checkBuildStreet(x, y, dir, playerId)) {
			Edge e = board.getEdgeAt(x, y, dir);
			e.setHasStreet(true);
			e.setOwnedByPlayer(playerModels[playerId]);
			playerModels[playerId].decreaseAmountStreets();
			int[] costs = new int[5];
			for (int i = 0; i < costs.length; i++) {
				costs[i] = settings.DefaultSettings.STREET_BUILD_COST[i];
			}
			subFromPlayersResources(playerId, costs);

			viewController.setStreet(x, y, dir, playerId);
		}

	}

	@Override
	public void buildCity(int x, int y, int dir, int playerId) {
		if (gameLogic.checkBuildCity(x, y, dir, playerId)) {
			Corner c = board.getCornerAt(x, y, dir);
			c.setStatus(enums.CornerStatus.CITY);
			c.setOwnedByPlayer(playerModels[playerId]);
			playerModels[playerId].increaseAmountVillages();
			playerModels[playerId].decreaseAmountCities();
			int[] costs = new int[5];
			for (int i = 0; i < costs.length; i++) {
				costs[i] = settings.DefaultSettings.CITY_BUILD_COST[i];
			}

			subFromPlayersResources(playerId, costs);

			viewController.setCorner(x, y, dir, enums.CornerStatus.CITY, playerId);
		}

	}

	public void buildInitialStreet(int x, int y, int dir, int playerId) {
		if (gameLogic.checkBuildInitialStreet(x, y, dir, playerId)) {
			Edge e = board.getEdgeAt(x, y, dir);
			e.setHasStreet(true);
			e.setOwnedByPlayer(playerModels[playerId]);
			playerModels[playerId].decreaseAmountStreets();

			viewController.setStreet(x, y, dir, playerId);
		}
	}

	public void buildInitialVillage(int x, int y, int dir, int playerId) {
		if (gameLogic.checkBuildInitialVillage(x, y, dir)) {
			Corner c = board.getCornerAt(x, y, dir);
			c.setStatus(enums.CornerStatus.VILLAGE);
			c.setOwnedByPlayer(playerModels[playerId]);
			playerModels[playerId].decreaseAmountVillages();
			Corner[] neighbors = board.getAdjacentCorners(x, y, dir);
			for (int i = 0; i < neighbors.length; i++) {
				if (neighbors[i] != null){
				neighbors[i].setStatus(enums.CornerStatus.BLOCKED);
				}
			}
			viewController.setCorner(x, y, dir, enums.CornerStatus.VILLAGE, playerId);
		}
	}

	private void addToPlayersResource(int playerId, ResourceType resType, int amount) {
		ArrayList<ResourceType> resourceCards = playerModels[playerId].getResourceCards();
		for (int i = 0; i < amount; i++) {
			resourceCards.add(resType);
		}
		playerModels[playerId].setResourceCards(resourceCards);
	}

	private void subFromPlayersResources(int playerId, int[] costsparam) {
		int[] costs = new int[5];
		for (int i = 0;i<costsparam.length;i++){
			costs[i] = costsparam[i];
		}
		ResourceType currResType;
		ArrayList<ResourceType> list = new ArrayList<ResourceType>();
		list = playerModels[playerId].getResourceCards();
		for (int i = 0; i < costs.length; i++) {
			for (int j = list.size() - 1; j >= 0; j--) { // umkehren wegen
															// remove
				currResType = list.get(j);
				switch (currResType) {
				// Build costs: {WOOD, CLAY, ORE, SHEEP, CORN}
				case WOOD:
					if (costs[0] > 0) {
						list.remove(j);
						costs[0]--;
					}
					break;
				case CLAY:
					if (costs[1] > 0) {
						list.remove(j);
						costs[1]--;
					}
					break;
				case ORE:
					if (costs[2] > 0) {
						list.remove(j);
						costs[2]--;
					}
					break;
				case SHEEP:
					if (costs[3] > 0) {
						list.remove(j);
						costs[3]--;
					}
					break;
				case CORN:
					if (costs[4] > 0) {
						list.remove(j);
						costs[4]--;
					}
					break;
				default:
					break;
				}
			}

		}
		playerModels[playerId].setResourceCards(list);
	}

	@Override
	public void setBandit(int x, int y) {
		if (gameLogic.checkSetBandit(x, y)) {
			board.setBandit(board.getFieldAt(x, y));
			viewController.setBandit(x, y);
			// view.setBandit ?
		}

	}

	/**
	 * basic method for switching the player states 
	 * updates all clients via networkController
	 * @param playerId
	 * @param state
	 */
	@Override
	public void setPlayerState(int playerId,PlayerState state) {
		switch(state){
		case TRADING: //set all other players to offering
		for (int i = 1; i < playerModels.length;i++){
			if (i == playerId){
				playerModels[i].setPlayerState(state);
			} else{
				playerModels[i].setPlayerState(PlayerState.OFFERING);
			}
		}
		case PLAYING: //set all other players waiting
			for (int i = 1; i < playerModels.length;i++){
				if (i == playerId){
					playerModels[i].setPlayerState(state);
				} else{
					playerModels[i].setPlayerState(PlayerState.WAITING);
				}
			}
		default: //else set only player state of playerId
			playerModels[playerId].setPlayerState(state);
		}

		//DEBUG ONLY!
		//viewController.setPlayerState(playerId);
		/*
		 for (int i = 1;i < playerModels.length;i++){
		     networkController.setPlayerState(i,playerModels[i].getPlayerState());
		 }    
		 */
		
	}

	@Override
	public void setGameState() {
		// TODO Auto-generated method stub

	}

}
