package client.view;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import model.Corner;
import model.Edge;
import model.Field;

public class View implements ViewInterface {

	private Stage primaryStage;
	// jeweils die letzte Dimension des Arrays zur Speicherung der Koordinaten;
	// für Edge 2 Koordinaten (4 Punkte), weil Anfangs- und Endpunkt
	public int[][][] fieldCoordinates; // [6][6][2]
	public int[][][][] edgeCoordinates; // [6][6][3][4]
	public int[][][][] cornerCoordinates; // [6][6][2][2]
	private BorderPane rootPane;
	public static double radius = 50.0;
	public static double[] windowCenter = new double[2]; // [2]
	public static double sin60 = Math.sqrt(3) / 2;
	public static double rad60 = Math.PI / 3;// Hilfsvariable sqrt(3)/2

	public Button button;
	public ArrayList<Polygon> hexagons = new ArrayList<Polygon>(1);

	public View(Stage stage) {
		this.primaryStage = stage;
		try {
			rootPane = new BorderPane();
			Scene scene = new Scene(rootPane, 400, 400);
			primaryStage.setMaximized(true);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			windowCenter[0] = primaryStage.getWidth() / 2;
			windowCenter[1] = primaryStage.getHeight() / 2;

		} catch (Exception e) {
			e.printStackTrace();
		}
		initialize();
		// TODO getFields
	}

	@Override
	public boolean initialize() {
		double[] hexpoints;
		for (int i = 0; i < 1; i++) {
			hexpoints = createHexagon(windowCenter);
			Polygon hexagon = drawHexagon(hexpoints);
			hexagon.setVisible(true);
			hexagons.add(hexagon);
		}

		button = new Button("Do Something!");
		rootPane.setTop(button);

		Pane centerPane = new Pane();
		centerPane.getChildren().addAll(0, hexagons);
		centerPane.getChildren().add(drawVillage(windowCenter));

		rootPane.setCenter(centerPane);

		return true;
	}

	/**
	 * @param centerCoordinates
	 * @return double array of coordinates of 6 Points (12 double values)
	 *         calculates coordinates of Hexagon from given center coordinates
	 */
	@Override
	public double[] createHexagon(double[] centerCoordinates) {
		double[] points = new double[12];
		int j = 1;
		for (int i = 0; i < points.length; i = i + 2) {
			points[i] = (double) (centerCoordinates[0] + radius * Math.sin(j * rad60));
			points[i + 1] = (double) (centerCoordinates[1] + radius * Math.cos(j * rad60));
			j++;
		}
		return points;
	}

	@Override
	public Polygon drawHexagon(double[] points) {
		Polygon hexagon = new Polygon(points);
		hexagon.setFill(Color.DARKMAGENTA);
		return hexagon;
	}

	@Override
	public void drawChips() {
		// TODO Auto-generated method stub

	}

	@Override
	public Polygon drawVillage(double[] center) {
		double[] points = { center[0] + 20, center[1], center[0], center[1] + 20, center[0] - 20, center[1],
				center[0] - 20, center[1] - 10, center[0] + 20, center[1] - 10 };
		Polygon village = new Polygon(center[0], center[1] - 18, center[0] + 10, center[1] - 10, center[0] + 10,
				center[1] + 10, center[0] - 10, center[1] + 10, center[0] - 10, center[1] - 10);

		return village;
	}

	@Override
	public void drawStreet() {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawCity() {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawDices() {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawBandit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void fillFields(Field[] fields) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVillage(Corner corner) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStreet(Edge edge) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCity(Corner corner) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBandid(Field field) {
		// TODO Auto-generated method stub

	}

	@Override
	public void calculateFieldCenters() {
		/*
		 * ausgehend von windowCenter = [x,y] die Mittelpunkte aller 43 Felder
		 * in fieldcoordinates speichern 0,0 -> 3,3 -2,-1 -> 1,2
		 */

	}

	@Override
	public int convertToHex(int x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int convertToRect(int x) {
		// TODO Auto-generated method stub
		return 0;
	}

}
