package application.lobby;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import network.client.controller.ViewController;

public class LobbyController implements Initializable {

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
	private Button startButton;

	// NameSelectDialog
	@FXML
	private TextField playerName;

	@FXML
	private ChoiceBox<enums.Color> colorSelect;

	@FXML
	private Button startGameButton;

	private static Logger logger = LogManager.getLogger(LobbyController.class.getSimpleName());

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		chatInput.setDisable(true);
		dateFormat = DateTimeFormatter.ofPattern("hh:mm:ss");

		// Debug only
		addServers();
		// Debug only
		addPorts();

	}

	// Debug only
	private void addServers() {
		serverComboBox.getItems().add("localhost");
		serverComboBox.getItems().add("aruba.dbs.ifi.lmu.de");

		// First item that was added is the first item shown.
		serverComboBox.setValue(serverComboBox.getItems().get(0));

	}

	// Debug only
	private void addPorts() {
		portComboBox.getItems().add("8080");
		portComboBox.getItems().add("10000");
		portComboBox.getItems().add("10001");

		// First item that was added is the first item shown.
		portComboBox.setValue(portComboBox.getItems().get(0));
	}

	@FXML
	public void handleConnectButton() {
		String server = serverComboBox.getValue();
		int port = Integer.parseInt(portComboBox.getValue());
		viewController.getClientController().connectToServer(server, port);
	}

	@FXML
	public void startGame() {
		try {
			viewController.startChooseView();
		} catch (IOException e) {
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}
	}

	@FXML
	public void sendChatMessage() {
		String message = chatInput.getText();
		chatInput.clear();
		viewController.getClientController().sendChatMessage(message);
	}

	public void receiveChatMessage(String string) {
		messages.appendText(currentTime() + string + "\n");
	}

	public void enableChat() {
		messages.appendText(currentTime() + "Connected to lobby chat.\n");
		chatInput.setDisable(false);
		startButton.setDisable(false);
	}

	public void disconnect() {
		messages.appendText(currentTime() + "Disconnected.\n");
		chatInput.setDisable(true);
	}

	private String currentTime() {
		return "[" + LocalTime.now().format(dateFormat) + "] ";
	}

	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}

}
