package application;

import client.controller.ViewController;
import client.view.View;
import javafx.application.Application;
import javafx.stage.Stage;
import server.controller.GameController;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {
	private static Logger logger = LogManager.getLogger(Main.class.getName());
	
	private GameController gc;
	
	

	@Override
	public void start(Stage primaryStage) {
		gc = new GameController(primaryStage,1); //one player Only
		logger.info("Test");
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
