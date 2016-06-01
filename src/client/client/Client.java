package client.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import client.controller.ClientNetworkController;
import client.controller.MainViewController;
import javafx.application.Platform;

public class Client extends Thread {
	static int connectionTry = 0;
	Socket socket = null;
	OutputStreamWriter writer = null;
	BufferedReader reader = null;
	int port = 8080;
	boolean scanning = true;
	boolean connectionActive = false;
	protected ClientInputHandler inputHandler;
	
	public Client(ClientInputHandler inputHandler) {
		super();
		this.inputHandler = inputHandler;
	}
	
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		try {
			socket = new Socket("localhost", port);
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
		while (connectionActive) {
			String line = reader.readLine();
			System.out.println("Received from Server: " + line);
			// redirect line to Networkcontroller
		}
	}

	public void write(String s) throws IOException {
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
