package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import enums.Color;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import network.client.controller.ViewController;

public class NameSelectDialogController implements Initializable {

	private ViewController viewController;

	@FXML
	private TextField playerName;

	@FXML
	private Button startGame;

	@FXML
	private ChoiceBox<enums.Color> playerColor;

	@FXML
	private Label serverColorAnswer;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		playerName.setPromptText("Name in Game (required)");
		playerColor.getItems().addAll(enums.Color.BLUE, enums.Color.ORANGE, enums.Color.RED, enums.Color.WHITE);
	}

	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}

	@FXML
	private void handleStartButton() {
		Color chosenColor = playerColor.getValue();
		String name = playerName.getText();
		System.out.println("chosen: " + name + " " + chosenColor);
		// viewController.flowController.startGame();
	}
	
	@FXML
	public void setServerColorText(String s){
		serverColorAnswer.setText(s);
	}
}
