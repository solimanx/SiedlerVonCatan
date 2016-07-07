package ai.agents;

public class TradeOffer {

	private int[] offer;
	private int[] demand;

	public TradeOffer(int[] initOffer, int[] initDemand){
		if(initDemand.length != 5 || initOffer.length != 5){
			throw new IllegalArgumentException("allray length != 5");
		}
		offer = initOffer;
		demand = initDemand;
	}

	public int[] getOffer(){
		return offer;
	}

	public int[] getDemand(){
		return demand;
	}
}
