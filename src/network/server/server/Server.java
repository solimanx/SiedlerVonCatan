package network.server.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import network.ModelToProtocol;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import settings.DefaultSettings;

public class Server {
	private static Logger logger = LogManager.getLogger(Server.class.getSimpleName());
	// HashMap PlayerID => Thread
	private ClientThread[] clients = new ClientThread[DefaultSettings.MAXIMUM_PLAYERS_AMOUNT];

	private int clientCounter = 0;

	private ServerInputHandler serverInputHandler;
	private ServerOutputHandler serverOutputHandler;
	private int serverPort;

	public Server(ServerInputHandler inputHandler, int serverPort) {
		this.serverInputHandler = inputHandler;
		this.serverPort = serverPort;
	}

	public void start() throws IOException {

		ServerSocket serverSocket = new ServerSocket(serverPort, 150);
		logger.info("Server running");
		try {
			while (clientCounter < getClients().length) {
				Socket socket = serverSocket.accept();
				startHandler(socket, serverInputHandler);
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

		public ClientThread(Socket socket, ServerInputHandler inputHandler, ServerOutputHandler outputHandler,
				int threadID) {
			this.socket = socket;
			this.inputHandler = inputHandler;
			this.outputHandler = inputHandler.getGameController().getServerOutputHandler();
			this.threadID = threadID;

		}

		@Override
		public void run() {

			try {
				writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				logger.info("Client connected! " + socket.getRemoteSocketAddress());
				// outputHandler.hello(serverVersion, protocolVersion,
				// threadID);
				inputHandler.hello(threadID);
				logger.debug("Hello sent to " + threadID + " Thread");
				// socket.setTcpNoDelay(true);
				while (true) {
					String line = reader.readLine();
					logger.debug("Server got message: " + line);
					inputHandler.sendToParser(line, threadID);
				}
			} catch (IOException e) {
				e.printStackTrace();
				closeSocket();
				// TODO server beendet sich momentan noch vollständig, wenn
				// Client abbricht
			} finally {
			}
		}
	}

	private void startHandler(Socket socket, ServerInputHandler inputHandler) throws IOException {
		ClientThread thread = new ClientThread(socket, inputHandler, serverOutputHandler, clientCounter);
		thread.start();
		getClients()[clientCounter] = thread;
		clientCounter++;
		logger.debug("The Next Client gets Number " + clientCounter);
	}

	public void broadcast(String s) throws IOException {
		for (ClientThread clientThread : getClients()) {
			if (clientThread != null) {
				clientThread.writer.write(s + "\n");
				clientThread.writer.flush();
			}
		}
	}

	public void sendToClient(String s, int threadID) throws IOException {
		ClientThread thread = getClients()[threadID];
		thread.writer.write(s + "\n");
		thread.writer.flush();
	}

	public void closeSocket() {
		for (ClientThread clientThread : getClients()) {
			try {
				clientThread.socket.close();
			} catch (IOException e) {
				logger.error("Input/Output Exception ", e);
				logger.catching(Level.ERROR, e);
				e.printStackTrace();
			}
		}

	}

	public ServerInputHandler getServerInputHandler() {
		return serverInputHandler;
	}

	public ClientThread[] getClients() {
		return clients;
	}

	public void setClients(ClientThread[] clients) {
		this.clients = clients;
	}

	public int getClientCounter() {
		return clientCounter;
	}
}