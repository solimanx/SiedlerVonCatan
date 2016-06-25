package ai.agents;

import ai.AdvancedAI;
import enums.CardType;

/**
 * Tracks all cards, and what strategy to use when having the cards, or seeing
 * the cards getting purchased.
 */
public class CardAgent {
	// {KNIGHT, INVENTION, MONOPOLY, ROADBUILDING}
	private int[] myCards = { 0, 0, 0, 0 };
	private AdvancedAI aai;

	public CardAgent(AdvancedAI aai) {
		this.aai = aai;
	}

	public void updateCards() {
		// {KNIGHT, VICTORY, INVENTION , MONOPOLY, ROAD}
		int[] cards = aai.getMe().getPlayerDevCards();
		myCards[0] = cards[0];
		myCards[1] = cards[2];
		myCards[2] = cards[3];
		myCards[3] = cards[4];

	}
}
