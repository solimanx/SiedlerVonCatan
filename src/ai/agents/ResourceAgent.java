package ai.agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import ai.AdvancedAI;
import enums.CornerStatus;
import enums.ResourceType;
import model.HexService;
import model.objects.Corner;
import model.objects.Edge;
import network.ProtocolToModel;
import settings.DefaultSettings;

/**
 * Tracks own resources and sees what AI can purchase at any given time.
 */
public class ResourceAgent {
	private AdvancedAI aai;
	// {WOOD, CLAY, ORE, SHEEP, CORN}
	private int[] ownResources;
	private int currentBuyingFocus; // 0 = Street; 1 = Village; 2 = City; 3 =
									// DevCard
	private ArrayList<Corner> myCorners = new ArrayList<Corner>();
	private ArrayList<Edge> myEdges = new ArrayList<Edge>();
	private ArrayList<StreetSet> myStreetSets = new ArrayList<StreetSet>(); // evtl.
																			// import
																			// ändern

	public ArrayList<StreetSet> getMyStreetSets() {
		return myStreetSets;
	}

	public final static HashMap<Integer, int[]> buildingCosts = new HashMap<Integer, int[]>() {
		{
			put(0, DefaultSettings.STREET_BUILD_COST);
			put(1, DefaultSettings.VILLAGE_BUILD_COST);
			put(2, DefaultSettings.CITY_BUILD_COST);
			put(3, DefaultSettings.DEVCARD_BUILD_COST);
		}
	};
	// (Street,Village,City,DevCard)
	private boolean[] affords;
	// {THREE_TO_ONE, WOOD, CLAY, ORE, SHEEP, CORN}
	// maybe in trading agent?
	private boolean[] harbours = { false, false, false, false, false, false };

	/**
	 * Instantiates a new resource agent.
	 *
	 * @param ai
	 *            the ai
	 */
	public ResourceAgent(AdvancedAI ai) {
		this.aai = ai;

	}

	/**
	 * Initialize resources.
	 */
	public void initializeResources() {
		ownResources = aai.getMe().getResources();
	}

	/**
	 * Calculates the cards to give to the robber.
	 *
	 * @return int[] to give to robber
	 */
	public int[] getRobberLossCards() {
		int size = sumArray(ownResources) / 2;
		int[] result = new int[5];
		// if (aai.getCurrentTactic().equals("CORN_ORE_STRATEGY")){
		for (int i = 0; i < ownResources[0] + ownResources[1]; i++) {
			if (size == 0) {
				break;
			} else {
				if (ownResources[0] > 0) {
					ownResources[0]--;
					result[0]++;
				} else {
					ownResources[1]--;
					result[1]++;
				}
			}
		}
		if (size == 0) {
			return result;
		} else {
			for (int i = 0; i < sumArray(ownResources); i++) {
				if (size == 0) {
					break;
				} else {
					if (ownResources[2] > 0) {
						ownResources[2]--;
						result[2]++;
					} else if (ownResources[3] > 0) {
						ownResources[3]--;
						result[3]++;
					} else {
						ownResources[4]--;
						result[4]++;
					}
				}
			}
			return result;
		}
		/*
		 * } else {
		 * 
		 * }
		 */
	}

	/**
	 * gets possible buys with current resourceCards.
	 *
	 * @return boolean list (Street,Village,City,DevCard)
	 */
	public boolean[] getPossibleBuildings() {
		boolean[] results = new boolean[4];
		if (compareResources(ownResources, DefaultSettings.STREET_BUILD_COST)) {
			results[0] = true;
		}
		if (compareResources(ownResources, DefaultSettings.VILLAGE_BUILD_COST)) {
			results[1] = true;
		}
		if (compareResources(ownResources, DefaultSettings.CITY_BUILD_COST)) {
			results[2] = true;
		}
		if (compareResources(ownResources, DefaultSettings.DEVCARD_BUILD_COST)) {
			results[3] = true;
		}
		return results;
	}

