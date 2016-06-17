package network.client.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import enums.CornerStatus;
import enums.HarbourStatus;
import enums.PlayerState;
import enums.ResourceType;
import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
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
import network.client.view.robberview.RobberViewController;
import network.client.view.tradeview.TradeViewController;

public class GameViewController implements Initializable {

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
	private Button rollDiceButton;

	@FXML
	private Button startTradingButton;

	@FXML
	private Button endTurnButton;

	@FXML
	private Button buyCardButton;
	
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
	private Label selfName;

	@FXML
	private TextArea serverLog;

	@FXML
	private TextField diceResult;

	// DEBUG
	@FXML
	private Button openRobberView;

	private ViewController viewController;

	// jeweils die letzte Dimension des Arrays zur Speicherung der Koordinaten;
	// für Edge 2 Koordinaten (4 Punkte), weil Anfangs- und Endpunkt
	public double[][][] fieldCoordinates = new double[7][7][2]; // [6][6][2]
	public double[][][][] edgeCoordinates = new double[7][7][3][4]; // [6][6][3][4]
	public double[][][][] cornerCoordinates = new double[7][7][2][2]; // [6][6][2][2]
	private Polygon[][] fields = new Polygon[7][7];
	private Polygon[][][] villages = new Polygon[7][7][2];
	private Polygon[][][] cities = new Polygon[7][7][2];
	public Line[][][] streets = new Line[7][7][3];
	public Circle bandit;

	// Constant values for calculations
	public static double radius = 50.0;
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

	private int[] selfResources;

	private SimpleStringProperty response;

	private TradeViewController tradeViewController;

	private Stage tradeStage;

	private FadeTransition woodTransition;

	private FadeTransition clayTransition;

	private FadeTransition woolTransition;

	private FadeTransition cornTransition;

	private FadeTransition oreTransition;

