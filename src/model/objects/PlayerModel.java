package model.objects;

import java.util.ArrayList;

import enums.ResourceType;
//TODO import DevelopmentCard enum
import enums.PlayerState;
import settings.DefaultSettings;

public class PlayerModel {
	ArrayList<ResourceType> resourceCards;
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

	// every Player gets own id (1..4)
	public PlayerModel(int id) {
		this.playerID = id;
		this.resourceCards = new ArrayList<ResourceType>();
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

	public ArrayList<ResourceType> getResourceCards() {
		return resourceCards;
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

	public void setResourceCards(ArrayList<ResourceType> resourceCards) {
		this.resourceCards = resourceCards;
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

}
