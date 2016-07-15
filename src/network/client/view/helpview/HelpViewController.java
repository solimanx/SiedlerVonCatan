package network.client.view.helpview;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;


import javafx.event.Event;
import javafx.fxml.FXML;


import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;


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
    @FXML
    protected TabPane pane;
    private boolean tabSelection = false;

    private boolean checkTab() {
        tabSelection = !tabSelection;
        return tabSelection;
    }


    /**
     * Handle Instruction button in Help View.
     *
     * @param event the event
     */


    @FXML
    void handleInstructionsTab(Event event) {
        pane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                if (checkTab()) {
                    playHelpButtonSound();
                }
            }
        });


    }

    /**
     * Handle Start Game button in Help View.
     *
     * @param event the event
     */
    @FXML
    void handleStartGameTab(Event event) {

        pane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                if (checkTab()) {
                    playHelpButtonSound();
                }
            }
        });
    }

    /**
     * Handle Game Board button in Help View.
     *
     * @param event the event
     */
    @FXML
    void handleGameBoardTab(Event event) {
        pane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                if (checkTab()) {
                    playHelpButtonSound();

                }
            }
        });
    }

    /**
     * Handle Resource button in Help View.
     *
     * @param event the event
     */
    @FXML
    void handleResourcesTab(Event event) {
        pane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                if (checkTab()) {
                    playHelpButtonSound();
                }
            }
        });
    }

    /**
     * Handle Build button in Help View.
     *
     * @param event the event
     */
    @FXML
    void handleBuildTab(Event event) {
        pane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                if (checkTab()) {
                    playHelpButtonSound();
                }
            }
        });
    }

    /**
     * Handle Trade button in Help View.
     *
     * @param event the event
     */
    @FXML
    void handleTradeTab(Event event) {
        pane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                if (checkTab()) {
                    playHelpButtonSound();
                }
            }
        });
    }

    /**
     * Handle Robber button in Help View.
     *
     * @param event the event
     */
    @FXML
    void handleRobberTab(Event event) {
        pane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                if (checkTab()) {
                    playHelpButtonSound();
                }
            }
        });
    }

    /**
     * Handle Development Card button in Help View.
     *
     * @param event the event
     */
    @FXML
    void handleDevelopmentCardsTab(Event event) {
        pane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                if (checkTab()) {
                    playHelpButtonSound();
                }
            }
        });
    }

    /**
     * Handle Tips button in Help View.
     *
     * @param event the event
     */

    @FXML
    void handleTipsTab(Event event) {
        pane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                if (checkTab()) {
                    playHelpButtonSound();
                }
            }
        });
    }

    @FXML
    void handleQuickInfo(Event event) {
        pane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                if (checkTab()) {
                    playHelpButtonSound();
                }
            }
        });
    }


}

