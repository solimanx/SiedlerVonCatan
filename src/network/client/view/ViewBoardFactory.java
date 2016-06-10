package network.client.view;

import java.util.LinkedList;
import java.util.List;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Screen;
import javafx.stage.Stage;

public final class ViewBoardFactory {

	// jeweils die letzte Dimension des Arrays zur Speicherung der Koordinaten;
	// für Edge 2 Koordinaten (4 Punkte), weil Anfangs- und Endpunkt
	public double[][][] fieldCoordinates = new double[7][7][2]; // [6][6][2]
	public double[][][][] edgeCoordinates = new double[7][7][3][4]; // [6][6][3][4]
	public double[][][][] cornerCoordinates = new double[7][7][2][2]; // [6][6][2][2]
	public Polygon[][] fields = new Polygon[7][7];
	public Polygon[][][] corners = new Polygon[7][7][2];
	public Line[][][] streets = new Line[7][7][3];
	public Circle bandit;

	// Constant values for calculations
	public static double radius = 50.0;
	public double[] windowCenter = new double[2];
	public double[] screenCenter = new double[2];// [2]
	public static double sin60 = Math.sqrt(3) / 2;
	public static double rad60 = Math.PI / 3; // Hilfsvariable sqrt(3)/2
	private static double halfWidth = sin60 * radius;

	public List<Shape> figures = new LinkedList<Shape>();
	private Pane boardPane;

	public Pane getViewBoard(Stage stage) {
		boardPane = new Pane();
		// windowCenter[0] = stage.getWidth() / 2;
		// windowCenter[1] = stage.getHeight() / 2;
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		windowCenter[1] = screenBounds.getHeight() / 2;
		windowCenter[0] = screenBounds.getWidth() / 2;

		calculateFieldCenters(windowCenter);
		calculateCornerCenters();
		calculateEdgeCorners();
		initBoard();

		boardPane.getChildren().addAll(0, figures);

		return boardPane;
	}

	private void initBoard() {
		// TODO Auto-generated method stub

	}

	public void initCorners() {
		for (int i = 0; i < fieldCoordinates.length; i++) {
			for (int j = 0; j < fieldCoordinates.length; j++) {
				corners[i][j][0] = createVillage(i, j, 0);
			}
		}

	}

	/**
	 * @param centerCoordinates
	 * @return double array of coordinates of 6 Points (12 double values)
	 *         calculates coordinates of Hexagon from given center coordinates
	 */
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

