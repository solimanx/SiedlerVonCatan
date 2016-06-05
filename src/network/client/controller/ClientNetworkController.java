package network.client.controller;

import model.Corner;
import model.Edge;
import model.Field;
import network.client.client.Client;
import network.client.client.ClientInputHandler;
import network.client.client.ClientOutputHandler;

public class ClientNetworkController {

	private FlowController flowController;
	private ClientOutputHandler outputHandler;
	private ClientInputHandler inputHandler;
	protected Client client;
	private int amountPlayers;

	public ClientNetworkController(FlowController fc) {
		this.flowController = fc;
		this.inputHandler = new ClientInputHandler(this);
		// this.amountPlayers = 1;
	}

	public void connectToServer(String serverHost, int port) {
		this.client = new Client(inputHandler, serverHost, port);
		this.outputHandler = new ClientOutputHandler(client);
		client.start();
	}

	// Bauen
	// 9.4
	public void requestBuildStreet(int x, int y, int dir) {
		// outputHandler.handleBuildRequest(x, y, dir, playerIDs = new
		// int[4][this.ownPlayerId], "Street");

	}

	// 9.4
	public void requestBuildVillage(int x, int y, int dir) {
		// outputHandler.handleBuildRequest(x, y, dir, playerIDs = new
		// int[4][this.ownPlayerId], "Village");
	}

	// 9.4
	public void requestBuildCity(int x, int y, int dir) {
		// outputHandler.handleBuildRequest(x, y, dir, playerIDs = new
		// int[4][this.ownPlayerId], "City");
	}

	// Bauvorgang

	// 8.6
	public void buildStreet(int x, int y, int dir, int playerId) {
		// flowController.buildStreet(x, y, dir, getPlayerModelId(playerId));

	}

	// 8.6
	public void buildVillage(int x, int y, int dir, int playerId) {
		// flowController.buildVillage(x, y, dir, getPlayerModelId(playerId));
	}

	// 8.6
	public void buildCity(int x, int y, int dir, int playerId) {
		// flowController.buildCity(x, y, dir, getPlayerModelId(playerId));

	}

	// 4.1
	public void clientHello() {
		outputHandler.clientHello(settings.DefaultSettings.CLIENT_VERSION);

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
			clientHello();
		}

	}

	// 4.2
	public void welcome(int playerID) {
		flowController.setOwnPlayerId(playerID);
		// TODO flowController.setPlayerState(playerID,
		// enums.PlayerState.WAITING_FOR_GAMESTART);
		System.out.println("Handshake finished!");
		flowController.viewController.getLobbyController().enableChat();
	}

	private void addToPlayerIDs(int playerID) {

	}

	// 7.2
	public void clientReady() {
		outputHandler.clientReady();

	}

	// 7.4
	public void gameStarted(Field[][] fields, Edge[][][] edges, Corner[][][] corners, Field bandit) {
		flowController.initBoard(amountPlayers, fields, edges, corners, bandit);

	}

	// 7.3
	public void error(String s) {
		System.out.println(s);

	}

	// 7.1
//
	public void sendPlayerProfile(enums.Color color, String name) {
		outputHandler.sendPlayerProfile(name, color);

	}


	// 6.2
	public void chatSendMessage(String s) {
		outputHandler.chatSendMessage(s);

	}

	// 6.3
	public void chatReceiveMessage(int playerId, String s) {
		flowController.chatReceiveMessage(playerId, s);
	}

	// 6.1
	public void serverConfirmation(String s) {
		System.out.println("Sever said OK");
	}

	// 9.1
	public void diceRollRequest() {
		outputHandler.diceRollRequest();

	}

	// 8.2
	public void diceRollResult(int playerId, int result) {
		// flowController.diceRollResult(getPlayerModelId(playerId), result);

	}

	// 8.3
	public void resourceObtain(int playerId, int[] resources) {
		// flowController.addToPlayersResource(getPlayerModelId(playerId),
		// resources);

	}

	// 8.1
	public void statusUpdate(int playerId, enums.Color color, String name, enums.PlayerState status, int victoryPoints,
			int[] resources) {
		// //int modelPlayerId = getPlayerModelId(playerId);
		// //if (modelPlayerId == 0) { // first Time id received
		// modelPlayerId = amountPlayers;
		// flowController.setPlayerColor(modelPlayerId, color);
		// flowController.setPlayerName(modelPlayerId, name);
		// amountPlayers++;
		// }
		// flowController.setPlayerState(modelPlayerId, status);
		// flowController.setPlayerVictoryPoints(modelPlayerId, victoryPoints);
		// flowController.setPlayerResources(modelPlayerId, resources);

	}

	// private int getPlayerModelId(int playerId) {
	// for (int i = 0; i < playerIDs = new int[4].length; i++) {
	// if (playerIDs = new int[4][i] == playerId) {
	// return i;
	// }
	// }
	// return 0;
	// }

	// 9.7
	public void endTurn() {
		outputHandler.endTurn();

	}

	// Bandit

	// 9.3
	public void requestSetBandit(int x, int y, int stealFromPlayerId) {
		// TODO fix
		// outputHandler.requestSetBandit(x, y, stealFromPlayerId);

	}

	// 8.5
	public void setBandit(int stealingPlayerId, int x, int y, int stealFromPlayerId) {

	}

}