	public TradeViewController getTradeViewController() {
		return tradeViewController;
	}

	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javafx.fxml.Initializable#initialize(java.net.URL,
	 * java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initFieldColors();
		response = new SimpleStringProperty("Server responses will appear here");
		factory = new ViewBoardFactory();

	}

	/**
	 * draws and shows the board Pane with game board
	 *
	 * @param stage
	 */
	public void startScene(Stage stage) {
		this.gameStage = stage;
		board.getChildren().add(factory.getViewBoard(stage));
		board.toBack();
		viewController.getClientController().initializeGUI();

		shadow = new DropShadow();
		shadow.setRadius(4);
		shadow.setColor(Color.SLATEGRAY);

		FXMLLoader loader = new FXMLLoader();
		Pane root;
		try {
			root = loader.load(getClass().getResource("/network/client/view/tradeview/tradeView.fxml").openStream());
			tradeViewController = (TradeViewController) loader.getController();
			Scene scene = new Scene(root);
			tradeStage = new Stage();
			tradeStage.setScene(scene);

			tradeViewController.init(selfResources, viewController);

			tradeStage.initModality(Modality.WINDOW_MODAL);
			tradeStage.initOwner(gameStage);
			tradeStage.setOnCloseRequest(e -> {
				tradeStage.hide();
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
				selfWoodText.setFill(Color.BLACK);
			}
		});

		clayTransition.onFinishedProperty().set(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				selfClayText.setFill(Color.BLACK);
			}
		});

		woolTransition.onFinishedProperty().set(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				selfSheepText.setFill(Color.BLACK);
			}
		});

		cornTransition.onFinishedProperty().set(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				selfCornText.setFill(Color.BLACK);
			}
		});

		oreTransition.onFinishedProperty().set(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				selfOreText.setFill(Color.BLACK);
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
					serverLog.appendText("\n" + newValue);
				}
			}
		});

	}

	/**
	 * @param text
	 * @return
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
				text.setFill(Color.BLACK);
				// TODO Auto-generated method stub
			}
		});
		return transition;
	}

	public String getPlayerNames(Integer playerID) {
		return viewController.getClientController().getGameLogic().getBoard().getPlayer(playerID).getName();
	}

	/**
	 * @param modelID
	 * @param playerName
	 * @param playerColor
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
			if (playerColor != enums.Color.WHITE) {
				playerNameOne.setTextFill(playerColor.getValue());
			}
			playerNames.put(1, playerName);
			break;
		case 2:
			playerNameTwo.setText(playerName);
			if (playerColor != enums.Color.WHITE) {
				playerNameTwo.setTextFill(playerColor.getValue());
			}
			playerNames.put(1, playerName);
			break;
		case 3:
			playerNameThree.setText(playerName);
			if (playerColor != enums.Color.WHITE) {
				playerNameThree.setTextFill(playerColor.getValue());
			}
			playerNames.put(1, playerName);
			break;
		case 4:
			playerNameFour.setText(playerName);
			if (playerColor != enums.Color.WHITE) {
				playerNameFour.setTextFill(playerColor.getValue());
			}
			playerNames.put(1, playerName);
			break;
		}

	}

	/**
	 * Auxiliary method filling field color hashmap
	 */
	private void initFieldColors() {
		fieldColors.put(ResourceType.CLAY, Color.web("#A1887F"));
		fieldColors.put(ResourceType.CORN, Color.web("#FFEE58"));
		fieldColors.put(ResourceType.NOTHING, Color.web("#FAFAFA"));
		fieldColors.put(ResourceType.ORE, Color.web("#9E9E9E"));
		fieldColors.put(ResourceType.SHEEP, Color.web("#9CCC65"));
		fieldColors.put(ResourceType.WOOD, Color.web("#26A69A"));
		fieldColors.put(ResourceType.SEA, Color.web("#81D4FA"));

		ImagePattern woodPattern = new ImagePattern(new Image("/textures/wood.jpg"));
		ImagePattern clayPattern = new ImagePattern(new Image("/textures/clay.jpg"));
		ImagePattern woolPattern = new ImagePattern(new Image("/textures/sheep.jpg"));
		ImagePattern cornPattern = new ImagePattern(new Image("/textures/corn.jpg"));
		ImagePattern orePattern = new ImagePattern(new Image("/textures/ore.jpg"));
		ImagePattern desertPattern = new ImagePattern(new Image("/textures/desert.jpg"));
		ImagePattern seaPattern = new ImagePattern(new Image("/textures/sea.jpg"));

		imagePatterns.put(ResourceType.WOOD, woodPattern);
		imagePatterns.put(ResourceType.CLAY, clayPattern);
		imagePatterns.put(ResourceType.SHEEP, woolPattern);
		imagePatterns.put(ResourceType.CORN, cornPattern);
		imagePatterns.put(ResourceType.ORE, orePattern);
		imagePatterns.put(ResourceType.NOTHING, desertPattern);
		imagePatterns.put(ResourceType.SEA, seaPattern);

		ImagePattern woodHarbour = new ImagePattern(new Image("/textures/woodHarbour.jpg"));
		ImagePattern clayHarbour = new ImagePattern(new Image("/textures/clayHarbour.jpg"));
		ImagePattern woolHarbour = new ImagePattern(new Image("/textures/sheepHarbour.jpg"));
		ImagePattern cornHarbour = new ImagePattern(new Image("/textures/cornHarbour.jpg"));
		ImagePattern oreHarbour = new ImagePattern(new Image("/textures/oreHarbour.jpg"));
		ImagePattern genericHarbour = new ImagePattern(new Image("/textures/genericHarbour.jpg"));

		harbourImages.put(HarbourStatus.WOOD, woodHarbour);
		harbourImages.put(HarbourStatus.CLAY, clayHarbour);
		harbourImages.put(HarbourStatus.SHEEP, woolHarbour);
		harbourImages.put(HarbourStatus.CORN, cornHarbour);
		harbourImages.put(HarbourStatus.ORE, oreHarbour);
		harbourImages.put(HarbourStatus.THREE_TO_ONE, genericHarbour);

	}

	/**
	 * @param event
	 */
	@FXML
	void handleEndTurnButton(ActionEvent event) {
		viewController.getClientController().endTurn();
	}

	/**
	 * @param event
	 */
	@FXML
	void handleRollDiceButton(ActionEvent event) {
		viewController.getClientController().diceRollRequest();
	}

	/**
	 * @param event
	 * @throws IOException
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
		tradeStage.show();
	}

	@FXML
	void handleBuyCardButton(ActionEvent event) throws IOException{
		
	}
	
	
	/**
	 * @param event
	 */
	@FXML
	void sendMessage(ActionEvent event) {
		String message = messageInput.getText();
		messageInput.clear();
		viewController.getClientController().sendChatMessage(message);
	}

	/**
	 * @param villageCoordinates
	 */
	public void villageClick(int[] villageCoordinates) {
		Polygon village = villages[villageCoordinates[0]][villageCoordinates[1]][villageCoordinates[2]];
		// DEBUG
		// if(village.getFill().equals(playerColors.get(1))){
		// viewController.getClientController().requestBuildCity(villageCoordinates[0],
		// villageCoordinates[1],
		// villageCoordinates[2]);
		// }
		if (selfState == PlayerState.TRADING_OR_BUILDING || selfState == PlayerState.BUILDING_VILLAGE)
			viewController.getClientController().requestBuildVillage(villageCoordinates[0], villageCoordinates[1],
					villageCoordinates[2]);

	}

	/**
	 * @param streetCoordinates
	 */
	public void streetClick(int[] streetCoord) {
		if (selfState == PlayerState.TRADING_OR_BUILDING || selfState == PlayerState.BUILDING_STREET)
			viewController.getClientController().requestBuildStreet(streetCoord[0], streetCoord[1], streetCoord[2]);
	}

	/**
	 * @param fieldCoordinates
	 */
	public void fieldClick(int[] fieldCoordinates) {
		if (selfState == PlayerState.MOVE_ROBBER) {
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
				if (string.equals(player2)) {
					resultID = 2;
				} else if (string.equals(player3)) {
					resultID = 3;
				} else if (string.equals(player4)) {
					resultID = 4;
				}
				viewController.getClientController().requestSetBandit(fieldCoordinates[0] - 3, fieldCoordinates[1] - 3,
						viewPositiontoPlayerID.get(resultID));
			}
		}

	}

	/**
	 * @param line
	 */
	public void receiveChatMessage(String line) {
		messages.appendText(line + "\n");
	}

	/**
	 * sets Street on coordinates u,v,dir to Player Color of Player with ID
	 * modelID
	 *
	 * @param u
	 *            axial coordinate (e.g. -3)
	 * @param v
	 *            axial coordinate (e.g. -3)
	 * @param dir
	 *            coordinate (e.g. 1 | 2 | 0)
	 * @param modelID
	 */
	public void setStreet(int u, int v, int dir, int modelID) {
		Line street = streets[u + 3][v + 3][dir];
		street.setOpacity(1.0);
		street.setStroke(playerColors.get(modelID));
		street.getStyleClass().remove("street");
		street.setEffect(shadow);
	}

	/**
	 * @param u
	 * @param v
	 */
	public void setBandit(int u, int v) {
		bandit.setCenterX(fieldCoordinates[u + 3][v + 3][0]);
		bandit.setCenterY(fieldCoordinates[u + 3][v + 3][1]);
		bandit.setOpacity(1.0);

	}

	/**
	 * @param u
	 * @param v
	 * @param dir
	 * @param buildType
	 * @param modelID
	 */
	public void setCorner(int u, int v, int dir, CornerStatus buildType, int modelID) {
		if (buildType == enums.CornerStatus.VILLAGE) {
			setVillage(u, v, dir, playerColors.get(modelID));
		} else {
			setCity(u, v, dir, playerColors.get(modelID));
		}
	}

	/**
	 * @param u
	 * @param v
	 * @param dir
	 * @param playerColor
	 */
	public void setVillage(int u, int v, int dir, Color playerColor) {
		Polygon village = villages[u + 3][v + 3][dir];
		village.setFill(playerColor);
		village.setOpacity(1.0);
		village.setEffect(shadow);
		village.getStyleClass().remove("village");
	}

	/**
	 * @param u
	 * @param v
	 * @param resourceType
	 * @param diceIndex
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
	 *
	 * @param u
	 * @param v
	 * @param harbourType
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
			enums.HarbourStatus hstate = viewController.getClientController().getGameLogic().getBoard()
					.getCornerAt(corner1[0], corner1[1], corner1[2]).getHarbourStatus();
			circle.setFill(harbourImages.get(hstate));
			circle.toFront();

			board.getChildren().addAll(triangle, circle);
		}
	}

	/**
	 * sets the amount of recource cards of self and other players
	 *
	 * @param modelID
	 * @param resources
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
		default:
			break;
		}
	}

	/**
	 * shows result of dice roll on game view
	 *
	 * @param result
	 */
	public void setDiceRollResult(int result) {
		diceResult.clear();
		diceResult.setText("" + result);
	}

	/**
	 * sets the victory points in the game view to corresponding player
	 *
	 * @param modelID
	 * @param victoryPoints
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
	 * @param modelID
	 * @param state
	 */
	public void setPlayerStatus(int modelID, PlayerState state) {
		switch (playerIDtoViewPosition.get(modelID)) {
		case 1:
			playerStatusOne.setText(state.toString());
			selfState = state;
			switch (state) {
			case DICEROLLING:
				rollDiceButton.setDisable(false);
				break;
			case DISPENSE_CARDS_ROBBER_LOSS:
				setRobberLossState();
				break;
			case MOVE_ROBBER:
				setMoveRobberState();
				break;
			case WAITING:
				rollDiceButton.setDisable(true);
				endTurnButton.setDisable(true);
				break;
			default:
				rollDiceButton.setDisable(false);
				endTurnButton.setDisable(false);
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

	private void setMoveRobberState() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Move the Robber");
		alert.setHeaderText("You can move the robber and steal from adjoining players!");
		alert.setContentText("Click on any field to move the robber on the field.");
		alert.initOwner(gameStage);
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.showAndWait();

	}

	/**
	 * Auxiliary methode showing Robber Loss window
	 */
	@FXML
	private void setRobberLossState() {
		FXMLLoader loader = new FXMLLoader();
		Parent root;
		try {
			root = loader
					.load(getClass().getResource("/network/client/view/robberview/GiveResources.fxml").openStream());
			Scene scene = new Scene(root);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param result
	 */
	public void robberLoss(int[] result) {
		viewController.getClientController().robberLoss(result);
	}

	public void setServerResponse(String response) {
		this.response.set(response);
	}

	/**
	 * @param u
	 * @param v
	 * @param dir
	 * @param playerColor
	 */
	public void setCity(int u, int v, int dir, Color playerColor) {
		Polygon city = cities[u + 3][v + 3][dir];
		city.setFill(playerColor);
		city.setOpacity(1.0);
		city.setEffect(shadow);
		city.setStroke(Color.BLACK);
	}
	
	public void showVictory(int playerID){
		//FXMLLoader loader = new FXMLLoader();
		//Parent root;
		//try {
		//	root = loader.load(getClass().getResource().openStream());
		//	Scene scene = new Scene(root);
		//	Stage victoryStage = new Stage();
		//    victoryStage.setScene(scene);
		//    victoryStage.initModality(Modality.APPLICATION_MODAL);
		//	victoryStage.initOwner(gameStage);
		//	victoryStage.show();
		    
		//} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		
		//playerIDtoViewPosition(playerID); // 1,2,3,4 als viewPosition, 1 == self
		// new Stage
	}

	/**
	 * Inner Class for constructing the board. Instantiated at initial phase of
	 * Game
	 *
	 * @author mattmoos
	 *
	 */
	private class ViewBoardFactory {
		private Pane boardPane;
		private List<Shape> streetFigures = new LinkedList<Shape>();
		private List<Shape> fieldFigures = new LinkedList<Shape>();
		private List<Shape> villageFigures = new LinkedList<Shape>();
		private List<Shape> cityFigures = new LinkedList<Shape>();

		public Pane getViewBoard(Stage stage) {
			boardPane = new Pane();
			boardCenter[0] = stage.getWidth() / 2;
			boardCenter[1] = stage.getHeight() / 2 - 40;

			calculateFieldCenters(boardCenter);
			calculateCornerCenters();
			calculateEdgeCorners();
			initBoard();

			boardPane.getChildren().addAll(0, villageFigures);
			boardPane.getChildren().addAll(0, streetFigures);
			boardPane.getChildren().addAll(0, cityFigures);
			boardPane.getChildren().addAll(0, fieldFigures);

			return boardPane;
		}

		private void initBoard() {

			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 7; j++) {
					if (fieldCoordinates[i][j][0] > 0) {
						Polygon hexagon = drawHexagon(
								createHexagon(fieldCoordinates[i][j][0], fieldCoordinates[i][j][1]));
						hexagon.setVisible(true);
						int[] resourceCoordinates = { i, j };
						hexagon.setOnMouseClicked(e -> {
							fieldClick(resourceCoordinates);
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
								Polygon village = drawVillage(cornerCoordinates[i][j][k]);
								village.setOpacity(0);
								village.getStyleClass().add("village");
								villages[i][j][k] = village;

								int[] villageCoordinates = { i, j, k };

								village.setOnMouseClicked(e -> {
									villageClick(villageCoordinates);
								});
								village.toFront();
								villageFigures.add(village);

								Polygon city = drawCity(cornerCoordinates[i][j][k]);
								city.setOpacity(0);
								cities[i][j][k] = city;
								city.toFront();
								cityFigures.add(city);

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

		// /**
		// * TODO perhaps unnecessary
		// */
		// public void initCorners() {
		// for (int i = 0; i < fieldCoordinates.length; i++) {
		// for (int j = 0; j < fieldCoordinates.length; j++) {
		// corners[i][j][0] = createVillage(i, j, 0);
		// }
		// }
		//
		// }

		/**
		 * @param centerCoordinates
		 * @return double array of coordinates of 6 Points (12 double values)
		 *         calculates coordinates of Hexagon from given center
		 *         coordinates
		 */
		public double[] createHexagon(double x, double y) {
			double[] points = new double[12];
			int j = 1;
			for (int i = 0; i < points.length; i = i + 2) {
				points[i] = (double) (x + radius * Math.sin(j * rad60));
				points[i + 1] = (double) (y + radius * Math.cos(j * rad60));
				j++;
			}
			return points;
		}

		private Polygon createVillage(int i, int j, int k) {
			Polygon village;
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
		 * draws a Circle with diceIndex
		 *
		 * @param u
		 * @param v
		 * @param diceIndex
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
		 * takes 6 coordinate pairs (x,y) and draws a Polygon hexagon
		 *
		 * @param points
		 * @return Polygon
		 */
		public Polygon drawHexagon(double[] points) {
			Polygon hexagon = new Polygon(points);
			hexagon.setFill(Color.LIGHTSKYBLUE);
			hexagon.setStroke(Color.LIGHTGRAY);
			return hexagon;
		}

		/**
		 * takes pair of coordinates as center point and draws a village
		 *
		 * @param center
		 * @return Polygon
		 */
		public Polygon drawVillage(double[] center) {
			Polygon village = new Polygon(center[0], center[1] - 18, center[0] + 10, center[1] - 10, center[0] + 10,
					center[1] + 10, center[0] - 10, center[1] + 10, center[0] - 10, center[1] - 10);

			return village;
		}

		/**
		 * Auxiliary method drawing cities
		 *
		 * @param center
		 * @return Polygon city
		 */
		private Polygon drawCity(double[] center) {
			Polygon city = new Polygon(center[0] + 5, center[1] - 10, center[0] + 5, center[1] - 20, center[0] + 10,
					center[1] - 20, center[0] + 10, center[1] + 10, center[0] - 10, center[1] + 10, center[0] - 10,
					center[1] - 20, center[0] - 5, center[1] - 20, center[0] - 5, center[1] - 10);
			return city;
		}

		public Line drawStreet(double[] coordinates) {
			Line street = new Line(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
			street.setStrokeWidth(6.0);

			return street;
		}

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
		 * are sea
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
		 * won't be initialized
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

		public void initSelf(int ownPlayerId, String name, enums.Color color) {
			playerIDtoViewPosition.put(ownPlayerId, 0);
			playerColors.put(0, color.getValue());
			selfName.setText(name);

		}

	}
}
