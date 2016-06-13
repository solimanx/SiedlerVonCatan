package debugging;

import application.lobby.LobbyController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * To "illegaly" test sending JSONs, and figuring out how the test server
 * responds.
 */
public class DebugClient extends Application{
	DebugClientController dcc;
	public DebugClient(Stage primaryStage) {
		System.out.println("Running debugging/testing mode");
		try {
			start(primaryStage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/debugging/DebugClientFXML.fxml").openStream());
		Scene scene = new Scene(root);
		dcc = (DebugClientController) loader.getController();
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		//
		primaryStage.setTitle("Debug Client");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(e -> System.exit(0));
		// primaryStage.initStyle(StageStyle.UTILITY);
		//
		primaryStage.show();
		
	}
}
