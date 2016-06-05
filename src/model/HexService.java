package model;

import java.util.Random;
import java.util.ArrayList;

import settings.DefaultSettings;

public class HexService {
	final static int[][] DIRECTIONS = { { 1, -1 }, { 1, 0 }, { 0, 1 }, { -1, 1 }, { -1, 0 }, { 0, -1 } };
	BoardNew board;

	public static int[] getFieldCoordinates(String field_id) {
		// filter bad input
		if (field_id.length() == 1) {
			// water coordinates
			if (field_id.matches("[a-r]")) {
				switch (field_id) {
				case "a":
					return new int[] { 0, -3 };
				case "b":
					return new int[] { 1, -3 };
				case "c":
					return new int[] { 2, -3 };
				case "d":
					return new int[] { 3, -3 };
				case "e":
					return new int[] { -1, -2 };
				case "f":
					return new int[] { 3, -2 };
				case "g":
					return new int[] { -2, -1 };
				case "h":
					return new int[] { 3, -1 };
				case "i":
					return new int[] { -3, 0 };
				case "j":
					return new int[] { 3, 0 };
				case "k":
					return new int[] { -3, 1 };
				case "l":
					return new int[] { 2, 1 };
				case "m":
					return new int[] { -3, 2 };
				case "n":
					return new int[] { 1, 2 };
				case "o":
					return new int[] { -3, 3 };
				case "p":
					return new int[] { -2, 3 };
				case "q":
					return new int[] { -1, 3 };
				case "r":
					return new int[] { 0, 3 };
				}
			}
			// field coordinates
			else if (field_id.matches("[A-S]")) {
				switch (field_id) {
				case "A":
					return new int[] { 0, -2 };
				case "B":
					return new int[] { 1, -2 };
				case "C":
					return new int[] { 2, -2 };
				case "D":
					return new int[] { -1, -1 };
				case "E":
					return new int[] { 0, -1 };
				case "F":
					return new int[] { 1, -1 };
				case "G":
					return new int[] { 2, -1 };
				case "H":
					return new int[] { -2, 0 };
				case "I":
					return new int[] { -1, 0 };
				case "J":
					return new int[] { 0, 0 };
				case "K":
					return new int[] { 1, 0 };
				case "L":
					return new int[] { 2, 0 };
				case "M":
					return new int[] { -2, 1 };
				case "N":
					return new int[] { -1, 1 };
				case "O":
					return new int[] { 0, 1 };
				case "P":
					return new int[] { 1, 1 };
				case "Q":
					return new int[] { -2, 2 };
				case "R":
					return new int[] { -1, 2 };
				case "S":
					return new int[] { 0, 2 };
				}
			}
		}
		// wrong id
		return null;

	}

	public int[] getCornerCoordinates(int x1, int y1, int x2, int y2, int x3, int y3) {
		Corner[] a = board.getSurroundingCorners(x1,y1);
		Corner[] b = board.getSurroundingCorners(x1,y1);
		Corner[] c = board.getSurroundingCorners(x1,y1);
		ArrayList<Corner> lista = new ArrayList<Corner>();
		ArrayList<Corner> listb = new ArrayList<Corner>();
		ArrayList<Corner> lastCorner = new ArrayList<Corner>();
		for(int i = 0; i < a.length; i++){
			lista.add(a[i]);
		}
		for(int i= 0; i < b.length; i++){
			if(lista.contains(b[i])){
				listb.add(b[i]);
			}
		}
		for(int i= 0; i < c.length; i++){
			if(listb.contains(c[i])){
				lastCorner.add(c[i]);
			}
		}
		if(board.getCornerAt(x1, y1, 1) == lastCorner.get(0)){
			int[] result = {x1, y1, 1};
			return result;
		}
		if (board.getCornerAt(x3, y3, 0) == lastCorner.get(0)){
			int[] result = {x3, y3, 0};
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
		for(int i = 0; i < a.length; i++){
			firstList.add(a[i]);
		}
		for(int j = 0; j<b.length; j++){
			if(firstList.contains(b[j])){
				resultList.add(b[j]);
			}
		}
		for(int i = 0; i<3; i++){
			if(board.getEdgeAt(x1, y1, i) == resultList.get(0)){
				int[] result = {x1, y1, i};
				return result;
			}
			if(board.getEdgeAt(x2, y2, i) == resultList.get(0)){
				int[] result = {x2, y2, i};
				return result;
			}
		}
		return null;
	}
	
	public Edge[] getSurroundingEdges(int aX, int aY){
		Edge n = board.getEdgeAt(aX, aY, 0);
		Edge ne = board.getEdgeAt(aX + 1, aY - 1, 1);
		Edge se = board.getEdgeAt(aX, aY + 1, 0);
		Edge s = board.getEdgeAt(aX, aY, 1);
		Edge sw = board.getEdgeAt(aX - 1, aY + 1, 0);
		Edge nw = board.getEdgeAt(aX, aY - 1, 1);
		Edge[] surroundingEdges = { n, ne, se, s, sw, nw };
		return surroundingEdges;
	}
	

//	public String getSpiral(String starting_point_id) {
//		// Starting point;
//		int x = 0;
//		int y = 0;
//		int radius = DefaultSettings.BOARD_SIZE / 2;
//		String spiral_sequence = board.getFieldAt(x, y).getFieldID();
//
//		// Random direction between 0 and 5;
//		int random = new Random().nextInt(6);
//		int[] rd = DIRECTIONS[random];
//		x += rd[0];
//		y += rd[1];
//		int starting_x;
//		int starting_y;
//		// from 0 to radius-1
//		for (int i = 0; i < radius; i++) {
//			x += rd[0];
//			y += rd[1];
//			starting_x = x;
//			starting_y = y;
//
//			rd = DIRECTIONS[random];
//			// insert ID of new block to beginning of string
//			spiral_sequence = board.getFieldAt(x, y).getFieldID() + spiral_sequence;
//			do {
//				if (x - radius == 0) {
//					if (y - radius > 0) {
//						x += DIRECTIONS[0][0];
//						y += DIRECTIONS[0][1];
//					} else if (y - radius < 0) {
//						x += DIRECTIONS[3][0];
//						y += DIRECTIONS[3][1];
//					}
//				} else if (x - radius > 0) {
//					if (y - radius > 0) {
//						x += DIRECTIONS[4][0];
//						y += DIRECTIONS[4][1];
//					} else if (y - radius < 0) {
//						x += DIRECTIONS[1][0];
//						y += DIRECTIONS[1][1];
//					} else if (y - radius == 0) {
//						x += DIRECTIONS[5][0];
//						y += DIRECTIONS[5][1];
//					}
//				} else if (x - radius < 0) {
//					if (y - radius == 0) {
//						x += DIRECTIONS[2][0];
//						y += DIRECTIONS[2][1];
//					}
//
//				} else {
//					// for testing purposes
//				}
//				spiral_sequence = board.getFieldAt(x, y).getFieldID() + spiral_sequence;
//				// get next direction
//
//			} while (x != starting_x && y != starting_y);
//			random = random == 5 ? 0 : random + 1;
//		}
//
//		return spiral_sequence;
//
//	}


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
			System.out.println("Unable to convert: " + a.toString() + " to cube.");
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
			System.out.println("Unable to convert: " + c.toString() + " to axial.");
			return null;
		} else {
			int x = c[0];
			int y = c[2];
			return new int[] { x, y };
		}
	}
	
	public static int sumOfCubeXY(int[] subarray){
		return subarray[0]+subarray[1];
	}

}
