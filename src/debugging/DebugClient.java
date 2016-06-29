package debugging;

import application.lobby.LobbyController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * To "illegaly" test sending JSONs, and figuring out how the test server
 * responds.
 */
public class DebugClient extends Application {
	private static Logger logger = LogManager.getLogger(DebugClient.class.getSimpleName());
	DebugClientController dcc;

	/**
	 * Instantiates a new debug client.
	 *
	 * @param primaryStage the primary stage
	 */
	public DebugClient(Stage primaryStage) {
		System.out.println("Running debugging/testing mode");
		logger.info("Running debugging/testing mode");

		try {
			start(primaryStage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
			logger.catching(Level.DEBUG, e);
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
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
		primaryStage.setResizable(true);
		primaryStage.setOnCloseRequest(e -> System.exit(0));
		// primaryStage.initStyle(StageStyle.UTILITY);
		//
		primaryStage.show();

	}
}
