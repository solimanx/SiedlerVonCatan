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

	private static Logger logger = LogManager.getLogger(Main.class.getSimpleName());

	private ServerController gc;
	private ClientController fc;
	private DebugClient dc;
	private PrimitiveAI pa;
	private AdvancedAI pa2;

	@FXML
	private ToggleGroup startMode;

	@FXML
	private RadioButton startClient;

	@FXML
	private RadioButton startAI;

	@FXML
	private RadioButton startServer;

	@FXML
	private Button startButton;

	@FXML
	private TextField serverPort;

	@FXML
	private TextField aiServer;

	@FXML
	private TextField aiPort;

	// 0 for client, 1 for server , 2 for AI, 3 for debug/test mode
	private int mode = 0;

	private Stage primaryStage;

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
		this.primaryStage = primaryStage;
		FXMLLoader loader = new FXMLLoader();
		Parent root;
		try {
			root = loader.load(getClass().getResource("/application/startView.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.sizeToScene();
			primaryStage.setTitle("Choose mode");
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest(e -> System.exit(0));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		primaryStage.show();
		ModelToProtocol.initModelToProtocol();
		ProtocolToModel.initProtocolToModel();
//		startClient.setUserData(1);
//		switch (mode) {
//		case 0:
////			setClientController(new ClientController(primaryStage));
//			break;
//		case 1:
//			setServerController(new ServerController(8080));
//			break;
//		case 2:
//			pa = new PrimitiveAI();
//			pa.commence();
//			break;
//		case 3:
//			dc = new DebugClient(primaryStage);
//			break;
//		case 4:
//			pa = new AdvancedAI();
//			pa.commence();
//		}

	}

	public static void main(String[] args) {

		launch(args);

	}

	@FXML
	void handleAiSelected(ActionEvent event) {
		aiPort.setDisable(false);
		aiServer.setDisable(false);
		serverPort.setDisable(true);
	}

	@FXML
	void handleClientSelected(ActionEvent event) {
		serverPort.setDisable(true);
		aiPort.setDisable(true);
		aiServer.setDisable(true);
	}

	@FXML
	void handleServerSelected(ActionEvent event) {
		serverPort.setDisable(false);
		aiPort.setDisable(true);
		aiServer.setDisable(true);

	}

	@FXML
	void handleStartButton(ActionEvent event) {
		Stage s = (Stage) startButton.getScene().getWindow();
		RadioButton rb = (RadioButton) startMode.getSelectedToggle();
		switch (rb.getText()) {
		case "Client":
			s.hide();
			setClientController(new ClientController(new Stage()));
			break;
		case "Server":
			s.hide();
			this.setServerController(new ServerController(Integer.parseInt(serverPort.getText())));
			break;
		case "AI":
			s.hide();
			String server = aiServer.getText();
			int port = Integer.parseInt(aiPort.getText());
			pa = new AdvancedAI(server,port);
			pa.commence();
			break;
			default:System.out.println(rb.getText());
		}
	}


	/**
	 * @return the gc
	 */
	public ServerController getGameController() {
		return gc;
	}

	/**
	 * @param gc
	 *            the gc to set
	 */
	public void setServerController(ServerController gc) {
		this.gc = gc;
	}

	/**
	 * @return the fc
	 */
	public ClientController getFlowController() {
		return fc;
	}

	/**
	 * @param fc
	 *            the fc to set
	 */
	public void setClientController(ClientController fc) {
		this.fc = fc;
	}
}
