package application.lobby;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import network.client.controller.ViewController;

public class LobbyController implements Initializable {

	private ViewController viewController;

	private DateTimeFormatter dateFormat;

	@FXML
	private Button myButton;

	@FXML
	private TextField serverIPTextField;

	@FXML
	private TextField portTextField;

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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		chatInput.setDisable(true);
		dateFormat = DateTimeFormatter.ofPattern("hh:mm:ss");

	}

	@FXML
	public void handleConnectButton() {
		String server = serverIPTextField.getText();
		int port = Integer.parseInt(portTextField.getText());
		viewController.getFlowController().getNetworkController().connectToServer(server, port);
	}

	@FXML
	public void startGame() {
		try {
			viewController.startChooseView();
		} catch (IOException e) {
			// TODO Logging
			e.printStackTrace();
		}
	}

	@FXML
	public void sendChatMessage() {
		String message = chatInput.getText();
		chatInput.clear();
		viewController.getFlowController().getNetworkController().chatSendMessage(message);
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
