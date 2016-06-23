package ai.agents;

import ai.AdvancedAI;

/**
 * Handles the trading process for the ai.
 */
public class TradeAgent {
	private boolean woodHarbour = false;
	private boolean clayHarbour = false;
	private boolean woolHarbour = false;
	private boolean cornHarbour = false;
	private boolean oreHarbour = false;
	private boolean threeOneHarbour = false;

	private AdvancedAI aai;
	private ResourceAgent ra;
	
	public TradeAgent(AdvancedAI aai, ResourceAgent ra) {
		this.aai = aai;
		this.ra = ra;
		updateAgent();
	}
	protected void calculateHand(ResourceAgent ra){
		//TODO calculates possible hands given resources from resource agent
	}

	protected void updateAgent() {
		if (woodHarbour == false)
			woodHarbour = aai.getGl().hasWoodHarbour(aai.getID());
		if (clayHarbour == false)
			clayHarbour = aai.getGl().hasClayHarbour(aai.getID());
		if (woolHarbour == false)
			woolHarbour = aai.getGl().hasWoolHarbour(aai.getID());
		if (cornHarbour == false)
			cornHarbour = aai.getGl().hasCornHarbour(aai.getID());
		if (oreHarbour == false)
			oreHarbour = aai.getGl().hasOreHarbour(aai.getID());
		if (threeOneHarbour == false)
			threeOneHarbour = aai.getGl().hasThreeOneHarbour(aai.getID());

	}

	public boolean hasWoodHarbour() {
		return woodHarbour;
	}

	public boolean hasClayHarbour() {
		return clayHarbour;
	}

	public boolean hasWoolHarbour() {
		return woolHarbour;
	}

	public boolean hasCornHarbour() {
		return cornHarbour;
	}

	public boolean hasOreHarbour() {
		return oreHarbour;
	}

	public boolean hasThreeOneHarbour() {
		return threeOneHarbour;
	}
}
