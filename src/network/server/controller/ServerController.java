package network.server.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import enums.Color;
import enums.HarbourStatus;
import enums.PlayerState;
import enums.ResourceType;
import javafx.stage.Stage;
import model.Board;
import model.DevelopmentCardsStack;
import model.GameLogic;
import model.HexService;
import model.Index;
import model.objects.Corner;
import model.objects.Edge;
import model.objects.Field;
import model.objects.PlayerModel;
import model.objects.DevCards.DevCardFactory;
import model.objects.DevCards.DevelopmentCard;
import model.objects.DevCards.InventionCard;
import model.objects.DevCards.KnightCard;
import model.objects.DevCards.MonopolyCard;
import model.objects.DevCards.StreetBuildingCard;
import model.objects.DevCards.VictoryPointCard;
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
	protected GameLogic gameLogic;
	private ServerOutputHandler serverOutputHandler;
	private int amountPlayers = 0;
	private Server server;
	protected Map<Integer, Integer> modelPlayerIdMap;
	protected Map<Integer, Integer> threadPlayerIdMap;
	private ServerInputHandler serverInputHandler;
	private int InitialStreetCounter;
	// private ArrayList<Corner> initialVillages = new ArrayList<Corner>();
	private Integer currentPlayer;
	private int robberLossCounter;
	private TradeController tradeController;
	private int[] playerOrder;
	private Board board;
	public int[] resourceStack = { 19, 19, 19, 19, 19 };
	private static Logger logger = LogManager.getLogger(ServerController.class.getSimpleName());
	private ArrayList<StreetSet> streetSets = new ArrayList<StreetSet>();
	private int[] longestRoutes;
	private int longestTradingRoutePlayer = -1;
	private DevelopmentCardsStack devStack;

	public ServerController(int serverPort) {
		board = new Board();
		this.gameLogic = new GameLogic(board);
		// ModelPlayerID => threadID
		modelPlayerIdMap = new HashMap<Integer, Integer>();

		// threadID => ModelPlayerID
		threadPlayerIdMap = new HashMap<Integer, Integer>();

		ServerInputHandler serverInputHandler = new ServerInputHandler(this);
		this.server = new Server(serverInputHandler, serverPort);
		this.serverOutputHandler = new ServerOutputHandler(server);

		devStack = new DevelopmentCardsStack();
		DevelopmentCard[] debugCards = new DevelopmentCard[10];
		debugCards[0] = new StreetBuildingCard();
		debugCards[1] = new InventionCard();
		debugCards[2] = new MonopolyCard();
		debugCards[3] = new VictoryPointCard();
		debugCards[4] = new KnightCard();
		debugCards[5] = new InventionCard();
		debugCards[6] = new StreetBuildingCard();
		debugCards[7] = new MonopolyCard();
		debugCards[8] = new VictoryPointCard();
		debugCards[9] = new KnightCard();
		gameLogic.getBoard().getDevCardStack().setCardStack(debugCards);

		try {
			server.start();
		} catch (IOException e) {
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	@Deprecated
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
	protected void error(int modelID, String string) {
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
		if (currentPlayer != null){
			error(modelPlayerIdMap.get(currentThreadID),"Spiel bereits gestratet");
		}

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
			int[] devCards = pM.getPlayerDevCards();
			serverOutputHandler.statusUpdate(modelPlayerIdMap.get(playerModelID), pM.getColor(), pM.getName(),
					pM.getPlayerState(), pM.getVictoryPoints(), resources, pM.getPlayedKnightCards(), devCards,
					pM.hasLongestRoad(), pM.hasLargestArmy(), modelPlayerIdMap.get(sendToPlayer));
		} else {
			int[] resources = { gameLogic.getBoard().getPlayer(playerModelID).sumResources() };
			int[] devCards = { pM.sumDevCards() };
			serverOutputHandler.statusUpdate(modelPlayerIdMap.get(playerModelID), pM.getColor(), pM.getName(),
					pM.getPlayerState(), pM.getVictoryPoints(), resources, pM.getPlayedKnightCards(), devCards,
					pM.hasLongestRoad(), pM.hasLargestArmy(), modelPlayerIdMap.get(sendToPlayer));
		}

	}

	/**
	 * starts the game: generates the board generates player order sets first
	 * player to play
	 */
	public void initializeBoard() {
		longestRoutes = new int[amountPlayers];
		this.tradeController = new TradeController(this, amountPlayers);
		generateBoard("A", true);
		inizializeHarbour();
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
	 * sets the harbors
	 */
	public void inizializeHarbour() {
		String outerRing = "abcdfhjlnrqpomkige";
		Random generator = new Random();
		int random = generator.nextInt() % 2;
		int harbourCounter = 0;
		int[] coord = new int[2];
		int[] edgeCoord = new int[3];
		Corner[] corner = new Corner[2];
		Corner[] harbourCorners = new Corner[18];
		HarbourStatus[] harbourOrder = { HarbourStatus.THREE_TO_ONE, HarbourStatus.CLAY, HarbourStatus.CORN,
				HarbourStatus.THREE_TO_ONE, HarbourStatus.ORE, HarbourStatus.THREE_TO_ONE, HarbourStatus.SHEEP,
				HarbourStatus.WOOD, HarbourStatus.THREE_TO_ONE };
		shuffleArray(harbourOrder);
		if (random == 0) {
			for (int i = 0; i < outerRing.length(); i = i + 2) {
				coord = Board.getStringToCoordMap().get(outerRing.substring(i, i + 1));
				Field[] neighbours = board.getNeighbouringFields(coord[0], coord[1]);
				ArrayList<Field> landNeighbours = new ArrayList<Field>();
				for (int j = 0; j < neighbours.length; j++) {
					if (neighbours[j] != null) {
						if (neighbours[j].getResourceType() != enums.ResourceType.SEA) {
							landNeighbours.add(neighbours[j]);
						}
					}
				}
				int idx = new Random().nextInt(landNeighbours.size());
				String secondDefiningField = (landNeighbours.get(idx).getFieldID());
				edgeCoord = HexService.getEdgeCoordinates(outerRing.substring(i, i + 1), secondDefiningField);
				corner = board.getAttachedCorners(edgeCoord[0], edgeCoord[1], edgeCoord[2]);
				corner[0].setHarbourStatus(harbourOrder[harbourCounter]);
				corner[1].setHarbourStatus(harbourOrder[harbourCounter]);
				harbourCorners[2 * harbourCounter] = corner[0];
				harbourCorners[2 * harbourCounter + 1] = corner[1];
				harbourCounter++;
			}
		} else {
			for (int i = 1; i < outerRing.length(); i = i + 2) {
				coord = Board.getStringToCoordMap().get(outerRing.substring(i, i + 1));
				Field[] neighbours = board.getNeighbouringFields(coord[0], coord[1]);
				ArrayList<Field> landNeighbours = new ArrayList<Field>();
				for (int j = 0; j < neighbours.length; j++) {
					if (neighbours[j] != null) {
						if (neighbours[j].getResourceType() != enums.ResourceType.SEA) {
							landNeighbours.add(neighbours[j]);
						}
					}
				}
				int idx = new Random().nextInt(landNeighbours.size());
				String secondDefiningField = (landNeighbours.get(idx).getFieldID());
				edgeCoord = HexService.getEdgeCoordinates(outerRing.substring(i, i + 1), secondDefiningField);
				corner = board.getAttachedCorners(edgeCoord[0], edgeCoord[1], edgeCoord[2]);
				corner[0].setHarbourStatus(harbourOrder[harbourCounter]);
				corner[1].setHarbourStatus(harbourOrder[harbourCounter]);
				harbourCorners[2 * harbourCounter] = corner[0];
				harbourCorners[2 * harbourCounter + 1] = corner[1];
				harbourCounter++;
			}
		}
		board.setHarbourCorner(harbourCorners);
	}

	public HarbourStatus[] shuffleArray(HarbourStatus[] harbourOrder) {
		Random rnd = ThreadLocalRandom.current();
		for (int i = harbourOrder.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			HarbourStatus a = harbourOrder[index];
			harbourOrder[index] = harbourOrder[i];
			harbourOrder[i] = a;
		}
		return harbourOrder;
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
				} else {
					if (pM.getPlayerState() != PlayerState.DISPENSE_CARDS_ROBBER_LOSS) {
						pM.setPlayerState(PlayerState.WAITING);
						statusUpdate(modelID);
					}
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
					Corner c = buildVillage(x, y, dir, modelID);
					subFromPlayersResources(modelID, DefaultSettings.VILLAGE_BUILD_COST);
					resourceStackIncrease(DefaultSettings.VILLAGE_BUILD_COST);
					increaseVictoryPoints(modelID);
					checkIfVillageInterruptsStreetSet(c);
					// evtl. auch in initial village?

					serverOutputHandler.buildVillage(x, y, dir, threadID);
					costsToAll(modelID, DefaultSettings.VILLAGE_BUILD_COST, true);
					statusUpdate(modelID);
				}
			}
		}

	}

	protected void costsToAll(int modelID, int[] resources, boolean visible) {
		int threadID = modelPlayerIdMap.get(modelID);
		if (visible) {
			for (int i = 0; i < amountPlayers; i++) {
				serverOutputHandler.costs(threadID, resources, modelPlayerIdMap.get(i));
			}
		} else {
			int[] unknown = { resources[0] + resources[1] + resources[2] + resources[3] + resources[4] };
			for (int i = 0; i < amountPlayers; i++) {
				if (i == modelID) {
					serverOutputHandler.costs(threadID, resources, threadID);
				} else {
					serverOutputHandler.costs(threadID, unknown, modelPlayerIdMap.get(i));
				}
			}
		}
	}

	protected void obtainToAll(int modelID, int[] resources, boolean visible) {
		int threadID = modelPlayerIdMap.get(modelID);
		if (visible) {
			for (int i = 0; i < amountPlayers; i++) {
				serverOutputHandler.resourceObtain(threadID, resources, modelPlayerIdMap.get(i));
			}
		} else {
			int[] unknown = { resources[0] + resources[1] + resources[2] + resources[3] + resources[4] };
			for (int i = 0; i < amountPlayers; i++) {
				if (i == modelID) {
					serverOutputHandler.resourceObtain(threadID, resources, threadID);
				} else {
					serverOutputHandler.resourceObtain(threadID, unknown, modelPlayerIdMap.get(i));
				}
			}
		}
	}

	/**
	 * builds a village on specified position checks if position is a harbour
	 * and adds it to player model
	 *
	 * @param x
	 * @param y
	 * @param dir
	 * @param modelID
	 * @return corner object
	 */
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
		if (c.getHarbourStatus() != null) {
			gameLogic.getBoard().getPlayer(modelID).addToPlayerHarbours(c.getHarbourStatus());
		}
		return c;

	}

	/**
	 * increases victory points of a player and checks if the player has won
	 *
	 * @param modelID
	 */
	private void increaseVictoryPoints(int modelID) {
		int points = gameLogic.getBoard().getPlayer(modelID).getVictoryPoints();
		gameLogic.getBoard().getPlayer(modelID).setVictoryPoints(points + 1);
		if (points + 1 == DefaultSettings.MAX_VICTORY_POINTS) {
			victory(modelID);
		}
	}

	/**
	 * is called when a player has 10 victory points
	 *
	 * @param modelID
	 */
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
					costsToAll(modelID, DefaultSettings.STREET_BUILD_COST, true);
					statusUpdate(modelID);

				} else {
					error(modelID, "Kein Straßenbau möglich");
				}
			}
		}

	}

	/**
	 * builds a street at the specified position checks if street changes
	 * longest trading route
	 *
	 * @param x
	 * @param y
	 * @param dir
	 * @param modelID
	 */
	private void buildStreet(int x, int y, int dir, int modelID) {
		Edge e = gameLogic.getBoard().getEdgeAt(x, y, dir);
		e.setHasStreet(true);
		e.setOwnedByPlayer(gameLogic.getBoard().getPlayer(modelID).getID());
		gameLogic.getBoard().getPlayer(modelID).decreaseAmountStreets();
		addToStreetSet(e, modelID);

		checkLongestTradingRoute(modelID);
	}

	/**
	 * checks if a village interrupts a street set
	 *
	 * @param corner
	 */
	public void checkIfVillageInterruptsStreetSet(Corner c) {
		String id = c.getCornerID();
		int[] coords = HexService.getCornerCoordinates(id.substring(0, 1), id.substring(1, 2), id.substring(2, 3));
		Edge[] neighbours = gameLogic.getBoard().getProjectingEdges(coords[0], coords[1], coords[2]);
		Integer modelID = c.getOwnerID();
		ArrayList<Edge> streetEdges = new ArrayList<Edge>();
		Integer currPlayer = null;
		// first: check if there are two adjusting streets with other player
		for (int i = 0; i < neighbours.length; i++) {
			if (neighbours[i] != null) {
				Integer owner = neighbours[i].getOwnerID();
				if (owner != null && owner != modelID) {
					if (currPlayer == owner) {
						streetEdges.add(neighbours[i]);
					} else if (currPlayer == null) {
						streetEdges.add(neighbours[i]);
						currPlayer = owner;
					}
				}
			}
		}
		// if yes then divide street set which contains both streets into two
		// seperate sets; but if street set == circle then only remove the
		// 'circle' flag
		if (streetEdges.size() == 2) {
			if (!EdgeToStreetSet(streetEdges.get(0)).getHasCircle()) {
				for (int i = 0; i < streetSets.size(); i++) {
					if (streetSets.get(i).getPlayerID() == currPlayer) {
						if (streetSets.get(i).getEdges().contains(streetEdges.get(0))) {
							StreetSet newSet1 = new StreetSet(modelID, null);
							newSet1.addEdge(streetEdges.get(0));
							StreetSet newSet2 = new StreetSet(modelID, null);
							newSet2.addEdge(streetEdges.get(1));
							ArrayList<Edge> tempEdges = streetSets.get(i).getEdges();
							streetSets.remove(i);
							streetSets.add(newSet1);
							streetSets.add(newSet2);
							for (Edge e : tempEdges) {
								if (e != streetEdges.get(0) && e != streetEdges.get(1)) {
									addToStreetSet(e, currPlayer);
								}
							}
							break;
						}
					}
				}
				checkLongestTradingRoute(currPlayer);
			} else {
				EdgeToStreetSet(streetEdges.get(0)).setHasCircle(false);
			}
		}
	}

	/**
	 * calculates the StreetSet, which contains the given edge
	 *
	 * don't use this method, if the edge has no street!!!
	 *
	 * @param e
	 *            Edge with street
	 * @return StreetSet containing the Edge
	 */
	public StreetSet EdgeToStreetSet(Edge e) {
		if (e != null) {
			for (int i = 0; i < streetSets.size(); i++) {
				if (streetSets.get(i).getEdges().contains(e)) {
					return streetSets.get(i);
				}
			}
		}
		throw new IllegalArgumentException("Fatal Error in serverController.EdgeToStreetSet");
	}

	/**
	 * adds a specified street (edge object) to a street set
	 *
	 * @param edge
	 * @param modelID
	 */
	private void addToStreetSet(Edge e, int modelID) {
		int[] coord = ProtocolToModel.getEdgeCoordinates(e.getEdgeID());
		boolean edgeAdded = false;
		Edge[] neighbours = gameLogic.getBoard().getLinkedEdges(coord[0], coord[1], coord[2]);
		int addedSetIndex = 0;
		for (int i = 0; i < streetSets.size(); i++) {
			if (streetSets.get(i) != null && streetSets.get(i).getPlayerID() == modelID) {
				int j = 0;
				boolean currSetAdded = false;
				while (j < neighbours.length && currSetAdded == false) {
					if (streetSets.get(i).getEdges().contains(neighbours[j])) {
						if (edgeAdded) {
							streetSets.get(addedSetIndex).getEdges().addAll(streetSets.get(i).getEdges());
							streetSets.remove(i);
							break;
						}
						streetSets.get(i).addEdge(e);
						addedSetIndex = i;
						currSetAdded = true;
						edgeAdded = true;

					}
					j++;
				}
			}
		}
		if (edgeAdded == false) {
			ArrayList<Edge> edges = new ArrayList<Edge>();
			edges.add(e);
			StreetSet streetSet = new StreetSet(modelID, edges);
			streetSets.add(streetSet);
		}
	}

	public void checkLongestTradingRoute(int modelID) {
		int max = getLongestTradingRoute(modelID);
		longestRoutes[modelID] = max;
		if (max >= 5) {
			if (longestTradingRoutePlayer != -1) {
				if (longestTradingRoutePlayer != modelID && longestRoutes[longestTradingRoutePlayer] < max) {
					gameLogic.getBoard().getPlayer(longestTradingRoutePlayer).setHasLongestRoad(false);
					gameLogic.getBoard().getPlayer(modelID).setHasLongestRoad(true);
					longestTradingRoutePlayer = modelID;
					serverOutputHandler.longestRoad(modelPlayerIdMap.get(modelID));
				}
			} else {
				gameLogic.getBoard().getPlayer(modelID).setHasLongestRoad(true);
				longestTradingRoutePlayer = modelID;
				serverOutputHandler.longestRoad(modelPlayerIdMap.get(modelID));
			}
		} else {
			if (longestTradingRoutePlayer == modelID) { // special case
				gameLogic.getBoard().getPlayer(modelID).setHasLongestRoad(false);
				longestTradingRoutePlayer = -1;
				serverOutputHandler.longestRoad(null);
			}
		}

		System.out.println("Calculated longest Trading Route: Player = " + modelID + " Lenght = " + max);

	}

	/**
	 * DON'T CALL THIS WHEN PLAYER HAS NO STREETS!!! calculates the longest
	 * trading route for a specific player with streets
	 *
	 * @param modelID
	 *
	 * @return length of the longest trading route of the player
	 */
	public int getLongestTradingRoute(int modelID) {
		ArrayList<Integer> longestStreets = new ArrayList<Integer>();
		longestStreets.add(0);
		ArrayList<Edge> currEndingStreets = new ArrayList<Edge>();
		StreetSet currStreetSet = null;
		for (int i = 0; i < streetSets.size(); i++) {
			currStreetSet = streetSets.get(i);
			if (currStreetSet.getPlayerID() == modelID && currStreetSet.size() >= 5) {
				for (int j = 0; j < currStreetSet.size(); j++) {
					String id = currStreetSet.getEdgeAt(j).getEdgeID();
					int[] coords = HexService.getEdgeCoordinates(id.substring(0, 1), id.substring(1, 2));
					Edge[] neighbours = gameLogic.getBoard().getLinkedEdges(coords[0], coords[1], coords[2]);
					boolean top = false;
					boolean bottom = false;
					switch (coords[2]) {
					case 0: // neighbour order differs order of dir 1,2
						if (currStreetSet.getEdges().contains(neighbours[0])
								|| currStreetSet.getEdges().contains(neighbours[1])) {
							top = true;
						}
						if (currStreetSet.getEdges().contains(neighbours[2])
								|| currStreetSet.getEdges().contains(neighbours[3])) {
							bottom = true;
						}
						if (!(top && bottom)) {
							currEndingStreets.add(currStreetSet.getEdgeAt(j));
						}

						break;
					default:
						if (currStreetSet.getEdges().contains(neighbours[0])
								|| currStreetSet.getEdges().contains(neighbours[3])) {
							top = true;
						}
						if (currStreetSet.getEdges().contains(neighbours[2])
								|| currStreetSet.getEdges().contains(neighbours[1])) {
							bottom = true;
						}
						if (!(top && bottom)) {
							currEndingStreets.add(currStreetSet.getEdgeAt(j));
						}
						break;
					}
				}
				if (currEndingStreets.isEmpty() || currStreetSet.getHasCircle()) {
					currStreetSet.setHasCircle(true);
					ArrayList<Edge> alreadyChecked = new ArrayList<Edge>();
					ArrayList<Edge> lastNeighbours = new ArrayList<Edge>();
					ArrayList<Integer> greatestValue = new ArrayList<Integer>();
					// gehe über alle Straßen
					for (int j = 0; j < currStreetSet.size(); j++) {
						greatestValue.add(1 + depthFirstSearch(currStreetSet.getEdgeAt(j), currStreetSet,
								alreadyChecked, lastNeighbours));
					}
					longestStreets.add(Collections.max(greatestValue));
				} else {
					for (int j = 0; j < currEndingStreets.size(); j++) {
						ArrayList<Edge> alreadyChecked = new ArrayList<Edge>();
						ArrayList<Edge> lastNeighbours = new ArrayList<Edge>();
						longestStreets.add(1 + depthFirstSearch(currEndingStreets.get(j), currStreetSet, alreadyChecked,
								lastNeighbours));
					}
					currEndingStreets.clear();
				}
			}

		}
		return Collections.max(longestStreets);
	}

	/**
	 * recursive method for calculating the longest route starting from an
	 * specific edge
	 *
	 * @param edge
	 *            starting street
	 * @param currStreetSet
	 *            street set which contains the street
	 * @param alreadyChecked
	 *            streets which are already counted
	 * @param lastNeighbours
	 *            neighbours, which are prohibited to check
	 * @return longest rout of not checked streets
	 */
	private Integer depthFirstSearch(Edge edge, StreetSet currStreetSet, ArrayList<Edge> alreadyChecked,
			ArrayList<Edge> lastNeighbours) {
		int[] coord = HexService.getEdgeCoordinates(edge.getEdgeID().substring(0, 1), edge.getEdgeID().substring(1, 2));
		Edge[] neighbours = board.getLinkedEdges(coord[0], coord[1], coord[2]);
		ArrayList<Edge> aC = new ArrayList<Edge>(alreadyChecked);
		aC.add(edge);
		ArrayList<Edge> validNeighbours = new ArrayList<Edge>();
		for (int i = 0; i < neighbours.length; i++) {
			if (currStreetSet.getEdges().contains(neighbours[i]) && !aC.contains(neighbours[i])
					&& !lastNeighbours.contains(neighbours[i])) {
				validNeighbours.add(neighbours[i]);
			}
		}
		int greatestValue = 0;
		for (int i = 0; i < validNeighbours.size(); i++) {
			int currSize = 1 + depthFirstSearch(validNeighbours.get(i), currStreetSet, aC, validNeighbours);
			if (currSize > greatestValue) {
				greatestValue = currSize;
			}
		}
		return greatestValue;
	}

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
				costsToAll(modelID, DefaultSettings.CITY_BUILD_COST, true);
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
				costsToAll(modelID, DefaultSettings.DEVCARD_BUILD_COST, true);
				DevelopmentCard devCard = gameLogic.getBoard().getDevCardStack().getNextCard();
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
					// gainFirstBoardResources();
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
				if (InitialStreetCounter >= amountPlayers) { // second round
					gainFirstBoardResources(modelID, c);
				}

				increaseVictoryPoints(modelID);
				checkIfVillageInterruptsStreetSet(c);

				serverOutputHandler.buildVillage(x, y, dir, threadID);
				gameLogic.getBoard().getPlayer(modelID).setPlayerState(PlayerState.BUILDING_STREET);
				statusUpdate(modelID);

				// for check if next street is adjacent to exactly this corner
				gameLogic.setInitialLastVillage(c);
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
		if (modelID != currentPlayer) {
			error(modelID, "Unzulässige Aktion");
		} else {
			PlayerModel pM = gameLogic.getBoard().getPlayer(modelID);
			pM.setPlayerState(PlayerState.WAITING);
			ArrayList<DevelopmentCard> currDevCards = pM.getDevCardsBoughtInThisRound();
			if (currDevCards != null) {
				for (int i = 0; i < currDevCards.size(); i++) {
					// erst jetzt kann spieler über development card verfügen
					pM.incrementPlayerDevCard(currDevCards.get(i));
				}
				pM.getDevCardsBoughtInThisRound().clear();
			}
			// runde zu ende, nächste runde darf dev card gespielt werden
			pM.setHasPlayedDevCard(false);
			statusUpdate(modelID);

			currentPlayer = getNextPlayer(modelID); // next players turn
			gameLogic.getBoard().getPlayer(currentPlayer).setPlayerState(PlayerState.DICEROLLING);
			statusUpdate(currentPlayer);
		}

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
				costsToAll(modelID, playerRes, false);
				if (robberLossCounter == 0) {
					if (modelID != currentPlayer) {
						gameLogic.getBoard().getPlayer(modelID).setPlayerState(PlayerState.WAITING);
						statusUpdate(modelID);
					}
					gameLogic.getBoard().getPlayer(currentPlayer).setPlayerState(PlayerState.MOVE_ROBBER);
					statusUpdate(currentPlayer);
				} else {
					gameLogic.getBoard().getPlayer(modelID).setPlayerState(PlayerState.WAITING);
					statusUpdate(modelID);
				}
			} else {
				error(modelID, "You haven't specified enough resources");
			}
		} else {
			error(modelID, "You dont have the specified resources");
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
		int modelID = threadPlayerIdMap.get(currentThreadID);
		Integer victimModelID;
		if (victimThreadID != null) {
			victimModelID = threadPlayerIdMap.get(victimThreadID);
		} else {
			victimModelID = null;
		}
		if (gameLogic.checkSetBandit(x, y, victimModelID)) {
			if (victimThreadID != null) {
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
					costsToAll(victimModelID, costs, false); // evtl. auch
																// modelID
																// benachrichtigt?

					addToPlayersResource(modelID, costs);
					obtainToAll(modelID, costs, false);
				}
			}
			String location = gameLogic.getBoard().getCoordToStringMap().get(new Index(x, y));
			gameLogic.getBoard().setBandit(location);
			serverOutputHandler.robberMovement(currentThreadID, location, victimThreadID);
			gameLogic.getBoard().getPlayer(modelID).setPlayerState(PlayerState.TRADING_OR_BUILDING);
			statusUpdate(modelID);
		}

	}

	/**
	 *
	 * Basic methods for trading
	 */

	public void requestSeaTrade(int threadID, int[] offer, int[] demand) {
		int modelID = threadPlayerIdMap.get(threadID);
		if (gameLogic.checkPlayerResources(modelID, offer)) {
			tradeController.requestSeaTrade(modelID, offer, demand);
		}
	}

	public void clientOffersTrade(int threadID, int[] supply, int[] demand) {
		tradeController.clientOffersTrade(threadPlayerIdMap.get(threadID), supply, demand);
	}

	public void sendClientOffer(int modelID, int tradingID, int[] supply, int[] demand) {
		serverOutputHandler.tradePreview(modelPlayerIdMap.get(modelID), tradingID, supply, demand);
	}

	public void acceptTrade(int threadID, int tradingID, boolean accept) {
		tradeController.acceptTrade(threadPlayerIdMap.get(threadID), tradingID, accept);
	}

	public void tradeAccepted(int modelID, int tradingID, boolean accepted) {
		serverOutputHandler.tradeConfirmation(modelPlayerIdMap.get(modelID), tradingID, accepted);
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

	/*
	 * public void buyDevelopmentCard(int threadID) { int modelID =
	 * threadPlayerIdMap.get(threadID); subFromPlayersResources(modelID,
	 * DefaultSettings.DEVCARD_BUILD_COST);
	 * resourceStackIncrease(DefaultSettings.DEVCARD_BUILD_COST);
	 * DevelopmentCard currCard = devStack.getNextCard(); //TODO: Only one dev
	 * card per round
	 * gameLogic.getBoard().getPlayer(modelID).incrementPlayerDevCard(currCard);
	 * serverOutputHandler.boughtDevelopmentCard(threadID, currCard); }
	 */

	public void playKnightCard(int threadID, int x, int y, Integer victimThreadID) {
		int modelID = threadPlayerIdMap.get(threadID);
		PlayerModel pM = gameLogic.getBoard().getPlayer(modelID);
		if (!gameLogic.checkPlayDevCard(modelID, currentPlayer)) {
			error(modelID, "Ungültiger Spielzug");
		} else {
			Integer victimModelID;
			if (victimThreadID != null) {
				victimModelID = threadPlayerIdMap.get(victimThreadID);
			} else {
				victimModelID = null;
			}
			if (gameLogic.checkSetBandit(x, y, victimModelID)) {
				if (victimThreadID != null) {
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
						costsToAll(victimModelID, costs, false);

						addToPlayersResource(modelID, costs);
						obtainToAll(modelID, costs, false);
					}
				}
				String location = gameLogic.getBoard().getCoordToStringMap().get(new Index(x, y));
				gameLogic.getBoard().setBandit(location);
				serverOutputHandler.robberMovement(threadID, location, victimThreadID);
				serverOutputHandler.knightCardPlayed(threadID, location, victimThreadID);

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
		if (!gameLogic.checkPlayDevCard(modelID, currentPlayer)) {
			error(modelID, "Ungültiger Spielzug");
		} else {
			serverOutputHandler.roadBuildingCardPlayed(threadID, x1, y1, dir1, x2, y2, dir2);
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
		if (!gameLogic.checkPlayDevCard(modelID, currentPlayer)) {
			error(modelID, "Ungültiger Spielzug");
		} else {
			int resIndex = DefaultSettings.RESOURCE_VALUES.get(resType);
			int[] obtain = new int[5];
			int currPRes;
			int[] currLoss = new int[5];
			serverOutputHandler.monopolyCardPlayed(resType, threadID);
			for (int i = 0; i < amountPlayers; i++) {
				if (i != modelID) {
					currPRes = gameLogic.getBoard().getPlayer(i).getResourceAmountOf(resIndex);
					obtain[resIndex] = obtain[resIndex] + currPRes;
					currLoss[resIndex] = currPRes;
					subFromPlayersResources(i, currLoss);
					costsToAll(i, currLoss, true);
				}
				currLoss[resIndex] = 0; // reset for next player
			}
			addToPlayersResource(modelID, obtain);
			obtainToAll(modelID, obtain, true);
			gameLogic.getBoard().getPlayer(modelID).setHasPlayedDevCard(true);
		}
	}

	public void playInventionCard(int threadID, int[] resources) {
		int modelID = threadPlayerIdMap.get(threadID);
		if (!gameLogic.checkPlayDevCard(modelID, currentPlayer)) {
			error(modelID, "Ungültiger Spielzug");
		} else {
			int resAmountCheck = 0;
			int[] obtain = new int[5];
			ResourceType currResType;
			serverOutputHandler.inventionCardPlayed(resources, threadID);
			for (int i = 0; i < resources.length; i++) {
				if (resources[i] != 0) {
					currResType = DefaultSettings.RESOURCE_ORDER[i];
					if (resourceStackDecrease(currResType)) {
						obtain[i]++;
						resAmountCheck++;
					}
				}
			}
			if (resAmountCheck > 2) {
				error(modelID, "Du hast du viele Resourcen angegeben; jetzt bekommst du keine ;)");
				resourceStackIncrease(obtain);
			} else {
				addToPlayersResource(modelID, obtain);
				obtainToAll(modelID, obtain, false);
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
			if (!f.getFieldID().equals(gameLogic.getBoard().getBandit())) {
				coords = gameLogic.getBoard().getFieldCoordinates(f.getFieldID());
				neighbors = gameLogic.getBoard().getSurroundingCorners(coords[0], coords[1]);

				for (int i = 0; i < neighbors.length; i++) {
					if (neighbors[i] != null) {
						switch (neighbors[i].getStatus()) {
						case VILLAGE:
							currResType = f.getResourceType();
							if (resourceStackDecrease(currResType)) {
								playersObtain[neighbors[i].getOwnerID()][DefaultSettings.RESOURCE_VALUES
										.get(currResType)]++;
							}
							break;
						case CITY:
							currResType = f.getResourceType();
							for (int j = 0; j < 2; j++) {
								if (resourceStackDecrease(currResType)) {
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
			obtainToAll(i, playersObtain[i], true);
		}
		statusUpdateForAllPlayers();
	}

	/**
	 * decreases the resource stack
	 *
	 * @param resType
	 * @return
	 */
	protected boolean resourceStackDecrease(ResourceType resType) {
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
	protected void resourceStackIncrease(ResourceType resType) {
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

	private void gainFirstBoardResources(int modelID, Corner c) {
		int[] playersObtain = new int[5];
		int[] coords = ProtocolToModel.getCornerCoordinates(c.getCornerID());
		Field[] connFields = gameLogic.getBoard().getTouchingFields(coords[0], coords[1], coords[2]);
		ResourceType currResType;
		for (int i = 0; i < connFields.length; i++) {
			currResType = connFields[i].getResourceType();
			if (currResType != ResourceType.NOTHING && currResType != ResourceType.SEA) {
				if (resourceStackDecrease(currResType)) {
					playersObtain[DefaultSettings.RESOURCE_VALUES.get(currResType)]++;
				}
			}
		}
		addToPlayersResource(modelID, playersObtain);
		obtainToAll(modelID, playersObtain, true);
		statusUpdate(modelID);

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
	protected int[] getPlayerResources(int modelPlayerID) {
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
	public void subFromPlayersResources(int playerID, int[] costs) {
		int[] pResources = getPlayerResources(playerID);
		for (int i = 0; i < costs.length; i++) {
			pResources[i] = pResources[i] - costs[i];
		}
		gameLogic.getBoard().getPlayer(playerID).setResources(pResources);
	}

	public void subFromPlayersResources(int playerID, ResourceType resType, int amount) {
		int[] resources = getPlayerResources(playerID);
		for (int i = 0; i < amount; i++) {
			resources[DefaultSettings.RESOURCE_VALUES.get(resType)]--;
		}
		gameLogic.getBoard().getPlayer(playerID).setResources(resources);
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
