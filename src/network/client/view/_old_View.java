package network.client.view;

import java.util.LinkedList;
import java.util.List;

import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Board;

public class _old_View implements _old_ViewInterface {

	private Stage primaryStage;
	private BorderPane rootPane;
	private Pane centerPane;

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

	// Items on the Board
	public Button button;
	public Button button2;
	public Button button3;
	public Button button4;

	public TextArea messages = new TextArea();
	public TextField chatInput = new TextField();

	public List<Shape> figures = new LinkedList<Shape>();

	/**
	 * @param board
	 * @param stage
	 */
	public _old_View(Board board, Stage stage ) {
		this.primaryStage = stage;
		try {
			rootPane = new BorderPane();

			Scene scene = new Scene(rootPane);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primaryStage.setFullScreen(true);
			// primaryStage.setMaximized(true);
			primaryStage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
		initialize();
		primaryStage.show();
	}

	@Override
	public boolean initialize() {
		windowCenter[0] = primaryStage.getWidth() / 2;
		windowCenter[1] = primaryStage.getHeight() / 2;
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		windowCenter[1] = screenBounds.getHeight() / 2;
		windowCenter[0] = screenBounds.getWidth() / 2;

		calculateFieldCenters(windowCenter);
		calculateCornerCenters();
		calculateEdgeCorners();
		initBoard();
		// drawDices();

		// Test Buttons
//		button = new Button("Do Something!");
//		button2 = new Button();
//		button3 = new Button();
//		button4 = new Button();
//		HBox buttons = new HBox(button, button2, button3, button4);
//		rootPane.setTop(buttons);
//		// end Test Buttons

		centerPane = new Pane();
		centerPane.getChildren().addAll(0, figures);

		messages.setPrefHeight(100);
		messages.setEditable(false);
		VBox chatPane = new VBox(10, messages, chatInput);
		chatPane.setMaxWidth(500.0);

		rootPane.setCenter(centerPane);
		rootPane.setTop(chatPane);
		rootPane.setAlignment(chatPane, Pos.TOP_CENTER);

		return true;
	}

	/**
	 *
	 */
	private void initBoard() {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				if (fieldCoordinates[i][j][0] > 0) {
					Polygon hexagon = drawHexagon(createHexagon(fieldCoordinates[i][j][0], fieldCoordinates[i][j][1]));
					hexagon.setVisible(true);
					figures.add(0, hexagon);
					fields[i][j] = hexagon;
					for (int l = 0; l < 3; l++) {
						if (edgeCoordinates[i][j][l][0] > 0) {
							Line street = drawStreet(edgeCoordinates[i][j][l]);
							street.setOpacity(0);
							street.setStroke(Color.WHITE);
							street.getStyleClass().add("street");
							streets[i][j][l] = street;

							int[] streetCoordinates = { i, j, l };

//							street.setOnMouseClicked(e -> {
//								mainViewController.streetClick(streetCoordinates);
//							});

							figures.add(street);
						}
					}
					for (int k = 0; k < 2; k++) {
						if (cornerCoordinates[i][j][k][0] > 0) {
							Polygon village = drawVillage(cornerCoordinates[i][j][k]);
							village.setOpacity(0);
							village.getStyleClass().add("village");
							corners[i][j][k] = village;

							int[] villageCoordinates = { i, j, k };

//							village.setOnMouseClicked(e -> {
//								mainViewController.villageClick(villageCoordinates);
//							});

							figures.add(village);
						}
					}
				}
			}
		}
		bandit = drawBandit();
		bandit.setOpacity(0);
		bandit.toFront();
		figures.add(bandit);
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

	/**
	 * takes 6 coordinate pairs (x,y) and draws a Polygon hexagon
	 *
	 * @param points
	 * @return Polygon
	 */
	@Override
	public Polygon drawHexagon(double[] points) {
		Polygon hexagon = new Polygon(points);
		hexagon.setFill(Color.LIGHTSKYBLUE);
		hexagon.setStroke(Color.LIGHTGRAY);
		;
		return hexagon;
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
		centerPane.getChildren().add(chip);
	}

	/**
	 * takes pair of coordinates as center point and draws a village
	 *
	 * @param center
	 * @return Polygon
	 */
	@Override
	public Polygon drawVillage(double[] center) {
		Polygon village = new Polygon(center[0], center[1] - 18, center[0] + 10, center[1] - 10, center[0] + 10,
				center[1] + 10, center[0] - 10, center[1] + 10, center[0] - 10, center[1] - 10);

		return village;
	}

