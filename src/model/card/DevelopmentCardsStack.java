package model.card;

import settings.DefaultSettings;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import enums.CardType;

// TODO: Auto-generated Javadoc
public class DevelopmentCardsStack {

	private DevCardFactory devFactory;
	private DevelopmentCard[] devCardStack;
	private int nextCard;

	/**
	 * Instantiates a new development cards stack.
	 *
	 * @param devCardStack the dev card stack
	 */
	public DevelopmentCardsStack(DevelopmentCard[] devCardStack){
		this.devCardStack = devCardStack;
		this.nextCard = 0;
	}
	/**
	 * constructor sets standard values creates a stack of DevelopmentCards.
	 */
	public DevelopmentCardsStack() {
		devFactory = new DevCardFactory();
		devCardStack = new DevelopmentCard[DefaultSettings.amountDevelopmentCards];
		nextCard = 0;
		// TODO random order
		int arrayPosition = 0;
		for (int j = 0; j < DefaultSettings.amountKnightCards; j++) {
			devCardStack[arrayPosition] = devFactory.createDevelopmentCard("knightCard");
			arrayPosition++;
		}
		for (int j = 0; j < DefaultSettings.amountVictoryCards; j++) {
			devCardStack[arrayPosition] = devFactory.createDevelopmentCard("victoryCard");
			arrayPosition++;
		}
		for (int j = 0; j < DefaultSettings.amountMonopolyCards; j++) {
			devCardStack[arrayPosition] = devFactory.createDevelopmentCard("monopolyCard");
			arrayPosition++;
		}
		for (int j = 0; j < DefaultSettings.amountStreetBuildingCards; j++) {
			devCardStack[arrayPosition] = devFactory.createDevelopmentCard("streetBuilding");
			arrayPosition++;
		}
		for (int j = 0; j < DefaultSettings.amountInventionCards; j++) {
			devCardStack[arrayPosition] = devFactory.createDevelopmentCard("inventionCard");
			arrayPosition++;
		}
		shuffleArray(devCardStack);
	}

	/**
	 * randomizes the order of the DevelopmentCards in the array.
	 *
	 * @param stack
	 *            Array of DevelopmentCards
	 * @return the same array with randomized order
	 */
	public DevelopmentCard[] shuffleArray(DevelopmentCard[] stack) {
		Random rnd = ThreadLocalRandom.current();
		for (int i = stack.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			DevelopmentCard a = stack[index];
			stack[index] = stack[i];
			stack[i] = a;
		}
		return stack;
	}

	/**
	 * getter.
	 *
	 * @return returns the whole DevelopmentCardStack, as if there was no card
	 *         drawn
	 */
	public DevelopmentCard[] getCardStack() {
		return devCardStack;
	}

	/**
	 * Sets the card stack.
	 *
	 * @param cards
	 *            the new card stack
	 */
	// Debug only!!
	public void setCardStack(DevelopmentCard[] cards) {
		this.devCardStack = cards;
	}

	/**
	 * returns the next DevelopmentCard from the stack.
	 *
	 * @return next card from the stack null if empty
	 */
	public DevelopmentCard getNextCard() {
		if (buyable()) {
			DevelopmentCard result = devCardStack[nextCard];
			nextCard++;
			return result;
		}
		return null;
	}

	// debug
	/**
	 * returns a card of the specific CardType.
	 *
	 * @param c the c
	 * @return the specific card
	 */
	public DevelopmentCard getSpecificCard(CardType c){
		if(!buyable()){
			return null;
		}
		
		int i = nextCard;
		boolean found = false;
		while(!found){
			if(i > devCardStack.length){
				return null;
			}
			if(devCardStack[i].getCardType().equals(c)){
				found = true;
			}
			else{
				i++;
			}
		}
		DevelopmentCard devCardFound = devCardStack[i];
		for(int j = i-nextCard; j>0; j--){
			devCardStack[j] = devCardStack[j-1];
		}
		devCardStack[nextCard] = devCardFound;
		
		DevelopmentCard result = devCardStack[nextCard];
		nextCard++;
		return result;
	}

	/**
	 * Buyable.
	 *
	 * @return true, if successful
	 */
	public boolean buyable() {
		if (nextCard < DefaultSettings.amountDevelopmentCards) {
			return true;
		}
		return false;
	}

}
