package ai.agents;

import ai.AdvancedAI;

public class TestTrade {

	public static void main(String[] args){
		// Amount of Landscape Resource Cards: {WOOD, CLAY, ORE, SHEEP, CORN}
		//	CITY_BUILD_COST = { 0, 0, 3, 0, 2 };
		TradeAgent ta = new TradeAgent(null, null);
		int[] resources = { 4, 0, 2, 0, 2};
		System.out.println(ta.isBuildableAfterTrade(2, resources));
	}


}