	private Polygon createVillage(int i, int j, int k) {
		Polygon village;
		if (k == 0) {
			double[] center = { fieldCoordinates[i][j][0], fieldCoordinates[i][j][1] - radius };
			village = drawVillage(center);
			village.setVisible(false);
			// village.setOnMouseClicked(e -> gc.buildVillage()); // TODO
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

	/**
	 * draws a Circle with diceIndex
	 */
	public void setFieldChip(int u, int v, int diceIndex) {
		Text text = new Text("" + diceIndex);
		text.setBoundsType(TextBoundsType.VISUAL);
		text.setTextAlignment(TextAlignment.CENTER);
		Circle circle = new Circle(15.0);
		circle.setFill(Color.WHITE);
		StackPane chip = new StackPane(circle, text);
		chip.toFront();
		chip.setTranslateX(fieldCoordinates[u + 3][v + 3][0] - 15.0);
		chip.setTranslateY(fieldCoordinates[u + 3][v + 3][1] - 15.0);
		boardPane.getChildren().add(chip);
	}

	/**
	 * takes 6 coordinate pairs (x,y) and draws a Polygon hexagon
	 *
	 * @param points
	 * @return Polygon
	 */
	public Polygon drawHexagon(double[] points) {
		Polygon hexagon = new Polygon(points);
		hexagon.setFill(Color.LIGHTSKYBLUE);
		hexagon.setStroke(Color.LIGHTGRAY);
		return hexagon;
	}

	/**
	 * takes pair of coordinates as center point and draws a village
	 *
	 * @param center
	 * @return Polygon
	 */
	public Polygon drawVillage(double[] center) {
		Polygon village = new Polygon(center[0], center[1] - 18, center[0] + 10, center[1] - 10, center[0] + 10,
				center[1] + 10, center[0] - 10, center[1] + 10, center[0] - 10, center[1] - 10);

		return village;
	}

	public Line drawStreet(double[] coordinates) {
		Line street = new Line(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
		street.setStrokeWidth(6.0);

		return street;
	}

	public Polygon drawCity(double[] center) {
		Polygon city = new Polygon(center[0] + 5, center[1] - 10, center[0] + 5, center[1] - 20, center[0] + 10,
				center[1] - 20, center[0] + 10, center[1] + 10, center[0] - 10, center[1] + 10, center[0] - 10,
				center[1] - 20, center[0] - 5, center[1] - 20, center[0] - 5, center[1] - 10);
		return city;
	}

	public Circle drawBandit() {
		Circle bandit = new Circle(25.0);
		bandit.setFill(Color.BLACK);
		bandit.setCenterX(windowCenter[0]);
		bandit.setCenterY(windowCenter[1]);
		return bandit;
	}

	/**
	 * auxiliary method calculating center coordinates of every field.
	 * windowsCenter is taken as center point of board
	 *
	 * @param windowCenter
	 */
	private void calculateFieldCenters(double[] windowCenter) {
		double x;
		double y;
		for (int u = -3; u <= 3; u++) {
			for (int v = -3; v <= 3; v++) {
				if (Math.abs(u + v) <= 3) {
					x = +1 * halfWidth * (u + 2 * v) + windowCenter[0];
					y = +1 * 1.5 * radius * u + windowCenter[1];
					fieldCoordinates[v + 3][u + 3][0] = x;
					fieldCoordinates[v + 3][u + 3][1] = y;
				}
			}
		}
	}

	private void calculateEdgeCorners() {
		double x1;
		double y1;
		double x2;
		double y2;
		for (int u = 0; u < 7; u++) {
			for (int v = 0; v < 7; v++) {
				x1 = fieldCoordinates[u][v][0];
				y1 = fieldCoordinates[u][v][1] - radius;
				x2 = fieldCoordinates[u][v][0] - halfWidth;
				y2 = fieldCoordinates[u][v][1] - radius / 2;
				edgeCoordinates[u][v][0][0] = x1;
				edgeCoordinates[u][v][0][1] = y1;
				edgeCoordinates[u][v][0][2] = x2;
				edgeCoordinates[u][v][0][3] = y2;

				x1 = fieldCoordinates[u][v][0];
				y1 = fieldCoordinates[u][v][1] - radius;
				x2 = fieldCoordinates[u][v][0] + halfWidth;
				y2 = fieldCoordinates[u][v][1] - (radius / 2);
				edgeCoordinates[u][v][1][0] = x1;
				edgeCoordinates[u][v][1][1] = y1;
				edgeCoordinates[u][v][1][2] = x2;
				edgeCoordinates[u][v][1][3] = y2;

				edgeCoordinates[u][v][2][0] = x2;
				edgeCoordinates[u][v][2][1] = y2;
				edgeCoordinates[u][v][2][2] = x2;
				edgeCoordinates[u][v][2][3] = y2 + radius;

			}

		}
		filterUnusedEdges();

	}

	/**
	 * calculates the center points of corners and saves them in
	 * cornerCoordinate array. dependency: filled fieldCoordinates array
	 */
	private void calculateCornerCenters() {
		double x;
		double y;
		for (int u = -3; u <= 3; u++) {
			for (int v = -3; v <= 3; v++) {
				if (true) {
					x = fieldCoordinates[u + 3][v + 3][0];
					y = fieldCoordinates[u + 3][v + 3][1] - radius;
					cornerCoordinates[u + 3][v + 3][0][0] = x;
					cornerCoordinates[u + 3][v + 3][0][1] = y;
					// x = fieldCoordinates[u][v][0];
					y = fieldCoordinates[u + 3][v + 3][1] + radius;
					cornerCoordinates[u + 3][v + 3][1][0] = x;
					cornerCoordinates[u + 3][v + 3][1][1] = y;
				}
			}

		}
		filterUnusedCorners();
	}

	/**
	 * sets x-coordinate of unused corners to 0 fields with x-coordinate 0 are
	 * sea
	 */
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

	/**
	 * sets x-coordinate of unused edges to 0; edges with x-coordinate 0 won't
	 * be initialized
	 */
	private void filterUnusedEdges() {
		// row 0
		edgeCoordinates[3][0][0][0] = 0;
		edgeCoordinates[3][0][1][0] = 0;
		edgeCoordinates[3][0][2][0] = 0;

		edgeCoordinates[4][0][0][0] = 0;
		edgeCoordinates[4][0][1][0] = 0;
		edgeCoordinates[4][0][2][0] = 0;

		edgeCoordinates[5][0][0][0] = 0;
		edgeCoordinates[5][0][1][0] = 0;
		edgeCoordinates[5][0][2][0] = 0;

		edgeCoordinates[6][0][0][0] = 0;
		edgeCoordinates[6][0][1][0] = 0;
		edgeCoordinates[6][0][2][0] = 0;

		// row 1
		edgeCoordinates[2][1][0][0] = 0;
		edgeCoordinates[2][1][1][0] = 0;

		edgeCoordinates[6][1][0][0] = 0;
		edgeCoordinates[6][1][1][0] = 0;
		edgeCoordinates[6][1][2][0] = 0;

		// row 2
		edgeCoordinates[1][2][0][0] = 0;
		edgeCoordinates[1][2][1][0] = 0;

		edgeCoordinates[6][2][0][0] = 0;
		edgeCoordinates[6][2][1][0] = 0;
		edgeCoordinates[6][2][2][0] = 0;

		// row 3
		edgeCoordinates[0][3][0][0] = 0;
		edgeCoordinates[0][3][1][0] = 0;

		edgeCoordinates[6][3][0][0] = 0;
		edgeCoordinates[6][3][1][0] = 0;
		edgeCoordinates[6][3][2][0] = 0;

		// row 4
		edgeCoordinates[0][4][0][0] = 0;

		edgeCoordinates[5][4][1][0] = 0;
		edgeCoordinates[5][4][2][0] = 0;

		// row 5
		edgeCoordinates[0][5][0][0] = 0;

		edgeCoordinates[4][5][1][0] = 0;
		edgeCoordinates[4][5][2][0] = 0;

		// row 6
		edgeCoordinates[0][6][0][0] = 0;
		edgeCoordinates[0][6][2][0] = 0;

		edgeCoordinates[1][6][2][0] = 0;

		edgeCoordinates[2][6][2][0] = 0;

		edgeCoordinates[3][6][1][0] = 0;
		edgeCoordinates[3][6][2][0] = 0;

	}

	public int convertToHex(int x) {
		return x - 3;
	}

	public int convertToRect(int x) {
		return x + 3;
	}

}
