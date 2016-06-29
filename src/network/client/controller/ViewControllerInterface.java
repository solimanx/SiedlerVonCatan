package network.client.controller;

// TODO: Auto-generated Javadoc
public interface ViewControllerInterface {

	/**
	 * Sets the field.
	 *
	 * @param u the u
	 * @param v the v
	 * @param ressourceType the ressource type
	 * @param diceIndex the dice index
	 */
	public void setField(int u, int v, enums.ResourceType ressourceType, int diceIndex);

	/**
	 * Sets the corner.
	 *
	 * @param u the u
	 * @param v the v
	 * @param dir the dir
	 * @param cornerStatus the corner status
	 * @param playerID the player ID
	 */
	public void setCorner(int u, int v, int dir, enums.CornerStatus cornerStatus, int playerID);

	/**
	 * Sets the street.
	 *
	 * @param u the u
	 * @param v the v
	 * @param dir the dir
	 * @param playerID the player ID
	 */
	public void setStreet(int u, int v, int dir, int playerID);

	/**
	 * Sets the bandit.
	 *
	 * @param u the u
	 * @param v the v
	 */
	public void setBandit(int u, int v);

}
