package ai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import model.Board;
import model.GameLogic;
import model.objects.Corner;
import model.objects.Edge;
import model.objects.Field;
import network.ProtocolToModel;
import network.client.client.Client;
import network.client.client.ClientInputHandler;
import network.client.controller.ClientController;
import settings.DefaultSettings;

/**
 * Primivite AI that can connect to server, play the initial rounds, build two
 * villages and two roadss.
 */
public class PrimitiveAI extends Thread {
	// 3-part corner coordinate (e.g (0,0,1)
	private int[] firstVillageLocation;
	private int[] secondVillageLocation;
	// 3-part edge coordinate (e.g (0,1,0)
	@SuppressWarnings("unused")
	private int[] firstRoadLocation;
	@SuppressWarnings("unused")
	private int[] secondRoadLocation;

	private boolean scanning = true;
	private static int connectionTry = 0;

	private Socket socket;
	private final String PROTOCOL = "0.3";
	private final String VERSION = "NiedlichePixel 0.3 (KI)";

	private final String SERVERHOST = "aruba.dbs.ifi.lmu.de";
	private final int PORT = 10000;

	private OutputStreamWriter writer;
	private BufferedReader reader;

	// own ID in server
	@SuppressWarnings("unused")
	private int ID;

	private int colorCounter = 0;

	private boolean started = false;

	// Handlers

	private PrimitiveAIInputHandler pI = new PrimitiveAIInputHandler(this);
	private PrimitiveAIOutputHandler pO = new PrimitiveAIOutputHandler(this);

	// USELESS GAMELOGIC/BOARD ONLY TO SEE CODE
	private GameLogic gl;
	@SuppressWarnings("unused")
	private ClientController cc;
	private Board board;

	public PrimitiveAI() {
		System.out.println(DefaultSettings.getCurrentTime() + " AI started.");
		this.board = new Board();
		this.gl = new GameLogic(board);
		this.start();

	}

