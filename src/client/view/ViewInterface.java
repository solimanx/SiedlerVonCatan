package client.view;

import com.sun.javafx.geom.Shape;

import enums.ResourceType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import model.Corner;
import model.Edge;
import model.Field;

public interface ViewInterface {
	public boolean initialize();

	public double[] createHexagon(double x, double y);

	public Polygon drawHexagon(double[] points);

	public Shape drawChips(double[] center, int diceIndex);

	public int convertFromHexToWorld(int x);

	public int convertToRect(int x);

	public Polygon drawVillage(double[] center);

	public Polygon drawStreet();

	public Polygon drawCity(double[] center);

	public void drawDices();

	public Polygon drawBandit();

	/**
	 * @param fields
	 *            TODO: aus jedem Field den RessourceType holen und entsprechend
	 *            zeichnen
	 */
	public void setField(int u, int v, ResourceType resourceType, int diceIndex);

	public void setVillage(int u, int v, int dir, Color playerColor);

	public void setStreet(int u, int v, int dir, Color playerColor);

	/**
	 * @param corner
	 *            TODO: vorher village hier auf der corner löschen
	 */
	public void setCity(int u, int v, int dir, Color playerColor);

	/**
	 * @param field
	 *            TODO: Bandit an alter Position vorher löschen
	 */
	public void setBandit(Field field);

	int convertToHex(int x);

}
