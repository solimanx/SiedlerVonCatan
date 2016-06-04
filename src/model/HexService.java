package model;

import java.util.Random;

import settings.DefaultSettings;

public class HexService {
	final static int[][] DIRECTIONS = { { 1, -1 }, { 1, 0 }, { 0, 1 }, { -1, 1 }, { -1, 0 }, { 0, -1 } };
	Board board;

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

	public int[] getCornerCoordinates(String field_ids) {
		// TODO write
		return null;
	}

	public int[] getEdgeCoordinates(String field_ids) {
		// TODO write
		return null;
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


	public static int sumAbsoluteValues(int[] temp) {
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

}
