package model;

import java.util.Random;

import javax.swing.InternalFrameFocusTraversalPolicy;

import model.objects.*;

import java.io.IOException;
import java.util.ArrayList;

import settings.DefaultSettings;

public class HexService {
	Board board;

	public int[] getCornerCoordinates(int x1, int y1, int x2, int y2, int x3, int y3) {
		Corner[] a = board.getSurroundingCorners(x1, y1);
		Corner[] b = board.getSurroundingCorners(x1, y1);
		Corner[] c = board.getSurroundingCorners(x1, y1);
		ArrayList<Corner> lista = new ArrayList<Corner>();
		ArrayList<Corner> listb = new ArrayList<Corner>();
		ArrayList<Corner> lastCorner = new ArrayList<Corner>();
		for (int i = 0; i < a.length; i++) {
			lista.add(a[i]);
		}
		for (int i = 0; i < b.length; i++) {
			if (lista.contains(b[i])) {
				listb.add(b[i]);
			}
		}
		for (int i = 0; i < c.length; i++) {
			if (listb.contains(c[i])) {
				lastCorner.add(c[i]);
			}
		}
		if (board.getCornerAt(x1, y1, 1) == lastCorner.get(0)) {
			int[] result = { x1, y1, 1 };
			return result;
		}
		if (board.getCornerAt(x3, y3, 0) == lastCorner.get(0)) {
			int[] result = { x3, y3, 0 };
			return result;
		}
		return null;
	}
	
	
	
	public int[] getEdgeCoordinates(int x1, int y1, int x2, int y2) {
		Edge[] a = getSurroundingEdges(x1, x2);
		Edge[] b = getSurroundingEdges(x2, y2);
		ArrayList<Edge> firstList = new ArrayList<Edge>();
		ArrayList<Edge> resultList = new ArrayList<Edge>();
		for (int i = 0; i < a.length; i++) {
			firstList.add(a[i]);
		}
		for (int j = 0; j < b.length; j++) {
			if (firstList.contains(b[j])) {
				resultList.add(b[j]);
			}
		}
		for (int i = 0; i < 3; i++) {
			if (board.getEdgeAt(x1, y1, i) == resultList.get(0)) {
				int[] result = { x1, y1, i };
				return result;
			}
			if (board.getEdgeAt(x2, y2, i) == resultList.get(0)) {
				int[] result = { x2, y2, i };
				return result;
			}
		}
		return null;
	}
	
	
	
	public Edge[] getSurroundingEdges(int aX, int aY) {
		Edge n = board.getEdgeAt(aX, aY, 0);
		Edge ne = board.getEdgeAt(aX + 1, aY - 1, 1);
		Edge se = board.getEdgeAt(aX, aY + 1, 0);
		Edge s = board.getEdgeAt(aX, aY, 1);
		Edge sw = board.getEdgeAt(aX - 1, aY + 1, 0);
		Edge nw = board.getEdgeAt(aX, aY - 1, 1);
		Edge[] surroundingEdges = { n, ne, se, s, sw, nw };
		return surroundingEdges;
	}
	
	
	
