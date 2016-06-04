package network.server.controller;

import java.io.IOException;

import model.Board;
import model.Corner;
import model.Edge;
import model.Field;
import model.PlayerModel;
import network.server.controller.GameController;
import network.server.server.Server;
import network.server.server.ServerInputHandler;
import network.server.server.ServerOutputHandler;

public class ServerNetworkController {

	private GameController gameController;
	private ServerOutputHandler outputHandler;
	private ServerInputHandler inputHandler;
	private Server server;

	public ServerNetworkController(GameController gc) {
		this.gameController = gc;
		this.inputHandler = new ServerInputHandler(this);
		this.server = new Server(inputHandler, this);
		this.outputHandler = new ServerOutputHandler(server, this);
		try {
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ServerOutputHandler getOutputHandler() {
		return outputHandler;
	}

	public void setOutputHandler(ServerOutputHandler outputHandler) {
		this.outputHandler = outputHandler;
	}

	public ServerInputHandler getInputHandler() {
		return inputHandler;
	}

	public void setInputHandler(ServerInputHandler inputHandler) {
		this.inputHandler = inputHandler;
	}

	public void initBoard(int amountPlayers, Field[][] serverFields, Edge[][][] edges, Corner[][][] corners,
			Field bandit) {
		// outputHandler

	}

	public void statusUpdate(int playerID, enums.Color color, String name, enums.PlayerState status, int victoryPoints,
			int[] resources) {
		outputHandler.statusUpdate(playerID, color, name, status, victoryPoints, resources);
	}

	public void statusUpdate(enums.Color color, String name, int playerID) {
		if (gameController.checkColor(color)) {
			gameController.setPlayerColor(playerID, color);
			gameController.setPlayerName(playerID, name);
		} else {
			// error(playerID, "Farbe bereits vergeben!");
		}
	}

	// Bauen
	// 9.4
	public void requestBuildStreet(int x, int y, int dir, int playerID) {
		gameController.buildStreet(x, y, dir, getPlayerModelId(playerID));

	}

	// 9.4
	public void requestBuildVillage(int x, int y, int dir, int playerID) {
		gameController.buildVillage(x, y, dir, getPlayerModelId(playerID));
	}

	// 9.4
	public void requestBuildCity(int x, int y, int dir, int playerID) {
		gameController.buildCity(x, y, dir, getPlayerModelId(playerID));
	}

	// Bauvorgang

	// 8.6
	public void buildStreet(int x, int y, int dir, int playerID) {
		//TODO
		//outputHandler.buildBuilding(x, y, dir, playerIDs[playerID], "Street");

	}

	// 8.6
	public void buildVillage(int x, int y, int dir, int playerID) {
		//TODO
		//outputHandler.buildBuilding(x, y, dir, playerIDs[playerID], "Village");
	}

	// 8.6
	public void buildCity(int x, int y, int dir, int playerID) {
		//TODO
		//outputHandler.buildBuilding(x, y, dir, playerIDs[playerID], "City");

	}

	// 4.1
	// useless? can be deleted since it's never used
	public void clientHello(int playerID, String version) {
		welcome(playerID);

	}

	/**
	 * Server sends Hello message to ServerOutputHandler
	 */
	public void serverHello(int playerID) {
		outputHandler.hello(settings.DefaultSettings.SERVER_VERSION, settings.DefaultSettings.PROTOCOL_VERSION,
				getPlayerModelId(playerID));

	}

	// 4.2
	public void welcome(int playerID) {
		outputHandler.welcome(playerID);
	}

	private int getNewId() {
		//TODO return IdCounter++;
		return 0;
	}

	// 7.2
	/**
	 * checks if all clients are Ready (PlayerState.WAITING) if yes then
	 * gameController initializes the Board
	 *
	 * @param playerID
	 */
	public void clientReady(int playerID) {
		PlayerModel player = new PlayerModel(playerID);
		//gameController.addPlayerToArray(player);
//		gameController.setPlayerState(playerID, enums.PlayerState.WAITING); //TODO WaintingforGamestart
//		boolean allPlayersReady = true;
//		for (int i = 1; i <= amountPlayers; i++) {
//			if (gameController.getPlayerState(playerID) != enums.PlayerState.WAITING) { //TODO WaintingforGamestart
//				// allPlayersReady = false;
//			}
//		}
//		if (allPlayersReady) {
		gameController.addPlayerToArray(player);
//		}
	}

	// 7.4
	public void gameStarted(Field[][] fields, Edge[][][] edges, Corner[][][] corners, Field bandit) {
		//TODO 
		//outputHandler.initBoard(amountPlayers, fields, edges, corners, bandit);

	}

	// 7.3
	public void error(String s) {
		outputHandler.error(s);
		// System.out.println(s);

	}

	// 6.2
	public void chatSendMessage(String s, int threadID) {
		chatReceiveMessage(getPlayerModelId(threadID), s);

	}

	// 6.3
	public void chatReceiveMessage(int playerID, String s) {
		outputHandler.chatReceiveMessage(playerID, s);

	}

	// 6.1
	public void serverConfirmation(String s) {

	}

	// 9.1
	public void diceRollRequest(int playerID) {
		gameController.diceRollRequest(getPlayerModelId(playerID));

	}

	// 8.2
	public void diceRollResult(int playerID, int result) {
		//TODO
		//outputHandler.diceRollResult(playerIDs[playerID], result);

	}

	// 8.3
	public void resourceObtain(int playerID, int[] resources) {
		//TODO
		//outputHandler.resourceObtain(playerIDs[playerID], resources);

	}

	public int getPlayerModelId(int threadID) {
		for (int i = 0; i < server.getClients().length; i++) {
			if (server.getClients()[i].threadID == threadID) {
				return i + 1;
			}
		}
		return 0;
	}

	public int getThreadID(int playerModelID) {
		return server.getClients()[playerModelID - 1].threadID;
		// playerIDs[playerModelID];
	}

	// 9.7
	public void endTurn(int playerID) {
		gameController.endTurn(getPlayerModelId(playerID));

	}

	// Bandit

	// 9.3
	public void requestSetBandit(int x, int y, int stealFromPlayerID, int playerID) {
		gameController.requestSetBandit(x, y, stealFromPlayerID, playerID);

	}

	// 8.5
	public void setBandit(int stealingPlayerID, int x, int y, int stealFromPlayerID) {
		// outputHandler.

	}

}

// outputHandler.clientHello("JavaFXClient
// "+settings.DefaultSettings.CLIENT_VERSION+" (Niedliche
// Pixel)",settings.DefaultSettings.PROTOCOL_VERSION);
