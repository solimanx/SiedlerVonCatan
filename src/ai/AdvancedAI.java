package ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import ai.agents.BanditAgent;
import ai.agents.CardAgent;
import ai.agents.CornerAgent;
import ai.agents.OpponentAgent;
import ai.agents.ResourceAgent;
import ai.agents.TradeAgent;
import ai.agents.TradeOffer;
import enums.CardType;
import enums.CornerStatus;
import enums.ResourceType;
import model.Board;
import model.GameLogic;
import model.HexService;
import model.objects.Corner;
import model.objects.Edge;
import model.objects.Field;
import model.objects.PlayerModel;
import network.ModelToProtocol;
import network.ProtocolToModel;
import settings.DefaultSettings;

// TODO: Auto-generated Javadoc
/**
 * Communication between other agents and input/output from/to server. The brain
 * behind the AI.
 *
 */
public class AdvancedAI {

	private GameLogic gl;

	private Board board;

	private PlayerModel me;
	private int ID;
	private int colorCounter = 0;
	private boolean started = false;

	private final String PROTOCOL = DefaultSettings.PROTOCOL_VERSION;
	private final String VERSION = DefaultSettings.AI_VERSION;

	private ResourceBundle rb = ResourceBundle.getBundle("ai.bundle.AIProperties");

	private CornerAgent[] cornerAgent;
	private CardAgent cardAgent;
	private ResourceAgent resourceAgent;
	private OpponentAgent opponentAgent;
	private TradeAgent tradeAgent;

	private BanditAgent banditAgent;

	private ArrayList<CornerAgent> myCornerAgents = new ArrayList<CornerAgent>();

	private Map<Integer, Double> diceRollProbabilities;

	private int[] initialResourceWeight = { 0, 0, Integer.parseInt(rb.getString("ORE_INITIAL_BENEFIT")), 0,
			Integer.parseInt(rb.getString("CORN_INITIAL_BENEFIT")) };

	private int[] globalResourceWeight = { 100, 100, 100, 100, 100 };

	private Double[] buildingWeight = { 50.0, 50.0, 50.0, 50.0 };

	private int initialRoundCounter = 0;
	private int roadCounter = 0;

	// a counter for the AI InputHandler;
	protected int devCardActionCounter = 0;

	protected boolean isInitialPhase;

	private double knightValue;
	private double monopolyValue;
	private double inventionValue;
	private double roadBuildingValue;

	private PrimitiveAI primitiveAI;

	private AIOutputHandler pO;

	protected Integer tradeWaitForBuilding;

	protected CardType currentDevCard;

	protected CardType boughtDevCard;

	/**
	 * Instantiates a new advanced AI.
	 *
	 * @param serverHost
	 *            the server host
	 * @param port
	 *            the port
	 */
	public AdvancedAI(String serverHost, int port) {

		AIInputHandler inputHa = new AIInputHandler(this);
		this.primitiveAI = new PrimitiveAI(serverHost, port, inputHa);
		this.pO = new AIOutputHandler(primitiveAI);

		this.board = new Board();
		this.gl = new GameLogic(board);

		primitiveAI.commence();

		initializeDiceRollProbabilities();
		// Integer.parseInt(rb.getString("CORNER_AGENTS"))
		this.cornerAgent = new CornerAgent[54];
		this.cardAgent = new CardAgent(this);
		this.resourceAgent = new ResourceAgent(this);
		this.opponentAgent = new OpponentAgent();
		this.tradeAgent = new TradeAgent(this, resourceAgent);
		this.banditAgent = new BanditAgent(this, opponentAgent);
	}

	/**
	 * Initialize corner agents.
	 */
	private void initializeCornerAgents() {
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
	}

