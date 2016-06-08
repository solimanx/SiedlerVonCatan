package model.objects.DevCards;


public class VictoryPointCard implements DevelopmentCard {
	
	String name = "Victory Card";
	String text = "";
	int victoryPoints = 1;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return text;
	}
	
	public int getVictorypoints(){
		return victoryPoints;
	}

}
