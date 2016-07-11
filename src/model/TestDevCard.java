package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import enums.CardType;

import java.util.Stack;

public class TestDevCard {

//TestClass
	public static void main(String[] Args) {
		DevelopmentCardsStack devStack = new DevelopmentCardsStack();
		for (int i = 0; i < 15; i++) {
			System.out.println(i + " " + devStack.getNextCard().getName());
		}
		System.out.println("Specific Card " + devStack.getSpecificCard(CardType.STREET).getName());
		for (int i = 16; i < 25; i++) {
			System.out.println(i + " " + devStack.getNextCard().getName());
		}
	}
}
