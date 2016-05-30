package client.controller;

import java.util.ArrayList;

import enums.PlayerState;
import enums.ResourceType;
import model.Board;
import model.Corner;
import model.Edge;
import model.GameLogic;
import model.PlayerModel;
import settings.DefaultSettings;

/**
 * @author NiedlichePixel Controls the game flow.
 */
public class FlowController implements FlowControllerInterface {
	Board board;
	GameLogic gameLogic;
	PlayerModel[] playerModels;
	int ownPlayerId;

	@Override
	public void init(int ownPlayerId, int amountOfPlayers) {
		this.board = Board.getInstance(amountOfPlayers);
		this.gameLogic = new GameLogic(board);
		this.playerModels = board.getPlayerModels();
		this.ownPlayerId = ownPlayerId;
		// TODO Auto-generated method stub

	}

	/**
	 * sets player state in own client model; is called by network controller
	 * after server has changed a player state
	 * 
	 * @param state
	 */
	@Override
	public void setPlayerState(int playerId, PlayerState state) {
		playerModels[playerId].setPlayerState(state);
		if (playerId == ownPlayerId) {
			// update GUI
			// viewController.setPlayerState(state);
		}
	}

	@Override
	public void setGameState() {
		// TODO Auto-generated method stub

	}

	@Override
	public void buildVillage(int x, int y, int dir) {
		if (gameLogic.checkBuildVillage(x, y, dir, ownPlayerId)) {
			// networkController.buildVillage(x, y, dir, ownPlayerId);
		}

	}

	@Override
	public void buildStreet(int x, int y, int dir) {
		if (gameLogic.checkBuildStreet(x, y, dir, ownPlayerId)) {
			// networkController.buildStreet(x, y, dir, ownPlayerId);
		}

	}

	@Override
	public void buildCity(int x, int y, int dir) {
		if (gameLogic.checkBuildCity(x, y, dir, ownPlayerId)) {
			// networkController.buildCity(x, y, dir, ownPlayerId);
		}
	}

	@Override
	public void setBandit(int x, int y) {
		if (gameLogic.checkSetBandit(x, y)) {
			// networkController.setBandit(x,y);
		}

	}

}
