package ai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.GameLogic;
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
	private int[] firstRoadLocation;
	private int[] secondRoadLocation;

	private boolean scanning = true;
	private boolean connectionActive = false;
	private static int connectionTry = 0;

	private Socket socket;
	private final String PROTOCOL = "0.3";
	private final String VERSION = "NiedlichePixel 0.3 (KI)";


	private final String SERVERHOST = "aruba.dbs.ifi.lmu.de";
	private final int PORT = 10000;

	private OutputStreamWriter writer;
	private BufferedReader reader;

	// own ID in server
	private int ID;
	

	private int colorCounter = 0;

	// Handlers

	private PrimitiveAIInputHandler pI = new PrimitiveAIInputHandler(this);
	private PrimitiveAIOutputHandler pO = new PrimitiveAIOutputHandler(this);

	// USELESS GAMELOGIC/BOARD ONLY TO SEE CODE
	private GameLogic gl;
	private ClientController cc;

	public PrimitiveAI() {
		System.out.println("AI started.");
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
				connectionActive = true;
				System.out.println("Client connected to server.");
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
			System.out.println(DefaultSettings.getCurrentTime()+" Server: " + line);
			pI.sendToParser(line);
		}
	}

	public void write(String json) throws IOException {
		System.out.println(DefaultSettings.getCurrentTime()+ "Client:" + json);
		writer.write(json + "\n");
		writer.flush();
	}

	/**
	 * Builds a village at the first legal empty location, then attaches a roads
	 * to it, then repeats it.
	 */
	public void initialRound() {
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
						// TODO cc.requestBuildInitialVillage(j, i, k);
						break outerloop;

					}
				}
			}
		}
		// Build road at first village location
		int x, y, dir;
		if (secondVillageLocation == null) {
			x = firstVillageLocation[0];
			y = firstVillageLocation[1];
			dir = firstVillageLocation[2];

			// find first non null road
			if (dir == 0) {
				if (gl.getBoard().getEdgeAt(x, y - 1, 2) != null) {
					// TODO cc.requestBuildInitialRoad(x,y,dir);
					firstRoadLocation = new int[] { x, y - 1, 2 };
				} else if (gl.getBoard().getEdgeAt(x, y, 1) != null) {
					// TODO cc.requestBuildInitialRoad(x,y,dir);
					firstRoadLocation = new int[] { x, y, 1 };
				} else if (gl.getBoard().getEdgeAt(x, y, dir) != null) {
					// TODO cc.requestBuildInitialRoad(x,y,dir);
					firstRoadLocation = new int[] { x, y, dir };
				} else {
					throw new IllegalArgumentException("Error at PrimitiveAI.initialRound()");
				}
			} else if (dir == 1) {
				if (gl.getBoard().getEdgeAt(x, y + 1, 0) != null) {
					// TODO cc.requestBuildInitialRoad(x,y+1,0);
					secondRoadLocation = new int[] { x, y + 1, 0 };
				} else if (gl.getBoard().getEdgeAt(x - 1, y - 1, 2) != null) {
					// TODO cc.requestBuildInitialRoad(x-1,y-1,2);
					secondRoadLocation = new int[] { x - 1, y - 1, 2 };
				} else if (gl.getBoard().getEdgeAt(x - 1, y - 1, dir) != null) {
					// TODO cc.requestBuildInitialRoad(x-1,y-1,dir);
					secondRoadLocation = new int[] { x - 1, y - 1, dir };
				} else {
					throw new IllegalArgumentException("Error at PrimitiveAI.initialRound()");
				}

			} else {
				throw new IllegalArgumentException("Error at PrimitiveAI.initialRound()");
			}

		}

		// // ROAD
		// Boolean roadBuilt = false;
		// Edge[] projectingEdges;
		// if (secondVillageLocation == null) {
		// projectingEdges =
		// gl.getBoard().getProjectingEdges(firstVillageLocation[0],
		// firstVillageLocation[1],
		// firstVillageLocation[2]);
		// } else {
		// projectingEdges =
		// gl.getBoard().getProjectingEdges(secondVillageLocation[0],
		// secondVillageLocation[1],
		// secondVillageLocation[2]);
		// }
		//
		// if (projectingEdges != null) {
		// for (int i = 0; i < projectingEdges.length; i++) {
		// if (projectingEdges[i] != null && !roadBuilt) {
		//
		// }
		//
		// }
		// }

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

	public void setID(int player_id) {
		ID = player_id;

	}
	
	public int getColorCounter() {
		return colorCounter;
	}

	public void setColorCounter(int colorCounter) {
		this.colorCounter = colorCounter;
	}

}
