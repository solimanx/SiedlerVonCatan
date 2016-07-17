/*
 *
 */
package network.client.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;

import audio.Soundeffects;
import enums.Color;
import enums.PlayerState;
import enums.ResourceType;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.board.Board;
import model.board.Corner;
import model.board.Edge;
import model.board.Field;
import model.board.PlayerModel;
import model.card.DevelopmentCard;
import model.card.InventionCard;
import model.card.KnightCard;
import model.card.MonopolyCard;
import model.card.StreetBuildingCard;
import model.card.UnknownCard;
import model.unit.Index;
import model.unit.TradeOffer;
import network.ModelToProtocol;
import network.ProtocolToModel;
import network.client.client.Client;
import network.client.client.ClientInputHandler;
import network.client.client.ClientOutputHandler;
import network.client.view.PlayerResourceUpdateRunnable;
import network.client.view.PlayerStatusGUIUpdate;
import service.GameLogic;
import settings.DefaultSettings;

// TODO: Auto-generated Javadoc
/**
 * The central point between InputHandler and OutputHandler for the client.
 */
public class ClientController {
	/**
	 * e.g. (0 -> 45), (1, -> 32) etc. HashMap mapping modelIDs to playerIDs
	 */
	private Map<Integer, Integer> modelPlayerIdMap;

	/**
	 * e.g. (45-> 0), (32, -> 1) etc. HashMap mapping playerIDs to modelIDs
	 */
	private Map<Integer, Integer> threadPlayerIdMap;
	private static org.apache.logging.log4j.Logger logger = LogManager
			.getLogger(ClientController.class.getSimpleName());
	private Board board;
	private GameLogic gameLogic;
	private Integer ownTradingID;
	private int ownModelID;
	private int amountPlayers = 1;

	protected ViewController viewController;

	private ClientOutputHandler clientOutputHandler;
	private ClientInputHandler clientInputHandler;

	protected Client client;
	private int initialRoundCount = 0;
	private PlayerState currentState;

	private ArrayList<TradeOffer> tradeOffers = new ArrayList<TradeOffer>();

	private String theme;

	/**
	 * Default constructor for the ClientController.
	 *
	 * @param primaryStage
	 *            the primary stage
	 * @param theme
	 *            the theme
	 */
	public ClientController(Stage primaryStage, String theme) {
		// ModelPlayerID => threadID
		modelPlayerIdMap = new HashMap<Integer, Integer>();

		// threadID => ModelPlayerID
		threadPlayerIdMap = new HashMap<Integer, Integer>();

		this.theme = theme;
		this.clientInputHandler = new ClientInputHandler(this);
		this.viewController = new ViewController(primaryStage, this, this.theme);
		this.board = new Board();
		this.gameLogic = new GameLogic(board);

	}

	/**
	 * Load up the client.
	 *
	 * @param serverHost
	 *            the server host
	 * @param port
	 *            the port
	 */
	public void connectToServer(String serverHost, int port) {
		if (client != null) {
			viewController.getLobbyController().clearChat();
			client.stopScanning();
		}
		this.client = new Client(clientInputHandler, serverHost, port);
		this.clientOutputHandler = new ClientOutputHandler(client);
		client.start();
	}

	// ================================================================================
	// RECEIVE
	// ================================================================================

	/**
	 * Check if versions match and act accordingly; if they match begin sending
	 * confirmation from client otherwise disconnect.
	 *
	 * @param serverVersion
	 *            the server version
	 * @param protocolVersion
	 *            the protocol version
	 */
	public void receiveHello(String serverVersion, String protocolVersion) {
		if (!protocolVersion.equals(settings.DefaultSettings.PROTOCOL_VERSION)) {
			client.stopClient();
			logger.error("Invalid Protocol Version; Disconnected");
		} else {
			clientOutputHandler.sendHello(DefaultSettings.CLIENT_VERSION);
		}

	}

	/**
	 * After receiving an ID from the server, assign to self , confirm the
	 * handshake and and enable chat in the lobby.
	 *
	 * @param playerID
	 *            the player ID
	 */
	public void receiveWelcome(int playerID) {
		// playerID is 42,35, etc.
		setOwnModelID(playerID);
		// add myself to hashmap
		threadPlayerIdMap.put(playerID, 0);
		modelPlayerIdMap.put(0, playerID);
		logger.info("Handshake complete!");
		viewController.getLobbyController().enableChat();
	}

	/**
	 * Sending the Server response up to the view.
	 *
	 * @param serverResponse
	 *            the server response
	 */
	public void receiveServerConfirmation(String serverResponse) {
		switch (currentState) {
		case GAME_STARTING:
		case WAITING_FOR_GAMESTART:
		default:
			viewController.setServerResponse(serverResponse);
		}
	}

