package client.view;

import com.sun.javafx.geom.Shape;

import enums.ResourceType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import model.Corner;
import model.Edge;
import model.Field;

public interface ViewInterface {
	public boolean initialize();

	public double[] createHexagon(double x, double y);

	public Polygon drawHexagon(double[] points);

	public void setFieldChip(int u, int v, int diceIndex);

	public int convertToRect(int x);

	public Polygon drawVillage(double[] center);

	public Line drawStreet(double[] center);

	public Polygon drawCity(double[] center);

	public void drawDices();

	public Circle drawBandit();

	public void setFieldResourceType(int u, int v, Color color);

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
	public void setBandit(int u, int v);

	int convertToHex(int x);

}