	/**
	 * returns all fieldIDs in the order of a spiral, beginning with the inputID and ending in the middle of the field.
	 * example: input: "N"
	 * 			output: "NIEFKOJ"
	 * 
	 * does not work with water
	 */
	//TODO water
	public static String getSpiral(String f){
		String result = "";
		String nextField = f;
		String plannedNextField = f;
		int radius = getDistanceFromMid(f);
		int[] coord = new int [2];
		for(int i = radius; i>0; i--){
			for(int j = 0; j<7; j++){
				for(int k = 0; k<i; k++){
					switch(j){
					case 0:
						if(i == radius){
							if(getRing(nextField).contains(plannedNextField)){
								nextField = plannedNextField;
								result = result + nextField;
								coord = Board.getStringToCoordMap().get(nextField);
								plannedNextField = getNextField(coord[0], coord[1], getDirection(nextField));
								break;
							}else{
								continue;
							}
						}
						else{
							if(result.contains(plannedNextField)){
								continue;
							}else{
								nextField = plannedNextField;
								result = result + nextField;
								coord = Board.getStringToCoordMap().get(nextField);
								plannedNextField = getNextField(coord[0], coord[1], getDirection(nextField));
								break;
							}
						}
					case 6:
						if(result.contains(plannedNextField)){
							k=i;
							break;
						}
						else{
							nextField = plannedNextField;
							result = result + nextField;
							coord = Board.getStringToCoordMap().get(nextField);
							plannedNextField = getNextField(coord[0], coord[1], getDirection(nextField));
							break;
						}
					default:
						nextField = plannedNextField;
						result = result + nextField;
						coord = Board.getStringToCoordMap().get(nextField);
						plannedNextField = getNextField(coord[0], coord[1], getDirection(nextField));
						break;
					}
				}
			}
			plannedNextField = getNextField(coord[0], coord[1], getDirection(nextField)+1);
		}
		result = result + Board.getCoordToStringMap().get(new Index(0,0));
		return result;
	}
	
	
	
	/**
	 * calculates the distance to center
	 * example: (0,0) -> 0
	 * 			(-2,2) -> 2
	 * 
	 * @param f
	 * 		Field ID
	 * @return
	 * 		distance to mid or radius
	 */
	public static int getDistanceFromMid(String f){
		String nextField = f;
		int result = 0;
		int [] coord = new int[2];
		String end = Board.getCoordToStringMap().get(new Index(0, 0));
		while(!nextField.equals(end)){
			coord = Board.getStringToCoordMap().get(nextField);
			nextField = getNextField(coord[0], coord[1], getDirection(nextField)+1);
			result++;
		}
		return result;
	}
	
	
	/**
	 * returns all fieldIDs, which are one the same ring as the inputID.
	 * example: input: "E"
	 * 			output: "NIEFKO"
	 * @param f
	 * 		Field ID
	 * @return
	 * 		Field ID's
	 */
	public static String getRing(String f){
		String result = "";
		int radius = getDistanceFromMid(f);
		String nextField = Board.getCoordToStringMap().get(new Index(-radius, radius));
		int[] coord = new int[2];
		for(int i = 0; i< 6; i++){
			for(int j = 0; j< radius; j++){
				result = result + nextField;
				coord = Board.getStringToCoordMap().get(nextField);
				nextField = getNextField(coord[0], coord[1], getDirection(nextField));
			}
		}
		return result;
	}


	/**
	 * calculates clockwise the direction of the next field
	 * 
	 * @param f
	 * 		Field ID
	 * @return
	 * 		Edge Direction
	 */
	public static int getDirection(String f) {
		int x = Board.getStringToCoordMap().get((f))[0];
		int y = Board.getStringToCoordMap().get((f))[1];
		int sum = x + y;
		if (x < 0 && y <= 0 && sum < 0) {
			return 0;
		}
		if (x >= 0 && y < 0 && sum < 0) {
			return 1;
		}
		if (x > 0 && y < 0 && sum >= 0) {
			return 2;
		}
		if (x > 0 && y >= 0 && sum > 0) {
			return 3;
		}
		if (x <= 0 && y > 0 && sum > 0) {
			return 4;
		}
		if (x < 0 && y > 0 && sum <= 0) {
			return 5;
		}
		throw new IllegalArgumentException("illegal Argument in HexService.getDirection()");
	}
	