	/**
	 * Giving up half of resources by order.
	 */
	protected void looseToBandit() {
		// Count all my resources
		int[] myResources = getMe().getResources().clone();
		int sum = resourceAgent.sumArray(myResources);
		// loss is half of sum
		int loss = sum / 2;
		// losses array
		int[] losses = { 0, 0, 0, 0, 0 };
		for (int i = 0; i < loss; i++) {	
			Double[] currResWeighting = resourceAgent.getMyResourceWeight();
			for (int j = 0; j < currResWeighting.length; j++) {
				currResWeighting[j] = myResources[j] * (51 - currResWeighting[j]);
			}
			int maxIndex = 0;
			Double maxValue = currResWeighting[0];
			for (int j = 0; j < currResWeighting.length; j++) {
				if (maxValue < currResWeighting[j]){
					maxValue = currResWeighting[j];
					maxIndex = j;
				}
			}
			myResources[maxIndex]--;
			losses[maxIndex]++;	    
		}

		pO.respondRobberLoss(losses);

	}

	/**
	 * Giving up half of resources by order.
	 */
	protected void loseToBandit() {
		// Count all my resources
		int[] myResources = getMe().getResources().clone();
		int sum = 0;

		for (int i = 0; i < 5; i++)
			sum += getMe().getResourceAmountOf(i);
		// loss is half of sum
		int loss = sum / 2;
		// losses array
		int[] losses = { 0, 0, 0, 0, 0 };

		// until losses amount is reached
		while (loss > 0) {
			// scan every resource
			for (int j = 0; j < 5; j++) {
				// if there's some of it
				if (myResources[j] > 0) {
					// decrement it from your list
					myResources[j] -= 1;
					// increment it to losses array
					losses[j]++;
					loss -= 1;
					// check if losses amount is reached
					break;
				}
				// if there's none of it
				else {
					// check the next resource type
					continue;
				}

			}
		}

		// send the losses to the output handler
		pO.respondRobberLoss(losses);

	}

	// ================================================================================
	// BOARD UPDATES
	// ================================================================================

	/**
	 * Initialize board.
	 *
	 * @param fields
	 *            the fields
	 * @param corners
	 *            the corners
	 * @param streets
	 *            the streets
	 * @param harbourCorners
	 *            the harbour corners
	 * @param banditLocation
	 *            the bandit location
	 */
	protected void updateBoard(Field[] fields, Corner[] corners, ArrayList<Edge> streets, Corner[] harbourCorners,
			String banditLocation) {

		this.me = new PlayerModel(ID);
		for (Field f : fields) {
			String location = f.getFieldID();
			int[] coords = ProtocolToModel.getFieldCoordinates(location);
			Field bField = gl.getBoard().getFieldAt(coords[0], coords[1]);
			bField.setFieldID(location);
			bField.setDiceIndex(f.getDiceIndex());
			;
			bField.setResourceType(f.getResourceType());
		}
		for (Corner c : corners) {
			String location = c.getCornerID();
			int coords[] = ProtocolToModel.getCornerCoordinates(location);
			Corner bCorner = gl.getBoard().getCornerAt(coords[0], coords[1], coords[2]);
			bCorner.setCornerID(location);
			bCorner.setOwnerID(c.getOwnerID());
			bCorner.setStatus(c.getStatus());
		}
		for (Edge s : streets) {
			String location = s.getEdgeID();
			int coords[] = ProtocolToModel.getEdgeCoordinates(location);
			Edge bEdge = gl.getBoard().getEdgeAt(coords[0], coords[1], coords[2]);
			bEdge.setEdgeID(location);
			bEdge.setHasStreet(s.isHasStreet());
			bEdge.setOwnedByPlayer(s.getOwnerID());
		}
		for (Corner c : harbourCorners) {
			String location = c.getCornerID();
			int[] coords = ProtocolToModel.getCornerCoordinates(location);
			Corner bCorner = gl.getBoard().getCornerAt(coords[0], coords[1], coords[2]);
			bCorner.setCornerID(location);
			bCorner.setHarbourStatus(c.getHarbourStatus());
		}

		gl.getBoard().setBandit(banditLocation);
		gl.getBoard().deletePlayers();
		isInitialPhase = true;
		initializeCornerAgents();
	}