	@Override
	public Line drawStreet(double[] coordinates) {
		Line street = new Line(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
		street.setStrokeWidth(6.0);

		return street;
	}

	@Override
	public Polygon drawCity(double[] center) {
		Polygon city = new Polygon(center[0] + 5, center[1] - 10, center[0] + 5, center[1] - 20, center[0] + 10,
				center[1] - 20, center[0] + 10, center[1] + 10, center[0] - 10, center[1] + 10, center[0] - 10,
				center[1] - 20, center[0] - 5, center[1] - 20, center[0] - 5, center[1] - 10);
		return city;
	}

	@Override
	public void drawDices() {
		Pane dice = new Pane();

		dice.setTranslateX(200);
		dice.setTranslateY(200);
		Box b = new Box(100, 100, 100);
		b.setMaterial(new PhongMaterial(Color.BEIGE));
		b.getTransforms().add(new Rotate(10, 0, 0, 0, Rotate.X_AXIS));
		b.getTransforms().add(new Rotate(10, 0, 0, 0, Rotate.Y_AXIS));
		b.getTransforms().add(new Rotate(10, 0, 0, 0, Rotate.Z_AXIS));

		dice.getChildren().addAll(b);

		TranslateTransition t1 = new TranslateTransition(Duration.millis(2000), b);
		t1.setToX(100);
		t1.setCycleCount(2);
		t1.setAutoReverse(true);

		RotateTransition r2 = new RotateTransition(Duration.millis(2000), b);
		r2.setAxis(Rotate.X_AXIS);
		r2.setToAngle(45);
		r2.setCycleCount(2);
		r2.setAutoReverse(true);
		r2.setAutoReverse(true);

		RotateTransition r3 = new RotateTransition(Duration.millis(2000), b);
		r3.setAxis(Rotate.Y_AXIS);
		r3.setToAngle(45);
		r3.setCycleCount(2);
		r3.setAutoReverse(true);
		r3.setAutoReverse(true);

		RotateTransition r4 = new RotateTransition(Duration.millis(2000), b);
		r4.setAxis(Rotate.Z_AXIS);
		r4.setToAngle(45);
		r4.setCycleCount(2);
		r4.setAutoReverse(true);
		r4.setAutoReverse(true);

		SequentialTransition s = new SequentialTransition();
		s.getChildren().addAll(t1, r2, r3, r4);
		s.play();

		rootPane.getChildren().add(dice);
	}

	@Override
	public Circle drawBandit() {
		Circle bandit = new Circle(25.0);
		bandit.setFill(Color.BLACK);
		bandit.setCenterX(windowCenter[0]);
		bandit.setCenterY(windowCenter[1]);
		return bandit;
	}

	@Override
	public void setVillage(int u, int v, int dir, Color playerColor) {
		Polygon village = corners[u + 3][v + 3][dir];
		village.setFill(playerColor);
		village.setOpacity(1.0);
		System.out.println("Village set on " + u + "," + v + " Direction: " + dir);
	}

	@Override
	public void setStreet(int u, int v, int dir, Color playerColor) {
		Line street = streets[u + 3][v + 3][dir];
		street.setOpacity(1.0);
		street.setStroke(playerColor);
		System.out.println("Street set on " + u + "," + v + " Direction: " + dir);

	}

	@Override
	public void setCity(int u, int v, int dir, Color playerColor) {
		Polygon city = drawCity(cornerCoordinates[u + 3][v + 3][dir]);
		city.setFill(playerColor);
		city.setVisible(true);
		centerPane.getChildren().add(city);
	}

	@Override
	public void setBandit(int u, int v) {
		bandit.setCenterX(fieldCoordinates[u + 3][v + 3][0]);
		bandit.setCenterY(fieldCoordinates[u + 3][v + 3][1]);
		bandit.setOpacity(1.0);
		// TODO Auto-generated method stub

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

				// x1 = fieldCoordinates[u][v][0];
				// y1 = fieldCoordinates[u][v][1] - radius;
				// x2 = fieldCoordinates[u][v][0] - halfWidth;
				// y2 = fieldCoordinates[u][v][1] - radius / 2;
				edgeCoordinates[u][v][2][0] = x2;
				edgeCoordinates[u][v][2][1] = y2;
				edgeCoordinates[u][v][2][2] = x2;
				edgeCoordinates[u][v][2][3] = y2 + radius;

			}

		}
		filterUnusedEdges();

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

	public void afterVillageBuilt(int i, int j, int dir) {
		// corners[i][j][dir].setOnMouseClicked(e -> gc.buildCity());
	}

	public void setFieldResourceType(int u, int v, Color color) {
		fields[u + 3][v + 3].setFill(color);
	}
}
