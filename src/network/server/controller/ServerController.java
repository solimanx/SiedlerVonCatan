package network.server.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import model.objects.DevCards.DevCardFactory;
import model.objects.DevCards.DevelopmentCard;
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
	private Board board;
	public int[] resourceStack = { 19, 19, 19, 19, 19 };
	private static Logger logger = LogManager.getLogger(ServerController.class.getName());
	private int lengthLongestTradeRoute;

	public ServerController() {
		board = new Board();
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
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	public void setServerOutputHandler(ServerOutputHandler sNC) {
		this.serverOutputHandler = sNC;

	}

	/**
	 * sends a hello message at server start
	 * 
	 * @param currentThreadID
	 */
	public void hello(int currentThreadID) {
		serverOutputHandler.hello(DefaultSettings.SERVER_VERSION, DefaultSettings.PROTOCOL_VERSION, currentThreadID);
	}

	/**
	 * is called when a client sends hello message registers threadID and sends
	 * status update
	 * 
	 * @param currentThreadID
	 */
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

	/**
	 * sends a welcome message to client
	 * 
	 * @param modelPlayerID
	 */
	public void welcome(int modelPlayerID) {
		serverOutputHandler.welcome(modelPlayerIdMap.get(modelPlayerID));
	}

	/**
	 * is called when a client is ready, if all clients are ready then start
	 * game
	 * 
	 * @param currentThreadID
	 */
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

	/**
	 * sends server response to specified client
	 * 
	 * @param modelID
	 * @param server_response
	 */
	public void serverResponse(int modelID, String server_response) {
		serverOutputHandler.serverConfirm(server_response, modelPlayerIdMap.get(modelID));

	}

	/**
	 * sends an error to specified client
	 * 
	 * @param modelID
	 * @param string
	 */
	private void error(int modelID, String string) {
		serverOutputHandler.error(string, modelPlayerIdMap.get(modelID));

	}

	/**
	 * is called when client sets own name and color before gamestart
	 * 
	 * @param color
	 * @param name
	 * @param currentThreadID
	 */
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

	/**
	 * sends a received chat message to all clients
	 * 
	 * @param playerId
	 * @param chatMessage
	 */
	public void chatReceiveMessage(int playerId, String s) {
		serverOutputHandler.chatReceiveMessage(playerId, s);

	}

	/**
	 * is called when client sends a chat message
	 * 
	 * @param chatMessage
	 * @param currentThreadID
	 */
	public void chatSendMessage(String s, int currentThreadID) {
		serverOutputHandler.serverConfirm("OK", currentThreadID);
		chatReceiveMessage(currentThreadID, s);

	}

	/**
	 * sends status update of all players to all clients
	 */
	public void statusUpdateForAllPlayers() {
		for (int i = 0; i < amountPlayers; i++) {
			statusUpdate(i);
		}
	}

	/**
	 * sends status update of specified player to all clients
	 * 
	 * @param playerModelID
	 */
	public void statusUpdate(int playerModelID) {
		for (int i = 0; i < amountPlayers; i++) {
			statusUpdateToPlayer(i, playerModelID);
		}
	}

	/**
	 * sends status update of a player to specified client
	 * 
	 * @param sendToPlayer
	 * @param playerModelID
	 */
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
	 * starts the game: generates the board generates player order sets first
	 * player to play
	 */
	public void initializeBoard() {
		generateBoard("A", true);
		serverOutputHandler.initBoard(amountPlayers, gameLogic.getBoard());
		int[] currDiceRollResult;
		boolean noDuplicates;
		Map<Integer, Integer> diceResults = new HashMap<Integer, Integer>();
		this.playerOrder = new int[amountPlayers];
		do {
			noDuplicates = true;
			for (int i = 0; i < amountPlayers; i++) {
				currDiceRollResult = rollDice();
				serverOutputHandler.diceRollResult(modelPlayerIdMap.get(i), currDiceRollResult);
				diceResults.put(currDiceRollResult[0] + currDiceRollResult[1], i);
			}
			if (diceResults.size() != amountPlayers) {
				diceResults.clear();
				noDuplicates = false;
			}
			;
		} while (!noDuplicates);
		SortedSet<Integer> keys = new TreeSet<Integer>(diceResults.keySet());
		int index = amountPlayers - 1;
		for (Integer key : keys) {
			Integer value = diceResults.get(key);
			playerOrder[index] = value;
			index--;
		}

		gameLogic.getBoard().getPlayer(playerOrder[0]).setPlayerState(PlayerState.BUILDING_VILLAGE);
		statusUpdate(playerOrder[0]); // firstPlayers turn
		currentPlayer = playerOrder[0];
		for (int i = 1; i < amountPlayers; i++) {
			gameLogic.getBoard().getPlayer(playerOrder[i]).setPlayerState(PlayerState.WAITING);
			statusUpdate(playerOrder[i]);
		}
		InitialStreetCounter = 0;

	}

	/**
	 * dice roll request from client
	 * 
	 * @param threadID
	 */
	public void diceRollRequest(int threadID) {
		int modelID = threadPlayerIdMap.get(threadID);
		if (gameLogic.isActionForbidden(modelID, currentPlayer, PlayerState.DICEROLLING)) {
			serverResponse(modelID, "Unzulässige Aktion");
		} else {
			int[] result = rollDice();
			serverOutputHandler.diceRollResult(threadID, result);

			PlayerModel pM = gameLogic.getBoard().getPlayer(modelID);
			if (result[0] + result[1] == 7) {
				pM.setPlayerState(PlayerState.WAITING);
				statusUpdate(modelID);
				PlayerModel currPM;
				for (int i = 0; i < amountPlayers; i++) {
					currPM = gameLogic.getBoard().getPlayer(i);
					if (currPM.sumResources() > 7) {
						currPM.setPlayerState(PlayerState.DISPENSE_CARDS_ROBBER_LOSS);
						statusUpdate(i);
						robberLossCounter++;
					}
				}
				if (robberLossCounter == 0) { // continue only if no robber
												// losses
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

	/**
	 * generates a random dice result
	 * 
	 * @return integer array [2] values
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
		if (InitialStreetCounter < amountPlayers * 2) {
			requestBuildInitialVillage(x, y, dir, threadID);
		} else {
			if (gameLogic.isActionForbidden(modelID, currentPlayer, PlayerState.TRADING_OR_BUILDING)) {
				serverResponse(modelID, "Unzulässige Aktion");
			} else {
				if (gameLogic.checkBuildVillage(x, y, dir, modelID)) {

					serverResponse(modelID, "OK");
					buildVillage(x, y, dir, modelID);
					subFromPlayersResources(modelID, DefaultSettings.VILLAGE_BUILD_COST);
					resourceStackIncrease(DefaultSettings.VILLAGE_BUILD_COST);
					increaseVictoryPoints(modelID);

					serverOutputHandler.buildVillage(x, y, dir, threadID);
					serverOutputHandler.costs(threadID, DefaultSettings.VILLAGE_BUILD_COST);
					statusUpdate(modelID);
				}
			}
		}

	}

	private Corner buildVillage(int x, int y, int dir, int modelID) {
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
		return c;

	}

	private void increaseVictoryPoints(int modelID) {
		int points = gameLogic.getBoard().getPlayer(modelID).getVictoryPoints();
		gameLogic.getBoard().getPlayer(modelID).setVictoryPoints(points + 1);
		if (points + 1 == DefaultSettings.MAX_VICTORY_POINTS) {
			victory(modelID);
		}
	}

	private void victory(int modelID) {
		PlayerModel pM = gameLogic.getBoard().getPlayer(modelID);
		serverOutputHandler.victory("Spieler " + pM.getName() + " hat das Spiel gewonnen.",
				modelPlayerIdMap.get(modelID));
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

		if (InitialStreetCounter < amountPlayers * 2) {
			requestBuildInitialStreet(x, y, dir, threadID);
		} else {
			if (gameLogic.isActionForbidden(modelID, currentPlayer, PlayerState.TRADING_OR_BUILDING)) {
				serverResponse(modelID, "Unzulässige Aktion");
			} else {
				if (gameLogic.checkBuildStreet(x, y, dir, modelID)) {
					serverResponse(modelID, "OK");
					buildStreet(x, y, dir, modelID);
					subFromPlayersResources(modelID, DefaultSettings.STREET_BUILD_COST);
					resourceStackIncrease(DefaultSettings.STREET_BUILD_COST);
					serverOutputHandler.buildStreet(x, y, dir, threadID);
					serverOutputHandler.costs(threadID, DefaultSettings.STREET_BUILD_COST);
					statusUpdate(modelID);

				} else {
					error(modelID, "Kein Straßenbau möglich");
				}
			}
		}

	}

	private void buildStreet(int x, int y, int dir, int modelID) {
		Edge e = gameLogic.getBoard().getEdgeAt(x, y, dir);
		e.setHasStreet(true);
		e.setOwnedByPlayer(gameLogic.getBoard().getPlayer(modelID).getID());
		gameLogic.getBoard().getPlayer(modelID).decreaseAmountStreets();

		checkLongestTradingRoute(modelID, x, y, dir);
	}

	/**
	 * Checks if a player has longest Trading Route, which contains 5 or more
	 * streets and sets it (for all players)
	 * 
	 * @param modelID
	 * @param aX
	 * @param aY
	 * @param dir
	 */
	private void checkLongestTradingRoute(int modelID, int aX, int aY, int dir) {
/*		ArrayList<Edge> alreadyChecked = new ArrayList<Edge>();
		int longestRoute = LongestTradingRoute(modelID, aX, aY, dir, alreadyChecked);
		if (longestRoute > lengthLongestTradeRoute && longestRoute > 4) {
			board.getPlayer(0).setHasLongestRoad(false);
			board.getPlayer(1).setHasLongestRoad(false);
			board.getPlayer(2).setHasLongestRoad(false);
			board.getPlayer(3).setHasLongestRoad(false);
			board.getPlayer(modelID).setHasLongestRoad(true);
			serverOutputHandler.longestRoad(modelID);
			lengthLongestTradeRoute = longestRoute;
		}*/
	}

	/**
	 * recursive method, which calculates the longest possible rout from the
	 * given street (edge)
	 * 
	 * @param modelID
	 * @param aX
	 *            edge x-coordinate
	 * @param aY
	 *            edge y-coordinate
	 * @param dir
	 *            edge direction
	 * @param alreadyChecked
	 *            list of already calculated streets
	 * @return length of the longest possible road
	 */
/*	// TODO verbindung von zwei straßensystemen
	private int LongestTradingRoute(int modelID, int aX, int aY, int dir, ArrayList<Edge> alreadyChecked) {
		int a = 0;
		int b = 0;
		int c = 0;
		int d = 0;
		int[] coord = new int[3];
		int[] fieldOneCoords = new int[2];
		int[] fieldTwoCoords = new int[2];
		String fieldOne;
		String fieldTwo;
		ArrayList<Edge> ac = new ArrayList<Edge>();
		Edge[] neighbours = board.getLinkedEdges(aX, aY, dir);
		
		ArrayList<Edge> notToCheck = new ArrayList<Edge>();
		for(int i = 0; i<lastNeighbours.length; i++){
			for(int j = 0; j< neighbours.length; j++){
				if(lastNeighbours[i].equals(neighbours[j])){
					notToCheck.add(neighbours[j]);
				}
			}
		}
		
		if (neighbours.length > 0) {
			if (neighbours[0].isHasStreet()) {
				if (neighbours[0].getOwnerID() == modelID) {
					if (!alreadyChecked.contains(neighbours[0])) {
		if(neighbours.length > 0){
			if(neighbours[0].isHasStreet() && !notToCheck.contains(neighbours[0])){
				if(neighbours[0].getOwnerID() == modelID){
					if(!alreadyChecked.contains(neighbours[0])){
						fieldOne = neighbours[0].getEdgeID().substring(0, 1);
						fieldTwo = neighbours[0].getEdgeID().substring(1, 2);
						fieldOneCoords = Board.getStringToCoordMap().get(fieldOne);
						fieldTwoCoords = Board.getStringToCoordMap().get(fieldTwo);
						coord = HexService.getEdgeCoordinates(fieldOneCoords[0], fieldOneCoords[1], fieldTwoCoords[0],
								fieldTwoCoords[1]);
						ac = alreadyChecked;
						ac.add(neighbours[0]);
						a = 1 + LongestTradingRoute(modelID, coord[0], coord[1], coord[2], ac, neighbours);
					}
				}
			}

		}
		if (neighbours.length > 1) {
			if (neighbours[1].isHasStreet()) {
				if (neighbours[1].getOwnerID() == modelID) {
					if (!alreadyChecked.contains(neighbours[1])) {
						fieldOne = neighbours[1].getEdgeID().substring(0, 1);
						fieldTwo = neighbours[1].getEdgeID().substring(1, 2);
						fieldOneCoords = Board.getStringToCoordMap().get(fieldOne);
						fieldTwoCoords = Board.getStringToCoordMap().get(fieldTwo);
						coord = HexService.getEdgeCoordinates(fieldOneCoords[0], fieldOneCoords[1], fieldTwoCoords[0],
								fieldTwoCoords[1]);
						ac = alreadyChecked;
						ac.add(neighbours[1]);
						b = 1 + LongestTradingRoute(modelID, coord[0], coord[1], coord[2], ac);
			if(neighbours.length > 1){
				if(neighbours[1].isHasStreet() && !notToCheck.contains(neighbours[1])){
					if(neighbours[1].getOwnerID() == modelID){
						if(!alreadyChecked.contains(neighbours[1])){
							fieldOne = neighbours[1].getEdgeID().substring(0, 1);
							fieldTwo = neighbours[1].getEdgeID().substring(1, 2);
							fieldOneCoords = Board.getStringToCoordMap().get(fieldOne);
							fieldTwoCoords = Board.getStringToCoordMap().get(fieldTwo);
							coord = HexService.getEdgeCoordinates(fieldOneCoords[0], fieldOneCoords[1], fieldTwoCoords[0], fieldTwoCoords[1]);
							ac = alreadyChecked;
							ac.add(neighbours[1]);
							b = 1 + LongestTradingRoute(modelID, coord[0], coord[1], coord[2], ac, neighbours);
						}
					}
				}
			}
		}
		if (neighbours.length > 2) {
			if (neighbours[2].isHasStreet()) {
				if (neighbours[2].getOwnerID() == modelID) {
					if (!alreadyChecked.contains(neighbours[2])) {
						fieldOne = neighbours[2].getEdgeID().substring(0, 1);
						fieldTwo = neighbours[2].getEdgeID().substring(1, 2);
						fieldOneCoords = Board.getStringToCoordMap().get(fieldOne);
						fieldTwoCoords = Board.getStringToCoordMap().get(fieldTwo);
						coord = HexService.getEdgeCoordinates(fieldOneCoords[0], fieldOneCoords[1], fieldTwoCoords[0],
								fieldTwoCoords[1]);
						ac = alreadyChecked;
						ac.add(neighbours[2]);
						c = 1 + LongestTradingRoute(modelID, coord[0], coord[1], coord[2], ac);
			if(neighbours.length > 2){
				if(neighbours[2].isHasStreet() && !notToCheck.contains(neighbours[2])){
					if(neighbours[2].getOwnerID() == modelID){
						if(!alreadyChecked.contains(neighbours[2])){
							fieldOne = neighbours[2].getEdgeID().substring(0, 1);
							fieldTwo = neighbours[2].getEdgeID().substring(1, 2);
							fieldOneCoords = Board.getStringToCoordMap().get(fieldOne);
							fieldTwoCoords = Board.getStringToCoordMap().get(fieldTwo);
							coord = HexService.getEdgeCoordinates(fieldOneCoords[0], fieldOneCoords[1], fieldTwoCoords[0], fieldTwoCoords[1]);
							ac = alreadyChecked;
							ac.add(neighbours[2]);
							c = 1 + LongestTradingRoute(modelID, coord[0], coord[1], coord[2], ac, neighbours);
						}
					}
				}
			}
		}
		if (neighbours.length > 3) {
			if (neighbours[3].isHasStreet()) {
				if (neighbours[3].getOwnerID() == modelID) {
					if (!alreadyChecked.contains(neighbours[3])) {
						fieldOne = neighbours[3].getEdgeID().substring(0, 1);
						fieldTwo = neighbours[3].getEdgeID().substring(1, 2);
						fieldOneCoords = Board.getStringToCoordMap().get(fieldOne);
						fieldTwoCoords = Board.getStringToCoordMap().get(fieldTwo);
						coord = HexService.getEdgeCoordinates(fieldOneCoords[0], fieldOneCoords[1], fieldTwoCoords[0],
								fieldTwoCoords[1]);
						ac = alreadyChecked;
						ac.add(neighbours[3]);
						d = 1 + LongestTradingRoute(modelID, coord[0], coord[1], coord[2], ac);
					}
			if(neighbours.length > 3){
				if(neighbours[3].isHasStreet() && !notToCheck.contains(neighbours[3])){
					if(neighbours[3].getOwnerID() == modelID){
						if(!alreadyChecked.contains(neighbours[3])){
							fieldOne = neighbours[3].getEdgeID().substring(0, 1);
							fieldTwo = neighbours[3].getEdgeID().substring(1, 2);
							fieldOneCoords = Board.getStringToCoordMap().get(fieldOne);
							fieldTwoCoords = Board.getStringToCoordMap().get(fieldTwo);
							coord = HexService.getEdgeCoordinates(fieldOneCoords[0], fieldOneCoords[1], fieldTwoCoords[0], fieldTwoCoords[1]);
							ac = alreadyChecked;
							ac.add(neighbours[3]);
							d = 1 + LongestTradingRoute(modelID, coord[0], coord[1], coord[2], ac, neighbours);
						}
					}		
				}
			}
		}
		int result;
		if (a < b) {
			result = b;
		} else {
			result = a;
		}
		if (c > result) {
			result = c;
		}
		if (d > result) {
			result = d;
		}
		return result;
	}
*/
	/**
	 * checks if the player has the most played knight cards
	 * 
	 * @param modelID
	 *            to chaeck player
	 */
	private void checkLargestArmy(int modelID) {
		PlayerModel pM = gameLogic.getBoard().getPlayer(modelID);
		int amountCards = pM.getPlayedKnightCards();
		boolean hasLargestArmy = true;
		if (pM.getPlayedKnightCards() >= 3) {
			for (int i = 0; i < amountPlayers; i++) {
				if (i != modelID) {
					if (amountCards <= gameLogic.getBoard().getPlayer(i).getPlayedKnightCards()) {
						hasLargestArmy = false;
					}
				}
			}
			if (hasLargestArmy) {
				pM.setHasLargestArmy(true);
				serverOutputHandler.biggestKnightProwess(modelID);
			}
		}
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
		if (gameLogic.isActionForbidden(modelID, currentPlayer, PlayerState.TRADING_OR_BUILDING)) {
			serverResponse(modelID, "Unzulässige Aktion");
		} else {
			if (gameLogic.checkBuildCity(x, y, dir, modelID)) {

				serverResponse(modelID, "OK");
				buildCity(x, y, dir, modelID);

				subFromPlayersResources(modelID, DefaultSettings.CITY_BUILD_COST);
				resourceStackIncrease(DefaultSettings.CITY_BUILD_COST);
				increaseVictoryPoints(modelID);

				serverOutputHandler.buildCity(x, y, dir, threadID);
				serverOutputHandler.costs(threadID, DefaultSettings.CITY_BUILD_COST);
				statusUpdate(modelID);
			}
		}

	}

	private void buildCity(int x, int y, int dir, int modelID) {
		Corner c = gameLogic.getBoard().getCornerAt(x, y, dir);
		c.setStatus(enums.CornerStatus.CITY);
		c.setOwnerID(modelID);
		gameLogic.getBoard().getPlayer(modelID).increaseAmountVillages();
		gameLogic.getBoard().getPlayer(modelID).decreaseAmountCities();
	}

	/**
	 * 
	 * @param threadID
	 */
	public void requestBuyDevCard(int threadID) {
		int modelID = threadPlayerIdMap.get(threadID);
		if (!gameLogic.isActionForbidden(modelID, currentPlayer, PlayerState.TRADING_OR_BUILDING)) {
			if (gameLogic.checkBuyDevCard(modelID)) {
				PlayerModel pm = gameLogic.getBoard().getPlayer(modelID);
				subFromPlayersResources(modelID, DefaultSettings.DEVCARD_BUILD_COST);
				// TODO: costs to all
				serverOutputHandler.costs(threadID, DefaultSettings.DEVCARD_BUILD_COST);
				DevelopmentCard devCard = board.getDevCardStack().getNextCard();
				pm.getDevCardsBoughtInThisRound().add(devCard);

				serverOutputHandler.boughtDevelopmentCard(threadID, devCard);

			} else {
				serverResponse(modelID, "Unzulässige Aktion");
			}
		} else {
			serverResponse(modelID, "Unzulässige Aktion");
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
				serverResponse(modelID, "OK");
				buildStreet(x, y, dir, modelID);

				serverOutputHandler.buildStreet(x, y, dir, threadID);

				InitialStreetCounter++;
				PlayerModel currPM = gameLogic.getBoard().getPlayer(currentPlayer);
				if (InitialStreetCounter >= amountPlayers * 2) {
					// initial building phase finished
					gainFirstBoardResources();
					currPM.setPlayerState(PlayerState.DICEROLLING);
					statusUpdate(currentPlayer);
				} else if (InitialStreetCounter == amountPlayers) {
					// no change
					currPM.setPlayerState(PlayerState.BUILDING_VILLAGE);
					statusUpdate(currentPlayer);
				} else if (InitialStreetCounter > amountPlayers) {
					// go backwards
					currPM.setPlayerState(PlayerState.WAITING);
					statusUpdate(currentPlayer);
					// getPreviousPlayer
					for (int i = 0; i < amountPlayers; i++) {
						if (playerOrder[i] == currentPlayer) {
							currentPlayer = playerOrder[i - 1];
						}
					}

					gameLogic.getBoard().getPlayer(currentPlayer).setPlayerState(PlayerState.BUILDING_VILLAGE);
					statusUpdate(currentPlayer);
				} else {
					// go forward
					currPM.setPlayerState(PlayerState.WAITING);
					statusUpdate(currentPlayer);

					currentPlayer = getNextPlayer(currentPlayer);
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
				serverResponse(modelID, "OK");
				Corner c = buildVillage(x, y, dir, modelID);
				initialVillages.add(c);

				increaseVictoryPoints(modelID);
				serverOutputHandler.buildVillage(x, y, dir, threadID);
				gameLogic.getBoard().getPlayer(modelID).setPlayerState(PlayerState.BUILDING_STREET);
				statusUpdate(modelID);
			}
		}
	}

	/**
	 * is called when client has finished his turn
	 * 
	 * @param playerID
	 */
	public void endTurn(int playerID) {
		int modelID = threadPlayerIdMap.get(playerID);
		PlayerModel pM = gameLogic.getBoard().getPlayer(modelID);
		pM.setPlayerState(PlayerState.WAITING);
		ArrayList<DevelopmentCard> currDevCards = pM.getDevCardsBoughtInThisRound();
		for (int i = 0; i < currDevCards.size(); i++) {
			// erst jetzt kann spieler über development card verfügen
			pM.incrementPlayerDevCard(currDevCards.get(i));
		}
		// runde zu ende, nächste runde darf dev card gespielt werden
		pM.setHasPlayedDevCard(false);
		statusUpdate(modelID);

		currentPlayer = getNextPlayer(modelID); // next players turn
		gameLogic.getBoard().getPlayer(currentPlayer).setPlayerState(PlayerState.DICEROLLING);
		statusUpdate(currentPlayer);

	}

	/**
	 * is called when client sends a robber loss message checks if action is
	 * valid sends status updates
	 * 
	 * @param threadID
	 * @param resources
	 */
	public void robberLoss(int threadID, int[] resources) {
		int modelID = threadPlayerIdMap.get(threadID);
		int[] playerRes = getPlayerResources(modelID);
		int newSize = 0;
		int sizeToReach = (gameLogic.getBoard().getPlayer(modelID).sumResources() + 1) / 2;
		if (gameLogic.checkPlayerResources(modelID, resources)) {
			for (int i = 0; i < playerRes.length; i++) {
				playerRes[i] = playerRes[i] - resources[i];
				newSize = newSize + playerRes[i];
			}
			if (newSize <= sizeToReach) {
				serverResponse(modelID, "OK");
				robberLossCounter--;
				subFromPlayersResources(modelID, resources);
				resourceStackIncrease(resources);
				serverOutputHandler.costs(threadID, playerRes);
				if (robberLossCounter == 0) {
					if (modelID != currentPlayer) {
						gameLogic.getBoard().getPlayer(modelID).setPlayerState(PlayerState.WAITING);
					}
					gameLogic.getBoard().getPlayer(currentPlayer).setPlayerState(PlayerState.MOVE_ROBBER);
					statusUpdate(currentPlayer);
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
	 * @param victimID
	 * @param currentThreadID
	 */
	public void robberMovementRequest(int x, int y, Integer victimThreadID, int currentThreadID) {
		Integer victimModelID;
		if (victimThreadID != null) {
			victimModelID = threadPlayerIdMap.get(victimThreadID);
		} else {
			victimModelID = null;
		}
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
						stealResource = rand.nextInt(4); // random resource
						// while no resource of this type
					} while (victimResources[stealResource] == 0);

					int[] costs = { 0, 0, 0, 0, 0 };
					costs[stealResource] = 1;
					subFromPlayersResources(victimModelID, costs);
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
		serverOutputHandler.tradePreview(modelPlayerIdMap.get(modelID), tradingID, supply, demand);
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

	public void buyDevelopmentCard(int threadID) {
		int modelID = threadPlayerIdMap.get(threadID);
		subFromPlayersResources(modelID, DefaultSettings.DEVCARD_BUILD_COST);
		resourceStackIncrease(DefaultSettings.DEVCARD_BUILD_COST);
	}

	public void playKnightCard(int threadID, int x, int y, Integer victimThreadID) {
		int modelID = threadPlayerIdMap.get(threadID);
		PlayerModel pM = gameLogic.getBoard().getPlayer(modelID);
		if (gameLogic.checkPlayDevCard(modelID, currentPlayer)) {
			error(modelID, "Ungültiger Spielzug");
		} else {
			Integer victimModelID;
			if (victimThreadID != null) {
				victimModelID = threadPlayerIdMap.get(victimThreadID);
			} else {
				victimModelID = null;
			}
			if (gameLogic.checkSetBandit(x, y, victimModelID)) {
				if (victimThreadID == null) {
					String location = gameLogic.getBoard().getCoordToStringMap().get(new Index(x, y));
					gameLogic.getBoard().setBandit(location);
					serverOutputHandler.robberMovement(threadID, location, null);
				} else {
					PlayerModel victimPM = gameLogic.getBoard().getPlayer(victimModelID);
					if (victimPM.sumResources() != 0) { // steal a random card
						int[] victimResources = getPlayerResources(victimModelID);
						Random rand = new Random();
						int stealResource;
						do {
							stealResource = rand.nextInt(4); // random resource
							// while no resource of this type
						} while (victimResources[stealResource] == 0);

						int[] costs = { 0, 0, 0, 0, 0 };
						costs[stealResource] = 1;
						subFromPlayersResources(victimModelID, costs);
						serverOutputHandler.costs(victimThreadID, costs);

						addToPlayersResource(modelID, costs);
						serverOutputHandler.resourceObtain(threadID, costs);

						String location = gameLogic.getBoard().getCoordToStringMap().get(new Index(x, y));
						gameLogic.getBoard().setBandit(location);
						serverOutputHandler.robberMovement(threadID, location, victimThreadID);

					}
				}
				pM.incrementPlayedKnightCards();
				pM.setHasPlayedDevCard(true);
				checkLargestArmy(modelID);
			} else {
				error(modelID, "Ungültige Eingabe");
			}
		}

	}

	public void playStreetCard(int threadID, int x1, int y1, int dir1, int x2, int y2, int dir2) {
		int modelID = threadPlayerIdMap.get(threadID);
		if (gameLogic.checkPlayDevCard(modelID, currentPlayer)) {
			error(modelID, "Ungültiger Spielzug");
		} else {
			// serverOutputHandler.roadBuildingCardInfo(road1_id, road2_id,
			// player_id);
			// workaround:
			// resourcen für gameLogic hinzufügen
			addToPlayersResource(modelID, DefaultSettings.STREET_BUILD_COST);
			addToPlayersResource(modelID, DefaultSettings.STREET_BUILD_COST);
			if (gameLogic.checkBuildStreet(x1, y1, dir1, modelID)) {
				serverResponse(modelID, "OK");
				buildStreet(x1, y1, dir1, modelID);
				serverOutputHandler.buildStreet(x1, y1, dir1, modelID);

			} else {
				error(modelID, "Eine Straße konnte nicht gebaut werden");
			}
			if (gameLogic.checkBuildStreet(x2, y2, dir2, modelID)) {
				serverResponse(modelID, "OK");
				buildStreet(x2, y2, dir2, modelID);
				serverOutputHandler.buildStreet(x2, y2, dir2, modelID);
			} else {
				error(modelID, "Eine Straße konnte nicht gebaut werden");
			}
			// resourcen für gameLogic wieder abziehen
			subFromPlayersResources(modelID, DefaultSettings.STREET_BUILD_COST);
			subFromPlayersResources(modelID, DefaultSettings.STREET_BUILD_COST);
			gameLogic.getBoard().getPlayer(modelID).setHasPlayedDevCard(true);
		}

	}

	public void playMonopolyCard(int threadID, ResourceType resType) {
		int modelID = threadPlayerIdMap.get(threadID);
		if (gameLogic.checkPlayDevCard(modelID, currentPlayer)) {
			error(modelID, "Ungültiger Spielzug");
		} else {
			int resIndex = DefaultSettings.RESOURCE_VALUES.get(resType);
			int[] obtain = new int[5];
			int currPRes;
			int[] currLoss = new int[5];
			serverOutputHandler.monopolyCardInfo(resType, threadID);
			for (int i = 0; i < amountPlayers; i++) {
				if (i != modelID) {
					currPRes = gameLogic.getBoard().getPlayer(i).getResourceAmountOf(resIndex);
					obtain[resIndex] = obtain[resIndex] + currPRes;
					currLoss[resIndex] = currPRes;
					subFromPlayersResources(i, currLoss);
					serverOutputHandler.costs(i, currLoss);
				}
				currLoss[resIndex] = 0; // reset for next player
			}
			addToPlayersResource(modelID, obtain);
			serverOutputHandler.resourceObtain(modelID, obtain);
			gameLogic.getBoard().getPlayer(modelID).setHasPlayedDevCard(true);
		}
	}

	public void playInventionCard(int threadID, int resources[]) {
		int modelID = threadPlayerIdMap.get(threadID);
		if (gameLogic.checkPlayDevCard(modelID, currentPlayer)) {
			error(modelID, "Ungültiger Spielzug");
		} else {
			int resAmountCheck = 0;
			int[] obtain = new int[5];
			ResourceType currResType;
			serverOutputHandler.inventionCardInfo(resources, threadID);
			for (int i = 0; i < resources.length; i++) {
				if (resources[i] != 0) {
					currResType = DefaultSettings.RESOURCE_ORDER[i];
					if (resourceStackDecrease(currResType)){
						obtain[i]++;
						resAmountCheck++;
					}

				}
			}
			if (resAmountCheck > 2){
				error(modelID, "Du hast du viele Resourcen angegeben; jetzt bekommst du keine ;)");
				resourceStackIncrease(obtain);
			} else {
				addToPlayersResource(modelID, obtain);
				serverOutputHandler.resourceObtain(threadID, obtain);
			}
			gameLogic.getBoard().getPlayer(modelID).setHasPlayedDevCard(true);
		}

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
				if (currNum != 5) {
					currBoard.setFieldAt(coords[0], coords[1], DefaultSettings.RESOURCE_ORDER[currNum],
							DefaultSettings.DICE_NUMBERS[diceInd]);
					diceInd++;
				} else {
					currBoard.setFieldAt(coords[0], coords[1], DefaultSettings.RESOURCE_ORDER[currNum], null);
					currBoard.setBandit("" + fields.charAt(i));
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
				currBoard.setFieldAt(coords[0], coords[1], DefaultSettings.RESOURCE_ORDER[currNum],
						DefaultSettings.DICE_NUMBERS[i]);
			}
			int[] coords = ProtocolToModel.getFieldCoordinates("" + fields.charAt(fields.length() - 1));
			currBoard.setFieldAt(coords[0], coords[1], ResourceType.NOTHING, null);
			currBoard.setBandit("" + fields.charAt(fields.length() - 1));

		}
		String outerRing = currBoard.getOuterRing();
		for (int i = 0; i < outerRing.length(); i++) {
			int[] coords = ProtocolToModel.getFieldCoordinates("" + outerRing.charAt(i));
			currBoard.setFieldAt(coords[0], coords[1], ResourceType.SEA, null);
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

	/**
	 * sets new player resources after dice roll
	 * 
	 * @param diceNum
	 */
	public void gainBoardResources(int diceNum) {
		ArrayList<Field> diceFields = new ArrayList<Field>();
		for (Map.Entry<String, int[]> entry : gameLogic.getBoard().getStringToCoordMap().entrySet()) {
			int[] coord = entry.getValue();
			Field f = gameLogic.getBoard().getFieldAt(coord[0], coord[1]);
			Integer diceInd = f.getDiceIndex();
			if (diceInd != null) {
				if (diceInd == diceNum) {
					diceFields.add(f);
				}
			}
		}
		int[] coords;
		Corner[] neighbors;
		ResourceType currResType;
		int[][] playersObtain = new int[amountPlayers][5];
		for (Field f : diceFields) {
			if (f.getFieldID() != gameLogic.getBoard().getBandit()) {
				coords = gameLogic.getBoard().getFieldCoordinates(f.getFieldID());
				neighbors = gameLogic.getBoard().getSurroundingCorners(coords[0], coords[1]);

				for (int i = 0; i < neighbors.length; i++) {
					if (neighbors[i] != null) {
						switch (neighbors[i].getStatus()) {
						case VILLAGE:
							currResType = f.getResourceType();
							if (resourceStackDecrease(currResType)) {
								addToPlayersResource(neighbors[i].getOwnerID(), currResType, 1);
								playersObtain[neighbors[i].getOwnerID()][DefaultSettings.RESOURCE_VALUES
										.get(currResType)]++;
							}
							break;
						case CITY:
							currResType = f.getResourceType();
							for (int j = 0; j < 2; j++) {
								if (resourceStackDecrease(currResType)) {
									addToPlayersResource(neighbors[i].getOwnerID(), currResType, 2);
									playersObtain[neighbors[i].getOwnerID()][DefaultSettings.RESOURCE_VALUES
											.get(currResType)]++;
								}
							}
							break;
						default:
						}
					}
				}
			}
		}
		for (int i = 0; i < amountPlayers; i++) {
			addToPlayersResource(i, playersObtain[i]);
			serverOutputHandler.resourceObtain(modelPlayerIdMap.get(i), playersObtain[i]);
		}
		statusUpdateForAllPlayers();
	}

	/**
	 * decreases the resource stack
	 * 
	 * @param resType
	 * @return
	 */
	private boolean resourceStackDecrease(ResourceType resType) {
		int resIndex = DefaultSettings.RESOURCE_VALUES.get(resType);
		if (resourceStack[resIndex] > 0) {
			resourceStack[resIndex]--;
		} else {
			return false;
		}
		return true;
	}

	/**
	 * increases the resource stack
	 * 
	 * @param resType
	 */
	private void resourceStackIncrease(ResourceType resType) {
		int resIndex = DefaultSettings.RESOURCE_VALUES.get(resType);
		resourceStack[resIndex] = resourceStack[resIndex]++;
	}

	/**
	 * increases the resource stack with resource array
	 * 
	 * @param resources
	 */
	private void resourceStackIncrease(int[] resources) {
		for (int i = 0; i < resources.length; i++) {
			for (int j = 0; j < resources[i]; j++) {
				resourceStackIncrease(DefaultSettings.RESOURCE_ORDER[i]);
			}
		}
	}

	/**
	 * sets player resources after initial building phase
	 * 
	 */
	private void gainFirstBoardResources() {
		int[] coords; // wegen performance nur einmaliges Initialisieren
		Field[] connFields;
		ResourceType currResType;
		int[][] playersObtain = new int[amountPlayers][5];
		for (int i = 0; i < amountPlayers * 2; i++) {
			coords = ProtocolToModel.getCornerCoordinates(initialVillages.get(i).getCornerID());
			connFields = gameLogic.getBoard().getTouchingFields(coords[0], coords[1], coords[2]);
			for (int j = 0; j < connFields.length; j++) {
				currResType = connFields[j].getResourceType();
				if (currResType != ResourceType.NOTHING && currResType != ResourceType.SEA) {
					if (resourceStackDecrease(currResType)) {
						addToPlayersResource(initialVillages.get(i).getOwnerID(), currResType, 1);
						playersObtain[initialVillages.get(i).getOwnerID()][DefaultSettings.RESOURCE_VALUES
								.get(currResType)]++;
					} else {
						System.out.println("Stack empty: " + currResType);
					}
				}

			}
		}
		for (int i = 0; i < amountPlayers; i++) {
			addToPlayersResource(i, playersObtain[i]);
			serverOutputHandler.resourceObtain(modelPlayerIdMap.get(i), playersObtain[i]);
		}
		statusUpdateForAllPlayers();
	}

	/**
	 * gets next player in the player order
	 * 
	 * @param modelPlayerID
	 * @return nextPlayerID
	 */
	private int getNextPlayer(int modelPlayerID) {
		for (int i = 0; i < playerOrder.length - 1; i++) {
			if (playerOrder[i] == modelPlayerID) {
				return playerOrder[i + 1];
			}
		}
		return playerOrder[0];
	}

	/**
	 * gets player resources
	 * 
	 * @param modelPlayerID
	 * @return int[] resources
	 */
	private int[] getPlayerResources(int modelPlayerID) {
		int[] result = new int[5];
		for (int i = 0; i < result.length; i++) {
			result[i] = gameLogic.getBoard().getPlayer(modelPlayerID).getResourceAmountOf(i);
		}

		return result;
	}

	/**
	 * sets player resources
	 * 
	 * @param modelID
	 * @param resources
	 */
	private void setPlayerResources(int modelID, int[] resources) {
		gameLogic.getBoard().getPlayer(modelID).setResources(resources);
	}

	/**
	 * adds a single resource to players resource
	 * 
	 * @param playerID
	 * @param resType
	 * @param amount
	 */
	public void addToPlayersResource(int playerID, ResourceType resType, int amount) {
		int[] resources = getPlayerResources(playerID);
		for (int i = 0; i < amount; i++) {
			resources[DefaultSettings.RESOURCE_VALUES.get(resType)]++;
		}
		gameLogic.getBoard().getPlayer(playerID).setResources(resources);
	}

	/**
	 * adds resources to player resource
	 * 
	 * @param playerID
	 * @param resourcesToAdd
	 */
	public void addToPlayersResource(int playerID, int[] resourcesToAdd) {
		int[] resources = getPlayerResources(playerID);
		for (int i = 0; i < resourcesToAdd.length; i++) {
			for (int j = 0; j < resourcesToAdd[i]; j++) {
				resources[i]++;
			}
		}
		gameLogic.getBoard().getPlayer(playerID).setResources(resources);
	}

	/**
	 * subtracts resources from players resource
	 * 
	 * @param playerID
	 * @param costsparam
	 */
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
