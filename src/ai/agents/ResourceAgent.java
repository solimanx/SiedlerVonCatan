package ai.agents;

import java.util.ResourceBundle;

import enums.ResourceType;
import settings.DefaultSettings;

/**
 * Tracks own resources and sees what AI can purchase at any given time.
 */
public class ResourceAgent {
	ResourceBundle rb;
	int[] resourceWeighting;
	
	//{WOOD, CLAY, ORE, SHEEP, CORN}	
	public ResourceAgent(){
		this.rb = ResourceBundle.getBundle("ai.bundle.AIProperties");
		resourceWeighting = new int[]{0,0,Integer.parseInt(rb.getString("ORE_INITIAL_BENEFIT")),0,Integer.parseInt(rb.getString("CORN_INITIAL_BENEFIT"))};
	}
	
	public int[] getResourceWeighting(){
		return resourceWeighting;
	}
	
	public void setResourceWeighting(int[] weighting){
		
	}
	
	public void setSingleResourceWeight(ResourceType resType,int weight){
		resourceWeighting[DefaultSettings.RESOURCE_VALUES.get(resType)] = weight;
	}
	
	public int getSingleResourceWeight(ResourceType resType){
		return resourceWeighting[DefaultSettings.RESOURCE_VALUES.get(resType)];
	}

	public void incrementSingleResourceWeight(ResourceType resType,int change){
		resourceWeighting[DefaultSettings.RESOURCE_VALUES.get(resType)] += change;
	}
	
	public void decrementSingleResourceWeight(ResourceType resType,int change){
		resourceWeighting[DefaultSettings.RESOURCE_VALUES.get(resType)] -= change;
	}
}
