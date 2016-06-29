package network.client.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * Client class, which is responsible for connecting to a server, as well as
 * writing and receiving message from the server.
 */
public class Client extends Thread {
	static int connectionTry = 0;
	private Socket socket = null;
	private OutputStreamWriter writer = null;
	private BufferedReader reader = null;
	private int port = 8080;
	private String serverHost = "localhost";
	boolean scanning = true;
	boolean connectionActive = false;
	protected ClientInputHandler inputHandler;
	private static Logger logger = LogManager.getLogger(Client.class.getSimpleName());

	/**
	 * Instantiates a new client.
	 *
	 * @param inputHandler the input handler
	 * @param serverHost the server host
	 * @param port the port
	 */
	public Client(ClientInputHandler inputHandler, String serverHost, int port) {
		this.port = port;
		this.serverHost = serverHost;
		this.inputHandler = inputHandler;
	}

	/**
	 * Connecting to server.
	 */
	@Override
	public void run() {
		while (scanning && connectionTry < 10) {
			try {
				socket = new Socket(serverHost, port);
				writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				scanning = false;
				connectionActive = true;
				logger.info("Connected to server");
				runClient();
			} catch (IOException e) {
				logger.info("Connection to server failed." + " Attempt:" + connectionTry);
				connectionTry++;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException ie) {
					logger.error("Interrupted Exception", ie);
					logger.catching(Level.ERROR, ie);
					ie.printStackTrace();
				}
			}
		}
	}

	/**
	 * Commence reading messages from the server.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void runClient() throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			logger.info("Received from Server: " + line);
			inputHandler.sendToParser(line);

		}
	}

	/**
	 * Commence sending a message to the server.
	 *
	 * @param s the s
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void write(String s) throws IOException {
		logger.debug("Client sends to Server: " + s);
		writer.write(s + "\n");
		writer.flush();
	}

	/**
	 * Shutting down the client and the connection to the server.
	 */
	public void stopClient() {
		try {
			socket.close();
			connectionActive = false;
		} catch (IOException e) {
			logger.error("Input/Output Exception", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}
	}

}
