package ai.agents;

import java.util.ArrayList;

import ai.AdvancedAI;
import enums.ResourceType;
import model.board.Edge;
import model.unit.StreetSet;
import service.ProtocolToModel;

// TODO: Auto-generated Javadoc
/**
 * Tracks all cards, and what strategy to use when having the cards, or seeing
 * the cards getting purchased.
 */
public class CardAgent {
	// {KNIGHT, INVENTION, MONOPOLY, ROADBUILDING}
	private int[] myCards = { 0, 0, 0, 0 };
	private AdvancedAI aai;
	private int amountOfBoughtDevCards = 0;

	/**
	 * Instantiates a new card agent.
	 *
	 * @param aai
	 *            the AdvancedAi
	 */
	public CardAgent(AdvancedAI aai) {
		this.aai = aai;
	}

	/**
	 * Strategy for playing the cards.
	 */
	public void playCardStrategy() {

	}

	/**
	 * Update cards in the model.
	 */
	public void updateCards() {
		// {KNIGHT, VICTORY, INVENTION , MONOPOLY, ROAD}
		int[] cards = aai.getMe().getPlayerDevCards();
		myCards = new int[] { 0, 0, 0, 0 };
		myCards[0] = cards[0];
		myCards[1] = cards[2];
		myCards[2] = cards[3];
		myCards[3] = cards[4];

	}

	/**
	 * Checks for knight card.
	 *
	 * @return true, if successful
	 */
	public boolean hasKnight() {
		return myCards[0] > 0;
	}

	/**
	 * Checks for invention card.
	 *
	 * @return true, if successful
	 */
	public boolean hasInvention() {
		return myCards[1] > 0;
	}

	/**
	 * Checks for monopoly card.
	 *
	 * @return true, if successful
	 */
	public boolean hasMonopoly() {
		return myCards[2] > 0;
	}

	/**
	 * Checks for road building card.
	 *
	 * @return true, if successful
	 */
	public boolean hasRoad() {
		return myCards[3] > 0;
	}

	/**
	 * Returns the sum of all cards.
	 * 
	 * @return sum
	 */
	public int getSum() {
		return myCards[0] + myCards[1] + myCards[2] + myCards[3];
	}

	/**
	 * Play monopoly card.
	 */
	public void playMonopolyCard() {
		// TODO DEBUG ONLY
		ResourceType rt = (aai.getResourceAgent().getLowestResource(null));
		aai.playMonopolyCard(rt);

	}

	/**
	 * Play knight card.
	 */
	public void playKnightCard() {
		// TODO Auto-generated method stub

	}

	/**
	 * Play invention card.
	 */
	public void playInventionCard() {
		// TODO DEBUG ONLY
		ResourceType rt = (aai.getResourceAgent().getLowestResource(null));
		ResourceType rt2 = (aai.getResourceAgent().getLowestResource(rt));
		aai.playInventionCard(rt, rt2);
	}

	/**
	 * Play road card.
	 */
	public void playRoadCard() {
		Edge e1 = aai.getResourceAgent().calculateBestStreet();
		int[] coords1 = ProtocolToModel.getEdgeCoordinates(e1.getEdgeID());
		aai.getResourceAgent().addToOwnStreetSet(e1);
		int[] coords2 = null;
		if (aai.getMe().getAmountStreets() > 1) {
			Edge e2 = aai.getResourceAgent().calculateBestStreet();
			coords2 = ProtocolToModel.getEdgeCoordinates(e2.getEdgeID());
		}

		ArrayList<StreetSet> myStreetSets = aai.getResourceAgent().getMyStreetSets();

		for (int i = 0; i < myStreetSets.size(); i++) {
			if (myStreetSets.get(i).getEdges().contains(e1)) {
				myStreetSets.get(i).getEdges().remove(e1);
				break;
			}
		}

		aai.playStreetCard(coords1, coords2);

	}
	
	/**
	 * Increase amount of bought dev cards.
	 */
	public void increaseAmountOfBoughtDevCards(){
		this.amountOfBoughtDevCards++;
	}

	/**
	 * Gets the amount of bought dev cards.
	 *
	 * @return the amount of bought dev cards
	 */
	public int getAmountOfBoughtDevCards() {
		return amountOfBoughtDevCards;
	}

}
