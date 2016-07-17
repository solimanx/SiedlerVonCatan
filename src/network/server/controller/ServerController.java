package network.server.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import enums.CardType;
import enums.Color;
import enums.HarbourStatus;
import enums.PlayerState;
import enums.ResourceType;
import model.board.Board;
import model.board.Corner;
import model.board.Edge;
import model.board.Field;
import model.board.PlayerModel;
import model.card.DevelopmentCard;
import model.unit.Index;
import model.unit.StreetSet;
import network.server.server.Server;
import network.server.server.ServerInputHandler;
import network.server.server.ServerOutputHandler;
import service.GameLogic;
import service.HexService;
import service.ProtocolToModel;
import settings.DefaultSettings;

// TODO: Auto-generated Javadoc
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

	/**
	 * thread to model
	 */
	protected Map<Integer, Integer> modelPlayerIdMap;
	/**
	 * model to thread
	 */
	protected Map<Integer, Integer> threadPlayerIdMap;
	private ServerInputHandler serverInputHandler;
	private int InitialStreetCounter;
	private Integer currentPlayer = null;
	private int robberLossCounter;
	private TradeController tradeController;
	private int[] playerOrder;
	private Board board;
	public int[] resourceStack = { DefaultSettings.amountResourceCards, DefaultSettings.amountResourceCards,
			DefaultSettings.amountResourceCards, DefaultSettings.amountResourceCards,
			DefaultSettings.amountResourceCards };
	private static Logger logger = LogManager.getLogger(ServerController.class.getSimpleName());
	private ArrayList<StreetSet> streetSets = new ArrayList<StreetSet>();
	private int[] longestRoutes;
	private int longestTradingRoutePlayer = -1;
	private int biggestKnightForcePlayer = -1;
	private Integer currentExtraPlayer;
	private boolean longestTurnEnabled = false;

	private boolean FiveSixGame;
	private ArrayList<PlayerModel> lobbyPlayers = new ArrayList<PlayerModel>();

	/**
	 * Instantiates a new server controller.
	 *
	 * @param serverPort
	 *            the server port
	 */
	public ServerController(int serverPort) {
		modelPlayerIdMap = new HashMap<Integer, Integer>();
		threadPlayerIdMap = new HashMap<Integer, Integer>();

		ServerInputHandler serverInputHandler = new ServerInputHandler(this);
		this.server = new Server(serverInputHandler, serverPort);
		this.serverOutputHandler = new ServerOutputHandler(server);

		startServer();

	}

	public void startServer() {
		Thread serverThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					server.start();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		serverThread.start();
	}

	/**
	 * Sets the server output handler.
	 *
	 * @param sNC
	 *            the new server output handler
	 */
	@Deprecated
	public void setServerOutputHandler(ServerOutputHandler sNC) {
		this.serverOutputHandler = sNC;

	}

	/**
	 * sends a hello message at server start.
	 *
	 * @param currentThreadID
	 *            the current thread ID
	 */
	public void hello(int currentThreadID) {
		serverOutputHandler.hello(DefaultSettings.SERVER_VERSION, DefaultSettings.PROTOCOL_VERSION, currentThreadID);
	}

	/**
	 * is called when a client sends hello message registers threadID and sends
	 * status update.
	 *
	 * @param currentThreadID
	 *            the current thread ID
	 * @param string
	 *            the string
	 */
	public void receiveHello(int currentThreadID, String string) {
		// if type mismatch
		// if (!string.contains(DefaultSettings.SERVER_VERSION)) {
		// server.disconnectPlayer(currentThreadID);
		// } else {
		threadPlayerIdMap.put(currentThreadID, amountPlayers);
		modelPlayerIdMap.put(amountPlayers, currentThreadID);
		amountPlayers++;
		lobbyPlayers.add(new PlayerModel(amountPlayers - 1));
		lobbyPlayers.get(amountPlayers - 1).setPlayerState(PlayerState.GAME_STARTING);
		welcome(amountPlayers - 1);
		for (int i = 0; i < amountPlayers; i++) {
			// send status of all players to new player
			lobbyStatusUpdate(i, amountPlayers - 1);
			if (i != amountPlayers - 1) {
				// send status of new player to others
				lobbyStatusUpdate(amountPlayers - 1, i);
			}
		}
		// }

	}

	/**
	 * sends a welcome message to client.
	 *
	 * @param modelPlayerID
	 *            the model player ID
	 */
	public void welcome(int modelPlayerID) {
		serverOutputHandler.welcome(modelPlayerIdMap.get(modelPlayerID));
	}

	/**
	 * Lobby status update.
	 *
	 * @param modelID
	 *            the model ID
	 * @param sendToPlayer
	 *            the send to player
	 */
	private void lobbyStatusUpdate(int modelID, int sendToPlayer) {
		serverOutputHandler.statusUpdate(modelPlayerIdMap.get(modelID), lobbyPlayers.get(modelID).getColor(),
				lobbyPlayers.get(modelID).getName(), lobbyPlayers.get(modelID).getPlayerState(), 0, new int[5], null,
				new int[5], false, false, modelPlayerIdMap.get(sendToPlayer));
	}

	/**
	 * is called when a client is ready, if all clients are ready then start
	 * game.
	 *
	 * @param currentThreadID
	 *            the current thread ID
	 */
	public void clientReady(int currentThreadID) {
		int modelID = threadPlayerIdMap.get(currentThreadID);
		if (lobbyPlayers.get(modelID).getColor() == null) {
			serverResponse(modelID, "Wähle zuerst eine Farbe und einen Namen");
		} else {
			boolean colorAvailable = true;
			Color currColor;
			Color ownColor = lobbyPlayers.get(modelID).getColor();

			for (int i = 0; i < amountPlayers; i++) {
				currColor = lobbyPlayers.get(i).getColor();
				if (currColor != null && i != modelID) {
					if (currColor.equals(ownColor)
							&& lobbyPlayers.get(i).getPlayerState() == PlayerState.WAITING_FOR_GAMESTART) {
						colorAvailable = false;
						break;
					}
				}
			}
			if (!colorAvailable) {
				error(modelID, "Farbe bereits vergeben!");
			} else {
				lobbyPlayers.get(modelID).setPlayerState(PlayerState.WAITING_FOR_GAMESTART);
				for (int i = 0; i < amountPlayers; i++) {
					lobbyStatusUpdate(modelID, i);
				}

				if (amountPlayers >= 3) {// && amountPlayers ==
											// server.getConnectedPlayers()) {
					boolean allReady = true;
					for (int i = 0; i < amountPlayers; i++) {
						if (lobbyPlayers.get(i).getPlayerState() != PlayerState.WAITING_FOR_GAMESTART) {
							allReady = false;
							break;
						}
					}
					if (allReady) {
						if (amountPlayers > 4) {
							FiveSixGame = true;
						}
						initializeBoard();
					}
				}
			}
		}

	}

	/**
	 * sends server response to specified client.
	 *
	 * @param modelID
	 *            the model ID
	 * @param server_response
	 *            the server response
	 */
	public void serverResponse(int modelID, String server_response) {
		serverOutputHandler.serverConfirm(server_response, modelPlayerIdMap.get(modelID));

	}

	/**
	 * sends an error to specified client.
	 *
	 * @param modelID
	 *            the model ID
	 * @param string
	 *            the string
	 */
	protected void error(int modelID, String string) {
		serverOutputHandler.error(string, modelPlayerIdMap.get(modelID));

	}

	/**
	 * is called when client sets own name and color before gamestart.
	 *
	 * @param color
	 *            the color
	 * @param name
	 *            the name
	 * @param currentThreadID
	 *            the current thread ID
	 */
	public void playerProfileUpdate(Color color, String name, int currentThreadID) {
		Integer modelID = threadPlayerIdMap.get(currentThreadID);
		if (currentPlayer != null) {
			serverResponse(modelID, "Spiel bereits gestartet");
		} else {

			lobbyPlayers.get(modelID).setColor(color);
			lobbyPlayers.get(modelID).setName(name);
			serverResponse(modelID, "OK");
			for (int i = 0; i < amountPlayers; i++) {
				lobbyStatusUpdate(modelID, i);
			}
		}
	}

	/**
	 * sends a received chat message to all clients.
	 *
	 * @param playerId
	 *            the player id
	 * @param s
	 *            the s
	 */
	public void chatReceiveMessage(int playerId, String s) {
		serverOutputHandler.chatReceiveMessage(playerId, s);

	}

	/**
	 * is called when client sends a chat message.
	 *
	 * @param s
	 *            the s
	 * @param currentThreadID
	 *            the current thread ID
	 */
	public void chatSendMessage(String s, int currentThreadID) {
		serverOutputHandler.serverConfirm("OK", currentThreadID);
		chatReceiveMessage(currentThreadID, s);

	}

	/**
	 * sends status update of all players to all clients.
	 */
	public void statusUpdateForAllPlayers() {
		for (int i = 0; i < amountPlayers; i++) {
			statusUpdate(i);
		}
	}

	/**
	 * sends status update of specified player to all clients.
	 *
	 * @param playerModelID
	 *            the player model ID
	 */
	public void statusUpdate(int playerModelID) {
		for (int i = 0; i < amountPlayers; i++) {
			statusUpdateToPlayer(i, playerModelID);
		}
	}

	/**
	 * sends status update of a player to specified client.
	 *
	 * @param sendToPlayer
	 *            the send to player
	 * @param playerModelID
	 *            the player model ID
	 */
	public void statusUpdateToPlayer(int sendToPlayer, int playerModelID) {
		PlayerModel pM = gameLogic.getBoard().getPlayer(playerModelID);

		if (sendToPlayer == playerModelID) {
			int[] resources = getPlayerResources(playerModelID);
			int[] devCards = pM.getPlayerDevCards();
			serverOutputHandler.statusUpdate(modelPlayerIdMap.get(playerModelID), pM.getColor(), pM.getName(),
					pM.getPlayerState(), pM.getVictoryPoints() + pM.getHiddenVictoryPoints(), resources,
					pM.getPlayedKnightCards(), devCards, pM.hasLongestRoad(), pM.hasLargestArmy(),
					modelPlayerIdMap.get(sendToPlayer));
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
	 * player to play.
	 */
	public void initializeBoard() {
		longestRoutes = new int[amountPlayers];
		this.tradeController = new TradeController(this, amountPlayers);
		
		if(FiveSixGame){
			Board.extendBoard();
		}
		board = new Board();
		this.gameLogic = new GameLogic(board);

		if (FiveSixGame) {
			generateFiveSixBoard("A");
			initializeFiveSixHarbours();
		} else {
			generateBoard("A", true);
			initializeHarbour();
		}
		PlayerModel[] pm = new PlayerModel[lobbyPlayers.size()];
		pm = lobbyPlayers.toArray(pm);
		for (int i = 0; i < amountPlayers; i++) {
			// insert playerModels
			board.getPlayerModels()[i] = pm[i];
		}

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

	private void generateFiveSixBoard(String string) {
		//String fields = HexService.getSpiralFifeSix(string);
		//TODO  ONLY FOR DEBUG
		String fields = "CBADHMSX\\]^[WRLGFEINTYZVQKJOUP";
		Board currBoard = gameLogic.getBoard();
		int[] cards = DefaultSettings.landscapeAmount.clone();
		int currNum;
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
						DefaultSettings.diceNumbers[diceInd]);
				diceInd++;
			} else {
				currBoard.setFieldAt(coords[0], coords[1], DefaultSettings.RESOURCE_ORDER[currNum], null);
				currBoard.setBandit("" + fields.charAt(i));
			}
		}
		String outerRing = currBoard.getOuterRing();
		for (int i = 0; i < outerRing.length(); i++) {
			int[] coords = ProtocolToModel.getFieldCoordinates("" + outerRing.charAt(i));
			currBoard.setFieldAt(coords[0], coords[1], ResourceType.SEA, null);
		}

	}

	/**
	 * sets the harbors.
	 */
	public void initializeHarbour() {
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

	public void initializeFiveSixHarbours() {
		String outerRing = board.getOuterRing();
		Random generator = new Random();
		int random = generator.nextInt() % 2;
		int harbourCounter = 0;
		int[] coord = new int[2];
		int[] edgeCoord = new int[3];
		Corner[] corner = new Corner[2];
		Corner[] harbourCorners = new Corner[22];
		HarbourStatus[] harbourOrder = { HarbourStatus.THREE_TO_ONE, HarbourStatus.CLAY, HarbourStatus.CORN,
				HarbourStatus.THREE_TO_ONE, HarbourStatus.ORE, HarbourStatus.THREE_TO_ONE, HarbourStatus.SHEEP,
				HarbourStatus.WOOD, HarbourStatus.THREE_TO_ONE, HarbourStatus.SHEEP, HarbourStatus.THREE_TO_ONE };
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

	/**
	 * Shuffle array.
	 *
	 * @param harbourOrder
	 *            the harbour order
	 * @return the harbour status[]
	 */
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
	 * dice roll request from client.
	 *
	 * @param threadID
	 *            the thread ID
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
	 * generates a random dice result.
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
	 * then jumps to requestBuildInitialVillage.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param dir
	 *            the dir
	 * @param threadID
	 *            the thread ID
	 */
	public void requestBuildVillage(int x, int y, int dir, int threadID) {
		int modelID = threadPlayerIdMap.get(threadID);
		if (InitialStreetCounter < amountPlayers * 2) {
			requestBuildInitialVillage(x, y, dir, threadID);
		} else {
			if (!(gameLogic.isActionForbidden(modelID, currentPlayer, PlayerState.TRADING_OR_BUILDING)
					^ gameLogic.isActionForbidden(modelID, currentPlayer, PlayerState.BUILDING))) {
				serverResponse(modelID, "Unzulässige Aktion");
			} else {
				if (gameLogic.checkBuildVillage(x, y, dir, modelID)) {

					serverResponse(modelID, "OK");
					Corner c = buildVillage(x, y, dir, modelID);
					subFromPlayersResources(modelID, DefaultSettings.VILLAGE_BUILD_COST);
					resourceStackIncrease(DefaultSettings.VILLAGE_BUILD_COST);
					increaseVictoryPoints(modelID, 1);
					checkIfVillageInterruptsStreetSet(c);
					// evtl. auch in initial village?
					costsToAll(modelID, DefaultSettings.VILLAGE_BUILD_COST, true);
					serverOutputHandler.buildVillage(x, y, dir, threadID);

					// statusUpdate(modelID);
				}
			}
		}

	}

	/**
	 * Costs to all.
	 *
	 * @param modelID
	 *            the model ID
	 * @param resources
	 *            the resources
	 * @param visible
	 *            the visible
	 */
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

	/**
	 * Obtain to all.
	 *
	 * @param modelID
	 *            the model ID
	 * @param resources
	 *            the resources
	 * @param visible
	 *            the visible
	 */
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
	 * and adds it to player model.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param dir
	 *            the dir
	 * @param modelID
	 *            the model ID
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
	 * increases victory points of a player and checks if the player has won.
	 *
	 * @param modelID
	 *            the model ID
	 * @param amount
	 *            the amount
	 */
	private void increaseVictoryPoints(int modelID, int amount) {
		int points = gameLogic.getBoard().getPlayer(modelID).getVictoryPoints();
		gameLogic.getBoard().getPlayer(modelID).setVictoryPoints(points + amount);
		checkVictory(modelID);
	}

	/**
	 * decreases victory points of a player and checks if the player has won.
	 *
	 * @param modelID
	 *            the model ID
	 * @param amount
	 *            the amount
	 */
	private void decreaseVictoryPoints(int modelID, int amount) {
		int points = gameLogic.getBoard().getPlayer(modelID).getVictoryPoints();
		gameLogic.getBoard().getPlayer(modelID).setVictoryPoints(points - amount);
	}

	/**
	 * increases victory points of a player and checks if the player has won.
	 *
	 * @param modelID
	 *            the model ID
	 * @param amount
	 *            the amount
	 */
	private void increaseHiddenVictoryPoints(int modelID, int amount) {
		int points = gameLogic.getBoard().getPlayer(modelID).getHiddenVictoryPoints();
		gameLogic.getBoard().getPlayer(modelID).setHiddenVictoryPoints(points + amount);
		checkVictory(modelID);
	}

	/**
	 * Check victory.
	 *
	 * @param modelID
	 *            the model ID
	 */
	private void checkVictory(int modelID) {
		int points = gameLogic.getBoard().getPlayer(modelID).getVictoryPoints();
		int hiddenpoints = gameLogic.getBoard().getPlayer(modelID).getHiddenVictoryPoints();
		if (points + hiddenpoints >= DefaultSettings.MAX_VICTORY_POINTS) {
			victory(modelID);
		}
	}

	/**
	 * is called when a player has or more 10 victory points.
	 *
	 * @param modelID
	 *            the model ID
	 */
	private void victory(int modelID) {
		PlayerModel pM = gameLogic.getBoard().getPlayer(modelID);
		serverOutputHandler.victory("Spieler " + pM.getName() + " hat das Spiel gewonnen.",
				modelPlayerIdMap.get(modelID));
		server.disconnectServer();
		server.closeSocket();
		System.exit(0);
	}

	/**
	 * builds a street is called by the server controller.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param dir
	 *            the dir
	 * @param threadID
	 *            the thread ID
	 */
	public void requestBuildStreet(int x, int y, int dir, int threadID) {
		int modelID = threadPlayerIdMap.get(threadID);

		if (InitialStreetCounter < amountPlayers * 2) {
			requestBuildInitialStreet(x, y, dir, threadID);
		} else {
			if (!(gameLogic.isActionForbidden(modelID, currentPlayer, PlayerState.TRADING_OR_BUILDING)
					^ gameLogic.isActionForbidden(modelID, currentExtraPlayer, PlayerState.BUILDING))) {
				serverResponse(modelID, "Unzulässige Aktion");
			} else {
				if (gameLogic.checkBuildStreet(x, y, dir, modelID)) {
					serverResponse(modelID, "OK");
					buildStreet(x, y, dir, modelID);
					subFromPlayersResources(modelID, DefaultSettings.STREET_BUILD_COST);
					resourceStackIncrease(DefaultSettings.STREET_BUILD_COST);
					costsToAll(modelID, DefaultSettings.STREET_BUILD_COST, true);
					serverOutputHandler.buildStreet(x, y, dir, threadID);
					// statusUpdate(modelID);

				} else {
					serverResponse(modelID, "Kein Straßenbau möglich");
				}
			}
		}

	}

	/**
	 * builds a street at the specified position checks if street changes
	 * longest trading route.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param dir
	 *            the dir
	 * @param modelID
	 *            the model ID
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
	 * checks if a village interrupts a street set.
	 *
	 * @param c
	 *            the c
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
							StreetSet newSet1 = new StreetSet(modelID, new ArrayList<Edge>());
							newSet1.addEdge(streetEdges.get(0));
							StreetSet newSet2 = new StreetSet(modelID, new ArrayList<Edge>());
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
	 * don't use this method, if the edge has no street!!!.
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
	 * adds a specified street (edge object) to a street set.
	 *
	 * @param e
	 *            the e
	 * @param modelID
	 *            the model ID
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

	/**
	 * Check longest trading route.
	 *
	 * @param modelID
	 *            the model ID
	 */
	public void checkLongestTradingRoute(int modelID) {
		int max = getLongestTradingRoute(modelID);
		longestRoutes[modelID] = max;
		if (max >= 5) {
			if (longestTradingRoutePlayer != -1) {
				if (longestTradingRoutePlayer != modelID && longestRoutes[longestTradingRoutePlayer] < max) {
					gameLogic.getBoard().getPlayer(longestTradingRoutePlayer).setHasLongestRoad(false);
					decreaseVictoryPoints(longestTradingRoutePlayer, 2);
					gameLogic.getBoard().getPlayer(modelID).setHasLongestRoad(true);

					increaseVictoryPoints(modelID, 2);

					longestTradingRoutePlayer = modelID;
					serverOutputHandler.longestRoad(modelPlayerIdMap.get(modelID));
				}
			} else {
				gameLogic.getBoard().getPlayer(modelID).setHasLongestRoad(true);
				increaseVictoryPoints(modelID, 2);
				longestTradingRoutePlayer = modelID;
				serverOutputHandler.longestRoad(modelPlayerIdMap.get(modelID));
			}
		} else {
			if (longestTradingRoutePlayer == modelID) { // special case
				gameLogic.getBoard().getPlayer(modelID).setHasLongestRoad(false);
				decreaseVictoryPoints(modelID, 2);
				longestTradingRoutePlayer = -1;
				serverOutputHandler.longestRoad(null);
			}
		}

		logger.info("Calculated longest Trading Route: Player = " + modelID + " Length = " + max);

	}

	/**
	 * DON'T CALL THIS WHEN PLAYER HAS NO STREETS!!! calculates the longest
	 * trading route for a specific player with streets.
	 *
	 * @param modelID
	 *            the model ID
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
	 * specific edge.
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
	 * checks if the player has the most played knight cards.
	 *
	 * @param modelID
	 *            to chaeck player
	 */
	private void checkLargestArmy(int modelID) {
		PlayerModel pM = gameLogic.getBoard().getPlayer(modelID);
		if (!pM.hasLargestArmy()) {
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
					if (biggestKnightForcePlayer != -1) {
						gameLogic.getBoard().getPlayer(biggestKnightForcePlayer).setHasLargestArmy(false);
						decreaseVictoryPoints(biggestKnightForcePlayer, 2);
					}
					pM.setHasLargestArmy(true);
					biggestKnightForcePlayer = modelID;
					increaseVictoryPoints(modelID, 2);
					serverOutputHandler.biggestKnightProwess(modelID);
				}
			}
		}
	}

	/**
	 * is called by the serverController after a build request from a client
	 * builds a city.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param dir
	 *            the dir
	 * @param threadID
	 *            the thread ID
	 */
	public void requestBuildCity(int x, int y, int dir, int threadID) {
		int modelID = threadPlayerIdMap.get(threadID);
		if (!(gameLogic.isActionForbidden(modelID, currentPlayer, PlayerState.TRADING_OR_BUILDING)
				^ gameLogic.isActionForbidden(modelID, currentExtraPlayer, PlayerState.BUILDING))) {
			serverResponse(modelID, "Unzulässige Aktion");
		} else {
			if (gameLogic.checkBuildCity(x, y, dir, modelID)) {

				serverResponse(modelID, "OK");
				buildCity(x, y, dir, modelID);

				subFromPlayersResources(modelID, DefaultSettings.CITY_BUILD_COST);
				resourceStackIncrease(DefaultSettings.CITY_BUILD_COST);
				increaseVictoryPoints(modelID, 1);

				costsToAll(modelID, DefaultSettings.CITY_BUILD_COST, true);
				serverOutputHandler.buildCity(x, y, dir, threadID);

				// statusUpdate(modelID);
			}
		}

	}

	/**
	 * Builds the city.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param dir
	 *            the dir
	 * @param modelID
	 *            the model ID
	 */
	private void buildCity(int x, int y, int dir, int modelID) {
		Corner c = gameLogic.getBoard().getCornerAt(x, y, dir);
		c.setStatus(enums.CornerStatus.CITY);
		c.setOwnerID(modelID);
		gameLogic.getBoard().getPlayer(modelID).increaseAmountVillages();
		gameLogic.getBoard().getPlayer(modelID).decreaseAmountCities();
	}

	/**
	 * Request buy dev card.
	 *
	 * @param threadID
	 *            the thread ID
	 */
	public void requestBuyDevCard(int threadID) {
		int modelID = threadPlayerIdMap.get(threadID);
		if (!(gameLogic.isActionForbidden(modelID, currentPlayer, PlayerState.TRADING_OR_BUILDING)
				^ gameLogic.isActionForbidden(modelID, currentExtraPlayer, PlayerState.BUILDING))) {
			serverResponse(modelID, "Unzulässige Aktion");
		} else {
			if (gameLogic.checkBuyDevCard(modelID)) {
				PlayerModel pm = gameLogic.getBoard().getPlayer(modelID);

				subFromPlayersResources(modelID, DefaultSettings.DEVCARD_BUILD_COST);
				costsToAll(modelID, DefaultSettings.DEVCARD_BUILD_COST, true);
				resourceStackIncrease(DefaultSettings.DEVCARD_BUILD_COST);

				DevelopmentCard devCard = gameLogic.getBoard().getDevCardStack().getNextCard();
				
				if (devCard.getName().equals("Victory Card")) {
					increaseHiddenVictoryPoints(modelID, 1);
					pm.incrementPlayerDevCard(devCard);
					statusUpdateToPlayer(modelID, modelID);
				} else {
				    pm.getDevCardsBoughtInThisRound().add(devCard);
				}    
				
				for (int i = 0; i < amountPlayers; i++) {
					if (i == modelID) {
						serverOutputHandler.boughtDevelopmentCard(threadID, devCard, threadID);
					} else {
						serverOutputHandler.boughtDevelopmentCard(threadID, null, modelPlayerIdMap.get(i));
					}
				}

			} else {
				serverResponse(modelID, "Unzulässige Aktion");
			}
		}
	}

	/**
	 * Is called by serverController when there is a street build request during
	 * the initial phase.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param dir
	 *            the dir
	 * @param threadID
	 *            the thread ID
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
	 * initial Phase.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param dir
	 *            the dir
	 * @param threadID
	 *            the thread ID
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

				increaseVictoryPoints(modelID, 1);
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
	 * is called when client has finished his turn.
	 *
	 * @param playerID
	 *            the player ID
	 */
	public void endTurn(int playerID) {
		Integer modelID = threadPlayerIdMap.get(playerID);
		// XOR
		if ((modelID == currentPlayer
				&& !gameLogic.isActionForbidden(modelID, currentPlayer, PlayerState.TRADING_OR_BUILDING))
				^ modelID == currentExtraPlayer) {
			PlayerModel pM = gameLogic.getBoard().getPlayer(modelID);
			pM.setPlayerState(PlayerState.WAITING);
			ArrayList<DevelopmentCard> currDevCards = pM.getDevCardsBoughtInThisRound();
			if (currDevCards != null) {
				for (int i = 0; i < currDevCards.size(); i++) {
					// erst jetzt kann spieler über development card
					// verfügen
					pM.incrementPlayerDevCard(currDevCards.get(i));
				}
				pM.getDevCardsBoughtInThisRound().clear();
			}
			// runde zu ende, nächste runde darf dev card gespielt werden
			pM.setHasPlayedDevCard(false);
			statusUpdate(modelID);

			if (modelID == currentExtraPlayer) {
				currentExtraPlayer = getNextPlayer(currentExtraPlayer);
				if (currentExtraPlayer == currentPlayer) {
					// ende der ausserordentlichen Bauphase
					currentExtraPlayer = null;
					currentPlayer = getNextPlayer(currentPlayer);
					gameLogic.getBoard().getPlayer(currentPlayer).setPlayerState(PlayerState.DICEROLLING);
					statusUpdate(currentPlayer);
				} else {
					gameLogic.getBoard().getPlayer(currentExtraPlayer).setPlayerState(PlayerState.BUILDING);
					statusUpdate(currentExtraPlayer);
				}
			} else {
				if (FiveSixGame) {
					currentExtraPlayer = getNextPlayer(modelID); // next players
																	// turn
					gameLogic.getBoard().getPlayer(currentExtraPlayer).setPlayerState(PlayerState.BUILDING);
					statusUpdate(currentExtraPlayer);
				} else {
					currentPlayer = getNextPlayer(modelID);
					gameLogic.getBoard().getPlayer(currentPlayer).setPlayerState(PlayerState.DICEROLLING);
					statusUpdate(currentPlayer);
				}
			}

		} else {
			serverResponse(modelID, "Unzulässige Aktion");
		}

	}

	/**
	 * is called when client sends a robber loss message checks if action is
	 * valid sends status updates.
	 *
	 * @param threadID
	 *            the thread ID
	 * @param resources
	 *            the resources
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
				costsToAll(modelID, resources, false);
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
				serverResponse(modelID, "You haven't specified enough resources");
			}
		} else {
			serverResponse(modelID, "You dont have the specified resources");
		}

	}

	/**
	 * Sends a robberMovementRequest to all clients.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param victimThreadID
	 *            the victim thread ID
	 * @param currentThreadID
	 *            the current thread ID
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
			String location = Board.getCoordToStringMap().get(new Index(x, y));
			gameLogic.getBoard().setBandit(location);
			serverOutputHandler.robberMovement(currentThreadID, location, victimThreadID);
			gameLogic.getBoard().getPlayer(modelID).setPlayerState(PlayerState.TRADING_OR_BUILDING);
			statusUpdate(modelID);
		}

	}

	/**
	 * Basic methods for trading.
	 *
	 * @param threadID
	 *            the thread ID
	 * @param offer
	 *            the offer
	 * @param demand
	 *            the demand
	 */

	public void requestSeaTrade(int threadID, int[] offer, int[] demand) {
		int modelID = threadPlayerIdMap.get(threadID);
		tradeController.requestSeaTrade(modelID, offer, demand);
	}

	/**
	 * Client offers trade.
	 *
	 * @param threadID
	 *            the thread ID
	 * @param supply
	 *            the supply
	 * @param demand
	 *            the demand
	 */
	public void clientOffersTrade(int threadID, int[] supply, int[] demand) {
		tradeController.clientOffersTrade(threadPlayerIdMap.get(threadID), supply, demand);
	}

	/**
	 * Send client offer.
	 *
	 * @param modelID
	 *            the model ID
	 * @param tradingID
	 *            the trading ID
	 * @param supply
	 *            the supply
	 * @param demand
	 *            the demand
	 */
	public void sendClientOffer(int modelID, int tradingID, int[] supply, int[] demand) {
		serverOutputHandler.tradePreview(modelPlayerIdMap.get(modelID), tradingID, supply, demand);
	}

	/**
	 * Accept trade.
	 *
	 * @param threadID
	 *            the thread ID
	 * @param tradingID
	 *            the trading ID
	 * @param accept
	 *            the accept
	 */
	public void acceptTrade(int threadID, int tradingID, boolean accept) {
		tradeController.acceptTrade(threadPlayerIdMap.get(threadID), tradingID, accept);
	}

	/**
	 * Trade accepted.
	 *
	 * @param modelID
	 *            the model ID
	 * @param tradingID
	 *            the trading ID
	 * @param accepted
	 *            the accepted
	 */
	public void tradeAccepted(int modelID, int tradingID, boolean accepted) {
		serverOutputHandler.tradeConfirmation(modelPlayerIdMap.get(modelID), tradingID, accepted);
	}

	/**
	 * Fulfill trade.
	 *
	 * @param threadID
	 *            the thread ID
	 * @param tradingID
	 *            the trading ID
	 * @param partnerThreadID
	 *            the partner thread ID
	 */
	public void fulfillTrade(int threadID, int tradingID, int partnerThreadID) {
		tradeController.fulfillTrade(threadPlayerIdMap.get(threadID), tradingID,
				threadPlayerIdMap.get(partnerThreadID));
	}

	/**
	 * Trade fulfilled.
	 *
	 * @param modelID
	 *            the model ID
	 * @param partnerModelID
	 *            the partner model ID
	 */
	public void tradeFulfilled(int modelID, int partnerModelID) {
		serverOutputHandler.tradeIsCompleted(modelPlayerIdMap.get(modelID), modelPlayerIdMap.get(partnerModelID));
	}

	/**
	 * Cancel trade.
	 *
	 * @param threadID
	 *            the thread ID
	 * @param tradingID
	 *            the trading ID
	 */
	public void cancelTrade(int threadID, int tradingID) {
		tradeController.cancelTrade(threadPlayerIdMap.get(threadID), tradingID);
	}

	/**
	 * Trade cancelled.
	 *
	 * @param modelID
	 *            the model ID
	 * @param tradingID
	 *            the trading ID
	 */
	public void tradeCancelled(int modelID, int tradingID) {
		serverOutputHandler.tradeIsCanceled(modelPlayerIdMap.get(modelID), tradingID);
	}

	/**
	 * Play knight card.
	 *
	 * @param threadID
	 *            the thread ID
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param victimThreadID
	 *            the victim thread ID
	 */
	public void playKnightCard(int threadID, int x, int y, Integer victimThreadID) {
		int modelID = threadPlayerIdMap.get(threadID);
		PlayerModel pM = gameLogic.getBoard().getPlayer(modelID);
		if (!gameLogic.checkPlayDevCard(modelID, currentPlayer, CardType.KNIGHT)) {
			serverResponse(modelID, "Ungültiger Spielzug");
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
				String location = Board.getCoordToStringMap().get(new Index(x, y));
				gameLogic.getBoard().setBandit(location);
				serverOutputHandler.robberMovement(threadID, location, victimThreadID);
				serverOutputHandler.knightCardPlayed(threadID, location, victimThreadID);

				pM.incrementPlayedKnightCards();
				pM.setHasPlayedDevCard(true);
				checkLargestArmy(modelID);
			} else {
				serverResponse(modelID, "Ungültige Eingabe");
			}
		}

	}

	/**
	 * Play street card.
	 *
	 * @param threadID
	 *            the thread ID
	 * @param x1
	 *            the x 1
	 * @param y1
	 *            the y 1
	 * @param dir1
	 *            the dir 1
	 * @param x2
	 *            the x 2
	 * @param y2
	 *            the y 2
	 * @param dir2
	 *            the dir 2
	 */
	public void playStreetCard(int threadID, int x1, int y1, int dir1, Integer x2, Integer y2, Integer dir2) {
		int modelID = threadPlayerIdMap.get(threadID);
		if (!gameLogic.checkPlayDevCard(modelID, currentPlayer, CardType.STREET)) {
			serverResponse(modelID, "Ungültiger Spielzug");
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
				serverResponse(modelID, "Eine Straße konnte nicht gebaut werden");
			}
			if (x2 != null) {
				if (gameLogic.checkBuildStreet(x2, y2, dir2, modelID)) {
					serverResponse(modelID, "OK");
					buildStreet(x2, y2, dir2, modelID);
					serverOutputHandler.buildStreet(x2, y2, dir2, modelID);
				} else {
					serverResponse(modelID, "Eine Straße konnte nicht gebaut werden");
				}
			}
			// resourcen für gameLogic wieder abziehen
			subFromPlayersResources(modelID, DefaultSettings.STREET_BUILD_COST);
			subFromPlayersResources(modelID, DefaultSettings.STREET_BUILD_COST);
			gameLogic.getBoard().getPlayer(modelID).setHasPlayedDevCard(true);
		}

	}

	/**
	 * Play monopoly card.
	 *
	 * @param threadID
	 *            the thread ID
	 * @param resType
	 *            the res type
	 */
	public void playMonopolyCard(int threadID, ResourceType resType) {
		int modelID = threadPlayerIdMap.get(threadID);
		if (!gameLogic.checkPlayDevCard(modelID, currentPlayer, CardType.MONOPOLY)) {
			serverResponse(modelID, "Ungültiger Spielzug");
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

	/**
	 * Play invention card.
	 *
	 * @param threadID
	 *            the thread ID
	 * @param resources
	 *            the resources
	 */
	public void playInventionCard(int threadID, int[] resources) {
		int modelID = threadPlayerIdMap.get(threadID);
		if (!gameLogic.checkPlayDevCard(modelID, currentPlayer, CardType.INVENTION)) {
			serverResponse(modelID, "Ungültiger Spielzug");
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
				serverResponse(modelID, "Zu viele Resourcen angegeben");
				resourceStackIncrease(obtain);
			} else {
				addToPlayersResource(modelID, obtain);
				obtainToAll(modelID, obtain, false);
				gameLogic.getBoard().getPlayer(modelID).setHasPlayedDevCard(true);
			}
		}

	}

	/**
	 * Generates the resource and the dice index of each field calls gui via
	 * setField to set the correct graphics if randomDesert is set then the
	 * desert will be placed random at the board, else it will be set in the
	 * middle.
	 *
	 * @param initialField
	 *            gameLogic.getBoard()ram randomDesert
	 * @param randomDesert
	 *            the random desert
	 */
	private void generateBoard(String initialField, boolean randomDesert) {
		String fields = HexService.getSpiral(initialField);
		Board currBoard = gameLogic.getBoard();
		int[] cards = DefaultSettings.landscapeAmount;
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
							DefaultSettings.diceNumbers[diceInd]);
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
						DefaultSettings.diceNumbers[i]);
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

	/**
	 * Generate debugging board.
	 */
	// DEBUGGING ONLY
	public void generateDebuggingBoard() {

		for (String key : Board.getStringToCoordMap().keySet()) {
			int coords[] = Board.getStringToCoordMap().get(key);
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
	 * sets new player resources after dice roll.
	 *
	 * @param diceNum
	 *            the dice num
	 */
	public void gainBoardResources(int diceNum) {
		ArrayList<Field> diceFields = new ArrayList<Field>();
		for (Map.Entry<String, int[]> entry : Board.getStringToCoordMap().entrySet()) {
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
		// statusUpdateForAllPlayers();
	}

	/**
	 * decreases the resource stack.
	 *
	 * @param resType
	 *            the res type
	 * @return true, if successful
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
	 * increases the resource stack.
	 *
	 * @param resType
	 *            the res type
	 */
	protected void resourceStackIncrease(ResourceType resType) {
		int resIndex = DefaultSettings.RESOURCE_VALUES.get(resType);
		resourceStack[resIndex]++;
	}

	/**
	 * increases the resource stack with resource array.
	 *
	 * @param resources
	 *            the resources
	 */
	protected void resourceStackIncrease(int[] resources) {
		for (int i = 0; i < resources.length; i++) {
			for (int j = 0; j < resources[i]; j++) {
				resourceStackIncrease(DefaultSettings.RESOURCE_ORDER[i]);
			}
		}
	}

	/**
	 * Gain first board resources.
	 *
	 * @param modelID
	 *            the model ID
	 * @param c
	 *            the c
	 */
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

	}

	/**
	 * gets next player in the player order.
	 *
	 * @param modelPlayerID
	 *            the model player ID
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
	 * gets player resources.
	 *
	 * @param modelPlayerID
	 *            the model player ID
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
	 * sets player resources.
	 *
	 * @param modelID
	 *            the model ID
	 * @param resources
	 *            the resources
	 */
	@Deprecated
	@SuppressWarnings("unused")
	private void setPlayerResources(int modelID, int[] resources) {
		gameLogic.getBoard().getPlayer(modelID).setResources(resources);
	}

	/**
	 * adds a single resource to players resource.
	 *
	 * @param playerID
	 *            the player ID
	 * @param resType
	 *            the res type
	 * @param amount
	 *            the amount
	 */
	public void addToPlayersResource(int playerID, ResourceType resType, int amount) {
		int[] resources = getPlayerResources(playerID);
		for (int i = 0; i < amount; i++) {
			resources[DefaultSettings.RESOURCE_VALUES.get(resType)]++;
		}
		gameLogic.getBoard().getPlayer(playerID).setResources(resources);
	}

	/**
	 * adds resources to player resource.
	 *
	 * @param playerID
	 *            the player ID
	 * @param resourcesToAdd
	 *            the resources to add
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
	 * subtracts resources from players resource.
	 *
	 * @param playerID
	 *            the player ID
	 * @param costs
	 *            the costs
	 */
	public void subFromPlayersResources(int playerID, int[] costs) {
		int[] pResources = getPlayerResources(playerID);
		for (int i = 0; i < costs.length; i++) {
			pResources[i] = pResources[i] - costs[i];
		}
		gameLogic.getBoard().getPlayer(playerID).setResources(pResources);
	}

	/**
	 * Sub from players resources.
	 *
	 * @param playerID
	 *            the player ID
	 * @param resType
	 *            the res type
	 * @param amount
	 *            the amount
	 */
	public void subFromPlayersResources(int playerID, ResourceType resType, int amount) {
		int[] resources = getPlayerResources(playerID);
		for (int i = 0; i < amount; i++) {
			resources[DefaultSettings.RESOURCE_VALUES.get(resType)]--;
		}
		gameLogic.getBoard().getPlayer(playerID).setResources(resources);
	}

	/**
	 * Gets the amount players.
	 *
	 * @return the amount players
	 */
	public int getAmountPlayers() {
		return amountPlayers;
	}

	/**
	 * Sets the amount players.
	 *
	 * @param amountPlayers
	 *            the new amount players
	 */
	public void setAmountPlayers(int amountPlayers) {
		this.amountPlayers = amountPlayers;
	}

	/**
	 * Gets the server output handler.
	 *
	 * @return the server output handler
	 */
	public ServerOutputHandler getServerOutputHandler() {
		return serverOutputHandler;
	}

	/**
	 * Gets the server input handler.
	 *
	 * @return the server input handler
	 */
	public ServerInputHandler getServerInputHandler() {
		return serverInputHandler;
	}

	/**
	 * Connection lost.
	 *
	 * @param threadID
	 *            the thread ID
	 */
	public void connectionLost(int threadID) {
		Integer modelID = threadPlayerIdMap.get(threadID);
		/*
		 * PlayerModel pM = new PlayerModel(); pM = pM.
		 */
		if (modelID != null) {
			if (currentPlayer != null) {
				gameLogic.getBoard().getPlayer(modelID).setPlayerState(PlayerState.CONNECTION_LOST);
				statusUpdate(modelID);
				Integer currTID;
				for (int i = 0; i < amountPlayers; i++) {
					currTID = modelPlayerIdMap.get(i);
					if (currTID != null) {
						serverOutputHandler.victory("Verbindung zu einem Spieler verloren!", -1);
						server.closeSocket();
						System.exit(0);
					}
				}

			} else { // während lobbybetrieb disconnect möglich
				// zuerst noch ein letztes statusupdate
				lobbyPlayers.get(modelID).setPlayerState(PlayerState.CONNECTION_LOST);
				for (int i = 0; i < amountPlayers; i++) {
					if (i != modelID) {
						lobbyStatusUpdate(modelID, i);
					}
				}
				lobbyPlayers.remove(modelID);

				// danach entfernen aus dem speicher
				// PlayerModel[] playerModels =
				// gameLogic.getBoard().getPlayerModels();
				// PlayerModel tempPM = playerModels[amountPlayers - 1];
				int tempThreadID = modelPlayerIdMap.get(amountPlayers - 1);
				// temp ModelID = i;
				// verschiebe alle PlayerModels um 1 nach unten bis zu
				// spieler der sich disconnected hat...
				for (int i = amountPlayers - 2; i >= modelID; i--) {
					// PlayerModel currPM = playerModels[i];
					// playerModels[i] = tempPM;
					// tempPM = currPM;

					int newTID = modelPlayerIdMap.get(i);
					modelPlayerIdMap.replace(i, tempThreadID);
					threadPlayerIdMap.replace(tempThreadID, i);
					tempThreadID = newTID;

				}
				// resette den letzten Eintrag
				// playerModels[amountPlayers - 1] = new PlayerModel();
				modelPlayerIdMap.remove(amountPlayers - 1);
				threadPlayerIdMap.remove(threadID);

				amountPlayers--;

			}
		}

	}

	/**
	 * Send invalid JSON.
	 *
	 * @param currentThreadID
	 *            the current thread ID
	 */
	public void sendInvalidJSON(int currentThreadID) {
		serverResponse(currentThreadID, "Unzulässige Aktion");

	}

	/**
	 * Gets the server.
	 *
	 * @return the server
	 */
	public Server getServer() {
		return server;
	}

	/**
	 * Gets the thread player id map.
	 *
	 * @return the thread player id map
	 */
	public Map<Integer, Integer> getThreadPlayerIdMap() {
		return threadPlayerIdMap;
	}

	/**
	 * Gets the board.
	 *
	 * @return the board
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * Gets the robber loss counter.
	 *
	 * @return the robber loss counter
	 */
	public int getRobberLossCounter() {
		return robberLossCounter;
	}

	/**
	 * Sets the robber loss counter.
	 *
	 * @param i
	 *            the new robber loss counter
	 */
	public void setRobberLossCounter(int i) {
		robberLossCounter = i;

	}

	public void disconnectServer() {
		System.out.print("ServerController");
		server.disconnectServer();
	}

	public boolean isLongestTurnEnabled() {
		return longestTurnEnabled;
	}

	public void setLongestTurnEnabled(boolean longestTurnEnabled) {
		this.longestTurnEnabled = longestTurnEnabled;
	}

}
