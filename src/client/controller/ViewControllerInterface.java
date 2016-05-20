package client.controller;

public interface ViewControllerInterface {
	
	/**
	 * @param u
	 * @param v
	 * @param ressourceType
	 * @param diceIndex
	 */
	public void setField(int u, int v, enums.ResourceType ressourceType, int diceIndex);
	
	/**
	 * @param u
	 * @param v
	 * @param dir
	 * @param cornerStatus
	 * @param playerID
	 */
	public void setCorner(int u, int v, int dir, enums.CornerStatus cornerStatus, int playerID);
	
	/**
	 * @param u
	 * @param v
	 * @param dir
	 * @param playerID
	 */
	public void setStreet(int u, int v, int dir, int playerID);
	
	/**
	 * @param u
	 * @param v
	 */
	public void setBandid(int u, int v);
	
	
}
