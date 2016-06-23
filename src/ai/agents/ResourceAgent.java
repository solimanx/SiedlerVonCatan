package ai.agents;

import java.util.ResourceBundle;

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
	
	

}
