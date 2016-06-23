package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import ai.agents.CornerAgent;
import enums.ResourceType;
import model.objects.Field;
import settings.DefaultSettings;

/**
 * Communication between other agents and input/output from/to server. The brain
 * behind the AI.
 *
 */
public class AdvancedAI extends PrimitiveAI {
	private ResourceBundle rb = ResourceBundle.getBundle("ai.bundle.AIProperties");

	private CornerAgent[] cA = new CornerAgent[Integer.parseInt(rb.getString("CORNER_AGENTS"))];
	private Map<Integer, Double> diceRollProbabilities;

	// belongs to resourceAgent
	private ArrayList<CornerAgent> myCorners = new ArrayList<CornerAgent>();
	
	int[] resourceWeighting;
	
	int initialRoundCounter = 0;

	public AdvancedAI() {
		resourceWeighting = new int[]{0,0,Integer.parseInt(rb.getString("ORE_INITIAL_BENEFIT")),0,Integer.parseInt(rb.getString("CORN_INITIAL_BENEFIT"))};
		initializeDiceRollProbabilities();
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

	@Override
	public void initialVillage() {
		int c = 0;
		int radius = DefaultSettings.BOARD_RADIUS;
		for (int i = -radius; i <= radius; i++) {
			for (int j = -radius; j <= radius; j++) {
				for (int k = 0; k < 2; k++) {
					if (getGl().getBoard().getCornerAt(j, i, k) != null) {
						cA[c] = new CornerAgent(new int[] { j, i, k }, getGl().getBoard(), this);
						c++;

					}
				}
			}
		}
		int x = 0;
		int y = 0;
		int z = 0;

		int bestUtility = 0;
		int d = -1;
		for (int i = 0; i < cA.length; i++) {
			if (cA[i].calculateInitialVillageUtility() > bestUtility) {
				bestUtility = cA[i].calculateInitialVillageUtility();
				x = cA[i].getLocation()[0];
				y = cA[i].getLocation()[1];
				z = cA[i].getLocation()[2];
				d = i;
			}
			System.out.println(cA[i].getLocationString() + " " + cA[i].calculateInitialVillageUtility());
		}
		initialRoundCounter = 1;
		myCorners.add(cA[d]);
		super.pO.requestBuildInitialVillage(x, y, z);
		if (initialRoundCounter== 1){
			subtractResources(myCorners.get(0));
		}
	}

	private void subtractResources(CornerAgent cornerAgent) {
		Field[] fields = cornerAgent.getFields();
		for (int i = 0;i < fields.length;i++){
			if (fields[i] != null){
				decrementSingleResourceWeight(fields[i].getResourceType(), Integer.parseInt(rb.getString("INITIAL_RESOURCE_REDUNDANCY")));
			}
		}		
	}


	@Override
	public void initialRoad() {
		myCorners.get(0).calculateInitialRoadOne();
		int[] rC = myCorners.get(0).getBestRoad();
		super.pO.requestBuildInitialRoad(rC[0], rC[1], rC[2]);

	}

	private void initializeDiceRollProbabilities() {
		diceRollProbabilities = new HashMap<Integer, Double>();
		diceRollProbabilities.put(2, Double.parseDouble(rb.getString("probability_two")));
		diceRollProbabilities.put(3, Double.parseDouble(rb.getString("probability_three")));
		diceRollProbabilities.put(4, Double.parseDouble(rb.getString("probability_four")));
		diceRollProbabilities.put(5, Double.parseDouble(rb.getString("probability_five")));
		diceRollProbabilities.put(6, Double.parseDouble(rb.getString("probability_six")));
		diceRollProbabilities.put(7, Double.parseDouble(rb.getString("probability_seven")));
		diceRollProbabilities.put(8, Double.parseDouble(rb.getString("probability_eight")));
		diceRollProbabilities.put(9, Double.parseDouble(rb.getString("probability_nine")));
		diceRollProbabilities.put(10, Double.parseDouble(rb.getString("probability_ten")));
		diceRollProbabilities.put(11, Double.parseDouble(rb.getString("probability_eleven")));
		diceRollProbabilities.put(12, Double.parseDouble(rb.getString("probability_twelve")));

	}

	public Map<Integer, Double> getDiceRollProbabilities() {
		return diceRollProbabilities;
	}

	public CornerAgent getCornerAgentByID(String id) {
		if (id.length() != 3 ) {
			throw new IllegalArgumentException("id unequal 3");
		}
		for (int i = 0; i < cA.length; i++) {
			String a = id.substring(0, 1);
			String b = id.substring(1, 2);
			String c = id.substring(2, 3);
			if ((a + b + c).equals(cA[i].getID()) || (a + c + b).equals(cA[i].getID())
					|| (b + a + c).equals((cA[i].getID())) || (b + c + a).equals((cA[i].getID()))
					|| (c + a + b).equals((cA[i].getID())) || (c + b + a).equals((cA[i].getID()))) {
				return cA[i];
			}
		}
		throw new IllegalArgumentException(id + " doesn't exist");

	}
}
