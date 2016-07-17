package view.devcardview;

import java.net.URL;
import java.util.ResourceBundle;

import enums.CardType;
import enums.ResourceType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import network.client.controller.ViewController;

/**
 * Controls the DevelopmentCard view when viewing player's deck.
 */
public class DevCardViewController implements Initializable {

	@FXML
	private ListView<String> devCardListView;

	@FXML
	private Button playCardButton;

	@FXML
	private Button cancelButton;

	@FXML
	private ComboBox<String> resourceA;

	@FXML
	private ComboBox<String> resourceB;

	@FXML
	private Button okButton;

	@FXML
	private Button debugButton;

	@FXML
	private ChoiceBox<CardType> debugChoice;

	private ViewController viewController;

	private ObservableList<String> devCardList = FXCollections.observableArrayList();

	private String devCardSelected;

	private Stage stage;

	private Stage monopolyStage;

	private Stage inventionStage;

	private ChoiceBox<enums.ResourceType> inventRChooser2;

	private ChoiceBox<enums.ResourceType> inventRChooser1;

	private Button inventButton;

	private ChoiceBox<ResourceType> monopolyRChooser;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.fxml.Initializable#initialize(java.net.URL,
	 * java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	/**
	 * Inits the deck view.
	 *
	 * @param viewController
	 *            the view controller
	 * @param stage
	 *            the stage
	 * @param devCards
	 *            the dev cards
	 */
	public void init(ViewController viewController, Stage stage, int[] devCards) {
		this.stage = stage;
		this.viewController = viewController;

		// DEBUG
		// debugChoice.setItems(FXCollections.observableArrayList(CardType.INVENTION,
		// CardType.STREET, CardType.KNIGHT, CardType.MONOPOLY));
		// END DEBUG

		devCardList.clear();
		devCardListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		devCardListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				devCardSelected = newValue;
				playCardButton.setDisable(false);

			}
		});

		for (int i = 0; i < devCards.length; i++) {
			for (int j = 0; j < devCards[i]; j++) {
				String string = "";
				switch (i) {
				case 0:
					string = CardType.KNIGHT.toString();
					break;
				case 1:
					string = CardType.VICTORYPOINT.toString();
					break;
				case 2:
					string = CardType.INVENTION.toString();
					break;
				case 3:
					string = CardType.MONOPOLY.toString();
					break;
				case 4:
					string = CardType.STREET.toString();
					break;
				}
				devCardList.add(string);
			}
		}
		devCardListView.setItems(devCardList);
	}

	/**
	 * Handle clicking the Cancel button.
	 *
	 * @param event
	 *            the event
	 */
	@FXML
	void handleCancel(ActionEvent event) {
		this.stage.close();
	}

	/**
	 * Handle clicking OK in the invention play menu.
	 *
	 * @param event
	 *            the event
	 */
	@FXML
	void handleInventionOK(ActionEvent event) {
		int[] resources = { 0, 0, 0, 0, 0 };
		switch (inventRChooser1.getSelectionModel().getSelectedItem()) {
		case WOOD:
			resources[0] += 1;
			break;
		case CLAY:
			resources[1] += 1;
			break;
		case SHEEP:
			resources[3] += 1;
			break;
		case CORN:
			resources[4] += 1;
			break;
		case ORE:
			resources[2] += 1;
			break;
		default:
			throw new IllegalArgumentException("Resource doesn't exist");
		}
		switch (inventRChooser2.getSelectionModel().getSelectedItem()) {
		case WOOD:
			resources[0] += 1;
			break;
		case CLAY:
			resources[1] += 1;
			break;
		case SHEEP:
			resources[3] += 1;
			break;
		case CORN:
			resources[4] += 1;
			break;
		case ORE:
			resources[2] += 1;
			break;
		default:
			throw new IllegalArgumentException("Resource doesn't exist");
		}
		viewController.getClientController().playInventionCard(resources);
		inventionStage.close();
		this.stage.close();
	}

	/**
	 * Handle clicking OK in the monopoly play menu.
	 *
	 * @param event
	 *            the event
	 */
	@FXML
	void handleMonopolyOK(ActionEvent event) {

		viewController.getClientController().playMonopolyCard(monopolyRChooser.getSelectionModel().getSelectedItem());
		monopolyStage.close();
		this.stage.close();
	}

	/**
	 * Handle clicking play card button.
	 *
	 * @param event
	 *            the event
	 */
	@FXML
	void handlePlayCardButton(ActionEvent event) {
		if (devCardSelected.equals(CardType.KNIGHT.toString())) {
			stage.close();
			viewController.getGameViewController().setKnight(true);
			viewController.getGameViewController().setMoveRobberState();
		} else if (devCardSelected.equals(CardType.STREET.toString())) {
			stage.close();
			viewController.getGameViewController().setIsStreetDev(true);
		} else if (devCardSelected.equals(CardType.MONOPOLY.toString())) {

			monopolyRChooser = new ChoiceBox<enums.ResourceType>();
			monopolyRChooser.getItems().addAll(ResourceType.WOOD, ResourceType.CLAY, ResourceType.SHEEP,
					ResourceType.CORN, ResourceType.ORE);
			inventButton = new Button("OK");
			inventButton.setOnAction(e -> {
				handleMonopolyOK(e);
			});
			Label label = new Label("Choose Resource ...");
			VBox monopolyBox = new VBox(label, monopolyRChooser, inventButton);
			monopolyBox.setPrefWidth(200);
			monopolyBox.setPrefHeight(200);
			monopolyBox.setPadding(new Insets(20, 20, 20, 20));
			monopolyBox.setSpacing(20);
			Scene scene = new Scene(monopolyBox);
			scene.getStylesheets().add("/textures/" + viewController.getGameViewController().theme + ".css");
			monopolyStage = new Stage();
			monopolyStage.setScene(scene);
			monopolyStage.initModality(Modality.WINDOW_MODAL);
			monopolyStage.initOwner(this.stage);
			monopolyStage.show();
		} else if (devCardSelected.equals(CardType.INVENTION.toString())) {
			inventRChooser1 = new ChoiceBox<enums.ResourceType>();
			inventRChooser1.getItems().addAll(ResourceType.WOOD, ResourceType.CLAY, ResourceType.SHEEP,
					ResourceType.CORN, ResourceType.ORE);
			inventRChooser2 = new ChoiceBox<enums.ResourceType>();
			inventRChooser2.getItems().addAll(ResourceType.WOOD, ResourceType.CLAY, ResourceType.SHEEP,
					ResourceType.CORN, ResourceType.ORE);
			inventButton = new Button("OK");
			inventButton.setOnAction(e -> {
				handleInventionOK(e);
			});
			Label label = new Label("Choose Resources ...");
			VBox inventionBox = new VBox(label, inventRChooser1, inventRChooser2, inventButton);
			inventionBox.setPrefWidth(200);
			inventionBox.setPrefHeight(250);
			inventionBox.setPadding(new Insets(20, 20, 20, 20));
			inventionBox.setSpacing(20);
			Scene scene = new Scene(inventionBox);
			scene.getStylesheets().add("/textures/" + viewController.getGameViewController().theme + ".css");
			inventionStage = new Stage();
			inventionStage.setScene(scene);
			inventionStage.initModality(Modality.WINDOW_MODAL);
			inventionStage.initOwner(this.stage);
			inventionStage.show();

		} else if (devCardSelected.equals(CardType.VICTORYPOINT.toString())) {
			stage.close();
		}

	}

	/**
	 * Handle clicking debug button.
	 *
	 * @param event
	 *            the event
	 */
	@FXML
	void handleDebugButton(ActionEvent event) {
		devCardSelected = debugChoice.getSelectionModel().getSelectedItem().toString();
		handlePlayCardButton(event);
	}

}
