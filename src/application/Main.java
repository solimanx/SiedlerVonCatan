package application;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import client.client.Client;
import javafx.application.Application;
import javafx.stage.Stage;
import server.controller.GameController;

public class Main extends Application {
	private static Logger logger = LogManager.getLogger(Main.class.getName());
	
	private GameController gc;
	private Client client;

	@Override
	public void start(Stage primaryStage) {
		gc = new GameController(primaryStage,1);
		client = new Client();
		client.run();
		
	}
	
	@Override
	public void init() throws Exception{
		Parameters p = getParameters();
		List<String> raw = p.getRaw();
		for (String string : raw) {
			System.out.println("Parameter: " + string);
		}
	}

	public static void main(String[] args) {
		
		launch(args);
		
	}
}
