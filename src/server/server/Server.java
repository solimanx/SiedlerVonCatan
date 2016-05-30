package server.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import client.client.InputHandler;


public class Server {
    
    private InputHandler inputHandler;
    ArrayList<Thread> clients = new ArrayList<Thread>(4);
    int maxClients = settings.DefaultSettings.maxClients;
    
	public static void main(String[] args) throws IOException {

		ServerSocket serverSocket = new ServerSocket(8080);
		System.out.println("Server Running!");
		try {
			while (clients. < maxClients) {
				Socket socket = serverSocket.accept();
				startHandler(socket);
			}
		} finally {
			serverSocket.close();
		}
	}

	private static void startHandler(Socket socket) throws IOException {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
					System.out.println("Client connected! " + socket.getRemoteSocketAddress());

					String line = reader.readLine();
					inputHandler.sendToParser(line);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					closeSocket();
				}
			}
            
            public void write(String s){
                writer.write(s + "\n");
				writer.flush();
            }
            
			public void closeSocket() {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		thread.start();
		clients.add(thread);
	}

}