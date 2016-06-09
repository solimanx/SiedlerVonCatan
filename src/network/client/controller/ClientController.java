package network.client.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import network.client.client.Client;
import network.client.client.ClientInputHandler;
import network.client.client.ClientOutputHandler;
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

	private Map<Integer, Integer> modelPlayerIdMap;
	private Map<Integer, Integer> threadPlayerIdMap;

	protected Client client;

	public ClientController(Stage primaryStage) {
		// ModelPlayerID => threadID
		modelPlayerIdMap = new HashMap<Integer, Integer>();

		// threadID => ModelPlayerID
		threadPlayerIdMap = new HashMap<Integer, Integer>();

		this.clientInputHandler = new ClientInputHandler(this);
		this.viewController = new ViewController(primaryStage, this);

	}

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
	public void serverHello(String serverVersion, String protocolVersion) {
		if (!protocolVersion.equals(settings.DefaultSettings.PROTOCOL_VERSION)) {
			client.stopClient();
			System.out.println("Invalid Protocol Version; Disconnected");
		} else {
			clientOutputHandler.clientHello(DefaultSettings.CLIENT_VERSION);
		}

	}

	// 4.2
	public void welcome(int playerID) {
		setOwnPlayerID(playerID);
		System.out.println("Handshake complete!");
		viewController.getLobbyController().enableChat();
	}

	// 6.1
	public void receiveServerConfirmation(String server_response) {
		// TODO client confirm in later protocols
	}

	// 6.2
	public void chatSendMessage(String s) {
		clientOutputHandler.chatSendMessage(s);
	}

	// 6.3
	public void chatReceiveMessage(int playerId, String s) {
		// viewController.mainViewController.receiveChatMessage("Spieler
		// "+playerId+": "+s);
		viewController.getLobbyController().receiveChatMessage("Spieler " + playerId + ": " + s);
	}

	// 7.1
	public void sendPlayerProfile(String name, Color color) {
		clientOutputHandler.sendPlayerProfile(name, color);
		setPlayerName(ownPlayerId, name);
		setPlayerColor(ownPlayerId, color);
	}

	// 7.2
	public void sendReady() {
		clientOutputHandler.clientReady();
	}

	// 7.3
	public void error(String notice) {
		System.out.println(notice);
	}

	// 8.1
	public void statusUpdate(int threadID, enums.Color color, String name, enums.PlayerState status, int victoryPoints,
			int[] resources) {
		Integer modelID = threadPlayerIdMap.get(threadID);
		if (modelID == null) {
			threadPlayerIdMap.put(threadID, amountPlayers);
			modelPlayerIdMap.put(amountPlayers, threadID);
			amountPlayers++;
		}
		PlayerModel pM = gameLogic.getBoard().getPlayer(modelID);
		if (pM.getColor() == null) {
			pM.setColor(color);
			pM.setName(name);
			viewController.setPlayerColor(modelID, color);
			viewController.setPlayerName(modelID, name);
		}
		pM.setPlayerState(status);
		pM.setVictoryPoints(victoryPoints);
		addToPlayersResource(modelID, resources);

	}

	// 7.4
	public void initBoard(Field[] serverFields, Corner[] corners,ArrayList<Edge> streets, Corner[] harbourCorners, String banditLocation) {
		for(Field f : serverFields){
			String location = f.getFieldID();
			int[] coords = Board.getStringToCoordMap().get(location);
			Field bField = board.getFieldAt(coords[0], coords[1]);
			bField.setFieldID(location);
			bField.setDiceIndex(f.getDiceIndex());;
			bField.setResourceType(f.getResourceType());
		}
		for (Corner c : corners){
			String location = c.getCornerID();
			int coords[] = Board.getStringToCoordMap().get(location);
			Corner bCorner = board.getCornerAt(coords[0], coords[1], coords[2]);
			bCorner.setCornerID(location);
			bCorner.setOwnerID(c.getOwnerID());
			bCorner.setStatus(c.getStatus());
		}
		for (Edge s : streets){
			String location = s.getEdgeID();
			int coords[] = Board.getStringToCoordMap().get(location);
			Edge bEdge = board.getEdgeAt(coords[0], coords[1], coords[2]);
			bEdge.setEdgeID(location);
			bEdge.setHasStreet(s.isHasStreet());
			bEdge.setOwnedByPlayer(s.getOwnerID());
		}
		for (Corner c : harbourCorners){
			String location = c.getCornerID();
			int coords[] = Board.getStringToCoordMap().get(location);
			Corner bCorner = board.getCornerAt(coords[0], coords[1], coords[2]);
			bCorner.setCornerID(location);
			bCorner.setHarbourStatus(c.getHarbourStatus());
		}
		
		board.setBandit(banditLocation);
		
		this.gameLogic = new GameLogic(board);

		viewController.startGameView();

		

	}

	public void addToPlayersResource(int playerID, int[] resources) {
		ArrayList<ResourceType> resourceCards = board.getPlayer(playerID).getResourceCards();
		for (int i = 0; i < resources.length; i++) {
			for (int j = 0; j < resources[i]; j++) {
				resourceCards.add(settings.DefaultSettings.RESOURCE_ORDER[i]);
			}
		}
		board.getPlayer(playerID).setResourceCards(resourceCards);

	}

	public void setPlayerResources(int playerId, int[] resources) {
		ArrayList<ResourceType> resourceCards = new ArrayList<ResourceType>();
		for (int i = 0; i < resources.length; i++) {
			for (int j = 0; j < resources[i]; j++) {
				resourceCards.add(settings.DefaultSettings.RESOURCE_ORDER[i]);
			}
		}
		board.getPlayer(playerId).setResourceCards(resourceCards);

	}

	public void setPlayerColor(int playerId, Color color) {
		viewController.setPlayerColor(playerId, color);

	}

	public void setPlayerName(int playerId, String name) {
		viewController.setPlayerName(playerId, name);

	}

	public void setPlayerVictoryPoints(int playerId, int victoryPoints) {
		board.getPlayer(playerId).setVictoryPoints(victoryPoints);
	}

	public GameLogic getGameLogic() {
		return gameLogic;
	}

	public int getOwnPlayerId() {
		return ownPlayerId;
	}

	public void setOwnPlayerID(int ownPlayerId) {
		this.ownPlayerId = ownPlayerId;
	}

	public int getAmountPlayers() {
		return amountPlayers;
	}

	public void setAmountPlayers(int amountPlayers) {
		this.amountPlayers = amountPlayers;
	}

	// 8.2
	public void diceRollResult(int playerId, int[] result) {
		int res = result[0] + result[1];
		viewController.setDiceRollResult(playerId, res);
		// output
	}

	// 8.3
	public void resourceObtain(int playerId, int[] resources) {
		// flowController.addToPlayersResource(getPlayerModelId(playerId),
		// resources);

	}

	// 8.5
	public void setBandit(int stealingPlayerId, int x, int y, int stealFromPlayerId) {

	}

	// 8.6
	public void buildStreet(int x, int y, int dir, int playerID) {
		Edge e = board.getEdgeAt(x, y, dir);
		e.setHasStreet(true);
		e.setOwnedByPlayer(board.getPlayer(playerID).getID());

		viewController.getGameViewController().setStreet(x, y, dir, playerID);
	}

	// 8.6
	public void buildVillage(int x, int y, int dir, int playerID) {
		Corner c = board.getCornerAt(x, y, dir);
		c.setStatus(enums.CornerStatus.VILLAGE);
		c.setOwnerID(playerID);
		Corner[] neighbors = board.getAdjacentCorners(x, y, dir);
		for (int i = 0; i < neighbors.length; i++) {
			if (neighbors[i] != null) {
				neighbors[i].setStatus(enums.CornerStatus.BLOCKED);
			}
		}

		viewController.getGameViewController().setCorner(x, y, dir, enums.CornerStatus.VILLAGE, playerID);
	}

	// 8.6
	public void buildCity(int x, int y, int dir, int playerID) {
		Corner c = board.getCornerAt(x, y, dir);
		c.setStatus(enums.CornerStatus.CITY);
		c.setOwnerID(playerID);

		viewController.getGameViewController().setCorner(x, y, dir, enums.CornerStatus.CITY, playerID);
	}

	// 9.1
	public void diceRollRequest() {
		clientOutputHandler.diceRollRequest();

	}

	// 9.3
	public void requestSetBandit(int x, int y, int stealFromPlayerId) {
		clientOutputHandler.requestSetBandit(x, y, stealFromPlayerId);

	}

	// 9.4
	public void requestBuildVillage(int x, int y, int dir) {
		if (gameLogic.checkBuildVillage(x, y, dir, ownPlayerId)) {
			clientOutputHandler.requestBuildVillage(x, y, dir);
		}

	}

	// 9.4
	public void requestBuildStreet(int x, int y, int dir) {
		if (gameLogic.checkBuildStreet(x, y, dir, ownPlayerId)) {
			clientOutputHandler.requestBuildStreet(x, y, dir);
		}

	}

	// 9.4
	public void requestBuildCity(int x, int y, int dir) {
		if (gameLogic.checkBuildCity(x, y, dir, ownPlayerId)) {
			clientOutputHandler.requestBuildCity(x, y, dir);
		}
	}

	// 9.7
	public void endTurn() {
		clientOutputHandler.endTurn();
	}

	// Protocol 0.2
	public void robberMovement() {
		// TODO
	}

	public void robberLoss() {
		// TODO
	}
	// Protocol 0.3

	public void tradeIsRequested(int player_id, int trade_id, ProtocolResource offer, ProtocolResource withdrawal) {
		// TODO
	}

	public void tradeConfirmation(int player_id, int trade_id) {
		// TODO
	}

	public void tradeIsCanceled(int player_id, int trade_id) {
		// TODO
	}

	public void tradeIsCompleted(int player_id, int tradePartner_id) {
		// TODO
	}

	public void victory() {
		// TODO
	}

	public void costs() {
		// TODO
	}

	public void biggestKnightProwess(int player_id) {
		// TODO
	}

	public void inventionCardInfo(ProtocolResource resource, int player_id) {
		// TODO
	}

	public void longestRoad(int player_id) {
		// TODO
	}

	public void monopolyCardInfo() {
		// TODO
	}

	public void playKnightCard(String road1_id, String road2_id, int player_id) {
		// TODO
	}

	public void boughtPlayerCard() {
		// TODO
	}

	/**
	 * sets player state in own client model; is called by network controller
	 * after server has changed a player state
	 *
	 * @param state
	 */
	public void setPlayerState(int playerId, PlayerState state) {
		board.getPlayer(playerId).setPlayerState(state);
		if (playerId == ownPlayerId) {
			// update GUI
			// viewController.setPlayerState(state);
		}
	}

}
