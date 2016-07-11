package model.objects.DevCards;

import settings.DefaultSettings;

// TODO: Auto-generated Javadoc
public class DevCardFactory {

	int knightCards = DefaultSettings.amountKnightCards;
	int victoryCards = DefaultSettings.amountVictoryCards;
	int monopolyCards = DefaultSettings.amountMonopolyCards;
	int buildStreetCards = DefaultSettings.amountStreetBuildingCards;
	int inventionCards = DefaultSettings.amountInventionCards;

	/**
	 * Creates a new DevCard object.
	 *
	 * @param name the name
	 * @return the development card
	 */
	public DevelopmentCard createDevelopmentCard(String name) {
		switch (name) {
		case "knightCard":
			knightCards = decrementArgument(knightCards);
			return new KnightCard();
		case "victoryCard":
			victoryCards = decrementArgument(victoryCards);
			return new VictoryPointCard();
		case "monopolyCard":
			monopolyCards = decrementArgument(monopolyCards);
			return new MonopolyCard();
		case "streetBuilding":
			buildStreetCards = decrementArgument(buildStreetCards);
			return new StreetBuildingCard();
		case "inventionCard":
			inventionCards = decrementArgument(inventionCards);
			return new InventionCard();
		default:
			throw new IllegalArgumentException("Illegal Card Name");
		}
	}

	/**
	 * Decrement argument.
	 *
	 * @param a the a
	 * @return the int
	 */
	public int decrementArgument(int a) {
		if (a > 0) {
			return --a;
		} else {
			throw new IllegalArgumentException("Too many cards of this type");
		}
	}
}
