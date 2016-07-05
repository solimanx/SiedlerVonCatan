package ai.agents;

import ai.AdvancedAI;

public class TestTrade {

	public static void main(String[] args){
		// Amount of Landscape Resource Cards: {WOOD, CLAY, ORE, SHEEP, CORN}
		//	CITY_BUILD_COST = { 0, 0, 3, 0, 2 };
		TradeAgent ta = new TradeAgent(null, null);
		//ta.setHarbor();
		int[] resources = { 4, 2, 1, 0, 2};
		System.out.println(ta.isBuildableAfterTrade(2, resources));
	}

//	//debug method //TODO
//		public void setHarbor(){
//			woodHarbour = true;
//			clayHarbour = true;
//			woolHarbour = true;
//			cornHarbour = true;
//			oreHarbour = true;
//			threeOneHarbour = true;
//		}

}
