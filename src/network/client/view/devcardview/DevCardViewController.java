package network.client.view.devcardview;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

public class DevCardViewController implements Initializable {
	
    @FXML
    private ListView<String> devCardList;

    @FXML
    private Button playCardButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<String> resourceA;

    @FXML
    private ComboBox<String> resourceB;

    @FXML
    private Button okButton;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}
	
    @FXML
    void handleCancel(ActionEvent event) {

    }

    @FXML
    void handleOK(ActionEvent event) {

    }

    @FXML
    void handlePlayCardButton(ActionEvent event) {

    }


}
