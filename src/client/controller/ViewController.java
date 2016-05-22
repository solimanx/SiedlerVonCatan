package client.controller;

import java.util.HashMap;

import client.view.View;
import enums.CornerStatus;
import enums.ResourceType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import model.Board;

public class ViewController implements ViewControllerInterface {

	private View view;
	private Board board;
	private HashMap<Integer, Color> playerColor;

	public ViewController(Stage primaryStage, Board board) {
		this.board = board;
		view = new View(board, primaryStage);
		init();
	}

	public View getView() {
		return view;
	}

	private void init() {
		view.button.setText("toggle village 0,-1");
		view.button.setOnAction(e -> {
			Polygon hexagon = view.corners[3][2][0];
			hexagon.setVisible(!hexagon.isVisible());
			setCorner(2, -2, 0, CornerStatus.VILLAGE, 23);
		});

	}

	@Override
	public void setField(int u, int v, ResourceType resourceType, int diceIndex) {
		//view.setFieldResourceType(u, v, resourceType);
		//view.setFieldChip(u, v, diceIndex);

	}

	@Override
	public void setCorner(int u, int v, int dir, CornerStatus cornerStatus, int playerID) {
		Color playerColor = getPlayerColor(playerID);

		switch (cornerStatus) {
		case VILLAGE:
			view.setVillage(u, v, dir, playerColor);
			break;
		case CITY:
			view.setCity(u, v, dir, playerColor);
		default:
			break;
		}

	}

	private Color getPlayerColor(int playerID) {
		// TODO Auto-generated method stub
		return Color.BLUEVIOLET;
	}

	@Override
	public void setStreet(int u, int v, int dir, int playerID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBandid(int u, int v) {
		// TODO Auto-generated method stub

	}

}
