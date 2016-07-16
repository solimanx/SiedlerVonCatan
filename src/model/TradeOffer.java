package model;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
public class TradeOffer {

	private int ownerID;
	private int tradingID;
	private int[] supply;
	private int[] demand;
	private ArrayList<Integer> acceptingPlayers = new ArrayList<Integer>();
	private ArrayList<Integer> decliningPlayers = new ArrayList<Integer>();

	/**
	 * Instantiates a new trade offer.
	 *
	 * @param ownerID
	 *            the owner ID
	 * @param tradingID
	 *            the trading ID
	 * @param supply
	 *            the supply
	 * @param demand
	 *            the demand
	 */
	public TradeOffer(int ownerID, int tradingID, int[] supply, int[] demand) {
		this.ownerID = ownerID;
		this.tradingID = tradingID;
		this.supply = supply;
		this.demand = demand;
	}

	/**
	 * Instantiates a new trade offer.
	 *
	 * @param initOffer
	 *            the init offer
	 * @param initDemand
	 *            the init demand
	 */
	public TradeOffer(int[] initSupply, int[] initDemand) {
		if (initDemand.length != 5 || initSupply.length != 5) {
			throw new IllegalArgumentException("allray length != 5");
		} else {
			supply = initSupply;
			demand = initDemand;
		}
	}

	/**
	 * Gets the owner ID.
	 *
	 * @return the owner ID
	 */
	public int getOwnerID() {
		return ownerID;
	}

	/**
	 * Gets the supply.
	 *
	 * @return the supply
	 */
	public int[] getSupply() {
		return supply;
	}

	/**
	 * Gets the demand.
	 *
	 * @return the demand
	 */
	public int[] getDemand() {
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

	public ArrayList<Integer> getAcceptingPlayers() {
		return acceptingPlayers;
	}
	
	public ArrayList<Integer> getDecliningPlayers() {
		return decliningPlayers;
	}

}
