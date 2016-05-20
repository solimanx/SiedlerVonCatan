package client.controller;

import java.util.ArrayList;

import enums.PlayerState;
import enums.ResourceType;
import model.Board;
import model.Corner;
import model.Edge;
import model.PlayerModel;
import settings.DefaultSettings;

/**
 * @author NiedlichePixel
 * Controls the game flow.
 */
public class FlowController implements FlowControllerInterface {
	Board board;
	GameLogic gameLogic;
	PlayerModel[] playerModels;
	PlayerState playerState;

	@Override
	public void init() {
		this.board = Board.getInstance();
		this.gameLogic = new GameLogic(board);
		this.playerModels = board.getPlayerModels();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlayerState(PlayerState state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGameState() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildVillage(int x, int y,int dir, int playerId) {
		if (gameLogic.checkBuildVillage(x, y, dir, playerId)){
			//networkController.buildVillage(x, y, dir, player);
		}
		
	}

	@Override
	public void buildStreet(int x, int y,int dir,int playerId) {
		if (gameLogic.checkBuildStreet(x, y, dir, playerId)){
			//networkController.buildStreet(x, y, dir, player);
		}		
		
	}

	@Override
	public void buildCity(int x, int y,int dir,int playerId) {
		if (gameLogic.checkBuildCity(x, y, dir, playerId)){
			//networkController.buildCity(x, y, dir, player);
		}
	}

	@Override
	public void setBandit(int x,int y) {
		if (gameLogic.checkSetBandit(x, y)){
			//networkController.setBandit(x,y);
		}
		
	}


}
