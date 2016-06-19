package network.client.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;

import enums.Color;
import enums.PlayerState;
import enums.ResourceType;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.Board;
import model.GameLogic;
import model.Index;
import model.objects.Corner;
import model.objects.Edge;
import model.objects.Field;
import model.objects.PlayerModel;
import model.objects.DevCards.DevelopmentCard;
import network.ProtocolToModel;
import network.client.client.Client;
import network.client.client.ClientInputHandler;
import network.client.client.ClientOutputHandler;
import network.client.view.PlayerStatusGUIUpdate;
import network.client.view.ServerResponseRunnable;
import settings.DefaultSettings;

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
	private int ownPlayerID;
	private int amountPlayers = 1;

	protected ViewController viewController;

	private ClientOutputHandler clientOutputHandler;
	private ClientInputHandler clientInputHandler;

	protected Client client;
	private int initialRoundCount = 0;
	private String currentServerResponse;
	private PlayerState currentState;

	private ArrayList<TradeOffer> tradeOffers = new ArrayList<TradeOffer>();

	/**
	 * Default constructor for the ClientController.
	 *
	 * @param primaryStage
	 */
	public ClientController(Stage primaryStage) {
		// ModelPlayerID => threadID
		modelPlayerIdMap = new HashMap<Integer, Integer>();

		// threadID => ModelPlayerID
		threadPlayerIdMap = new HashMap<Integer, Integer>();

		this.clientInputHandler = new ClientInputHandler(this);
		this.viewController = new ViewController(primaryStage, this);
		this.board = new Board();
		this.gameLogic = new GameLogic(board);

	}

	/**
	 * Load up the client.
	 *
	 */
	public void connectToServer(String serverHost, int port) {
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
	 * @param protocolVersion
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
	 */
	public void receiveWelcome(int playerID) {
		// playerID is 42,35, etc.
		setOwnPlayerID(playerID);
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
	 */
	public void receiveServerConfirmation(String serverResponse) {
		// TODO client confirm in later protocols
		currentServerResponse = serverResponse;
		switch (currentState) {
		case GAME_STARTING:
		case WAITING_FOR_GAMESTART:
			viewController.setServerResponse(serverResponse);
		default:
			viewController.setServerResponse(serverResponse);
		}
	}

	/**
	 * Displaying the received chat message to the client.
	 *
	 * @param playerID
	 * @param message
	 */
	public void receiveChatMessage(Integer playerID, String message) {
		if (playerID != null) {
			viewController.messageReceive("Spieler "
					+ gameLogic.getBoard().getPlayer(threadPlayerIdMap.get(playerID)).getName() + ": " + message);

		} else {
			viewController.messageReceive("Server: " + message);
		}
	}

	/**
	 * Receiving an error message from the server and displaying it to the
	 * client.
	 *
	 * @param notice
	 */
	public void receiveError(String notice) {
		// TODO
		System.out.println(notice);
		logger.debug(notice);
	}

	/**
	 * Receiving a new status update message, and updating the client through
	 * it.
	 *
	 * @param threadID
	 * @param color
	 * @param name
	 * @param status
	 * @param victoryPoints
	 * @param resources
	 */
	public void receiveStatusUpdate(int threadID, enums.Color color, String name, enums.PlayerState status,
			int victoryPoints, int[] resources) {
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
			// addToPlayersResource(modelID, resources);
			break;
		case WAITING_FOR_GAMESTART:
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
						victoryPoints, status, resources));
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
	 * @param corners
	 * @param streets
	 * @param harbourCorners
	 * @param banditLocation
	 */
	public void initializeBoard(Field[] serverFields, Corner[] corners, ArrayList<Edge> streets,
			Corner[] harbourCorners, String banditLocation) {

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
		setOwnPlayerID(threadPlayerIdMap.get(getOwnPlayerID()));
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
	 *
	 * @param playerID
	 * @param result
	 */
	public void receiveDiceRollResult(int playerID, int[] result) {
		// TODO WARNING, PLAYERID NOTHING GETS DONE WITH IT
		if (viewController.getGameViewController() != null) {
			int res = result[0] + result[1];
			viewController.getGameViewController().setDiceRollResult(res);
		}

	}

	// ================================================================================
	// SEND
	// ================================================================================

	/**
	 * Send a message to the server.
	 *
	 * @param message
	 */
	public void sendChatMessage(String message) {
		clientOutputHandler.sendChatMessage(message);
	}

	/**
	 * Send player profile to the server.
	 *
	 * @param name
	 * @param color
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
		for (Map.Entry<String, int[]> entry : this.board.getStringToCoordMap().entrySet()) {
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

		int[] banditCorners = gameLogic.getBoard().getStringToCoordMap().get(gameLogic.getBoard().getBandit());
		viewController.getGameViewController().setBandit(banditCorners[0], banditCorners[1]);

	}

	/**
	 * @param playerID
	 * @param resourcesGained
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
	 * @return
	 */
	public GameLogic getGameLogic() {
		return gameLogic;
	}
	
	public ClientOutputHandler getClientOuptputHandler() {
		return clientOutputHandler;
	}

	/**
	 * Returns own ID.
	 *
	 * @return
	 */
	public int getOwnPlayerID() {
		return ownPlayerID;
	}

	/**
	 * Sets own ID.
	 *
	 * @param ownPlayerID
	 */
	public void setOwnPlayerID(int ownPlayerID) {
		this.ownPlayerID = ownPlayerID;
	}

	/**
	 * Returns the amount of players that are playing the game.
	 *
	 * @return
	 */
	public int getAmountPlayers() {
		return amountPlayers;
	}

	/**
	 * Sets the amount of players that are playing the game.
	 *
	 * @param amountPlayers
	 */
	public void setAmountPlayers(int amountPlayers) {
		this.amountPlayers = amountPlayers;
	}

	/**
	 * @param playerID
	 * @param resources
	 */
	public void resourceObtain(int playerID, int[] resources) {
		int modelID = threadPlayerIdMap.get(playerID);
		// if self
		if (modelID == 0) {
			gameLogic.getBoard().getPlayer(modelID).incrementResources(resources);
			viewController.getGameViewController().setResourceCards(modelID, getPlayerResources(modelID));
			Platform.runLater(
					new ServerResponseRunnable(DefaultSettings.getCurrentTime() + " You have gained resources!",
							viewController.getGameViewController()));
		} // if someone else
		else {
			// increment their hiddenresources
			gameLogic.getBoard().getPlayer(modelID).incrementHiddenResources(resources[0]);
			//viewController.getGameViewController().setResourceCards(modelID, getPlayerHiddenResource(modelID));
		}

	}

	/**
	 *
	 */
	public void costs(int threadID, int[] resources) {
		int modelID = threadPlayerIdMap.get(threadID);
		// if self
		if (modelID == 0) {
			gameLogic.getBoard().getPlayer(modelID).decrementResources(resources);
			viewController.getGameViewController().setResourceCards(modelID, getPlayerResources(modelID));
			Platform.runLater(new ServerResponseRunnable(DefaultSettings.getCurrentTime() + " You have lost resources!",
					viewController.getGameViewController()));
		} // if someone else
		else {
			// decrement their hiddenresources
			gameLogic.getBoard().getPlayer(modelID).decrementHiddenResources(resources[0]);
			//viewController.getGameViewController().setResourceCards(modelID, getPlayerHiddenResource(modelID));
		}
	}

	// 8.6

	/**
	 * @param x
	 * @param y
	 * @param dir
	 * @param playerID
	 */
	public void buildStreet(int x, int y, int dir, int playerID) {
		int modelID = threadPlayerIdMap.get(playerID);
		Edge e = gameLogic.getBoard().getEdgeAt(x, y, dir);
		e.setHasStreet(true);
		e.setOwnedByPlayer(gameLogic.getBoard().getPlayer(modelID).getID());

		viewController.getGameViewController().setStreet(x, y, dir, modelID);
	}

	// 8.6

	/**
	 * @param x
	 * @param y
	 * @param dir
	 * @param playerID
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

		viewController.getGameViewController().setCorner(x, y, dir, enums.CornerStatus.VILLAGE, modelID);
	}

	// 8.6

	/**
	 * @param x
	 * @param y
	 * @param dir
	 * @param playerID
	 */
	public void buildCity(int x, int y, int dir, int playerID) {
		Corner c = gameLogic.getBoard().getCornerAt(x, y, dir);
		c.setStatus(enums.CornerStatus.CITY);
		c.setOwnerID(playerID);
		int modelID = threadPlayerIdMap.get(playerID);
		viewController.getGameViewController().setCorner(x, y, dir, enums.CornerStatus.CITY, modelID);
	}

	// 9.1

	/**
	 *
	 */
	public void diceRollRequest() {
		clientOutputHandler.sendDiceRollRequest();

	}

	// 9.3

	/**
	 * @param x
	 * @param y
	 * @param stealFromPlayerId
	 */
	public void requestSetBandit(int x, int y, int stealFromPlayerId) {
		clientOutputHandler.sendBanditRequest(x, y, modelPlayerIdMap.get(stealFromPlayerId));

	}

	// 9.4

	/**
	 * @param x
	 * @param y
	 * @param dir
	 */
	public void requestBuildVillage(int x, int y, int dir) {
		// view saves coordinates from 0 to 7 system
		int radius = DefaultSettings.BOARD_RADIUS;
		if (initialRoundCount < 2) {
			System.out.println("Building Initial Village");
			logger.debug("Building Initial Village");
			requestBuildInitialVillage(x - radius, y - radius, dir);

		}
		if (gameLogic.checkBuildVillage(x - radius, y - radius, dir, ownPlayerID)) {
			clientOutputHandler.requestBuildVillage(x - radius, y - radius, dir);
		}
	}

	/**
	 * @param x
	 * @param y
	 * @param dir
	 */
	public void requestBuildInitialVillage(int x, int y, int dir) {
		if (gameLogic.checkBuildInitialVillage(x, y, dir)) {
			clientOutputHandler.requestBuildVillage(x, y, dir);
		}
	}

	// 9.4

	/**
	 * @param x
	 * @param y
	 * @param dir
	 */
	public void requestBuildStreet(int x, int y, int dir) {
		int radius = DefaultSettings.BOARD_RADIUS;
		if (initialRoundCount < 2) {
			System.out.println("Building Initial Village");
			logger.debug("Building Initial Village");
			requestBuildInitialStreet(x - radius, y - radius, dir);
		}
		if (gameLogic.checkBuildStreet(x - radius, y - radius, dir, ownPlayerID)) {
			clientOutputHandler.requestBuildStreet(x - radius, y - radius, dir);
		}

	}

	/**
	 * @param x
	 * @param y
	 * @param dir
	 */
	public void requestBuildInitialStreet(int x, int y, int dir) {
		if (gameLogic.checkBuildInitialStreet(x, y, dir, ownPlayerID)) {
			clientOutputHandler.requestBuildStreet(x, y, dir);
			initialRoundCount++; // TODO: this should happen after server OK
		}
	}

	// 9.4

	/**
	 * @param x
	 * @param y
	 * @param dir
	 */
	public void requestBuildCity(int x, int y, int dir) {
		int radius = DefaultSettings.BOARD_RADIUS;
		// DEBUG
		// if (gameLogic.checkBuildCity(x - radius, y - radius, dir,
		// ownPlayerId)) {
		clientOutputHandler.requestBuildCity(x - radius, y - radius, dir);
		// }
	}

	// 9.7

	/**
	 *
	 */
	public void endTurn() {
		clientOutputHandler.sendEndTurn();
	}

	// Protocol 0.2

	/**
	 * @param result
	 */
	public void robberLoss(int[] result) {
		clientOutputHandler.sendRobberLoss(result);
	}
	// Protocol 0.3

	/**
	 * Basic methods for trading
	 */

	public void requestTrade(int[] offer, int[] demand) {
		clientOutputHandler.requestTrade(offer, demand);
	}

	public void showTrade(int threadID, int tradingID, int[] supply, int[] demand) {
		int modelID = threadPlayerIdMap.get(threadID);
		TradeOffer tOf = new TradeOffer(threadID, tradingID, supply, demand);
		tradeOffers.add(tOf);
		if (modelID == ownPlayerID) {
			setOwnTradingID(tradingID);
			viewController.getGameViewController().getTradeViewController().addOwnOffer(supply, demand, tradingID);
		} else {
			viewController.getGameViewController().getTradeViewController().addOffer(supply, demand, tradingID,
					modelID);

		}
	}

	/**
	 * @param tradingID
	 */
	public void acceptTrade(int tradingID) {
		clientOutputHandler.acceptTrade(tradingID);
	}

	/**
	 * A Player has accepted your offer. Show acceptance on list
	 * @param threadID
	 * @param tradingID
	 */
	public void tradeAccepted(int threadID, int tradingID) {
		// TradeOffer currTOf;
		// for (int i = 0; i < tradeOffers.size(); i++) {
		// currTOf = tradeOffers.get(i);
		// if (currTOf.getTradingID() == tradingID) {
		// currTOf.acceptingPlayers.add(modelID);
		// }
		// }
		if (getOwnTradingID() != null && getOwnTradingID() == tradingID) {
			viewController.getGameViewController().getTradeViewController()
					.acceptingOffer(threadPlayerIdMap.get(threadID), tradingID);
		}
	}

	/**
	 * You agree to the trading
	 * @param tradingID
	 * @param partnerThreadID
	 */
	public void fulfillTrade(int tradingID, int partnerModelID) {
		clientOutputHandler.tradeComplete(tradingID, modelPlayerIdMap.get(partnerModelID));
	}

	/**
	 * trade was fulfilled
	 * @param threadID
	 * @param partnerModelID
	 */
	public void tradeFulfilled(int threadID, int partnerModelID) {
		TradeOffer currTOf;
		int modelID = threadPlayerIdMap.get(threadID);
		int id = 0;
		for (int i = 0; i < tradeOffers.size(); i++) {
			currTOf = tradeOffers.get(i);
			if (currTOf.getOwnerID() == modelID) {
				id = tradeOffers.get(i).getTradingID();
				tradeOffers.remove(i);
			}
		}
		viewController.getGameViewController().getTradeViewController().offerFulfilled(id);
	}

	public void cancelTrade(int tradingID) {
		clientOutputHandler.cancelTrade(tradingID);
	}

	public void tradeCancelled(int threadID, int tradingID) {
		TradeOffer currTOf;
		int modelID = threadPlayerIdMap.get(threadID);
		for (int i = 0; i < tradeOffers.size(); i++) {
			currTOf = tradeOffers.get(i);
			if (currTOf.getTradingID() == tradingID) {
				if (currTOf.getOwnerID() == modelID) {
					tradeOffers.remove(i);
				} else {
					for (int j = 0; j < currTOf.acceptingPlayers.size(); j++) {
						if (currTOf.acceptingPlayers.get(j) == modelID) {
							currTOf.acceptingPlayers.remove(j);
						}
					}
				}
				break;
			}
		}
		if (modelID == 0) {
			viewController.getGameViewController().getTradeViewController().cancelOwnOffer();
		} else {
			viewController.getGameViewController().getTradeViewController().cancelOffer(modelID);
		}
	}

	public void requestSeaTrade(int[] offer, int[] demand) {
		clientOutputHandler.requestHarbourTrade(offer, demand);
	}

	/**
	 * @param message
	 */
	public void victory(String message, int threadID) {
		if(threadID != -1){
			viewController.getGameViewController().showVictory(threadPlayerIdMap.get(threadID));
		} else {
			viewController.getGameViewController().alert(message + " Spieler: " + threadID);
		}
	}

	/**
	 * // * @param playerID
	 */
	public void largestArmy(int threadID) {
		gameLogic.getBoard().getPlayer(threadPlayerIdMap.get(threadID)).setHasLargestArmy(true);
		viewController.getGameViewController().setGreatestKnightForce(threadPlayerIdMap.get(threadID));
		
	}

	/**
	 * //* @param playerID
	 */
	public void longestRoad(Integer threadID) {
		if (threadID != null) {
			if (threadID == ownPlayerID) {
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

	public void requestBuyDevelopmentCard() {
		// if (gameLogic.checkBuyDevCard(0)) {
		clientOutputHandler.buyDevelopmentCard();
		// }
	}

	/**
	 * //* @param resource
	 */
	public void playInventionCard(int[] resources) {

	}

	public void playMonopolyCard(ResourceType resType) {

	}

	public void playStreetBuildCard(int u1, int v1, int dir1, int u2, int v2, int dir2) {

	}

	public void playKnightCard(int x, int y, int modelID) {
			clientOutputHandler.playKnightCard(x, y, modelPlayerIdMap.get(modelID));

	}
	
	private int[] getPlayerHiddenResource(int playerID){
		int[] resource = new int[]{gameLogic.getBoard().getPlayer(playerID).getHiddenResources()};
		return resource;
	}

	/**
	 * @param playerID
	 * @return
	 */
	private int[] getPlayerResources(int playerID) {
		int[] resources = gameLogic.getBoard().getPlayer(playerID).getResources();
		return resources;
	}

	public void addToDeck(int playerID, DevelopmentCard devCard) {
		int modelID = threadPlayerIdMap.get(playerID);
		gameLogic.getBoard().getPlayer(modelID).incrementPlayerDevCard(devCard);
		// viewController.getGameViewController().card
	}

	/**
	 *
	 */
	public void robberMove(Index index) {
		String location = ProtocolToModel.getProtocolOneID(index);
		gameLogic.getBoard().setBandit(location);
		int[] coords = ProtocolToModel.getFieldCoordinates(location);
		// TODO display user and victim popup
		viewController.getGameViewController().setBandit(coords[0], coords[1]);
	}

	public void receiveRoadCard(int playerID, Index[] locationID1, Index[] locationID2) {
		// TODO Auto-generated method stub

	}

	public void receiveMonopolyCard(int playerID, ResourceType rt) {
		// TODO Auto-generated method stub

	}

	public void receiveInventionCard(int playerID, int[] resource) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the ownTradingID
	 */
	public Integer getOwnTradingID() {
		return ownTradingID;
	}

	/**
	 * @param ownTradingID
	 *            the ownTradingID to set
	 */
	public void setOwnTradingID(Integer ownTradingID) {
		this.ownTradingID = ownTradingID;
	}
	
	public int getPlayerID(int modelID){
		return modelPlayerIdMap.get(modelID);
	}

}
