package ai.agents;

import java.util.ArrayList;
import java.util.HashMap;

import ai.AdvancedAI;
import model.objects.Corner;
import model.objects.Edge;
import settings.DefaultSettings;

/**
 * Tracks own resources and sees what AI can purchase at any given time.
 */
public class ResourceAgent {
	private AdvancedAI aai;

	private int[] ownResources = aai.getMe().getResources();
	private int currentBuyingFocus; // 0 = Street; 1 = Village; 2 = City; 3 =
									// DevCard
	private ArrayList<Corner> myCorners = new ArrayList<Corner>();
	private ArrayList<Edge> myEdges = new ArrayList<Edge>();
	
	public final static HashMap<Integer, int[]> buildingCosts = new HashMap<Integer, int[]>() {
		{
			put(0, DefaultSettings.STREET_BUILD_COST);
			put(1, DefaultSettings.VILLAGE_BUILD_COST);
			put(2, DefaultSettings.CITY_BUILD_COST);
			put(3, DefaultSettings.DEVCARD_BUILD_COST);
		}
	};

	/*
	 * private boolean canBuildStreet; private boolean canBuildVillage; private
	 * boolean canBuildCity;
	 */
	// {THREE_TO_ONE, WOOD, CLAY, ORE, SHEEP, CORN}
	// maybe in trading agent?
	private boolean[] harbours = { false, false, false, false, false, false };

	public ResourceAgent(AdvancedAI ai) {
		this.aai = ai;
	}

	/**
	 * calculates the cards to give to the robber
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
	 * gets possible buys with current resourceCards
	 * 
	 * @return boolean list (Street,Village,City,DevCard)
	 */
	public boolean[] getPossibleBuildings() {
		boolean[] results = new boolean[4];
		if (compareResources(ownResources, DefaultSettings.STREET_BUILD_COST)) {
			results[1] = true;
		}
		if (compareResources(ownResources, DefaultSettings.VILLAGE_BUILD_COST)) {
			results[2] = true;
		}
		if (compareResources(ownResources, DefaultSettings.CITY_BUILD_COST)) {
			results[3] = true;
		}
		if (compareResources(ownResources, DefaultSettings.DEVCARD_BUILD_COST)) {
			results[4] = true;
		}
		return results;
	}

	/**
	 * returns the building type with the lowest resource difference to
	 * playersResources
	 * 
	 * @return
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
	 * returns the resources missing for building a certain type
	 * 
	 * @param bType
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
	 * compares two resource arrays
	 * 
	 * @param playerResources
	 * @param resource
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

	private int sumArray(int[] array) {
		int result = 0;
		for (int i = 0; i < array.length; i++) {
			result += array[i];
		}
		return result;
	}

	public void add(Corner cornerAt) {
		myCorners.add(cornerAt);
	}
	
	public void add(Edge edgeAt){
		myEdges.add(edgeAt);
	}

}
