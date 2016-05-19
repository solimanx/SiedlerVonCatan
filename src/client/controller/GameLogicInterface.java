package client.controller;


public interface GameLogicInterface {
	public boolean checkBuildVillage(int x, int y, int dir, int player);
	public boolean checkBuildCity(int x, int y, int dir, int player);
	public boolean checkBuildStreet(int x, int y, int dir, int player);
	public boolean checkSetBandit(int x, int y);
	public boolean canTrade();
	public boolean canPlayCard(); 

}
