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
import model.objects.Edge;
import model.objects.Field;
import network.ModelToProtocol;
import network.ProtocolToModel;
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

	int[] initialResourceWeight = { 0, 0, Integer.parseInt(rb.getString("ORE_INITIAL_BENEFIT")), 0,
			Integer.parseInt(rb.getString("CORN_INITIAL_BENEFIT")) };

	int initialRoundCounter = 0;

	public AdvancedAI(String serverHost, int port) {
		super(serverHost, port);
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
		resourceAgent.initializeResources();

	}

	@Override
	public void actuate() {
		resourceAgent.update();

		if (getMe().getAmountCities() != 0) {
			while (resourceAgent.canBuildCity()) {
				for (int i = 0; i < resourceAgent.getMyCorners().size(); i++) {
					// if it's a village
					if (resourceAgent.getMyCorners().get(i).getStatus().equals(CornerStatus.VILLAGE)) {
						// upgrade it
						int[] coords = ProtocolToModel
								.getCornerCoordinates(resourceAgent.getMyCorners().get(i).getCornerID());
						pO.requestBuildCity(coords[0], coords[1], coords[2]);
						resourceAgent.update();
					}
				}
			}
		}

		// if i can get cards
		else if (resourceAgent.canBuyCard()) {
			// i'll get them
			pO.requestBuyCard();
			resourceAgent.update();
		}
		// try getting largest army
		if (!getMe().hasLargestArmy()) {
			if (cardAgent.hasKnight()) {
				banditAgent.moveRobber();
				int[] coords = banditAgent.bestNewRobber();
				Integer target = banditAgent.getTarget();
				String newRobber = ModelToProtocol.getFieldID(coords[0], coords[1]);
				pO.respondKnightCard(newRobber, target);

			}
		}

		// try getting longest road
		else if (!getMe().hasLongestRoad()) {
			if (getMe().getAmountStreets() > 0) {
				if (resourceAgent.canBuildRoad()) {
					int radius = DefaultSettings.BOARD_RADIUS;
					int[][][] bestUtilityRoad = new int[7][7][3];
					for (int i = 0; i < resourceAgent.getMyEdges().size(); i++) {
						// my road
						Edge myEdge = resourceAgent.getMyEdges().get(i);
						int[] coord = ProtocolToModel.getEdgeCoordinates(myEdge.getEdgeID());
						// that road's neighbours
						Edge[] linkedEdges = getGl().getBoard().getLinkedEdges(coord[0], coord[1], coord[2]);
						// loop neighbours
						for (int j = 0; j < linkedEdges.length; j++) {
							// if neighbour exists and is empty
							if (linkedEdges[j] != null && !linkedEdges[j].isHasStreet()) {
								int[] coord2 = ProtocolToModel.getEdgeCoordinates(linkedEdges[j].getEdgeID());
								Edge[] neighbourOfLinked = getGl().getBoard().getLinkedEdges(coord2[0], coord2[1],
										coord2[2]);
								for (int k = 0; k < neighbourOfLinked.length; k++) {
									if (neighbourOfLinked[k] != null && neighbourOfLinked[k] != myEdge) {
										// if it's my road that can be connected
										if (neighbourOfLinked[k].isHasStreet()) {
											if (neighbourOfLinked[k].getOwnerID() == getID()) {
												bestUtilityRoad[coord2[0] + radius][coord2[1]
														+ radius][coord2[2]] += 100;
											} else
												bestUtilityRoad[coord2[0] + radius][coord2[1]
														+ radius][coord2[2]] += 10;
										} else {
											bestUtilityRoad[coord2[0] + radius][coord2[1] + radius][coord2[2]] += 1;
										}
									}

								}
							}
						}

					}
					int max = bestUtilityRoad[0][0][0];
					int x = 0, y = 0, z = 0;
					for (int i = 0; i < 7; i++) {
						for (int j = 0; j < 7; j++) {
							for (int k = 0; k < 3; k++) {
								if (bestUtilityRoad[i][j][k] > max) {
									max = bestUtilityRoad[i][j][k];
									x = i - radius;
									y = j - radius;
									z = k;

								}
							}
						}
					}
					pO.requestBuildRoad(x, y, z);
					resourceAgent.update();
				}
				// }
			}
		}

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

	@Override
	public void updateCards() {
		cardAgent.updateCards();
	}
}