	/**
	 * calculates a field in the searched direction
	 * 
	 * @param aX
	 * 		Field x-coordinate
	 * @param aY
	 * 		Field y-coordinate
	 * @param dir
	 * 		direction of the searched field
	 * @return
	 * 		Field ID of the next Field
	 * 
	 */
	public static String getNextField(int aX, int aY, int dir) {
		String neighbours = Board.getNeighbouringFields(aX, aY);
		char[] result = neighbours.toCharArray();
		String a = "";
		switch (dir) {
		case 0:
			a = a + result[0];
			break;
		case 1:
			a = a + result[1];
			break;
		case 2:
			a = a + result[2];
			break;
		case 3:
			a = a + result[3];
			break;
		case 4:
			a = a + result[4];
			break;
		case 5:
			a = a + result[5];
			break;
		default:
			a = null;
			break;
		}
		return a;
	}
	

	public static int sumAbsCubeXYZ(int[] temp) {
		return Math.abs(temp[0]) + Math.abs(temp[1]) + Math.abs(temp[2]);
	}

	/**
	 * Converts Axial (x,y) to Cube (x,y,z)
	 *
	 * @param a
	 *            (x,y)
	 */
	public static int[] convertAxialToCube(int[] a) {

		if (a.length != 2) {
			// TODO: logging
			System.out.println("Error in HexService.convertAxialToCube");
			return null;

		} else {

			int x = a[0];
			int z = a[1];
			int y = -x - z;

			return new int[] { x, y, z };
		}
	}

	/**
	 * Converts Cube (x,y,z) to Axial (x,y)
	 *
	 * @param c
	 *            (x,y,z)
	 */
	public static int[] convertCubeToAxial(int[] c) {
		if (c.length != 3) {
			// TODO: logging
			System.out.println("Error in HexService.convertCubeToAxial");
			return null;
		} else {
			int x = c[0];
			int y = c[2];
			return new int[] { x, y };
		}
	}

	public static int sumOfCubeXY(int[] subarray) {
		return subarray[0] + subarray[1];
	}


	/**
	 * 
	 * @param u
	 * 		Field 1 x-coordinate
	 * @param v
	 * 		Field 1 y-coordinate
	 * @param x
	 * 		Field 2 x-coordinate
	 * @param y
	 * 		Field 2 y-coordinate
	 * @return
	 * 		the water Field
	 * 		
	 */
	public Field getHarbourSeaField(int u, int v, int x, int y){
		if(board.getField(u, v).getResourceType() == enums.ResourceType.SEA){
			return board.getField(u, v);
		}
		if(board.getField(x, y).getResourceType() == enums.ResourceType.SEA){
			return board.getField(x, y);
		}
		if(board.getField(u, v).getResourceType() == enums.ResourceType.SEA && board.getField(x, y).getResourceType() == enums.ResourceType.SEA){
			throw new IllegalArgumentException("Both Fields are sea Fields");
		}
		throw new IllegalArgumentException("None of both Fields is a sea Fields");
	}

	/**
	 * returns both adjacent corners of an edge
	 * @param s
	 * 		Field ID of two Fields defining the edge
	 * @return
	 * 		two Strings of 3 characters defining the upper and lower corner
	 */
	public static String[] getCornerFromEdge(String s){
		if(s.length()!= 2){
			throw new IllegalArgumentException("Too many or too few Fields as input in HexService.getCornerFromEdge");
		}
		int[] a = Board.getStringToCoordMap().get(s.substring(0, 1));
		int[] b = Board.getStringToCoordMap().get(s.substring(1, 2));
		String fields1 = Board.getNeighbouringFields(a[0],a[1]);
		String fields2 = Board.getNeighbouringFields(b[0],b[1]);

		String common = "";

		for(int i=0;i<fields1.length();i++){
		    for(int j=0;j<fields2.length();j++){
		        if(fields1.charAt(i)==fields2.charAt(j)){
		            common += fields1.charAt(i);
		            break;
		        }
		    }
		}
		String [] result = new String [2];
		result[0] = s.substring(0, 1) + s.substring(1, 2) + common.substring(0, 1);
		result[1] = s.substring(0, 1) + s.substring(1, 2) + common.substring(1, 2);
		return result;
	}



}