	@Override
	public void run() {
		while (scanning && connectionTry < 10) {
			try {
				socket = new Socket(SERVERHOST, PORT);
				writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				scanning = false;
				System.out.println(DefaultSettings.getCurrentTime() + " AI connected to server.");
				read();
			} catch (IOException e) {
				System.out.println("Connection to server failed." + " Attempt:" + connectionTry + 1);
				connectionTry++;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}
	}

	private void read() throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			System.out.println(DefaultSettings.getCurrentTime() + " Server: " + line);
			pI.sendToParser(line);
		}
	}

	public void write(String json) throws IOException {
		System.out.println(DefaultSettings.getCurrentTime() + " Client:" + json);
		writer.write(json + "\n");
		writer.flush();
	}

	/**
	 * Builds a village at the first legal empty location, then attaches a roads
	 * to it, then repeats it.
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
						pO.requestBuildInitialVillage(j, i, k);
						System.out.println("Building at " + j + " " + i + " " + k);
						break outerloop;

					}
				}
			}
		}

	}

	public void initialRoad() {
		int x, y, dir;
		if (secondVillageLocation == null) {
			x = firstVillageLocation[0];
			y = firstVillageLocation[1];
			dir = firstVillageLocation[2];

			// find first non null road
			if (dir == 0) {
				if (gl.checkBuildInitialStreet(x, y - 1, 2, ID)) {
					pO.requestBuildInitialRoad(x, y, dir);
					firstRoadLocation = new int[] { x, y - 1, 2 };
				} else if (gl.checkBuildInitialStreet(x, y, 1, ID)) {
					pO.requestBuildInitialRoad(x, y, dir);
					firstRoadLocation = new int[] { x, y, 1 };
				} else if (gl.checkBuildInitialStreet(x, y, dir, ID)) {
					pO.requestBuildInitialRoad(x, y, dir);
					firstRoadLocation = new int[] { x, y, dir };
				} else {
					throw new IllegalArgumentException("Error at PrimitiveAI.initialRound()");
				}
			} else if (dir == 1) {
				if (gl.checkBuildInitialStreet(x, y + 1, 0, ID)) {
					pO.requestBuildInitialRoad(x, y + 1, 0);
					firstRoadLocation = new int[] { x, y + 1, 0 };
				} else if (gl.checkBuildInitialStreet(x - 1, y - 1, 2, ID)) {
					pO.requestBuildInitialRoad(x - 1, y - 1, 2);
					firstRoadLocation = new int[] { x - 1, y - 1, 2 };
				} else if (gl.checkBuildInitialStreet(x - 1, y - 1, dir, ID)) {
					pO.requestBuildInitialRoad(x - 1, y - 1, dir);
					firstRoadLocation = new int[] { x - 1, y - 1, dir };
				} else {
					throw new IllegalArgumentException("Error at PrimitiveAI.initialRound()");
				}

			} else {
				throw new IllegalArgumentException("Error at PrimitiveAI.initialRound()");
			}

		} else {
			x = secondVillageLocation[0];
			y = secondVillageLocation[1];
			dir = secondVillageLocation[2];

			// find first non null road
			if (dir == 0) {
				if (gl.checkBuildInitialStreet(x, y - 1, 2, ID)) {
					secondRoadLocation = new int[] { x, y - 1, 2 };
					pO.requestBuildInitialRoad(x, y, dir);
				} else if (gl.checkBuildInitialStreet(x, y, 1, ID)) {
					secondRoadLocation = new int[] { x, y, 1 };
					pO.requestBuildInitialRoad(x, y, dir);
				} else if (gl.checkBuildInitialStreet(x, y, dir, ID)) {
					secondRoadLocation = new int[] { x, y, dir };
					pO.requestBuildInitialRoad(x, y, dir);
				} else {
					throw new IllegalArgumentException("Error at PrimitiveAI.initialRound()");
				}
			} else if (dir == 1) {
				if (gl.checkBuildInitialStreet(x, y + 1, 0, ID)) {
					secondRoadLocation = new int[] { x, y + 1, 0 };
					pO.requestBuildInitialRoad(x, y + 1, 0);
				} else if (gl.checkBuildInitialStreet(x - 1, y - 1, 2, ID)) {
					secondRoadLocation = new int[] { x - 1, y - 1, 2 };
					pO.requestBuildInitialRoad(x - 1, y - 1, 2);
				} else if (gl.checkBuildInitialStreet(x - 1, y - 1, dir, ID)) {
					secondRoadLocation = new int[] { x - 1, y - 1, dir };
					pO.requestBuildInitialRoad(x - 1, y - 1, dir);
				} else {
					throw new IllegalArgumentException("Error at PrimitiveAI.initialRound()");
				}

			} else {
				throw new IllegalArgumentException("Error at PrimitiveAI.initialRound()");
			}
		}
	}

	public void buildVillage(int x, int y, int dir, int playerID) {
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

	public void buildRoad(int i, int j, int k, int playerID) {
		Edge e = gl.getBoard().getEdgeAt(i, j, k);
		e.setHasStreet(true);
		e.setOwnedByPlayer(gl.getBoard().getPlayer(playerID).getID());

	}

	protected PrimitiveAIOutputHandler getOutput() {
		return this.pO;
	}

	protected PrimitiveAIInputHandler getInput() {
		return this.pI;

	}

	public String getVERSION() {
		return VERSION;
	}

	public String getPROTOCOL() {
		return PROTOCOL;
	}

	public int getID() {
		return ID;
	}

	public void setID(int player_id) {
		ID = player_id;

	}

	public int getColorCounter() {
		return colorCounter;
	}

	public void setColorCounter(int colorCounter) {
		this.colorCounter = colorCounter;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public int[] getFirstVillageLocation() {
		return firstVillageLocation;
	}

	public int[] getSecondVillageLocation() {
		return secondVillageLocation;
	}

	public int[] getFirstRoadLocation() {
		return firstRoadLocation;
	}

	public int[] getSecondRoadLocation() {
		return secondRoadLocation;
	}

	public void initBoard(Field[] fields, Corner[] corners, ArrayList<Edge> streets, Corner[] harbourCorners,
			String banditLocation) {
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

}
