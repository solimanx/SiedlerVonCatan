package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class TestController {
	@FXML
	Button myButton;

	@FXML
	public void handleOne(){
		myButton.setText("Clicked");
	}

	@FXML
	public void handleTOne(){
		myButton.setText("AnotherClass was here!");

	}
}
