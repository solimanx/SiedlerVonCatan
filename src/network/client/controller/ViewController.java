package network.client.controller;

import java.io.IOException;

import application.lobby.LobbyController;
import application.lobby.PlayerProfileController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import network.client.view.GameViewController;
import network.client.view.tradeview.TradeViewController;
import network.server.controller.ServerController;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// TODO: Auto-generated Javadoc
public class ViewController {
	private static Logger logger = LogManager.getLogger(ViewController.class.getSimpleName());
	private FXMLLoader loader;
	protected ServerController serverController; // DEBUG
	private ClientController clientController;
	private GameViewController gameViewController;
	private LobbyController lobbyController;
	// private PlayerProfileController playerProfileController;
	private Stage primaryStage;

	/**
	 * Gets the primary stage.
	 *
	 * @return the primary stage
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	private Stage choosingStage;
	private boolean isGameView = false;
	public boolean isChoosingStage = false;
	private TradeViewController tradeViewController;
	private String theme;

	/**
	 * Instantiates a new view controller.
	 *
	 * @param primaryStage
	 *            the primary stage
	 * @param fc
	 *            the fc
	 */
	public ViewController(Stage primaryStage, ClientController fc, String theme) {
		this.primaryStage = primaryStage;
		this.clientController = fc;
		this.theme = theme;
		loader = new FXMLLoader();

		try {
			startLobbyView(this.primaryStage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Input/Output Exception", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * Starts the lobby view, which provides connecting to server and chat.
	 *
	 * @param primaryStage
	 *            the primary stage
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	/**
	 * @param primaryStage
	 * @throws IOException
	 */
	private void startLobbyView(Stage primaryStage) throws IOException {

		Parent root = loader.load(getClass().getResource("/application/lobby/LobbyFXML.fxml").openStream());
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/textures/" + theme + ".css").toExternalForm());

		lobbyController = (LobbyController) loader.getController();
		lobbyController.setViewController(this);
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		//
		primaryStage.setTitle("Settlers of Catan : Lobby");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(e -> System.exit(0));
		isChoosingStage = true;
		// primaryStage.initStyle(StageStyle.UTILITY);
		//
		primaryStage.show();
	}

	/**
	 * starts the View for choosing Player name and Player Color view also
	 * offers Button for setting Player ready for game to start.
	 */
	/**
	 * @throws IOException
	 */

	// public void startChooseView() throws IOException {
	//
	// try {
	// FXMLLoader fxmlLoader = new
	// FXMLLoader(getClass().getResource("/application/lobby/PlayerProfileFXML.fxml"));
	// Parent root1 = (Parent) fxmlLoader.load();
	// playerProfileController = fxmlLoader.getController();
	// playerProfileController.setViewController(this);
	// choosingStage = new Stage();
	// choosingStage.setTitle("Choose Name and Color");
	// choosingStage.setScene(new Scene(root1));
	// isChoosingStage = true;
	// choosingStage.show();
	//
	// } catch (IOException e) {
	// logger.error("Input/Output Exception", e);
	// logger.catching(Level.ERROR, e);
	// e.printStackTrace();
	// }
	// }

	/**
	 * starts GameView and GameViewController
	 */
	/**
	 *
	 */
	void startGameView() {
		isChoosingStage = false;
		FXMLLoader loader = new FXMLLoader();
		Parent root;
		try {
			root = loader.load(getClass().getResource("/network/client/view/GameView.fxml").openStream());
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/textures/" + theme + ".css").toExternalForm());
			primaryStage.setScene(scene);
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			primaryStage.setX(primaryScreenBounds.getMinX());
			primaryStage.setY(primaryScreenBounds.getMinY());
			primaryStage.setWidth(primaryScreenBounds.getWidth());
			primaryStage.setHeight(primaryScreenBounds.getHeight());
			// primaryStage.setFullScreen(false);
			gameViewController = (GameViewController) loader.getController();
			gameViewController.setViewController(this);
			gameViewController.startScene(primaryStage, theme);
			isGameView = true;
			String title = theme.equals("biergarten") ? "Settlers of Biergarten" : "Settlers of Catan";
			primaryStage.setTitle(title);
			primaryStage.show();

		} catch (IOException e) {
			logger.error("Input/Output Exception", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}
	}
	// this.GameViewController = new GameViewController(gameViewStage);
	// this.mainViewController = new MainViewController(this,
	// clientController.getGameLogic().getBoard(), gameViewStage);
	// init();
	// }
	//
	// public View getView() {
	// return mainViewController.getView();
	// }

	/**
	 * Inits the.
	 */
	private void init() {

		/*
		 * view.button.setText("build initial village 2,-2,0");
		 * view.button.setOnAction(e -> { // setCorner(2, -2, 0,
		 * CornerStatus.VILLAGE, 23); serverController.buildInitialVillage(2,
		 * -2, 0, 1); }); view.button2.setText("build initial street 2,-2,1");
		 * view.button2.setOnAction(e -> { // setStreet(-1,-1,0,1);
		 * serverController.buildInitialStreet(2, -2, 1, 1);
		 * serverController.buildStreet(2, -2, 2, 1); //
		 * view.setFieldResourceType(-1, -1, //
		 * fieldColors.get(ResourceType.SHEEP)); }); view.button3.setText(
		 * "build city 2,-2,0"); view.button3.setOnAction(e -> { // setCorner(0,
		 * -2, 1, CornerStatus.CITY, 1); serverController.buildCity(2, -2, 0,
		 * 1); serverController.buildVillage(2, -1, 0, 1);
		 * serverController.buildCity(2, -1, 0, 1); }); view.button4.setText(
		 * "set bandit 2,-2"); view.button4.setOnAction(e -> { //
		 * gameController.setBandit(2, -2); });
		 */

	}

	/**
	 * Gets the client controller.
	 *
	 * @return the client controller
	 */
	public ClientController getClientController() {
		return clientController;
	}

	/**
	 * Gets the game view controller.
	 *
	 * @return the game view controller
	 */
	public GameViewController getGameViewController() {
		return gameViewController;
	}

	/**
	 * Gets the lobby controller.
	 *
	 * @return the lobby controller
	 */
	public LobbyController getLobbyController() {
		return lobbyController;
	}

	/**
	 * Sets the flow controller.
	 *
	 * @param clientController
	 *            the new flow controller
	 */
	public void setFlowController(ClientController clientController) {
		this.clientController = clientController;
	}

	/**
	 * Sets the main view controller.
	 *
	 * @param gameViewController
	 *            the new main view controller
	 */
	public void setMainViewController(GameViewController gameViewController) {
		this.gameViewController = gameViewController;
	}

	/**
	 * Sets the lobby controller.
	 *
	 * @param lobbyController
	 *            the new lobby controller
	 */
	public void setLobbyController(LobbyController lobbyController) {
		this.lobbyController = lobbyController;
	}

	/**
	 * Message receive.
	 *
	 * @param s
	 *            the s
	 */
	public void messageReceive(String s) {
		if (isGameView) {
			gameViewController.receiveChatMessage(s);
		} else {
			lobbyController.receiveChatMessage(s);
		}
	}

	/**
	 * New trade view.
	 */
	public void newTradeView() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getResource("/application/network/client/view/tradeview/tradeView.fxml"));
			Parent root2 = (Parent) fxmlLoader.load();
			tradeViewController = (TradeViewController) fxmlLoader.getController();
			tradeViewController.setViewController(this);
			Stage tradeStage = new Stage();
			tradeStage.setTitle("Choose Name and Color");
			tradeStage.setScene(new Scene(root2));
			tradeStage.show();

		} catch (IOException e) {
			logger.error("Input/Output Exception", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	// public PlayerProfileController getPlayerProfileController() {
	// return playerProfileController;
	//
	// }

	/**
	 * Creates the S response profile runnable.
	 *
	 * @param paramStr
	 *            the param str
	 * @param lobbyController
	 *            the lobby controller
	 * @return the runnable
	 */
	private Runnable createSResponseProfileRunnable(final String paramStr, final LobbyController lobbyController) {
		Runnable aRunnable = new Runnable() {
			public void run() {
				lobbyController.setServerColorAnswer(paramStr);
			}
		};
		return aRunnable;
	}

	/**
	 * Creates the S response runnable.
	 *
	 * @param paramStr
	 *            the param str
	 * @param c
	 *            the c
	 * @return the runnable
	 */
	private Runnable createSResponseRunnable(final String paramStr, final GameViewController c) {
		Runnable aRunnable = new Runnable() {
			public void run() {
				c.setServerResponse(paramStr);
			}
		};
		return aRunnable;
	}

	/**
	 * Sets the server response.
	 *
	 * @param server_response
	 *            the new server response
	 */
	public void setServerResponse(String server_response) {
		if (isGameView) {
			Platform.runLater(createSResponseRunnable(server_response, gameViewController));
		} else if (isChoosingStage) {
			Platform.runLater(createSResponseProfileRunnable(server_response, lobbyController));
		} else {
			logger.debug("Server response: " + server_response);
		}

		// TODO Auto-generated method stub

	}

}
