package ai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import model.Board;
import model.GameLogic;
import model.objects.Corner;
import model.objects.Edge;
import model.objects.Field;
import model.objects.PlayerModel;
import network.ProtocolToModel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ai.agents.OpponentAgent;
import ai.agents.ResourceAgent;
import settings.DefaultSettings;

// TODO: Auto-generated Javadoc
/**
 * Primivite AI that can connect to server, play the initial rounds, build two
 * villages and two roadss.
 */
public class PrimitiveAI extends Thread {

	// ================================================================================
	// CLASS FIELDS
	// ================================================================================

	// --------------------------------------------------------------------------------
	// COORDINATES STORAGE
	// --------------------------------------------------------------------------------

	// 3-part corner coordinate (e.g (0,0,1)
	private int[] firstVillageLocation;
	private int[] secondVillageLocation;
	// 3-part edge coordinate (e.g (0,1,0)
	private int[] firstRoadLocation;
	private int[] secondRoadLocation;

	// --------------------------------------------------------------------------------
	// CONNECTION FIELDS
	// --------------------------------------------------------------------------------

	private Socket socket;
	private final String SERVERHOST;// = "localhost";// "aruba.dbs.ifi.lmu.de";
	private final int PORT;// = 8080;// 10001;

	private final String PROTOCOL = DefaultSettings.PROTOCOL_VERSION;
	private final String VERSION = DefaultSettings.AI_VERSION;

	// --------------------------------------------------------------------------------
	// I/O FIELDS
	// --------------------------------------------------------------------------------
	private OutputStreamWriter writer;
	private BufferedReader reader;
	protected AIInputHandler pI = new AIInputHandler(this);
	protected AIOutputHandler pO = new AIOutputHandler(this);
	private static Logger logger = LogManager.getLogger(PrimitiveAI.class.getSimpleName());

	// --------------------------------------------------------------------------------
	// PROFILE FIELDS
	// --------------------------------------------------------------------------------
	private PlayerModel me;
	private int ID;
	private int colorCounter = 0;
	private boolean started = false;
	private GameLogic gl;

	private Board board;

	// ================================================================================
	// CONSTRUCTORS
	// ================================================================================

	/**
	 * Creates a PrimitiveAI object, and forces it to connect to the 0.3 server.
	 *
	 * @param serverHost the server host
	 * @param port the port
	 */
	public PrimitiveAI(String serverHost, int port) {
		// logger.info("AI started");
		this.SERVERHOST = serverHost;
		this.PORT = port;
		this.board = new Board();
		this.gl = new GameLogic(board);

	}

	// ================================================================================
	// CONNECTION METHODS
	// ================================================================================

	/**
	 * Commence.
	 */
	public void commence() {
		System.out.println(DefaultSettings.getCurrentTime() + " AI started.");
		this.start();
	}

	/**
	 * Initializing socket, writer and reader, then attempt connection.
	 */
	@Override
	public void run() {
		try {
			socket = new Socket(SERVERHOST, PORT);
			writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			System.out.println(DefaultSettings.getCurrentTime() + " AI connected to server.");
			// logger.info(" AI connected to server.");
			read();
		} catch (IOException e) {
			logger.error("Input/Output Excepton", e);
			logger.catching(Level.ERROR, e);
			System.out.println("Connection to server failed.");
			logger.warn("Connection to server failed");

			try {
				Thread.sleep(2000);
			} catch (InterruptedException ie) {
				logger.error("Interrupted Exception", ie);
				logger.catching(Level.ERROR, ie);
				ie.printStackTrace();
			}
		}
	}

	/**
	 * Reading input from the servers output.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void read() throws IOException {
		// logger.warn("Reading input from the servers output(read()) throws
		// IOException");
		String line;
		while ((line = reader.readLine()) != null) {
			System.out.println(DefaultSettings.getCurrentTime() + " Server: " + line);
			// logger.debug("Server"+ line);
			pI.sendToParser(line);
		}
	}

	/**
	 * Output to server.
	 *
	 * @param json the json
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void write(String json) throws IOException {
		// logger.warn("Writing output to server, throws IOException");
		System.out.println(DefaultSettings.getCurrentTime() + "    A.I: " + json);
		// logger.info(DefaultSettings.getCurrentTime()+ " A.I: "+ json);
		writer.write(json + "\n");
		writer.flush();
	}

	// ================================================================================
	// PRIMITIVE AI LOGIC
	// ================================================================================

	/**
	 * Builds the initial (free) village at the first legal empty location. Used
	 * for the first round (2 turns)
	 */
	public void initialVillage() {
		int radius = DefaultSettings.BOARD_RADIUS;

		// VILLAGE
		// axial y coordinate loop
		outerloop: for (int i = -radius; i <= radius; i++) {
			// axial x coordinate loop
			for (int j = -radius; j <= radius; j++) {
				// corner direction loop
				for (int k = 0; k < 2; k++) {
					// if corner exists and is unoccupied
					if (gl.checkBuildInitialVillage(j, i, k)) {
						if (firstVillageLocation == null) {
							firstVillageLocation = new int[] { j, i, k };
						} else {
							secondVillageLocation = new int[] { j, i, k };
						}
						pO.requestBuildVillage(j, i, k);
						break outerloop;

					}
				}
			}
		}

	}

