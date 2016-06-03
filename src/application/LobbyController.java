package application;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import network.client.controller.ViewController;

public class LobbyController implements Initializable {

	ViewController viewController;

	DateTimeFormatter dateFormat;

	@FXML
	Button myButton;

	@FXML
	TextField serverIPTextField;

	@FXML
	TextField portTextField;

	@FXML
	Button connectButton;

	@FXML
	TextField chatInput;

	@FXML
	TextArea messages;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		chatInput.setDisable(true);
		dateFormat = DateTimeFormatter.ofPattern("hh:mm:ss");

	}

	@FXML
	public void handleConnectButton() {
		String server = serverIPTextField.getText();
		int port = Integer.parseInt(portTextField.getText());
		viewController.flowController.networkController.connectToServer(server, port);
		System.out.println("handled");

	}

	@FXML
	public void sendChatMessage() {
		String message = chatInput.getText();
		chatInput.clear();
		viewController.flowController.networkController.chatSendMessage(message);
	}

	public void receiveChatMessage(String string) {
		messages.appendText(currentTime() + string + "\n");

	}
	

	public void enableChat() {
		chatInput.setDisable(false);
		messages.appendText(currentTime() + "Connected to lobby chat.\n");
		//viewController.openChooseNameMenu();
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
