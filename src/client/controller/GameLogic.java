package client.controller;

import java.util.ArrayList;

import model.Board;
import model.Corner;
import model.Edge;
import model.Field;
import model.PlayerModel;
import enums.ResourceType;

/**
 * @author 
 * Implements rules of the game
 */
public class GameLogic {
Board board;
PlayerModel playerModels;
private Field[][] fields;
private Corner[][][] corners;
private Edge[][][] edges;


    public GameLogic(Board b){
    	this.board = b;
    	this.playerModels = board.getPlayerModels();
        this.fields = board.getFields();
        this.corners = board.getCorners();
        this.edges = board.getEdges();
    }
	
	/**
	 * tries to build a settlement on the given position with the given player. 
	 * if there is already a village 
	 * @param x
	 * @param y
	 * @param dir
	 * @param player
	 * @return
	 */
	public boolean buildSettlement(int x, int y,char dir,int player){
		if (playerModels.get(player).amountVillages > 0){ //has this Player a Village left to build?
			enums.CornerStatus status = corners[x][y][dir].getStatus(); 
			switch(status){
			case EMPTY:
				return buildVillage(x,y,dir,player);
			case VILLAGE:
				return buildCity(x,y,dir,player);
			default: //CITY or BLOCKED; do nothing
				return false;
			}
		} else {
			return false;
		}
		
	}

	private boolean buildVillage(int x, int y, char dir, int player) {
		int[] resources = getPlayerResources(player);
		for (int i = 0;i < 5;i++){
			if (resources[i] < settings.DefaultSettings.VILLAGE_BUILD_COST[i]){
				return false;
			}
		}
		corners[x][y].dir.setStatus(enums.CornerStatus.VILLAGE);
		corners[x][y].dir.setPlayer(player);
		playerModels.get(player).decreaseVillageAmount();
		//view.setCornerBuilding(x,y,dir,enums.CornerStatus.VILLAGE,player);
		return true;
	}

	private boolean buildCity(int x, int y, char dir, int player) {
		int[] resources = getPlayerResources(player);
		for (int i = 0;i < 5;i++){
			if (resources[i] < settings.DefaultSettings.CITY_BUILD_COST[i]){
				return false;
			}
		}
		corners[x][y].dir.setStatus(enums.CornerStatus.CITY);
		corners[x][y].dir.setPlayer(player);
		playerModels.get(player).decreaseCityAmount();
		//view.setCornerBuilding(x,y,dir,enums.CornerStatus.CITY,player);
		return true;
	}
	/**
	 * Checks if the player can build this Street and if the Position of the Street is Valid
	 * @param x
	 * @param y
	 * @param dir
	 * @param player
	 * @return boolean successful
	 */
	public boolean buildStreet(int x, int y, char dir, int player){
		if (playerModels.get(player).amountStreets > 0){ //has this Player a Street left to build?
			if (edges[x][y].dir.hasStreet == false){
				int[] resources = getPlayerResources(player);
				for (int i = 0;i < 5;i++){
					if (resources[i] < settings.DefaultSettings.STREET_BUILD_COST[i]){
						return false;
					}
				}
				//Edge Get Neighbor Corners; check if village/city there
				// + edge get adjusting edges; check if there is any street of this player
				edges[x][y].dir.createStreet(player);
				playerModels.get(player).decreaseStreetAmount();
				//view.setStreet(x,y,dir,player);
				return true;
			}
		}
			return false;
	}
	/**
	 * Gets an array wich contains the player resources in form of (AmountWood,AmountClay...)
	 * @param player
	 * @return resource Array
	 */
	private int[] getPlayerResources(int player){
		ArrayList<ResourceType> resList = playerModels.get(player).getResourceCards();
		int[] result = new int[5];
		enums.ResourceType resType;
		for (int i = 0;i<resList.size();i++){
			resType = resList.get(i);
			switch(resType){
			// Build costs: {WOOD, CLAY, ORE, SHEEP, CORN}
			case WOOD:
				result[0]++;
			case CLAY:
				result[1]++;
			case ORE:
				result[2]++;
			case SHEEP:
				result[3]++;
			case CORN:
				result[4]++;
			default:	
			}
		}
		return result;
	}
	
	public void setBandit(int x,int y){
		board.setBandit(fields[x][y]);
	}
	private void addToPlayersResources(int player,enums.ResourceType resType,int amount){
		for (int i = 0;i<amount;i++){
		   playerModels.get(player).addResource(resType);
		}
	}
	

}
