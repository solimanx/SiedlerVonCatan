package client.controller;

import model.Corner;
import model.Edge;
import model.Field;

public interface GameLogicInterface {
	public boolean checkBuildVillage(Corner c);
	public boolean checkBuildCity(Corner c);
	public boolean checkBuildStreet(Edge e);
	public boolean checkSetBandit(Field f);
	public boolean canTrade();
	public boolean canPlayCard(); 

}
