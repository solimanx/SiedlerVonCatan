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
public class FlowController implements GameControllerInterface {
	Board board;
	GameLogic gameLogic;
	PlayerModel[] playerModels;

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
	public void buildVillage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildStreet() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildCity() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBandit() {
		// TODO Auto-generated method stub
		
	}


}
