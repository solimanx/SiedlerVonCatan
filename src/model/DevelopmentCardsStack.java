package model;

import model.objects.DevCards.DevCardFactory;
import model.objects.DevCards.DevelopmentCard;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DevelopmentCardsStack {

	private DevCardFactory devFactory;
	private DevelopmentCard[] devCardStack;
	private int nextCard;

	public DevelopmentCardsStack() {
		devFactory = new DevCardFactory();
		devCardStack = new DevelopmentCard[25];
		nextCard = 0;
		// TODO random order
		int arrayPosition = 0;
		for (int j = 0; j < 14; j++) {
			devCardStack[arrayPosition] = devFactory.createDevelopmentCard("knightCard");
			arrayPosition++;
		}
		for (int j = 0; j < 5; j++) {
			devCardStack[arrayPosition] = devFactory.createDevelopmentCard("victoryCard");
			arrayPosition++;
		}
		for (int j = 0; j < 2; j++) {
			devCardStack[arrayPosition] = devFactory.createDevelopmentCard("monopolyCard");
			arrayPosition++;
		}
		for (int j = 0; j < 2; j++) {
			devCardStack[arrayPosition] = devFactory.createDevelopmentCard("streetBuilding");
			arrayPosition++;
		}
		for (int j = 0; j < 2; j++) {
			devCardStack[arrayPosition] = devFactory.createDevelopmentCard("inventionCard");
			arrayPosition++;
		}
		shuffleArray(devCardStack);
	    for (int i = 0; i < devCardStack.length; i++)
	    {
	      System.out.println(devCardStack[i].getName() + " ");
	    }
	}
	
	
	public DevelopmentCard[] shuffleArray(DevelopmentCard[] stack){
	    Random rnd = ThreadLocalRandom.current();
	    for (int i = stack.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      // Simple swap
	      DevelopmentCard a = stack[index];
	      stack[index] = stack[i];
	      stack[i] = a;
	    }
		return stack;
	}

	public DevelopmentCard[] getCardStack() {
		return devCardStack;
	}

	public DevelopmentCard getNextCard() {
		if (nextCard < 25) {
			DevelopmentCard result = devCardStack[nextCard];
			nextCard++;
			return result;
		}
		return null;
	}

}