	/**
	 * Builds the initial (free) road at the first legal empty location,
	 * depending on the initial village location. Used for the first round (2
	 * turns)
	 */
	public void initialRoad() {
		int x, y, dir;
		// First road
		if (secondVillageLocation == null) {
			x = firstVillageLocation[0];
			y = firstVillageLocation[1];
			dir = firstVillageLocation[2];
			// find first non null road
			if (dir == 0) {
				if (gl.checkBuildInitialStreet(x, y - 1, 2, ID)) {
					pO.requestBuildRoad(x, y - 1, 2);
					firstRoadLocation = new int[] { x, y - 1, 2 };
				} else if (gl.checkBuildInitialStreet(x, y, 1, ID)) {
					pO.requestBuildRoad(x, y, 1);
					firstRoadLocation = new int[] { x, y, 1 };
				} else if (gl.checkBuildInitialStreet(x, y, dir, ID)) {
					pO.requestBuildRoad(x, y, dir);
					firstRoadLocation = new int[] { x, y, dir };
				} else {
					// logger.warn("Throws new IllegalArgumentException,\"Error
					// at PrimitiveAI.initialRound()1\" ");
					throw new IllegalArgumentException("Error at PrimitiveAI.initialRound()1");
				}
			} else if (dir == 1) {
				if (gl.checkBuildInitialStreet(x, y + 1, 0, ID)) {
					pO.requestBuildRoad(x, y + 1, 0);
					firstRoadLocation = new int[] { x, y + 1, 0 };
				} else if (gl.checkBuildInitialStreet(x - 1, y - 1, 2, ID)) {
					pO.requestBuildRoad(x - 1, y - 1, 2);
					firstRoadLocation = new int[] { x - 1, y - 1, 2 };
				} else if (gl.checkBuildInitialStreet(x - 1, y - 1, dir, ID)) {
					pO.requestBuildRoad(x - 1, y - 1, dir);
					firstRoadLocation = new int[] { x - 1, y - 1, dir };
				} else {
					// logger.warn("Throws new IllegalArgumentException,\"Error
					// at PrimitiveAI.initialRound()2\" ");
					throw new IllegalArgumentException("Error at PrimitiveAI.initialRound()2");
				}

			} else {
				// logger.warn("Throws new IllegalArgumentException,\"Error at
				// PrimitiveAI.initialRound()3\" ");
				throw new IllegalArgumentException("Error at PrimitiveAI.initialRound()3");
			}

		} // Second road
		else {
			x = secondVillageLocation[0];
			y = secondVillageLocation[1];
			dir = secondVillageLocation[2];

			// find first non null road
			if (dir == 0) {
				if (gl.checkBuildInitialStreet(x, y - 1, 2, ID)) {
					secondRoadLocation = new int[] { x, y - 1, 2 };
					pO.requestBuildRoad(x, y - 1, 2);
				} else if (gl.checkBuildInitialStreet(x, y, 1, ID)) {
					secondRoadLocation = new int[] { x, y, 1 };
					pO.requestBuildRoad(x, y, 1);
				} else if (gl.checkBuildInitialStreet(x, y, dir, ID)) {
					secondRoadLocation = new int[] { x, y, dir };
					pO.requestBuildRoad(x, y, dir);
				} else {
					// logger.warn("Throws new IllegalArgumentException,\"Error
					// at PrimitiveAI.initialRound()\" ");
					throw new IllegalArgumentException("Error at PrimitiveAI.initialRound()");
				}
			} else if (dir == 1) {
				if (gl.checkBuildInitialStreet(x, y + 1, 0, ID)) {
					secondRoadLocation = new int[] { x, y + 1, 0 };
					pO.requestBuildRoad(x, y + 1, 0);
				} else if (gl.checkBuildInitialStreet(x - 1, y + 1, 2, ID)) {
					secondRoadLocation = new int[] { x - 1, y + 1, 2 };
					pO.requestBuildRoad(x - 1, y + 1, 2);
				} else if (gl.checkBuildInitialStreet(x - 1, y + 1, dir, ID)) {
					secondRoadLocation = new int[] { x - 1, y + 1, dir };
					pO.requestBuildRoad(x - 1, y + 1, dir);
				} else {
					// logger.warn("Throws new IllegalArgumentException,\"Error
					// at PrimitiveAI.initialRound()\" ");
					throw new IllegalArgumentException("Error at PrimitiveAI.initialRound()");
				}

			} else {
				logger.warn("Throws new IllegalArgumentException,\"Error at PrimitiveAI.initialRound()\" ");
				throw new IllegalArgumentException("Error at PrimitiveAI.initialRound()");
			}
		}
	}

