package server.controller;

import enums.PlayerState;

public interface GameControllerInterface {
	/**
	 * inserts Fields with DiceIndex and ResourceType sets Bandit
	 */
	public void init();

	/**
	 * @param state
	 *            sets Player State notifies GUI
	 */
	public void setPlayerState(int playerId, PlayerState state); // player

	/**
	 * If Player hasLost/hasWon notify GUI...
	 */
	public void setGameState();

	public void gainBoardResources(int diceNum);

	/**
	 * asks the Game Logic if build is allowed and sets a village at the
	 * specified position
	 * 
	 * @param x
	 * @param y
	 * @param dir
	 * @param playerId
	 */
	public void buildVillage(int x, int y, int dir, int playerId);

	/**
	 * asks the Game Logic if build is allowed and sets a street at the
	 * specified position
	 * 
	 * @param x
	 * @param y
	 * @param dir
	 * @param playerId
	 */
	public void buildStreet(int x, int y, int dir, int playerId);

	public void buildCity(int x, int y, int dir, int playerId);

	public void setBandit(int x, int y,int playerId);
}