	/**
	 * Displaying the received chat message to the client.
	 *
	 * @param playerID
	 *            the player ID
	 * @param message
	 *            the message
	 */
	public void receiveChatMessage(Integer playerID, String message) {
		if (viewController.isChoosingStage && playerID == null) {
			viewController.messageReceive("Error: " + message);

		} else if (playerID != null) {
			Integer modelID = threadPlayerIdMap.get(playerID);
			if (modelID == null) {
				viewController.messageReceive("Player " + playerID.toString() + ": " + message);
			} else {
				String playerName = gameLogic.getBoard().getPlayer(modelID).getName();
				if (!playerName.equals("")) {
					viewController.messageReceive("Player " + playerName + ": " + message);
				} else {
					viewController.messageReceive("Player " + playerID.toString() + ": " + message);
				}
			}

		} else {
			viewController.messageReceive("Server: " + message);
		}
	}

	/**
	 * Receiving an error message from the server and displaying it to the
	 * client.
	 *
	 * @param notice
	 *            the notice
	 */
	public void receiveError(String notice) {
		viewController.getLobbyController().setServerColorAnswer(notice);
		logger.debug(notice);
	}

	/**
	 * Receiving a new status update message, and updating the client through
	 * it.
	 *
	 * @param threadID
	 *            the thread ID
	 * @param color
	 *            the color
	 * @param name
	 *            the name
	 * @param status
	 *            the status
	 * @param victoryPoints
	 *            the victory points
	 * @param resources
	 *            the resources
	 */
	public void receiveStatusUpdate(int threadID, enums.Color color, String name, enums.PlayerState status,
			int victoryPoints, int[] resources, Integer devCards) {
		Integer modelID = threadPlayerIdMap.get(threadID);
		currentState = status;
		switch (status) {
		case CONNECTION_LOST:
			if (modelID != null) {
				threadPlayerIdMap.remove(modelID);
				modelPlayerIdMap.remove(threadID);
				// TODO remove from playermodel[] of board
			}
			break;
		case GAME_STARTING:
			// addToPlayersResource(modelID, resources);#
			viewController.getLobbyController().updatePlayer(threadID, name, color, status);
			break;
		case WAITING_FOR_GAMESTART:
			viewController.getLobbyController().updatePlayer(threadID, name, color, status);
			// if player wasn't saved in list
			if (modelID == null) {
				// e.g. (42 -> 0), (13 -> 1) etc.
				threadPlayerIdMap.put(threadID, amountPlayers);
				// e.g. (0 -> 42), (1 -> 13) etc.
				modelPlayerIdMap.put(amountPlayers, threadID);
				modelID = amountPlayers;
				amountPlayers++;
			}
		default:
			// five six
			if (modelID > 3) {
				PlayerModel[] oldPM = gameLogic.getBoard().getPlayerModels();
				Board.extendBoard();
				gameLogic.setBoard(new Board());
				gameLogic.getBoard().extendPlayers(oldPM);
			}
			PlayerModel pM = gameLogic.getBoard().getPlayer(modelID);
			if (pM.getColor() == null) {
				pM.setColor(color);
				pM.setName(name);
			}
			pM.setPlayerState(status);
			pM.setVictoryPoints(victoryPoints);
			// TODO this should be done ONCE

			if (viewController.getGameViewController() != null) {
				// and here..
				// modelID = 0,1,2,3
				Platform.runLater(new PlayerStatusGUIUpdate(modelID, viewController.getGameViewController(),
						victoryPoints, status, resources, devCards));
			}
		}

		// Hier get bei Break weiter
	}

