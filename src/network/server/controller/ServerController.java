package network.server.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Map;

import enums.Color;
import enums.PlayerState;
import enums.ResourceType;
import javafx.stage.Stage;
import model.Board;
import model.GameLogic;
import model.HexService;
import model.Index;
import model.objects.Corner;
import model.objects.Edge;
import model.objects.Field;
import model.objects.PlayerModel;
import network.ProtocolToModel;
import network.client.controller.ViewController;
import network.server.server.Server;
import network.server.server.ServerInputHandler;
import network.server.server.ServerOutputHandler;
import protocol.messaging.ProtocolServerResponse;
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
	private int amountPlayers = 0;
	private Server server;
	private Map<Integer, Integer> modelPlayerIdMap;
	private Map<Integer, Integer> threadPlayerIdMap;
	private ServerInputHandler serverInputHandler;
	private int InitialStreetCounter;
	private ArrayList<Corner> initialVillages = new ArrayList<Corner>();
	private int currentPlayer;
	private int robberLossCounter;
	private TradeController tradeController;
	private int[] playerOrder;
	public int[] resourceStack = { 19, 19, 19, 19, 19 };

	public ServerController() {
		Board board = new Board();
		this.gameLogic = new GameLogic(board);
		this.tradeController = new TradeController(this);
		// ModelPlayerID => threadID
		modelPlayerIdMap = new HashMap<Integer, Integer>();

		// threadID => ModelPlayerID
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

	public void hello(int currentThreadID) {
		serverOutputHandler.hello(DefaultSettings.SERVER_VERSION, DefaultSettings.PROTOCOL_VERSION, currentThreadID);
	}

	public void receiveHello(int currentThreadID) {
		threadPlayerIdMap.put(currentThreadID, amountPlayers);
		modelPlayerIdMap.put(amountPlayers, currentThreadID);
		amountPlayers++;

		int playerID = threadPlayerIdMap.get(currentThreadID);
		gameLogic.getBoard().getPlayer(playerID).setPlayerState(PlayerState.GAME_STARTING);

		welcome(playerID);
		statusUpdate(playerID);
		for (int i = 0; i < amountPlayers; i++) {
			if (i != playerID) {
				statusUpdateToPlayer(playerID, i);
			}
		}

	}

	public void welcome(int modelPlayerID) {
		serverOutputHandler.welcome(modelPlayerIdMap.get(modelPlayerID));
	}

	public void clientReady(int currentThreadID) {
		int playerID = threadPlayerIdMap.get(currentThreadID);
		gameLogic.getBoard().getPlayer(playerID).setPlayerState(PlayerState.WAITING_FOR_GAMESTART);
		statusUpdate(playerID);

		if (amountPlayers >= 3 && amountPlayers == server.getClientCounter()) {
			boolean allReady = true;
			for (int i = 0; i < amountPlayers; i++) {
				if (gameLogic.getBoard().getPlayer(i).getPlayerState() != PlayerState.WAITING_FOR_GAMESTART) {
					allReady = false;
					break;
				}
			}
			if (allReady) {
				initializeBoard();
			}
		}

	}

	public void serverResponse(int modelID, String server_response) {
		serverOutputHandler.serverConfirm(server_response, modelPlayerIdMap.get(modelID));

	}

	private void error(int modelID, String string) {
		serverOutputHandler.error(string, modelPlayerIdMap.get(modelID));

	}

	public void playerProfileUpdate(Color color, String name, int currentThreadID) {

		boolean colorAvailable = true;
		Color currColor;

		for (int i = 0; i < amountPlayers; i++) {
			currColor = gameLogic.getBoard().getPlayer(i).getColor();
			if (currColor != null) {
				if (currColor.equals(color)) {
					colorAvailable = false;
					break;
				}
			}
		}
		int modelID = threadPlayerIdMap.get(currentThreadID);
		if (colorAvailable) {
			PlayerModel pM = gameLogic.getBoard().getPlayer(modelID);
			pM.setColor(color);
			pM.setName(name);
			pM.setPlayerState(PlayerState.GAME_STARTING);
			statusUpdate(modelID);
			serverResponse(modelID, "OK");
		} else {
			error(modelID, "Farbe bereits vergeben!");
		}
	}

	public void chatReceiveMessage(int playerId, String s) {
		serverOutputHandler.chatReceiveMessage(playerId, s);

	}

	public void chatSendMessage(String s, int currentThreadID) {
		chatReceiveMessage(currentThreadID, s);

	}

	public void statusUpdateForAllPlayers() {
		for (int i = 0; i < amountPlayers; i++) {
			statusUpdate(i);
		}
	}

	public void statusUpdate(int playerModelID) {
		for (int i = 0; i < amountPlayers; i++) {
			statusUpdateToPlayer(i, playerModelID);
		}
	}

	public void statusUpdateToPlayer(int sendToPlayer, int playerModelID) {
		PlayerModel pM = gameLogic.getBoard().getPlayer(playerModelID);

		if (sendToPlayer == playerModelID) {
			int[] resources = getPlayerResources(playerModelID);
			serverOutputHandler.statusUpdate(modelPlayerIdMap.get(playerModelID), pM.getColor(), pM.getName(),
					pM.getPlayerState(), pM.getVictoryPoints(), resources, modelPlayerIdMap.get(sendToPlayer));
		} else {
			int[] resources = { gameLogic.getBoard().getPlayer(playerModelID).sumResources() };
			serverOutputHandler.statusUpdate(modelPlayerIdMap.get(playerModelID), pM.getColor(), pM.getName(),
					pM.getPlayerState(), pM.getVictoryPoints(), resources, modelPlayerIdMap.get(sendToPlayer));
		}

	}

	/**
	 * Inits
	 *
	 * @param amountPlayers
	 */
	public void initializeBoard() {
		generateBoard("A", true);
		serverOutputHandler.initBoard(amountPlayers, gameLogic.getBoard());
		int[] firstDiceResults = new int[amountPlayers];
		int[] currDiceRollResult;
		boolean noDuplicates = true;
		ArrayList<Integer> diceRollResults;
		//untauglich; wird wann anders weitergemacht
		/*do { 
			for (int i = 0; i < amountPlayers; i++) {
				currDiceRollResult = rollDice();
				serverOutputHandler.diceRollResult(modelPlayerIdMap.get(i), currDiceRollResult);
				firstDiceResults[i] = currDiceRollResult[0] + currDiceRollResult[1];
			}
			diceRollResults = new ArrayList<Integer>();
			for (int i = 0;i < firstDiceResults.length;i++){
				diceRollResults.add(firstDiceResults[i]);
			}
			//check for duplicates
			for (int i = 0;i<diceRollResults.size();i++){
				for (int j = i+1;j < diceRollResults.size();j++){
					if (diceRollResults.get(i).equals(diceRollResults.get(j))){
						noDuplicates = false;
						break;
					}
				}
			}

		} while (!noDuplicates);
			Collections.sort(diceRollResults);
			for (int i = 0;i < firstDiceResults.length;i++){
				for (int j = 0;j <diceRollResults.size();j++){
					if (firstDiceResults[i] == diceRollResults.get(j)){
						playerOrder[i] = firstDiceResults[j]
					}
				}
			}*/
		this.playerOrder = firstDiceResults;

		gameLogic.getBoard().getPlayer(playerOrder[0]).setPlayerState(PlayerState.BUILDING_VILLAGE);
		statusUpdate(playerOrder[0]); // firstPlayers turn
		currentPlayer = playerOrder[0];
		for (int i = 1; i < amountPlayers; i++) {
			gameLogic.getBoard().getPlayer(playerOrder[i]).setPlayerState(PlayerState.WAITING);
			statusUpdate(playerOrder[i]);
		}
		InitialStreetCounter = 0;

	}
	
	private static int[] insertionSort(int[] sortieren) {
		int temp;
		for (int i = 1; i < sortieren.length; i++) {
			temp = sortieren[i];
			int j = i;
			while (j > 0 && sortieren[j - 1] > temp) {
				sortieren[j] = sortieren[j - 1];
				j--;
			}
			sortieren[j] = temp;
		}
		return sortieren;
	}

	public void diceRollRequest(int threadID) {
		int modelID = threadPlayerIdMap.get(threadID);
		if (gameLogic.isActionForbidden(modelID, currentPlayer, PlayerState.DICEROLLING)) {
			serverResponse(modelID, "Unzulässige Aktion");
		} else {
			int[] result = rollDice();
			serverOutputHandler.diceRollResult(threadID, result);

			PlayerModel pM = gameLogic.getBoard().getPlayer(modelID);
			if (result[0] + result[1] == 7) {
				PlayerModel currPM;
				for (int i = 0; i < amountPlayers; i++) {
					currPM = gameLogic.getBoard().getPlayer(i);
					if (currPM.sumResources() > 7) {
						currPM.setPlayerState(PlayerState.DISPENSE_CARDS_ROBBER_LOSS);
						statusUpdate(i);
						robberLossCounter++;
					}
				}
				if (robberLossCounter > 0) { // continue only if no robber
												// losses
					pM.setPlayerState(PlayerState.WAITING);
					statusUpdate(modelID);
				} else {
					pM.setPlayerState(PlayerState.MOVE_ROBBER);
					statusUpdate(modelID);
				}

			} else {
				gainBoardResources(result[0] + result[1]);
				pM.setPlayerState(PlayerState.TRADING_OR_BUILDING);
				statusUpdate(modelID);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.controller.GameControllerInterface#buildVillage(int, int,
	 * int, int)
	 */

	private int[] rollDice() {
		Random rand = new Random();
		int firstDice = 1 + rand.nextInt(5);
		int secondDice = 1 + rand.nextInt(5);
		int[] result = { firstDice, secondDice };
		return result;
	}

	/**
	 * is called when client wants to build a village if initialBuildingPhase
	 * then jumps to requestBuildInitialVillage
	 * 
	 * @param x
	 * @param y
	 * @param dir
	 * @param threadID
	 */
	public void requestBuildVillage(int x, int y, int dir, int threadID) {
		int modelID = threadPlayerIdMap.get(threadID);
		if (gameLogic.isActionForbidden(modelID, currentPlayer, PlayerState.BUILDING_VILLAGE)) {
			serverResponse(modelID, "Unzulässige Aktion");
		} else {
			if (InitialStreetCounter < amountPlayers * 2) {
				requestBuildInitialVillage(x, y, dir, threadID);
			} else {
				if (gameLogic.checkBuildVillage(x, y, dir, modelID)) {
					Corner c = gameLogic.getBoard().getCornerAt(x, y, dir);
					c.setStatus(enums.CornerStatus.VILLAGE);
					c.setOwnerID(modelID);
					gameLogic.getBoard().getPlayer(modelID).decreaseAmountVillages();
					Corner[] neighbors = gameLogic.getBoard().getAdjacentCorners(x, y, dir);
					for (int i = 0; i < neighbors.length; i++) {
						if (neighbors[i] != null) {
							neighbors[i].setStatus(enums.CornerStatus.BLOCKED);
						}
					}

					subFromPlayersResources(modelID, DefaultSettings.VILLAGE_BUILD_COST);
					resourceStackIncrease(DefaultSettings.VILLAGE_BUILD_COST);

					serverOutputHandler.buildVillage(x, y, dir, threadID);
					serverOutputHandler.costs(threadID, DefaultSettings.VILLAGE_BUILD_COST);
					statusUpdate(modelID);
				}
			}
		}

	}

	/**
	 * builds a street is called by the server controller
	 * 
	 * @param x
	 * @param y
	 * @param dir
	 * @param playerID
	 */
	public void requestBuildStreet(int x, int y, int dir, int threadID) {
		int modelID = threadPlayerIdMap.get(threadID);
		if (gameLogic.isActionForbidden(modelID, currentPlayer, PlayerState.BUILDING_STREET)) {
			serverResponse(modelID, "Unzulässige Aktion");
		} else {
			if (InitialStreetCounter < amountPlayers * 2) {
				requestBuildInitialStreet(x, y, dir, threadID);
			} else {
				if (gameLogic.checkBuildStreet(x, y, dir, modelID)) {
					Edge e = gameLogic.getBoard().getEdgeAt(x, y, dir);
					e.setHasStreet(true);
					e.setOwnedByPlayer(gameLogic.getBoard().getPlayer(modelID).getID());
					gameLogic.getBoard().getPlayer(modelID).decreaseAmountStreets();

					subFromPlayersResources(modelID, DefaultSettings.STREET_BUILD_COST);
					resourceStackIncrease(DefaultSettings.STREET_BUILD_COST);

					serverOutputHandler.buildStreet(x, y, dir, threadID);
					serverOutputHandler.costs(threadID, DefaultSettings.STREET_BUILD_COST);
					statusUpdate(modelID);
					checkLongestTradingRoute(modelID);
				} else {
					error(modelID, "Kein Straßenbau möglich");
				}
			}
		}

	}

	/**
	 * Checks if a player has longest Trading Route
	 * 
	 * @param modelID
	 */
	private void checkLongestTradingRoute(int modelID) {
		// TODO Auto-generated method stub

	}

	/**
	 * is called by the serverController after a build request from a client
	 * builds a city
	 * 
	 * @param x
	 * @param y
	 * @param dir
	 * @param playerID
	 */
	public void requestBuildCity(int x, int y, int dir, int threadID) {
		int modelID = threadPlayerIdMap.get(threadID);
		if (gameLogic.isActionForbidden(modelID, currentPlayer, PlayerState.BUILDING_VILLAGE)) {
			serverResponse(modelID, "Unzulässige Aktion");
		} else {
			if (gameLogic.checkBuildCity(x, y, dir, modelID)) {
				Corner c = gameLogic.getBoard().getCornerAt(x, y, dir);
				c.setStatus(enums.CornerStatus.CITY);
				c.setOwnerID(modelID);
				gameLogic.getBoard().getPlayer(modelID).increaseAmountVillages();
				gameLogic.getBoard().getPlayer(modelID).decreaseAmountCities();

				subFromPlayersResources(modelID, DefaultSettings.CITY_BUILD_COST);
				resourceStackIncrease(DefaultSettings.CITY_BUILD_COST);

				serverOutputHandler.buildCity(x, y, dir, threadID);
				serverOutputHandler.costs(threadID, DefaultSettings.CITY_BUILD_COST);
				statusUpdate(modelID);
			}
		}

	}

	/**
	 * Is called by serverController when there is a street build request during
	 * the initial phase
	 * 
	 * @param x
	 * @param y
	 * @param dir
	 * @param playerID
	 */
	public void requestBuildInitialStreet(int x, int y, int dir, int threadID) {
		int modelID = threadPlayerIdMap.get(threadID);
		if (gameLogic.isActionForbidden(modelID, currentPlayer, PlayerState.BUILDING_STREET)) {
			serverResponse(modelID, "Unzulässige Aktion");
		} else {
			if (gameLogic.checkBuildInitialStreet(x, y, dir, modelID)) {
				Edge e = gameLogic.getBoard().getEdgeAt(x, y, dir);
				e.setHasStreet(true);
				e.setOwnedByPlayer(modelID);
				gameLogic.getBoard().getPlayer(modelID).decreaseAmountStreets();

				serverOutputHandler.buildStreet(x, y, dir, threadID);
				InitialStreetCounter++;
				if (InitialStreetCounter >= amountPlayers * 2) {
					gainFirstBoardResources();
				} else {
					gameLogic.getBoard().getPlayer(modelID).setPlayerState(PlayerState.WAITING);
					statusUpdate(modelID);

					currentPlayer = getNextPlayer(modelID);
					gameLogic.getBoard().getPlayer(currentPlayer).setPlayerState(PlayerState.BUILDING_VILLAGE);
					statusUpdate(currentPlayer);
				}
			}
		}
	}

	/**
	 * is called by serverController when there is a Build Request during the
	 * initial Phase
	 * 
	 * @param x
	 * @param y
	 * @param dir
	 * @param playerID
	 */
	public void requestBuildInitialVillage(int x, int y, int dir, int threadID) {
		int modelID = threadPlayerIdMap.get(threadID);
		if (gameLogic.isActionForbidden(modelID, currentPlayer, PlayerState.BUILDING_VILLAGE)) {
			serverResponse(modelID, "Unzulässige Aktion");
		} else {
			if (gameLogic.checkBuildInitialVillage(x, y, dir)) {
				Corner c = gameLogic.getBoard().getCornerAt(x, y, dir);
				c.setStatus(enums.CornerStatus.VILLAGE);
				c.setOwnerID(modelID);
				gameLogic.getBoard().getPlayer(modelID).decreaseAmountVillages();
				Corner[] neighbors = gameLogic.getBoard().getAdjacentCorners(x, y, dir);
				for (int i = 0; i < neighbors.length; i++) {
					if (neighbors[i] != null) {
						neighbors[i].setStatus(enums.CornerStatus.BLOCKED);
					}
				}
				initialVillages.add(c);
				serverOutputHandler.buildVillage(x, y, dir, threadID);
				gameLogic.getBoard().getPlayer(modelID).setPlayerState(PlayerState.BUILDING_STREET);
				statusUpdate(modelID);
			}
		}
	}

	/**
	 * Is called by View when ownPlayer has finished his turn
	 * 
	 * @param playerID
	 */
	public void endTurn(int playerID) {
		int modelID = threadPlayerIdMap.get(playerID);
		PlayerModel pM = gameLogic.getBoard().getPlayer(modelID);
		pM.setPlayerState(PlayerState.WAITING);
		statusUpdate(modelID);

		currentPlayer = getNextPlayer(modelID); // next players turn
		gameLogic.getBoard().getPlayer(currentPlayer).setPlayerState(PlayerState.DICEROLLING);
		statusUpdate(currentPlayer);

	}

	public void robberLoss(int threadID, int[] resources) {
		int modelID = threadPlayerIdMap.get(threadID);
		int[] playerRes = getPlayerResources(modelID);
		int newSize = 0;
		if (gameLogic.checkPlayerResources(modelID, resources)) {
			for (int i = 0; i < playerRes.length; i++) {
				playerRes[i] = playerRes[i] - resources[i];
				newSize = newSize + playerRes[i];
			}
			if (newSize <= 7) {
				robberLossCounter--;
				subFromPlayersResources(modelID, resources);
				resourceStackIncrease(resources);
				serverOutputHandler.costs(threadID, playerRes);
				if (modelID == currentPlayer && robberLossCounter == 0) {
					gameLogic.getBoard().getPlayer(modelID).setPlayerState(PlayerState.MOVE_ROBBER);
				} else {
					gameLogic.getBoard().getPlayer(modelID).setPlayerState(PlayerState.WAITING);
				}
				statusUpdate(modelID);
			} else {
				error(modelID, "You haven't specified enough resources");
			}
		} else {
			error(modelID, "You haven't the specified resources");
		}

	}

	/**
	 * Sends a robberMovementRequest to all clients
	 * 
	 * @param x
	 * @param y
	 * @param victim_id
	 * @param currentThreadID
	 */
	public void robberMovementRequest(int x, int y, Integer victimThreadID, int currentThreadID) {
		int victimModelID = threadPlayerIdMap.get(victimThreadID);
		if (gameLogic.checkSetBandit(x, y, victimModelID)) {
			if (victimThreadID == null) {
				String location = gameLogic.getBoard().getCoordToStringMap().get(new Index(x, y));
				gameLogic.getBoard().setBandit(location);
				serverOutputHandler.robberMovement(currentThreadID, location, null);
			} else {
				PlayerModel victimPM = gameLogic.getBoard().getPlayer(victimModelID);
				if (victimPM.sumResources() != 0) { // steal a random card
					int[] victimResources = getPlayerResources(victimModelID);
					Random rand = new Random();	
					int stealResource;
					do {
						stealResource = rand.nextInt(4); //random resource
						// while no resource of this type
					} while (victimResources[stealResource] == 0); 
					
					int[] costs = { 0, 0, 0, 0, 0 };
					costs[stealResource] = 1;
					subFromPlayersResources(victimModelID,costs);
					serverOutputHandler.costs(victimThreadID, costs);
					
					int modelID = threadPlayerIdMap.get(currentThreadID);
					addToPlayersResource(modelID, costs);
					serverOutputHandler.resourceObtain(currentThreadID, costs);
					
					String location = gameLogic.getBoard().getCoordToStringMap().get(new Index(x, y));
					gameLogic.getBoard().setBandit(location);
					serverOutputHandler.robberMovement(currentThreadID, location, victimThreadID);
					
					gameLogic.getBoard().getPlayer(modelID).setPlayerState(PlayerState.TRADING_OR_BUILDING);
					statusUpdate(modelID);
				}
			}

		}

	}

	/**
	 * 
	 * Basic methods for trading
	 */

	public void clientOffersTrade(int threadID, int[] supply, int[] demand) {
		tradeController.clientOffersTrade(threadPlayerIdMap.get(threadID), supply, demand);
	}

	public void sendClientOffer(int modelID, int tradingID, int[] supply, int[] demand) {
		serverOutputHandler.protocolTradeIsRequested(modelPlayerIdMap.get(modelID), tradingID, supply, demand);
	}

	public void acceptTrade(int threadID, int tradingID) {
		tradeController.acceptTrade(threadPlayerIdMap.get(threadID), tradingID);
	}

	public void tradeAccepted(int modelID, int tradingID) {
		serverOutputHandler.tradeConfirmation(modelPlayerIdMap.get(modelID), tradingID);
	}

	public void fulfillTrade(int threadID, int tradingID, int partnerThreadID) {
		tradeController.fulfillTrade(threadPlayerIdMap.get(threadID), tradingID,
				threadPlayerIdMap.get(partnerThreadID));
	}

	public void tradeFulfilled(int modelID, int partnerModelID) {
		serverOutputHandler.tradeIsCompleted(modelPlayerIdMap.get(modelID), modelPlayerIdMap.get(partnerModelID));
	}

	public void cancelTrade(int threadID, int tradingID) {
		tradeController.cancelTrade(threadPlayerIdMap.get(threadID), tradingID);
	}

	public void tradeCancelled(int modelID, int tradingID) {
		serverOutputHandler.tradeIsCanceled(modelPlayerIdMap.get(modelID), tradingID);
	}
	
	public void buyDevelopmentCard(int threadID){
		int modelID = threadPlayerIdMap.get(threadID);
		subFromPlayersResources(modelID,DefaultSettings.DEVCARD_BUILD_COST);
		resourceStackIncrease(DefaultSettings.DEVCARD_BUILD_COST);
	}

	/**
	 * Generates the resource and the dice index of each field calls gui via
	 * setField to set the correct graphics if randomDesert is set then the
	 * desert will be placed random at the board, else it will be set in the
	 * middle
	 *
	 * @param initialField
	 *            gameLogic.getBoard()ram randomDesert
	 */
	private void generateBoard(String initialField, boolean randomDesert) {
		String fields = HexService.getSpiral(initialField);
		Board currBoard = gameLogic.getBoard();
		int[] cards = DefaultSettings.LANDSCAPE_CARDS;
		int currNum;
		if (randomDesert) {
			int diceInd = 0;
			for (int i = 0; i < fields.length(); i++) {

				Random r = new Random();
				boolean notFound = true;
				do {
					currNum = r.nextInt(6); // desert allowed
					if (cards[currNum] > 0) {
						notFound = false;
					}
				} while (notFound);
				cards[currNum]--;
				int[] coords = ProtocolToModel.getFieldCoordinates("" + fields.charAt(i));
				currBoard.getFieldAt(coords[0], coords[1]).setFieldID("" + fields.charAt(i));
				if (currNum != 5) {
					currBoard.setFieldAt(coords[0], coords[1], DefaultSettings.RESOURCE_ORDER[currNum],
							DefaultSettings.DICE_NUMBERS[diceInd]);
					diceInd++;
				} else {
					currBoard.setFieldAt(coords[0], coords[1], DefaultSettings.RESOURCE_ORDER[currNum], null);
				}
			}
		} else {
			for (int i = 0; i < fields.length() - 1; i++) {
				Random r = new Random();
				boolean notFound = true;
				do {
					currNum = r.nextInt(5);
					if (cards[currNum] > 0) {
						notFound = false;
					}
				} while (notFound);
				cards[currNum]--;
				int[] coords = ProtocolToModel.getFieldCoordinates("" + fields.charAt(i));
				currBoard.getFieldAt(coords[0], coords[1]).setFieldID("" + fields.charAt(i));
				currBoard.setFieldAt(coords[0], coords[1], DefaultSettings.RESOURCE_ORDER[currNum],
						DefaultSettings.DICE_NUMBERS[i]);
			}
			int[] coords = ProtocolToModel.getFieldCoordinates("" + fields.charAt(fields.length() - 1));
			currBoard.getFieldAt(coords[0], coords[1]).setFieldID("" + fields.charAt(fields.length() - 1));
			currBoard.setFieldAt(coords[0], coords[1], ResourceType.NOTHING, null);

		}
		String outerRing = currBoard.getOuterRing();
		for (int i = 0; i < outerRing.length(); i++) {
			int[] coords = ProtocolToModel.getFieldCoordinates("" + outerRing.charAt(i));
			currBoard.getFieldAt(coords[0], coords[1]).setFieldID("" + outerRing.charAt(i));
			currBoard.setFieldAt(coords[0], coords[1], ResourceType.SEA, null);
		}
		currBoard.setBandit("J");
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
		ArrayList<Field> diceFields = new ArrayList<Field>();
		for (Map.Entry<String, int[]> entry : gameLogic.getBoard().getStringToCoordMap().entrySet()) {
			int[] coord = entry.getValue();
			Field f = gameLogic.getBoard().getFieldAt(coord[0], coord[1]);
			if (f.getDiceIndex() == diceNum) {
				diceFields.add(f);
			}
		}
		int[] coords;
		Corner[] neighbors;
		ResourceType currResType;

		for (Field f : diceFields) {
			if (f.getFieldID() != gameLogic.getBoard().getBandit()) {
				coords = gameLogic.getBoard().getFieldCoordinates(f.getFieldID());
				neighbors = gameLogic.getBoard().getSurroundingCorners(coords[0], coords[1]);
				for (int i = 0; i < neighbors.length; i++) {
					switch (neighbors[i].getStatus()) {
					case VILLAGE:
						currResType = f.getResourceType();
						if (resourceStackDecrease(currResType)){
							addToPlayersResource(neighbors[i].getOwnerID(), currResType, 1);
						}						
						break;
					case CITY:
						currResType = f.getResourceType();
						for (int j = 0;j < 2;j++){
							if (resourceStackDecrease(currResType)){
								addToPlayersResource(neighbors[i].getOwnerID(), f.getResourceType(), 2);
							}
						}					
						break;
					default:
					}
				}
			}
		}
		statusUpdateForAllPlayers();
	}

	private boolean resourceStackDecrease(ResourceType resType) {
		int resIndex = DefaultSettings.RESOURCE_VALUES.get(resType);
		if (resourceStack[resIndex] > 0) {
			resourceStack[resIndex]--;
		} else {
			return false;
		}
		return true;
	}

	private void resourceStackIncrease(ResourceType resType) {
		int resIndex = DefaultSettings.RESOURCE_VALUES.get(resType);
		resourceStack[resIndex] = resourceStack[resIndex]++;
	}
	
	private void resourceStackIncrease(int[] resources){
		for (int i = 0;i < resources.length;i++){
			for (int j = 0; j < resources[i]; j++) {
				resourceStackIncrease(DefaultSettings.RESOURCE_ORDER[i]);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.controller.GameControllerInterface#buildVillage(int, int,
	 * int, int)
	 */

	private void gainFirstBoardResources() {
		int[] coords; // wegen performance nur einmaliges Initialisieren
		Field[] connFields;
		ResourceType currResType;
		for (int i = 0; i < amountPlayers * 2; i++) {
			coords = ProtocolToModel.getCornerCoordinates(initialVillages.get(i).getCornerID());
			connFields = gameLogic.getBoard().getConnectedFields(coords[0], coords[1], coords[2]);
			for (int j = 0; j < connFields.length; j++) {
				currResType = connFields[j].getResourceType();
				if (resourceStackDecrease(currResType)) {
					addToPlayersResource(initialVillages.get(i).getOwnerID(), currResType, 1);
				} else {
					System.out.println("Stapel leer: " + currResType);
				}

			}
		}
		statusUpdateForAllPlayers();
	}

	private int getNextPlayer(int modelPlayerID) {
		for (int i = 0; i < playerOrder.length - 1; i++) {
			if (playerOrder[i] == modelPlayerID) {
				return playerOrder[i + 1];
			}
		}
		return playerOrder[0];
	}

	private int[] getPlayerResources(int modelPlayerID) {
		return gameLogic.getBoard().getPlayer(modelPlayerID).getResources();
	}

	private void setPlayerResources(int modelID, int[] resources) {
		gameLogic.getBoard().getPlayer(modelID).setResources(resources);
	}

	public void addToPlayersResource(int playerID, ResourceType resType, int amount) {
		int[] resources = getPlayerResources(playerID);
		for (int i = 0; i < amount; i++) {
			resources[DefaultSettings.RESOURCE_VALUES.get(resType)]++;
		}
		gameLogic.getBoard().getPlayer(playerID).setResources(resources);
	}

	public void addToPlayersResource(int playerID, int[] resourcesToAdd) {
		int[] resources = getPlayerResources(playerID);
		for (int i = 0; i < resourcesToAdd.length; i++) {
			for (int j = 0; j < resourcesToAdd[i]; j++) {
				resources[i]++;
			}
		}
		gameLogic.getBoard().getPlayer(playerID).setResources(resources);
	}

	public void subFromPlayersResources(int playerID, int[] costsparam) {
		int[] costs = new int[5];
		for (int i = 0; i < costsparam.length; i++) { // copy array
			costs[i] = costsparam[i];
		}
		int[] pResources = getPlayerResources(playerID);
		for (int i = 0; i < costs.length; i++) {
			pResources[i] = pResources[i] - costs[i];

		}
		gameLogic.getBoard().getPlayer(playerID).setResources(pResources);
	}

	public int getAmountPlayers() {
		return amountPlayers;
	}

	public void setAmountPlayers(int amountPlayers) {
		this.amountPlayers = amountPlayers;
	}

	public ServerOutputHandler getServerOutputHandler() {
		return serverOutputHandler;
	}

	public ServerInputHandler getServerInputHandler() {
		return serverInputHandler;
	}

}
