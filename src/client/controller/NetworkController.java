package client.controller;

import client.client.Client;
import client.client.InputHandler;
import client.client.OutputHandler;
import model.Corner;
import model.Edge;
import model.Field;

public class NetworkController {

	private FlowController flowController;
	private OutputHandler outputHandler;
	private InputHandler inputHandler;
	private Client client;

	public NetworkController(FlowController fc) {
		this.flowController = fc;
		this.client = new Client();
		client.start();
		this.outputHandler = new OutputHandler(this, client);
		this.inputHandler = new InputHandler(this);
	}

	// Bauen
	// 9.4
	public void requestBuildStreet(int x, int y, int dir) {

	}

	// 9.4
	public void requestBuildVillage(int x, int y, int dir) {
		// int playerID = flowController.getPlayerID();
		// outputHandler.handleBuildRequest(x,y,dir, enum.VILLAGE,playerID);
	}

	// 9.4
	public void requestBuildCity(int x, int y, int dir) {
		// int playerID = flowController.getPlayerID();
		// outputHandler.handleBuildRequest(x,y,dir, enum.CITY,playerID);
	}

	// Bauvorgang

	// 8.6
	public void buildStreet(int x, int y, int dir, int playerId) {

	}

	// 8.6
	public void buildVillage(int x, int y, int dir, int playerId) {
		// flowController.buildVillage(x,y,dir,playerId);
	}

	// 8.6
	public void buildCity(int x, int y, int dir, int playerId) {

	}

	// 4.1
	public void hello() {

	}

	// 4.2
	public void welcome(int id) {

	}

	// 7.2
	public void clientReady() {

	}

	// 7.4
	public void gameStarted(Field[] fields, Edge[] edges, Corner[] corners, Field bandit) {

	}

	// 7.3
	public void error(String s) {

	}

	// 7.1
	public void playerProfile(enums.Color color, String name) {

	}

	// 6.2
	public void chatSendMessage(String s) {

	}

	// 6.3
	public void chatReceiveMessage(int playerId, String s) {

	}

	// 6.1
	public void serverConfirmation(String s) {

	}

	// 9.1
	public void diceRollRequest() {

	}

	// 8.2
	public void diceRollResult(int playerId, int result) {

	}

	// 8.3
	public void resourceObtain(int playerId, int resources) {

	}

	// 8.1
	public void statusUpdate(int playerId, enums.Color color, String name, enums.PlayerState status, int victoryPoints,
			int[] resources) {

	}

	// 9.7
	public void endTurn() {

	}

	// Bandit

	// 9.3
	public void requestSetBandit(int x, int y, int stealFromPlayerId) {

	}

	// 8.5
	public void setBandit(int stealingPlayerId, int x, int y, int stealFromPlayerId) {

	}

}