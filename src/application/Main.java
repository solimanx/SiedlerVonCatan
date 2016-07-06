package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import network.ModelToProtocol;
import network.ProtocolToModel;

import static sounds.Sound.musicLoop;

// TODO: Auto-generated Javadoc
public class Main extends Application {

    StartViewController startViewCtrl;

    private int mode = 0;

    private double xOffset = 0;
    private double yOffset = 0;

    /*
     * (non-Javadoc)
     *
     * @see javafx.application.Application#init()
     */
    @Override
    public void init() throws Exception {

        Parameters p = getParameters();
        List<String> raw = p.getRaw();
        for (String string : raw) {
            if (string.equals("server")) {
                mode = 1;
            } else if (string.equals("ai")) {
                mode = 2;
            } else if (string.equals("debug")) {
                mode = 3;
            } else if (string.equals("ai2")) {
                mode = 4;
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public void start(Stage primaryStage) {


        ModelToProtocol.initModelToProtocol();
        ProtocolToModel.initProtocolToModel();

        try {
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/application/startView.fxml").openStream());


            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    primaryStage.setX(event.getScreenX() - xOffset);
                    primaryStage.setY(event.getScreenY() - yOffset);
                }
            });
            startViewCtrl = loader.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/textures/standard.css").toExternalForm());
            scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent t) {
                    if (t.getCode() == KeyCode.ESCAPE) {
                        Stage sb = (Stage) root.getScene().getWindow();
                        sb.close();
                        System.exit(0);
                    } else if (t.getCode() == KeyCode.ENTER) {
                        startViewCtrl.handleStartButton(null);
                    }
                }
            });
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image("/textures/standard/Catan-Logo.png"));

            // primaryStage.sizeToScene();
            // primaryStage.setResizable(false);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setOnCloseRequest(e -> System.exit(0));
            primaryStage.show();

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        startViewCtrl.setStage(primaryStage);
    }

    // startClient.setUserData(1);
    // switch (mode) {
    // case 0:
    //// setClientController(new ClientController(primaryStage));
    // break;
    // case 1:
    // setServerController(new ServerController(8080));
    // break;
    // case 2:
    // pa = new PrimitiveAI();
    // pa.commence();
    // break;
    // case 3:
    // dc = new DebugClient(primaryStage);
    // break;
    // case 4:
    // pa = new AdvancedAI();
    // pa.commence();
    // }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {


        launch(args);

    }

}
