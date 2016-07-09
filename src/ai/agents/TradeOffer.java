package ai.agents;

// TODO: Auto-generated Javadoc
public class TradeOffer {

	private int[] offer;
	private int[] demand;

	/**
	 * Instantiates a new trade offer.
	 *
	 * @param initOffer the init offer
	 * @param initDemand the init demand
	 */
	public TradeOffer(int[] initOffer, int[] initDemand){
		if(initDemand.length != 5 || initOffer.length != 5){
			throw new IllegalArgumentException("allray length != 5");
		}
		offer = initOffer;
		demand = initDemand;
	}

	/**
	 * Gets the offer.
	 *
	 * @return the offer
	 */
	public int[] getOffer(){
		return offer;
	}

	/**
	 * Gets the demand.
	 *
	 * @return the demand
	 */
	public int[] getDemand(){
		return demand;
	}
}
