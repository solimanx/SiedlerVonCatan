package network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import network.server.io.ServerInputHandler;
import network.server.io.ServerOutputHandler;

// TODO: Auto-generated Javadoc
public class Server {
	private static Logger logger = LogManager.getLogger(Server.class.getSimpleName());
	// HashMap PlayerID => Thread
	// private ClientThread[] clients = new
	// ClientThread[DefaultSettings.MAXIMUM_PLAYERS_AMOUNT];
	private HashMap<Integer, ClientThread> idToClientThread = new HashMap<Integer, ClientThread>();

	private int clientCounter = 0;

	private ServerInputHandler serverInputHandler;
	private ServerOutputHandler serverOutputHandler;
	private int serverPort;
	ServerSocket serverSocket;
	private int connectedPlayers;
	private ArrayList<ClientThread> threadList = new ArrayList<ClientThread>();
	private boolean shutdown;

	/**
	 * Instantiates a new server.
	 *
	 * @param inputHandler
	 *            the input handler
	 * @param serverPort
	 *            the server port
	 */
	public Server(ServerInputHandler inputHandler, int serverPort) {
		this.serverInputHandler = inputHandler;
		this.serverPort = serverPort;
	}

	/**
	 * Start.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void start() throws IOException {

		serverSocket = new ServerSocket(serverPort, 150);
		logger.info("Server running");
		try {
			// geändert weil sonst können sich nur 4 spieler verbinden; keine
			// disconnects möglich
			while (clientCounter < 10 && !shutdown) {
				try{
				Socket socket = serverSocket.accept();
				startHandler(socket, serverInputHandler);
				clientCounter++;
				}
				catch(java.net.SocketException se){

				}

			}
			if (shutdown) {
				for (ClientThread ct : threadList) {
					ct.disconnect();
				}
				System.out.println("ClientThreads disconnected");
			}

		} finally {
			serverSocket.close();
		}
	}

	public class ClientThread extends Thread {
		public OutputStreamWriter writer;
		public BufferedReader reader;
		public Socket socket;
		public int threadID;
		public ServerInputHandler inputHandler;
		public ServerOutputHandler outputHandler;
		public boolean connected = true;

		/**
		 * Instantiates a new client thread.
		 *
		 * @param socket
		 *            the socket
		 * @param inputHandler
		 *            the input handler
		 * @param outputHandler
		 *            the output handler
		 * @param threadID
		 *            the thread ID
		 */
		public ClientThread(Socket socket, ServerInputHandler inputHandler, ServerOutputHandler outputHandler,
				int threadID) {
			this.socket = socket;
			this.inputHandler = inputHandler;
			this.outputHandler = inputHandler.getGameController().getServerOutputHandler();
			this.threadID = threadID;

		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {

			try {
				writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				logger.info("Client connected! " + socket.getRemoteSocketAddress());
				connectedPlayers++;
				// outputHandler.hello(serverVersion, protocolVersion,
				// threadID);
				serverInputHandler.getServerController().hello(threadID);
				logger.debug("Hello sent to " + threadID + " Thread");
				// socket.setTcpNoDelay(true);
				while (connected) {
					String line = reader.readLine();
					logger.debug("Server got message: " + line);
					if (line != null) {
						inputHandler.sendToParser(line, threadID);

					} else {
						connected = false;
						disconnectPlayer(connectedPlayers);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				inputHandler.lostConnection(threadID);
				idToClientThread.remove(threadID);
				connectedPlayers--;
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// closeSocket();
				// TODO server beendet sich momentan noch vollständig, wenn
				// Client abbricht
			} finally {
				disconnect();
			}
		}

		/**
		 * Disconnect.
		 */
		public void disconnect() {
			try {
				connected = false;
				reader.close();
				writer.close();
				socket.close();
				logger.info("Player disconnected");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Start handler.
	 *
	 * @param socket
	 *            the socket
	 * @param inputHandler
	 *            the input handler
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void startHandler(Socket socket, ServerInputHandler inputHandler) throws IOException {
		ClientThread thread = new ClientThread(socket, inputHandler, serverOutputHandler, clientCounter);
		threadList.add(thread);
		thread.start();
		idToClientThread.put(clientCounter, thread);
		logger.debug("The Next Client gets Number " + clientCounter);
	}

	/**
	 * Broadcast.
	 *
	 * @param s
	 *            the s
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void broadcast(String s) throws IOException {
		logger.info("Broadcast: " + s);
		ClientThread currClientThread;
		for (Integer keyIDs : idToClientThread.keySet()) {
			currClientThread = idToClientThread.get(keyIDs);
			if (currClientThread != null && currClientThread.isAlive()) {
				currClientThread.writer.write(s + "\n");
				currClientThread.writer.flush();
			}
		}
	}

	/**
	 * Send to client.
	 *
	 * @param s
	 *            the s
	 * @param threadID
	 *            the thread ID
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void sendToClient(String s, int threadID) throws IOException {
		ClientThread thread = idToClientThread.get(threadID);
		logger.info("Send to Client: " + threadID + " " + s);
		thread.writer.write(s + "\n");
		thread.writer.flush();
	}

	/**
	 * Close socket.
	 */
	public void closeSocket() {
		for (Integer keyIDs : idToClientThread.keySet()) {
			ClientThread clientThread = idToClientThread.get(keyIDs);
			if (clientThread != null) {
				clientThread.disconnect();
			}
		}

	}

	/**
	 * Gets the server input handler.
	 *
	 * @return the server input handler
	 */
	public ServerInputHandler getServerInputHandler() {
		return serverInputHandler;
	}

	/**
	 * Gets the clients.
	 *
	 * @return the clients
	 */
	/*
	 * public ClientThread[] getClients() { return clients; }
	 */

	/**
	 * Sets the clients.
	 *
	 * @param clients
	 *            the new clients
	 */
	/*
	 * public void setClients(ClientThread[] clients) { this.clients = clients;
	 * }
	 */

	/**
	 * Gets the client counter.
	 *
	 * @return the client counter
	 */
	public int getClientCounter() {
		return clientCounter;
	}

	/**
	 * Gets the connected players.
	 *
	 * @return the connected players
	 */
	public int getConnectedPlayers() {
		return connectedPlayers;
	}

	/**
	 * Disconnect player.
	 *
	 * @param id
	 *            the id
	 */
	public void disconnectPlayer(Integer id) {
		idToClientThread.get(id - 1).disconnect();
		serverInputHandler.getServerController().connectionLost(id - 1);

	}

	public void disconnectServer() {
		System.out.println("Server");
		shutdown = true;
		closeSocket();
		System.out.println("ClientThreads disconnected");
		try {
			if (serverSocket != null)
				serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}