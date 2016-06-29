package model.objects.DevCards;

// TODO: Auto-generated Javadoc
public class VictoryPointCard implements DevelopmentCard {

	String name = "Victory Card";
	String text = "";
	int victoryPoints = 1;

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

}
