package network.client.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElementDecl.GLOBAL;

import com.sun.media.jfxmedia.logging.Logger;

import application.lobby.PlayerProfileController;
import enums.Color;
import enums.PlayerState;
import enums.ResourceType;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.Board;
import model.GameLogic;
import model.objects.Corner;
import model.objects.Edge;
import model.objects.Field;
import model.objects.PlayerModel;
import network.ProtocolToModel;
import network.client.client.Client;
import network.client.client.ClientInputHandler;
import network.client.client.ClientOutputHandler;
import network.client.view.GameViewController;
import network.client.view.PlayerStatusGUIUpdate;
import org.apache.logging.log4j.LogManager;
import protocol.object.ProtocolResource;
import settings.DefaultSettings;

/**
 * @author NiedlichePixel Controls the game flow.
 */
public class ClientController {
	private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(ClientController.class.getName());
	private Board board;
	private GameLogic gameLogic;

	private int ownPlayerId;
	private int amountPlayers = 1;

	protected ViewController viewController;

	private ClientOutputHandler clientOutputHandler;
	private ClientInputHandler clientInputHandler;

	/**
	 * e.g. (0 -> 45), (1, -> 32) etc. HashMap mapping modelIDs to playerIDs
	 */
	private Map<Integer, Integer> modelPlayerIdMap;

	/**
	 * e.g. (45-> 0), (32, -> 1) etc. HashMap mapping playerIDs to modelIDs
	 */
	private Map<Integer, Integer> threadPlayerIdMap;

	protected Client client;
	private int initialRoundCount = 0;
	private String currentServerResponse;
	private PlayerState currentState;
	
	private ArrayList<TradeOffer> tradeOffers = new ArrayList<TradeOffer>();

	/**
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
	 * @param serverHost
	 * @param port
	 */
	public void connectToServer(String serverHost, int port) {
		this.client = new Client(clientInputHandler, serverHost, port);
		this.clientOutputHandler = new ClientOutputHandler(client);
		client.start();
	}

	/**
	 * Check if versions match and act accordingly; if they match begin sending
	 * confirmation from client otherwise disconnect
	 *
	 * @param serverVersion
	 * @param protocolVersion
	 */
	/**
	 * @param serverVersion
	 * @param protocolVersion
	 */
	public void serverHello(String serverVersion, String protocolVersion) {
		if (!protocolVersion.equals(settings.DefaultSettings.PROTOCOL_VERSION)) {
			client.stopClient();
			System.out.println("Invalid Protocol Version; Disconnected");
			logger.error("Invalid Protocol Version; Disconnected");
		} else {
			clientOutputHandler.clientHello(DefaultSettings.CLIENT_VERSION);
		}

	}

	// 4.2

	/**
	 * @param playerID
	 */
	public void welcome(int playerID) {
		// playerID is 42,35, etc.
		setOwnPlayerID(playerID);
		// add myself to hashmap
		threadPlayerIdMap.put(playerID, 0);
		modelPlayerIdMap.put(0, playerID);
		System.out.println("Handshake complete!");
		logger.debug("Handshake complete!");
		viewController.getLobbyController().enableChat();
	}

	// 6.1

	/**
	 * @param server_response
	 */
	public void receiveServerConfirmation(String server_response) {
		// TODO client confirm in later protocols
		currentServerResponse = server_response;
		switch (currentState) {
		case GAME_STARTING:
		case WAITING_FOR_GAMESTART:
			viewController.setServerResponse(server_response);
		default:
			viewController.setServerResponse(server_response);
		}
	}

	// 6.2

	/**
	 * @param s
	 */
	public void chatSendMessage(String s) {
		clientOutputHandler.chatSendMessage(s);
	}

	// 6.3

	/**
	 * @param playerId
	 * @param s
	 */
	public void chatReceiveMessage(Integer playerId, String s) {
		if (playerId != null) {
			viewController.messageReceive(
					"Spieler " + gameLogic.getBoard().getPlayer(threadPlayerIdMap.get(playerId)).getName() + ": " + s);

		} else {
			viewController.messageReceive("Server: " + s);
		}
	}

