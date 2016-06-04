package network.client.controller;

import java.io.IOException;
import java.util.HashMap;

import application.LobbyController;
import enums.ResourceType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Board;
import network.client.view.View;
import network.server.controller.GameController;

public class ViewController {

	private FXMLLoader loader;
	protected View view;
	protected Board board;
	protected HashMap<enums.Color, Color> playerColors = new HashMap<enums.Color, Color>(4);
	protected HashMap<enums.ResourceType, Color> fieldColors = new HashMap<enums.ResourceType, Color>(6);
	protected GameController gameController; // DEBUG
	private FlowController flowController;
	private MainViewController mainViewController;
	private LobbyController lobbyController;

	public ViewController(Stage primaryStage, FlowController fc) {
		this.flowController = fc;
		loader = new FXMLLoader();

		try {
			startLobbyView(primaryStage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// this.board = board;
		// startMainView(primaryStage, board);
		// init();
	}
	// public void openChooseNameMenu(){
	// Parent root;
	// try{
	// root =
	// loader.load(getClass().getClassLoader().getResourceAsStream("/application/lobby.fxml"));
	// Stage stage = new Stage();
	// stage.setTitle("My New Stage Title");
	// stage.setScene(new Scene(root, 450, 450));
	// stage.show();
	// }
	// catch(IOException e){
	// e.printStackTrace();
	// }
	// }

	private void startLobbyView(Stage primaryStage) throws IOException {

		Parent root = loader.load(getClass().getResource("/application/lobby.fxml").openStream());
		Scene scene = new Scene(root);
		lobbyController = (LobbyController) loader.getController();
		lobbyController.setViewController(this);
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		//
		primaryStage.setTitle("Settlers of Catan : Lobby");
		primaryStage.setResizable(false);
		// primaryStage.initStyle(StageStyle.UTILITY);
		//
		primaryStage.show();
	}

	/**
	 * starts MainView and MainViewController
	 * 
	 * @param primaryStage
	 * @param board
	 */
	private void startMainView(Stage primaryStage, Board board) {
		view = new View(board, primaryStage, mainViewController);
		this.mainViewController = new MainViewController(view, this);
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

		playerColors.put(enums.Color.BLUE, Color.BLUE);
		playerColors.put(enums.Color.ORANGE, Color.ORANGE);
		playerColors.put(enums.Color.RED, Color.RED);
		playerColors.put(enums.Color.WHITE, Color.WHITE);

		view.button.setText("build initial village 2,-2,0");
		view.button.setOnAction(e -> {
			// setCorner(2, -2, 0, CornerStatus.VILLAGE, 23);
			gameController.buildInitialVillage(2, -2, 0, 1);
		});
		view.button2.setText("build initial street 2,-2,1");
		view.button2.setOnAction(e -> {
			// setStreet(-1,-1,0,1);
			gameController.buildInitialStreet(2, -2, 1, 1);
			gameController.buildStreet(2, -2, 2, 1);
			// view.setFieldResourceType(-1, -1,
			// fieldColors.get(ResourceType.SHEEP));
		});
		view.button3.setText("build city 2,-2,0");
		view.button3.setOnAction(e -> {
			// setCorner(0, -2, 1, CornerStatus.CITY, 1);
			gameController.buildCity(2, -2, 0, 1);
			gameController.buildVillage(2, -1, 0, 1);
			gameController.buildCity(2, -1, 0, 1);
		});
		view.button4.setText("set bandit 2,-2");
		view.button4.setOnAction(e -> {
			// gameController.setBandit(2, -2);
		});

	}

	public Color getPlayerColor(int playerID) {
		// TODO Auto-generated method stub
		return Color.BLUEVIOLET;
	}

	public void setPlayerColor(int playerId, enums.Color color) {
		// TODO Auto-generated method stub

	}

	public void setPlayerName(int playerId, String name) {
		// TODO Auto-generated method stub

	}

	public void setDiceRollResult(int playerId, int result) {
		// TODO Auto-generated method stub

	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public FlowController getFlowController() {
		return flowController;
	}

	public MainViewController getMainViewController() {
		return mainViewController;
	}

	public LobbyController getLobbyController() {
		return lobbyController;
	}

	public void setFlowController(FlowController flowController) {
		this.flowController = flowController;
	}

	public void setMainViewController(MainViewController mainViewController) {
		this.mainViewController = mainViewController;
	}

	public void setLobbyController(LobbyController lobbyController) {
		this.lobbyController = lobbyController;
	}

}