	/**
	 * Randomly moving robber position.
	 */
	protected void moveRobber() {
		String robber = gl.getBoard().getBandit();
		// choose random character between A-S
		String alphabet = "ABCDEFGHIJKLMNOPQRS";
		int n = alphabet.length();
		String newRobber = new String();
		do {
			newRobber = alphabet.charAt(new Random().nextInt(n)) + "";
		} while (newRobber.equals(robber));

		// send to output
		pO.respondMoveRobber(newRobber, null);

	}

	/**
	 * Giving up half of resources by order.
	 */
	protected void loseToBandit() {
		// Count all my resources
		int[] myResources = getMe().getResources().clone();
		int sum = 0;

		for (int i = 0; i < 5; i++)
			sum += getMe().getResourceAmountOf(i);
		// loss is half of sum
		int loss = sum / 2;
		// losses array
		int[] losses = { 0, 0, 0, 0, 0 };

		// until losses amount is reached
		while (loss > 0) {
			// scan every resource
			for (int j = 0; j < 5; j++) {
				// if there's some of it
				if (myResources[j] > 0) {
					// decrement it from your list
					myResources[j]-= 1;
					// increment it to losses array
					losses[j]++;
					loss -= 1;
					// check if losses amount is reached
					break;
				}
				// if there's none of it
				else {
					// check the next resource type
					continue;
				}

			}
		}

		// send the losses to the output handler
		pO.respondRobberLoss(losses);

	}

	// ================================================================================
	// BOARD UPDATES
	// ================================================================================

	/**
	 * Initialize board.
	 *
	 * @param fields the fields
	 * @param corners the corners
	 * @param streets the streets
	 * @param harbourCorners the harbour corners
	 * @param banditLocation the bandit location
	 */
	protected void updateBoard(Field[] fields, Corner[] corners, ArrayList<Edge> streets, Corner[] harbourCorners,
			String banditLocation) {

		this.me = new PlayerModel(ID);
		for (Field f : fields) {
			String location = f.getFieldID();
			int[] coords = ProtocolToModel.getFieldCoordinates(location);
			Field bField = gl.getBoard().getFieldAt(coords[0], coords[1]);
			bField.setFieldID(location);
			bField.setDiceIndex(f.getDiceIndex());
			;
			bField.setResourceType(f.getResourceType());
		}
		for (Corner c : corners) {
			String location = c.getCornerID();
			int coords[] = ProtocolToModel.getCornerCoordinates(location);
			Corner bCorner = gl.getBoard().getCornerAt(coords[0], coords[1], coords[2]);
			bCorner.setCornerID(location);
			bCorner.setOwnerID(c.getOwnerID());
			bCorner.setStatus(c.getStatus());
		}
		for (Edge s : streets) {
			String location = s.getEdgeID();
			int coords[] = ProtocolToModel.getEdgeCoordinates(location);
			Edge bEdge = gl.getBoard().getEdgeAt(coords[0], coords[1], coords[2]);
			bEdge.setEdgeID(location);
			bEdge.setHasStreet(s.isHasStreet());
			bEdge.setOwnedByPlayer(s.getOwnerID());
		}
		for (Corner c : harbourCorners) {
			String location = c.getCornerID();
			int[] coords = ProtocolToModel.getCornerCoordinates(location);
			Corner bCorner = gl.getBoard().getCornerAt(coords[0], coords[1], coords[2]);
			bCorner.setCornerID(location);
			bCorner.setHarbourStatus(c.getHarbourStatus());
		}

		gl.getBoard().setBandit(banditLocation);
	}

