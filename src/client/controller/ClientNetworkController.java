package client.controller;

import client.client.Client;
import client.client.ClientInputHandler;
import client.client.ClientOutputHandler;
import model.Board;
import model.Corner;
import model.Edge;
import model.Field;

public class ClientNetworkController {

	private FlowController flowController;
	private ClientOutputHandler outputHandler;
	private ClientInputHandler inputHandler;
	private Client client;
	private Object playerModels;
	private Board board;
	private int playerId;
	private int amountPlayers;
	private int[] playerIds;

	public ClientNetworkController(FlowController fc, Board board) {
		this.flowController = fc;
		this.board = board;
		this.client = new Client();
		client.start();
		this.outputHandler = new ClientOutputHandler(this, client);
		this.inputHandler = new ClientInputHandler(this);
		this.playerModels = flowController.board.getPlayerModels();
		this.amountPlayers = 1;
	}

	// Bauen
	// 9.4
	public void requestBuildStreet(int x, int y, int dir) {
		outputHandler.handleBuildRequest(x, y, dir, this.playerId, "Street");

	}

	// 9.4
	public void requestBuildVillage(int x, int y, int dir) {
		outputHandler.handleBuildRequest(x, y, dir, this.playerId, "Village");
		// int playerID = flowController.getPlayerID();
		// outputHandler.handleBuildRequest(x,y,dir, enum.VILLAGE,playerID);
	}

	// 9.4
	public void requestBuildCity(int x, int y, int dir) {
		outputHandler.handleBuildRequest(x, y, dir, this.playerId, "City");
		// int playerID = flowController.getPlayerID();
		// outputHandler.handleBuildRequest(x,y,dir, enum.CITY,playerID);
	}

	// Bauvorgang

	// 8.6
	public void buildStreet(int x, int y, int dir, int playerId) {
		flowController.buildStreet(x, y, dir, playerId);

	}

	// 8.6
	public void buildVillage(int x, int y, int dir, int playerId) {
		flowController.buildVillage(x, y, dir, playerId);
	}

	// 8.6
	public void buildCity(int x, int y, int dir, int playerId) {
		flowController.buildCity(x, y, dir, playerId);

	}

	// 4.1
	public void clientHello() {
		outputHandler.clientHello(settings.DefaultSettings.CLIENT_VERSION);

	}
	
	public void serverHello(String serverVersion,int protocolVersion){
		if(protocolVersion != settings.DefaultSettings.PROTOCOL_VERSION){
			client.stopClient();
			System.out.println("Invalid Protocol Version; Disconnected"); 
		}
			
	}

	// 4.2
	public void welcome(int id) {
		this.playerId = id;

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
	public void playerProfile(enums.Color color, String name) {
		playerIds[amountPlayers] = 
		flowController.createNewPlayer(color,name);
		amountPlayers++;

	}

	// 6.2
	public void chatSendMessage(String s) {
		outputHandler.chatSendMessage(s);

	}

	// 6.3
	public void chatReceiveMessage(int playerId, String s) {

	}

	// 6.1
	public void serverConfirmation(String s) {

	}

	// 9.1
	public void diceRollRequest() {
		outputHandler.diceRollRequest();

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
		outputHandler.endTurn();

	}

	// Bandit

	// 9.3
	public void requestSetBandit(int x, int y, int stealFromPlayerId) {
		outputHandler.requestSetBandit(x,y,stealFromPlayerId);

	}

	// 8.5
	public void setBandit(int stealingPlayerId, int x, int y, int stealFromPlayerId) {

	}

}