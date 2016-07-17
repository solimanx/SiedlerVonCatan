package service;

import network.ProtocolToModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.board.Board;
import model.unit.Index;

public class HexService {
	private static Logger logger = LogManager.getLogger(HexService.class.getSimpleName());

	/**
	 * Get coorner coordinates given coordinates of three fields
	 * <p>
	 * // * @param x1 FIELD ONE X AXIAL COORDINATE // * @param y1 FIELD ONE Y
	 * AXIAL COORDINATE // * @param x2 FIELD TWO X AXIAL COORDINATE //* @param
	 * y2 FIELD TWO Y AXIAL COORDINATE //* @param x3 FIELD ONE X AXIAL
	 * COORDINATE //* @param y3 FIELD TWO Y AXIAL COORDINATE.
	 *
	 * @param a the a
	 * @param b the b
	 * @param c the c
	 * @return Corner Axial coordinate (X,Y,DIR)
	 */
	public static int[] getCornerCoordinates(String a, String b, String c) {
		if (Board.getCMap().get(a + b + c) != null) {
			return Board.getCMap().get(a + b + c);

		} else if (Board.getCMap().get(a + c + b) != null) {
			return Board.getCMap().get(a + c + b);

		} else if (Board.getCMap().get(b + c + a) != null) {
			return Board.getCMap().get(b + c + a);

		} else if (Board.getCMap().get(b + a + c) != null) {
			return Board.getCMap().get(b + a + c);

		} else if (Board.getCMap().get(c + a + b) != null) {
			return Board.getCMap().get(c + a + b);

		} else if (Board.getCMap().get(c + b + a) != null) {
			return Board.getCMap().get(c + b + a);
		} else {
			logger.warn("Throws new IllegalArgumentException,\"Corner ID\" " + a + b + c + " doesn't exist");
			throw new IllegalArgumentException("Corner ID " + a + b + c + "doesn't exist");
		}

	}

	/**
	 * Get edge coordinates given coordinates of two fields.
	 *
	 * @param a the a
	 * @param b the b
	 * @return Edge Axial coordinate (X,Y,DIR)
	 */
	public static int[] getEdgeCoordinates(String a, String b) {
		if (Board.getEMap().get(a + b) != null) {
			return Board.getEMap().get(a + b);

		} else if (Board.getEMap().get(b + a) != null) {
			return Board.getEMap().get(b + a);

		} else
			logger.warn("Throws new IllegalArgumentException,\"Edge ID\" " + a + b + " doesn't exist");
		throw new IllegalArgumentException("Edge ID " + a + b + "doesn't exist");
	}

	// ================================================================================
	// SPIRAL CALCULATIONS
	// ================================================================================

	/**
	 * returns all fieldIDs in the order of a spiral, beginning with the inputID
	 * and ending in the middle of the field. example: input: "N" output:
	 * "NIEFKOJ"
	 * <p>
	 * does not work with sea fields
	 *
	 * @param f the field
	 * @return the spiral
	 */
	public static String getSpiral(String f) {
		StringBuffer result = new StringBuffer();
		String nextField = f;
		String plannedNextField = f;
		int radius = getDistanceFromMid(f);
		int[] coord = new int[2];
		for (int i = radius; i > 0; i--) {
			for (int j = 0; j < 7; j++) {
				for (int k = 0; k < i; k++) {
					switch (j) {
					case 0:
						if (i == radius) {
							if (getRing(nextField).contains(plannedNextField)) {
								nextField = plannedNextField;
								result.append(nextField);
								coord = Board.getStringToCoordMap().get(nextField);
								plannedNextField = getNextField(coord[0], coord[1], getDirection(nextField));
								break;
							} else {
								continue;
							}
						} else {
							if (result.toString().contains(plannedNextField)) {
								continue;
							} else {
								nextField = plannedNextField;
								result.append(nextField);
								coord = Board.getStringToCoordMap().get(nextField);
								plannedNextField = getNextField(coord[0], coord[1], getDirection(nextField));
								break;
							}
						}
					case 6:
						if (result.toString().contains(plannedNextField)) {
							k = i;
							break;
						} else {
							nextField = plannedNextField;
							result.append(nextField);
							coord = Board.getStringToCoordMap().get(nextField);
							plannedNextField = getNextField(coord[0], coord[1], getDirection(nextField));
							break;
						}
					default:
						nextField = plannedNextField;
						result.append(nextField);
						coord = Board.getStringToCoordMap().get(nextField);
						plannedNextField = getNextField(coord[0], coord[1], getDirection(nextField));
						break;
					}
				}
			}
			plannedNextField = getNextField(coord[0], coord[1], getDirection(nextField) + 1);
		}
		result.append(Board.getCoordToStringMap().get(new Index(0, 0)));
		return result.toString();
	}

