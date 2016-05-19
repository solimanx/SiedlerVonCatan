package application;

import client.controller.ViewController;
import client.view.View;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	private ViewController vc;

	@Override
	public void start(Stage primaryStage) {
		vc = new ViewController(primaryStage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
