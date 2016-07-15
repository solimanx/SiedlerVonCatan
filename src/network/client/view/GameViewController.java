package network.client.view;

import static sounds.Sound.playTradeButtonSound;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import audio.Soundeffects;
import enums.CornerStatus;
import enums.HarbourStatus;
import enums.PlayerState;
import enums.ResourceType;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.objects.Corner;
import network.ProtocolToModel;
import network.client.controller.ViewController;
import network.client.view.devcardview.DevCardViewController;
import network.client.view.robberview.RobberViewController;
import network.client.view.tradeview.TradeViewController;

// TODO: Auto-generated Javadoc
public class GameViewController implements Initializable {

	@FXML
	private Circle diceCircle;

	@FXML
	private Text selfWoodText;

	@FXML
	private TextFlow selfWood;

	@FXML
	private Text selfClayText;

	@FXML
	private TextFlow selfClay;

	@FXML
	private Text selfSheepText;

	@FXML
	private TextFlow selfSheep;

	@FXML
	private Text selfCornText;

	@FXML
	private TextFlow selfCorn;

	@FXML
	private Text selfOreText;

	@FXML
	private TextFlow selfOre;

	@FXML
	private ImageView iconLumber;

	@FXML
	private ImageView iconClay;

	@FXML
	private ImageView iconWool;

	@FXML
	private ImageView iconGrain;

	@FXML
	private ImageView iconOre;

	@FXML
	private Label selfLongestTradeRoute;

	@FXML
	private Label secondLongestTradeRoute;

	@FXML
	private Label thirdLongestTradeRoute;

	@FXML
	private Label fourthLongestTradeRoute;

	@FXML
	private Label selfGreatestKnightForce;

	@FXML
	private Label secondGreatestKnightForce;

	@FXML
	private Label thirdGreatestKnightForce;

	@FXML
	private Label fourthGreatestKnightForce;

	@FXML
	private Button rollDiceButton;

	@FXML
	private Button startTradingButton;

	@FXML
	private Button endTurnButton;

	@FXML
	private Button buyCardButton;

	@FXML
	private Button secretButton;

	@FXML
	private Text playerTwoCards;

	@FXML
	private Text playerThreeCards;

	@FXML
	private Text playerFourCards;

	@FXML
	private TextArea messages;

	@FXML
	private TextField messageInput;

	@FXML
	private Pane board;

	@FXML
	private Pane overlay;

	@FXML
	private Pane fieldPane;

	@FXML
	private StackPane boardStack;

	@FXML
	private VBox playerVBoxOne;

	@FXML
	private Label playerNameOne;

	@FXML
	private Label playerNameTwo;

	@FXML
	private Label playerNameThree;

	@FXML
	private Label playerNameFour;

	@FXML
	private Label playerStatusOne;

	@FXML
	private Label playerStatusTwo;

	@FXML
	private Label playerStatusThree;

	@FXML
	private Label playerStatusFour;

	@FXML
	private Label selfVictoryPoints;

	@FXML
	private Label playerTwoVPoints;

	@FXML
	private Label playerThreeVPoints;

	@FXML
	private Label playerFourVPoints;

	@FXML
	private VBox playerFourBox;

	@FXML
	private Label selfName;

	@FXML
	private Text diceResult;

	@FXML
	private Button playCardButton;

	@FXML
	private Button helpButton;

	@FXML
	private Button toggleSoundButton;

	// DEBUG
	@FXML
	private Button openRobberView;

	private ViewController viewController;
	private static Logger logger = LogManager.getLogger(GameViewController.class.getSimpleName());

	// jeweils die letzte Dimension des Arrays zur Speicherung der Koordinaten;
	// für Edge 2 Koordinaten (4 Punkte), weil Anfangs- und Endpunkt
	public double[][][] fieldCoordinates = new double[7][7][2]; // [6][6][2]
	public double[][][][] edgeCoordinates = new double[7][7][3][4]; // [6][6][3][4]
	public double[][][][] cornerCoordinates = new double[7][7][2][2]; // [6][6][2][2]
	private Polygon[][] fields = new Polygon[7][7];
	private ImageView[][][] villages = new ImageView[7][7][2];
	private Polygon[][][] cities = new Polygon[7][7][2];
	public Line[][][] streets = new Line[7][7][3];
	public Circle bandit;

	// Constant values for calculations
	public static double radius = 60.0;
	public double[] boardCenter = new double[2];
	public double[] screenCenter = new double[2];// [2]
	public static double sin60 = Math.sqrt(3) / 2;
	public static double rad60 = Math.PI / 3; // Hilfsvariable sqrt(3)/2
	private static double halfWidth = sin60 * radius;

	/**
	 * modelID => ViewPosition (1, 2, 3, 4)
	 */
	private HashMap<Integer, Integer> playerIDtoViewPosition = new HashMap<Integer, Integer>(4);
	private HashMap<Integer, Integer> viewPositiontoPlayerID = new HashMap<Integer, Integer>(4);
	private HashMap<Integer, Color> playerColors = new HashMap<Integer, Color>(4);
	private HashMap<Integer, String> playerNames = new HashMap<Integer, String>(4);
	// fieldColors kann weg
	private HashMap<enums.ResourceType, Color> fieldColors = new HashMap<enums.ResourceType, Color>(6);
	private HashMap<ResourceType, ImagePattern> resourceImages = new HashMap<ResourceType, ImagePattern>(6);
	private HashMap<HarbourStatus, ImagePattern> harbourImages = new HashMap<HarbourStatus, ImagePattern>(6);

	private HashMap<enums.ResourceType, ImagePattern> imagePatterns = new HashMap<enums.ResourceType, ImagePattern>(6);

	private ViewBoardFactory factory;

	private Stage gameStage;

	private int playerCounter = 2;

	private DropShadow shadow;

	private PlayerState selfState;

	private int[] selfResources = { 0, 0, 0, 0, 0 };

	private SimpleStringProperty response;

	private TradeViewController tradeViewController;

	private Stage tradeStage;

	private FadeTransition woodTransition;

	private FadeTransition clayTransition;

	private FadeTransition woolTransition;

	private FadeTransition cornTransition;

	private FadeTransition oreTransition;

	private List<int[]> streetsSelected = new ArrayList<int[]>();

	private Boolean isStreetDevCard = false;

	public boolean knight = false;

	public String theme;

	/**
	 * Gets the trade view controller.
	 *
	 * @return the trade view controller
	 */
	public TradeViewController getTradeViewController() {
		return tradeViewController;
	}

