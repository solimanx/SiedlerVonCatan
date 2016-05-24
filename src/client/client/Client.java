package client.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.google.gson.Gson;

public class Client {
	public static void main(String[] args) throws IOException {
		Socket socket = new Socket("localhost", 8080); //TODO: alle paar Sekunden neu versuchen.
		OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		
		String jsonTestString = "Hallo Welt";
		
		Gson gsonObject = new Gson();

		gsonObject.toJson(jsonTestString);
		writer.write(gsonObject.toString() + "\n");
		writer.flush();
		
		
		String line = reader.readLine();
		gsonObject = new Gson();
		gsonObject.toJson(line);
		
		System.out.println("Received from Server:\n" + gsonObject.toString());
		
		socket.close();
	}
}
