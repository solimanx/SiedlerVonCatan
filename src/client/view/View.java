package client.view;

import java.util.ArrayList;

import com.sun.javafx.geom.Shape;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import model.Board;
import model.Corner;
import model.Edge;
import model.Field;

public class View implements ViewInterface {

	private Stage primaryStage;
	// jeweils die letzte Dimension des Arrays zur Speicherung der Koordinaten;
	// für Edge 2 Koordinaten (4 Punkte), weil Anfangs- und Endpunkt
	public double[][][] fieldCoordinates = new double[7][7][2]; // [6][6][2]
	public int[][][][] edgeCoordinates; // [6][6][3][4]
	private Polygon[][][] corners = new Polygon[7][7][2];
	public double[][][][] cornerCoordinates = new double[7][7][2][2]; // [6][6][2][2]
	private BorderPane rootPane;
	private Board board;
	public static double radius = 50.0;
	public static double[] windowCenter = new double[2]; // [2]
	public static double sin60 = Math.sqrt(3) / 2;
	public static double rad60 = Math.PI / 3; // Hilfsvariable sqrt(3)/2
	private static double halfWidth = sin60 * radius;

	public Button button;
	public ArrayList<Polygon> figures = new ArrayList<Polygon>(1);
	private Pane centerPane;

	public View(Board board, Stage stage) {
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
		calculateFieldCenters(windowCenter);
		calculateCornerCenters();
		for (int i = 0; i < fieldCoordinates.length; i++) {
			for (int j = 0; j < fieldCoordinates.length; j++) {
				if (fieldCoordinates[i][j][0] > 0) {
					Polygon hexagon = drawHexagon(createHexagon(fieldCoordinates[i][j][0], fieldCoordinates[i][j][1]));
					hexagon.setVisible(true);
					figures.add(0, hexagon);
					for (int k = 0; k < 2; k++) {
						Polygon village = drawVillage(cornerCoordinates[i][j][k]);
						//set event listener
						village.setVisible(true);
						corners[i][j][k] = village;
						figures.add(village);
					}
				}
			}
		}

		button = new Button("Do Something!");
		button.setOpacity(0.0);
		rootPane.setTop(button);

		centerPane = new Pane();
		centerPane.getChildren().addAll(0, figures);

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
	public double[] createHexagon(double x, double y) {
		double[] points = new double[12];
		int j = 1;
		for (int i = 0; i < points.length; i = i + 2) {
			points[i] = (double) (x + radius * Math.sin(j * rad60));
			points[i + 1] = (double) (y + radius * Math.cos(j * rad60));
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
	public void setBandit(Field field) {
		// TODO Auto-generated method stub

	}

	private void calculateFieldCenters(double[] windowCenter) {
		double x;
		double y;
		for (int u = -3; u <= 3; u++) {
			for (int v = -3; v <= 3; v++) {
				if (Math.abs(u + v) <= 3) {
					x = -1 * halfWidth * (u + 2 * v) + windowCenter[0];
					y = -1 * 1.5 * radius * u + windowCenter[1];
					fieldCoordinates[u + 3][v + 3][0] = x;
					fieldCoordinates[u + 3][v + 3][1] = y;
				}
			}
		}
	}

	private void calculateCornerCenters() {
		double x;
		double y;
		for (int u = 0; u < 7; u++) {
			for (int v = 0; v < 7; v++) {
				if (Math.abs(u + v - 6) <= 3) {
						x = fieldCoordinates[u][v][0];
						y = fieldCoordinates[u][v][1] - radius;
						cornerCoordinates[u][v][0][0] = x;
						cornerCoordinates[u][v][0][1] = y;
						// x = fieldCoordinates[u][v][0];
						y = fieldCoordinates[u][v][1] + radius;
						cornerCoordinates[u][v][1][0] = x;
						cornerCoordinates[u][v][1][1] = y;
				}
			}

		}
		filterUnusedCorners();
	}
	
	private void filterUnusedCorners() {
		// row 0
		cornerCoordinates[3][0][0][0] = 0;
		cornerCoordinates[4][0][0][0] = 0;
		cornerCoordinates[5][0][0][0] = 0;
		cornerCoordinates[6][0][0][0] = 0;
		// row 1
		cornerCoordinates[2][1][0][0] = 0;
		cornerCoordinates[6][1][0][0] = 0;
		// row 2
		cornerCoordinates[1][2][0][0] = 0;
		cornerCoordinates[6][2][0][0] = 0;
		// row 3
		cornerCoordinates[0][3][0][0] = 0;
		cornerCoordinates[0][3][1][0] = 0;
		cornerCoordinates[6][3][0][0] = 0;
		cornerCoordinates[6][3][1][0] = 0;
		// row 4
		cornerCoordinates[0][4][1][0] = 0;
		cornerCoordinates[5][4][1][0] = 0;
		// row 5
		cornerCoordinates[0][5][1][0] = 0;
		cornerCoordinates[4][5][1][0] = 0;
		// row 6
		cornerCoordinates[0][6][1][0] = 0;
		cornerCoordinates[1][6][1][0] = 0;
		cornerCoordinates[2][6][1][0] = 0;
		cornerCoordinates[3][6][1][0] = 0;
	}
     
	private void calculateEdgeCorners() {
		
	}
	@Override
	public int convertToHex(int x) {
		return x - 3;
	}

	@Override
	public int convertToRect(int x) {
		return x + 3;
	}

	public void initCorners() {
		for (int i = 0; i < fieldCoordinates.length; i++) {
			for (int j = 0; j < fieldCoordinates.length; j++) {
				corners[i][j][0] = createVillage(i, j, 0);
			}
		}

	}

	private Polygon createVillage(int i, int j, int k) {
		Polygon village;
		if (k == 0) {
			double[] center = { fieldCoordinates[i][j][0], fieldCoordinates[i][j][1] - radius };
			village = drawVillage(center);
			village.setVisible(false);
			//village.setOnMouseClicked(e -> gc.buildVillage()); // TODO
																// Methodensignatur
																// im gc
																// unvollständig
			return village;
		} else {
			double[] center = { fieldCoordinates[i][j][0], fieldCoordinates[i][j][1] + radius };
			village = drawVillage(center);
			village.setVisible(false);
			return village;
		}
	}

	public void afterVillageBuilt(int i, int j, int dir) {
		//corners[i][j][dir].setOnMouseClicked(e -> gc.buildCity());
	}

	@Override
	public int convertFromHexToWorld(int x) {
		// TODO Auto-generated method stub
		return 0;
	}

}
