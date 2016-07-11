package model;

import enums.CardType;
import model.objects.DevCards.DevelopmentCard;
import model.objects.DevCards.InventionCard;
import model.objects.DevCards.KnightCard;
import model.objects.DevCards.MonopolyCard;

public class TestDevCard {

//TestClass
	public static void main(String[] Args) {
//		DevelopmentCardsStack devStack = new DevelopmentCardsStack();
//		for (int i = 0; i < 15; i++) {
//			System.out.println(i + " " + devStack.getNextCard().getName());
//		}
//		System.out.println("Specific Card " + devStack.getSpecificCard(CardType.STREET).getName());
//		for (int i = 16; i < 25; i++) {
//			System.out.println(i + " " + devStack.getNextCard().getName());
//		}
		DevelopmentCard[] devArray = {new KnightCard(), new KnightCard(), new MonopolyCard(), new InventionCard()};
		DevelopmentCardsStack devStack2 = new DevelopmentCardsStack(devArray);
		devStack2.getSpecificCard(CardType.INVENTION);
		for (int i=0; i<devArray.length; i++){
			System.out.println(i + " " + devStack2.getCardStack()[i].getName());
		}
		
	}
}
