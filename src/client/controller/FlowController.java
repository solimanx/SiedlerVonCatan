package client.controller;

import java.util.ArrayList;

import enums.Color;
import enums.PlayerState;
import enums.ResourceType;
import javafx.stage.Stage;
import model.Board;
import model.Corner;
import model.Edge;
import model.Field;
import model.GameLogic;
import model.PlayerModel;
import settings.DefaultSettings;

/**
 * @author NiedlichePixel Controls the game flow.
 */
public class FlowController {
	Board board;
	GameLogic gameLogic;
	PlayerModel[] playerModels;
	int ownPlayerId;
	protected ViewController viewController;
	protected Field[][] fields;
	protected ClientNetworkController networkController;
	private Object mainViewController;
	
	
	public FlowController(Stage primaryStage, int amountPlayers){
		this.networkController = new ClientNetworkController(this);
		
		this.viewController = new ViewController(primaryStage, board, this);
		
		// hier muss das Board über Netzwerk erhalten werden
		this.board = Board.getInstance(amountPlayers);
		this.gameLogic = new GameLogic(board);
		this.playerModels = board.getPlayerModels();
		this.fields = board.getFields();
	}
	
	
	public void init(Stage primaryStage) {
		this.viewController = new ViewController(primaryStage, this);
		this.mainViewController = viewController.getMainViewController();
		this.networkController = new ClientNetworkController(this);		
		//this.viewController = new ViewController(primaryStage, board, this);
		//this.networkController = new ClientNetworkController(this);

	}

	/**
	 * sets player state in own client model; is called by network controller
	 * after server has changed a player state
	 * 
	 * @param state
	 */
	public void setPlayerState(int playerId, PlayerState state) {
		playerModels[playerId].setPlayerState(state);
		if (playerId == ownPlayerId) {
			// update GUI
			// viewController.setPlayerState(state);
		}
	}

	public void setGameState() {
		// TODO Auto-generated method stub

	}

	public Board initBoard(int amountPlayers, Field[][] serverFields, Edge[][][] edges, Corner[][][] corners,
			Field bandit) {

		this.board = Board.getInstance(amountPlayers);
		this.fields = board.getFields();
		for (int i = 0; i < fields.length; i++) {
			for (int j = 0; j < fields[i].length; j++) {
				fields[i][j].setResourceType(serverFields[i][j].getResourceType());
				fields[i][j].setDiceIndex(serverFields[i][j].getDiceIndex());
			}
		}
		Edge[][][] ownEdges = board.getEdges();
		for (int i = 0; i < edges.length; i++) {
			for (int j = 0; j < edges[i].length; j++) {
				ownEdges[i][j][0].setHasStreet(edges[i][j][0].isHasStreet());
				ownEdges[i][j][0].setOwnedByPlayer(edges[i][j][0].getOwnedByPlayer());

				ownEdges[i][j][1].setHasStreet(edges[i][j][1].isHasStreet());
				ownEdges[i][j][1].setOwnedByPlayer(edges[i][j][1].getOwnedByPlayer());
			}
		}

		Corner[][][] ownCorners = board.getCorners();
		for (int i = 0; i < corners.length; i++) {
			for (int j = 0; j < corners[i].length; j++) {
				ownCorners[i][j][0].setHarbourStatus(corners[i][j][0].getHarbourStatus());
				ownCorners[i][j][0].setOwnedByPlayer(corners[i][j][0].getOwnedByPlayer());
				ownCorners[i][j][0].setStatus(corners[i][j][0].getStatus());

				ownCorners[i][j][1].setHarbourStatus(corners[i][j][1].getHarbourStatus());
				ownCorners[i][j][1].setOwnedByPlayer(corners[i][j][1].getOwnedByPlayer());
				ownCorners[i][j][1].setStatus(corners[i][j][1].getStatus());

				ownCorners[i][j][2].setHarbourStatus(corners[i][j][2].getHarbourStatus());
				ownCorners[i][j][2].setOwnedByPlayer(corners[i][j][2].getOwnedByPlayer());
				ownCorners[i][j][2].setStatus(corners[i][j][2].getStatus());
			}
		}
		int[] banditCoordinates = board.getFieldCoordinates(bandit);
		board.setBandit(board.getFieldAt(banditCoordinates[0], banditCoordinates[1]));

		this.gameLogic = new GameLogic(board);
		this.playerModels = board.getPlayerModels();
		
		//viewController.mainViewController.init(board);
		
		return board;

	}
	
