package network.client.controller;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
public class TradeOffer {

	private int ownerID;
	private int tradingID;
	private int[] supply;
	private int[] demand;
	public ArrayList<Integer> acceptingPlayers = new ArrayList<Integer>();

	/**
	 * Instantiates a new trade offer.
	 *
	 * @param ownerID the owner ID
	 * @param tradingID the trading ID
	 * @param supply the supply
	 * @param demand the demand
	 */
	public TradeOffer(int ownerID, int tradingID, int[] supply, int[] demand) {
		this.ownerID = ownerID;
		this.tradingID = tradingID;
		this.supply = supply;
		this.demand = demand;
	}

	/**
	 * Gets the owner ID.
	 *
	 * @return the owner ID
	 */
	protected int getOwnerID() {
		return ownerID;
	}

	/**
	 * Gets the supply.
	 *
	 * @return the supply
	 */
	protected int[] getSupply() {
		return supply;
	}

	/**
	 * Gets the demand.
	 *
	 * @return the demand
	 */
	protected int[] getDemand() {
		return demand;
	}

	/**
	 * Gets the trading ID.
	 *
	 * @return the trading ID
	 */
	public int getTradingID() {
		return tradingID;
	}

}
