package network.client.controller;

import java.io.IOException;
import java.util.HashMap;

import application.lobby.LobbyController;
import application.lobby.PlayerProfileController;
import enums.ResourceType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Board;
import network.client.view.View;
import network.server.controller.ServerController;

public class ViewController {

	private FXMLLoader loader;
	protected View view;
	public HashMap<enums.Color, Color> playerColors = new HashMap<enums.Color, Color>(4);
	public HashMap<enums.ResourceType, Color> fieldColors = new HashMap<enums.ResourceType, Color>(6);
	protected ServerController serverController; // DEBUG
	private ClientController clientController;
	private MainViewController mainViewController;
	private LobbyController lobbyController;
	private PlayerProfileController nameSelectDialogController;
	private Stage primaryStage;
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	private Stage choosingStage;
	private Stage mainViewStage;

	public ViewController(Stage primaryStage, ClientController fc) {
		this.primaryStage = primaryStage;
		this.clientController = fc;
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

	/**
	 * Starts the lobby view, which provides connecting to server and chat
	 *
	 * @param primaryStage
	 * @throws IOException
	 */
	private void startLobbyView(Stage primaryStage) throws IOException {

		Parent root = loader.load(getClass().getResource("/application/lobby/LobbyFXML.fxml").openStream());
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
	 * starts the View for choosing Player name and Player Color view also
	 * offers Button for setting Player ready for game to start
	 *
	 * @throws IOException
	 */
	public void startChooseView() throws IOException {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/lobby/PlayerProfileFXML.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			nameSelectDialogController = fxmlLoader.getController();
			nameSelectDialogController.setViewController(this);
			choosingStage = new Stage();
			choosingStage.setTitle("Choose Name and Color");
			choosingStage.setScene(new Scene(root1));
			choosingStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * starts MainView and MainViewController
	 */
	void startMainView() {
		primaryStage.close();
		choosingStage.close();

		mainViewStage = new Stage();
		this.mainViewController = new MainViewController(this, clientController.getGameLogic().getBoard(), mainViewStage);
		init();
	}

	public View getView() {
		return mainViewController.getView();
	}

	private void init() {
		view = getView();
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

		/*view.button.setText("build initial village 2,-2,0");
		view.button.setOnAction(e -> {
			// setCorner(2, -2, 0, CornerStatus.VILLAGE, 23);
			serverController.buildInitialVillage(2, -2, 0, 1);
		});
		view.button2.setText("build initial street 2,-2,1");
		view.button2.setOnAction(e -> {
			// setStreet(-1,-1,0,1);
			serverController.buildInitialStreet(2, -2, 1, 1);
			serverController.buildStreet(2, -2, 2, 1);
			// view.setFieldResourceType(-1, -1,
			// fieldColors.get(ResourceType.SHEEP));
		});
		view.button3.setText("build city 2,-2,0");
		view.button3.setOnAction(e -> {
			// setCorner(0, -2, 1, CornerStatus.CITY, 1);
			serverController.buildCity(2, -2, 0, 1);
			serverController.buildVillage(2, -1, 0, 1);
			serverController.buildCity(2, -1, 0, 1);
		});
		view.button4.setText("set bandit 2,-2");
		view.button4.setOnAction(e -> {
			// gameController.setBandit(2, -2);
		});*/

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

	public ClientController getClientController() {
		return clientController;
	}

	public MainViewController getMainViewController() {
		return mainViewController;
	}

	public LobbyController getLobbyController() {
		return lobbyController;
	}

	public void setFlowController(ClientController clientController) {
		this.clientController = clientController;
	}

	public void setMainViewController(MainViewController mainViewController) {
		this.mainViewController = mainViewController;
	}

	public void setLobbyController(LobbyController lobbyController) {
		this.lobbyController = lobbyController;
	}

}
