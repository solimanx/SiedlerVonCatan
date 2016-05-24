package server.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;


public class Server {

	public static void main(String[] args) throws IOException {

		ServerSocket serverSocket = new ServerSocket(8080);

		try {
			while (true) {
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
					Gson gsonObject = new Gson();
					gsonObject.toJson(line);
					
					writer.write(gsonObject.toString() + "\n");
					writer.flush();

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					closeSocket();
				}
			}

			private void closeSocket() {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		thread.start();
	}

}
