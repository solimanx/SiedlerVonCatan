package client.controller;

import client.view.View;
import javafx.stage.Stage;

public class ViewController {

	private View view;
	
	public ViewController(Stage primaryStage) {
		view = new View(primaryStage);
	}

}