	/**
	 * Updates a new village in the board.
	 *
	 * @param x
	 *            Axial-x corner coordinate
	 * @param y
	 *            Axial-y corner coordinate
	 * @param dir
	 *            corner direction
	 * @param playerID
	 *            owner
	 */
	protected void updateVillage(int x, int y, int dir, int playerID) {
		Corner c = gl.getBoard().getCornerAt(x, y, dir);
		c.setStatus(enums.CornerStatus.VILLAGE);
		c.setOwnerID(playerID);
		Corner[] neighbors = gl.getBoard().getAdjacentCorners(x, y, dir);
		for (int i = 0; i < neighbors.length; i++) {
			if (neighbors[i] != null) {
				neighbors[i].setStatus(enums.CornerStatus.BLOCKED);
			}
		}
	}

	/**
	 * Updates a new road in the board.
	 *
	 * @param i
	 *            Axial-x edge coordinate
	 * @param j
	 *            Axial-y edge coordinate
	 * @param k
	 *            edge direction
	 * @param playerID
	 *            owner
	 */
	protected void updateRoad(int i, int j, int k, int playerID) {
		Edge e = gl.getBoard().getEdgeAt(i, j, k);
		e.setHasStreet(true);
		e.setOwnedByPlayer(playerID);

	}

	/**
	 * Updates a new city in the board.
	 *
	 * @param i
	 *            Axial-x corner coordinate
	 * @param j
	 *            Axial-y corner coordinate
	 * @param k
	 *            corner direction
	 * @param playerID
	 *            owner
	 */
	protected void updateCity(int i, int j, int k, int playerID) {
		Corner c = gl.getBoard().getCornerAt(i, j, k);
		c.setStatus(enums.CornerStatus.CITY);

	}

	/**
	 * Update robber.
	 *
	 * @param locationID the location ID
	 */
	protected void updateRobber(String locationID) {
		gl.getBoard().setBandit(locationID);

	}

	// ================================================================================
	// GETTERS AND SETTERS
	/**
	 * Gets the output.
	 *
	 * @return the output
	 */
	// ================================================================================
	protected AIOutputHandler getOutput() {
		return this.pO;
	}

	/**
	 * Gets the input.
	 *
	 * @return the input
	 */
	protected AIInputHandler getInput() {
		return this.pI;

	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	protected String getVersion() {
		return VERSION;
	}

	/**
	 * Gets the protocol.
	 *
	 * @return the protocol
	 */
	protected String getProtocol() {
		return PROTOCOL;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getID() {
		return ID;
	}

	/**
	 * Sets the id.
	 *
	 * @param playerID the new id
	 */
	protected void setID(int playerID) {
		ID = playerID;

	}

	/**
	 * Gets the color counter.
	 *
	 * @return the color counter
	 */
	protected int getColorCounter() {
		return colorCounter;
	}

	/**
	 * Sets the color counter.
	 *
	 * @param colorCounter the new color counter
	 */
	protected void setColorCounter(int colorCounter) {
		this.colorCounter = colorCounter;
	}

	/**
	 * Checks if is started.
	 *
	 * @return true, if is started
	 */
	protected boolean isStarted() {
		return started;
	}

	/**
	 * Sets the started.
	 *
	 * @param started the new started
	 */
	protected void setStarted(boolean started) {
		this.started = started;
	}

	/**
	 * Gets the first village location.
	 *
	 * @return the first village location
	 */
	protected int[] getFirstVillageLocation() {
		return firstVillageLocation;
	}

	/**
	 * Gets the second village location.
	 *
	 * @return the second village location
	 */
	protected int[] getSecondVillageLocation() {
		return secondVillageLocation;
	}

	/**
	 * Gets the first road location.
	 *
	 * @return the first road location
	 */
	protected int[] getFirstRoadLocation() {
		return firstRoadLocation;
	}

	/**
	 * Gets the second road location.
	 *
	 * @return the second road location
	 */
	protected int[] getSecondRoadLocation() {
		return secondRoadLocation;
	}

	/**
	 * Gets the me.
	 *
	 * @return the me
	 */
	public PlayerModel getMe() {
		return me;
	}

	/**
	 * Gets the gl.
	 *
	 * @return the gl
	 */
	public GameLogic getGl() {
		return gl;
	}

	/**
	 * Gets the resource agent.
	 *
	 * @return the resource agent
	 */
	public ResourceAgent getResourceAgent() {
		return null;

	}

	/**
	 * Actuate.
	 */
	public void actuate() {
		// TODO Auto-generated method stub

	}

	/**
	 * Update cards.
	 */
	public void updateCards() {
		// TODO Auto-generated method stub

	}

	public OpponentAgent getOpponentAgent() {
		return null;

	}
}
