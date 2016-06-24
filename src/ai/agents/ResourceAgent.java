package ai.agents;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import ai.AdvancedAI;
import enums.ResourceType;
import settings.DefaultSettings;

/**
 * Tracks own resources and sees what AI can purchase at any given time.
 */
public class ResourceAgent {
	private int[] ownResources = {0,0,0,0,0};
	private AdvancedAI aai;
	private int currentBuyingFocus; //0 = Street; 1 = Village; 2 = City; 3 = DevCard
	private Map<Integer,Integer[]> buyingResources = new HashMap<Integer,Integer[]>();
	/*private boolean canBuildStreet;
	private boolean canBuildVillage;
	private boolean canBuildCity; */
	//                            {THREE_TO_ONE, WOOD, CLAY, ORE, SHEEP, CORN}
	private boolean[] harbours = {false, false, false, false, false, false};
	
	public ResourceAgent(AdvancedAI ai){
		this.aai = ai;
	}
	
	public int[] getRobberLossCards(){
		int size = sumOwnResources()/2;
		int[] result = new int[5];
		//if (aai.getCurrentTactic().equals("CORN_ORE_STRATEGY")){
			for (int i = 0; i < ownResources[0]+ownResources[1];i++){
				if (size == 0){
					break;
				} else {
					if (ownResources[0] > 0){
						ownResources[0]--;
						result[0]++;
					} else {
						ownResources[1]--;
						result[1]++;
					}
				}
			}
			if (size == 0){
				return result;
			} else {
				for (int i = 0;i < sumOwnResources();i++){
					if (size == 0){
						break;
					} else {
						if (ownResources[2] > 0){
							ownResources[2]--;
							result[2]++;
						} else if (ownResources[3] > 0){
							ownResources[3]--;
							result[3]++;
						} else {
							ownResources[4]--;
							result[4]++;							
						}
					}					
				}
				return result;
			}
		/*} else {
			
		}*/
	}
	
	public int sumOwnResources(){
		int result = 0;
		for (int i = 0;i < ownResources.length;i++){
			result += ownResources[i];
		}
		return result;		
	}

    /**
     * gets possible buys
     * @return boolean list (Street,Village,City,DevCard)
     */
    public boolean[] getPossibleBuildings(){
    	boolean[] results = new boolean[4];
    	if (compareResources(ownResources,DefaultSettings.STREET_BUILD_COST)){
    		results[1] = true;
    	}
    	if (compareResources(ownResources,DefaultSettings.VILLAGE_BUILD_COST)){
    		results[2] = true;
    	}    	
    	if (compareResources(ownResources,DefaultSettings.CITY_BUILD_COST)){
    		results[3] = true;
    	}
    	if (compareResources(ownResources,DefaultSettings.DEVCARD_BUILD_COST)){
    		results[4] = true;
    	}
    	return results;
    }

	public static boolean compareResources(int[] playerResources, int[] resource) {
		for (int i = 0; i < playerResources.length;i++){
			if (playerResources[i] < resource[i]){
				return false;
			}
		}
		return true;
	}
    
}
