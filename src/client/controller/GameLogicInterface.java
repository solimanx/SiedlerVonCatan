package client.controller;

import model.PlayerModel;

public interface GameLogicInterface {
	public boolean checkBuildVillage(int x, int y, int dir, PlayerModel player);
	public boolean checkBuildCity(int x, int y, int dir, PlayerModel player);
	public boolean checkBuildStreet(int x, int y, int dir, PlayerModel player);
	public boolean checkSetBandit(int x, int y);
	public boolean canTrade();
	public boolean canPlayCard(); 

}
