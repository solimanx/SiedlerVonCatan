package model;

public interface BoardInterface {
	
	public Field[] getSurroundingF(Field f);
	public Field[]  getSurroundingF(Corner c);
	public Field[] getSurroundingF(Edge e);
	
	public Corner[] getSurroundingC(Field f);
	public Corner[] getSurroundingC(Corner c);
	public Corner[] getSurroundingC(Edge e);
	
	public Edge[] getSurroundingE(Field f);
	public Edge[] getSurroundingE(Corner c);
	public Edge[] getSurroundingE(Edge e);
	
	public int[] convertToCube(int[] a); //required for initialize Board (diceNumbers)
	public int[] convertToAxial(int[] c);
	
	public Field[] getSpiral(Field f); //out -> in
	
	public Field[][][] getFields();
	public Corner[][][] getCorners();
	public Edge[][][] getEdges();
	public PlayerModel[] getPlayerModels();
	
	public Field getBandit();
	public void setBandit(Field f);

}
