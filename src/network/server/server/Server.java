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
import network.server.controller.ServerNetworkController;

public class Server {

	// HashMap PlayerID => Thread
	public ClientThread[] clients = new ClientThread[4];

	int clientCounter = 0;

	private ServerInputHandler inputHandler;
	private ServerOutputHandler outputHandler;
	private ServerNetworkController networkController;

	String serverVersion = settings.DefaultSettings.SERVER_VERSION;
	String protocolVersion = settings.DefaultSettings.PROTOCOL_VERSION;

	public Server(ServerInputHandler inputHandler, ServerNetworkController serverNetworkController) {
		this.inputHandler = inputHandler;
		this.networkController = serverNetworkController;
	}

	public void start() throws IOException {
		this.outputHandler = networkController.getOutputHandler();

		ServerSocket serverSocket = new ServerSocket(8080, 150);
		System.out.println("Server Running!");
		try {
			while (clientCounter <= clients.length) {
				Socket socket = serverSocket.accept();
				startHandler(socket, inputHandler);
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

		public ClientThread(Socket socket, ServerInputHandler inputHandler, ServerOutputHandler outputHandler, int threadID) {
			this.socket = socket;
			this.inputHandler = inputHandler;
			this.outputHandler = outputHandler;
			this.threadID = threadID;

		}

		@Override
		public void run() {

			try {
				writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				System.out.println("Client connected! " + socket.getRemoteSocketAddress());
				
				outputHandler.hello(serverVersion, protocolVersion, threadID);
				System.out.println("Hello sent to " + threadID + " Thread");
				//socket.setTcpNoDelay(true);
				while (true) {
					String line = reader.readLine();
					System.out.println("Server got message: " + line);
					inputHandler.sendToParser(line, threadID);
				}
			} catch (IOException e) {
				e.printStackTrace();
				closeSocket();
				// TODO server beendet sich momentan noch vollständig, wenn Client abbricht
			} finally {
			}
		}
	}

	private void startHandler(Socket socket, ServerInputHandler inputHandler) throws IOException {
		ClientThread thread = new ClientThread(socket, inputHandler, outputHandler, clientCounter);
		thread.start();
		clients[clientCounter] = thread;
		clientCounter++;
		System.out.println("The Next Client gets Number " + clientCounter);
	}

	public void broadcast(String s) throws IOException {
		for (ClientThread clientThread : clients) {
			if (clientThread != null) {
				clientThread.writer.write(s + "\n");
				clientThread.writer.flush();
			}
		}
	}

	public void sendToClient(String s, int threadID) throws IOException {
		ClientThread thread = clients[threadID];
		thread.writer.write(s + "\n");
		thread.writer.flush();
	}

	public void closeSocket() {
		for (ClientThread clientThread : clients) {
			try {
				clientThread.socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}