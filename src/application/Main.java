package application;

import java.io.IOException;
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
import service.ModelToProtocol;
import service.ProtocolToModel;
import view.startView.StartViewController;

public class Main extends Application {

	private StartViewController startViewCtrl;

	private double xOffset = 0;
	private double yOffset = 0;

	@Override
	public void start(Stage primaryStage) {

		ModelToProtocol.initModelToProtocol();
		ProtocolToModel.initProtocolToModel();

		try {
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/view/startView/StartView.fxml").openStream());

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
					} else if (t.getCode() == KeyCode.ENTER) {
						startViewCtrl.handleStartButton(null);
					}
				}
			});
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("/textures/standard/Catan-Logo.png"));
			primaryStage.setTitle("Settlers of Catan: Launcher");

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
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		launch(args);

	}

}
