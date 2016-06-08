package model;

import model.objects.DevCards.DevCardFactory;
import model.objects.DevCards.DevelopmentCard;

public class DevelopmentCardsStack {
	
	DevCardFactory devFactory;
	DevelopmentCard[] devCardStack;
	int nextCard = 0;
	
	public DevelopmentCardsStack(){
		devFactory = new DevCardFactory();
		devCardStack = new DevelopmentCard[25];
		//TODO insert all card types, random order
		for(int i = 0; i<25; i++){
			devCardStack[i] = devFactory.createDevelopmentCard("knightCard");
		}
	}
	
	public DevelopmentCard[] getCardStack(){
		return devCardStack;
	}
	
	public DevelopmentCard getNextCard(){
		if(nextCard < 25){
			return devCardStack[nextCard];
		}
		//TODO generate new DevCardStack and get first Card
//		else{
//			newStack().getNextCard;
//		}
		return null;
	}
	
	
}
