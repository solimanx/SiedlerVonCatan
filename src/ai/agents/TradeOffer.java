package ai.agents;

public class TradeOffer {

	int[] offer;
	int[] demand;

	public TradeOffer(int[] initOffer, int[] initDemand){
		if(initDemand.length != 5 || initOffer.length != 5){
			throw new IllegalArgumentException("allray length != 5");
		}
		offer = initOffer;
		demand = initDemand;
	}
}