	/**
	 * Sets the view controller.
	 *
	 * @param viewController
	 *            the new view controller
	 */
	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}

	/**
	 * Sets the checks if is street dev.
	 *
	 * @param isStreetDev
	 *            the new checks if is street dev
	 */
	public void setIsStreetDev(Boolean isStreetDev) {
		this.isStreetDevCard = isStreetDev;
		Platform.runLater(new NewNotification("Street build Developement Card",
				"Build two streets, or double-click on a new street to build just one."));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javafx.fxml.Initializable#initialize(java.net.URL,
	 * java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		response = new SimpleStringProperty("Server responses will appear here");
		factory = new ViewBoardFactory();

	}

	/**
	 * draws and shows the board Pane with game board.
	 *
	 * @param stage
	 *            the stage
	 * @param theme
	 *            the theme
	 */
	public void startScene(Stage stage, String theme) {
		this.theme = theme;
		initFieldColors();
		this.gameStage = stage;
		board.getChildren().add(factory.getViewBoard(stage));
		board.toBack();
		viewController.getClientController().initializeGUI();

		shadow = new DropShadow();
		shadow.setRadius(5);
		shadow.setColor(Color.BLACK);

		FXMLLoader loader = new FXMLLoader();
		Pane root = new Pane();
		try {

			root = loader.load(getClass().getResource("/network/client/view/tradeview/tradeView.fxml").openStream());
			tradeViewController = (TradeViewController) loader.getController();
			Scene scene = new Scene(root);
			scene.getStylesheets().add("/textures/" + theme + ".css");
			tradeStage = new Stage();
			tradeStage.setScene(scene);

			tradeViewController.init(selfResources, viewController, tradeStage);

			tradeStage.setOnCloseRequest(e -> {
				tradeStage.hide();

			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

		woodTransition = generateTransition(selfWoodText);
		clayTransition = generateTransition(selfClayText);
		woolTransition = generateTransition(selfSheepText);
		cornTransition = generateTransition(selfCornText);
		oreTransition = generateTransition(selfOreText);

		woodTransition.onFinishedProperty().set(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				selfWoodText.setFill(Color.WHITE);
			}
		});

		clayTransition.onFinishedProperty().set(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				selfClayText.setFill(Color.WHITE);
			}
		});

		woolTransition.onFinishedProperty().set(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				selfSheepText.setFill(Color.WHITE);
			}
		});

		cornTransition.onFinishedProperty().set(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				selfCornText.setFill(Color.WHITE);
			}
		});

		oreTransition.onFinishedProperty().set(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				selfOreText.setFill(Color.WHITE);
			}
		});

		selfWoodText.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (Integer.parseInt(newValue) - Integer.parseInt(oldValue) > 0) {
					selfWoodText.setFill(Color.LIMEGREEN);
				} else {
					selfWoodText.setFill(Color.RED);
				}
				woodTransition.play();

			}
		});

		selfClayText.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (Integer.parseInt(newValue) - Integer.parseInt(oldValue) > 0) {
					selfClayText.setFill(Color.LIMEGREEN);
				} else {
					selfClayText.setFill(Color.RED);
				}
				clayTransition.play();
			}
		});

		selfSheepText.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (Integer.parseInt(newValue) - Integer.parseInt(oldValue) > 0) {
					selfSheepText.setFill(Color.LIMEGREEN);
				} else {
					selfSheepText.setFill(Color.RED);
				}
				woolTransition.play();
			}
		});

		selfCornText.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (Integer.parseInt(newValue) - Integer.parseInt(oldValue) > 0) {
					selfCornText.setFill(Color.LIMEGREEN);
				} else {
					selfCornText.setFill(Color.RED);
				}
				cornTransition.play();
			}
		});

		selfOreText.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (Integer.parseInt(newValue) - Integer.parseInt(oldValue) > 0) {
					selfOreText.setFill(Color.LIMEGREEN);
				} else {
					selfOreText.setFill(Color.RED);
				}
				oreTransition.play();
			}
		});

		response.addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.equals("OK")) {
					messages.appendText(newValue + "\n");
				}
			}
		});


		toggleSoundButton.graphicProperty().bind(new ImageSoundBinding(Soundeffects.globalVolumeBoolean));

	}

	/**
	 * Generate transition.
	 *
	 * @param text
	 *            the text
	 * @return the fade transition
	 */
	private FadeTransition generateTransition(Text text) {
		FadeTransition transition = new FadeTransition(new Duration(100), text);
		transition.setFromValue(1);
		transition.setToValue(0);
		transition.setAutoReverse(true);
		transition.setCycleCount(4);
		transition.onFinishedProperty().set(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				text.setFill(Color.WHITE);
			}
		});
		return transition;
	}

	/**
	 * Gets the player names.
	 *
	 * @param playerID
	 *            the player ID
	 * @return the player names
	 */
	public String getPlayerNames(Integer playerID) {
		return viewController.getClientController().getGameLogic().getBoard().getPlayer(playerID).getName();
	}

	/**
	 * Inits the player.
	 *
	 * @param modelID
	 *            the model ID
	 * @param playerName
	 *            the player name
	 * @param playerColor
	 *            the player color
	 */
	public void initPlayer(int modelID, String playerName, enums.Color playerColor) {
		if (modelID == viewController.getClientController().getOwnPlayerID()) {
			playerIDtoViewPosition.put(modelID, 1);

		} else {
			playerIDtoViewPosition.put(modelID, playerCounter);
			viewPositiontoPlayerID.put(playerCounter, modelID);

			playerCounter++;
		}
		playerColors.put(modelID, playerColor.getValue());
		switch (playerIDtoViewPosition.get(modelID)) {
		case 1:
			playerNameOne.setText(playerName);
			playerNameOne.setTextFill(playerColor.getValue());
			playerNames.put(1, playerName);
			break;
		case 2:
			playerNameTwo.setText(playerName);
			playerNameTwo.setTextFill(playerColor.getValue());
			playerNames.put(1, playerName);
			break;
		case 3:
			playerNameThree.setText(playerName);
			playerNameThree.setTextFill(playerColor.getValue());
			playerNames.put(1, playerName);
			break;
		case 4:
			playerFourBox.setVisible(true);
			playerNameFour.setText(playerName);
			playerNameFour.setTextFill(playerColor.getValue());
			playerNames.put(1, playerName);
			break;
		}

	}

	/**
	 * Start resource updater.
	 */
	public void startResourceUpdater() {
		Thread th = new Thread(resourceUpdater);
		th.setDaemon(true);
		th.start();
	}

	/**
	 * Auxiliary method filling field color hashmap.
	 */
	private void initFieldColors() {
		ViewFactory vFactory = new ViewFactory(theme);
		fieldColors = vFactory.getFieldColors();
		imagePatterns = vFactory.getImagePatterns();
		harbourImages = vFactory.getHarbourImages();
	}

	/**
	 * Handle end turn button.
	 *
	 * @param event
	 *            the event
	 */
	@FXML
	void handleEndTurnButton(ActionEvent event) {
		viewController.getClientController().endTurn();
		Soundeffects.SELECT.play(Soundeffects.globalVolume);

		// playButtonSound();
	}

	/**
	 * Handle roll dice button.
	 *
	 * @param event
	 *            the event
	 */
	@FXML
	void handleRollDiceButton(ActionEvent event) {
		viewController.getClientController().diceRollRequest();
		// playDiceRollSound();
	}

	/**
	 * Handle start trading button.
	 *
	 * @param event
	 *            the event
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@FXML
	void handleStartTradingButton(ActionEvent event) throws IOException {

		// FXMLLoader loader = new FXMLLoader();
		// Pane root =
		// loader.load(getClass().getResource("/network/client/view/tradeview/tradeView.fxml").openStream());
		// tradeViewController = (TradeViewController) loader.getController();
		// Scene scene = new Scene(root);
		// Stage tradeStage = new Stage();
		// tradeStage.setScene(scene);
		//
		// tradeViewController.init(selfResources, viewController);
		//
		// tradeStage.initModality(Modality.WINDOW_MODAL);
		// tradeStage.initOwner(gameStage);
		tradeViewController.start(selfResources);
		tradeViewController.isPlayerTradingStatus.set((selfState == PlayerState.TRADING_OR_BUILDING) ? true : false);
		tradeStage.show();
		tradeStage.toFront();
		playTradeButtonSound();
	}

	/**
	 * Handle buy card button.
	 *
	 * @param event
	 *            the event
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@FXML
	void handleBuyCardButton(ActionEvent event) throws IOException {
		viewController.getClientController().requestBuyDevelopmentCard();
		Soundeffects.BUYCARD.play(Soundeffects.globalVolume);
		// playCardButton.setDisable(true);
	}

	/**
	 * Handle play card button.
	 *
	 * @param event
	 *            the event
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@FXML
	void handlePlayCardButton(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Parent root;
		try {
			root = loader
					.load(getClass().getResource("/network/client/view/devcardview/DevCardView.fxml").openStream());
			Scene scene = new Scene(root);
			scene.getStylesheets().add("/textures/" + theme + ".css");
			Stage devCardStage = new Stage();
			devCardStage.setScene(scene);
			// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			DevCardViewController devController = (DevCardViewController) loader.getController();
			int[] devCards = viewController.getClientController().getGameLogic().getBoard().getPlayer(0)
					.getPlayerDevCards();
			devController.init(viewController, devCardStage, devCards);
			devCardStage.show();
		} catch (IOException e) {
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}
		// playCardButtonSound();
	}

	/**
	 * Handle help button.
	 *
	 * @param event
	 *            the event
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@FXML
	void handleHelpButton(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Parent root;
		try {
			root = loader.load(getClass().getResource("/network/client/view/helpview/HelpView.fxml").openStream());
			Scene scene = new Scene(root);
			scene.getStylesheets().add("/textures/" + theme + ".css");
			Stage helpView = new Stage();
			helpView.setScene(scene);
			helpView.show();
		} catch (IOException e) {
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}
		Soundeffects.SELECT.play(Soundeffects.globalVolume);

		// playButtonSound();
	}

	/**
	 * Handle cheat button.
	 *
	 * @param event
	 *            the event
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@FXML
	@Deprecated
	void handleCheatButton(ActionEvent event) throws IOException {
		Stage cheatStage = new Stage();
		VBox cheatRoot = new VBox();
		Scene cheatScene = new Scene(cheatRoot);
		TextField cheatField = new TextField();
		Button ok = new Button("Send JSON");
		Soundeffects.SELECT.play(Soundeffects.globalVolume);
		// playButtonSound();
		ok.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				cheatStage.close();
				viewController.getClientController().getClientOuptputHandler().sendCheat(cheatField.getText());

			}
		});

		cheatRoot.getChildren().addAll(cheatField, ok);
		cheatStage.setScene(cheatScene);
		cheatStage.show();
	}

	@FXML
	void handleToggleSound(ActionEvent event) throws IOException {
		Soundeffects.toggleMuteOnOff();

		if (true) {
			toggleSoundButton.setGraphic(new ImageView("/textures/vol_mute.png"));
		} else {
			toggleSoundButton.setGraphic(new ImageView("/textures/vol_up.png"));
		}
	}

	/**
	 * Handle secret button.
	 *
	 * @param event
	 *            the event
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@FXML
	void handleSecretButton(ActionEvent event) throws IOException {
		Stage cheatStage = new Stage();
		VBox cheatRoot = new VBox();
		Scene cheatScene = new Scene(cheatRoot);
		TextField cheatField = new TextField();
		Button ok = new Button("Send Cheat");
		Soundeffects.SELECT.play(Soundeffects.globalVolume);
		// playButtonSound();
		ok.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				viewController.getClientController().getClientOuptputHandler().sendCheatCode(cheatField.getText());
				cheatStage.close();

			}
		});

		cheatRoot.getChildren().addAll(cheatField, ok);
		cheatStage.setScene(cheatScene);
		cheatStage.show();
	}

	/**
	 * Send message.
	 *
	 * @param event
	 *            the event
	 */
	@FXML
	void sendMessage(ActionEvent event) {
		String message = messageInput.getText();
		messageInput.clear();
		viewController.getClientController().sendChatMessage(message);
		// playNotificationSound();
	}

	/**
	 * Village click.
	 *
	 * @param villageCoordinates
	 *            the village coordinates
	 */
	public void villageClick(int[] villageCoordinates) {
		// Polygon village =
		// villages[villageCoordinates[0]][villageCoordinates[1]][villageCoordinates[2]];
		// if (village.getFill().equals(playerColors.get(0))) {
		// viewController.getClientController().requestBuildCity(villageCoordinates[0],
		// villageCoordinates[1],
		// villageCoordinates[2]);
		// }
		if (selfState == PlayerState.TRADING_OR_BUILDING || selfState == PlayerState.BUILDING_VILLAGE)
			viewController.getClientController().requestBuildVillage(villageCoordinates[0], villageCoordinates[1],
					villageCoordinates[2]);

	}

	/**
	 * //* @param streetCoordinates.
	 *
	 * @param streetCoord
	 *            the street coord
	 */
	public void streetClick(int[] streetCoord) {
		if (isStreetDevCard && streetsSelected.size() <= 2) {
			Line street = streets[streetCoord[0]][streetCoord[1]][streetCoord[2]];
			streetsSelected.add(streetCoord);
			street.setOpacity(0.8);
			if (streetsSelected.size() == 2) {
				int[] streetCoord1 = streetsSelected.get(0);
				int[] streetCoord2 = streetsSelected.get(1);

				viewController.getClientController().playStreetBuildCard(streetCoord1[0], streetCoord1[1],
						streetCoord1[2], streetCoord2[0], streetCoord2[1], streetCoord2[2]);

				streetsSelected.clear();
				isStreetDevCard = false;

			}
		} else if (selfState == PlayerState.TRADING_OR_BUILDING || selfState == PlayerState.BUILDING_STREET) {
			viewController.getClientController().requestBuildStreet(streetCoord[0], streetCoord[1], streetCoord[2]);
		}
	}

	/**
	 * Field click.
	 *
	 * @param fieldCoordinates
	 *            the field coordinates
	 * @param knight
	 *            the knight
	 */
	@SuppressWarnings("null")
	public void fieldClick(int[] fieldCoordinates, boolean knight) {
		if (selfState == PlayerState.MOVE_ROBBER || knight == true) {
			List<String> choices = new ArrayList<>();
			String player2 = playerNameTwo.getText();
			choices.add(player2);
			String player3 = playerNameThree.getText();
			choices.add(player3);
			String player4 = playerNameFour.getText();
			choices.add(player4);

			ChoiceDialog<String> dialog = new ChoiceDialog<>("", choices);
			dialog.initStyle(StageStyle.UTILITY);
			dialog.setTitle("Stealing from...");
			dialog.setHeaderText("Choose Player from whom you want to steal");
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				int resultID = 0;
				String string = result.get();
				if (string != null) {
					if (string.equals(player2)) {
						resultID = 2;
					} else if (string.equals(player3)) {
						resultID = 3;
					} else if (string.equals(player4)) {
						resultID = 4;
					}
					if (knight == false) {
						viewController.getClientController().requestSetBandit(fieldCoordinates[0] - 3,
								fieldCoordinates[1] - 3, viewPositiontoPlayerID.get(resultID));
					} else {
						knight = false;
						viewController.getClientController().playKnightCard(fieldCoordinates[0] - 3,
								fieldCoordinates[1] - 3, viewPositiontoPlayerID.get(resultID));
					}
				} else {
					viewController.getClientController().requestSetBandit(fieldCoordinates[0] - 3,
							fieldCoordinates[1] - 3, (Integer) null);
				}

			}
		}

	}

	/**
	 * Receive chat message.
	 *
	 * @param line
	 *            the line
	 */
	public void receiveChatMessage(String line) {
		messages.appendText(line + "\n");
		Soundeffects.CHATRECEIVE.play(Soundeffects.globalVolume);
		// playNotificationSound();
	}

	/**
	 * sets Street on coordinates u,v,dir to Player Color of Player with ID
	 * modelID.
	 *
	 * @param u
	 *            axial coordinate (e.g. -3)
	 * @param v
	 *            axial coordinate (e.g. -3)
	 * @param dir
	 *            coordinate (e.g. 1 | 2 | 0)
	 * @param modelID
	 *            the model ID
	 */
	public void setStreet(int u, int v, int dir, int modelID) {
		Soundeffects.BUILD.play(Soundeffects.globalVolume);
		Line street = streets[u + 3][v + 3][dir];
		street.setOpacity(1.0);
		street.setStroke(playerColors.get(modelID));
		street.getStyleClass().remove("street");
		// street.setEffect(shadow);
	}

	/**
	 * Sets the bandit.
	 *
	 * @param u
	 *            the u
	 * @param v
	 *            the v
	 */
	public void setBandit(int u, int v) {
		bandit.setCenterX(fieldCoordinates[u + 3][v + 3][0]);
		bandit.setCenterY(fieldCoordinates[u + 3][v + 3][1]);
		bandit.setOpacity(1.0);
		Soundeffects.ROBBER.play(Soundeffects.globalVolume);

	}

	/**
	 * Sets the corner.
	 *
	 * @param u
	 *            the u
	 * @param v
	 *            the v
	 * @param dir
	 *            the dir
	 * @param buildType
	 *            the build type
	 * @param modelID
	 *            the model ID
	 */
	public void setCorner(int u, int v, int dir, CornerStatus buildType, int modelID) {
		if (buildType == enums.CornerStatus.VILLAGE) {
			setVillage(u, v, dir, playerColors.get(modelID));

		} else {
			setCity(u, v, dir, playerColors.get(modelID));
		}
	}

	/**
	 * Sets the village.
	 *
	 * @param u
	 *            the u
	 * @param v
	 *            the v
	 * @param dir
	 *            the dir
	 * @param playerColor
	 *            the player color
	 */
	public void setVillage(int u, int v, int dir, Color playerColor) {
		if (!Soundeffects.isMuted())
			Soundeffects.BUILD.play();
		ImageView village = villages[u + 3][v + 3][dir];

		village.setEffect(getBlushEffect(playerColor, village));

		// village.setFill(playerColor);
		village.setOpacity(1.0);
		// village.setEffect(shadow);
		village.getStyleClass().remove("village");
		int[] coordinates = { u, v, dir };
		village.setOnMouseClicked(e -> {
			cityClick(coordinates);
		});
	}

	/**
	 * Hides village hover.
	 *
	 * @param u
	 *            the u
	 * @param v
	 *            the v
	 * @param dir
	 *            the dir
	 */
	public void removeVillage(int u, int v, int dir) {
		ImageView village = villages[u + 3][v + 3][dir];
		village.getStyleClass().remove("village");
	}

	/**
	 * City click.
	 *
	 * @param coordinates
	 *            the coordinates
	 */
	private void cityClick(int[] coordinates) {

		viewController.getClientController().requestBuildCity(coordinates[0], coordinates[1], coordinates[2]);

	}

	/**
	 * Sets the field.
	 *
	 * @param u
	 *            the u
	 * @param v
	 *            the v
	 * @param resourceType
	 *            the resource type
	 * @param diceIndex
	 *            the dice index
	 */
	public void setField(int u, int v, ResourceType resourceType, Integer diceIndex) {
		fields[u + 3][v + 3].setFill(imagePatterns.get(resourceType));
		if (diceIndex != null) {
			Text text = new Text("" + diceIndex);
			text.setBoundsType(TextBoundsType.VISUAL);
			text.setTextAlignment(TextAlignment.CENTER);
			Circle circle = new Circle(15.0);
			circle.setFill(Color.WHITE);
			StackPane chip = new StackPane(circle, text);
			chip.toFront();
			chip.setTranslateX(fieldCoordinates[u + 3][v + 3][0] - 15.0);
			chip.setTranslateY(fieldCoordinates[u + 3][v + 3][1] - 15.0);
			board.getChildren().add(chip);
		}

	}

	/**
	 * sets Harbour on a Field(u,v)
	 * <p>
	 * //* @param u //* @param v //* @param harbourType.
	 *
	 * @param hCorners
	 *            the new harbour
	 */
	public void setHarbour(Corner[] hCorners) {
		for (int i = 0; i < hCorners.length; i += 2) {
			int[] corner1 = ProtocolToModel.getCornerCoordinates(hCorners[i].getCornerID());
			int[] corner2 = ProtocolToModel.getCornerCoordinates(hCorners[i + 1].getCornerID());
			double[] cCoord1 = cornerCoordinates[corner1[0] + 3][corner1[1] + 3][corner1[2]];
			double[] cCoord2 = cornerCoordinates[corner2[0] + 3][corner2[1] + 3][corner2[2]];
			int[] fieldCoord = viewController.getClientController().getGameLogic().getBoard()
					.getHarbourMiddlepoint(corner1, corner2);
			double[] fCoord = fieldCoordinates[fieldCoord[0] + 3][fieldCoord[1] + 3];
			Polygon triangle = new Polygon(cCoord1[0], cCoord1[1], cCoord2[0], cCoord2[1], fCoord[0], fCoord[1]);
			triangle.setFill(Color.web("#fff", 0.5));
			Circle circle = new Circle(20.0);
			circle.setTranslateX(fCoord[0]);
			circle.setTranslateY(fCoord[1]);
			circle.getStyleClass().add("shadow");
			enums.HarbourStatus hstate = viewController.getClientController().getGameLogic().getBoard()
					.getCornerAt(corner1[0], corner1[1], corner1[2]).getHarbourStatus();
			circle.setFill(harbourImages.get(hstate));
			circle.toFront();

			fieldPane.getChildren().addAll(triangle, circle);
		}
	}

	/**
	 * sets the amount of recource cards of self and other players.
	 *
	 * @param modelID
	 *            the model ID
	 * @param resources
	 *            the resources
	 */
	public void setResourceCards(int modelID, int[] resources) {
		switch (playerIDtoViewPosition.get(modelID)) {
		case 1:
			// Amount of Landscape Resource Cards: {WOOD, CLAY, ORE, SHEEP,
			// CORN}
			selfResources = resources;
			selfWoodText.setText(Integer.toString(resources[0]));
			selfClayText.setText(Integer.toString(resources[1]));
			selfSheepText.setText(Integer.toString(resources[3]));
			selfCornText.setText(Integer.toString(resources[4]));
			selfOreText.setText(Integer.toString(resources[2]));
			break;
		case 2:
			playerTwoCards.setText(Integer.toString(resources[0]));
			break;
		case 3:
			playerThreeCards.setText(Integer.toString(resources[0]));
			break;
		case 4:
			playerFourCards.setText(Integer.toString(resources[0]));
			break;
		}
	}

	/**
	 * shows result of dice roll on game view.
	 *
	 * @param playerID
	 *            the player ID
	 * @param result
	 *            the new dice roll result
	 */
	public void setDiceRollResult(Integer playerID, int result) {
		Soundeffects.DICEROLL.play(Soundeffects.globalVolume);
		diceCircle.setFill(playerColors.get(playerID));
		diceResult.setText(String.valueOf(result));
	}

	/**
	 * sets the victory points in the game view to corresponding player.
	 *
	 * @param modelID
	 *            the model ID
	 * @param victoryPoints
	 *            the victory points
	 */
	public void setVictoryPoints(int modelID, int victoryPoints) {
		String victoryString = victoryPoints + " Victory Points";
		switch (playerIDtoViewPosition.get(modelID)) {
		case 1:
			selfVictoryPoints.setText(victoryString);
			break;
		case 2:
			playerTwoVPoints.setText(victoryString);
			break;
		case 3:
			playerThreeVPoints.setText(victoryString);
			break;
		case 4:
			playerFourVPoints.setText(victoryString);
			break;
		default:
			break;
		}
	}

	/**
	 * Sets the player status.
	 *
	 * @param modelID
	 *            the model ID
	 * @param state
	 *            the state
	 */
	public void setPlayerStatus(int modelID, PlayerState state) {
		switch (playerIDtoViewPosition.get(modelID)) {
		case 1:
			playerStatusOne.setText(state.toString());
			selfState = state;
			if (viewController.getClientController().getGameLogic().getBoard().getPlayer(modelID).hasLongestRoad()) {
				Soundeffects.LONGESTROAD.play(Soundeffects.globalVolume);
				selfLongestTradeRoute.setText("Longest Trade Road");
			} else {
				selfLongestTradeRoute.setText("");
			}
			if (viewController.getClientController().getGameLogic().getBoard().getPlayer(modelID).hasLargestArmy()) {
				Soundeffects.SWORD.play(Soundeffects.globalVolume);
				selfGreatestKnightForce.setText("Largest Army");
			} else {
				selfGreatestKnightForce.setText("");
			}

			switch (state) {
			case DICEROLLING:
				rollDiceButton.setDisable(false);
				endTurnButton.setDisable(true);
				break;
			case DISPENSE_CARDS_ROBBER_LOSS:
				setRobberLossState();
				break;
			case MOVE_ROBBER:
				knight = false;
				setMoveRobberState();
				break;
			case WAITING:
				rollDiceButton.setDisable(true);
				endTurnButton.setDisable(true);
				playCardButton.setDisable(true);
				buyCardButton.setDisable(true);
				break;
			case TRADING_OR_BUILDING:
				endTurnButton.setDisable(false);
			default:
				rollDiceButton.setDisable(true);
				endTurnButton.setDisable(false);
				playCardButton.setDisable(false);
				playCardButton.setDisable(false);
				buyCardButton.setDisable(false);
				break;
			}
			break;
		case 2:
			playerStatusTwo.setText(state.toString());
			break;
		case 3:
			playerStatusThree.setText(state.toString());
			break;
		case 4:
			playerStatusFour.setText(state.toString());
			break;
		default:
			break;
		}
	}

	/**
	 * Sets the move robber state.
	 */
	public void setMoveRobberState() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Move the Robber");
		alert.setHeaderText("You can move the robber and steal from adjoining players!");
		alert.setContentText("Click on any field to move the robber on the field.");
		alert.initOwner(gameStage);
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.showAndWait();
		// playMoveRobberSound();
	}

	/**
	 * Auxiliary methode showing Robber Loss window.
	 */
	@FXML
	private void setRobberLossState() {
		FXMLLoader loader = new FXMLLoader();
		Parent root;
		try {
			root = loader
					.load(getClass().getResource("/network/client/view/robberview/GiveResources.fxml").openStream());
			Scene scene = new Scene(root);
			scene.getStylesheets().add("/textures/" + theme + ".css");
			Stage robberStage = new Stage();
			robberStage.setScene(scene);
			// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			RobberViewController robberController = (RobberViewController) loader.getController();
			robberController.init(this);
			robberController.createSpinner(selfResources);
			robberStage.initModality(Modality.APPLICATION_MODAL);
			robberStage.initOwner(gameStage);
			robberStage.show();
		} catch (IOException e) {
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * Robber loss.
	 *
	 * @param result
	 *            the result
	 */
	public void robberLoss(int[] result) {
		viewController.getClientController().robberLoss(result);
	}

	/**
	 * Sets the server response.
	 *
	 * @param response
	 *            the new server response
	 */
	public void setServerResponse(String response) {
		this.response.set(response);
	}

	/**
	 * Sets the city.
	 *
	 * @param u
	 *            the u
	 * @param v
	 *            the v
	 * @param dir
	 *            the dir
	 * @param playerColor
	 *            the player color
	 */
	public void setCity(int u, int v, int dir, Color playerColor) {
		Soundeffects.BUILD.play(Soundeffects.globalVolume);
		ImageView city = villages[u + 3][v + 3][dir];
		city.setImage(new Image("/textures/city.png"));
		city.setEffect(getBlushEffect(playerColor, city));
		city.setOpacity(1.0);
		// city.setEffect(shadow);
		// city.setStroke(Color.BLACK);
	}

	/**
	 * Sets the longest trade road.
	 *
	 * @param modelID
	 *            the new longest trade road
	 */
	public void setLongestTradeRoad(int modelID) {
		Platform.runLater(new Runnable() {
			int modelID;

			@Override
			public void run() {
				selfLongestTradeRoute.setText("");
				secondLongestTradeRoute.setText("");
				thirdLongestTradeRoute.setText("");
				fourthLongestTradeRoute.setText("");
				String string = "Longest Trade Road!";
				if (modelID != -1) {
					switch (playerIDtoViewPosition.get(modelID)) {
					case 1:
						selfLongestTradeRoute.setText(string);
						break;
					case 2:
						secondLongestTradeRoute.setText(string);
						break;
					case 3:
						thirdLongestTradeRoute.setText(string);
						break;
					case 4:
						fourthLongestTradeRoute.setText(string);
						break;
					}
				}
			}

			public Runnable init(int modelID) {
				this.modelID = modelID;
				return (this);
			}
		}.init(modelID));

	}

	/**
	 * Sets the greatest knight force.
	 *
	 * @param modelID
	 *            the new greatest knight force
	 */
	public void setGreatestKnightForce(int modelID) {
		Platform.runLater(new Runnable() {
			int modelID;

			@Override
			public void run() {
				selfGreatestKnightForce.setText("");
				secondGreatestKnightForce.setText("");
				thirdGreatestKnightForce.setText("");
				fourthGreatestKnightForce.setText("");
				String string = "Greatest Knight Force!";
				switch (playerIDtoViewPosition.get(modelID)) {
				case 1:
					selfGreatestKnightForce.setText(string);
					break;
				case 2:
					secondGreatestKnightForce.setText(string);
					break;
				case 3:
					thirdGreatestKnightForce.setText(string);
					break;
				case 4:
					fourthGreatestKnightForce.setText(string);
					break;
				}
			}

			public Runnable init(int modelID) {
				this.modelID = modelID;
				return (this);
			}
		}.init(modelID));

	}

	/**
	 * Method showing a new window, which displays, who the winner of the game
	 * is.
	 *
	 * @param winnerID
	 *            the winner ID
	 */
	public void showVictory(int winnerID) {
		String winnerName = viewController.getClientController().getGameLogic().getBoard().getPlayer(winnerID)
				.getName();

		Platform.runLater(new Runnable() {
			String winner;

			@Override
			public void run() {
				Soundeffects.VICTORY.play(Soundeffects.globalVolume);
				// TODO Loss Sound
				VBox vBox = new VBox(10);
				vBox.setPadding(new Insets(5));
				vBox.setSpacing(8);
				Text title = new Text("Das Spiel ist aus!");
				Text text = new Text("Glückwunsch zum verdienten Sieg.");
				Text text1 = new Text("Unser Gewinner ist: " + winner);

				ImageView image = new ImageView(
						new Image(getClass().getResourceAsStream("/textures/standard/winner.png")));
				vBox.getChildren().addAll(title, text, text1, image);

				Scene scene = new Scene(vBox, 600, 600, Color.BEIGE);
				Stage victoryStage = new Stage();
				victoryStage.setWidth(415);
				victoryStage.setHeight(200);
				victoryStage.setScene(scene);
				victoryStage.sizeToScene();
				victoryStage.initModality(Modality.APPLICATION_MODAL);
				victoryStage.initOwner(gameStage);
				victoryStage.show();
			}

			public Runnable init(String winnerName) {
				this.winner = winnerName;
				return (this);
			}
		}.init(winnerName));

	}

	/**
	 * Alert.
	 *
	 * @param message
	 *            the message
	 */
	public void alert(String message) {
		Platform.runLater(new Runnable() {
			String msg;

			@Override
			public void run() {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Alert");
				alert.setHeaderText(msg);
				alert.initOwner(gameStage);
				alert.initModality(Modality.APPLICATION_MODAL);
				alert.showAndWait();
			}

			public Runnable init(String message) {
				this.msg = message;
				return (this);
			}
		}.init(message));

	}

	public void notify(String title, String message) {
		NewNotification newNotification = new NewNotification(title, message);
		Platform.runLater(newNotification);
	}

	/**
	 * Checks if is knight.
	 *
	 * @return true, if is knight
	 */
	public boolean isKnight() {
		return knight;
	}

	/**
	 * Sets the knight.
	 *
	 * @param knight
	 *            the new knight
	 */
	public void setKnight(boolean knight) {
		this.knight = knight;
	}

	Task resourceUpdater = new Task<Void>() {

		protected Void call() throws Exception {
			while (true) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						setResourceCards(0, viewController.getClientController().getGameLogic().getBoard().getPlayer(0)
								.getResources());
						int[] hidden1 = { viewController.getClientController().getGameLogic().getBoard().getPlayer(1)
								.getHiddenResources() };
						setResourceCards(1, hidden1);
						int[] hidden2 = { viewController.getClientController().getGameLogic().getBoard().getPlayer(2)
								.getHiddenResources() };
						setResourceCards(2, hidden2);
						if (viewController.getClientController().getAmountPlayers() > 3) {
							int[] hidden3 = { viewController.getClientController().getGameLogic().getBoard()
									.getPlayer(3).getHiddenResources() };
							setResourceCards(3, hidden3);
						}
					}
				});
				Thread.sleep(1000);
			}

		}

	};

	public class NewNotification implements Runnable {

		String message;
		String title;

		/**
		 * Instantiates a new new alert.
		 *
		 * @param title
		 *            the title
		 * @param message
		 *            the message
		 */
		public NewNotification(String title, String message) {
			this.title = title;
			this.message = message;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(title);
			alert.setContentText(message);

			alert.showAndWait();

		}

	}

	/**
	 * Gets the blush effect.
	 *
	 * @param playerColor
	 *            the player color
	 * @param imageView
	 *            the image view
	 * @return the blush effect
	 */
	private Effect getBlushEffect(Color playerColor, ImageView imageView) {
		ColorAdjust monochrome = new ColorAdjust();
		monochrome.setSaturation(-1.0);

		Blend blush = new Blend(BlendMode.MULTIPLY, monochrome,
				new ColorInput(0, 0, imageView.getImage().getWidth(), imageView.getImage().getHeight(), playerColor));

		return blush;
	}

	/**
	 * Inner Class for constructing the board. Instantiated at initial phase of
	 * Game
	 *
	 * @author mattmoos
	 */
	private class ViewBoardFactory {
		private Pane boardPane;
		private List<Shape> streetFigures = new LinkedList<Shape>();
		private List<Shape> fieldFigures = new LinkedList<Shape>();
		private List<ImageView> villageFigures = new LinkedList<ImageView>();
		private List<Shape> cityFigures = new LinkedList<Shape>();

		/**
		 * Gets the view board.
		 *
		 * @param stage
		 *            the stage
		 * @return the view board
		 */
		public StackPane getViewBoard(Stage stage) {
			boardPane = new Pane();
			boardStack = new StackPane();
			fieldPane = new Pane();
			overlay = new Pane();
			boardCenter[0] = stage.getWidth() / 2;
			boardCenter[1] = stage.getHeight() / 2 - 40;
			radius = stage.getHeight() / 16;
			halfWidth = sin60 * radius;

			calculateFieldCenters(boardCenter);
			calculateCornerCenters();
			calculateEdgeCorners();
			initBoard();

			// boardPane.getChildren().addAll(0, villageFigures);
			overlay.getChildren().addAll(0, villageFigures);
			overlay.getChildren().addAll(0, streetFigures);
			overlay.getChildren().addAll(0, cityFigures);
			overlay.setPickOnBounds(false);
			fieldPane.getChildren().addAll(0, fieldFigures);

			boardStack.getChildren().addAll(fieldPane, overlay);

			// boardPane.getChildren().addAll(0, streetFigures);
			// boardPane.getChildren().addAll(0, cityFigures);
			// boardPane.getChildren().addAll(0, fieldFigures);

			return boardStack;
			// return boardPane;
		}

		/**
		 * Inits the board.
		 */
		private void initBoard() {

			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 7; j++) {
					if (fieldCoordinates[i][j][0] > 0) {
						Polygon hexagon = drawHexagon(
								createHexagon(fieldCoordinates[i][j][0], fieldCoordinates[i][j][1]));
						hexagon.setVisible(true);
						int[] resourceCoordinates = { i, j };
						hexagon.setOnMouseClicked(e -> {
							fieldClick(resourceCoordinates, knight);
						});
						fieldFigures.add(0, hexagon);
						fields[i][j] = hexagon;
						for (int l = 0; l < 3; l++) {
							if (edgeCoordinates[i][j][l][0] > 0) {
								Line street = drawStreet(edgeCoordinates[i][j][l]);
								street.setOpacity(0);
								street.setStroke(Color.WHITE);
								street.getStyleClass().add("street");
								streets[i][j][l] = street;

								int[] streetCoordinates = { i, j, l };

								street.setOnMouseClicked(e -> {
									streetClick(streetCoordinates);
								});

								streetFigures.add(street);
							}
						}
						for (int k = 0; k < 2; k++) {
							if (cornerCoordinates[i][j][k][0] > 0) {
								ImageView village = drawVillage(cornerCoordinates[i][j][k]);
								village.setOpacity(0);
								village.getStyleClass().add("village");
								villages[i][j][k] = village;

								int[] villageCoordinates = { i, j, k };

								village.setOnMouseClicked(e -> {
									villageClick(villageCoordinates);
								});
								village.toFront();
								villageFigures.add(village);

								// Polygon city =
								// drawCity(cornerCoordinates[i][j][k]);
								// city.setOpacity(0);
								// cities[i][j][k] = city;
								// city.toFront();
								// cityFigures.add(city);

							}
						}
					}
				}
			}
			bandit = drawBandit();
			bandit.setOpacity(0);
			bandit.toFront();
			fieldFigures.add(bandit);

		}

		/**
		 * // * @param centerCoordinates.
		 *
		 * @param x
		 *            the x
		 * @param y
		 *            the y
		 * @return double array of coordinates of 6 Points (12 double values)
		 *         calculates coordinates of Hexagon from given center
		 *         coordinates
		 */
		public double[] createHexagon(double x, double y) {
			double[] points = new double[12];
			int j = 1;
			for (int i = 0; i < points.length; i = i + 2) {
				points[i] = (double) (x + (radius) * Math.sin(j * rad60));
				points[i + 1] = (double) (y + (radius) * Math.cos(j * rad60));
				j++;
			}
			return points;
		}

		/**
		 * Creates a new ViewBoard object.
		 *
		 * @param i
		 *            the i
		 * @param j
		 *            the j
		 * @param k
		 *            the k
		 * @return the polygon
		 */
		private ImageView createVillage(int i, int j, int k) {
			ImageView village;
			if (k == 0) {
				double[] center = { fieldCoordinates[i][j][0], fieldCoordinates[i][j][1] - radius };
				village = drawVillage(center);
				village.setVisible(false);
				return village;
			} else {
				double[] center = { fieldCoordinates[i][j][0], fieldCoordinates[i][j][1] + radius };
				village = drawVillage(center);
				village.setVisible(false);
				return village;
			}
		}

		/**
		 * draws a Circle with diceIndex.
		 *
		 * @param u
		 *            the u
		 * @param v
		 *            the v
		 * @param diceIndex
		 *            the dice index
		 */
		public void setFieldChip(int u, int v, int diceIndex) {
			Text text = new Text("" + diceIndex);
			text.setBoundsType(TextBoundsType.VISUAL);
			text.setTextAlignment(TextAlignment.CENTER);
			Circle circle = new Circle(15.0);
			circle.setFill(Color.WHITE);
			StackPane chip = new StackPane(circle, text);
			chip.toFront();
			chip.setTranslateX(fieldCoordinates[u + 3][v + 3][0] - 15.0);
			chip.setTranslateY(fieldCoordinates[u + 3][v + 3][1] - 15.0);
			board.getChildren().add(chip);
		}

		/**
		 * takes 6 coordinate pairs (x,y) and draws a Polygon hexagon.
		 *
		 * @param points
		 *            the points
		 * @return Polygon
		 */
		public Polygon drawHexagon(double[] points) {
			Polygon hexagon = new Polygon(points);
			hexagon.setFill(Color.LIGHTSKYBLUE);
			hexagon.getStyleClass().add("hexborder");
			hexagon.setStrokeWidth(3);
			return hexagon;
		}

		/**
		 * takes pair of coordinates as center point and draws a village.
		 *
		 * @param center
		 *            the center
		 * @return Polygon
		 */
		public ImageView drawVillage(double[] center) {
			// Polygon village = new Polygon(center[0], center[1] - 18,
			// center[0] + 10, center[1] - 10, center[0] + 10,
			// center[1] + 10, center[0] - 10, center[1] + 10, center[0] - 10,
			// center[1] - 10);
			Image villageImage = new Image("/textures/village.png");
			ImageView villageImageView = new ImageView(villageImage);
			villageImageView.setTranslateX(center[0] - 20);
			villageImageView.setTranslateY(center[1] - 20);
			villageImageView.setClip(new ImageView(villageImage));
			villageImageView.setScaleX(40 * 1.5 / radius);
			villageImageView.setScaleY(40 * 1.5 / radius);

			villageImageView.setEffect(getBlushEffect(Color.PINK, villageImageView));
			villageImageView.setCache(true);
			villageImageView.setCacheHint(CacheHint.SPEED);
			// village.setStroke(Color.BLACK);
			return villageImageView;
		}

		/**
		 * Auxiliary method drawing cities.
		 *
		 * @param center
		 *            the center
		 * @return Polygon city
		 */
		private Polygon drawCity(double[] center) {

			Polygon city = new Polygon(center[0] + 5, center[1] - 10, center[0] + 5, center[1] - 20, center[0] + 10,
					center[1] - 20, center[0] + 10, center[1] + 10, center[0] - 10, center[1] + 10, center[0] - 10,
					center[1] - 20, center[0] - 5, center[1] - 20, center[0] - 5, center[1] - 10);
			city.setStroke(Color.BLACK);
			return city;
		}

		/**
		 * Draw street.
		 *
		 * @param coordinates
		 *            the coordinates
		 * @return the line
		 */
		public Line drawStreet(double[] coordinates) {
			Line street = new Line(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
			street.setStrokeWidth(8.0);

			return street;
		}

		/**
		 * Draw bandit.
		 *
		 * @return the circle
		 */
		public Circle drawBandit() {
			Circle bandit = new Circle(25.0);
			bandit.setFill(Color.BLACK);
			bandit.setCenterX(boardCenter[0]);
			bandit.setCenterY(boardCenter[1]);
			return bandit;
		}

		/**
		 * auxiliary method calculating center coordinates of every field.
		 * windowsCenter is taken as center point of board
		 *
		 * @param windowCenter
		 *            the window center
		 */
		private void calculateFieldCenters(double[] windowCenter) {
			double x;
			double y;
			for (int u = -3; u <= 3; u++) {
				for (int v = -3; v <= 3; v++) {
					if (Math.abs(u + v) <= 3) {
						x = +1 * halfWidth * (u + 2 * v) + windowCenter[0];
						y = +1 * 1.5 * radius * u + windowCenter[1];
						fieldCoordinates[v + 3][u + 3][0] = x;
						fieldCoordinates[v + 3][u + 3][1] = y;
					}
				}
			}
		}

		/**
		 * calculates the edge corners and saves them in edgeCoordinate array.
		 * dependency: filled fieldCoordinates array
		 */
		private void calculateEdgeCorners() {
			double x1;
			double y1;
			double x2;
			double y2;
			for (int u = 0; u < 7; u++) {
				for (int v = 0; v < 7; v++) {
					x1 = fieldCoordinates[u][v][0];
					y1 = fieldCoordinates[u][v][1] - radius;
					x2 = fieldCoordinates[u][v][0] - halfWidth;
					y2 = fieldCoordinates[u][v][1] - radius / 2;
					edgeCoordinates[u][v][0][0] = x1;
					edgeCoordinates[u][v][0][1] = y1;
					edgeCoordinates[u][v][0][2] = x2;
					edgeCoordinates[u][v][0][3] = y2;

					x1 = fieldCoordinates[u][v][0];
					y1 = fieldCoordinates[u][v][1] - radius;
					x2 = fieldCoordinates[u][v][0] + halfWidth;
					y2 = fieldCoordinates[u][v][1] - (radius / 2);
					edgeCoordinates[u][v][1][0] = x1;
					edgeCoordinates[u][v][1][1] = y1;
					edgeCoordinates[u][v][1][2] = x2;
					edgeCoordinates[u][v][1][3] = y2;

					edgeCoordinates[u][v][2][0] = x2;
					edgeCoordinates[u][v][2][1] = y2;
					edgeCoordinates[u][v][2][2] = x2;
					edgeCoordinates[u][v][2][3] = y2 + radius;

				}

			}
			filterUnusedEdges();

		}

		/**
		 * calculates the center points of corners and saves them in
		 * cornerCoordinate array. dependency: filled fieldCoordinates array
		 */
		private void calculateCornerCenters() {
			double x;
			double y;
			for (int u = -3; u <= 3; u++) {
				for (int v = -3; v <= 3; v++) {
					if (true) {
						x = fieldCoordinates[u + 3][v + 3][0];
						y = fieldCoordinates[u + 3][v + 3][1] - radius;
						cornerCoordinates[u + 3][v + 3][0][0] = x;
						cornerCoordinates[u + 3][v + 3][0][1] = y;
						// x = fieldCoordinates[u + 3][v + 3][0];
						y = fieldCoordinates[u + 3][v + 3][1] + radius;
						cornerCoordinates[u + 3][v + 3][1][0] = x;
						cornerCoordinates[u + 3][v + 3][1][1] = y;
					}
				}

			}
			filterUnusedCorners();
		}

		/**
		 * sets x-coordinate of unused corners to 0 fields with x-coordinate 0
		 * are sea.
		 */
		private void filterUnusedCorners() {

			// row 0
			cornerCoordinates[3][0][0][0] = 0;
			cornerCoordinates[4][0][0][0] = 0;
			cornerCoordinates[5][0][0][0] = 0;
			cornerCoordinates[6][0][0][0] = 0;
			// row 1
			cornerCoordinates[2][1][0][0] = 0;
			cornerCoordinates[6][1][0][0] = 0;
			// row 2
			cornerCoordinates[1][2][0][0] = 0;
			cornerCoordinates[6][2][0][0] = 0;
			// row 3
			cornerCoordinates[0][3][0][0] = 0;
			cornerCoordinates[0][3][1][0] = 0;
			cornerCoordinates[6][3][0][0] = 0;
			cornerCoordinates[6][3][1][0] = 0;
			// row 4
			cornerCoordinates[0][4][1][0] = 0;
			cornerCoordinates[5][4][1][0] = 0;
			// row 5
			cornerCoordinates[0][5][1][0] = 0;
			cornerCoordinates[4][5][1][0] = 0;
			// row 6
			cornerCoordinates[0][6][1][0] = 0;
			cornerCoordinates[1][6][1][0] = 0;
			cornerCoordinates[2][6][1][0] = 0;
			cornerCoordinates[3][6][1][0] = 0;
		}

		/**
		 * sets x-coordinate of unused edges to 0; edges with x-coordinate 0
		 * won't be initialized.
		 */
		private void filterUnusedEdges() {
			// row 0
			edgeCoordinates[3][0][0][0] = 0;
			edgeCoordinates[3][0][1][0] = 0;
			edgeCoordinates[3][0][2][0] = 0;

			edgeCoordinates[4][0][0][0] = 0;
			edgeCoordinates[4][0][1][0] = 0;
			edgeCoordinates[4][0][2][0] = 0;

			edgeCoordinates[5][0][0][0] = 0;
			edgeCoordinates[5][0][1][0] = 0;
			edgeCoordinates[5][0][2][0] = 0;

			edgeCoordinates[6][0][0][0] = 0;
			edgeCoordinates[6][0][1][0] = 0;
			edgeCoordinates[6][0][2][0] = 0;

			// row 1
			edgeCoordinates[2][1][0][0] = 0;
			edgeCoordinates[2][1][1][0] = 0;

			edgeCoordinates[6][1][0][0] = 0;
			edgeCoordinates[6][1][1][0] = 0;
			edgeCoordinates[6][1][2][0] = 0;

			// row 2
			edgeCoordinates[1][2][0][0] = 0;
			edgeCoordinates[1][2][1][0] = 0;

			edgeCoordinates[6][2][0][0] = 0;
			edgeCoordinates[6][2][1][0] = 0;
			edgeCoordinates[6][2][2][0] = 0;

			// row 3
			edgeCoordinates[0][3][0][0] = 0;
			edgeCoordinates[0][3][1][0] = 0;

			edgeCoordinates[6][3][0][0] = 0;
			edgeCoordinates[6][3][1][0] = 0;
			edgeCoordinates[6][3][2][0] = 0;

			// row 4
			edgeCoordinates[0][4][0][0] = 0;

			edgeCoordinates[5][4][1][0] = 0;
			edgeCoordinates[5][4][2][0] = 0;

			// row 5
			edgeCoordinates[0][5][0][0] = 0;

			edgeCoordinates[4][5][1][0] = 0;
			edgeCoordinates[4][5][2][0] = 0;

			// row 6
			edgeCoordinates[0][6][0][0] = 0;
			edgeCoordinates[0][6][2][0] = 0;

			edgeCoordinates[1][6][2][0] = 0;

			edgeCoordinates[2][6][2][0] = 0;

			edgeCoordinates[3][6][1][0] = 0;
			edgeCoordinates[3][6][2][0] = 0;

		}

		/**
		 * Inits the self.
		 *
		 * @param ownPlayerId
		 *            the own player id
		 * @param name
		 *            the name
		 * @param color
		 *            the color
		 */
		public void initSelf(int ownPlayerId, String name, enums.Color color) {
			playerIDtoViewPosition.put(ownPlayerId, 0);
			playerColors.put(0, color.getValue());
			selfName.setText(name);

		}

	}

}
