package model;


public interface GameLogicInterface {
	public boolean checkBuildVillage(int x, int y, int dir, int playerId);
	public boolean checkBuildCity(int x, int y, int dir, int playerId);
	public boolean checkBuildStreet(int x, int y, int dir, int playerId);
	public boolean checkSetBandit(int x, int y);
	public boolean canTrade();
	public boolean canPlayCard(); 

}
