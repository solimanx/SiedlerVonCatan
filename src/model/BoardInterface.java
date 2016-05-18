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

	public int[] convertToCube(int[] a); // required for initialize Board
	public int[] convertToAxial(int[] c);

	public Field[] getSpiral(Field f); // out -> in

	public Field[][] getFields();
	public Corner[][][] getCorners();
	public Edge[][][] getEdges();
	public PlayerModel[] getPlayerModels();
	public Field getBandit();

	public void setBandit(Field f);

}
