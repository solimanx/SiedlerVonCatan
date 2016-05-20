package client.controller;

import enums.PlayerState;
import model.PlayerModel;

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
	
	
	public void buildVillage(int x,int y,int dir,PlayerModel player); //request only!!
	public void buildStreet(int x,int y,int dir,PlayerModel player);
	public void buildCity(int x,int y,int dir,PlayerModel player);
	public void setBandit(int x,int y);
    //TODO: Build Village/Street... after Server ok
}
