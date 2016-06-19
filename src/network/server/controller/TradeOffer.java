package network.server.controller;

import java.util.ArrayList;

public class TradeOffer {

	private int ownerID;
	private int tradingID;
	private int[] supply;
	private int[] demand;
	public ArrayList<Integer> acceptingPlayers = new ArrayList<Integer>();
	public ArrayList<Integer> decliningPlayers = new ArrayList<Integer>();

	public TradeOffer(int ownerID, int tradingID, int[] supply, int[] demand) {
		this.ownerID = ownerID;
		this.tradingID = tradingID;
		this.supply = supply;
		this.demand = demand;
	}

	protected int getOwnerID() {
		return ownerID;
	}

	protected int[] getSupply() {
		return supply;
	}

	protected int[] getDemand() {
		return demand;
	}

	public int getTradingID() {
		return tradingID;
	}

}
