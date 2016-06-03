package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LobbyController {
	@FXML
	Button myButton;

	@FXML
	TextField serverIPTextField;

	@FXML
	TextField portTextField;

	@FXML
	Button connectButton;

	@FXML
	public void handleOne(){
		myButton.setText("Clicked");
	}

	@FXML
	public void handleTOne(){
		myButton.setText("AnotherClass was here!");

	}

	@FXML
	public void handleConnectButton(){
		String server = serverIPTextField.getText();
		int port = Integer.parseInt(portTextField.getText());
		//viewController.flowController.networkController.connectToServer(server,port);


	}
}
