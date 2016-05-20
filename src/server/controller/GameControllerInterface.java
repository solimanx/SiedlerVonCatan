package server.controller;

import enums.PlayerState;
import model.PlayerModel;

public interface GameControllerInterface {
	/**
	 * inserts Fields with DiceIndex and ResourceType
	 * sets Bandit
	 */
	public void init();
	/**
	 * @param state
	 * sets Player State
	 * notifies GUI 
	 */
	public void setPlayerState(PlayerState state); //player	
	/**
	 * If Player hasLost/hasWon notify GUI...
	 */
	public void setGameState();
	
	public void gainBoardResources(int diceNum);
	
	
	public void buildVillage(int x,int y,int dir,PlayerModel player);
	public void buildStreet(int x,int y,int dir,PlayerModel player);
	public void buildCity(int x,int y,int dir,PlayerModel player);
	public void setBandit(int x,int y);
}
