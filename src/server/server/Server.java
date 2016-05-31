package server.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import client.client.InputHandler;

public class Server {
	private InputHandler inputHandler;
	// HashMap PlayerID => Thread
	private ArrayList<ClientThread> clients = new ArrayList<ClientThread>(4);
	int maxClients = settings.DefaultSettings.maxClients;
	int clientCounter = 1;

	public void start() throws IOException {
		ServerSocket serverSocket = new ServerSocket(8080);
		System.out.println("Server Running!");
		try {
			while (clients.size() < maxClients) {
				Socket socket = serverSocket.accept();
				startHandler(socket);
			}
		} finally {
			serverSocket.close();
		}
	}

	private class ClientThread extends Thread {
		public OutputStreamWriter writer;
		public BufferedReader reader;
		public Socket socket;
		public int threadID = clientCounter;

		public ClientThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {

			try {
				writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				System.out.println("Client connected! " + socket.getRemoteSocketAddress());
				socket.setTcpNoDelay(true);
				while (true) {
					String line = reader.readLine();
					// inputHandler.sendToParser(line, id);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				closeSocket();
			}
		}
	}

	private void startHandler(Socket socket) throws IOException {
		ClientThread thread = new ClientThread(socket);
		thread.start();
		clients.add(thread);
		clientCounter++;
	}

	public void write(String s) throws IOException {
		for (ClientThread clientThread : clients) {
			clientThread.writer.write(s + "\n");
			clientThread.writer.flush();
		}
	}

	public void closeSocket() {
		for ( ClientThread clientThread : clients ){
		try {
			clientThread.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}