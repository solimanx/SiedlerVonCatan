package application.lobby;

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerProfileController implements Initializable {
    private static Logger logger = LogManager.getLogger(PlayerProfileController.class.getName());
    private ViewController viewController;

    @FXML
    private TextField playerName;

    @FXML
    private Button sendNameAndColor;

    @FXML
    private Button readyButton;

    @FXML
    private ChoiceBox<enums.Color> playerColor;

    @FXML
    private Label serverColorAnswer;

    public void setServerColorAnswer(String server_response) {
        serverColorAnswer.setText(server_response);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playerName.setPromptText("Name in Game (required)");
        playerColor.getItems().addAll(enums.Color.BLUE, enums.Color.ORANGE, enums.Color.RED, enums.Color.WHITE);
        readyButton.setDisable(true);
    }

    public void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }

    @FXML
    private void handleSendButton() {
        Color chosenColor = playerColor.getValue();
        String name = playerName.getText();
        serverColorAnswer.setText("");
        if (name.equals("")) {
            System.out.println("No name given.");
            logger.info("No name given");
        } else if (chosenColor == null) {
            System.out.println("No color selected.");
            logger.info("No color selected");
        } else {
            viewController.getClientController().sendPlayerProfile(name, chosenColor);
            // TODO receive confirmation from server that color isnt taken
            // FOR DEBUG ONLY ASSUME SERVER CONFIRMED
            System.out.println("Profile: " + name + " " + chosenColor);
            logger.debug("Profile", name, chosenColor);
            readyButton.setDisable(false);
        }
    }

    @FXML
    private void handleReadyButton() {
        viewController.getClientController().sendReady();
    }

    // @FXML
    // public void setServerColorText(String s) {
    // serverColorAnswer.setText(s);
    // }
}
