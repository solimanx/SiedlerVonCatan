package ai.agents;

import ai.AdvancedAI;

// TODO: Auto-generated Javadoc
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
	
	/**
	 * Instantiates a new trade agent.
	 *
	 * @param aai the aai
	 * @param ra the ra
	 */
	public TradeAgent(AdvancedAI aai, ResourceAgent ra) {
		this.aai = aai;
		this.ra = ra;
		updateAgent();
	}
	
	/**
	 * Calculate hand.
	 *
	 * @param ra the ra
	 */
	protected void calculateHand(ResourceAgent ra){
		//TODO calculates possible hands given resources from resource agent
	}

	/**
	 * Update agent.
	 */
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

	/**
	 * Checks for wood harbour.
	 *
	 * @return true, if successful
	 */
	public boolean hasWoodHarbour() {
		return woodHarbour;
	}

	/**
	 * Checks for clay harbour.
	 *
	 * @return true, if successful
	 */
	public boolean hasClayHarbour() {
		return clayHarbour;
	}

	/**
	 * Checks for wool harbour.
	 *
	 * @return true, if successful
	 */
	public boolean hasWoolHarbour() {
		return woolHarbour;
	}

	/**
	 * Checks for corn harbour.
	 *
	 * @return true, if successful
	 */
	public boolean hasCornHarbour() {
		return cornHarbour;
	}

	/**
	 * Checks for ore harbour.
	 *
	 * @return true, if successful
	 */
	public boolean hasOreHarbour() {
		return oreHarbour;
	}

	/**
	 * Checks for three one harbour.
	 *
	 * @return true, if successful
	 */
	public boolean hasThreeOneHarbour() {
		return threeOneHarbour;
	}
}
