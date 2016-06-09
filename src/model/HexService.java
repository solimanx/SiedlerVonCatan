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
		ArrayList<Edge> array = new ArrayList<>();
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


	public ArrayList<Field> getSpiral(Field f){
		ArrayList<Field> result = new ArrayList<Field>();
		Field nextField = f;
		Field plannedNextField = f;
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
								result.add(nextField);
								coord = board.getFieldCoordinates(nextField.getFieldID());
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
								result.add(nextField);
								coord = board.getFieldCoordinates(nextField.getFieldID());
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
							result.add(nextField);
							coord = board.getFieldCoordinates(nextField.getFieldID());
							plannedNextField = getNextField(coord[0], coord[1], getDirection(nextField));
							break;
						}
					default:
						nextField = plannedNextField;
						result.add(nextField);
						coord = board.getFieldCoordinates(nextField.getFieldID());
						plannedNextField = getNextField(coord[0], coord[1], getDirection(nextField));
						break;
					}
				}
			}
			plannedNextField = getNextField(coord[0], coord[1], getDirection(nextField)+1);
		}
		result.add(board.getField(0, 0));
		return null;
	}

	public int getDistanceFromMid(Field f){
		Field nextField = f;
		int result = 0;
		int [] coord = new int[2];
		while(nextField != board.getField(0, 0)){
			coord = board.getFieldCoordinates(f.getFieldID());
			nextField = getNextField(coord[0], coord[1], getDirection(nextField)+1);
			result++;
		}
		return result;
	}


	public ArrayList<Field> getRing(Field f){
		ArrayList<Field> result = new ArrayList<Field>();
		int radius = getDistanceFromMid(f);
		Field nextField = board.getField(-radius, radius);
		int[] coord = new int[2];
		for(int i = 0; i< 6; i++){
			for(int j = 0; j< radius; j++){
				result.add(nextField);
				coord = board.getFieldCoordinates(nextField.getFieldID());
				nextField = getNextField(coord[0], coord[1], getDirection(nextField));
			}
		}
		return result;
	}



	public int getDirection(Field f) {
		int x = board.getFieldCoordinates(f.getFieldID())[0];
		int y = board.getFieldCoordinates(f.getFieldID())[1];
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

	public Field getNextField(int aX, int aY, int dir) {
//		Field[] result = board.getNeighbouringFields(aX, aY);
//		switch (dir) {
//		case 0:
//			return result[0];
//		case 1:
//			return result[1];
//		case 2:
//			return result[2];
//		case 3:
//			return result[3];
//		case 4:
//			return result[4];
//		case 5:
//			return result[5];
//		default:
			return null;
//		}
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



	public Field getHarbourSeaField(int u, int v, int x, int y){
		if(board.getField(u, v).getResourceType() == enums.ResourceType.SEA && board.getField(x, y).getResourceType() == enums.ResourceType.SEA){
			throw new IllegalArgumentException("Both Fields are are see Fields");
		}
		if(board.getField(u, v).getResourceType() == enums.ResourceType.SEA){
			return board.getField(u, v);
		}
		if(board.getField(x, y).getResourceType() == enums.ResourceType.SEA){
			return board.getField(x, y);
		}
		return null;
	}


	public static String[] getCornerFromEdge(String s){
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
