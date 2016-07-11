package model.objects.DevCards;

import enums.CardType;

// TODO: Auto-generated Javadoc
public interface DevelopmentCard {
	

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName();

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText();
	
	public CardType getCardType();

}
