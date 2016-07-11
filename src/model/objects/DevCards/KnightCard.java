package model.objects.DevCards;

import enums.CardType;

// TODO: Auto-generated Javadoc
public class KnightCard implements DevelopmentCard {

	private String name = "Knight Card";
	private String text = "";
	private CardType cardType = CardType.KNIGHT;

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
	
	/* (non-Javadoc)
	 * @see model.objects.DevCards.DevelopmentCard#getCardType()
	 */
	public CardType getCardType(){
		return cardType;
	}

}
