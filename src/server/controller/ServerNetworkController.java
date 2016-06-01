package server.controller;

import server.server.ServerInputHandler;
import server.server.ServerOutputHandler;
import server.server.Server;

import java.io.IOException;

import model.Board;
import model.Corner;
import model.Edge;
import model.Field;
import server.controller.GameController;

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
		this.server = new Server();
		try {
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.outputHandler = new ServerOutputHandler(this, server);
		this.inputHandler = new ServerInputHandler(this);
		
		this.amountPlayers = 1;
		this.playerIds = new int[5];
	}
	
public void initBoard(int amountPlayers, Field[][] serverFields, Edge[][][] edges, Corner[][][] corners,
		Field bandit){
	//outputHandler
	
}

public void statusUpdate(int playerId, enums.Color color, String name, enums.PlayerState status, int victoryPoints,
		int[] resources) {
	   outputHandler.statusUpdate(playerId,color,name,status,victoryPoints,resources);
}

public void statusUpdate(enums.Color color,String name,int playerId){
	if (gameController.checkColor(color)){
		gameController.setPlayerColor(playerId,color);
		gameController.setPlayerName(playerId,name);
	} else {
		error("Farbe bereits vergeben!");
	}
}

// Bauen
// 9.4
public void requestBuildStreet(int x, int y, int dir,int playerId) {
	gameController.buildStreet(x, y, dir, getModelPlayerId(playerId));

}

// 9.4
public void requestBuildVillage(int x, int y, int dir,int playerId) {
	gameController.buildVillage(x, y, dir, getModelPlayerId(playerId));
}

// 9.4
public void requestBuildCity(int x, int y, int dir,int playerId) {
	gameController.buildCity(x, y, dir, getModelPlayerId(playerId));
}

// Bauvorgang

// 8.6
public void buildStreet(int x, int y, int dir, int playerId) {
	outputHandler.buildStreet(x, y, dir, playerIds[playerId]);

}

// 8.6
public void buildVillage(int x, int y, int dir, int playerId) {
	outputHandler.buildVillage(x, y, dir, playerIds[playerId]);
}

// 8.6
public void buildCity(int x, int y, int dir, int playerId) {
	outputHandler.buildCity(x, y, dir, playerIds[playerId]);

}

// 4.1
public void clientHello(int playerId,double version) {
	if (version != settings.DefaultSettings.PROTOCOL_VERSION){
		error(playerId,"Different Protocol Versions; Disconnecting!");
		//server.client[playerId].close();
	} else{
		welcome();
	}

}

public void serverHello(){
	outputHandler.hello(settings.DefaultSettings.SERVER_VERSION,settings.DefaultSettings.PROTOCOL_VERSION);
		
}

// 4.2
public void welcome() {
    playerIds[amountPlayers] = getNewId();			
    amountPlayers++;
}

private int getNewId(){
	return IdCounter ++;
}

// 7.2
/**
 * checks if all clients are Ready (PlayerState.WAITING)
 * if yes then gameController initializes the Board
 * @param playerId
 */
public void clientReady(int playerId) {
    gameController.setPlayerState(playerId, enums.PlayerState.WAITING);
    boolean allPlayersReady = true;
    for (int i = 1; i <= amountPlayers;i++){
    	if (gameController.getPlayerState(playerId) != enums.PlayerState.WAITING){
    		allPlayersReady = false;
    	}
    }
    if (allPlayersReady){
    	gameController.init();
    }
}

// 7.4
public void gameStarted(Field[][] fields, Edge[][][] edges, Corner[][][] corners, Field bandit) {
	outputHandler.initBoard(amountPlayers, fields, edges, corners, bandit);

}

// 7.3
public void error(int playerId,String s) {
	outputHandler.error(playerId,s);
	//System.out.println(s);

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
public void diceRollRequest(int playerId) {
	gameController.diceRollRequest(getPlayerModelId(playerId));

}

// 8.2
public void diceRollResult(int playerId, int result) {
	outputHandler.diceRollResult(playerIds[playerId],result);

}

// 8.3
public void resourceObtain(int playerId, int[] resources) {
	outputHandler.addToPlayersResource(playerIds[playerId],resources);

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





//outputHandler.clientHello("JavaFXClient "+settings.DefaultSettings.CLIENT_VERSION+" (Niedliche Pixel)",settings.DefaultSettings.PROTOCOL_VERSION);
