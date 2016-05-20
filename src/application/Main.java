package application;

import client.controller.ViewController;
import client.view.View;
import javafx.application.Application;
import javafx.stage.Stage;
import server.controller.GameController;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	private GameController gc;

	@Override
	public void start(Stage primaryStage) {
		gc = new GameController(primaryStage);
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
