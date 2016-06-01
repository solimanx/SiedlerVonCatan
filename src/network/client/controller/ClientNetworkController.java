package network.client.controller;

import model.Board;
import model.Corner;
import model.Edge;
import model.Field;
import model.PlayerModel;
import network.client.client.Client;
import network.client.client.ClientInputHandler;
import network.client.client.ClientOutputHandler;

public class ClientNetworkController {

	private FlowController flowController;
	private ClientOutputHandler outputHandler;
	private ClientInputHandler inputHandler;
	protected Client client;
	private PlayerModel[] playerModels;
	private Board board;
	private int ownPlayerId;
	private int amountPlayers;
	private int[] playerIds;

	public ClientNetworkController(FlowController fc) {
		this.flowController = fc;
		this.inputHandler = new ClientInputHandler(this);		
		this.client = new Client(inputHandler);
		client.start();
		this.outputHandler = new ClientOutputHandler(client);
		
		this.playerModels = flowController.board.getPlayerModels();
		this.amountPlayers = 1;
	}

	// Bauen
	// 9.4
	public void requestBuildStreet(int x, int y, int dir) {
		outputHandler.handleBuildRequest(x, y, dir, playerIds[this.ownPlayerId], "Street");

	}

	// 9.4
	public void requestBuildVillage(int x, int y, int dir) {
		outputHandler.handleBuildRequest(x, y, dir, playerIds[this.ownPlayerId], "Village");
	}

	// 9.4
	public void requestBuildCity(int x, int y, int dir) {
		outputHandler.handleBuildRequest(x, y, dir, playerIds[this.ownPlayerId], "City");
	}

	// Bauvorgang

	// 8.6
	public void buildStreet(int x, int y, int dir, int playerId) {
		flowController.buildStreet(x, y, dir, getPlayerModelId(playerId));

	}

	// 8.6
	public void buildVillage(int x, int y, int dir, int playerId) {
		flowController.buildVillage(x, y, dir, getPlayerModelId(playerId));
	}

	// 8.6
	public void buildCity(int x, int y, int dir, int playerId) {
		flowController.buildCity(x, y, dir, getPlayerModelId(playerId));

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
	    playerIds[amountPlayers] = id;			
		this.ownPlayerId = amountPlayers;
		flowController.setOwnPlayerId(ownPlayerId);
        amountPlayers++;
	}

	// 7.2
	public void clientReady() {
		outputHandler.clientReady();

	}

	// 7.4
	public void gameStarted(Field[][] fields, Edge[][][] edges, Corner[][][] corners, Field bandit) {
		this.board = flowController.initBoard(amountPlayers, fields, edges, corners, bandit);

	}

	// 7.3
	public void error(String s) {
		System.out.println(s);

	}

	// 7.1
	/*public void playerProfile(enums.Color color, String name) {
		playerIds[amountPlayers] = 
		flowController.createNewPlayer(color,name);
		amountPlayers++;

	}*/

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
		flowController.diceRollResult(getPlayerModelId(playerId),result);

	}

	// 8.3
	public void resourceObtain(int playerId, int[] resources) {
		flowController.addToPlayersResource(getPlayerModelId(playerId),resources);

	}

	// 8.1
	public void statusUpdate(int playerId, enums.Color color, String name, enums.PlayerState status, int victoryPoints,
			int[] resources) {
		int modelPlayerId = getPlayerModelId(playerId);
		if (modelPlayerId == 0){ //first Time id received
			playerIds[amountPlayers] = playerId;
			modelPlayerId = amountPlayers;
			flowController.setPlayerColor(modelPlayerId,color);
			flowController.setPlayerName(modelPlayerId,name);
			amountPlayers++;
		}
		flowController.setPlayerState(modelPlayerId, status);
		flowController.setPlayerVictoryPoints(modelPlayerId,victoryPoints);
		flowController.setPlayerResources(modelPlayerId,resources);

	}
	
	private int getPlayerModelId(int playerId){
		for (int i = 0;i <playerIds.length;i++){
			if (playerIds[i] == playerId){
				return i;
			}
		}
		return 0;
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