	/**
	 * returns the building type with the lowest resource difference to
	 * playersResources.
	 *
	 * @return the most probable building
	 */
	public int[] getMostProbableBuilding() {
		int[] buildingDifference = new int[4];
		int mostProbable = 0;
		int lowestDifference = 10; // initial value
		for (int i = 0; i < 4; i++) {
			int[] currDifference = new int[5];
			for (int j = 0; j < 5; j++) {
				currDifference[j] = buildingCosts.get(i)[j] - ownResources[j];
				if (currDifference[j] < 0) {
					buildingDifference[i] += currDifference[j];
				}
			}
			if (buildingDifference[i] < lowestDifference) {
				lowestDifference = buildingDifference[i];
				mostProbable = i;
			}
		}
		return new int[] { mostProbable, lowestDifference };
	}

	/**
	 * returns the resources missing for building a certain type.
	 *
	 * @param bType
	 *            the b type
	 * @return int[] resourcesMissing
	 */
	public int[] getResourcesMissingForBuilding(int bType) {
		int[] costs = buildingCosts.get(bType);
		int[] difference = new int[5];
		for (int i = 0; i < 5; i++) {
			if (costs[i] - ownResources[i] > 0) {
				difference[i] = costs[i] - ownResources[i];
			}

		}
		return difference;
	}

