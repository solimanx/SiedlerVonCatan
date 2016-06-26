package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import ai.agents.BanditAgent;
import ai.agents.CardAgent;
import ai.agents.CornerAgent;
import ai.agents.OpponentAgent;
import ai.agents.ResourceAgent;
import ai.agents.TradeAgent;
import enums.ResourceType;
import model.objects.Field;
import network.ModelToProtocol;
import settings.DefaultSettings;

/**
 * Communication between other agents and input/output from/to server. The brain
 * behind the AI.
 *
 */
public class AdvancedAI extends PrimitiveAI {
	private ResourceBundle rb = ResourceBundle.getBundle("ai.bundle.AIProperties");

	private CornerAgent[] cornerAgent = new CornerAgent[Integer.parseInt(rb.getString("CORNER_AGENTS"))];
	private CardAgent cardAgent = new CardAgent(this);
	private ResourceAgent resourceAgent = new ResourceAgent(this);
	private OpponentAgent opponentAgent = new OpponentAgent(this);
	private TradeAgent tradeAgent = new TradeAgent(this, resourceAgent);
	private BanditAgent banditAgent = new BanditAgent(this, opponentAgent);

	private ArrayList<CornerAgent> myCornerAgents = new ArrayList<CornerAgent>();

	private Map<Integer, Double> diceRollProbabilities;

	int[] initialResourceWeight;

	int initialRoundCounter = 0;

	public AdvancedAI(String serverHost, int port) {
		super(serverHost, port);
		initialResourceWeight = new int[] { 0, 0, Integer.parseInt(rb.getString("ORE_INITIAL_BENEFIT")), 0,
				Integer.parseInt(rb.getString("CORN_INITIAL_BENEFIT")) };
		initializeDiceRollProbabilities();
	}

	@Override
	public void initialVillage() {
		if (initialRoundCounter == 1) {
			subtractResources(myCornerAgents.get(0));
		}
		int c = 0;
		int radius = DefaultSettings.BOARD_RADIUS;
		for (int i = -radius; i <= radius; i++) {
			for (int j = -radius; j <= radius; j++) {
				for (int k = 0; k < 2; k++) {
					if (getGl().getBoard().getCornerAt(j, i, k) != null) {
						cornerAgent[c] = new CornerAgent(new int[] { j, i, k }, getGl().getBoard(), this);
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
		for (int i = 0; i < cornerAgent.length; i++) {
			if (cornerAgent[i].calculateInitialVillageUtility() > bestUtility) {
				bestUtility = cornerAgent[i].calculateInitialVillageUtility();
				x = cornerAgent[i].getLocation()[0];
				y = cornerAgent[i].getLocation()[1];
				z = cornerAgent[i].getLocation()[2];
				d = i;
			}
			System.out.println(
					cornerAgent[i].getLocationString() + " " + cornerAgent[i].calculateInitialVillageUtility());
		}
		myCornerAgents.add(cornerAgent[d]);
		super.pO.requestBuildVillage(x, y, z);

	}

	@Override
	public void initialRoad() {
		setResourceWeighting(new int[] { 0, 0, 0, 0, 0 });
		myCornerAgents.get(initialRoundCounter).calculateInitialRoad();
		int[] rC = myCornerAgents.get(initialRoundCounter).getBestRoad();
		super.pO.requestBuildRoad(rC[0], rC[1], rC[2]);

		initialRoundCounter++;

	}

	@Override
	protected void moveRobber() {
		banditAgent.moveRobber();
		int[] coords = banditAgent.bestNewRobber();
		Integer target = banditAgent.getTarget();
		String newRobber = ModelToProtocol.getFieldID(coords[0], coords[1]);
		pO.respondMoveRobber(newRobber, target);

	}

	private void subtractResources(CornerAgent cornerAgent) {
		Field[] fields = cornerAgent.getFields();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i] != null) {
				decrementSingleResourceWeight(fields[i].getResourceType(),
						Integer.parseInt(rb.getString("INITIAL_RESOURCE_REDUNDANCY")));
			}
		}
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
		if (id.length() != 3) {
			throw new IllegalArgumentException("id unequal 3");
		}
		for (int i = 0; i < cornerAgent.length; i++) {
			String a = id.substring(0, 1);
			String b = id.substring(1, 2);
			String c = id.substring(2, 3);
			if ((a + b + c).equals(cornerAgent[i].getID()) || (a + c + b).equals(cornerAgent[i].getID())
					|| (b + a + c).equals((cornerAgent[i].getID())) || (b + c + a).equals((cornerAgent[i].getID()))
					|| (c + a + b).equals((cornerAgent[i].getID())) || (c + b + a).equals((cornerAgent[i].getID()))) {
				return cornerAgent[i];
			}
		}
		throw new IllegalArgumentException(id + " doesn't exist");

	}

	public int[] getResourceWeighting() {
		return initialResourceWeight;
	}

	public void setResourceWeighting(int[] weighting) {

	}

	public void setSingleResourceWeight(ResourceType resType, int weight) {
		initialResourceWeight[DefaultSettings.RESOURCE_VALUES.get(resType)] = weight;
	}

	public int getSingleResourceWeight(ResourceType resType) {
		return initialResourceWeight[DefaultSettings.RESOURCE_VALUES.get(resType)];
	}

	public void incrementSingleResourceWeight(ResourceType resType, int change) {
		initialResourceWeight[DefaultSettings.RESOURCE_VALUES.get(resType)] += change;
	}

	public void decrementSingleResourceWeight(ResourceType resType, int change) {
		initialResourceWeight[DefaultSettings.RESOURCE_VALUES.get(resType)] -= change;
	}
	
	@Override
	public ResourceAgent getResourceAgent() {
		return this.resourceAgent;

	}
}
