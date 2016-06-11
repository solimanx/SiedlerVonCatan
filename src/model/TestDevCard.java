package model;

import java.util.Stack;

public class TestDevCard {
	
	public static void main(String[] Args) {
		DevelopmentCardsStack devStack = new DevelopmentCardsStack();
		for(int i = 0; i< 25; i++){
			System.out.println(i + " " + devStack.getNextCard().getName());
		}
	}
}