	/**
	 * returns all fieldIDs in the order of a spiral, beginning with the inputID
	 * and ending in the middle of the field.
	 *
	 * does not work with sea fields
	 *
	 * @param f the field
	 * @return the spiral
	 */
	public static String getSpiralFifeSix(String f) {
		StringBuffer result = new StringBuffer();
		String nextField = f;
		String plannedNextField = f;
		int radius = getDistanceFromMidFifeSix(f);
		int[] coord = new int[2];
		int smallSide = 0;
		for (int i = radius; i > 0; i--) {
			for (int j = 0; j < 7; j++) {
				for (int k = 0; k < i-smallSide; k++) {
					switch (j) {
					case 0:
						if (i == radius) {
							if (getRing(nextField).contains(plannedNextField)) {
								nextField = plannedNextField;
								result.append(nextField);
								coord = Board.getStringToCoordMap().get(nextField);
								plannedNextField = getNextField(coord[0], coord[1], getDirection(nextField));
								if(getDirection(nextField) == 1 || getDirection(nextField) == 4){
									smallSide = 1;
								}
								else {
									smallSide = 0;
								}
								break;
							} else {
								continue;
							}
						} else {
							if (result.toString().contains(plannedNextField)) {
								continue;
							} else {
								nextField = plannedNextField;
								result.append(nextField);
								coord = Board.getStringToCoordMap().get(nextField);
								plannedNextField = getNextField(coord[0], coord[1], getDirection(nextField));
								if(getDirection(nextField) == 1 || getDirection(nextField) == 4){
									smallSide = 1;
								}
								else {
									smallSide = 0;
								}
								break;
							}
						}
					case 6:
						if (result.toString().contains(plannedNextField)) {
							k = i;
							break;
						} else {
							nextField = plannedNextField;
							result.append(nextField);
							coord = Board.getStringToCoordMap().get(nextField);
							plannedNextField = getNextField(coord[0], coord[1], getDirection(nextField));
							if(getDirection(nextField) == 1 || getDirection(nextField) == 4){
								smallSide = 1;
							}
							else {
								smallSide = 0;
							}
							break;
						}
					default:
						nextField = plannedNextField;
						result.append(nextField);
						coord = Board.getStringToCoordMap().get(nextField);
						plannedNextField = getNextField(coord[0], coord[1], getDirection(nextField));
						if(getDirection(nextField) == 1 || getDirection(nextField) == 4){
							smallSide = 1;
						}
						else {
							smallSide = 0;
						}
						break;
					}
				}
			}
			plannedNextField = getNextField(coord[0], coord[1], getDirection(nextField) + 1);
		}
		return result.toString();
	}

	/**
	 * calculates the distance to center. example: (0,0) -> 0 (-2,2) -> 2.
	 *
	 * @param f            Field ID
	 * @return distance to mid or radius
	 */
	public static int getDistanceFromMid(String f) {
		String nextField = f;
		int result = 0;
		int[] coord;
		String end = "J";// Board.getCoordToStringMap().get(new Index(0, 0));
		while (!nextField.equals(end)) {
			coord = Board.getStringToCoordMap().get(nextField);
			nextField = getNextField(coord[0], coord[1], nextDirection(getDirection(nextField), 1));
			result++;
		}
		return result;
	}

