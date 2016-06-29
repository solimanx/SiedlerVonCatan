package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Stack;

public class TestDevCard {
	private static Logger logger = LogManager.getLogger(TestDevCard.class.getSimpleName());

	/**
	 * The main method.
	 *
	 * @param Args the arguments
	 */
	public static void main(String[] Args) {
		DevelopmentCardsStack devStack = new DevelopmentCardsStack();
		for (int i = 0; i < 25; i++) {
			System.out.println(i + " " + devStack.getNextCard().getName());
			logger.info("Karte " + i + devStack.getNextCard().getName());
		}
	}
}
