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

import ai.agents.CornerAgent;
import ai.agents.OpponentAgent;
import ai.agents.ResourceAgent;
import enums.CornerStatus;
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
	// CONNECTION FIELDS
	// --------------------------------------------------------------------------------

	private Socket socket;
	private final String SERVERHOST;// = "localhost";// "aruba.dbs.ifi.lmu.de";
	private final int PORT;// = 8080;// 10001;


	// --------------------------------------------------------------------------------
	// I/O FIELDS
	// --------------------------------------------------------------------------------
	private OutputStreamWriter writer;
	private BufferedReader reader;
	protected AIInputHandler pI;
	private static Logger logger = LogManager.getLogger(PrimitiveAI.class.getSimpleName());

	// --------------------------------------------------------------------------------
	// PROFILE FIELDS
	// --------------------------------------------------------------------------------

	// ================================================================================
	// CONSTRUCTORS
	// ================================================================================

	/**
	 * Creates a PrimitiveAI object, and forces it to connect to the 0.3 server.
	 *
	 * @param serverHost            the server host
	 * @param port            the port
	 * @param inputHandler the input handler
	 */
	public PrimitiveAI(String serverHost, int port,AIInputHandler inputHandler) {
		// logger.info("AI started");
		this.SERVERHOST = serverHost;
		this.PORT = port;
		this.pI = inputHandler;

	}

	// ================================================================================
	// CONNECTION METHODS
	// ================================================================================

	/**
	 * Commence.
	 */
	public void commence() {
		logger.info(" AI started.");
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
			logger.info(" AI connected to server.");
			read();
		} catch (IOException e) {
			logger.catching(Level.ERROR, e);

			try {
				Thread.sleep(2000);
			} catch (InterruptedException ie) {
				logger.catching(Level.ERROR, ie);
			}
		}
	}

	/**
	 * Reading input from the servers output.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void read() throws IOException {
		// logger.warn("Reading input from the servers output(read()) throws
		// IOException");
		String line;
		while ((line = reader.readLine()) != null) {
			logger.debug("Server: " + line);
			// logger.debug("Server"+ line);
			
			 try { Thread.sleep(100); } catch (InterruptedException e) {
				 
			 }
			 
			pI.sendToParser(line);
		}
	}

	/**
	 * Output to server.
	 *
	 * @param json
	 *            the json
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void write(String json) throws IOException {
		// logger.warn("Writing output to server, throws IOException");
		logger.info("    A.I: " + json);
		// logger.info(DefaultSettings.getCurrentTime()+ " A.I: "+ json);
		writer.write(json + "\n");
		writer.flush();
	}

	/**
	 * Gets the input.
	 *
	 * @return the input
	 */
	protected AIInputHandler getInput() {
		return this.pI;

	}
	

}
