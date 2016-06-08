package model;

import java.util.Random;

import model.objects.Corner;
import model.objects.Edge;

import java.util.ArrayList;

import settings.DefaultSettings;

public class HexService {
	final static int[][] DIRECTIONS = { { 1, -1 }, { 1, 0 }, { 0, 1 }, { -1, 1 }, { -1, 0 }, { 0, -1 } };
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

	// public String getSpiral(String starting_point_id) {
	// // Starting point;
	// int x = 0;
	// int y = 0;
	// int radius = DefaultSettings.BOARD_SIZE / 2;
	// String spiral_sequence = board.getFieldAt(x, y).getFieldID();
	//
	// // Random direction between 0 and 5;
	// int random = new Random().nextInt(6);
	// int[] rd = DIRECTIONS[random];
	// x += rd[0];
	// y += rd[1];
	// int starting_x;
	// int starting_y;
	// // from 0 to radius-1
	// for (int i = 0; i < radius; i++) {
	// x += rd[0];
	// y += rd[1];
	// starting_x = x;
	// starting_y = y;
	//
	// rd = DIRECTIONS[random];
	// // insert ID of new block to beginning of string
	// spiral_sequence = board.getFieldAt(x, y).getFieldID() + spiral_sequence;
	// do {
	// if (x - radius == 0) {
	// if (y - radius > 0) {
	// x += DIRECTIONS[0][0];
	// y += DIRECTIONS[0][1];
	// } else if (y - radius < 0) {
	// x += DIRECTIONS[3][0];
	// y += DIRECTIONS[3][1];
	// }
	// } else if (x - radius > 0) {
	// if (y - radius > 0) {
	// x += DIRECTIONS[4][0];
	// y += DIRECTIONS[4][1];
	// } else if (y - radius < 0) {
	// x += DIRECTIONS[1][0];
	// y += DIRECTIONS[1][1];
	// } else if (y - radius == 0) {
	// x += DIRECTIONS[5][0];
	// y += DIRECTIONS[5][1];
	// }
	// } else if (x - radius < 0) {
	// if (y - radius == 0) {
	// x += DIRECTIONS[2][0];
	// y += DIRECTIONS[2][1];
	// }
	//
	// } else {
	// // for testing purposes
	// }
	// spiral_sequence = board.getFieldAt(x, y).getFieldID() + spiral_sequence;
	// // get next direction
	//
	// } while (x != starting_x && y != starting_y);
	// random = random == 5 ? 0 : random + 1;
	// }
	//
	// return spiral_sequence;
	//
	// }

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

}
