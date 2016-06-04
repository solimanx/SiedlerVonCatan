package network.server.controller;

import java.util.ArrayList;
import java.util.Random;

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
import network.client.client.Client;
import network.client.controller.ViewController;
import settings.DefaultSettings;

/**
 * Controls the game flow
 *
 * @author NiedlichePixel
 *
 *
 */
public class GameController implements GameControllerInterface {
	private Board board;
	private GameLogic gameLogic;
	private PlayerModel[] playerModels;
	private Field[][] fields;
	private ViewController viewController;
	private ServerNetworkController networkController;

	public GameController(Stage primaryStage) {
		this.networkController = new ServerNetworkController(this);

	}

	// @Override
	public void init(int amountPlayers) {
		//TODO fix this.board = Board.getInstance(amountPlayers);
		this.gameLogic = new GameLogic(board);
		this.playerModels = board.getPlayerModels();
		this.fields = board.getFields();
		// ONLY!!
		// viewController = new ViewController(primaryStage, board, this); //
		// DEBUG
		init();
		generateBoard(fields[2][2], false);
		/*
		 * for (int i = 1; i <= amountPlayers;i++){
		 * networkController.initClients(i,board)... end
		 */
		addToPlayersResource(1, ResourceType.WOOD, 3); // All DEBUG!!
		addToPlayersResource(1, ResourceType.CLAY, 3);
		addToPlayersResource(1, ResourceType.ORE, 3);
		addToPlayersResource(1, ResourceType.SHEEP, 3);
		addToPlayersResource(1, ResourceType.CORN, 3);
		setPlayerState(1, PlayerState.PLAYING); // player 1 begins

	}