	public void createNewPlayer(enums.Color color,String name){
		
	}

	public void requestBuildVillage(int x, int y, int dir) {
		if (gameLogic.checkBuildVillage(x, y, dir, ownPlayerId)) {
			networkController.requestBuildVillage(x, y, dir);
		}

	}

	public void requestBuildStreet(int x, int y, int dir) {
		if (gameLogic.checkBuildStreet(x, y, dir, ownPlayerId)) {
			networkController.requestBuildStreet(x, y, dir);
		}

	}

	public void requestBuildCity(int x, int y, int dir) {
		if (gameLogic.checkBuildCity(x, y, dir, ownPlayerId)) {
			networkController.requestBuildCity(x, y, dir);
		}
	}

	public void buildStreet(int x, int y, int dir, int playerId) {
		Edge e = board.getEdgeAt(x, y, dir);
		e.setHasStreet(true);
		e.setOwnedByPlayer(playerModels[playerId]);

		viewController.mainViewController.setStreet(x, y, dir, playerId);
	}

	public void buildVillage(int x, int y, int dir, int playerId) {
		Corner c = board.getCornerAt(x, y, dir);
		c.setStatus(enums.CornerStatus.VILLAGE);
		c.setOwnedByPlayer(playerModels[playerId]);
		Corner[] neighbors = board.getAdjacentCorners(x, y, dir);
		for (int i = 0; i < neighbors.length; i++) {
			if (neighbors[i] != null) {
				neighbors[i].setStatus(enums.CornerStatus.BLOCKED);
			}
		}

		viewController.mainViewController.setCorner(x, y, dir, enums.CornerStatus.VILLAGE, playerId);
	}

	public void buildCity(int x, int y, int dir, int playerId) {
		Corner c = board.getCornerAt(x, y, dir);
		c.setStatus(enums.CornerStatus.CITY);
		c.setOwnedByPlayer(playerModels[playerId]);

		viewController.mainViewController.setCorner(x, y, dir, enums.CornerStatus.CITY, playerId);
	}

	public void setBandit(int x, int y,int playerId) {
		if (gameLogic.checkSetBandit(x, y,playerId)) {
			networkController.requestSetBandit(x,y,playerId);
		}

	}

	public void addToPlayersResource(int playerId, int[] resources) {
		ArrayList<ResourceType> resourceCards = playerModels[playerId].getResourceCards();
		for (int i = 0;i < resources.length;i++){
			for (int j = 0;j < resources[i];j++){
				resourceCards.add(settings.DefaultSettings.RESOURCE_ORDER[i]);
			}
		}
		playerModels[playerId].setResourceCards(resourceCards);
		
	}
	
	public void setPlayerResources(int playerId, int[] resources) {
		ArrayList<ResourceType> resourceCards = new ArrayList<ResourceType>();
		for (int i = 0;i < resources.length;i++){
			for (int j = 0;j < resources[i];j++){
				resourceCards.add(settings.DefaultSettings.RESOURCE_ORDER[i]);
			}
		}
		playerModels[playerId].setResourceCards(resourceCards);
		
	}	
	
	public void setPlayerColor(int playerId, Color color) {
		viewController.setPlayerColor(playerId,color);
		
	}

	public void setPlayerName(int playerId, String name) {
		viewController.setPlayerName(playerId,name);
		
	}

	public void setPlayerVictoryPoints(int playerId, int victoryPoints) {
		playerModels[playerId].setVictoryPoints(victoryPoints);		
	}		

	public void diceRollResult(int playerId, int result) {
		viewController.setDiceRollResult(playerId,result);		
	}

	public void setOwnPlayerId(int ownPlayerId) {
		this.ownPlayerId = ownPlayerId;		
	}
}
