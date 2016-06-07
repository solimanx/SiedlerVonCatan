package network.server.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Map;

import enums.Color;
import enums.PlayerState;
import enums.ResourceType;
import javafx.stage.Stage;
import model.Board;
import model.GameLogic;
import model.objects.Corner;
import model.objects.Edge;
import model.objects.Field;
import model.objects.PlayerModel;
import network.client.controller.ViewController;
import network.server.server.Server;
import network.server.server.ServerInputHandler;
import network.server.server.ServerOutputHandler;
import settings.DefaultSettings;

/**
 * Controls the game flow
 *
 * @author NiedlichePixel
 *
 *
 */
public class ServerController {
	private GameLogic gameLogic;
	private ServerOutputHandler serverOutputHandler;
	private ArrayList<PlayerModel> tempPlayers = new ArrayList<PlayerModel>();
	private int amountPlayers = 0;
	private Server server;
	private Map<Integer, Integer> modelPlayerIdMap;
	private Map<Integer, Integer> threadPlayerIdMap;
	private ServerInputHandler serverInputHandler;

	public ServerController() {
		modelPlayerIdMap = new HashMap<Integer, Integer>();
		threadPlayerIdMap = new HashMap<Integer, Integer>();		
		
		ServerInputHandler serverInputHandler = new ServerInputHandler(this);
		this.server = new Server(serverInputHandler);
		this.serverOutputHandler = new ServerOutputHandler(server);
		try {
			server.start();
		} catch (IOException e) {
			// TODO Logging
			e.printStackTrace();
		}

	}

	public void setServerOutputHandler(ServerOutputHandler sNC) {
		this.serverOutputHandler = sNC;

	}

	/**
	 * Inits
	 * 
	 * @param amountPlayers
	 */
	public void initializeBoard() {
		Board board = new Board(tempPlayers);
		this.gameLogic = new GameLogic(board);
		generateDebuggingBoard();
		serverOutputHandler.initBoard(amountPlayers, gameLogic.getBoard());

	}


	public int getAmountPlayers() {
		return amountPlayers;
	}

	public void setAmountPlayers(int amountPlayers) {
		this.amountPlayers = amountPlayers;
	}

	/**
	 * Generates the resource and the dice index of each field calls gui via
	 * setField to set the correct graphics if randomDesert is set then the
	 * desert will be placed random at the board, else it will be set in the
	 * middle
	 *
	 * @param initialField
	 gameLogic.getBoard()ram randomDesert
	 */
	private void generateBoard(Field initialField, boolean randomDesert) {
		ArrayList<Field> fields = gameLogic.getBoard().getAllFields(); // spiral implementieren
		int[] cards = DefaultSettings.LANDSCAPE_CARDS;
		int currNum;
		if (randomDesert) {
			int diceInd = 0;
			for (int i = 0; i < fields.size(); i++) {

				Random r = new Random();
				boolean notFound = true;
				do {
					currNum = r.nextInt(6); // desert allowed
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
					currNum = r.nextInt(5);
					if (cards[currNum] > 0) {
						notFound = false;
					}
				} while (notFound);
				cards[currNum]--;
				fields.get(i).setResourceType(DefaultSettings.RESOURCE_ORDER[currNum]);
				fields.get(i).setDiceIndex(DefaultSettings.DICE_NUMBERS[i]);
			}
			fields.get(fields.size() - 1).setResourceType(ResourceType.NOTHING); // inner
																					// field
																					// =
																					// desert;
			fields.get(fields.size() - 1).setDiceIndex(0);

		}
	}

