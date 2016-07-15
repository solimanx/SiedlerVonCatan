package network.client.view.helpview;


import javafx.event.ActionEvent;


import javafx.fxml.FXML;


import javafx.scene.control.Button;
import javafx.scene.control.Tab;


import static sounds.Sound.playHelpButtonSound;

/**
 * Created by Amina on 13.07.2016.
 */

public class HelpViewController {
    @FXML
    private Tab instructionsTab;
    @FXML
    private Tab startGameTab;
    @FXML
    private Tab gameBoardTab;
    @FXML
    private Tab resourcesTab;
    @FXML
    private Tab buildTab;
    @FXML
    private Tab tradeTab;
    @FXML
    private Tab robberTab;
    @FXML
    private Tab developmentCardsTab;
    @FXML
    private Tab tipsTab;
    @FXML
    private Button btn2;
    @FXML
    private Button btnOpenNewWindow;

    /**
     * Handle Instruction button in Help View.
     *
     * @param event the event
     */
    @FXML
    void handleInstructionsTab(ActionEvent event) {
        playHelpButtonSound();

    }

    /**
     * Handle Start Game button in Help View.
     *
     * @param event the event
     */
    @FXML
    void handleStartGameTab(ActionEvent event) {
        playHelpButtonSound();
    }

    /**
     * Handle Game Board button in Help View.
     *
     * @param event the event
     */
    @FXML
    void handleGameBoardTab(ActionEvent event) {
        playHelpButtonSound();
    }

    /**
     * Handle Resource button in Help View.
     *
     * @param event the event
     */
    @FXML
    void handleResourcesTab(ActionEvent event) {
        playHelpButtonSound();
    }

    /**
     * Handle Build button in Help View.
     *
     * @param event the event
     */
    @FXML
    void handleBuildTab(ActionEvent event) {
        playHelpButtonSound();
    }

    /**
     * Handle Trade button in Help View.
     *
     * @param event the event
     */
    @FXML
    void handleTradeTab(ActionEvent event) {
        playHelpButtonSound();
    }

    /**
     * Handle Robber button in Help View.
     *
     * @param event the event
     */
    @FXML
    void handleRobberTab(ActionEvent event) {
        playHelpButtonSound();
    }

    /**
     * Handle Development Card button in Help View.
     *
     * @param event the event
     */
    @FXML
    void handleDevelopmentCardsTab(ActionEvent event) {
        playHelpButtonSound();
    }

    /**
     * Handle Tips button in Help View.
     *
     * @param event the event
     */

    @FXML
    void handleTipsTab(ActionEvent event) {
        playHelpButtonSound();
    }
    @FXML
    void handleQuickInfo(ActionEvent event) {
        playHelpButtonSound();
    }



}