	/**
	 * Generates the resource and the dice index of each field calls gui via
	 * setField to set the correct graphics if randomDesert is set then the
	 * desert will be placed random at the board, else it will be set in the
	 * middle
	 *
	 * @param initialField
	 * @param randomDesert
	 */
	private void generateBoard(Field initialField, boolean randomDesert) {
		ArrayList<Field> fields = board.getAllFields(); // spiral implementieren
		System.out.println("Size" + fields.size());
		int[] cards = DefaultSettings.LANDSCAPE_CARDS;
		int currNum;
		if (randomDesert) {
			int diceInd = 0;
			for (int i = 0; i < fields.size(); i++) {

				Random r = new Random();
				boolean notFound = true;
				do {
					currNum = r.nextInt(6); // desert allowed
					if (cards[currNum] > 0) {
						notFound = false;
					}
				} while (notFound);
				cards[currNum]--;
				fields.get(i).setResourceType(DefaultSettings.RESOURCE_ORDER[currNum]);
				if (currNum != 5) {
					fields.get(i).setDiceIndex(DefaultSettings.DICE_NUMBERS[diceInd]);
					diceInd++;
				} else {
					fields.get(i).setDiceIndex(0);
				}
			}
		} else {
			for (int i = 0; i < fields.size() - 1; i++) {
				Random r = new Random();
				boolean notFound = true;
				do {
					currNum = r.nextInt(5);
					if (cards[currNum] > 0) {
						notFound = false;
					}
				} while (notFound);
				cards[currNum]--;
				fields.get(i).setResourceType(DefaultSettings.RESOURCE_ORDER[currNum]);
				fields.get(i).setDiceIndex(DefaultSettings.DICE_NUMBERS[i]);
			}
			fields.get(fields.size() - 1).setResourceType(ResourceType.NOTHING); // inner
																					// field
																					// =
																					// desert;
			fields.get(fields.size() - 1).setDiceIndex(0);
		}
		int[] viewCoord = new int[2];
		for (int i = 0; i < fields.size(); i++) {
			viewCoord = board.getFieldCoordinates(fields.get(i));
			viewController.getMainViewController().setField(viewCoord[0], viewCoord[1], fields.get(i).getResourceType(),
					fields.get(i).getDiceIndex());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.controller.GameControllerInterface#gainBoardResources(int)
	 */
	public void gainBoardResources(int diceNum) {
		ArrayList<model.Field> correspondingFields = new ArrayList<model.Field>();
		for (int i = 0; i < fields.length; i++) {
			for (int j = 0; j < fields[i].length; j++) {
				if (fields[i][j] != null) {
					if (fields[i][j].getDiceIndex() == diceNum) {
						correspondingFields.add(fields[i][j]);
					}
				}
			}
		}

		Corner[] neighborCorners;
		enums.CornerStatus status;
		enums.ResourceType resType;
		Field bandit = board.getBandit();
		int[] fieldCoordinates = new int[2];
		for (Field p : correspondingFields) {
			if (p != bandit) {
				fieldCoordinates = board.getFieldCoordinates(p);
				neighborCorners = board.getSurroundingCorners(fieldCoordinates[0], fieldCoordinates[1]);
				resType = p.getResourceType();
				for (Corner o : neighborCorners) {
					status = o.getStatus();
					switch (status) {
					case VILLAGE:
						addToPlayersResource(o.getOwnedByPlayer().getId(), resType, 1);
						break;
					case CITY:
						addToPlayersResource(o.getOwnedByPlayer().getId(), resType, 2);
						break;
					default:
						break;
					}
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.controller.GameControllerInterface#buildVillage(int, int,
	 * int, int)
	 */
	@Override
	public void buildVillage(int x, int y, int dir, int playerID) {
		if (gameLogic.checkBuildVillage(x, y, dir, playerID)) {
			Corner c = board.getCornerAt(x, y, dir);
			c.setStatus(enums.CornerStatus.VILLAGE);
			c.setOwnedByPlayer(playerModels[playerID]);
			playerModels[playerID].decreaseAmountVillages();
			Corner[] neighbors = board.getAdjacentCorners(x, y, dir);
			for (int i = 0; i < neighbors.length; i++) {
				if (neighbors[i] != null) {
					neighbors[i].setStatus(enums.CornerStatus.BLOCKED);
				}
			}

			subFromPlayersResources(playerID, settings.DefaultSettings.VILLAGE_BUILD_COST);

			viewController.getMainViewController().setCorner(x, y, dir, enums.CornerStatus.VILLAGE, playerID);
		}

	}

	@Override
	public void buildStreet(int x, int y, int dir, int playerID) {
		if (gameLogic.checkBuildStreet(x, y, dir, playerID)) {
			Edge e = board.getEdgeAt(x, y, dir);
			e.setHasStreet(true);
			e.setOwnedByPlayer(playerModels[playerID]);
			playerModels[playerID].decreaseAmountStreets();

			subFromPlayersResources(playerID, settings.DefaultSettings.STREET_BUILD_COST);

			viewController.getMainViewController().setStreet(x, y, dir, playerID);
		}

	}

	@Override
	public void buildCity(int x, int y, int dir, int playerID) {
		if (gameLogic.checkBuildCity(x, y, dir, playerID)) {
			Corner c = board.getCornerAt(x, y, dir);
			c.setStatus(enums.CornerStatus.CITY);
			c.setOwnedByPlayer(playerModels[playerID]);
			playerModels[playerID].increaseAmountVillages();
			playerModels[playerID].decreaseAmountCities();

			subFromPlayersResources(playerID, settings.DefaultSettings.CITY_BUILD_COST);

			viewController.getMainViewController().setCorner(x, y, dir, enums.CornerStatus.CITY, playerID);
		}

	}

	public void buildInitialStreet(int x, int y, int dir, int playerID) {
		if (gameLogic.checkBuildInitialStreet(x, y, dir, playerID)) {
			Edge e = board.getEdgeAt(x, y, dir);
			e.setHasStreet(true);
			e.setOwnedByPlayer(playerModels[playerID]);
			playerModels[playerID].decreaseAmountStreets();

			viewController.getMainViewController().setStreet(x, y, dir, playerID);
		}
	}

	public void buildInitialVillage(int x, int y, int dir, int playerID) {
		if (gameLogic.checkBuildInitialVillage(x, y, dir)) {
			Corner c = board.getCornerAt(x, y, dir);
			c.setStatus(enums.CornerStatus.VILLAGE);
			c.setOwnedByPlayer(playerModels[playerID]);
			playerModels[playerID].decreaseAmountVillages();
			Corner[] neighbors = board.getAdjacentCorners(x, y, dir);
			for (int i = 0; i < neighbors.length; i++) {
				if (neighbors[i] != null) {
					neighbors[i].setStatus(enums.CornerStatus.BLOCKED);
				}
			}
			viewController.getMainViewController().setCorner(x, y, dir, enums.CornerStatus.VILLAGE, playerID);
		}
	}

	private void addToPlayersResource(int playerID, ResourceType resType, int amount) {
		ArrayList<ResourceType> resourceCards = playerModels[playerID].getResourceCards();
		for (int i = 0; i < amount; i++) {
			resourceCards.add(resType);
		}
		playerModels[playerID].setResourceCards(resourceCards);
	}

	private void subFromPlayersResources(int playerID, int[] costsparam) {
		int[] costs = new int[5];
		for (int i = 0; i < costsparam.length; i++) {
			costs[i] = costsparam[i];
		}
		ResourceType currResType;
		ArrayList<ResourceType> list = new ArrayList<ResourceType>();
		list = playerModels[playerID].getResourceCards();
		for (int i = 0; i < costs.length; i++) {
			for (int j = list.size() - 1; j >= 0; j--) { // umkehren wegen
															// remove
				currResType = list.get(j);
				switch (currResType) {
				// Build costs: {WOOD, CLAY, ORE, SHEEP, CORN}
				case WOOD:
					if (costs[0] > 0) {
						list.remove(j);
						costs[0]--;
					}
					break;
				case CLAY:
					if (costs[1] > 0) {
						list.remove(j);
						costs[1]--;
					}
					break;
				case ORE:
					if (costs[2] > 0) {
						list.remove(j);
						costs[2]--;
					}
					break;
				case SHEEP:
					if (costs[3] > 0) {
						list.remove(j);
						costs[3]--;
					}
					break;
				case CORN:
					if (costs[4] > 0) {
						list.remove(j);
						costs[4]--;
					}
					break;
				default:
					break;
				}
			}

		}
		playerModels[playerID].setResourceCards(list);
	}

	@Override
	public void setBandit(int x, int y, int playerID) {
		if (gameLogic.checkSetBandit(x, y, playerID)) {
			board.setBandit(board.getFieldAt(x, y));

			viewController.getMainViewController().setBandit(x, y); // Debug
		}

	}

	/**
	 * basic method for switching the player states updates all clients via
	 * networkController
	 *
	 * @param playerID
	 * @param state
	 */
	@Override
	public void setPlayerState(int playerID, PlayerState state) {
		switch (state) {
		case TRADING: // set all other players to offering
			for (int i = 1; i < playerModels.length; i++) {
				if (i == playerID) {
					playerModels[i].setPlayerState(state);
				} else {
					playerModels[i].setPlayerState(PlayerState.OFFERING);
				}
			}
		case PLAYING: // set all other players waiting
			for (int i = 1; i < playerModels.length; i++) {
				if (i == playerID) {
					playerModels[i].setPlayerState(state);
				} else {
					playerModels[i].setPlayerState(PlayerState.WAITING);
				}
			}
		default: // else set only player state of playerID
			playerModels[playerID].setPlayerState(state);
		}

		// DEBUG ONLY!
		// viewController.setPlayerState(playerID);
		/*
		 * for (int i = 1;i < playerModels.length;i++){
		 * networkController.setPlayerState(i,playerModels[i].getPlayerState());
		 * }
		 */

	}

	@Override
	public void setGameState() {
		// TODO Auto-generated method stub

	}

	public PlayerState getPlayerState(int playerID) {
		// TODO Auto-generated method stub
		return null;
	}

	public void diceRollRequest(int playerModelID) {
		// TODO Auto-generated method stub

	}

	public void endTurn(int playerModelID) {
		// TODO Auto-generated method stub

	}

	public boolean checkColor(Color color) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setPlayerColor(int playerID, Color color) {
		// TODO Auto-generated method stub

	}

	public void setPlayerName(int playerID, String name) {
		// TODO Auto-generated method stub

	}

	public void requestSetBandit(int x, int y, int stealFromPlayerID, int playerID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

}
