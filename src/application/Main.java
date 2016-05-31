package application;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import client.client.Client;
import javafx.application.Application;
import javafx.stage.Stage;
import server.controller.GameController;
import server.server.Server;

public class Main extends Application {
	private static Logger logger = LogManager.getLogger(Main.class.getName());

	private GameController gc;
	private Client client;
	private Server server;

	// true for server, false for client
	private boolean mode = false;

	@Override
	public void start(Stage primaryStage) {
		if (mode) {
			server = new Server();
			try {
				server.start();
			} catch (IOException e) {
				System.out.println("Couldn't instantiate Server unfortunately!");
				e.printStackTrace();
			}
		} else {
			client = new Client();
			gc = new GameController(primaryStage, 1, client);
			client.start();
		}

	}

	@Override
	public void init() throws Exception {
		Parameters p = getParameters();
		List<String> raw = p.getRaw();
		for (String string : raw) {
			if (string.equals("server")) {
				mode = true;
			}
		}
	}

	public static void main(String[] args) {

		launch(args);

	}
}