	// 7.1

	/**
	 * @param name
	 * @param color
	 */
	public void sendPlayerProfile(String name, Color color) {
		clientOutputHandler.sendPlayerProfile(name, color);
		// TODO later
		// viewController.getGameViewController().initSelf(getOwnPlayerId(),
		// name, color);
	}

	// 7.2

	/**
	 *
	 */
	public void sendReady() {
		clientOutputHandler.clientReady();
	}

	// 7.3

	/**
	 * @param notice
	 */
	public void error(String notice) {
		System.out.println(notice);
		logger.debug(notice);
	}

	// 8.1

	/**
	 * @param threadID
	 * @param color
	 * @param name
	 * @param status
	 * @param victoryPoints
	 * @param resources
	 */
	public void statusUpdate(int threadID, enums.Color color, String name, enums.PlayerState status, int victoryPoints,
			int[] resources) {
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
			addToPlayersResource(modelID, resources);

			if (viewController.getGameViewController() != null) {
				// and here..
				// modelID = 0,1,2,3
				Platform.runLater(new PlayerStatusGUIUpdate(modelID, viewController.getGameViewController(),
						victoryPoints, status, resources));
			}
		}

		// Hier get bei Break weiter
	}

	// 7.4

	/**
	 * @param serverFields
	 * @param corners
	 * @param streets
	 * @param harbourCorners
	 * @param banditLocation
	 */
	public void initBoard(Field[] serverFields, Corner[] corners, ArrayList<Edge> streets, Corner[] harbourCorners,
			String banditLocation) {

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
		}

		gameLogic.getBoard().setBandit(banditLocation);
		setOwnPlayerID(threadPlayerIdMap.get(getOwnPlayerId())); // change
		// ownPlayerId
		// to
		// modelID
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				System.out.println("STARTING GAME VIEW IN Platform.runLater");
				logger.debug("STARTING GAME VIEW IN Platform.runLater");
				// TODO vielleicht ein fix?
				viewController.startGameView();

			}
		});
	}

	/**
	 *
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
			//TODO set Harbour Fields in GameView. angleFactor * 60° edge,field -> int
			//viewController.getGameViewController().setHarbour(int angleFactor, int u, int v, enums.HarbourStatus);
		}
		int[] banditCorners = gameLogic.getBoard().getStringToCoordMap().get(gameLogic.getBoard().getBandit());
		viewController.getGameViewController().setBandit(banditCorners[0], banditCorners[1]);

	}

	/**
	 * @param playerID
	 * @param resourcesGained
	 */
	public void addToPlayersResource(int playerID, int[] resourcesGained) {
		// playerID = 0,1,2,3
		// if it's self
		if (playerID == 0) {
			// safety check
			if (resourcesGained.length == 5) {
				// playerID := 0,1,2,3
				gameLogic.getBoard().getPlayer(playerID).incrementResources(resourcesGained);

			} else {
				throw new IllegalArgumentException("Error at adding player resources to player");
			}

		}
		// other players
		else {

			// safety check
			if (resourcesGained.length == 1) {
				gameLogic.getBoard().getPlayer(playerID).setHiddenResources(resourcesGained[0]);
			} else {
				throw new IllegalArgumentException("Error at adding player resources to player");
			}
		}

	}

	/**
	 * @param playerId
	 * @param resources
	 */
	public void setPlayerResources(int playerId, int[] resources) {
		ArrayList<ResourceType> resourceCards = new ArrayList<ResourceType>();
		for (int i = 0; i < resources.length; i++) {
			for (int j = 0; j < resources[i]; j++) {
				resourceCards.add(settings.DefaultSettings.RESOURCE_ORDER[i]);
			}
		}
		gameLogic.getBoard().getPlayer(playerId).setResourceCards(resourceCards);

	}

	/**
	 * @return
	 */
	public GameLogic getGameLogic() {
		return gameLogic;
	}

	/**
	 * @return
	 */
	public int getOwnPlayerId() {
		return ownPlayerId;
	}

	/**
	 * @param ownPlayerId
	 */
	public void setOwnPlayerID(int ownPlayerId) {
		this.ownPlayerId = ownPlayerId;
	}

	/**
	 * @return
	 */
	public int getAmountPlayers() {
		return amountPlayers;
	}

	/**
	 * @param amountPlayers
	 */
	public void setAmountPlayers(int amountPlayers) {
		this.amountPlayers = amountPlayers;
	}

	// 8.2

	/**
	 * @param playerId
	 * @param result
	 */
	public void diceRollResult(int playerId, int[] result) {
		// WARNING, PLAYERID NOTHING GETS DONE WITH IT
		if (viewController.getGameViewController() != null) {
			int res = result[0] + result[1];
			viewController.getGameViewController().setDiceRollResult(res);
		}

	}

	// 8.3

	/**
	 * @param playerID
	 * @param resources
	 */
	public void resourceObtain(int playerID, int[] resources) {
		int modelID = threadPlayerIdMap.get(playerID);
		// if self
		if (modelID == 0) {
			gameLogic.getBoard().getPlayer(modelID).incrementResources(resources);
		} else {
			// decrement the hiddenresource not the resource
			gameLogic.getBoard().getPlayer(modelID).incrementHiddenResources(resources[0]);
		}
		// Happens in status update
		// viewController.getGameViewController().setResourceCards(modelID,
		// resources);

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
		clientOutputHandler.diceRollRequest();

	}

	// 9.3

	/**
	 * @param x
	 * @param y
	 * @param stealFromPlayerId
	 */
	public void requestSetBandit(int x, int y, int stealFromPlayerId) {
		clientOutputHandler.requestSetBandit(x, y, modelPlayerIdMap.get(stealFromPlayerId));

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
			requestBuildInitialVillage(x - radius, y - radius, dir);

		}
		if (gameLogic.checkBuildVillage(x - radius, y - radius, dir, ownPlayerId)) {
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
			logger.info("Building Initial Village");
			requestBuildInitialStreet(x - radius, y - radius, dir);
		}
		if (gameLogic.checkBuildStreet(x - radius, y - radius, dir, ownPlayerId)) {
			clientOutputHandler.requestBuildStreet(x - radius, y - radius, dir);
		}

	}

	/**
	 * @param x
	 * @param y
	 * @param dir
	 */
	public void requestBuildInitialStreet(int x, int y, int dir) {
		if (gameLogic.checkBuildInitialStreet(x, y, dir, ownPlayerId)) {
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
		if (gameLogic.checkBuildCity(x - radius, y - radius, dir, ownPlayerId)) {
			clientOutputHandler.requestBuildCity(x - radius, y - radius, dir);
		}
	}

	// 9.7

	/**
	 *
	 */
	public void endTurn() {
		clientOutputHandler.endTurn();
	}

	// Protocol 0.2

	/**
	 *
	 */
	public void robberMovement(String location) {
		gameLogic.getBoard().setBandit(location);
		int[] coords = ProtocolToModel.getFieldCoordinates(location);
		viewController.getGameViewController().setBandit(coords[0], coords[1]);
	}

	/**
	 * @param result
	 */
	public void robberLoss(int[] result) {
		clientOutputHandler.robberLoss(result);
	}
	// Protocol 0.3
	
	/**
	 * 
	 * Basic methods for trading
	 */

	public void requestTrade(int[] offer, int[] demand) {
		clientOutputHandler.requestTrade(offer, demand);
	}

	public void receiveTrade(int threadID, int tradingID, int[] supply, int[] demand) {
		int modelID = threadPlayerIdMap.get(threadID);
	    TradeOffer tOf = new TradeOffer(modelID,tradingID,supply,demand);
		tradeOffers.add(tOf);
		if (modelID == 0){
			viewController.getGameViewController().getTradeViewController().addOwnOffer(supply, demand, tradingID);
		} else {
		viewController.getGameViewController().getTradeViewController().addOffer(supply, demand, tradingID, modelID);
	
		}
	}
	public void acceptTrade(int tradingID) {
		clientOutputHandler.acceptTrade(tradingID);
	}

	public void tradeAccepted(int threadID, int tradingID) {
		TradeOffer currTOf;
		int modelID = threadPlayerIdMap.get(threadID);
		for (int i = 0;i <tradeOffers.size();i++){
			currTOf = tradeOffers.get(i);
			if (currTOf.getTradingID() == tradingID){
				currTOf.acceptingPlayers.add(modelID);
			}
		}
		viewController.getGameViewController().getTradeViewController().acceptingOffer(modelID,tradingID);
	}

	public void fulfillTrade(int tradingID, int partnerThreadID) {
		clientOutputHandler.tradeComplete(tradingID, partnerThreadID);
	}

	public void tradeFulfilled(int threadID, int partnerModelID) {
        TradeOffer currTOf;
        int modelID = threadPlayerIdMap.get(threadID);
        int id = 0;
		for (int i = 0; i < tradeOffers.size();i++){
			currTOf = tradeOffers.get(i);
			if (currTOf.getOwnerID() == modelID){
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
		for (int i = 0;i < tradeOffers.size();i++){
			currTOf = tradeOffers.get(i);
			if (currTOf.getTradingID() == tradingID){
				if (currTOf.getOwnerID() == modelID){
					tradeOffers.remove(i);
				} else {
					for (int j = 0; j <currTOf.acceptingPlayers.size();j++){
						if (currTOf.acceptingPlayers.get(j) == modelID){
							currTOf.acceptingPlayers.remove(j);
						}
					}
				}
				break;
			}
		}
		if (modelID == 0){
			viewController.getGameViewController().getTradeViewController().cancelOwnOffer();			
		} else {
			viewController.getGameViewController().getTradeViewController().cancelOffer(modelID);			
		}
	}
	
	
	public void requestSeaTrade(int[] offer,int[] demand){
		clientOutputHandler.requestHarbourTrade(offer, demand);
	}

	/**
	 * @param message 
	 *
	 */
	public void victory(String message, int threadID) {
		//viewController.getGameViewController().victory(message, threadPlayerIdMap.get(threadID));
	}

	/**
	 *
	 */
	public void costs(int threadID,int[] resources) {
		int modelID = threadPlayerIdMap.get(threadID);
		if (resources.length == 1){
			gameLogic.getBoard().getPlayer(modelID).incrementHiddenResources(resources[0]);
		} else {
			if ((modelID == 0)) {
			gameLogic.getBoard().getPlayer(0).decrementResources(resources);
		   } else {
			   int unknownSize = 0;
			   for (int i = 0;i <resources.length;i++){
				   unknownSize = unknownSize + resources[i];
			   }
			   gameLogic.getBoard().getPlayer(modelID).incrementHiddenResources(unknownSize);

		   }
		}

	}


	/**
	 * @param playerID
	 */
	public void biggestKnightProwess(int threadID) {
		gameLogic.getBoard().getPlayer(threadPlayerIdMap.get(threadID)).setHasLargestArmy(true);
		//viewController.getGameViewController().set...
	}
	
	/**
	 * @param playerID
	 */
	public void longestRoad(int threadID) {
		gameLogic.getBoard().getPlayer(threadPlayerIdMap.get(threadID)).setHasLongestRoad(true);
		//viewController.getGameViewController().set...
	}	
	
	
	public void requestBuyDevelopmentCard(){
		if (gameLogic.checkBuyDevCard(0)){
			clientOutputHandler.buyDevelopmentCard();
		}
	}

	/**
	 * @param resource
	 */
	public void playInventionCard(int[] resources) {
		
	}
	
	public void playMonopolyCard(ResourceType resType){
		
	}
	
	public void playStreetBuildCard(int u1,int v1, int dir1, int u2, int v2, int dir2){
		
	}
	
	public void playKnightCard(int x, int y, int modelID){
		
	}

	/**
	 * @param playerID
	 * @return
	 */
	private int[] getPlayerResources(int playerID) {
		int[] resources = gameLogic.getBoard().getPlayer(playerID).getResources();
		return resources;
	}

}
