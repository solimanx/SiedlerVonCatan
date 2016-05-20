package model;
/**
 * Refer to hexagonrelations.png in trello
 * 
 *
 */
public interface BoardInterface {
    
	public Field getFieldAt(int aX, int aY);
	public Corner getCornerAt(int aX, int aY, int i);
	public Edge getEdgeAt(int aX, int aY, int i);
	
	public Field[] getNeighbouringFields(int aX, int aY);
	public Field[] getTouchingFields(int aX, int aY, int i);
	public Field[] getConnectedFields(int aX, int aY, int i);

	public Corner[] getSurroundingCorners(int aX, int aY);
	public Corner[] getAdjacentCorners(int aX, int aY, int i);
	public Corner[] getAttachedCorners(int aX, int aY, int i);

	public Edge[] getBorderingEdges(int aX, int aY);
	public Edge[] getProjectingEdges(int aX, int aY, int i);
	public Edge[] getLinkedEdges(int aX, int aY, int i);

	public int[] convertAxialToCube(int[] a);
	public int[] convertCubeToAxial(int[] c);

	public Field[][] getFields();
	public Corner[][][] getCorners();
	public Edge[][][] getEdges();
	public PlayerModel[] getPlayerModels();
	
	public Field getBandit();
	public void setBandit(Field f);
	
	//public Field[] getSpiral(Field f); TODO in controller

}