	/**
	 * After receiving how the board appears, translate it to how the board is
	 * created in the model, and initialize all field types, harbour locations,
	 * buildings.
	 *
	 * @param serverFields
	 *            the server fields
	 * @param corners
	 *            the corners
	 * @param streets
	 *            the streets
	 * @param harbourCorners
	 *            the harbour corners
	 * @param banditLocation
	 *            the bandit location
	 */
	public void initializeBoard(Field[] serverFields, Corner[] corners, ArrayList<Edge> streets,
			Corner[] harbourCorners, String banditLocation) {
		if (serverFields.length == 52) {
			Board.extendBoard();
			Board board = new Board();
			gameLogic.setBoard(board);
		}
		for (Field f : serverFields) {
			String location = f.getFieldID();
			int[] coords = ProtocolToModel.getFieldCoordinates(location);
			Field bField = gameLogic.getBoard().getFieldAt(coords[0], coords[1]);
			bField.setFieldID(location);
			bField.setDiceIndex(f.getDiceIndex());
			;
			bField.setResourceType(f.getResourceType());
		}
		for (Corner c : corners) {
			String location = c.getCornerID();
			int coords[] = ProtocolToModel.getCornerCoordinates(location);
			Corner bCorner = gameLogic.getBoard().getCornerAt(coords[0], coords[1], coords[2]);
			bCorner.setCornerID(location);
			bCorner.setOwnerID(c.getOwnerID());
			bCorner.setStatus(c.getStatus());
		}
		for (Edge s : streets) {
			String location = s.getEdgeID();
			int coords[] = ProtocolToModel.getEdgeCoordinates(location);
			Edge bEdge = gameLogic.getBoard().getEdgeAt(coords[0], coords[1], coords[2]);
			bEdge.setEdgeID(location);
			bEdge.setHasStreet(s.isHasStreet());
			bEdge.setOwnedByPlayer(s.getOwnerID());
		}
		for (Corner c : harbourCorners) {
			String location = c.getCornerID();
			int[] coords = ProtocolToModel.getCornerCoordinates(location);
			Corner bCorner = gameLogic.getBoard().getCornerAt(coords[0], coords[1], coords[2]);
			bCorner.setCornerID(location);
			bCorner.setHarbourStatus(c.getHarbourStatus());
			board.setHarbourCorner(harbourCorners);
		}

		gameLogic.getBoard().setBandit(banditLocation);
		setOwnModelID(threadPlayerIdMap.get(getOwnModelID()));
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				logger.debug("STARTING GAME VIEW IN Platform.runLater");
				// TODO vielleicht ein fix?
				viewController.startGameView();

			}
		});
	}

	/**
	 * Receive dice roll result.
	 *
	 * @param playerID
	 *            the player ID
	 * @param result
	 *            the result
	 */
	public void receiveDiceRollResult(int playerID, int[] result) {
		// TODO WARNING, PLAYERID NOTHING GETS DONE WITH IT
		if (viewController.getGameViewController() != null) {
			int res = result[0] + result[1];
			viewController.getGameViewController().setDiceRollResult(threadPlayerIdMap.get(playerID), res);
		}

	}

	// ================================================================================
	// SEND
	// ================================================================================

	/**
	 * Send a message to the server.
	 *
	 * @param message
	 *            the message
	 */
	public void sendChatMessage(String message) {
		clientOutputHandler.sendChatMessage(message);
	}

	/**
	 * Send player profile to the server.
	 *
	 * @param name
	 *            the name
	 * @param color
	 *            the color
	 */
	public void sendPlayerProfile(String name, Color color) {
		clientOutputHandler.sendPlayerProfile(name, color);
	}

	/**
	 * Send the server the information that the client is ready to start
	 * playing.
	 *
	 */
	public void sendReady() {
		clientOutputHandler.sendReady();
	}

	// ================================================================================
	// RELAY TO VIEW
	// ================================================================================

	/**
	 * Starting up the view, and assigning the players and fields.
	 */
	public void initializeGUI() {

		for (int i = 0; i < amountPlayers; i++) {
			viewController.getGameViewController().initPlayer(i, gameLogic.getBoard().getPlayer(i).getName(),
					gameLogic.getBoard().getPlayer(i).getColor());
			viewController.getGameViewController().setResourceCards(i, getPlayerResources(i));
			viewController.getGameViewController().setVictoryPoints(i,
					gameLogic.getBoard().getPlayer(i).getVictoryPoints());
			viewController.getGameViewController().setPlayerStatus(i,
					gameLogic.getBoard().getPlayer(i).getPlayerState());
		}
		for (Map.Entry<String, int[]> entry : Board.getStringToCoordMap().entrySet()) {
			int[] coord = entry.getValue();
			Field f = gameLogic.getBoard().getFieldAt(coord[0], coord[1]);
			viewController.getGameViewController().setField(coord[0], coord[1], f.getResourceType(), f.getDiceIndex());
			// TODO set Harbour Fields in GameView. angleFactor * 60° edge,field
			// -> int
			// viewController.getGameViewController().setHarbour(int
			// angleFactor, int u, int v, enums.HarbourStatus);
		}
		Corner[] hCorners = board.getHarbourCorners();
		viewController.getGameViewController().setHarbour(hCorners);

		int[] banditCorners = Board.getStringToCoordMap().get(gameLogic.getBoard().getBandit());
		viewController.getGameViewController().setBandit(banditCorners[0], banditCorners[1]);

		viewController.getGameViewController().startResourceUpdater();

	}

	/**
	 * Adds the to players resource.
	 *
	 * @param playerID
	 *            the player ID
	 * @param resourcesGained
	 *            the resources gained
	 */
	@Deprecated
	public void addToPlayersResource(int playerID, int[] resourcesGained) {
		// playerID = 0,1,2,3
		// if it's self
		if (playerID == 0) {
			// safety check
			if (resourcesGained.length == 5) {
				// playerID := 0,1,2,3
				gameLogic.getBoard().getPlayer(playerID).incrementResources(resourcesGained);
				// Platform.runLater(new ServerResponseRunnable("You gained
				// ...", viewController.getGameViewController()));

			} else {
				logger.warn("Throws new IllegalArgumentException \"Error at adding player resource to player\"");
				throw new IllegalArgumentException("Error at adding player resource to player");
			}

		}
		// other players
		else {

			// safety check
			if (resourcesGained.length == 1) {
				gameLogic.getBoard().getPlayer(playerID).incrementHiddenResources(resourcesGained[0]);
			} else {
				logger.warn("Throws new IllegalArgumentException \"Error at adding player resource to player\"");
				throw new IllegalArgumentException("Error at adding player resources to player");
			}
		}

	}

	// ================================================================================
	// GETTERS AND SETTERS
	// ================================================================================
	/**
	 * Returns the game logic object.
	 *
	 * @return the game logic
	 */
	public GameLogic getGameLogic() {
		return gameLogic;
	}

	/**
	 * Gets the client ouptput handler.
	 *
	 * @return the client ouptput handler
	 */
	public ClientOutputHandler getClientOuptputHandler() {
		return clientOutputHandler;
	}

	/**
	 * Returns own ID.
	 *
	 * @return the own player ID
	 */
	public int getOwnModelID() {
		return ownModelID;
	}

	/**
	 * Sets own ID.
	 *
	 * @param ownPlayerID
	 *            the new own player ID
	 */
	public void setOwnModelID(int ownPlayerID) {
		this.ownModelID = ownPlayerID;
	}

	/**
	 * Returns the amount of players that are playing the game.
	 *
	 * @return the amount players
	 */
	public int getAmountPlayers() {
		return amountPlayers;
	}

	/**
	 * Sets the amount of players that are playing the game.
	 *
	 * @param amountPlayers
	 *            the new amount players
	 */
	public void setAmountPlayers(int amountPlayers) {
		this.amountPlayers = amountPlayers;
	}

	/**
	 * Resource obtain.
	 *
	 * @param playerID
	 *            the player ID
	 * @param resources
	 *            the resources
	 */
	public void resourceObtain(int playerID, int[] resources) {
		int modelID = threadPlayerIdMap.get(playerID);
		// if self
		if (modelID == 0) {
			gameLogic.getBoard().getPlayer(modelID).incrementResources(resources);
			viewController.getGameViewController().setResourceCards(modelID, getPlayerResources(modelID));
			Platform.runLater(new PlayerResourceUpdateRunnable(modelID, viewController.getGameViewController(),
					gameLogic.getBoard().getPlayer(modelID).getResources()));
		} // if someone else
		else {
			int resources2;
			if (resources.length == 5) {
				resources2 = resources[0] + resources[1] + resources[2] + resources[3] + resources[4];
			} else {
				resources2 = resources[0];
			}
			// increment their hiddenresources
			gameLogic.getBoard().getPlayer(modelID).incrementHiddenResources(resources2);
		}

	}

	/**
	 * Costs.
	 *
	 * @param threadID
	 *            the thread ID
	 * @param resources
	 *            the resources
	 */
	public void costs(int threadID, int[] resources) {
		int modelID = threadPlayerIdMap.get(threadID);
		// if self
		if (modelID == 0) {
			if (!Soundeffects.isMuted()) {
				Soundeffects.RESOURCE.play();
			}
			gameLogic.getBoard().getPlayer(modelID).decrementResources(resources);
			// viewController.getGameViewController().setResourceCards(modelID,
			// getPlayerResources(modelID));
			Platform.runLater(new PlayerResourceUpdateRunnable(modelID, viewController.getGameViewController(),
					gameLogic.getBoard().getPlayer(modelID).getResources()));
		} // if someone else
		else {
			int resources2;
			if (resources.length == 5) {
				resources2 = resources[0] + resources[1] + resources[2] + resources[3] + resources[4];
			} else {
				resources2 = resources[0];
			}
			// decrement their hiddenresources
			gameLogic.getBoard().getPlayer(modelID).decrementHiddenResources(resources2);
			// Platform.runLater(
			// new PlayerResourceUpdateRunnable(modelID,
			// viewController.getGameViewController(), hiddenResources));
		}
		// getPlayerHiddenResource(modelID));

	}

	// 8.6

	/**
	 * Builds the street.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param dir
	 *            the dir
	 * @param playerID
	 *            the player ID
	 */
	public void buildStreet(int x, int y, int dir, int playerID) {
		int modelID = threadPlayerIdMap.get(playerID);
		Edge e = gameLogic.getBoard().getEdgeAt(x, y, dir);
		e.setHasStreet(true);
		e.setOwnedByPlayer(gameLogic.getBoard().getPlayer(modelID).getID());
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				viewController.getGameViewController().setStreet(x, y, dir, modelID);
				// TODO Auto-generated method stub

			}
		});
	}

	// 8.6

	/**
	 * Builds the village.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param dir
	 *            the dir
	 * @param playerID
	 *            the player ID
	 */
	public void buildVillage(int x, int y, int dir, int playerID) {
		int modelID = threadPlayerIdMap.get(playerID);
		Corner c = gameLogic.getBoard().getCornerAt(x, y, dir);
		c.setStatus(enums.CornerStatus.VILLAGE);
		c.setOwnerID(modelID);
		Corner[] neighbors = gameLogic.getBoard().getAdjacentCorners(x, y, dir);
		for (int i = 0; i < neighbors.length; i++) {
			if (neighbors[i] != null) {
				neighbors[i].setStatus(enums.CornerStatus.BLOCKED);
			}
		}
		if (initialRoundCount < 2) {
			gameLogic.setInitialLastVillage(c);
		}
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				viewController.getGameViewController().setCorner(x, y, dir, enums.CornerStatus.VILLAGE, modelID);
				// remove other corners around it
				Corner[] aC = viewController.getClientController().getGameLogic().getBoard().getAdjacentCorners(x, y,
						dir);
				for (int i = 0; i < aC.length; i++) {
					if (aC[i] != null) {
						int[] coords = ProtocolToModel.getCornerCoordinates(aC[i].getCornerID());
						viewController.getGameViewController().removeVillage(coords[0], coords[1], coords[2]);
					}
				}

			}
		});
	}

	// 8.6

	/**
	 * Builds the city.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param dir
	 *            the dir
	 * @param playerID
	 *            the player ID
	 */
	public void buildCity(int x, int y, int dir, int playerID) {
		Corner c = gameLogic.getBoard().getCornerAt(x, y, dir);
		c.setStatus(enums.CornerStatus.CITY);
		c.setOwnerID(playerID);
		int modelID = threadPlayerIdMap.get(playerID);
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				viewController.getGameViewController().setCorner(x, y, dir, enums.CornerStatus.CITY, modelID);

			}
		});

	}

	// 9.1

	/**
	 * Dice roll request.
	 */
	public void diceRollRequest() {
		clientOutputHandler.sendDiceRollRequest();

	}

	// 9.3

	/**
	 * Request set bandit.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param stealFromPlayerId
	 *            the steal from player id
	 */
	public void requestSetBandit(int x, int y, Integer stealFromPlayerId) {
		clientOutputHandler.sendBanditRequest(x, y, modelPlayerIdMap.get(stealFromPlayerId));

	}

	// 9.4

	/**
	 * Request build village.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param dir
	 *            the dir
	 */
	public void requestBuildVillage(int x, int y, int dir) {
		// view saves coordinates from 0 to 7 system
		int radius = DefaultSettings.boardRadius;
		if (initialRoundCount < 2) {
			logger.info("Building Initial Village");
			requestBuildInitialVillage(x - radius, y - radius, dir);

		}
		if (gameLogic.checkBuildVillage(x - radius, y - radius, dir, ownModelID)) {
			clientOutputHandler.requestBuildVillage(x - radius, y - radius, dir);
		}
	}

	/**
	 * Request build initial village.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param dir
	 *            the dir
	 */
	public void requestBuildInitialVillage(int x, int y, int dir) {
		if (gameLogic.checkBuildInitialVillage(x, y, dir)) {
			clientOutputHandler.requestBuildVillage(x, y, dir);
		}
	}

	// 9.4

	/**
	 * Request build street.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param dir
	 *            the dir
	 */
	public void requestBuildStreet(int x, int y, int dir) {
		int radius = DefaultSettings.boardRadius;
		if (initialRoundCount < 2) {
			logger.info("Building Initial Road");
			requestBuildInitialStreet(x - radius, y - radius, dir);
		}
		if (gameLogic.checkBuildStreet(x - radius, y - radius, dir, ownModelID)) {
			clientOutputHandler.requestBuildStreet(x - radius, y - radius, dir);
		}

	}

	/**
	 * Request build initial street.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param dir
	 *            the dir
	 */
	public void requestBuildInitialStreet(int x, int y, int dir) {
		if (gameLogic.checkBuildInitialStreet(x, y, dir, ownModelID)) {
			clientOutputHandler.requestBuildStreet(x, y, dir);
			initialRoundCount++; // TODO: this should happen after server OK
		}
	}

	// 9.4

	/**
	 * Request build city.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param dir
	 *            the dir
	 */
	public void requestBuildCity(int x, int y, int dir) {
		clientOutputHandler.requestBuildCity(x, y, dir);
		// }
	}

	// 9.7

	/**
	 * End turn.
	 */
	public void endTurn() {
		clientOutputHandler.sendEndTurn();
	}

	// Protocol 0.2

	/**
	 * Robber loss.
	 *
	 * @param result
	 *            the result
	 */
	public void robberLoss(int[] result) {
		clientOutputHandler.sendRobberLoss(result);
	}
	// Protocol 0.3

	/**
	 * Basic methods for trading.
	 *
	 * @param offer
	 *            the offer
	 * @param demand
	 *            the demand
	 */

	public void requestTrade(int[] offer, int[] demand) {
		clientOutputHandler.requestTrade(offer, demand);
	}

	/**
	 * Show trade.
	 *
	 * @param threadID
	 *            the thread ID
	 * @param tradingID
	 *            the trading ID
	 * @param supply
	 *            the supply
	 * @param demand
	 *            the demand
	 */
	public void showTrade(int threadID, int tradingID, int[] supply, int[] demand) {
		int modelID = threadPlayerIdMap.get(threadID);
		TradeOffer tOf = new TradeOffer(threadID, tradingID, supply, demand);
		tradeOffers.add(tOf);
		if (modelID == ownModelID) {
			setOwnTradingID(tradingID);
			viewController.getGameViewController().getTradeViewController().addOwnOffer(supply, demand, tradingID);
		} else {
			viewController.getGameViewController().getTradeViewController().addOffer(supply, demand, tradingID,
					modelID);

		}
	}

	/**
	 * Accept trade.
	 *
	 * @param tradingID
	 *            the trading ID
	 */
	public void acceptTrade(int tradingID) {
		clientOutputHandler.acceptTrade(tradingID);
	}

	/**
	 * Decline trade.
	 *
	 * @param tradeID
	 *            the trade ID
	 */
	public void declineTrade(int tradeID) {
		clientOutputHandler.declineTrade(tradeID);
	}

	public void tradeDeclined(int threadID, int tradingID) {
		int modelID = threadPlayerIdMap.get(threadID);

		if (modelID == this.ownModelID) {
			if (getOwnTradingID() != null && getOwnTradingID() != tradingID)
				viewController.getGameViewController().getTradeViewController().setDeclined(tradingID);
		}
	}

	/**
	 * A Player has accepted your offer. Show acceptance on list
	 *
	 * @param threadID
	 *            the thread ID
	 * @param tradingID
	 *            the trading ID
	 */
	public void tradeAccepted(int threadID, int tradingID) {
		int modelID = threadPlayerIdMap.get(threadID);
		if (getOwnTradingID() != null && getOwnTradingID() == tradingID) {
			viewController.getGameViewController().getTradeViewController()
					.acceptingOffer(threadPlayerIdMap.get(threadID), tradingID);
		} else if (modelID == this.ownModelID) {
			viewController.getGameViewController().getTradeViewController().setAccepted(tradingID);
		}
	}

	/**
	 * You agree to the trading.
	 *
	 * @param tradingID
	 *            the trading ID
	 * @param partnerModelID
	 *            the partner model ID
	 */
	public void fulfillTrade(int tradingID, int partnerModelID) {
		clientOutputHandler.tradeComplete(tradingID, modelPlayerIdMap.get(partnerModelID));
	}

	/**
	 * trade was fulfilled.
	 *
	 * @param threadID
	 *            the thread ID
	 * @param partnerThreadID
	 *            the partner thread ID
	 */
	public void tradeFulfilled(int threadID, int partnerThreadID) {
		TradeOffer currTOf;
		int modelID = threadPlayerIdMap.get(threadID);
		int tradeID = 0;
		for (int i = 0; i < tradeOffers.size(); i++) {
			currTOf = tradeOffers.get(i);
			if (currTOf.getOwnerID() == modelID) {
				tradeID = tradeOffers.get(i).getTradingID();
				tradeOffers.remove(i);
			}
		}
		if (modelID == 0) {
			viewController.getGameViewController().getTradeViewController().offerFulfilled(modelID, partnerThreadID);
		} else {
			tradeCancelled(threadID, tradeID);
		}
	}

	/**
	 * Cancel trade.
	 *
	 * @param tradingID
	 *            the trading ID
	 */
	public void cancelTrade(int tradingID) {
		clientOutputHandler.cancelTrade(tradingID);
	}

	/**
	 * Trade cancelled.
	 *
	 * @param threadID
	 *            the thread ID
	 * @param tradingID
	 *            the trading ID
	 */
	public void tradeCancelled(int threadID, int tradingID) {
		TradeOffer currTOf;
		int modelID = threadPlayerIdMap.get(threadID);
		for (int i = 0; i < tradeOffers.size(); i++) {
			currTOf = tradeOffers.get(i);
			if (currTOf.getTradingID() == tradingID) {
				if (currTOf.getOwnerID() == modelID) {
					tradeOffers.remove(i);
				} else {
					for (int j = 0; j < currTOf.getAcceptingPlayers().size(); j++) {
						if (currTOf.getAcceptingPlayers().get(j) == modelID) {
							currTOf.getAcceptingPlayers().remove(j);
						}
					}
				}
				break;
			}
		}
		if (modelID != 0) {
			// if
			// (viewController.getGameViewController().getTradeViewController().tradeIDtoModelID.get(tradingID)
			// != null
			// &&
			// viewController.getGameViewController().getTradeViewController().tradeIDtoModelID
			// .get(tradingID) == modelID) {
			// viewController.getGameViewController().getTradeViewController().cancelOffer(tradingID);
			// }
			viewController.getGameViewController().getTradeViewController().cancelOffer(tradingID, modelID);
		} else {
			// if (ownTradingID != null && modelID == 0 && tradingID ==
			// ownTradingID) {
			// viewController.getGameViewController().getTradeViewController().cancelOwnOffer();
			// } else {
			// viewController.getGameViewController().getTradeViewController().cancelOffer(tradingID);
			// }
			viewController.getGameViewController().getTradeViewController().cancelOwnOffer();
		}
	}

	/**
	 * Request sea trade.
	 *
	 * @param offer
	 *            the offer
	 * @param demand
	 *            the demand
	 */
	public void requestSeaTrade(int[] offer, int[] demand) {
		clientOutputHandler.requestHarbourTrade(offer, demand);
	}

	/**
	 * Victory.
	 *
	 * @param message
	 *            the message
	 * @param threadID
	 *            the thread ID
	 */
	public void victory(String message, int threadID) {
		if (threadID != -1) {
			viewController.getGameViewController().showVictory(threadPlayerIdMap.get(threadID));
		} else {
			viewController.getGameViewController().alert(message + " Spieler: " + threadID);
		}
	}

	/**
	 * // * @param playerID.
	 *
	 * @param threadID
	 *            the thread ID
	 */
	public void largestArmy(int threadID) {
		gameLogic.getBoard().getPlayer(threadPlayerIdMap.get(threadID)).setHasLargestArmy(true);
		viewController.getGameViewController().setGreatestKnightForce(threadPlayerIdMap.get(threadID));

	}

	/**
	 * //* @param playerID.
	 *
	 * @param threadID
	 *            the thread ID
	 */
	public void longestRoad(Integer threadID) {
		if (threadID != null) {
			if (threadID == ownModelID) {
				gameLogic.getBoard().getPlayer(threadPlayerIdMap.get(threadID)).setHasLongestRoad(true);
			} else {
				gameLogic.getBoard().getPlayer(threadPlayerIdMap.get(threadID)).setHasLongestRoad(false);
			}
			viewController.getGameViewController().setLongestTradeRoad(threadPlayerIdMap.get(threadID));
		}
		// special case, if player loses to a settlement block by another player
		else {
			gameLogic.getBoard().getPlayer(threadPlayerIdMap.get(threadID)).setHasLargestArmy(false);
			viewController.getGameViewController().setLongestTradeRoad(-1);
		}

	}

	/**
	 * Request buy development card.
	 */
	public void requestBuyDevelopmentCard() {
		// if (gameLogic.checkBuyDevCard(0)) {
		clientOutputHandler.buyDevelopmentCard();
		// }
	}

	/**
	 * //* @param resource.
	 *
	 * @param resources
	 *            the resources
	 */
	public void playInventionCard(int[] resources) {
		clientOutputHandler.playInventionCard(resources);
	}

	/**
	 * Play monopoly card.
	 *
	 * @param resType
	 *            the res type
	 */
	public void playMonopolyCard(ResourceType resType) {
		clientOutputHandler.playMonopolyCard(resType);
	}

	/**
	 * Play street build card.
	 *
	 * @param u1
	 *            the u 1
	 * @param v1
	 *            the v 1
	 * @param dir1
	 *            the dir 1
	 * @param u2
	 *            the u 2
	 * @param v2
	 *            the v 2
	 * @param dir2
	 *            the dir 2
	 */
	public void playStreetBuildCard(int u1, int v1, int dir1, int u2, int v2, int dir2) {
		int radius = DefaultSettings.boardRadius;
		String road1 = ModelToProtocol.getEdgeID(u1 - radius, v1 - radius, dir1);
		String road2 = ModelToProtocol.getEdgeID(u2 - radius, v2 - radius, dir2);
		clientOutputHandler.playRoadCard(road1, road2);
	}

	/**
	 * Play knight card.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param modelID
	 *            the model ID
	 */
	public void playKnightCard(int x, int y, Integer modelID) {
		clientOutputHandler.playKnightCard(x, y, modelPlayerIdMap.get(modelID));

	}

	/**
	 * Gets the player hidden resource.
	 *
	 * @param playerID
	 *            the player ID
	 * @return the player hidden resource
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private int[] getPlayerHiddenResource(int playerID) {
		int[] resource = new int[] { gameLogic.getBoard().getPlayer(playerID).getHiddenResources() };
		return resource;
	}

	/**
	 * Gets the player resources.
	 *
	 * @param playerID
	 *            the player ID
	 * @return the player resources
	 */
	private int[] getPlayerResources(int playerID) {
		int[] resources = gameLogic.getBoard().getPlayer(playerID).getResources();
		return resources;
	}

	/**
	 * Adds the to deck.
	 *
	 * @param playerID
	 *            the player ID
	 * @param devCard
	 *            the dev card
	 */
	public void addToDeck(int playerID, DevelopmentCard devCard) {
		int modelID = threadPlayerIdMap.get(playerID);
		gameLogic.getBoard().getPlayer(modelID).incrementPlayerDevCard(devCard);
	}

	/**
	 * Robber move.
	 *
	 * @param index
	 *            the index
	 */
	public void robberMove(Index index) {
		String location = ProtocolToModel.getProtocolOneID(index);
		gameLogic.getBoard().setBandit(location);
		int[] coords = ProtocolToModel.getFieldCoordinates(location);
		// TODO display user and victim popup
		viewController.getGameViewController().setBandit(coords[0], coords[1]);
	}

	/**
	 * Receive road card.
	 *
	 * @param playerID
	 *            the player ID
	 * @param locationID1
	 *            the location ID 1
	 * @param locationID2
	 *            the location ID 2
	 */
	public void receiveRoadCard(int playerID, Index[] locationID1, Index[] locationID2) {
		// TODO Auto-generated method stub

	}

	/**
	 * Receive monopoly card.
	 *
	 * @param playerID
	 *            the player ID
	 * @param rt
	 *            the rt
	 */
	public void receiveMonopolyCard(int playerID, ResourceType rt) {
		// TODO Auto-generated method stub

	}

	/**
	 * Receive invention card.
	 *
	 * @param playerID
	 *            the player ID
	 * @param resource
	 *            the resource
	 */
	public void receiveInventionCard(int playerID, int[] resource) {
		// TODO Auto-generated method stub

	}

	/**
	 * Gets the own trading ID.
	 *
	 * @return the ownTradingID
	 */
	public Integer getOwnTradingID() {
		return ownTradingID;
	}

	/**
	 * Sets the own trading ID.
	 *
	 * @param ownTradingID
	 *            the ownTradingID to set
	 */
	public void setOwnTradingID(Integer ownTradingID) {
		this.ownTradingID = ownTradingID;
	}

	/**
	 * Gets the player ID.
	 *
	 * @param modelID
	 *            the model ID
	 * @return the player ID
	 */
	public int getPlayerID(int modelID) {
		return modelPlayerIdMap.get(modelID);
	}

	/**
	 * Removes the from deck.
	 *
	 * @param playerID
	 *            the player ID
	 * @param knightCard
	 *            the knight card
	 */
	public void removeFromDeck(int playerID, KnightCard knightCard) {
		int modelID = threadPlayerIdMap.get(playerID);
		if (modelID == getOwnModelID()) {
			gameLogic.getBoard().getPlayer(modelID).decrementPlayerDevCard(knightCard);

		} else {
			gameLogic.getBoard().getPlayer(modelID).decrementPlayerDevCard(new UnknownCard());
		}
		gameLogic.getBoard().getPlayer(modelID).incrementPlayedKnightCards();

	}

	/**
	 * Removes the from deck.
	 *
	 * @param playerID
	 *            the player ID
	 * @param streetBuildCard
	 *            the street build card
	 */
	public void removeFromDeck(int playerID, StreetBuildingCard streetBuildCard) {
		int modelID = threadPlayerIdMap.get(playerID);
		if (modelID == getOwnModelID()) {
			gameLogic.getBoard().getPlayer(modelID).decrementPlayerDevCard(streetBuildCard);
		} else {
			gameLogic.getBoard().getPlayer(modelID).decrementPlayerDevCard(new UnknownCard());
		}
	}

	/**
	 * Removes the from deck.
	 *
	 * @param playerID
	 *            the player ID
	 * @param monopolyCard
	 *            the monopoly card
	 */
	public void removeFromDeck(int playerID, MonopolyCard monopolyCard) {
		int modelID = threadPlayerIdMap.get(playerID);
		if (modelID == getOwnModelID()) {
			gameLogic.getBoard().getPlayer(modelID).decrementPlayerDevCard(monopolyCard);
		} else {
			gameLogic.getBoard().getPlayer(modelID).decrementPlayerDevCard(new UnknownCard());

		}
	}

	/**
	 * Removes the from deck.
	 *
	 * @param playerID
	 *            the player ID
	 * @param inventionCard
	 *            the invention card
	 */
	public void removeFromDeck(int playerID, InventionCard inventionCard) {
		int modelID = threadPlayerIdMap.get(playerID);
		if (modelID == getOwnModelID()) {
			gameLogic.getBoard().getPlayer(modelID).decrementPlayerDevCard(inventionCard);

		} else {
			gameLogic.getBoard().getPlayer(modelID).decrementPlayerDevCard(new UnknownCard());
		}
	}

	/**
	 * Gets the view controller.
	 *
	 * @return the view controller
	 */
	public ViewController getViewController() {
		return viewController;
	}

}
