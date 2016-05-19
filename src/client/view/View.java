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
		 int[] windowCenter={50,50};
		 //The integer Array windowCenter has two ELements widowCenter[0]= x Koordinate;
		 // windowCenter[1]= y Koordinate of the Middle Point od the Window
		// int x=50;//X koordinate of Middle Point set as 50
		// int y=50;//Y Koordinate of Middle Point set as 50
	//int[] WCkoord;// Array that saves the 2 Elements, the x Koordinate od the new Middle point, from 
		// a neighbour Field , also the y Koordinate from this Field.
		for(int i=-3;i<=3;i++){// tree neighbours of the Middle Field that are right from it , and left from it
			if(i!=0){// i shouldnt be 0
				//X koordinate is the same
				int x=windowCenter[0];
				int y=windowCenter[1]+i*sin60*radius;//or y=y+i*sin60*radius
				//example: for i=1
				// it gets the first right neighbours koordinates 
				int []WCkoord={x,y};
				//Save the new koordinate in fieldcoordinates, not done
			}
		}
		for(int j=-2;j<=2;j++){//j is the amount of radiuses that we need in order to go one row higher/lower or three rows higher/lower
			
		if(j!=0){//j shouldnt be 0 
			int x=windowCenter[0]+sin60*radius;
			int y=windowCenter[1]+j*radius;
			//if j=1, we get the koordinates of a field one row above our middle Field
			int []WCkoord={x,y};
			//Save those koordinates to fieldcoordinates, not done
			//so now we have the koordinates of the field one row above the middle Field
			// we go tree times right and left in order to get their neighbours coordinates
			for(int i=-3;i<=3;i++){
				if(i!=0);{
				int v= WCkoord[0];//the neighbour has the same x koordinate
				int u=WCkoord[1]+i*sin60*radius;
				int[] NeighbourKoord1_3={v,u};//koordinates of the neighbours from the Field that we got first, starting from the Middle Field and going one row or three roads above or under 
				//Problem: we have calculated more neighbours than we actually had, we calculated also the koordinates of Fiels that are with Water
				//Filter them?
				//Save into fieldCoordinates TODO
			}
			}
		}
		}
		//In order to calculate the Coordinates of a Field, that is two rows above or under our Middle Field
		for(int k=-2;k<=2;k++){
			if(k!=-1){
				if(k!=0){
					if(k!=1){
						int x=windowCenter[0];
					   int y=windowCenter[1]+k*sin60*radius;
					   int []WCkoord={x,y};
					   //SAVE ,not done
					   for(int i=-3;i<=3;i++){
							if(i!=0);{
							int g= WCkoord[0];//the neighbour has the same x koordinate
							int h=WCkoord[1]+i*sin60*radius;
							//Problem: we have calculated more neighbours than we actually had, we calculated also the koordinates od the Fiels that are with Water
							//Filter them?
							int [] NeighbourKoord2={g,h};
							//Save into fieldCoordinates, not done
						}
					}
					}
				}
			}
		}
		//WCkoord sind die Koordinaten der Felder der mitlleren Reihe von links nach rechts
		//WCkoord dazu kommen die Koordinaten eines Feldes das eine Reihen unten ist
		//NeighbourKoord1_3 Nachbarn diese Feldes von links nach recht
		//WCkoord dazu  kommen die Koordinaten eines Feldes das eine Reihen oben ist
		//NeighbourKoord1_3 Nachbarn diese Feldes von links nach recht
		//WCKoord dazu kommen die Koordinaten eines Feldes das zwei Reihen oben ist
		//NeighbourKoord1_3 Nachbarn diese Feldes von links nach recht
		//WCkoord dazu kommen die Koordinaten des Feldes das zwei Reihen unten ist
		//NeighbourKoord2 nachbarn dieses Feldes von links nach rechts
		//WCkoord dazu kommen die Koordinaten des Feldes das zwei Reihen oben ist
	    //NeighbourKoord2 Nachbarn dieses Feldes von links nach rechts

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
