package server.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import client.client.InputHandler;

public class Server {

	private InputHandler inputHandler;
	// HashMap PlayerID => Thread
	private static HashMap<Integer, Thread> clients = new HashMap<Integer, Thread>(4);
	static int maxClients = settings.DefaultSettings.maxClients;
	static int clientCounter = 1;

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

	private static void startHandler(Socket socket) throws IOException {
		Thread thread = new Thread() {
			OutputStreamWriter writer;
			BufferedReader reader;
			public int threadID = clientCounter;

			@Override
			public void run() {
				
				try {
					writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
					reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
					System.out.println("Client connected! " + socket.getRemoteSocketAddress());
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
		};
		thread.start();
		clients.put(clientCounter, thread);
		clientCounter++;
	}
	
	public void write(String s) throws IOException {
//		writer.write(s + "\n");
//		writer.flush();
	}

	public static void closeSocket() {
//		try {
////			socket.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}