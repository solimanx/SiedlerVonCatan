package ai.agents;

import ai.AIInputHandler;
import ai.AdvancedAI;
import settings.DefaultSettings;

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

	/**
	 *
	 * @param projekt 0,1,2,3
	 * @return
	 */
	public boolean isBuildableAfterTrade(int projekt){
		int[] costs;
		int[] ownResources = aai.getResourceAgent().getOwnResources();
		int[] resourcesNeeded = new int[5];
		int[] tradableResources = new int[5];
		switch (projekt) {
		case 0: costs = DefaultSettings.STREET_BUILD_COST;
			break;
		case 1:
			costs = DefaultSettings.VILLAGE_BUILD_COST;
			break;
		case 2:
			costs = DefaultSettings.CITY_BUILD_COST;
			break;
		case 3 :
			costs = DefaultSettings.DEVCARD_BUILD_COST;
			break;
		default:
			throw new IllegalStateException("currentBuyingFocus must not be any other number than 0,1,2,3!!!!");
		}
		for(int i = 0; i<5; i++){
			resourcesNeeded[i] = costs[i] - ownResources[i];
			tradableResources[i] = ownResources[i] - costs[i];
		}
		int amountResourcesNeeded = 0;
		for(int i = 0; i<5; i++){
			if(resourcesNeeded[i]>0){
				amountResourcesNeeded = amountResourcesNeeded + resourcesNeeded[i];
			}
		}
		int amountResourcesTradable = 0;
		for(int i = 0; i<5; i++){
			if(tradableResources[i]>0){
				amountResourcesTradable = amountResourcesTradable + tradableResources[i];
			}
		}
		if(amountResourcesNeeded> 2* amountResourcesTradable){
			return false;
		}

			// check if has harbor

			if(tradableResources[0]>2 && amountResourcesNeeded>0){
				if(hasWoodHarbour()){
					for(int i = 0 ; i<5; i++){
						while(tradableResources[0]>1 && amountResourcesNeeded>0 && resourcesNeeded[i]>0){
							tradableResources[0] = tradableResources[0]-2;
							amountResourcesNeeded--;
							amountResourcesTradable = amountResourcesTradable-2;
							boolean loop = true;
							int counter = 0;
							while(loop && counter<5){
								if(resourcesNeeded[counter]>0){
									resourcesNeeded[counter]--;
									loop = false;
								}
								counter++;
							}
						}
					}
				}
			}
			if(tradableResources[1]>2 && amountResourcesNeeded>0){
				if(hasClayHarbour()){
					for(int i = 0 ; i<5; i++){
						while(tradableResources[1]>1 && amountResourcesNeeded>0 && resourcesNeeded[i]>0){
							tradableResources[1] = tradableResources[1]-2;
							amountResourcesNeeded--;
							amountResourcesTradable = amountResourcesTradable-2;
							boolean loop = true;
							int counter = 0;
							while(loop && counter<5){
								if(resourcesNeeded[counter]>0){
									resourcesNeeded[counter]--;
									loop = false;
								}
								counter++;
							}
						}
					}
				}
			}
			if(tradableResources[2]>2 && amountResourcesNeeded>0){
				if(hasOreHarbour()){
					for(int i = 0 ; i<5; i++){
						while(tradableResources[2]>1 && amountResourcesNeeded>0 && resourcesNeeded[i]>0){
							tradableResources[2] = tradableResources[2]-2;
							amountResourcesNeeded--;
							amountResourcesTradable = amountResourcesTradable-2;
							boolean loop = true;
							int counter = 0;
							while(loop && counter<5){
								if(resourcesNeeded[counter]>0){
									resourcesNeeded[counter]--;
									loop = false;
								}
								counter++;
							}
						}
					}
				}
			}
			if(tradableResources[3]>2 && amountResourcesNeeded>0){
				if(hasWoolHarbour()){
					for(int i = 0 ; i<5; i++){
						while(tradableResources[3]>1 && amountResourcesNeeded>0 && resourcesNeeded[i]>0){
							tradableResources[3] = tradableResources[3]-2;
							amountResourcesNeeded--;
							amountResourcesTradable = amountResourcesTradable-2;
							boolean loop = true;
							int counter = 0;
							while(loop && counter<5){
								if(resourcesNeeded[counter]>0){
									resourcesNeeded[counter]--;
									loop = false;
								}
								counter++;
							}
						}
					}
				}
			}
			if(tradableResources[4]>2 && amountResourcesNeeded>0){
				if(hasCornHarbour()){
					for(int i = 0 ; i<5; i++){
						while(tradableResources[4]>1 && amountResourcesNeeded>0 && resourcesNeeded[i]>0){
							tradableResources[4] = tradableResources[4]-2;
							amountResourcesNeeded--;
							amountResourcesTradable = amountResourcesTradable-2;
							boolean loop = true;
							int counter = 0;
							while(loop && counter<5){
								if(resourcesNeeded[counter]>0){
									resourcesNeeded[counter]--;
									loop = false;
								}
								counter++;
							}
						}
					}
				}
			}

			// check if has 3:1

			if(amountResourcesNeeded> 3* amountResourcesTradable){
				return false;
			}

			if(amountResourcesNeeded>0){
				if(hasThreeOneHarbour()){
					for(int i = 0 ; i<5; i++){
						while(amountResourcesNeeded>0 && tradableResources[i]>2 && resourcesNeeded[i] == 0){
							amountResourcesNeeded--;
							amountResourcesTradable = amountResourcesTradable-3;
							tradableResources[i] = tradableResources[i]-3;
							boolean loop = true;
							int counter = 0;
							while(loop && counter<5){
								if(resourcesNeeded[counter]>0){
									resourcesNeeded[counter]--;
									loop = false;
								}
								counter++;
							}
						}
					}
				}
			}

			// 4:1

			if(amountResourcesNeeded> 4* amountResourcesTradable){
				return false;
			}

			if(amountResourcesNeeded>0){

					for(int i = 0 ; i<5; i++){
						while(amountResourcesNeeded>0 && tradableResources[i]>3 && resourcesNeeded[i] == 0){
							amountResourcesNeeded--;
							amountResourcesTradable = amountResourcesTradable-4;
							tradableResources[i] = tradableResources[i]-4;
							boolean loop = true;
							int counter = 0;
							while(loop && counter<5){
								if(resourcesNeeded[counter]>0){
									resourcesNeeded[counter]--;
									loop = false;
								}
								counter++;
							}
						}
					}

			}

			if(amountResourcesNeeded == 0){
				return true;
			} else {
				return false;
			}
	}


	////////////////////
	// PLAYER TRADING //
	////////////////////

	/**
	 *
	 * {WOOD, CLAY, ORE, SHEEP, CORN} amount of the needed resource
	 */
	public void tradeResourcesOffer(){
		int[] costs;
		int[] ownResources = aai.getResourceAgent().getOwnResources();
		int[] resourcesNeeded = new int[5];
		int[] tradableResources = new int[5];
		// currentBuyingFocus : 0 = Street; 1 = Village; 2 = City; 3 = DevCard
		switch (aai.getResourceAgent().getCurrentBuyingFocus()) {
		case 0: costs = DefaultSettings.STREET_BUILD_COST;
			break;
		case 1:
			costs = DefaultSettings.VILLAGE_BUILD_COST;
			break;
		case 2:
			costs = DefaultSettings.CITY_BUILD_COST;
			break;
		case 3 :
			costs = DefaultSettings.DEVCARD_BUILD_COST;
			break;
		default:
			throw new IllegalStateException("currentBuyingFocus must not be any other number than 0,1,2,3!!!!");
		}
		for(int i = 0; i<5; i++){
			resourcesNeeded[i] = costs[i] - ownResources[i];
			tradableResources[i] = ownResources[i] - costs[i];
		}
		int amountResourcesNeedet = 0;
		for(int i = 0; i<5; i++){
			if(resourcesNeeded[i]>0){
				amountResourcesNeedet = amountResourcesNeedet + resourcesNeeded[i];
			}
		}
		int amountResourcesTradable = 0;
		for(int i = 0; i<5; i++){
			if(tradableResources[i]>0){
				amountResourcesTradable = amountResourcesTradable + tradableResources[i];
			}
		}
		int[] demand = {0,0,0,0,0};
		int[] offer = {0,0,0,0,0};
		int demandValue = 0;
		int offerValue = 0;
		for(int i = 0; i<5; i++){
			if(resourcesNeeded[i]>0){
				demand[i] = resourcesNeeded[i];
				demandValue = demandValue + resourcesNeeded[i] * aai.getGlobalResourceWeight()[i];
			}
		}
		while(demandValue-offerValue>50){
			//TODO erstellen des angebots...
		}


	}


}