	// DEBUGGING ONLY
	public void generateDebuggingBoard() {

		for (String key : gameLogic.getBoard().getStringToCoordMap().keySet()) {
			int coords[] = gameLogic.getBoard().getStringToCoordMap().get(key);
			gameLogic.getBoard().getFieldAt(coords[0], coords[1]).setFieldID(key);
			// DEBUG assume all resourcetype is corn
			if (key.matches("[a-z]")) {
				gameLogic.getBoard().getFieldAt(coords[0], coords[1]).setResourceType(ResourceType.SEA);
				gameLogic.getBoard().getFieldAt(coords[0], coords[1]).setDiceIndex(null);
			} else {
				gameLogic.getBoard().getFieldAt(coords[0], coords[1]).setResourceType(ResourceType.CORN);
				// DEBUG assume all dice index is 3
				gameLogic.getBoard().getFieldAt(coords[0], coords[1]).setDiceIndex(3);

			}
		}

		gameLogic.getBoard().setBandit("J");

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.controller.GameControllerInterface#gainBoardResources(int)
	 */
	public void gainBoardResources(int diceNum) {
		int board_size = DefaultSettings.BOARD_SIZE;
		ArrayList<model.objects.Field> correspondingFields = new ArrayList<model.objects.Field>();
		for (int i = 0; i < board_size; i++) {
			for (int j = 0; j < board_size; j++) {
				if (gameLogic.getBoard().getField(i, j) != null) {
					if (gameLogic.getBoard().getField(i, j).getDiceIndex() == diceNum) {
						correspondingFields.add(gameLogic.getBoard().getField(i, j));
					}
				}
			}
		}

		Corner[] neighborCorners;
		enums.CornerStatus status;
		enums.ResourceType resType;
		String bandit = gameLogic.getBoard().getBandit();
		// int[] fieldCoordinates = new int[2];
		// for (Field p : correspondingFields) {
		// if (p != bandit) {
		// fieldCoordinates = board.getFieldCoordinates(p);
		// neighborCorners = board.getSurroundingCorners(fieldCoordinates[0],
		// fieldCoordinates[1]);
		// resType = p.getResourceType();
		// for (Corner o : neighborCorners) {
		// status = o.getStatus();
		// switch (status) {
		// case VILLAGE:
		// addToPlayersResource(o.getOwnedByPlayer().getId(), resType, 1);
		// break;
		// case CITY:
		// addToPlayersResource(o.getOwnedByPlayer().getId(), resType, 2);
		// break;
		// default:
		// break;
		// }
		// }
		// }
		// }

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.controller.GameControllerInterface#buildVillage(int, int,
	 * int, int)
	 */
	
	public void requestBuildVillage(int x, int y, int dir, int playerID) {
		if (gameLogic.checkBuildVillage(x, y, dir, playerID)) {
			Corner c = gameLogic.getBoard().getCornerAt(x, y, dir);
			c.setStatus(enums.CornerStatus.VILLAGE);
			c.setOwnerID(playerID);
			gameLogic.getBoard().getPlayer(playerID).decreaseAmountVillages();
			Corner[] neighbors = gameLogic.getBoard().getAdjacentCorners(x, y, dir);
			for (int i = 0; i < neighbors.length; i++) {
				if (neighbors[i] != null) {
					neighbors[i].setStatus(enums.CornerStatus.BLOCKED);
				}
			}

			subFromPlayersResources(playerID, settings.DefaultSettings.VILLAGE_BUILD_COST);

		    serverOutputHandler.buildVillage(x, y, dir, playerID);
		    statusUpdate(playerID);
		}

	}

	
	public void requestBuildStreet(int x, int y, int dir, int playerID) {
		if (gameLogic.checkBuildStreet(x, y, dir, playerID)) {
			Edge e = gameLogic.getBoard().getEdgeAt(x, y, dir);
			e.setHasStreet(true);
			e.setOwnedByPlayer(gameLogic.getBoard().getPlayer(playerID).getID());
			gameLogic.getBoard().getPlayer(playerID).decreaseAmountStreets();

			subFromPlayersResources(playerID, settings.DefaultSettings.STREET_BUILD_COST);

		    serverOutputHandler.buildStreet(x, y, dir, playerID);
		    statusUpdate(playerID);
		} else {
			serverOutputHandler.error("Kein Straßenbau möglich");
		}

	}

	
	public void requestBuildCity(int x, int y, int dir, int playerID) {
		if (gameLogic.checkBuildCity(x, y, dir, playerID)) {
			Corner c = gameLogic.getBoard().getCornerAt(x, y, dir);
			c.setStatus(enums.CornerStatus.CITY);
			c.setOwnerID(playerID);
			gameLogic.getBoard().getPlayer(playerID).increaseAmountVillages();
			gameLogic.getBoard().getPlayer(playerID).decreaseAmountCities();

			subFromPlayersResources(playerID, settings.DefaultSettings.CITY_BUILD_COST);

		    serverOutputHandler.buildCity(x, y, dir, playerID);
		    statusUpdate(playerID);
		}

	}

	public void requestBuildInitialStreet(int x, int y, int dir, int playerID) {
		if (gameLogic.checkBuildInitialStreet(x, y, dir, playerID)) {
			Edge e = gameLogic.getBoard().getEdgeAt(x, y, dir);
			e.setHasStreet(true);
			e.setOwnedByPlayer(gameLogic.getBoard().getPlayer(playerID).getID());
			gameLogic.getBoard().getPlayer(playerID).decreaseAmountStreets();

			serverOutputHandler.buildStreet(x,y,dir,playerID);
			statusUpdate(playerID);
		}
	}

	public void requestBuildInitialVillage(int x, int y, int dir, int playerID) {
		if (gameLogic.checkBuildInitialVillage(x, y, dir)) {
			Corner c = gameLogic.getBoard().getCornerAt(x, y, dir);
			c.setStatus(enums.CornerStatus.VILLAGE);
			c.setOwnerID(playerID);
			gameLogic.getBoard().getPlayer(playerID).decreaseAmountVillages();
			Corner[] neighbors = gameLogic.getBoard().getAdjacentCorners(x, y, dir);
			for (int i = 0; i < neighbors.length; i++) {
				if (neighbors[i] != null) {
					neighbors[i].setStatus(enums.CornerStatus.BLOCKED);
				}
			}
			serverOutputHandler.buildVillage(x,y,dir,playerID);
			statusUpdate(playerID);
		}
	}
	
	private int[] getPlayerResources(int playerID){
		ArrayList<ResourceType> resources = tempPlayers.get(playerID).getResourceCards();
		int[] result = new int[5];
		for (ResourceType r : resources){
			switch (r) {
			case WOOD:
				result[0]++;
				break;
			case CLAY:
				result[1]++;
				break;
			case ORE:
				result[2]++;
			case SHEEP:
				result[3]++;
			case CORN:
				result[4]++;
			default:
				break;
			}
			
			}
		return result;
	}

	private void addToPlayersResource(int playerID, ResourceType resType, int amount) {
		ArrayList<ResourceType> resourceCards = gameLogic.getBoard().getPlayer(playerID).getResourceCards();
		for (int i = 0; i < amount; i++) {
			resourceCards.add(resType);
		}
		gameLogic.getBoard().getPlayer(playerID).setResourceCards(resourceCards);
	}

	private void subFromPlayersResources(int playerID, int[] costsparam) {
		int[] costs = new int[5];
		for (int i = 0; i < costsparam.length; i++) {
			costs[i] = costsparam[i];
		}
		ResourceType currResType;
		ArrayList<ResourceType> list = new ArrayList<ResourceType>();
		list = gameLogic.getBoard().getPlayer(playerID).getResourceCards();
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
		gameLogic.getBoard().getPlayer(playerID).setResourceCards(list);
	}

	
	public void setBandit(int x, int y, int playerID) {
		if (gameLogic.checkSetBandit(x, y, playerID)) {
			// board.setBandit(board.getFieldAt(x, y));

			//viewController.getMainViewController().setBandit(x, y); // Debug
		}

	}

	/**
	 * basic method for switching the player states updates all clients via
	 * networkController
	 *
	 * @param playerID
	 * @param state
	 */
	
	public void setPlayerState(int playerID, PlayerState state) {
		switch (state) {
		case TRADING: // set all other players to offering
			for (int i = 0; i < gameLogic.getBoard().getAmountPlayers(); i++) {
				if (i == playerID) {
					gameLogic.getBoard().getPlayer(i).setPlayerState(state);
				} else {
					gameLogic.getBoard().getPlayer(i).setPlayerState(PlayerState.OFFERING);
				}
			}
		case PLAYING: // set all other players waiting
			for (int i = 0; i < gameLogic.getBoard().getAmountPlayers(); i++) {
				if (i == playerID) {
					gameLogic.getBoard().getPlayer(i).setPlayerState(state);
				} else {
					gameLogic.getBoard().getPlayer(i).setPlayerState(PlayerState.WAITING);
				}
			}
		default: // else set only player state of playerID
			gameLogic.getBoard().getPlayer(playerID).setPlayerState(state);
		}

		// DEBUG ONLY!
		// viewController.setPlayerState(playerID);
		/*
		 * for (int i = 1;i < playerModels.length;i++){
		 * networkController.setPlayerState(i,playerModels[i].getPlayerState());
		 * }
		 */

	}

	
	public void setGameState() {
		// überflüssig?

	}

	public PlayerState getPlayerState(int playerID) {
		// überflüssig?
		return null;
	}

	public void diceRollRequest(int playerModelID) {
		// TODO Auto-generated method stub

	}

	public void endTurn(int playerModelID) {
		// TODO Auto-generated method stub

	}

	public void requestSetBandit(int x, int y, int stealFromPlayerID, int playerID) {
		// TODO Auto-generated method stub

	}
    //überflüssig?
	public void addPlayerToArray(PlayerModel player) {
		// if (tempPlayers.size() >= DefaultSettings.MAXIMUM_PLAYERS_AMOUNT){
		// networkController.error("Server voll!");
		// } else if (tempPlayers.size() ==
		// DefaultSettings.MAXIMUM_PLAYERS_AMOUNT - 1) {
		// tempPlayers.add(player);
		// amountPlayers++;
		// init(amountPlayers);
		// } else {
		// tempPlayers.add(player);
		// amountPlayers++;
		// }
		tempPlayers.add(player); // DEBUG ONLY
		amountPlayers++; // DEBUG ONLY
		initializeBoard(); // DEBUG ONLY
	}

	public void chatReceiveMessage(int playerId, String s) {
		serverOutputHandler.chatReceiveMessage(playerId, s);
		
	}

	public void chatSendMessage(String s, int currentThreadID) {
		chatReceiveMessage(currentThreadID,s);
		
	}

	public void serverConfirmation(String server_response) {
		//serverOutputHandler.confirmation
		// TODO Auto-generated method stub
		
	}

	public void playerProfileUpdate(Color color, String name, int currentThreadID) {
		int playerID = threadPlayerIdMap.get(currentThreadID);
		PlayerModel playerModel = new PlayerModel(playerID);
		boolean colorAvailable = true;
		for (int i = 0;i< tempPlayers.size();i++){
			if (tempPlayers.get(i).getColor().equals(color)){
				colorAvailable = false;
				break;
			}
		}
		if (colorAvailable){			
		playerModel.setColor(color);
		playerModel.setName(name);
		playerModel.setPlayerState(PlayerState.GAME_SARTING);
		tempPlayers.add(playerModel);
		serverConfirmation("OK");
		serverOutputHandler.statusUpdate(playerID, color, name, PlayerState.GAME_SARTING, 0, new int[5]);
		} else {
			error("Farbe bereits vergeben!");
		}		
	}
	
	public void statusUpdate(int playerID){
		PlayerModel pM = gameLogic.getBoard().getPlayer(playerID);
	    serverOutputHandler.statusUpdate(modelPlayerIdMap.get(playerID), pM.getColor(), pM.getName(), pM.getPlayerState(), pM.getVictoryPoints(), getPlayerResources(playerID));
	}


	private void error(String string) {
		serverOutputHandler.error(string);
		
	}

	public void clientReady(int currentThreadID) {
		int playerID = threadPlayerIdMap.get(currentThreadID);
		for (int i = 0;i< tempPlayers.size();i++){
			if (tempPlayers.get(i).getID() == playerID){
				tempPlayers.get(i).setPlayerState(PlayerState.WAITING_FOR_GAMESTART);
			}
		}
	
		if (tempPlayers.size() >= 3){
			boolean allReady = true;
			for (int i = 0;i< tempPlayers.size();i++){
				if (tempPlayers.get(i).getPlayerState() != PlayerState.WAITING_FOR_GAMESTART){
					allReady = false;
					break;
				}
			}
			if (allReady){
				initializeBoard();
			}
		}
		
	}
	
	public void hello(int currentThreadID){
		serverOutputHandler.hello(DefaultSettings.SERVER_VERSION, DefaultSettings.PROTOCOL_VERSION, currentThreadID);
	}
	
	public void receiveHello(int currentThreadID){
		threadPlayerIdMap.put(currentThreadID, amountPlayers);
		modelPlayerIdMap.put(amountPlayers,currentThreadID);
		amountPlayers++;
		welcome(currentThreadID);
	}

	public void welcome(int currentThreadID) {
		serverOutputHandler.welcome(currentThreadID);		
	}

	public ServerOutputHandler getServerOutputHandler() {
		return serverOutputHandler;
	}

	public ServerInputHandler getServerInputHandler() {
		return serverInputHandler;
	}

	public void robberMovementRequest(int x, int y, int victim_id, int currentThreadID){
		if (gameLogic.checkSetBandit(x, y, victim_id)){
			
		}
		
	}


}
