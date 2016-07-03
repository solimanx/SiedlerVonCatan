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
import enums.CornerStatus;
import enums.ResourceType;
import model.HexService;
import model.objects.Corner;
import model.objects.Edge;
import model.objects.Field;
import network.ModelToProtocol;
import network.ProtocolToModel;
import settings.DefaultSettings;

// TODO: Auto-generated Javadoc
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

	private int[] initialResourceWeight = { 0, 0, Integer.parseInt(rb.getString("ORE_INITIAL_BENEFIT")), 0,
			Integer.parseInt(rb.getString("CORN_INITIAL_BENEFIT")) };

	private int initialRoundCounter = 0;

	private Double knightValue;
	private Double monopolyValue;
	private Double inventionValue;
	private Double roadBuildingValue;

	/**
	 * Instantiates a new advanced AI.
	 *
	 * @param serverHost
	 *            the server host
	 * @param port
	 *            the port
	 */
	public AdvancedAI(String serverHost, int port) {
		super(serverHost, port);
		initializeDiceRollProbabilities();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ai.PrimitiveAI#initialVillage()
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see ai.PrimitiveAI#initialRoad()
	 */
	@Override
	public void initialRoad() {
		setResourceWeighting(new int[] { 0, 0, 0, 0, 0 });
		myCornerAgents.get(initialRoundCounter).calculateInitialRoad();
		int[] rC = myCornerAgents.get(initialRoundCounter).getBestRoad();
		super.pO.requestBuildRoad(rC[0], rC[1], rC[2]);

		initialRoundCounter++;
		resourceAgent.initializeResources();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ai.PrimitiveAI#actuate()
	 */
	@Override
	public void actuate() {
		resourceAgent.update();

		receiveProposals();
		if (cardAgent.getSum() > 0) {
			if (cardAgent.getSum() == 1) {
				if (cardAgent.hasKnight()) {
					if (knightValue > 1.0) {
						cardAgent.playKnightCard();
					}
				} else if (cardAgent.hasMonopoly()
						&& (getMe().getVictoryPoints() == 8 || getMe().getVictoryPoints() == 9)) {
					if (monopolyValue > 0.5) {
						cardAgent.playMonopolyCard();
					}
				} else if (cardAgent.hasMonopoly()) {
					if (monopolyValue > 1.5) {
						cardAgent.playMonopolyCard();
					}
				} else if (cardAgent.hasInvention()) {
					if (inventionValue > 0.5) {
						cardAgent.playInventionCard();
					}
				} else if (cardAgent.hasRoad()) {
					if (roadBuildingValue > 0.5) {
						cardAgent.playRoadCard();
					}
				}
			} else {
				double max = Math.max(knightValue,
						Math.max(monopolyValue, Math.max(inventionValue, roadBuildingValue)));
				if (max > 0.5) {
					if (knightValue == max) {
						cardAgent.playKnightCard();
					} else if (monopolyValue == max) {
						cardAgent.playMonopolyCard();
					} else if (inventionValue == max) {
						cardAgent.playInventionCard();
					} else if (roadBuildingValue == max) {
						cardAgent.playRoadCard();
					}
				}
			}

		}

		if (getMe().getAmountCities() != 0 && resourceAgent.canBuildCity()) {
			boolean notFound = true;
			for (int i = 0; i < resourceAgent.getMyCorners().size(); i++) {
				// if it's a village
				if (resourceAgent.getMyCorners().get(i).getStatus().equals(CornerStatus.VILLAGE)) {
					// upgrade it
					int[] coords = ProtocolToModel
							.getCornerCoordinates(resourceAgent.getMyCorners().get(i).getCornerID());
					pO.requestBuildCity(coords[0], coords[1], coords[2]);
					notFound = false;
					break;
				}
			}
			if (notFound) {
				// TODO: Fix this, so other possibilities are checked!
				getOutput().respondEndTurn();
			}
		}
		// if i can get cards
		else if (resourceAgent.canBuyCard()) {
			// i'll get them
			pO.requestBuyCard();
		}

		else if (getMe().getAmountVillages() != 0 && resourceAgent.canBuildVillage()) {
			Corner bestCorner = resourceAgent.getBestVillage();
			if (bestCorner != null) {
				int[] coords = ProtocolToModel.getCornerCoordinates(bestCorner.getCornerID());
				pO.requestBuildVillage(coords[0], coords[1], coords[2]);
			} else { // try to build street
				if (!getMe().hasLongestRoad() && getMe().getAmountStreets() > 0 && resourceAgent.canBuildRoad()) {
					Edge bestEdge = resourceAgent.getBestStreet();
					String id = bestEdge.getEdgeID();
					int[] coords = HexService.getEdgeCoordinates(id.substring(0, 1), id.substring(1, 2));
					pO.requestBuildRoad(coords[0], coords[1], coords[2]);
				}
			}
		}

		// try getting largest army
		else if (!getMe().hasLargestArmy() && cardAgent.hasKnight()) {
			banditAgent.moveRobber();
			int[] coords = banditAgent.bestNewRobber();
			Integer target = banditAgent.getTarget();
			String newRobber = ModelToProtocol.getFieldID(coords[0], coords[1]);
			pO.respondKnightCard(newRobber, target);
		}

		// try getting longest road
		// TODO: This should also happen when the AI HAS the longest road
		else if (!getMe().hasLongestRoad() && getMe().getAmountStreets() > 0 && resourceAgent.canBuildRoad()) {
			Edge bestEdge = resourceAgent.getBestStreet();
			String id = bestEdge.getEdgeID();
			int[] coords = HexService.getEdgeCoordinates(id.substring(0, 1), id.substring(1, 2));
			pO.requestBuildRoad(coords[0], coords[1], coords[2]);
		}
		// if AI can do nothing
		else {
			getOutput().respondEndTurn();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ai.PrimitiveAI#moveRobber()
	 */
	@Override
	protected void moveRobber() {
		banditAgent.moveRobber();
		int[] coords = banditAgent.bestNewRobber();
		Integer target = banditAgent.getTarget();
		String newRobber = ModelToProtocol.getFieldID(coords[0], coords[1]);
		pO.respondMoveRobber(newRobber, target);

	}

	/**
	 * Subtract resources.
	 *
	 * @param cornerAgent
	 *            the corner agent
	 */
	private void subtractResources(CornerAgent cornerAgent) {
		Field[] fields = cornerAgent.getFields();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i] != null) {
				decrementSingleResourceWeight(fields[i].getResourceType(),
						Integer.parseInt(rb.getString("INITIAL_RESOURCE_REDUNDANCY")));
			}
		}
	}

	/**
	 * Initialize dice roll probabilities.
	 */
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

	/**
	 * Gets the dice roll probabilities.
	 *
	 * @return the dice roll probabilities
	 */
	public Map<Integer, Double> getDiceRollProbabilities() {
		return diceRollProbabilities;
	}

	/**
	 * Gets the corner agent by ID.
	 *
	 * @param id
	 *            the id
	 * @return the corner agent by ID
	 */
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

	/**
	 * Gets the resource weighting.
	 *
	 * @return the resource weighting
	 */
	public int[] getResourceWeighting() {
		return initialResourceWeight;
	}

	/**
	 * Sets the resource weighting.
	 *
	 * @param weighting
	 *            the new resource weighting
	 */
	public void setResourceWeighting(int[] weighting) {

	}

	/**
	 * Sets the single resource weight.
	 *
	 * @param resType
	 *            the res type
	 * @param weight
	 *            the weight
	 */
	public void setSingleResourceWeight(ResourceType resType, int weight) {
		initialResourceWeight[DefaultSettings.RESOURCE_VALUES.get(resType)] = weight;
	}

	/**
	 * Gets the single resource weight.
	 *
	 * @param resType
	 *            the res type
	 * @return the single resource weight
	 */
	public int getSingleResourceWeight(ResourceType resType) {
		return initialResourceWeight[DefaultSettings.RESOURCE_VALUES.get(resType)];
	}

	/**
	 * Increment single resource weight.
	 *
	 * @param resType
	 *            the res type
	 * @param change
	 *            the change
	 */
	public void incrementSingleResourceWeight(ResourceType resType, int change) {
		initialResourceWeight[DefaultSettings.RESOURCE_VALUES.get(resType)] += change;
	}

	/**
	 * Decrement single resource weight.
	 *
	 * @param resType
	 *            the res type
	 * @param change
	 *            the change
	 */
	public void decrementSingleResourceWeight(ResourceType resType, int change) {
		if (resType != ResourceType.SEA) {
			initialResourceWeight[DefaultSettings.RESOURCE_VALUES.get(resType)] -= change;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ai.PrimitiveAI#getResourceAgent()
	 */
	@Override
	public ResourceAgent getResourceAgent() {
		return this.resourceAgent;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ai.PrimitiveAI#updateCards()
	 */
	@Override
	public void updateCards() {
		cardAgent.updateCards();
	}

	public OpponentAgent getOpponentAgent() {
		return opponentAgent;
	}

	/**
	 * Calculate value of playing each card if available.
	 */
	public void receiveProposals() {

	}
}
