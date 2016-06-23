package ai;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import ai.agents.CornerAgent;
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

	public AdvancedAI() {

	}

	@Override
	public void initialVillage() {
		initializeDiceRollProbabilities();
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
		for (int i = 0; i < cA.length; i++) {
			if (cA[i].calculateInitialVillageUtility() > bestUtility) {
				bestUtility = cA[i].calculateInitialVillageUtility();
				x = cA[i].getLocation()[0];
				y = cA[i].getLocation()[1];
				z = cA[i].getLocation()[2];
			}
			System.out.println(cA[i].getLocationString() + " " + cA[i].calculateInitialVillageUtility());
		}
		super.pO.requestBuildInitialVillage(x, y, z);
	}

	@Override
	public void initialRoad() {
	};

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

	public CornerAgent getCornerAgentByID(String id){
		for(int i=0; i<cA.length;i++){
			if(id.equals(cA[i].getID())){
				return cA[i];
			}
		}
		throw new IllegalArgumentException(id+" doesn't exist");

	}
}
