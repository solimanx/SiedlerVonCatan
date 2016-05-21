package client.controller;

import enums.PlayerState;

public interface FlowControllerInterface {
	/**
	 * is called by network controller after Game Field was initialized by Server
	 * creates complete Board
	 */
	public void init(int ownPlayerId,int amountOfPlayers);
	/**
	 * @param state
	 * sets Player State
	 * notifies GUI 
	 */
	public void setPlayerState(int playerId, PlayerState state);	
	/**
	 * If Player hasLost/hasWon notify GUI...
	 */
	public void setGameState();
	
	
	public void buildVillage(int x,int y,int dir); //request only!!
	public void buildStreet(int x,int y,int dir);
	public void buildCity(int x,int y,int dir);
	public void setBandit(int x,int y);
    //TODO: Build Village/Street... after Server ok
}
