package application;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ai.AdvancedAI;
import ai.PrimitiveAI;
import debugging.DebugClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
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
	private Button stopServer;

	@FXML
	private TextField serverPort;

	@FXML
	private TextField aiServer;

	@FXML
	private TextField aiPort;

	@FXML
	private Label serverIP;

	private Stage primaryStage;

	private Thread serverThread;

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
	void handleStopServer(ActionEvent event){
		serverThread.interrupt();
		stopServer.setDisable(true);
		startClient.setDisable(false);
		startAI.setDisable(false);
		startButton.setDisable(false);
	}

	@FXML
	void handleStartButton(ActionEvent event) {
		RadioButton rb = (RadioButton) startMode.getSelectedToggle();
		switch (rb.getText()) {
		case "Client":
			setClientController(new ClientController(primaryStage));
			break;
		case "Server":
			int port = serverPort.getText().equals("") ? 8080 : Integer.parseInt(serverPort.getText());
			serverThread = new Thread( new Runnable() {
			    @Override
			    public void run() {
			    	gc = new ServerController(port);
			    }
			});
			serverThread.start();
			stopServer.setDisable(false);
			startClient.setDisable(true);
			startAI.setDisable(true);
			startButton.setDisable(true);
			InetAddress IP;
			try {
				IP = InetAddress.getLocalHost();
				serverIP.setText(IP.getHostAddress() + ":" + port);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "AI":
			primaryStage.hide();
			String server = !aiServer.getText().equals("") ? aiServer.getText() : "localhost" ;
			int aip = !aiPort.getText().equals("") ? Integer.parseInt(aiPort.getText()) : 8080;
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