	/**
	 * Updates a new village in the board.
	 *
	 * @param x
	 *            Axial-x corner coordinate
	 * @param y
	 *            Axial-y corner coordinate
	 * @param dir
	 *            corner direction
	 * @param playerID
	 *            owner
	 */
	protected void updateVillage(int x, int y, int dir, int playerID) {
		Corner c = gl.getBoard().getCornerAt(x, y, dir);
		c.setStatus(enums.CornerStatus.VILLAGE);
		c.setOwnerID(playerID);
		CornerAgent ca = getCornerAgentByID(c.getCornerID());
		ca.setPlayerID(playerID);
		ca.setState(CornerStatus.VILLAGE);
		Corner[] neighbors = gl.getBoard().getAdjacentCorners(x, y, dir);
		for (int i = 0; i < neighbors.length; i++) {
			if (neighbors[i] != null) {
				getCornerAgentByID(neighbors[i].getCornerID()).setState(CornerStatus.BLOCKED);
				neighbors[i].setStatus(CornerStatus.BLOCKED);
			}
		}
		if (!isInitialPhase) {
			if (playerID == getMe().getID()) {
				resourceAgent.calculateMyResourceWeight();
			}
			resourceAgent.calculateGlobalResourceWeight();
			calculateBuildingWeighting();
		}
	}

	/**
	 * Updates a new road in the board.
	 *
	 * @param i
	 *            Axial-x edge coordinate
	 * @param j
	 *            Axial-y edge coordinate
	 * @param k
	 *            edge direction
	 * @param playerID
	 *            owner
	 */
	protected void updateRoad(int i, int j, int k, int playerID) {
		roadCounter++;
		Edge e = gl.getBoard().getEdgeAt(i, j, k);
		e.setHasStreet(true);
		e.setOwnedByPlayer(playerID);
		if (roadCounter == (getOpponentAgent().getAmountPlayer() + 1) * 2) {
			isInitialPhase = false;
			resourceAgent.calculateMyResourceWeight();
			resourceAgent.calculateGlobalResourceWeight();
			calculateBuildingWeighting();
		}

	}

	/**
	 * Calculate building weighting.
	 */
	private void calculateBuildingWeighting() {
		Double[] resourceWeighting = resourceAgent.getMyResourceWeight();
		// buildingWeight[i] = average of resourceWeightings needed for this
		// building * (estimated) victoryPoints
		// * amountBoughtAlreadyOfThisType/TotalAmountofThisType (prevents the
		// ai from doing the same things too often
		// & e.g. no streets left the building weight will turn 0
		buildingWeight[0] = (50.0 - ((resourceWeighting[0] + resourceWeighting[1]) / 2)) * 0.2
				* ((double) getMe().getAmountStreets() / (double) DefaultSettings.START_AMOUNT_STREETS);
		buildingWeight[1] = (50.0
				- ((resourceWeighting[0] + resourceWeighting[1] + resourceWeighting[3] + resourceWeighting[4]) / 4))
				* ((double) getMe().getAmountVillages() / (double) DefaultSettings.START_AMOUNT_VILLAGES);
		buildingWeight[2] = (50.0 - (resourceWeighting[2] * 3 + resourceWeighting[4] * 2) / 5)
				* ((double) getMe().getAmountCities() / (double) DefaultSettings.START_AMOUNT_CITIES);
		// TODO: currently only knight cards are considered
		buildingWeight[3] = (50.0 - (resourceWeighting[2] + resourceWeighting[3] + resourceWeighting[4]) / 3) * 0.5
				* ((double) (DefaultSettings.AMOUNT_KNIGHT_CARDS - getMe().getPlayedKnightCards())
						/ (double) DefaultSettings.AMOUNT_KNIGHT_CARDS);

		System.out.println("Calculated new Building Weight: " + buildingWeight[0] + " " + buildingWeight[1] + " "
				+ buildingWeight[2] + " " + buildingWeight[3]);

	}

