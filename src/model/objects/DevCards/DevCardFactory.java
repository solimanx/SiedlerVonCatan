package model.objects.DevCards;

import settings.DefaultSettings;

// TODO: Auto-generated Javadoc
public class DevCardFactory {

	int knightCards = DefaultSettings.AMOUNT_KNIGHT_CARDS;
	int victoryCards = DefaultSettings.AMOUNT_VICTORY_CARDS;
	int monopolyCards = DefaultSettings.AMOUNT_MONOPOLY_CARDS;
	int buildStreetCards = DefaultSettings.AMOUNT_STREETBUILDING_CARDS;
	int inventionCards = DefaultSettings.AMOUNT_INVENTION_CARDS;

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
