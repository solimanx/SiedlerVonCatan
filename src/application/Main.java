package application;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ai.AdvancedAI;
import ai.PrimitiveAI;
import debugging.DebugClient;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import network.ModelToProtocol;
import network.ProtocolToModel;
import network.client.controller.ClientController;
import network.server.controller.ServerController;

public class Main extends Application {

	StartViewController startViewCtrl;

	private int mode = 0;

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

	@Override
	public void start(Stage primaryStage) {

		ModelToProtocol.initModelToProtocol();
		ProtocolToModel.initProtocolToModel();


		try {
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/application/startView.fxml").openStream());
			startViewCtrl = loader.getController();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/network/client/view/application.css").toExternalForm());
			primaryStage.setScene(scene);
//			primaryStage.sizeToScene();
			primaryStage.setTitle("Choose mode");
//			primaryStage.setResizable(false);
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

	

	public static void main(String[] args) {

		launch(args);

	}

}
