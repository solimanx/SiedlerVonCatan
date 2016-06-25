package application;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ai.AdvancedAI;
import ai.PrimitiveAI;
import debugging.DebugClient;
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

public class StartViewController {

	private static Logger logger = LogManager.getLogger(StartViewController.class.getSimpleName());

	private ServerController gc;
	private ClientController fc;
	private DebugClient dc;
	private PrimitiveAI pa;
	private AdvancedAI pa2;

	@FXML
	private RadioButton startClient;

	@FXML
	private ToggleGroup startMode;

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

	private Stage primaryStage;

	public void setStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
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
		RadioButton rb = (RadioButton) startMode.getSelectedToggle();
		switch (rb.getText()) {
		case "Client":
			setClientController(new ClientController(primaryStage));
			break;
		case "Server":
			primaryStage.hide();
			int port = !(serverPort.equals("")) ? Integer.parseInt(serverPort.getText()) : 8080;
			new Thread( new Runnable() {
			    @Override
			    public void run() {
			    	gc = new ServerController(port);			    	
			    }
			}).start();	
			break;
		case "AI":
			primaryStage.hide();
			String server = aiServer.getText();
			int aip = !aiPort.equals("") ? Integer.parseInt(aiPort.getText()) : 8080;
			pa = new AdvancedAI(server, aip);
			pa.commence();
			break;
		default:
			System.out.println(rb.getText());
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
