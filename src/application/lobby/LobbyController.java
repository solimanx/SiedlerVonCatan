package application.lobby;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import audio.Soundeffects;
import enums.Color;
import enums.PlayerState;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import network.client.controller.ViewController;

import static sounds.Sound.*;

// TODO: Auto-generated Javadoc
public class LobbyController {

	private ViewController viewController;

	private DateTimeFormatter dateFormat;

	@FXML
	private ComboBox<String> serverComboBox;

	@FXML
	private ComboBox<String> portComboBox;

	@FXML
	private Button connectButton;

	@FXML
	private TextField chatInput;

	@FXML
	private TextArea messages;

	@FXML
	private Button startGameButton;

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

	@FXML
	private AnchorPane colorNameSelectPane;

	private ObservableList<TablePlayer> players = FXCollections.observableArrayList();

	private static Logger logger = LogManager.getLogger(LobbyController.class.getSimpleName());

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
		chatInput.setDisable(true);
		dateFormat = DateTimeFormatter.ofPattern("hh:mm:ss");

		// Debug only
		addServers();
		// Debug only
		addPorts();
	}

	/**
	 * Adds the servers.
	 */
	// Debug only
	private void addServers() {
		serverComboBox.getItems().add("localhost");
		serverComboBox.getItems().add("aruba.dbs.ifi.lmu.de");

		// First item that was added is the first item shown.
		serverComboBox.setValue(serverComboBox.getItems().get(0));

	}

	/**
	 * Adds the ports.
	 */
	// Debug only
	private void addPorts() {
		portComboBox.getItems().add("8080");
		portComboBox.getItems().add("10000");
		portComboBox.getItems().add("10001");

		// First item that was added is the first item shown.
		portComboBox.setValue(portComboBox.getItems().get(0));
	}

	/**
	 * Handle connect button.
	 */
	@FXML
	public void handleConnectButton() {
		String server = serverComboBox.getValue();
		int port = Integer.parseInt(portComboBox.getValue());
		viewController.getClientController().connectToServer(server, port);
//		colorNameSelectPane.setDisable(false);
		// Soundeffects.SELECT.play();

		playConnectSound();

	}

	/**
	 * Send chat message.
	 */
	@FXML
	public void sendChatMessage() {
		String message = chatInput.getText();
		chatInput.clear();
		viewController.getClientController().sendChatMessage(message);
		playNotificationSound();
	}

	/**
	 * Receive chat message.
	 *
	 * @param string
	 *            the string
	 */
	public void receiveChatMessage(String string) {
		messages.appendText(currentTime() + string + "\n");
		//Soundeffects.CHATRECEIVE.play();
		// playNotificationSound();
	}

	/**
	 * Enable chat.
	 */
	public void enableChat() {
		messages.appendText(currentTime() + "Connected to lobby chat.\n");
		chatInput.setDisable(false);
	}

	/**
	 * Disconnect.
	 */
	public void disconnect() {
		messages.appendText(currentTime() + "Disconnected.\n");
		chatInput.setDisable(true);
	}

	/**
	 * Current time.
	 *
	 * @return the string
	 */
	private String currentTime() {
		return "[" + LocalTime.now().format(dateFormat) + "] ";
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
	 * Sets the server color answer.
	 *
	 * @param server_response
	 *            the new server color answer
	 */
	public void setServerColorAnswer(String server_response) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				serverColorAnswer.setText(server_response);
			}

		});
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
			Soundeffects.SELECT.play();

			// playButtonSound();
		}

	}

	/**
	 * Handle ready button.
	 */
	@FXML
	private void handleReadyButton() {
		viewController.getClientController().sendReady();
		playReadySound();
	}

	// public void deletePlayer(ProtocolPlayer player){
	// // TODO deletePlayer Method!
	// }

	/**
	 * Update player.
	 *
	 * @param threadID
	 *            the thread ID
	 * @param name
	 *            the name
	 * @param color
	 *            the color
	 * @param status
	 *            the status
	 */
	public void updatePlayer(int threadID, String name, Color color, PlayerState status) {
		Boolean playerFound = false;
		for (TablePlayer playersEntry : players) {
			if (threadID == playersEntry.getPlayerId()) {
				playerFound = true;
				if (name != null)
					playersEntry.nameProperty().set(name);
				if (color != null)
					playersEntry.colorProperty().set(color.toString());
				playersEntry.statusProperty().set(status.toString());

			}
		}
		if (!playerFound) {
			players.add(new TablePlayer(threadID, color, name, status));
		}
	}

	public AnchorPane getColorNameSelectPane() {
		return colorNameSelectPane;
	}

	public void clearChat() {
		messages.clear();
	}

}
