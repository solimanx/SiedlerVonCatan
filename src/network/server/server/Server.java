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
import settings.DefaultSettings;

public class Server {

	// HashMap PlayerID => Thread
	private ClientThread[] clients = new ClientThread[DefaultSettings.MAXIMUM_PLAYERS_AMOUNT];

	private int clientCounter = 0;

	private ServerInputHandler serverInputHandler;
	private ServerOutputHandler serverOutputHandler;

	public Server(ServerInputHandler inputHandler) {
		this.serverInputHandler = inputHandler;
	}

	public void start() throws IOException {

		ServerSocket serverSocket = new ServerSocket(8080, 150);
		System.out.println("Server running.");
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
				System.out.println("Client connected! " + socket.getRemoteSocketAddress());

				// outputHandler.hello(serverVersion, protocolVersion,
				// threadID);
				inputHandler.hello(threadID);
				System.out.println("Hello sent to " + threadID + " Thread");
				// socket.setTcpNoDelay(true);
				while (true) {
					String line = reader.readLine();
					System.out.println("Server got message: " + line);
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
		System.out.println("The Next Client gets Number " + clientCounter);
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