package model;

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
	
	
	
	public int getAmountStreets() {		
		return amountStreets;
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
	
}
