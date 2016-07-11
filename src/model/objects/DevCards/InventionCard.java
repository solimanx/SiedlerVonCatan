package model.objects.DevCards;

import enums.CardType;

// TODO: Auto-generated Javadoc
public class InventionCard implements DevelopmentCard {

	private String name = "Invention Card";
	private String text = "";
	private CardType cardType;

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
	
	public CardType getCardType(){
		return cardType;
	}

}
