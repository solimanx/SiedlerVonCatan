package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import network.client.controller.ViewController;

public class LobbyController implements Initializable{

	ViewController viewController;

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

	}

	@FXML
	public void handleConnectButton(){
		String server = serverIPTextField.getText();
		int port = Integer.parseInt(portTextField.getText());
		viewController.flowController.networkController.connectToServer(server,port);
		System.out.println("handled");


	}

	public void setViewController(ViewController viewController){
		this.viewController = viewController;
	}

	public void enableChat() {
		chatInput.setDisable(false);
	}

	@FXML
	public void sendChatMessage() {
		String message = chatInput.getText();
		chatInput.clear();
		viewController.flowController.networkController.chatSendMessage(message);
	}

	@FXML
	public void receiveChatMessage(String string) {
		messages.appendText(string + "\n");

	}


}