	/**
	 * calculates the distance to center.
	 * @param f Field ID
	 * @return distance to mid corner
	 */
	public static int getDistanceFromMidFifeSix(String f) {
		String nextField = f;
		int result = 1;
		int[] coord;
		int[][] end = {{0,0},{0,1},{1,0},{1,-1}};// Board.getCoordToStringMap().get(new Index(0, 0));
		boolean found = false;
		while (!found) {
			coord = Board.getStringToCoordMap().get(nextField);
			nextField = getNextField(coord[0], coord[1], nextDirection(getDirection(nextField), 1));
			result++;
			for(int i = 0; i<end.length; i++){
				if(Board.getCoordToStringMap().get(new Index(end[i][0], end[i][1])) == nextField){
					found = true;
				}
			}
		}
		return result;
	}

	/**
	 * returns all fieldIDs, which are one the same ring as the inputID.
	 * example: input: "E" output: "NIEFKO"
	 *
	 * @param f
	 *            Field ID
	 * @return Field ID's
	 */
	public static String getRing(String f) {
		StringBuffer result = new StringBuffer();
		int radius = getDistanceFromMid(f);
		String nextField = Board.getCoordToStringMap().get(new Index(-radius, radius));
		int[] coord;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < radius; j++) {
				result.append(nextField);
				coord = Board.getStringToCoordMap().get(nextField);
				nextField = getNextField(coord[0], coord[1], getDirection(nextField));
			}
		}
		return result.toString();
	}

	/**
	 * calculates clockwise the direction of the next field.
	 *
	 * @param f            Field ID
	 * @return Edge Direction
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
		logger.warn("Throws new IllegalArgumentException,\"illegal Argument in HexService.getDirection()\" ");
		throw new IllegalArgumentException("illegal Argument in HexService.getDirection()");
	}

	/**
	 * calculates a field in the searched direction.
	 *
	 * @param aX            Field x-coordinate
	 * @param aY            Field y-coordinate
	 * @param dir            direction of the searched field
	 * @return Field ID of the next Field
	 */
	public static String getNextField(int aX, int aY, int dir) {
		String neighbours = getNeighboursID(aX, aY);
		char[] result = neighbours.toCharArray();
		if (result.length != 6) {
			logger.warn("Throws new IllegalArgumentException,\"getNextField dont works with this input\" ");
			throw new IllegalArgumentException("getNextField dont works with this input");
		}
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
			if (dir < 0 || dir > 5) {
				logger.warn("Throws new IllegalArgumentException,\"undefined direction\" ");
				throw new IllegalArgumentException("undefined direction");
			}
			a = null;
			break;
		}
		return a;
	}

	/**
	 * Get neighbouring fields IDs.
	 *
	 * @param aX the a X
	 * @param aY the a Y
	 * @return the neighbours ID
	 */
	private static String getNeighboursID(int aX, int aY) {

		Index ne = new Index(aX + 1, aY - 1);
		Index e = new Index(aX + 1, aY);
		Index se = new Index(aX, aY + 1);
		Index sw = new Index(aX - 1, aY + 1);
		Index w = new Index(aX - 1, aY);
		Index nw = new Index(aX, aY - 1);
		String northEast = Board.getCoordToStringMap().get(ne) != null ? Board.getCoordToStringMap().get(ne) : "";
		String east = Board.getCoordToStringMap().get(e) != null ? Board.getCoordToStringMap().get(e) : "";
		String southEast = Board.getCoordToStringMap().get(se) != null ? Board.getCoordToStringMap().get(se) : "";
		String southWest = Board.getCoordToStringMap().get(sw) != null ? Board.getCoordToStringMap().get(sw) : "";
		String west = Board.getCoordToStringMap().get(w) != null ? Board.getCoordToStringMap().get(w) : "";
		String northWest = Board.getCoordToStringMap().get(nw) != null ? Board.getCoordToStringMap().get(nw) : "";

		String neighbours = northEast + east + southEast + southWest + west + northWest;

		return neighbours;

	}

	/**
	 * calculates a legal value of direction.
	 *
	 * @param dir            original direction
	 * @param i            value of increment
	 * @return legal direction
	 */
	public static int nextDirection(int dir, int i) {
		int result = (dir + i) % 6;
		return result;
	}

	// ================================================================================
	// COORDINATE CONVERSIONS AND SUMS
	// ================================================================================

	/**
	 * Sums the absolute value of X, absolute value of Y, and absolute value of
	 * Z of the cube coordinate together.
	 *
	 * @param temp the temp
	 * @return the int
	 */
	public static int sumAbsCubeXYZ(int[] temp) {
		return Math.abs(temp[0]) + Math.abs(temp[1]) + Math.abs(temp[2]);
	}

	/**
	 * Converts Axial (x,y) to Cube (x,y,z).
	 *
	 * @param a            (x,y)
	 * @return the int[]
	 */
	public static int[] convertAxialToCube(int[] a) {

		if (a.length != 2) {
			logger.error("Error in HexService.convertAxialToCube");
			return null;

		} else {

			int x = a[0];
			int z = a[1];
			int y = -x - z;

			return new int[] { x, y, z };
		}
	}

	/**
	 * Converts Axial (x,y) to Cube (x,y,z).
	 *
	 * @param a            (x,y)
	 * @return the int[]
	 */
	public static int[] specialConvertAxialToCube(int[] a) {

		if (a.length != 2) {
			logger.error("Error in HexService.convertAxialToCube");
			return null;

		} else {

			int x = a[0];
			int y = a[1];
			int z = -x - y;

			return new int[] { x, y, z };
		}
	}

	/**
	 * Converts Cube (x,y,z) to Axial (x,y).
	 *
	 * @param c            (x,y,z)
	 * @return the int[]
	 */
	public static int[] convertCubeToAxial(int[] c) {
		if (c.length != 3) {
			logger.error("Error in HexService.convertCubeToAxial");
			return null;
		} else {
			int x = c[0];
			int y = c[2];
			return new int[] { x, y };
		}
	}

	/**
	 * Sums the X and Y of the cube coordinate.
	 *
	 * @param subarray the subarray
	 * @return the int
	 */
	public static int sumOfCubeXY(int[] subarray) {
		return subarray[0] + subarray[1];
	}

	// ================================================================================
	// WORKAROUNDS
	// ================================================================================

	/**
	 * returns both adjacent corners of an edge.
	 *
	 * @param s            Field ID of two Fields defining the edge
	 * @return two Strings of 3 characters defining the upper and lower corner
	 */
	public static String[] getCornerFromEdge(String s) {
		if (s.length() != 2) {
			logger.warn(
					"Throws new IllegalArgumentException,\"Too many or too few Fields as input in HexService.getCornerFromEdge\" ");
			throw new IllegalArgumentException("Too many or too few Fields as input in HexService.getCornerFromEdge");
		}
		int[] a = Board.getStringToCoordMap().get(s.substring(0, 1));
		int[] b = Board.getStringToCoordMap().get(s.substring(1, 2));
		String fields1 = getNeighboursID(a[0], a[1]);
		String fields2 = getNeighboursID(b[0], b[1]);

		StringBuffer common = new StringBuffer();

		for (int i = 0; i < fields1.length(); i++) {
			for (int j = 0; j < fields2.length(); j++) {
				if (fields1.charAt(i) == fields2.charAt(j)) {
					common.append(fields1.charAt(i));
					break;
				}
			}
		}
		String[] result = new String[2];
		result[0] = s.substring(0, 1) + s.substring(1, 2) + common.substring(0, 1);
		result[1] = s.substring(0, 1) + s.substring(1, 2) + common.substring(1, 2);
		return result;
	}

	/**
	 * Gets the corner from edge.
	 *
	 * @param i the i
	 * @return the corner from edge
	 */
	public static String[] getCornerFromEdge(Index[] i) {
		String a = ProtocolToModel.getProtocolOneID(i[0]);
		String b = ProtocolToModel.getProtocolOneID(i[1]);
		return getCornerFromEdge(a + b);
	}

}
