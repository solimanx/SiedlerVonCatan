package client.controller;

import enums.PlayerState;

public interface GameControllerInterface {
	/**
	 * is called by network controller after Game Field was initialized by Server
	 * creates complete Board
	 */
	public void init();
	/**
	 * @param state
	 * sets Player State
	 * notifies GUI 
	 */
	public void setPlayerState(PlayerState state);	
	/**
	 * If Player hasLost/hasWon notify GUI...
	 */
	public void setGameState();
	
	
	public void buildVillage(); //request only!!
	public void buildStreet();
	public void buildCity();
	public void setBandit();
    //TODO: Build Village/Street... after Server ok
}