	/**
	 * compares two resource arrays.
	 *
	 * @param playerResources
	 *            the player resources
	 * @param resource
	 *            the resource
	 * @return true if the second array is <= for every resource, else false
	 */
	public static boolean compareResources(int[] playerResources, int[] resource) {
		for (int i = 0; i < playerResources.length; i++) {
			if (playerResources[i] < resource[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Sum array.
	 *
	 * @param array
	 *            the array
	 * @return the int
	 */
	private int sumArray(int[] array) {
		int result = 0;
		for (int i = 0; i < array.length; i++) {
			result += array[i];
		}
		return result;
	}

	/**
	 * Adds the.
	 *
	 * @param cornerAt
	 *            the corner at
	 */
	public void add(Corner cornerAt) {
		myCorners.add(cornerAt);
	}

	/**
	 * Adds the.
	 *
	 * @param edgeAt
	 *            the edge at
	 */
	public void add(Edge edgeAt) {
		myEdges.add(edgeAt);
	}

	/**
	 * Update.
	 */
	public void update() {
		initializeResources();
		affords = getPossibleBuildings();

	}

	/**
	 * Can build road.
	 *
	 * @return true, if successful
	 */
	public boolean canBuildRoad() {
		return affords[0];
	}

	/**
	 * Can build village.
	 *
	 * @return true, if successful
	 */
	public boolean canBuildVillage() {
		return affords[1];
	}

	/**
	 * Can build city.
	 *
	 * @return true, if successful
	 */
	public boolean canBuildCity() {
		return affords[2];
	}

	/**
	 * Can buy card.
	 *
	 * @return true, if successful
	 */
	public boolean canBuyCard() {
		return affords[3];
	}

	/**
	 * Gets the my corners.
	 *
	 * @return the my corners
	 */
	public ArrayList<Corner> getMyCorners() {
		return myCorners;
	}

	/**
	 * Gets the my edges.
	 *
	 * @return the my edges
	 */
	public ArrayList<Edge> getMyEdges() {
		return myEdges;
	}

	/**
	 * adds a specified street (edge object) to a street set.
	 *
	 * @param e
	 *            the e
	 */
	public void addToOwnStreetSet(Edge e) {
		int[] coord = ProtocolToModel.getEdgeCoordinates(e.getEdgeID());
		boolean edgeAdded = false;
		Edge[] neighbours = aai.getGl().getBoard().getLinkedEdges(coord[0], coord[1], coord[2]);
		int addedSetIndex = 0;
		for (int i = 0; i < myStreetSets.size(); i++) {
			if (myStreetSets.get(i) != null) {
				int j = 0;
				boolean currSetAdded = false;
				while (j < neighbours.length && currSetAdded == false) {
					if (myStreetSets.get(i).getEdges().contains(neighbours[j])) {
						if (edgeAdded) {
							myStreetSets.get(addedSetIndex).getEdges().addAll(myStreetSets.get(i).getEdges());
							myStreetSets.remove(i);
							break;
						}
						myStreetSets.get(i).addEdge(e);
						addedSetIndex = i;
						currSetAdded = true;
						edgeAdded = true;

					}
					j++;
				}
			}
		}
		if (edgeAdded == false) {
			ArrayList<Edge> edges = new ArrayList<Edge>();
			edges.add(e);
			StreetSet streetSet = new StreetSet(aai.getID(), edges);
			myStreetSets.add(streetSet);
		}
	}

	/**
	 * Gets the best street.
	 *
	 * @return the best street
	 */
	public Edge getBestStreet() {
		ArrayList<Object> bestStreets = getPossibleLTRExtensions();
		StreetSet streetSet = (StreetSet) bestStreets.get(0);
		ArrayList<Edge> edgeSuggestion = (ArrayList<Edge>) bestStreets.get(1);
		int[] edgeWeighting = new int[edgeSuggestion.size()];
		Edge[] currNeighbours;
		int[] currCoords;
		for (int i = 0; i < edgeSuggestion.size(); i++) {
			currCoords = ProtocolToModel.getEdgeCoordinates(edgeSuggestion.get(i).getEdgeID());
			currNeighbours = aai.getGl().getBoard().getLinkedEdges(currCoords[0], currCoords[1], currCoords[2]);
			boolean oneHostileEdge = false;
			for (int j = 0; j < currNeighbours.length; j++) {
				// is not already in street set
				if (currNeighbours[j] != null) {
					if (!streetSet.getEdges().contains(currNeighbours[j])) {
						// has street?
						if (currNeighbours[j].isHasStreet()) {
							if (currNeighbours[j].getOwnerID() == aai.getID()) {
								// own player; can connect two streets
								edgeWeighting[i] += 100;
							} else {
								if (oneHostileEdge) {
									edgeWeighting[i] -= 10;
								} else {
									edgeWeighting[i] += 10;
									oneHostileEdge = true;
								}

							}
						} else { // no street
							edgeWeighting[i] += 10;
						}
					}
				}
				// TODO: Schaue auf unmittelbare Corner Nachbarn und gewichte
				// nach angrenzenden fields

			}
		}
		// calculate max Weighting value
		int max = edgeWeighting[0];
		int maxIndex = 0;
		for (int i = 0; i < edgeWeighting.length; i++) {
			if (edgeWeighting[i] > max) {
				max = edgeWeighting[i];
				maxIndex = i;
			}
		}
		return edgeSuggestion.get(maxIndex);
	}

	/**
	 * gets the possible trading route extensions for the AI.
	 * 
	 * @return ArrayList<Object> with first: streetSet; second: ArrayList
	 *         <Edge> best street suggestions
	 */
	public ArrayList<Object> getPossibleLTRExtensions() {
		ArrayList<ArrayList<Edge>> bestNeighbours = new ArrayList<ArrayList<Edge>>();

		int[] maxIndex = { 0, 0 };
		for (int i = 0; i < myStreetSets.size(); i++) {
			ArrayList<Integer> longestStreets = new ArrayList<Integer>();
			ArrayList<Edge> currEndingStreets = new ArrayList<Edge>();
			ArrayList<Edge> bestEdges = new ArrayList<Edge>();
			bestNeighbours.add(new ArrayList<Edge>());
			StreetSet currStreetSet = null;

			currStreetSet = myStreetSets.get(i);
			// berechne zuerst alle endständigen Straßen dieses Street Sets
			for (int j = 0; j < currStreetSet.size(); j++) {
				String id = currStreetSet.getEdgeAt(j).getEdgeID();
				int[] coords = HexService.getEdgeCoordinates(id.substring(0, 1), id.substring(1, 2));
				Edge[] neighbours = aai.getGl().getBoard().getLinkedEdges(coords[0], coords[1], coords[2]);
				boolean top = false;
				boolean bottom = false;
				switch (coords[2]) {
				case 0: // neighbour order differs order of dir 1,2
					if (currStreetSet.getEdges().contains(neighbours[0])
							|| currStreetSet.getEdges().contains(neighbours[1])) {
						top = true;
					}
					if (currStreetSet.getEdges().contains(neighbours[2])
							|| currStreetSet.getEdges().contains(neighbours[3])) {
						bottom = true;
					}
					if (!(top && bottom)) {
						currEndingStreets.add(currStreetSet.getEdgeAt(j));
					}

					break;
				default:
					if (currStreetSet.getEdges().contains(neighbours[0])
							|| currStreetSet.getEdges().contains(neighbours[3])) {
						top = true;
					}
					if (currStreetSet.getEdges().contains(neighbours[2])
							|| currStreetSet.getEdges().contains(neighbours[1])) {
						bottom = true;
					}
					if (!(top && bottom)) {
						currEndingStreets.add(currStreetSet.getEdgeAt(j));
					}
					break;
				}
			}
			// berechne die Länge aus diesen...
			int gV;
			if (currEndingStreets.isEmpty() || currStreetSet.getHasCircle()) {
				currStreetSet.setHasCircle(true);
				ArrayList<Edge> alreadyChecked = new ArrayList<Edge>();
				ArrayList<Edge> lastNeighbours = new ArrayList<Edge>();
				ArrayList<Integer> greatestValue = new ArrayList<Integer>();
				// gehe über alle Straßen
				for (int j = 0; j < currStreetSet.size(); j++) {
					greatestValue.add(1 + depthFirstSearch(currStreetSet.getEdgeAt(j), currStreetSet, alreadyChecked,
							lastNeighbours));
				}
				gV = Collections.max(greatestValue);
				for (int j = 0; j < greatestValue.size(); j++) {
					// füge alle besten endständigen Edges hinzu
					// für den Fall dass es einen echten Kreis gibt; füge alle
					// hinzu
					// liefert falsches Ergebnis bei Doppelkreis, da keine
					// Straße das Strassenset
					// länger machen wird; aber dieser Fall ist sehr selten...
					if (greatestValue.get(j) == gV) {
						bestEdges.add(currStreetSet.getEdgeAt(j));
					}
				}

			} else {
				for (int j = 0; j < currEndingStreets.size(); j++) {
					ArrayList<Edge> alreadyChecked = new ArrayList<Edge>();
					ArrayList<Edge> lastNeighbours = new ArrayList<Edge>();
					longestStreets.add(1 + depthFirstSearch(currEndingStreets.get(j), currStreetSet, alreadyChecked,
							lastNeighbours));
				}
				gV = Collections.max(longestStreets);
				for (int j = 0; j < longestStreets.size(); j++) {
					// füge alle besten endständigen Edges hinzu
					if (longestStreets.get(j) == gV) {
						bestEdges.add(currEndingStreets.get(j));
					}
				}
				currEndingStreets.clear();
			}
			// berechne gültige Nachbarn für die besten Edges

			Edge[] currEdges;
			int[] currCoords;
			for (int j = 0; j < bestEdges.size(); j++) {
				String id = bestEdges.get(j).getEdgeID();
				currCoords = HexService.getEdgeCoordinates(id.substring(0, 1), id.substring(1, 2));
				currEdges = aai.getGl().getBoard().getLinkedEdges(currCoords[0], currCoords[1], currCoords[2]);
				for (int k = 0; k < currEdges.length; k++) {
					if (currEdges[k] != null && !currStreetSet.getEdges().contains(currEdges[k])
							&& currEdges[k].getOwnerID() == null) {
						bestNeighbours.get(i).add(currEdges[k]);
					}
				}
			}
			// Speichere Index i, falls die längste Straße des Spielers in
			// aktuellem
			// Street Set ist.
			if (gV > maxIndex[1]) {
				maxIndex[0] = i;
				maxIndex[1] = gV;
			}
		}
		ArrayList<Object> result = new ArrayList<Object>();
		result.add(myStreetSets.get(maxIndex[0]));
		result.add(bestNeighbours.get(maxIndex[0]));

		return result;
	}

	/**
	 * recursive method for calculating the longest route starting from an
	 * specific edge.
	 *
	 * @param edge
	 *            starting street
	 * @param currStreetSet
	 *            street set which contains the street
	 * @param alreadyChecked
	 *            streets which are already counted
	 * @param lastNeighbours
	 *            neighbours, which are prohibited to check
	 * @return longest rout of not checked streets
	 */
	private Integer depthFirstSearch(Edge edge, StreetSet currStreetSet, ArrayList<Edge> alreadyChecked,
			ArrayList<Edge> lastNeighbours) {
		int[] coord = HexService.getEdgeCoordinates(edge.getEdgeID().substring(0, 1), edge.getEdgeID().substring(1, 2));
		Edge[] neighbours = aai.getGl().getBoard().getLinkedEdges(coord[0], coord[1], coord[2]);
		ArrayList<Edge> aC = new ArrayList<Edge>(alreadyChecked);
		aC.add(edge);
		ArrayList<Edge> validNeighbours = new ArrayList<Edge>();
		for (int i = 0; i < neighbours.length; i++) {
			if (currStreetSet.getEdges().contains(neighbours[i]) && !aC.contains(neighbours[i])
					&& !lastNeighbours.contains(neighbours[i])) {
				validNeighbours.add(neighbours[i]);
			}
		}
		int greatestValue = 0;
		for (int i = 0; i < validNeighbours.size(); i++) {
			int currSize = 1 + depthFirstSearch(validNeighbours.get(i), currStreetSet, aC, validNeighbours);
			if (currSize > greatestValue) {
				greatestValue = currSize;
			}
		}
		return greatestValue;
	}

	public Corner getBestVillage() {
		ArrayList<Corner> validPositions = getPossibleVillages();
		int max = Integer.MIN_VALUE;
		int currVal;
		int maxIndex = 0;
		for (int i = 0; i < validPositions.size(); i++) {

			currVal = aai.getCornerAgentByID(validPositions.get(i).getCornerID()).calculateInitialVillageUtility();
			if (currVal > max) {
				max = currVal;
				maxIndex = i;
			}
		}
		if (max == Integer.MIN_VALUE) {
			return null;
		} else {
			return validPositions.get(maxIndex);
		}
	}

	private ArrayList<Corner> getPossibleVillages() {
		ArrayList<Corner> allCorners = new ArrayList<Corner>();
		StreetSet currStreetSet;
		Corner[] currNeighbours;
		int[] currCoords;
		for (int i = 0; i < myStreetSets.size(); i++) {
			currStreetSet = myStreetSets.get(i);
			for (int j = 0; j < currStreetSet.size(); j++) {
				String id = currStreetSet.getEdgeAt(j).getEdgeID();
				currCoords = HexService.getEdgeCoordinates(id.substring(0, 1), id.substring(1, 2));
				currNeighbours = aai.getGl().getBoard().getAttachedCorners(currCoords[0], currCoords[1], currCoords[2]);
				for (int k = 0; k < currNeighbours.length; k++) {
					if (currNeighbours[k] != null && currNeighbours[k].getStatus() == CornerStatus.EMPTY
							&& !allCorners.contains(currNeighbours[k])) {
						allCorners.add(currNeighbours[k]);
					}
				}
			}
		}
		return allCorners;
	}

	public int getCurrentBuyingFocus() {
		return currentBuyingFocus;
	}

	public int[] getOwnResources() {
		return ownResources;
	}

	public ResourceType getLowestResource() {
		//importance (ORE -> CORN -> SHEEP -> CLAY -> WOOD)
		// (2,4,3,1,0)
		int[] priority = {2,4,3,1,0};
		
		int min = ownResources[priority[0]];
		int c = priority[0];
		for (int i = 0; i < ownResources.length; i++) {
			if (ownResources[priority[i]] < min) {
				min = ownResources[priority[i]];
				c = priority[i];
			}
		}
		// {WOOD, CLAY, ORE, SHEEP, CORN}
		switch (c) {
		case 0:
			return ResourceType.WOOD;
		case 1:
			return ResourceType.CLAY;
		case 2:
			return ResourceType.ORE;
		case 3:
			return ResourceType.SHEEP;
		case 4:
			return ResourceType.CORN;
		default:
			throw new IllegalArgumentException("Resource " + c + " doesn't exist");
		}
	}

}
