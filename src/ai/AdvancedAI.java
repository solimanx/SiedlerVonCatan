package ai;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import ai.agents.CornerAgent;

/**
 * Communication between other agents and input/output from/to server. The brain
 * behind the AI.
 *
 */
public class AdvancedAI extends PrimitiveAI {
	private ResourceBundle rb = ResourceBundle.getBundle("agents.properties.AIProperties");

	private CornerAgent[] cA = new CornerAgent[Integer.parseInt(rb.getString("cornerAgents"))];
	private Map<Integer, Double> DiceRollProbabilities = new HashMap<Integer, Double>();

	private void initializeDiceRollProbabilities() {
		DiceRollProbabilities.put(2, Double.parseDouble(rb.getString("probabilities_two")));
		DiceRollProbabilities.put(3, Double.parseDouble(rb.getString("probabilities_three")));
		DiceRollProbabilities.put(4, Double.parseDouble(rb.getString("probabilities_four")));
		DiceRollProbabilities.put(5, Double.parseDouble(rb.getString("probabilities_five")));
		DiceRollProbabilities.put(6, Double.parseDouble(rb.getString("probabilities_six")));
		DiceRollProbabilities.put(7, Double.parseDouble(rb.getString("probabilities_seven")));
		DiceRollProbabilities.put(8, Double.parseDouble(rb.getString("probabilities_eight")));
		DiceRollProbabilities.put(9, Double.parseDouble(rb.getString("probabilities_nine")));
		DiceRollProbabilities.put(10, Double.parseDouble(rb.getString("probabilities_ten")));
		DiceRollProbabilities.put(11, Double.parseDouble(rb.getString("probabilities_eleven")));
		DiceRollProbabilities.put(12, Double.parseDouble(rb.getString("probabilities_twelve")));

	}
}
