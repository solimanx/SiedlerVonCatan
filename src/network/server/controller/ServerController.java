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
import model.HexService;
import model.objects.Corner;
import model.objects.Edge;
import model.objects.Field;
import model.objects.PlayerModel;
import network.ProtocolToModel;
import network.client.controller.ViewController;
import network.server.server.Server;
import network.server.server.ServerInputHandler;
import network.server.server.ServerOutputHandler;
import protocol.messaging.ProtocolServerConfirmation;
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
	private int InitialStreetCounter;
	private ArrayList<Corner> initialVillages;
	private int currentPlayer;

	public ServerController() {
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
		PlayerModel playerModel = new PlayerModel(playerID);
		playerModel.setPlayerState(PlayerState.GAME_STARTING);
		tempPlayers.add(playerModel);

		welcome(playerID);
		for (int i = 0; i < tempPlayers.size(); i++) {
			if (i == playerID) {
				int[] resources = { 0, 0, 0, 0, 0 };
				serverOutputHandler.statusUpdate(currentThreadID, null, null, PlayerState.GAME_STARTING, 0, resources,
						i);
			} else {
				int[] resources = { 0 };
				serverOutputHandler.statusUpdate(currentThreadID, null, null, PlayerState.GAME_STARTING, 0, resources,
						i);
			}
		}
		for (int i = 0; i < tempPlayers.size(); i++) {
			PlayerModel currPM;
			if (i != playerID) {
				currPM = tempPlayers.get(i);
				int[] resources = { currPM.getResourceCards().size() };
				serverOutputHandler.statusUpdate(modelPlayerIdMap.get(i), currPM.getColor(), currPM.getName(),
						currPM.getPlayerState(), 0, resources, currentThreadID);
			}

		}

	}

	public void welcome(int modelPlayerID) {
		serverOutputHandler.welcome(modelPlayerIdMap.get(modelPlayerID));
	}

	public void clientReady(int currentThreadID) {
		int playerID = threadPlayerIdMap.get(currentThreadID);
		for (int i = 0; i < tempPlayers.size(); i++) {
			if (tempPlayers.get(i).getID() == playerID) {
				tempPlayers.get(i).setPlayerState(PlayerState.WAITING_FOR_GAMESTART);
			}
		}

		for (int i = 0; i < tempPlayers.size(); i++) {
			if (i == playerID) {
				int[] resources = { 0, 0, 0, 0, 0 };
				serverOutputHandler.statusUpdate(currentThreadID, null, null, PlayerState.WAITING_FOR_GAMESTART, 0,
						resources, i);
			} else {
				int[] resources = { 0 };
				serverOutputHandler.statusUpdate(currentThreadID, null, null, PlayerState.WAITING_FOR_GAMESTART, 0,
						resources, i);
			}
		}

		if (tempPlayers.size() >= 3 && tempPlayers.size() == server.getClientCounter()) {
			boolean allReady = true;
			for (int i = 0; i < tempPlayers.size(); i++) {
				if (tempPlayers.get(i).getPlayerState() != PlayerState.WAITING_FOR_GAMESTART) {
					allReady = false;
					break;
				}
			}
			if (allReady) {
				initializeBoard();
			}
		}

	}

	public void serverResponse(int threadPlayerID, String server_response) {
		serverOutputHandler.serverConfirm(server_response, threadPlayerID);

	}

	private void error(int threadPlayerID, String string) {
		serverOutputHandler.error(string, threadPlayerID);

	}

	public void playerProfileUpdate(Color color, String name, int currentThreadID) {

		boolean colorAvailable = true;
		Color currColor;
		for (int i = 0; i < tempPlayers.size(); i++) {
			currColor = tempPlayers.get(i).getColor();
			if (currColor != null) {
				if (currColor.equals(color)) {
					colorAvailable = false;
					break;
				}
			}
		}
		if (colorAvailable) {
			int playerModelID = threadPlayerIdMap.get(currentThreadID);
			for (int i = 0; i < tempPlayers.size(); i++) {
				if (tempPlayers.get(i).getID() == playerModelID) {
					tempPlayers.get(i).setColor(color);
					tempPlayers.get(i).setName(name);
					tempPlayers.get(i).setPlayerState(PlayerState.GAME_STARTING);
				}
			}

			for (int i = 0; i < tempPlayers.size(); i++) {
				if (i == playerModelID) {
					int[] resources = { 0, 0, 0, 0, 0 };
					serverOutputHandler.statusUpdate(currentThreadID, color, name, PlayerState.GAME_STARTING, 0,
							resources, i);
				} else {
					int[] resources = { 0 };
					serverOutputHandler.statusUpdate(currentThreadID, color, name, PlayerState.GAME_STARTING, 0,
							resources, i);
				}
			}
			serverResponse(currentThreadID, "OK");
		} else {
			error(currentThreadID, "Farbe bereits vergeben!");
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
			int[] resources = { gameLogic.getBoard().getPlayer(playerModelID).getResourceCards().size() };
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
		Board board = new Board(tempPlayers);
		this.gameLogic = new GameLogic(board);
		generateBoard("A", true);
		serverOutputHandler.initBoard(amountPlayers, gameLogic.getBoard());

		gameLogic.getBoard().getPlayer(0).setPlayerState(PlayerState.BUILDING_VILLAGE);
		statusUpdate(0); // firstPlayers turn
		currentPlayer = 0;
		for (int i = 1; i < amountPlayers; i++) {
			gameLogic.getBoard().getPlayer(i).setPlayerState(PlayerState.WAITING);
			statusUpdate(i);
		}
		InitialStreetCounter = 0;

	}

	public void diceRollRequest(int playerID) {
		if (gameLogic.checkIfActionIsAllowed(threadPlayerIdMap.get(playerID), currentPlayer, PlayerState.DICEROLLING)) {
			serverResponse(playerID, "Unzulässige Aktion");
		} else {
			Random rand = new Random();
			int firstDice = 1 + rand.nextInt(5);
			int secondDice = 1 + rand.nextInt(5);
			int[] result = { firstDice, secondDice };
			serverOutputHandler.diceRollResult(playerID, result);

			int modelID = threadPlayerIdMap.get(playerID);
			PlayerModel pM = gameLogic.getBoard().getPlayer(modelID);
			if (firstDice + secondDice == 7) {
				PlayerModel currPM;
				boolean hasToWait = false;
				for (int i = 0; i < amountPlayers; i++) {
					currPM = gameLogic.getBoard().getPlayer(i);
					if (currPM.getResourceCards().size() > 7) {
						currPM.setPlayerState(PlayerState.DISPENSE_CARDS_ROBBER_LOSS);
						statusUpdate(i);
						hasToWait = true;
					}
				}
				if (!hasToWait) {
					pM.setPlayerState(PlayerState.MOVE_ROBBER);
					statusUpdate(modelID);
				}

			} else {
				gainBoardResources(firstDice + secondDice);
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

	public void requestBuildVillage(int x, int y, int dir, int playerID) {
		if (gameLogic.checkIfActionIsAllowed(threadPlayerIdMap.get(playerID), currentPlayer,
				PlayerState.BUILDING_VILLAGE)) {
			serverResponse(playerID, "Unzulässige Aktion");
		} else {
			if (InitialStreetCounter < amountPlayers * 2) {
				requestBuildInitialVillage(x, y, dir, playerID);
			} else {
				int modelPID = threadPlayerIdMap.get(playerID);
				if (gameLogic.checkBuildVillage(x, y, dir, modelPID)) {
					Corner c = gameLogic.getBoard().getCornerAt(x, y, dir);
					c.setStatus(enums.CornerStatus.VILLAGE);
					c.setOwnerID(modelPID);
					gameLogic.getBoard().getPlayer(modelPID).decreaseAmountVillages();
					Corner[] neighbors = gameLogic.getBoard().getAdjacentCorners(x, y, dir);
					for (int i = 0; i < neighbors.length; i++) {
						if (neighbors[i] != null) {
							neighbors[i].setStatus(enums.CornerStatus.BLOCKED);
						}
					}

					subFromPlayersResources(modelPID, DefaultSettings.VILLAGE_BUILD_COST);

					serverOutputHandler.buildVillage(x, y, dir, playerID);
					serverOutputHandler.costs(playerID, DefaultSettings.VILLAGE_BUILD_COST);
					statusUpdate(modelPID);
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
	public void requestBuildStreet(int x, int y, int dir, int playerID) {
		if (gameLogic.checkIfActionIsAllowed(threadPlayerIdMap.get(playerID), currentPlayer,
				PlayerState.BUILDING_STREET)) {
			serverResponse(playerID, "Unzulässige Aktion");
		} else {
			if (InitialStreetCounter < amountPlayers * 2) {
				requestBuildInitialStreet(x, y, dir, playerID);
			} else {
				int modelPID = threadPlayerIdMap.get(playerID);
				if (gameLogic.checkBuildStreet(x, y, dir, modelPID)) {
					Edge e = gameLogic.getBoard().getEdgeAt(x, y, dir);
					e.setHasStreet(true);
					e.setOwnedByPlayer(gameLogic.getBoard().getPlayer(modelPID).getID());
					gameLogic.getBoard().getPlayer(modelPID).decreaseAmountStreets();

					subFromPlayersResources(modelPID, DefaultSettings.STREET_BUILD_COST);

					serverOutputHandler.buildStreet(x, y, dir, playerID);
					serverOutputHandler.costs(playerID, DefaultSettings.STREET_BUILD_COST);
					statusUpdate(modelPID);
				} else {
					error(playerID, "Kein Straßenbau möglich");
				}
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
	public void requestBuildCity(int x, int y, int dir, int playerID) {
		if (gameLogic.checkIfActionIsAllowed(threadPlayerIdMap.get(playerID), currentPlayer,
				PlayerState.BUILDING_VILLAGE)) {
			serverResponse(playerID, "Unzulässige Aktion");
		} else {
			int modelPID = threadPlayerIdMap.get(playerID);
			if (gameLogic.checkBuildCity(x, y, dir, modelPID)) {
				Corner c = gameLogic.getBoard().getCornerAt(x, y, dir);
				c.setStatus(enums.CornerStatus.CITY);
				c.setOwnerID(modelPID);
				gameLogic.getBoard().getPlayer(modelPID).increaseAmountVillages();
				gameLogic.getBoard().getPlayer(modelPID).decreaseAmountCities();

				subFromPlayersResources(modelPID, settings.DefaultSettings.CITY_BUILD_COST);

				serverOutputHandler.buildCity(x, y, dir, playerID);
				serverOutputHandler.costs(playerID, DefaultSettings.CITY_BUILD_COST);
				statusUpdate(modelPID);
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
	public void requestBuildInitialStreet(int x, int y, int dir, int playerID) {
		if (gameLogic.checkIfActionIsAllowed(threadPlayerIdMap.get(playerID), currentPlayer,
				PlayerState.BUILDING_STREET)) {
			serverResponse(playerID, "Unzulässige Aktion");
		} else {
			if (gameLogic.checkBuildInitialStreet(x, y, dir, playerID)) {
				int modelPlayerID = threadPlayerIdMap.get(playerID);
				Edge e = gameLogic.getBoard().getEdgeAt(x, y, dir);
				e.setHasStreet(true);
				e.setOwnedByPlayer(modelPlayerID);
				gameLogic.getBoard().getPlayer(modelPlayerID).decreaseAmountStreets();

				serverOutputHandler.buildStreet(x, y, dir, playerID);
				InitialStreetCounter++;
				if (InitialStreetCounter >= amountPlayers * 2) {
					gainFirstBoardResources();
				} else {
					gameLogic.getBoard().getPlayer(modelPlayerID).setPlayerState(PlayerState.WAITING);
					statusUpdate(modelPlayerID);

					int nextPlayer = getNextPlayer(modelPlayerID);
					gameLogic.getBoard().getPlayer(nextPlayer).setPlayerState(PlayerState.BUILDING_VILLAGE);
					statusUpdate(nextPlayer);
				}
			}
		}
	}

	/**
	 * is called by serverController when there is a Building Request during the
	 * initial Phase
	 * 
	 * @param x
	 * @param y
	 * @param dir
	 * @param playerID
	 */
	public void requestBuildInitialVillage(int x, int y, int dir, int playerID) {
		if (gameLogic.checkIfActionIsAllowed(threadPlayerIdMap.get(playerID), currentPlayer,
				PlayerState.BUILDING_VILLAGE)) {
			serverResponse(playerID, "Unzulässige Aktion");
		} else {
			if (gameLogic.checkBuildInitialVillage(x, y, dir)) {
				int modelPlayerID = threadPlayerIdMap.get(playerID);
				Corner c = gameLogic.getBoard().getCornerAt(x, y, dir);
				c.setStatus(enums.CornerStatus.VILLAGE);
				c.setOwnerID(modelPlayerID);
				gameLogic.getBoard().getPlayer(modelPlayerID).decreaseAmountVillages();
				Corner[] neighbors = gameLogic.getBoard().getAdjacentCorners(x, y, dir);
				for (int i = 0; i < neighbors.length; i++) {
					if (neighbors[i] != null) {
						neighbors[i].setStatus(enums.CornerStatus.BLOCKED);
					}
				}
				initialVillages.add(c);
				serverOutputHandler.buildVillage(x, y, dir, playerID);
				gameLogic.getBoard().getPlayer(modelPlayerID).setPlayerState(PlayerState.BUILDING_STREET);
				statusUpdate(modelPlayerID);
			}
		}
	}

	public void requestSetBandit(int x, int y, int stealFromPlayerID, int playerID) {
		// TODO Auto-generated method stub

	}

	/**
	 * Sends a robberMovementRequest to server
	 * 
	 * @param x
	 * @param y
	 * @param victim_id
	 * @param currentThreadID
	 */
	public void robberMovementRequest(int x, int y, int victim_id, int currentThreadID) {
		if (gameLogic.checkSetBandit(x, y, victim_id)) {

		}

	}

	public void setBandit(int x, int y, int playerID) {
		if (gameLogic.checkSetBandit(x, y, playerID)) {
			// board.setBandit(board.getFieldAt(x, y));

			// viewController.getMainViewController().setBandit(x, y); // Debug
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
				gameLogic.getBoard().getFieldAt(coords[0], coords[1]).setFieldID("" + fields.charAt(i));
				if (currNum != 5) {
					gameLogic.getBoard().setFieldAt(coords[0], coords[1], DefaultSettings.RESOURCE_ORDER[currNum],
							DefaultSettings.DICE_NUMBERS[diceInd]);
					diceInd++;
				} else {
					gameLogic.getBoard().setFieldAt(coords[0], coords[1], DefaultSettings.RESOURCE_ORDER[currNum],
							null);
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
				gameLogic.getBoard().getFieldAt(coords[0], coords[1]).setFieldID("" + fields.charAt(i));
				gameLogic.getBoard().setFieldAt(coords[0], coords[1], DefaultSettings.RESOURCE_ORDER[currNum],
						DefaultSettings.DICE_NUMBERS[i]);
			}
			int[] coords = ProtocolToModel.getFieldCoordinates("" + fields.charAt(fields.length() - 1));
			gameLogic.getBoard().getFieldAt(coords[0], coords[1]).setFieldID("" + fields.charAt(fields.length() - 1));
			gameLogic.getBoard().setFieldAt(coords[0], coords[1], ResourceType.NOTHING, null);

		}
		String outerRing = gameLogic.getBoard().getOuterRing();
		for (int i = 0; i < outerRing.length(); i++) {
			int[] coords = ProtocolToModel.getFieldCoordinates("" + outerRing.charAt(i));
			gameLogic.getBoard().getFieldAt(coords[0], coords[1]).setFieldID("" + outerRing.charAt(i));
			gameLogic.getBoard().setFieldAt(coords[0], coords[1], ResourceType.SEA, null);
		}
		gameLogic.getBoard().setBandit("J");
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

		for (Field f : diceFields) {
			if (f.getFieldID() != gameLogic.getBoard().getBandit()) {
				int[] coords = gameLogic.getBoard().getFieldCoordinates(f.getFieldID());
				Corner[] neighbors = gameLogic.getBoard().getSurroundingCorners(coords[0], coords[1]);
				for (int i = 0; i < neighbors.length; i++) {
					switch (neighbors[i].getStatus()) {
					case VILLAGE:
						addToPlayersResource(neighbors[i].getOwnerID(), f.getResourceType(), 1);
						break;
					case CITY:
						addToPlayersResource(neighbors[i].getOwnerID(), f.getResourceType(), 2);
						break;
					default:
					}
				}
			}
		}
		statusUpdateForAllPlayers();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.controller.GameControllerInterface#buildVillage(int, int,
	 * int, int)
	 */

	private void gainFirstBoardResources() {
		for (int i = amountPlayers; i < amountPlayers * 2; i++) {
			int[] coords = ProtocolToModel.getCornerCoordinates(initialVillages.get(i).getCornerID());
			Field[] connFields = gameLogic.getBoard().getConnectedFields(coords[0], coords[1], coords[2]);
			for (int j = 0; j < connFields.length; j++) {
				addToPlayersResource(initialVillages.get(i).getOwnerID(), connFields[j].getResourceType(), 1);
			}
		}
		statusUpdateForAllPlayers();
	}

	private int getNextPlayer(int modelPlayerID) {
		if (modelPlayerID - 1 >= amountPlayers) {
			return 0;
		} else {
			return modelPlayerID + 1;
		}
	}

	private int[] getPlayerResources(int playerID) {
		ArrayList<ResourceType> resources = tempPlayers.get(playerID).getResourceCards();
		int[] result = new int[5];
		for (ResourceType r : resources) {
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

	public void setGameState() {
		// überflüssig?

	}

	public PlayerState getPlayerState(int playerID) {
		// überflüssig?
		return null;
	}

}
