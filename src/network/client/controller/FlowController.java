package network.client.controller;

import java.util.ArrayList;

import enums.Color;
import enums.HarbourStatus;
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
	private Board board;
	private GameLogic gameLogic;
	private int ownPlayerId;
	protected ViewController viewController;
	private ClientNetworkController clientNetworkController;

	public FlowController(Stage primaryStage) {
		this.setClientNetworkController(new ClientNetworkController(this));
		// this.mainViewController = viewController.getMainViewController();
		this.viewController = new ViewController(primaryStage, this);

	}

	public Board getBoard() {
		return board;
	}

	public int getOwnPlayerId() {
		return ownPlayerId;
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

	public void setGameState() {
		// TODO Auto-generated method stub

	}

	/*
	 * 
	 */
	public Board initBoard(int amountPlayers, Field[][] serverFields, Edge[][][] edges, Corner[][][] corners,
			Field bandit) {

		int board_size = DefaultSettings.BOARD_SIZE;
		for (int i = 0; i < board_size; i++) {
			for (int j = 0; j < board_size; j++) {
				board.getField(i, j).setResourceType(serverFields[i][j].getResourceType());
				board.getField(i, j).setDiceIndex(serverFields[i][j].getDiceIndex());
			}
		}
		for (int i = 0; i < board_size; i++) {
			for (int j = 0; j < board_size; j++) {
				for (int k = 0; j < 3; k++) {
					board.getEdge(i, j, k).setHasStreet(edges[i][j][k].isHasStreet());
					board.getEdge(i, j, k).setOwnedByPlayer(edges[i][j][k].getOwnerID());

				}
			}
		}
		for (int i = 0; i < corners.length; i++) {
			for (int j = 0; j < corners[i].length; j++) {
				for (int k = 0; k < 2; k++) {
					board.getCorner(i, j, k).setHarbourStatus(corners[i][j][k].getHarbourStatus());
					board.getCorner(i, j, k).setOwnerID(corners[i][j][k].getOwnerID());
					board.getCorner(i, j, k).setStatus(corners[i][j][k].getStatus());
				}
			}
		}
		// int[] banditCoordinates = board.getFieldCoordinates(bandit);
		// board.setBandit(board.getFieldAt(banditCoordinates[0],
		// banditCoordinates[1]));

		this.gameLogic = new GameLogic(board);

		viewController.startMainView();

		return board;

	}

	public void createNewPlayer(enums.Color color, String name) {

	}

	public void requestBuildVillage(int x, int y, int dir) {
		if (gameLogic.checkBuildVillage(x, y, dir, ownPlayerId)) {
			getClientNetworkController().requestBuildVillage(x, y, dir);
		}

	}

	public void requestBuildStreet(int x, int y, int dir) {
		if (gameLogic.checkBuildStreet(x, y, dir, ownPlayerId)) {
			getClientNetworkController().requestBuildStreet(x, y, dir);
		}

	}

	public void requestBuildCity(int x, int y, int dir) {
		if (gameLogic.checkBuildCity(x, y, dir, ownPlayerId)) {
			getClientNetworkController().requestBuildCity(x, y, dir);
		}
	}

	public void buildStreet(int x, int y, int dir, int playerID) {
		Edge e = board.getEdgeAt(x, y, dir);
		e.setHasStreet(true);
		e.setOwnedByPlayer(board.getPlayer(playerID).getID());

		viewController.getMainViewController().setStreet(x, y, dir, playerID);
	}

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

		viewController.getMainViewController().setCorner(x, y, dir, enums.CornerStatus.VILLAGE, playerID);
	}

	public void buildCity(int x, int y, int dir, int playerID) {
		Corner c = board.getCornerAt(x, y, dir);
		c.setStatus(enums.CornerStatus.CITY);
		c.setOwnerID(playerID);

		viewController.getMainViewController().setCorner(x, y, dir, enums.CornerStatus.CITY, playerID);
	}

	public void setBandit(int x, int y, int playerId) {
		if (gameLogic.checkSetBandit(x, y, playerId)) {
			getClientNetworkController().requestSetBandit(x, y, playerId);
		}

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

	public void sendPlayerProfile(String name, Color color) {
		clientNetworkController.sendPlayerProfile(color, name);
		setPlayerColor(ownPlayerId, color);
		setPlayerName(ownPlayerId, name);
	}

	public void setPlayerVictoryPoints(int playerId, int victoryPoints) {
		board.getPlayer(playerId).setVictoryPoints(victoryPoints);
	}

	public void diceRollResult(int playerId, int result) {
		viewController.setDiceRollResult(playerId, result);
	}

	public void setOwnPlayerId(int ownPlayerId) {
		this.ownPlayerId = ownPlayerId;
	}

	public void chatSendMessage(String s) {
		getClientNetworkController().chatSendMessage(s);
	}

	public void chatReceiveMessage(int playerId, String s) {
		// viewController.mainViewController.receiveChatMessage("Spieler
		// "+playerId+": "+s);
		viewController.getLobbyController().receiveChatMessage("Spieler " + playerId + ": " + s);
	}

	public void sendReady() {
		clientNetworkController.clientReady();
	}

	public ClientNetworkController getClientNetworkController() {
		return clientNetworkController;
	}

	public void setClientNetworkController(ClientNetworkController networkController) {
		this.clientNetworkController = networkController;
	}
}
