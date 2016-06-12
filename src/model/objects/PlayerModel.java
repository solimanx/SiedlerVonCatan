package model.objects;

import java.util.ArrayList;

import enums.ResourceType;
import enums.Color;
//TODO import DevelopmentCard enum
import enums.PlayerState;
import settings.DefaultSettings;

public class PlayerModel {
	// moved to line 30: private int[] resources
	// ArrayList<ResourceType> resourceCards;
	// TODO ArrayList<DevelopmentCard> developmentCards;
	int victoryPoints = 0;

	// TODO int hiddenVictoryPoints
	int amountVillages = DefaultSettings.START_AMOUNT_VILLAGES;
	int amountCities = DefaultSettings.START_AMOUNT_CITIES;
	int amountStreets = DefaultSettings.START_AMOUNT_STREETS;
	boolean hasLongestRoad;
	boolean hasLargestArmy;
	PlayerState playerState;
	int playerID;
	Color color;
	String name;

	
	// Amount of Landscape Resource Cards: {WOOD, CLAY, ORE, SHEEP, CORN,
	private int[] resources = { 0, 0, 0, 0, 0 };

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// every Player gets own id (0..3)
	public PlayerModel(int id) {
		this.playerID = id;
		//this.resourceCards = new ArrayList<ResourceType>();
	}

	public int getID() {
		return playerID;
	}

	public int getAmountStreets() {
		return amountStreets;
	}

	public void decreaseAmountStreets() {
		amountStreets--;
	}

	public void decreaseAmountVillages() {
		amountVillages--;
	}

	public void increaseAmountVillages() {
		amountVillages++;
	}

	public void decreaseAmountCities() {
		amountCities--;
	}

	@Deprecated
	public ArrayList<ResourceType> getResourceCards() {
		return null;
		//return resourceCards;
	}

	public int getAmountCities() {
		return amountCities;
	}

	public int getAmountVillages() {
		return amountVillages;
	}

	public int getVictoryPoints() {
		return victoryPoints;
	}

	public void setVictoryPoints(int victoryPoints) {
		this.victoryPoints = victoryPoints;
	}

	public boolean hasLongestRoad() {
		return hasLongestRoad;
	}

	public void setHasLongestRoad(boolean hasLongestRoad) {
		this.hasLongestRoad = hasLongestRoad;
	}

	public boolean hasLargestArmy() {
		return hasLargestArmy;
	}

	public void setHasLargestArmy(boolean hasLargestArmy) {
		this.hasLargestArmy = hasLargestArmy;
	}

	public PlayerState getPlayerState() {
		return playerState;
	}

	public void setPlayerState(PlayerState playerState) {
		this.playerState = playerState;
	}

	@Deprecated
	public void setResourceCards(ArrayList<ResourceType> resourceCards) {
		//this.resourceCards = resourceCards;
	}

	public void setAmountVillages(int amountVillages) {
		this.amountVillages = amountVillages;
	}

	public void setAmountCities(int amountCities) {
		this.amountCities = amountCities;
	}

	public void setAmountStreets(int amountStreets) {
		this.amountStreets = amountStreets;
	}

	// AI DEBUGGING

	/**
	 * @return the resources
	 */
	public void decrementResources(int[] resources) {
		if (resources.length == 5) {
			for (int i = 0; i < 5; i++)
				this.resources[i] -= resources[i];
		} else
			throw new IllegalArgumentException("Invalid resources object");
	}

	/**
	 * @param resources
	 *            the resources to set
	 */
	public void incrementResources(int[] resources) {
		if (resources.length == 5) {
			for (int i = 0; i < 5; i++)
				this.resources[i] += resources[i];
		} else
			throw new IllegalArgumentException("Invalid resources object");

	}

	/**
	 * Get all resources
	 * 
	 * @return
	 */
	public int getResourceAmountOf(int i) {
		// 0=WOOD, 1=CLAY, 2=ORE, 3=SHEEP, 4=CORN
		return resources[i];
	}

	public void decrementResourceAt(int i) {
		resources[i] -= 1;

	}

}