	/**
	 * Updates a new city in the board.
	 *
	 * @param i
	 *            Axial-x corner coordinate
	 * @param j
	 *            Axial-y corner coordinate
	 * @param k
	 *            corner direction
	 * @param playerID
	 *            owner
	 */
	protected void updateCity(int i, int j, int k, int playerID) {
		Corner c = gl.getBoard().getCornerAt(i, j, k);
		c.setStatus(enums.CornerStatus.CITY);
		getCornerAgentByID(c.getCornerID()).setState(CornerStatus.CITY);
		if (playerID == getMe().getID()) {
			resourceAgent.calculateMyResourceWeight();
		}
		resourceAgent.calculateGlobalResourceWeight();
		calculateBuildingWeighting();

	}

	/**
	 * Update robber.
	 *
	 * @param locationID
	 *            the location ID
	 */
	protected void updateRobber(String locationID) {
		gl.getBoard().setBandit(locationID);

	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getID() {
		return ID;
	}

	/**
	 * Sets the id.
	 *
	 * @param playerID
	 *            the new id
	 */
	protected void setID(int playerID) {
		ID = playerID;

	}

	/**
	 * Gets the color counter.
	 *
	 * @return the color counter
	 */
	protected int getColorCounter() {
		return colorCounter;
	}

	/**
	 * Sets the color counter.
	 *
	 * @param colorCounter
	 *            the new color counter
	 */
	protected void setColorCounter(int colorCounter) {
		this.colorCounter = colorCounter;
	}

	/**
	 * Checks if is started.
	 *
	 * @return true, if is started
	 */
	protected boolean isStarted() {
		return started;
	}

	/**
	 * Sets the started.
	 *
	 * @param started
	 *            the new started
	 */
	protected void setStarted(boolean started) {
		this.started = started;
	}

	/**
	 * Gets the me.
	 *
	 * @return the me
	 */
	public PlayerModel getMe() {
		return me;
	}

	/**
	 * Gets the gl.
	 *
	 * @return the gl
	 */
	public GameLogic getGl() {
		return gl;
	}

	/**
	 * Initial village.
	 */
	/*
	 * (non-Javadoc)
	 *
	 * @see ai.PrimitiveAI#initialVillage()
	 */
	public void initialVillage() {
		if (initialRoundCounter == 1) {
			subtractResources(myCornerAgents.get(0));

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
		pO.requestBuildVillage(x, y, z);

	}

	/**
	 * Initial road.
	 */
	/*
	 * (non-Javadoc)
	 *
	 * @see ai.PrimitiveAI#initialRoad()
	 */
	public void initialRoad() {
		setResourceWeighting(new int[] { 0, 0, 0, 0, 0 });
		myCornerAgents.get(initialRoundCounter).calculateInitialRoad();
		int[] rC = myCornerAgents.get(initialRoundCounter).getBestRoad();
		pO.requestBuildRoad(rC[0], rC[1], rC[2]);

		initialRoundCounter++;
		resourceAgent.initializeResources();
		if (initialRoundCounter > 1) {
			resourceAgent.calculateMyResourceWeight();
			// TODO; erst am Ende der initial phase
			// resourceAgent.calculateGlobalResourceWeight();
			// TODO: Strategie festlegen
		}

	}

	/**
	 * Oldactuate.
	 */
	/*
	 * (non-Javadoc)
	 *
	 * @see ai.PrimitiveAI#actuate()
	 */
	public void oldactuate() {
		resourceAgent.update();

		receiveProposals();
		/*
		 * if (cardAgent.getSum() > 0) { if (cardAgent.getSum() == 1) { if
		 * (cardAgent.hasKnight()) { if (knightValue > 1.0) {
		 * cardAgent.playKnightCard(); } } else if (cardAgent.hasMonopoly() &&
		 * (getMe().getVictoryPoints() == 8 || getMe().getVictoryPoints() == 9))
		 * { if (monopolyValue > 0.5) { cardAgent.playMonopolyCard(); } } else
		 * if (cardAgent.hasMonopoly()) { if (monopolyValue > 1.5) {
		 * cardAgent.playMonopolyCard(); } } else if (cardAgent.hasInvention())
		 * { if (inventionValue > 0.5) { cardAgent.playInventionCard(); } } else
		 * if (cardAgent.hasRoad()) { if (roadBuildingValue > 0.5) {
		 * cardAgent.playRoadCard(); } } } else { double max =
		 * Math.max(knightValue, Math.max(monopolyValue,
		 * Math.max(inventionValue, roadBuildingValue))); if (max > 0.5) { if
		 * (knightValue == max) { cardAgent.playKnightCard(); } else if
		 * (monopolyValue == max) { cardAgent.playMonopolyCard(); } else if
		 * (inventionValue == max) { cardAgent.playInventionCard(); } else if
		 * (roadBuildingValue == max) { cardAgent.playRoadCard(); } } }
		 * 
		 * }
		 */

		// DEBUG ONLY
		if (cardAgent.hasMonopoly()) {
			cardAgent.playMonopolyCard();
		}
		// DEBUG ONLY
		if (cardAgent.hasInvention()) {
			cardAgent.playInventionCard();
		}

		if (getMe().getAmountStreets() > 0 && cardAgent.hasRoad()) {
			cardAgent.playRoadCard();
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
			Corner bestCorner = resourceAgent.calculateBestVillage();
			if (bestCorner != null) {
				int[] coords = ProtocolToModel.getCornerCoordinates(bestCorner.getCornerID());
				pO.requestBuildVillage(coords[0], coords[1], coords[2]);
				// DEBUG
				pO.respondEndTurn();
			} else { // try to build street
				if (!getMe().hasLongestRoad() && getMe().getAmountStreets() > 0 && resourceAgent.canBuildRoad()) {
					Edge bestEdge = resourceAgent.calculateBestStreet();
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
			Edge bestEdge = resourceAgent.calculateBestStreet();
			String id = bestEdge.getEdgeID();
			int[] coords = HexService.getEdgeCoordinates(id.substring(0, 1), id.substring(1, 2));
			pO.requestBuildRoad(coords[0], coords[1], coords[2]);
		}
		// if AI can do nothing
		else {
			getOutput().respondEndTurn();
		}
	}

	/**
	 * Actuate.
	 */
	public void actuate() {
		resourceAgent.update();
		updateCards();
		tradeAgent.updateAgent();

		// Dev Cards hier
		if (cardAgent.hasMonopoly()) {
			cardAgent.playMonopolyCard();
			currentDevCard = CardType.MONOPOLY;
		}
		// DEBUG ONLY
		else if (cardAgent.hasInvention()) {
			cardAgent.playInventionCard();
			currentDevCard = CardType.INVENTION;
		}

		else if (getMe().getAmountStreets() > 0 && cardAgent.hasRoad()) {
			cardAgent.playRoadCard();
			currentDevCard = CardType.STREET;
		} else if (cardAgent.hasKnight()) {
			banditAgent.moveRobber();
			int[] coords = banditAgent.bestNewRobber();
			Integer target = banditAgent.getTarget();
			String newRobber = ModelToProtocol.getFieldID(coords[0], coords[1]);
			pO.respondKnightCard(newRobber, target);
		} else {
			// keine dev card; versuche zu bauen.

			Double[] buildingWeightCopy = new Double[4];
			System.arraycopy(buildingWeight, 0, buildingWeightCopy, 0, buildingWeight.length);
			for (int i = 0; i < buildingWeight.length; i++) {
				int max = 0;
				Double maxValue = buildingWeightCopy[0];
				for (int j = 1; j < buildingWeightCopy.length; j++) {
					if (maxValue < buildingWeightCopy[j]) {
						maxValue = buildingWeightCopy[j];
						max = j;
					}
				}
				boolean[] possibleBuildings = resourceAgent.getPossibleBuildings();
				System.out.println("Possible Buildings: " + possibleBuildings[0] + " " + possibleBuildings[1] + " "
						+ possibleBuildings[2] + " " + possibleBuildings[3]);
				if (possibleBuildings[max]) {
					// bester Fall: baue sofort
					System.out.println("AI: Build " + max);
					build(max);
					break;
				} else if (tradeAgent.isBuildableAfterTrade(max)) {
					// finalBuild Check nötig, da checks von
					// getPossibleBuildings im Trade Agent nicht stattfinden
					boolean finalBuildCheck = false;
					switch (max) {
					case 0:
						if (resourceAgent.getBestStreet() != null) {
							finalBuildCheck = true;
						}
						break;
					case 1:
						if (resourceAgent.getBestVillage() != null) {
							finalBuildCheck = true;
						}
						break;
					case 2:
						if (resourceAgent.getBestCity() != null) {
							finalBuildCheck = true;
						}
						break;
					case 3:
						// if (cardAgent.hasAlreadyPlayedCard() == false) {
						finalBuildCheck = true;
						// }
						break;
					default:
						break;
					}
					if (finalBuildCheck) {
						ArrayList<TradeOffer> trades = tradeAgent.tradesForBuilding(max);
						for (int j = 0; j < trades.size(); j++) {
							getOutput().requestSeaTrade(trades.get(j).getOffer(), trades.get(j).getDemand());
						}
						tradeWaitForBuilding = max;
						System.out.println("AI: Trade for " + max);
						break;
					}

				} else if (i == buildingWeight.length - 1) {
					// ende vom Array; nichts ist möglich
					endTurn();
				} else {
					int[] missing = resourceAgent.getResourcesMissingForBuilding(max);
					if (resourceAgent.sumArray(missing) >= 2
							&& resourceAgent.sumArray(resourceAgent.getOwnResources()) <= 4) {
						// sparen!!
						endTurn();
						break;
					}
				}
				System.out.println("Check next " + max);
				// next turn he will check second highest weighting
				buildingWeightCopy[max] = Double.MIN_VALUE;
			}
		}

	}

	/**
	 * Check incoming trade.
	 */
	public void checkIncomingTrade() {
		resourceAgent.update();
		boolean[] possibleBuildings = resourceAgent.getPossibleBuildings();
		if (possibleBuildings[tradeWaitForBuilding]) {
			build(tradeWaitForBuilding);
			tradeWaitForBuilding = null;
		}
	}

	/**
	 * Builds the.
	 *
	 * @param project
	 *            the project
	 */
	public void build(int project) {
		switch (project) {
		case 0:
			Edge bestEdge = resourceAgent.getBestStreet();
			String id = bestEdge.getEdgeID();
			int[] coords = HexService.getEdgeCoordinates(id.substring(0, 1), id.substring(1, 2));
			pO.requestBuildRoad(coords[0], coords[1], coords[2]);
			break;
		case 1:
			Corner bestVCorner = resourceAgent.getBestVillage();
			int[] vCoords = ProtocolToModel.getCornerCoordinates(bestVCorner.getCornerID());
			pO.requestBuildVillage(vCoords[0], vCoords[1], vCoords[2]);
			break;
		case 2:
			Corner bestCCorner = resourceAgent.getBestCity();
			int[] cCoords = ProtocolToModel.getCornerCoordinates(bestCCorner.getCornerID());
			pO.requestBuildCity(cCoords[0], cCoords[1], cCoords[2]);
			break;
		case 3:
			pO.requestBuyCard();
			break;
		default:
			// this should never happen
			endTurn();
			break;
		}
	}

	/**
	 * Move robber.
	 */
	/*
	 * (non-Javadoc)
	 *
	 * @see ai.PrimitiveAI#moveRobber()
	 */
	protected void moveRobber() {
		banditAgent.moveRobber();
		int[] coords = banditAgent.bestNewRobber();
		Integer target = banditAgent.getTarget();
		String newRobber = ModelToProtocol.getFieldID(coords[0], coords[1]);
		pO.respondMoveRobber(newRobber, target);

	}

	/**
	 * End turn.
	 */
	private void endTurn() {
		if (boughtDevCard != null) {
			getMe().incrementPlayerDevCard(ProtocolToModel.getDevCard(boughtDevCard));
		}
		boughtDevCard = null;
		pO.respondEndTurn();
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

	/**
	 * Gets the resource agent.
	 *
	 * @return the resource agent
	 */
	/*
	 * (non-Javadoc)
	 *
	 * @see ai.PrimitiveAI#getResourceAgent()
	 */
	public ResourceAgent getResourceAgent() {
		return this.resourceAgent;

	}

	/**
	 * Update cards.
	 */
	/*
	 * (non-Javadoc)
	 *
	 * @see ai.PrimitiveAI#updateCards()
	 */
	public void updateCards() {
		cardAgent.updateCards();
	}

	/**
	 * Gets the opponent agent.
	 *
	 * @return the opponent agent
	 */
	public OpponentAgent getOpponentAgent() {
		return opponentAgent;
	}

	/**
	 * Sets the opponent agent.
	 *
	 * @param opponentAgent
	 *            the new opponent agent
	 */
	public void setOpponentAgent(OpponentAgent opponentAgent) {
		this.opponentAgent = opponentAgent;
	}

	/**
	 * Gets the global resource weight.
	 *
	 * @return the global resource weight
	 */
	public int[] getGlobalResourceWeight() {
		return globalResourceWeight;
	}

	/**
	 * Sets the global resource weight.
	 *
	 * @param changes
	 *            value of the change positive values to increment negative
	 *            values to decrement
	 */
	public void setGlobalResourceWeight(int[] changes) {
		if (changes.length != 5) {
			throw new IllegalArgumentException("int Array length != 5 in aai.setGlobalResourceWeight");
		}
		for (int i = 0; i < globalResourceWeight.length; i++) {
			globalResourceWeight[i] = globalResourceWeight[i] + changes[i];
		}
	}

	/**
	 * Calculate value of playing each card if available.
	 */
	public void receiveProposals() {

	}

	/**
	 * Play street card.
	 *
	 * @param coords1
	 *            the coords 1
	 * @param coords2
	 *            the coords 2
	 */
	public void playStreetCard(int[] coords1, int[] coords2) {
		if (coords2 == null) {
			pO.requestPlayStreetCard(coords1, null);
			devCardActionCounter = 1;
		} else {
			pO.requestPlayStreetCard(coords1, coords2);
			devCardActionCounter = 2;
		}

	}

	/**
	 * Play monopoly card.
	 *
	 * @param rt
	 *            the rt
	 */
	public void playMonopolyCard(ResourceType rt) {
		pO.requestPlayMonopolyCard(rt);
		devCardActionCounter = 1;

	}

	/**
	 * Play invention card.
	 *
	 * @param rt
	 *            the rt
	 * @param rt2
	 *            the rt 2
	 */
	public void playInventionCard(ResourceType rt, ResourceType rt2) {
		int[] resources = { 0, 0, 0, 0, 0 };
		resources[ModelToProtocol.getIndexResource(rt)]++;
		resources[ModelToProtocol.getIndexResource(rt2)]++;
		pO.requestPlayInventionCard(resources);
		devCardActionCounter = 1;

	}

	/**
	 * Gets the my corner agents.
	 *
	 * @return the my corner agents
	 */
	public ArrayList<CornerAgent> getMyCornerAgents() {
		return myCornerAgents;
	}

	/**
	 * Gets the corner agents.
	 *
	 * @return the corner agents
	 */
	public CornerAgent[] getCornerAgents() {
		return cornerAgent;
	}

	/**
	 * Gets the output.
	 *
	 * @return the output
	 */
	protected AIOutputHandler getOutput() {
		return this.pO;
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	protected String getVersion() {
		return VERSION;
	}

	/**
	 * Gets the protocol.
	 *
	 * @return the protocol
	 */
	protected String getProtocol() {
		return PROTOCOL;
	}
}
