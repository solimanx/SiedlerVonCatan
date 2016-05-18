package model;
/**
 * Refer to 
 * 
 *
 */
public interface BoardInterface {
    
	public Field getFieldAt(int offsetX, int offsetY);
	public Corner getCornerAt(int offsetX, int offsetY, int i);
	public Edge getEdgeAt(int offsetX, int offsetY, int i);
	
	public Field[] getNeighbouringFields(int offsetX, int offsetY);
	public Field[] getTouchingFields(int offsetX, int offsetY, int i);
	public Field[] getConnectedFields(int offsetX, int offsetY, int i);

	public Corner[] getSurroundingCorners(int offsetX, int offsetY);
	public Corner[] getAdjacentCorners(int offsetX, int offsetY, int i);
	public Corner[] getAttachedCorners(int offsetX, int offsetY, int i);

	public Edge[] getBorders(int offsetX, int offsetY);
	public Edge[] getSurroundingE(Corner c);
	public Edge[] getSurroundingE(Edge e);

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
