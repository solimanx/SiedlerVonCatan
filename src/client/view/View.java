package client.view;

import model.Corner;
import model.Edge;
import model.Field;

public class View implements ViewInterface {

	// jeweils die letzte Dimension des Arrays zur Speicherung der Koordinaten;
	// für Edge 2 Koordinaten (4 Punkte), weil Anfangs- und Endpunkt
	public int[][][] fieldCoordinates; // [6][6][2]
	public int[][][][] edgeCoordinates; // [6][6][3][4]
	public int[][][][] cornerCoordinates; // [6][6][2][2]
	public static double radius;
	public static int[] windowCenter; // [2]
	public static double sin60 = Math.sqrt(3) / 2; // Hilfsvariable sqrt(3)/2

	@Override
	public boolean initialize() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double[] createHexagon(double[] center) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void drawHexagon(double[] points) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawChips() {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawVillage() {
		// TODO Auto-generated method stub

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
		* ausgehend von windowCenter = [x,y] die Mittelpunkte aller 43 Felder in fieldcoordinates speichern
		* 0,0 -> 3,3     -2,-1 -> 1,2
		*/
		
		
		

	}

}
