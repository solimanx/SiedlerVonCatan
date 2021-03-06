package view.lobbyView;

import enums.Color;
import enums.PlayerState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.unit.TablePlayer;
import network.client.controller.ViewController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// TODO: Auto-generated Javadoc
public class PlayerProfileController {
	private static Logger logger = LogManager.getLogger(PlayerProfileController.class.getSimpleName());
	private ViewController viewController;

	@FXML
	private TextField playerName;

	@FXML
	private Button sendNameAndColor;

	@FXML
	private Button readyButton;

	@FXML
	private ChoiceBox<enums.Color> playerColor;

	@FXML
	private Label serverColorAnswer;

	@FXML
	private TableView<TablePlayer> playerTable;

	@FXML
	private TableColumn<TablePlayer, Integer> idColumn;

	@FXML
	private TableColumn<TablePlayer, String> nameColumn;

	@FXML
	private TableColumn<TablePlayer, String> colorColumn;

	@FXML
	private TableColumn<TablePlayer, String> statusColumn;

	private ObservableList<TablePlayer> players = FXCollections.observableArrayList();

	/**
	 * Sets the server color answer.
	 *
	 * @param server_response the new server color answer
	 */
	public void setServerColorAnswer(String server_response) {
		serverColorAnswer.setText(server_response);
	}

	/**
	 * Initialize.
	 */
	@FXML
	public void initialize() {
		playerName.setPromptText("Name in Game (required)");
		playerColor.getItems().addAll(enums.Color.BLUE, enums.Color.ORANGE, enums.Color.RED, enums.Color.WHITE);
		readyButton.setDisable(true);
		idColumn.setCellValueFactory(cellData -> cellData.getValue().playerIdProperty().asObject());
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		colorColumn.setCellValueFactory(cellData -> cellData.getValue().colorProperty());
		statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
		playerTable.setItems(players);
	}

	/**
	 * Sets the view controller.
	 *
	 * @param viewController the new view controller
	 */
	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}

	/**
	 * Handle send button.
	 */
	@FXML
	private void handleSendButton() {
		Color chosenColor = playerColor.getValue();
		String name = playerName.getText();
		serverColorAnswer.setText("");
		if (name.equals("")) {
			logger.debug("No name given");
		} else if (chosenColor == null) {
			logger.debug("No color selected");
		} else {
			viewController.getClientController().sendPlayerProfile(name, chosenColor);
			// TODO receive confirmation from server that color isnt taken
			// FOR DEBUG ONLY ASSUME SERVER CONFIRMED
			logger.debug("Profile" + name + chosenColor);
			readyButton.setDisable(false);
		}
	}

	/**
	 * Handle ready button.
	 */
	@FXML
	private void handleReadyButton() {
		viewController.getClientController().sendReady();
	}

	// public void deletePlayer(ProtocolPlayer player){
	// // TODO deletePlayer Method!
	// }

	/**
	 * Update player.
	 *
	 * @param threadID the thread ID
	 * @param name the name
	 * @param color the color
	 * @param status the status
	 */
	public void updatePlayer(int threadID, String name, Color color, PlayerState status) {
		Boolean playerFound = false;
		for (TablePlayer playersEntry : players) {
			if (threadID == playersEntry.getPlayerId()) {
				playerFound = true;
				playersEntry.nameProperty().set(name);
				playersEntry.colorProperty().set(color.toString());
				playersEntry.statusProperty().set(status.toString());

			}
		}
		if (!playerFound){
			players.add(new TablePlayer(threadID, color, name, status));
		}
	}

	// @FXML
	// public void setServerColorText(String s) {
	// serverColorAnswer.setText(s);
	// }
}
