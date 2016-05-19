package client.controller;

import client.view.View;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class ViewController {

	private View view;
	
	public ViewController(Stage primaryStage) {
		view = new View(primaryStage);
		init();
	}
	
	public View getView(){
		return view;
	}

	private void init() {
		view.button.setOnAction(e -> {
			Polygon hexagon = view.hexagons.get(0);
			hexagon.setVisible(!hexagon.isVisible());
		});
		
	}

}
