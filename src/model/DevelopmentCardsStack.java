package model;

import model.objects.DevCards.DevCardFactory;
import model.objects.DevCards.DevelopmentCard;
import settings.DefaultSettings;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DevelopmentCardsStack {

	private DevCardFactory devFactory;
	private DevelopmentCard[] devCardStack;
	private int nextCard;

	/**
	 * constructor sets standard values creates a stack of DevelopmentCards
	 */
	public DevelopmentCardsStack() {
		devFactory = new DevCardFactory();
		devCardStack = new DevelopmentCard[DefaultSettings.AMOUNT_DEVELOPMENT_CARDS];
		nextCard = 0;
		// TODO random order
		int arrayPosition = 0;
		for (int j = 0; j < DefaultSettings.AMOUNT_KNIGHT_CARDS; j++) {
			devCardStack[arrayPosition] = devFactory.createDevelopmentCard("knightCard");
			arrayPosition++;
		}
		for (int j = 0; j < DefaultSettings.AMOUNT_VICTORY_CARDS; j++) {
			devCardStack[arrayPosition] = devFactory.createDevelopmentCard("victoryCard");
			arrayPosition++;
		}
		for (int j = 0; j < DefaultSettings.AMOUNT_MONOPOLY_CARDS; j++) {
			devCardStack[arrayPosition] = devFactory.createDevelopmentCard("monopolyCard");
			arrayPosition++;
		}
		for (int j = 0; j < DefaultSettings.AMOUNT_STREETBUILDING_CARDS; j++) {
			devCardStack[arrayPosition] = devFactory.createDevelopmentCard("streetBuilding");
			arrayPosition++;
		}
		for (int j = 0; j < DefaultSettings.AMOUNT_INVENTION_CARDS; j++) {
			devCardStack[arrayPosition] = devFactory.createDevelopmentCard("inventionCard");
			arrayPosition++;
		}
		shuffleArray(devCardStack);
	}

	/**
	 * randomizes the order of the DevelopmentCards in the array
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
	 * getter
	 *
	 * @return returns the whole DevelopmentCardStack, as if there was no card
	 *         drawn
	 */
	public DevelopmentCard[] getCardStack() {
		return devCardStack;
	}
	
	//Debug only!!
	public void setCardStack(DevelopmentCard[] cards){
		this.devCardStack = cards;
	}

	/**
	 * returns the next DevelopmentCard from the stack
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

	public boolean buyable() {
		if (nextCard < DefaultSettings.AMOUNT_DEVELOPMENT_CARDS) {
			return true;
		}
		return false;
	}

}
