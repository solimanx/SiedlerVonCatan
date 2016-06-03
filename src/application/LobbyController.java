package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import network.client.controller.ViewController;

public class LobbyController {

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
	public void handleConnectButton(){
		String server = serverIPTextField.getText();
		int port = Integer.parseInt(portTextField.getText());
		viewController.flowController.networkController.connectToServer(server,port);
		System.out.println("handled");


	}

	public void setViewController(ViewController viewController){
		this.viewController = viewController;
	}
}
