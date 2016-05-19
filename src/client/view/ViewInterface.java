package client.view;

import javafx.scene.shape.Polygon;
import model.Corner;
import model.Edge;
import model.Field;



public interface ViewInterface {
	public boolean initialize();

	public double[] createHexagon(double[] center);

	public Polygon drawHexagon(double[] points);

	public void drawChips();
	
	public int convertToHex(int x);
	
	public int convertToRect(int x);

	public Polygon drawVillage(double[] center);

	public void drawStreet();

	public void drawCity();

	public void drawDices();

	public void drawBandit();

	/**
	 * @param fields
	 *            TODO: aus jedem Field den RessourceType holen und entsprechend
	 *            zeichnen
	 */
	public void fillFields(Field[] fields);

	public void setVillage(Corner corner);

	public void setStreet(Edge edge);

	/**
	 * @param corner
	 *            TODO: vorher village hier auf der corner löschen
	 */
	public void setCity(Corner corner);
	
	/**
	 * @param field
	 * TODO: Bandit an alter Position vorher löschen
	 */
	public void setBandid(Field field);
	
	public void calculateFieldCenters();
	
	
}
