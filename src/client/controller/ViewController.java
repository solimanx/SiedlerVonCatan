package client.controller;

import client.view.View;
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
			view.hexagons.get(0).setTranslateX(2 * View.radius * View.sin60);
			view.hexagons.get(0).translateXProperty();
		});
		
	}

}
