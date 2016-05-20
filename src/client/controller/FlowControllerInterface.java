package client.controller;

import enums.PlayerState;

public interface FlowControllerInterface {
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
	
	
	public void buildVillage(int x,int y,int dir,int playerId); //request only!!
	public void buildStreet(int x,int y,int dir,int playerId);
	public void buildCity(int x,int y,int dir,int playerId);
	public void setBandit(int x,int y);
    //TODO: Build Village/Street... after Server ok
}
