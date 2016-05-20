package client.controller;

import client.view.View;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import model.Board;

public class ViewController {

	private View view;
	private Board board;
	
	public ViewController(Stage primaryStage, Board board) {
		this.board = board;
		view = new View(board, primaryStage);
		init();
	}
	
	public View getView(){
		return view;
	}

	private void init() {
		view.button.setOnAction(e -> {
			Polygon hexagon = view.figures.get(0);
			hexagon.setVisible(!hexagon.isVisible());
		});
		
	}

}
