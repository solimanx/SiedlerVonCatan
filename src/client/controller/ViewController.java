package client.controller;

import java.util.HashMap;

import client.view.View;
import enums.CornerStatus;
import enums.ResourceType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import model.Board;
import server.controller.GameController;

public class ViewController implements ViewControllerInterface {

	private View view;
	private Board board;
	private HashMap<Integer, Color> playerColors = new HashMap<Integer, Color>(4);
	private HashMap<enums.ResourceType, Color> fieldColors = new HashMap<enums.ResourceType, Color>(6);
	private GameController gc;

	public ViewController(Stage primaryStage, Board board,GameController gc) {
		this.gc = gc;
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
		
		view.button.setText("build initial village 2,-2,0");
		view.button.setOnAction(e -> {
			//setCorner(2, -2, 0, CornerStatus.VILLAGE, 23);
			gc.buildInitialVillage(2, -2, 0, 1);
		});
		view.button2.setText("build initial street 2,-2,1");
		view.button2.setOnAction(e -> {
			//setStreet(-1,-1,0,1);
			gc.buildInitialStreet(2, -2, 1, 1);
			//view.setFieldResourceType(-1, -1, fieldColors.get(ResourceType.SHEEP));			
		});
		view.button3.setText("build city 2,-2,0");
		view.button3.setOnAction(e -> {
			//setCorner(0, -2, 1, CornerStatus.CITY, 1);
			gc.buildCity(2, -2, 0, 1);
		});
		view.button4.setText("set bandit 2,-2");
		view.button4.setOnAction(e -> {
			gc.setBandit(2,-2);
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
		view.setStreet(u, v, dir, getPlayerColor(playerID));
		// TODO Auto-generated method stub

	}

	@Override
	public void setBandit(int u, int v) {
		view.setBandit(u, v);
		// TODO Auto-generated method stub

	}

}
