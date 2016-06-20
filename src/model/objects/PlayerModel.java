package model.objects;

import java.util.ArrayList;

import enums.ResourceType;
import model.objects.DevCards.DevelopmentCard;
import enums.Color;
import enums.HarbourStatus;
//TODO import DevelopmentCard enum
import enums.PlayerState;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import settings.DefaultSettings;

public class PlayerModel {
	private static Logger logger = LogManager.getLogger(PlayerModel.class.getSimpleName());

	private int victoryPoints = 0;

	// TODO int hiddenVictoryPoints
	private int amountVillages = DefaultSettings.START_AMOUNT_VILLAGES;
	private int amountCities = DefaultSettings.START_AMOUNT_CITIES;
	private int amountStreets = DefaultSettings.START_AMOUNT_STREETS;
	private boolean hasLongestRoad;
	private boolean hasLargestArmy;
	private PlayerState playerState;
	private int playerID;
	private Color color;
	private String name;
	private int playedKnightCards;
	private boolean hasPlayedDevCard = false;
	private ArrayList<DevelopmentCard> devCardsBoughtInThisRound;
	private ArrayList<HarbourStatus> harbours = new ArrayList<HarbourStatus>();

	// {KnightCard, VictoryCard, InventionCard, MonopolyCard, StreetBuilding}
	private int[] playerDevCard = { 0, 0, 0, 0, 0 };

	// Amount of Landscape Resource Cards: {WOOD, CLAY, ORE, SHEEP, CORN}
	// is hidden to other players
	private int[] resources = { 0, 0, 0, 0, 0 };

	// is null for self, but non null for other players
	private Integer hiddenResources = 0;

	public PlayerModel(int id) {
		this.playerID = id;
		this.playedKnightCards = 0;
		// this.resourceCards = new ArrayList<ResourceType>();
	}

	/**
	 * returns the actual amount of playerDevCards {KnightCard, VictoryCard,
	 * InventionCard, MonopolyCard, StreetBuilding}
	 *
	 * @return
	 */
	public int[] getPlayerDevCards() {
		return playerDevCard;
	}

	/**
	 * sets the playerDevCards new {KnightCard, VictoryCard, InventionCard,
	 * MonopolyCard, StreetBuilding}
	 *
	 * @param devCards
	 */
	public void setPlayerDevCards(int[] devCards) {
		if (devCards.length != 5) {
			throw new IllegalArgumentException("setPlayerDevCard Illegal Argument lenght");
		}
		playerDevCard = devCards;
	}

	/**
	 * increments the amount of the card
	 *
	 * @param devCard
	 *            DevelopmentCard
	 */
	public void incrementPlayerDevCard(DevelopmentCard devCard) {
		switch (devCard.getName()) {
		case "Knight Card":
			playerDevCard[0]++;
			break;
		case "Victory Card":
			playerDevCard[1]++;
			break;
		case "Invention Card":
			playerDevCard[2]++;
			break;
		case "Monopoly Card":
			playerDevCard[3]++;
			break;
		case "Street Building Card":
			playerDevCard[4]++;
			break;
		default:
			throw new IllegalArgumentException("Card Type doesn't exist");
		}
	}

	/**
	 * decrements the specific playerDevCards by 1
	 *
	 * @param devCard
	 *            specific DevelopmentCard
	 */
	public void decrementPlayerDevCard(DevelopmentCard devCard) {
		if (devCard.getName() == "Knight Card") {
			if (playerDevCard[0] != 0) {
				playerDevCard[0] = playerDevCard[0] - 1;
			} else {
				throw new IllegalArgumentException("not enough " + devCard.getName() + "s");
			}
		}
		if (devCard.getName() == "Victory Card") {
			if (playerDevCard[1] != 0) {
				playerDevCard[1] = playerDevCard[1] - 1;
			} else {
				throw new IllegalArgumentException("not enough " + devCard.getName() + "s");
			}
		}
		if (devCard.getName() == "Invention Card") {
			if (playerDevCard[2] != 0) {
				playerDevCard[2] = playerDevCard[2] - 1;
			} else {
				throw new IllegalArgumentException("not enough " + devCard.getName() + "s");
			}
		}
		if (devCard.getName() == "Monopoly Card") {
			if (playerDevCard[3] != 0) {
				playerDevCard[3] = playerDevCard[3] - 1;
			} else {
				throw new IllegalArgumentException("not enough " + devCard.getName() + "s");
			}
		}
		if (devCard.getName() == "Street Building Card") {
			if (playerDevCard[4] != 0) {
				playerDevCard[4] = playerDevCard[4] - 1;
			} else {
				throw new IllegalArgumentException("not enough " + devCard.getName() + "s");
			}
		}
	}

	public int getPlayedKnightCards() {
		return playedKnightCards;
	}

	public void incrementPlayedKnightCards() {
		playedKnightCards++;
	}

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

	public void setResources(int[] resources) {
		this.resources = resources;
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
			logger.catching(Level.FATAL, new IllegalArgumentException("Invalid resources object"));
	}

	/**
	 * @param resources
	 *            the resources to set
	 */
	public void incrementResources(int[] resources) {
		if (resources.length == 5) {
			for (int i = 0; i < 5; i++)
				this.resources[i] += resources[i];
		} else {
			logger.catching(Level.FATAL, new IllegalArgumentException("Invalid resources object"));

		}

	}

	/**
	 * Get specific resource
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

	/**
	 * Get all resources
	 *
	 * @return
	 */
	public int[] getResources() {
		// TODO Auto-generated method stub
		return resources;
	}

	/**
	 * @return the hiddenResources
	 */
	public Integer getHiddenResources() {
		return hiddenResources;
	}

	/**
	 * @param hiddenResources
	 *            the hiddenResources to set
	 */
	public void setHiddenResources(Integer hiddenResources) {
		this.hiddenResources = hiddenResources;
	}

	public void incrementHiddenResources(int increment) {
		this.hiddenResources += increment;
	}

	public void decrementHiddenResources(int decrement) {
		this.hiddenResources -= decrement;
	}

	// For server Only
	//

	public int sumResources() {
		return resources[0] + resources[1] + resources[2] + resources[3] + resources[4];
	}

	public ArrayList<DevelopmentCard> getDevCardsBoughtInThisRound() {
		return devCardsBoughtInThisRound;
	}

	public void setHasPlayedDevCard(boolean flag) {
		hasPlayedDevCard = flag;

	}

	public boolean hasPlayedDevCard() {
		return hasPlayedDevCard;
	}

	public void addToPlayerHarbours(HarbourStatus harbour) {
		harbours.add(harbour);
	}

	public ArrayList<HarbourStatus> getPlayerHarbours() {
		return harbours;
	}

}
