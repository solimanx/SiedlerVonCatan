package network.server.controller;

import java.io.IOException;

import model.Board;
import model.Corner;
import model.Edge;
import model.Field;
import network.server.controller.GameController;
import network.server.server.Server;
import network.server.server.ServerInputHandler;
import network.server.server.ServerOutputHandler;

public class ServerNetworkController {

	private GameController gameController;
	private ServerOutputHandler outputHandler;
	private ServerInputHandler inputHandler;
	private Server server;
	int amountPlayers;
	int[] playerIds;
	private int IdCounter = 0;

	public ServerNetworkController(GameController gc) {
		this.gameController = gc;
		this.inputHandler = new ServerInputHandler(this);
		this.server = new Server(inputHandler);
		try {
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.outputHandler = new ServerOutputHandler(server);

		this.amountPlayers = 1;
		this.playerIds = new int[5];
	}

	public void initBoard(int amountPlayers, Field[][] serverFields, Edge[][][] edges, Corner[][][] corners,
			Field bandit) {
		// outputHandler

	}

	public void statusUpdate(int playerId, enums.Color color, String name, enums.PlayerState status, int victoryPoints,
			int[] resources) {
		outputHandler.statusUpdate(playerId, color, name, status, victoryPoints, resources);
	}

	public void statusUpdate(enums.Color color, String name, int playerId) {
		if (gameController.checkColor(color)) {
			gameController.setPlayerColor(playerId, color);
			gameController.setPlayerName(playerId, name);
		} else {
			error(playerId, "Farbe bereits vergeben!");
		}
	}

	// Bauen
	// 9.4
	public void requestBuildStreet(int x, int y, int dir, int playerId) {
		gameController.buildStreet(x, y, dir, getPlayerModelId(playerId));

	}

	// 9.4
	public void requestBuildVillage(int x, int y, int dir, int playerId) {
		gameController.buildVillage(x, y, dir, getPlayerModelId(playerId));
	}

	// 9.4
	public void requestBuildCity(int x, int y, int dir, int playerId) {
		gameController.buildCity(x, y, dir, getPlayerModelId(playerId));
	}

	// Bauvorgang

	// 8.6
	public void buildStreet(int x, int y, int dir, int playerId) {
		outputHandler.buildBuilding(x, y, dir, playerIds[playerId], "Street");

	}

	// 8.6
	public void buildVillage(int x, int y, int dir, int playerId) {
		outputHandler.buildBuilding(x, y, dir, playerIds[playerId], "Village");
	}

	// 8.6
	public void buildCity(int x, int y, int dir, int playerId) {
		outputHandler.buildBuilding(x, y, dir, playerIds[playerId], "City");

	}

	// 4.1
	// useless? can be deleted since it's never used
	public void clientHello(int playerId, String version) {
		welcome(playerId);

	}

	/**
	 * Server sends Hello message to ServerOutputHandler
	 */
	public void serverHello() {
		outputHandler.hello(settings.DefaultSettings.SERVER_VERSION, settings.DefaultSettings.PROTOCOL_VERSION);

	}

	// 4.2
	public void welcome(int playerId) {
		outputHandler.welcome(playerId);
	}

	private int getNewId() {
		return IdCounter++;
	}

	// 7.2
	/**
	 * checks if all clients are Ready (PlayerState.WAITING) if yes then
	 * gameController initializes the Board
	 * 
	 * @param playerId
	 */
	public void clientReady(int playerId) {
		gameController.setPlayerState(playerId, enums.PlayerState.WAITING);
		boolean allPlayersReady = true;
		for (int i = 1; i <= amountPlayers; i++) {
			if (gameController.getPlayerState(playerId) != enums.PlayerState.WAITING) {
				allPlayersReady = false;
			}
		}
		if (allPlayersReady) {
			gameController.init();
		}
	}

	// 7.4
	public void gameStarted(Field[][] fields, Edge[][][] edges, Corner[][][] corners, Field bandit) {
		outputHandler.initBoard(amountPlayers, fields, edges, corners, bandit);

	}

	// 7.3
	public void error(int playerId, String s) {
		outputHandler.error(playerId, s);
		// System.out.println(s);

	}

	// 6.2
	public void chatSendMessage(String s, int playerId) {
		for (int i = 1; i <= playerIds.length; i++) {
			chatReceiveMessage(getPlayerModelId(playerId), s);
		}
	}

	// 6.3
	public void chatReceiveMessage(int playerId, String s) {
		outputHandler.chatReceiveMessage(playerIds[playerId], s);

	}

	// 6.1
	public void serverConfirmation(String s) {

	}

	// 9.1
	public void diceRollRequest(int playerId) {
		gameController.diceRollRequest(getPlayerModelId(playerId));

	}

	// 8.2
	public void diceRollResult(int playerId, int result) {
		outputHandler.diceRollResult(playerIds[playerId], result);

	}

	// 8.3
	public void resourceObtain(int playerId, int[] resources) {
		outputHandler.resourceObtain(playerIds[playerId], resources);

	}

	private int getPlayerModelId(int playerId) {
		for (int i = 0; i < playerIds.length; i++) {
			if (playerIds[i] == playerId) {
				return i;
			}
		}
		return 0;
	}
	
	private int getThreadID(int playerModelID) {
		return playerIds[playerModelID];
	}

	// 9.7
	public void endTurn(int playerId) {
		gameController.endTurn(getPlayerModelId(playerId));

	}

	// Bandit

	// 9.3
	public void requestSetBandit(int x, int y, int stealFromPlayerId, int playerId) {
		gameController.requestSetBandit(x, y, stealFromPlayerId, playerId);

	}

	// 8.5
	public void setBandit(int stealingPlayerId, int x, int y, int stealFromPlayerId) {
		// outputHandler.

	}

}

// outputHandler.clientHello("JavaFXClient
// "+settings.DefaultSettings.CLIENT_VERSION+" (Niedliche
// Pixel)",settings.DefaultSettings.PROTOCOL_VERSION);
