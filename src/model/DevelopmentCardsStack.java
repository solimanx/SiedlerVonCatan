package model;

import model.objects.DevCards.DevCardFactory;
import model.objects.DevCards.DevelopmentCard;

public class DevelopmentCardsStack {

	DevCardFactory devFactory;
	DevelopmentCard[] devCardStack;
	int nextCard = 0;

	public DevelopmentCardsStack() {
		devFactory = new DevCardFactory();
		devCardStack = new DevelopmentCard[25];
		// TODO random order
		int i = 0;
		for (int j = 0; j < 14; j++) {
			devCardStack[i] = devFactory.createDevelopmentCard("knightCard");
			i++;
		}
		for (int j = 0; j < 5; j++) {
			devCardStack[i] = devFactory.createDevelopmentCard("victoryCard");
			i++;
		}
		for (int j = 0; j < 2; j++) {
			devCardStack[i] = devFactory.createDevelopmentCard("monopolyCard");
			i++;
		}
		for (int j = 0; j < 2; j++) {
			devCardStack[i] = devFactory.createDevelopmentCard("streetBuilding");
			i++;
		}
		for (int j = 0; j < 2; j++) {
			devCardStack[i] = devFactory.createDevelopmentCard("inventionCard");
			i++;
		}
	}

	public DevelopmentCard[] getCardStack() {
		return devCardStack;
	}

	public DevelopmentCard getNextCard() {
		if (nextCard < 25) {
			nextCard++;
			return devCardStack[nextCard];
		}
		return null;
	}

}
