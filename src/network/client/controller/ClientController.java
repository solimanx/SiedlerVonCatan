package network.client.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sun.media.jfxmedia.logging.Logger;

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
import network.client.view.PlayerStatusGUIUpdate;
import protocol.object.ProtocolResource;
import settings.DefaultSettings;

/**
 * @author NiedlichePixel Controls the game flow.
 */
public class ClientController {
	private Board board;
	private GameLogic gameLogic;

	private int ownPlayerId;
	private int amountPlayers;

	protected ViewController viewController;

	private ClientOutputHandler clientOutputHandler;
	private ClientInputHandler clientInputHandler;

	// e.g. 0 -> 45
	private Map<Integer, Integer> modelPlayerIdMap;

	// e.g. 45 -> 0
	private Map<Integer, Integer> threadPlayerIdMap;

	protected Client client;
	private int initialRoundCount = 0;

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
		} else {
			clientOutputHandler.clientHello(DefaultSettings.CLIENT_VERSION);
		}

	}

	// 4.2
	/**
	 * @param playerID
	 */
	public void welcome(int playerID) {
		setOwnPlayerID(playerID);
		System.out.println("Handshake complete!");
		viewController.getLobbyController().enableChat();
	}

	// 6.1
	/**
	 * @param server_response
	 */
	public void receiveServerConfirmation(String server_response) {
		// TODO client confirm in later protocols
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
	public void chatReceiveMessage(int playerId, String s) {
		viewController.messageReceive(
				"Spieler " + gameLogic.getBoard().getPlayer(threadPlayerIdMap.get(playerId)).getName() + ": " + s);
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

		switch (status) {
		case CONNECTION_LOST:
			if (modelID != null) {
				threadPlayerIdMap.remove(modelID);
				modelPlayerIdMap.remove(threadID);
			}
			// if player exists, delete from array
			break;
		case GAME_STARTING:
			break;
		case WAITING_FOR_GAMESTART:
			if (modelID == null) {
				threadPlayerIdMap.put(threadID, amountPlayers);
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
			addToPlayersResource(modelID, resources);
			
			//hier bekommt dann der Player doch gar nicht die ganzen Resourcen???
			if (viewController.getGameViewController() != null) {
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
			// TODO change
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
		}
		int[] banditCorners = gameLogic.getBoard().getStringToCoordMap().get(gameLogic.getBoard().getBandit());
		viewController.getGameViewController().setBandit(banditCorners[0], banditCorners[1]);

	}

	/**
	 * @param playerID
	 * @param resources
	 */
	public void addToPlayersResource(int playerID, int[] resources) {
		ArrayList<ResourceType> resourceCards = gameLogic.getBoard().getPlayer(playerID).getResourceCards();
		for (int i = 0; i < resources.length; i++) {
			for (int j = 0; j < resources[i]; j++) {
				resourceCards.add(settings.DefaultSettings.RESOURCE_ORDER[i]);
			}
		}
		gameLogic.getBoard().getPlayer(playerID).setResourceCards(resourceCards);

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
		//WARNING, PLAYERID NOTHING GETS DONE WITH IT
		if(viewController.getGameViewController() != null)
		{
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
		
		//TODO Do not call setResourceCards! (this would _set_ the resource cards of player, not add!)
		//viewController.getGameViewController().setResourceCards(modelID, resources);

	}

	// 8.5
	/**
	 * @param stealingPlayerId
	 * @param x
	 * @param y
	 * @param stealFromPlayerId
	 */
	public void setBandit(int stealingPlayerId, int x, int y, int stealFromPlayerId) {

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
		clientOutputHandler.requestSetBandit(x, y, stealFromPlayerId);

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
		if (gameLogic.checkBuildCity(x, y, dir, ownPlayerId)) {
			clientOutputHandler.requestBuildCity(x, y, dir);
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
	public void robberMovement() {
		// TODO
	}

	/**
	 * @param result
	 */
	public void robberLoss(int[] result) {
		// TODO
	}
	// Protocol 0.3

	/**
	 * @param player_id
	 * @param trade_id
	 * @param offer
	 * @param withdrawal
	 */
	public void tradeIsRequested(int player_id, int trade_id, ProtocolResource offer, ProtocolResource withdrawal) {
		// TODO
	}

	/**
	 * @param player_id
	 * @param trade_id
	 */
	public void tradeConfirmation(int player_id, int trade_id) {
		// TODO
	}

	/**
	 * @param player_id
	 * @param trade_id
	 */
	public void tradeIsCanceled(int player_id, int trade_id) {
		// TODO
	}

	/**
	 * @param player_id
	 * @param tradePartner_id
	 */
	public void tradeIsCompleted(int player_id, int tradePartner_id) {
		// TODO
	}

	/**
	 * 
	 */
	public void victory() {
		// TODO
	}

	/**
	 * 
	 */
	public void costs() {
		// TODO
	}

	/**
	 * @param player_id
	 */
	public void biggestKnightProwess(int player_id) {
		// TODO
	}

	/**
	 * @param resource
	 * @param player_id
	 */
	public void inventionCardInfo(ProtocolResource resource, int player_id) {
		// TODO
	}

	/**
	 * @param player_id
	 */
	public void longestRoad(int player_id) {
		// TODO
	}

	/**
	 * 
	 */
	public void monopolyCardInfo() {
		// TODO
	}

	/**
	 * @param road1_id
	 * @param road2_id
	 * @param player_id
	 */
	public void playKnightCard(String road1_id, String road2_id, int player_id) {
		// TODO
	}

	/**
	 * 
	 */
	public void boughtDevelopmentCard() {
		// TODO
	}

	/**
	 * sets player state in own client model; is called by network controller
	 * after server has changed a player state
	 *
	 * @param state
	 */
	/**
	 * @param playerId
	 * @param state
	 */
	public void setPlayerState(int playerId, PlayerState state) {
		gameLogic.getBoard().getPlayer(playerId).setPlayerState(state);
		if (playerId == ownPlayerId) {
			// update GUI
			// viewController.setPlayerState(state);
		}
	}

	/**
	 * @param playerID
	 * @return
	 */
	private int[] getPlayerResources(int playerID) {
		ArrayList<ResourceType> resources = gameLogic.getBoard().getPlayer(playerID).getResourceCards();
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

}
