package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import network.client.controller.ViewController;

public class NameSelectDialogController implements Initializable {
	
	private ViewController viewController;
	
	@FXML
	private TextField playerName;
	
	@FXML
	private ChoiceBox<enums.Color> playerColor;
	
	@FXML
	private Button startGame;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}
	
	private void handleStartButton(){
			//Color playerColor = colorSelect.getValue();
			//String name = playerName.getText();
			// viewController.flowController.startGame();
		}
	}
	

