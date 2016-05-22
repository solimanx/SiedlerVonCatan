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
	private HashMap<Integer, Color> playerColors = new HashMap<Integer, Color>(4);
	private HashMap<enums.ResourceType, Color> fieldColors = new HashMap<enums.ResourceType, Color>(6);

	public ViewController(Stage primaryStage, Board board) {
		this.board = board;
		view = new View(board, primaryStage);
		init();
	}

	public View getView() {
		return view;
	}

	private void init() {
		fieldColors.put(ResourceType.CLAY, Color.TAN);
		fieldColors.put(ResourceType.CORN, Color.CORNSILK);
		fieldColors.put(ResourceType.NOTHING, Color.WHITE);
		fieldColors.put(ResourceType.ORE, Color.DARKGRAY);
		fieldColors.put(ResourceType.SHEEP, Color.LIGHTGREEN);
		fieldColors.put(ResourceType.WOOD, Color.FORESTGREEN);
		
		view.button.setText("set village 2,-2,0");
		view.button.setOnAction(e -> {
			setCorner(2, -2, 0, CornerStatus.VILLAGE, 23);
		});
		view.button2.setText("set Field -1,-1 to SHEEP");
		view.button2.setOnAction(e -> {
			view.setFieldResourceType(-1, -1, fieldColors.get(ResourceType.SHEEP));
		});

	}

	@Override
	public void setField(int u, int v, ResourceType resourceType, int diceIndex) {
		view.setFieldResourceType(u, v, fieldColors.get(resourceType));
		view.setFieldChip(u, v, diceIndex);
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
