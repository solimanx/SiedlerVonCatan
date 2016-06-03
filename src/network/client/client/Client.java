package network.client.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javafx.application.Platform;
import network.client.controller.ClientNetworkController;
import network.client.controller.MainViewController;

public class Client extends Thread {
	static int connectionTry = 0;
	Socket socket = null;
	OutputStreamWriter writer = null;
	BufferedReader reader = null;
	int port = 8080;
	String serverHost = "localhost";
	boolean scanning = true;
	boolean connectionActive = false;
	protected ClientInputHandler inputHandler;

	public Client(ClientInputHandler inputHandler, String serverHost, int port){
		this.port = port;
		this.serverHost = serverHost;
		this.inputHandler = inputHandler;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		try {
			socket = new Socket(serverHost, port);
			writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			scanning = false;
			connectionActive = true;
			System.out.println("Client: Client connected to Server");
			runClient();
		} catch (IOException e) {
			System.out.println("Connection failed");
			scanning = true;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void runClient() throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			System.out.println("Received from Server: " + line);
			inputHandler.sendToParser(line);
			// redirect line to Networkcontroller
		}
	}

	public void write(String s) throws IOException {
		System.out.println("Client sends to Server: " + s);
		writer.write(s + "\n");
		writer.flush();
	}

	public void stopClient() {
		try {
			socket.close();
			connectionActive = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
