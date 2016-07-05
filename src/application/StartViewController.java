package application;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ai.AdvancedAI;
import ai.PrimitiveAI;
import audio.Soundeffects;
import debugging.DebugClient;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import network.client.controller.ClientController;
import network.server.controller.ServerController;

// TODO: Auto-generated Javadoc
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

	@FXML
	private ChoiceBox<String> themeChooser;

	private Stage primaryStage;

	private Thread serverThread;

	@FXML
	public void initialize() {
		themeChooser.getItems().addAll("Standard", "Biergarten");
		themeChooser.setValue("Standard");
		themeChooser.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends String> observable, String oldValue,
						String newValue) -> reloadTheme(oldValue, newValue));
	}

	private void reloadTheme(String oldValue, String newValue) {
		this.primaryStage.getScene().getStylesheets().remove(
				getClass().getResource("/network/client/view/" + oldValue.toLowerCase() + ".css").toExternalForm());
		this.primaryStage.getScene().getStylesheets().add(
				getClass().getResource("/network/client/view/" + newValue.toLowerCase() + ".css").toExternalForm());
		
	}

	/**
	 * Sets the stage.
	 *
	 * @param primaryStage
	 *            the new stage
	 */
	public void setStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	/**
	 * Handle ai selected.
	 *
	 * @param event
	 *            the event
	 */
	@FXML
	void handleAiSelected(ActionEvent event) {
		aiPort.setDisable(false);
		aiServer.setDisable(false);
		serverPort.setDisable(true);
		Soundeffects.SELECT.play();
		// playButtonSound();
	}

	/**
	 * Handle client selected.
	 *
	 * @param event
	 *            the event
	 */
	@FXML
	void handleClientSelected(ActionEvent event) {
		serverPort.setDisable(true);
		aiPort.setDisable(true);
		aiServer.setDisable(true);
		Soundeffects.SELECT.play();

		// playButtonSound();
	}

	/**
	 * Handle server selected.
	 *
	 * @param event
	 *            the event
	 */
	@FXML
	void handleServerSelected(ActionEvent event) {
		serverPort.setDisable(false);
		aiPort.setDisable(true);
		aiServer.setDisable(true);
		Soundeffects.SELECT.play();

		// playButtonSound();
	}

	/**
	 * Handle stop server.
	 *
	 * @param event
	 *            the event
	 */
	@FXML
	void handleStopServer(ActionEvent event) {
		serverThread.interrupt();
		stopServer.setDisable(true);
		startClient.setDisable(false);
		startAI.setDisable(false);
		startButton.setDisable(false);
		Soundeffects.SELECT.play();

		// playButtonSound();
	}

	/**
	 * Handle start button.
	 *
	 * @param event
	 *            the event
	 */
	@FXML
	void handleStartButton(ActionEvent event) {
		RadioButton rb = (RadioButton) startMode.getSelectedToggle();
		Soundeffects.SELECT.play();

		switch (rb.getText()) {
		case "Client":
			Thread clientThread = new Thread(new Runnable() {

				@Override
				public void run() {
					setClientController(new ClientController(new Stage(), themeChooser.getValue().toLowerCase()));
				}
			});
			Platform.runLater(clientThread);
			break;
		case "Server":
			int port = serverPort.getText().equals("") ? 8080 : Integer.parseInt(serverPort.getText());
			serverThread = new Thread(new Runnable() {
				@Override
				public void run() {
					gc = new ServerController(port);
				}
			});
			serverThread.start();
			stopServer.setDisable(false);
			// startClient.setDisable(true);
			// startAI.setDisable(true);
			// startButton.setDisable(true);
			serverPort.setDisable(true);
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
			String server = !aiServer.getText().equals("") ? aiServer.getText() : "localhost";
			int aip = !aiPort.getText().equals("") ? Integer.parseInt(aiPort.getText()) : 8080;
			pa = new AdvancedAI(server, aip);
			pa.commence();
			break;
		default:
			System.out.println(rb.getText());

		}
	}

	/**
	 * Gets the game controller.
	 *
	 * @return the gc
	 */
	public ServerController getGameController() {
		return gc;
	}

	/**
	 * Sets the server controller.
	 *
	 * @param gc
	 *            the gc to set
	 */
	public void setServerController(ServerController gc) {
		this.gc = gc;
	}

	/*
	 * public static void playSound() { try { File file = new File(
	 * "E:\\Programing\\IntelliJ\\sep\\h\\NiedlichePixel\\src\\sounds\\testsong.wav"
	 * ); Clip clip = AudioSystem.getClip();
	 * clip.open(AudioSystem.getAudioInputStream(file)); clip.start();
	 * Thread.sleep(clip.getMicrosecondLength()); } catch (Exception e) {
	 * System.err.println(e.getMessage()); } }
	 */

	/**
	 * Gets the flow controller.
	 *
	 * @return the fc
	 */
	public ClientController getFlowController() {
		return fc;
	}

	/**
	 * Sets the client controller.
	 *
	 * @param fc
	 *            the fc to set
	 */
	public void setClientController(ClientController fc) {
		this.fc = fc;
	}
}
