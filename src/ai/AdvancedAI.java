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
	private ResourceBundle rb = ResourceBundle.getBundle("ai.bundle.AIProperties.properties");

	private CornerAgent[] cA = new CornerAgent[Integer.parseInt(rb.getString("cornerAgents"))];
	public Map<Integer, Double> diceRollProbabilities = new HashMap<Integer, Double>();

	private void initializeDiceRollProbabilities() {
		diceRollProbabilities.put(2, Double.parseDouble(rb.getString("probabilities_two")));
		diceRollProbabilities.put(3, Double.parseDouble(rb.getString("probabilities_three")));
		diceRollProbabilities.put(4, Double.parseDouble(rb.getString("probabilities_four")));
		diceRollProbabilities.put(5, Double.parseDouble(rb.getString("probabilities_five")));
		diceRollProbabilities.put(6, Double.parseDouble(rb.getString("probabilities_six")));
		diceRollProbabilities.put(7, Double.parseDouble(rb.getString("probabilities_seven")));
		diceRollProbabilities.put(8, Double.parseDouble(rb.getString("probabilities_eight")));
		diceRollProbabilities.put(9, Double.parseDouble(rb.getString("probabilities_nine")));
		diceRollProbabilities.put(10, Double.parseDouble(rb.getString("probabilities_ten")));
		diceRollProbabilities.put(11, Double.parseDouble(rb.getString("probabilities_eleven")));
		diceRollProbabilities.put(12, Double.parseDouble(rb.getString("probabilities_twelve")));

	}
}
