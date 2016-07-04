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

// TODO: Auto-generated Javadoc
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
	private String name = "";
	private int playedKnightCards;
	private boolean hasPlayedDevCard = false;
	private ArrayList<DevelopmentCard> devCardsBoughtInThisRound = new ArrayList<DevelopmentCard>();
	private ArrayList<HarbourStatus> harbours = new ArrayList<HarbourStatus>();

	// {KnightCard, VictoryCard, InventionCard, MonopolyCard, StreetBuilding}
	private int[] playerDevCard = { 0, 0, 0, 0, 0 };

	// Amount of Landscape Resource Cards: {WOOD, CLAY, ORE, SHEEP, CORN}
	// is hidden to other players
	private int[] resources = { 0, 0, 0, 0, 0 };

	// is null for self, but non null for other players
	private Integer hiddenResources = 0;

	/**
	 * Instantiates a new player model.
	 *
	 * @param id the id
	 */
	public PlayerModel(int id) {
		this.playerID = id;
		this.playedKnightCards = 0;
		// this.resourceCards = new ArrayList<ResourceType>();
	}

	/**
	 * returns the actual amount of playerDevCards {KnightCard, VictoryCard,
	 * InventionCard, MonopolyCard, StreetBuilding}.
	 *
	 * @return the player dev cards
	 */
	public int[] getPlayerDevCards() {
		return playerDevCard;
	}

	/**
	 * sets the playerDevCards new {KnightCard, VictoryCard, InventionCard,
	 * MonopolyCard, StreetBuilding}.
	 *
	 * @param devCards the new player dev cards
	 */
	public void setPlayerDevCards(int[] devCards) {
		if (devCards.length != 5) {
			throw new IllegalArgumentException("setPlayerDevCard Illegal Argument lenght");
		}
		playerDevCard = devCards;
	}

	/**
	 * increments the amount of the card.
	 *
	 * @param devCard            DevelopmentCard
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
	 * decrements the specific playerDevCards by 1.
	 *
	 * @param devCard            specific DevelopmentCard
	 */
	public void decrementPlayerDevCard(DevelopmentCard devCard) {
		if (devCard.getName() == "Knight Card") {
			if (playerDevCard[0] != 0) {
				playerDevCard[0] = playerDevCard[0] - 1;
			} else {
				//throw new IllegalArgumentException("not enough " + devCard.getName() + "s");
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

	/**
	 * Gets the played knight cards.
	 *
	 * @return the played knight cards
	 */
	public int getPlayedKnightCards() {
		return playedKnightCards;
	}

	/**
	 * Increment played knight cards.
	 */
	public void incrementPlayedKnightCards() {
		playedKnightCards++;
	}

	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the color.
	 *
	 * @param color the new color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	// every Player gets own id (0..3)

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getID() {
		return playerID;
	}
	
	/**
	 * Sets the id.
	 * AI Only!
	 */
	public void setID(int id) {
		this.playerID = id;
	}	

	/**
	 * Gets the amount streets.
	 *
	 * @return the amount streets
	 */
	public int getAmountStreets() {
		return amountStreets;
	}

	/**
	 * Decrease amount streets.
	 */
	public void decreaseAmountStreets() {
		amountStreets--;
	}

	/**
	 * Decrease amount villages.
	 */
	public void decreaseAmountVillages() {
		amountVillages--;
	}

	/**
	 * Increase amount villages.
	 */
	public void increaseAmountVillages() {
		amountVillages++;
	}

	/**
	 * Decrease amount cities.
	 */
	public void decreaseAmountCities() {
		amountCities--;
	}

	/**
	 * Gets the amount cities.
	 *
	 * @return the amount cities
	 */
	public int getAmountCities() {
		return amountCities;
	}

	/**
	 * Gets the amount villages.
	 *
	 * @return the amount villages
	 */
	public int getAmountVillages() {
		return amountVillages;
	}

	/**
	 * Gets the victory points.
	 *
	 * @return the victory points
	 */
	public int getVictoryPoints() {
		return victoryPoints;
	}

	/**
	 * Sets the victory points.
	 *
	 * @param victoryPoints the new victory points
	 */
	public void setVictoryPoints(int victoryPoints) {
		this.victoryPoints = victoryPoints;
	}

	/**
	 * Checks for longest road.
	 *
	 * @return true, if successful
	 */
	public boolean hasLongestRoad() {
		return hasLongestRoad;
	}

	/**
	 * Sets the checks for longest road.
	 *
	 * @param hasLongestRoad the new checks for longest road
	 */
	public void setHasLongestRoad(boolean hasLongestRoad) {
		this.hasLongestRoad = hasLongestRoad;
	}

	/**
	 * Checks for largest army.
	 *
	 * @return true, if successful
	 */
	public boolean hasLargestArmy() {
		return hasLargestArmy;
	}

	/**
	 * Sets the checks for largest army.
	 *
	 * @param hasLargestArmy the new checks for largest army
	 */
	public void setHasLargestArmy(boolean hasLargestArmy) {
		this.hasLargestArmy = hasLargestArmy;
	}

	/**
	 * Gets the player state.
	 *
	 * @return the player state
	 */
	public PlayerState getPlayerState() {
		return playerState;
	}

	/**
	 * Sets the player state.
	 *
	 * @param playerState the new player state
	 */
	public void setPlayerState(PlayerState playerState) {
		this.playerState = playerState;
	}

	/**
	 * Sets the resources.
	 *
	 * @param resources the new resources
	 */
	public void setResources(int[] resources) {
		this.resources = resources;
	}

	/**
	 * Sets the amount villages.
	 *
	 * @param amountVillages the new amount villages
	 */
	public void setAmountVillages(int amountVillages) {
		this.amountVillages = amountVillages;
	}

	/**
	 * Sets the amount cities.
	 *
	 * @param amountCities the new amount cities
	 */
	public void setAmountCities(int amountCities) {
		this.amountCities = amountCities;
	}

	/**
	 * Sets the amount streets.
	 *
	 * @param amountStreets the new amount streets
	 */
	public void setAmountStreets(int amountStreets) {
		this.amountStreets = amountStreets;
	}

	// AI DEBUGGING

	/**
	 * Decrement resources.
	 *
	 * @param resources the resources
	 * @return the resources
	 */
	public void decrementResources(int[] resources) {
		if (resources.length == 5) {
			for (int i = 0; i < 5; i++)
				this.resources[i] -= resources[i];
		} else if (resources.length == 1) {
			decrementHiddenResources(resources[0]);
		} else {
			logger.catching(Level.FATAL, new IllegalArgumentException("Invalid resources object"));
		}
	}

	/**
	 * Increment resources.
	 *
	 * @param resources            the resources to set
	 */
	public void incrementResources(int[] resources) {
		if (resources.length == 5) {
			for (int i = 0; i < 5; i++)
				this.resources[i] += resources[i];
		} else if (resources.length == 1) {
			incrementHiddenResources(resources[0]);
		} else {
			logger.catching(Level.FATAL, new IllegalArgumentException("Invalid resources object"));
		}

	}

	/**
	 * Get specific resource.
	 *
	 * @param i the i
	 * @return the resource amount of
	 */
	public int getResourceAmountOf(int i) {
		// 0=WOOD, 1=CLAY, 2=ORE, 3=SHEEP, 4=CORN
		return resources[i];
	}

	/**
	 * Decrement resource at.
	 *
	 * @param i the i
	 */
	public void decrementResourceAt(int i) {
		resources[i] -= 1;

	}

	/**
	 * Get all resources.
	 *
	 * @return the resources
	 */
	public int[] getResources() {
		// TODO Auto-generated method stub
		return resources;
	}

	/**
	 * Gets the hidden resources.
	 *
	 * @return the hiddenResources
	 */
	public Integer getHiddenResources() {
		return hiddenResources;
	}

	/**
	 * Sets the hidden resources.
	 *
	 * @param hiddenResources            the hiddenResources to set
	 */
	public void setHiddenResources(Integer hiddenResources) {
		this.hiddenResources = hiddenResources;
	}

	/**
	 * Increment hidden resources.
	 *
	 * @param increment the increment
	 */
	public void incrementHiddenResources(int increment) {
		this.hiddenResources += increment;
	}

	/**
	 * Decrement hidden resources.
	 *
	 * @param decrement the decrement
	 */
	public void decrementHiddenResources(int decrement) {
		this.hiddenResources -= decrement;
	}

	// For server Only
	//

	/**
	 * Sum resources.
	 *
	 * @return the int
	 */
	public int sumResources() {
		return resources[0] + resources[1] + resources[2] + resources[3] + resources[4];
	}

	/**
	 * Gets the dev cards bought in this round.
	 *
	 * @return the dev cards bought in this round
	 */
	public ArrayList<DevelopmentCard> getDevCardsBoughtInThisRound() {
		return devCardsBoughtInThisRound;
	}

	/**
	 * Sets the checks for played dev card.
	 *
	 * @param flag the new checks for played dev card
	 */
	public void setHasPlayedDevCard(boolean flag) {
		hasPlayedDevCard = flag;

	}

	/**
	 * Checks for played dev card.
	 *
	 * @return true, if successful
	 */
	public boolean hasPlayedDevCard() {
		return hasPlayedDevCard;
	}

	/**
	 * Adds the to player harbours.
	 *
	 * @param harbour the harbour
	 */
	public void addToPlayerHarbours(HarbourStatus harbour) {
		harbours.add(harbour);
	}

	/**
	 * Gets the player harbours.
	 *
	 * @return the player harbours
	 */
	public ArrayList<HarbourStatus> getPlayerHarbours() {
		return harbours;
	}

	/**
	 * Sum dev cards.
	 *
	 * @return the int
	 */
	public int sumDevCards() {
		return playerDevCard[0] + playerDevCard[1] + playerDevCard[2] + playerDevCard[3] + playerDevCard[4];
	}

}
