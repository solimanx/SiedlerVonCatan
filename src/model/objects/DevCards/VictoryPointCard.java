package model.objects.DevCards;

import enums.CardType;

// TODO: Auto-generated Javadoc
public class VictoryPointCard implements DevelopmentCard {

	private String name = "Victory Card";
	private String text = "";
	private int victoryPoints = 1;
	private CardType cardType = CardType.VICTORYPOINT;

	/* (non-Javadoc)
	 * @see model.objects.DevCards.DevelopmentCard#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	/* (non-Javadoc)
	 * @see model.objects.DevCards.DevelopmentCard#getText()
	 */
	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return text;
	}

	/**
	 * Gets the victorypoints.
	 *
	 * @return the victorypoints
	 */
	public int getVictorypoints() {
		return victoryPoints;
	}
	
	public CardType getCardType(){
		return cardType;
	}

}
