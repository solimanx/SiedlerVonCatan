package server.controller;

import enums.PlayerState;

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
	
	public void gainBoardResources();
	
	
	public void buildVillage(); //request only!!
	public void buildStreet();
	public void buildCity();
	public void setBandit();
}
