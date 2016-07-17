package view.startView;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ai.AdvancedAI;
import audio.Soundeffects;
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
	@SuppressWarnings("unused")
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

	/**
	 * Initialize.
	 */
	@FXML
	public void initialize() {
		themeChooser.getItems().addAll("Standard", "Biergarten");
		themeChooser.setValue("Standard");
		themeChooser.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends String> observable, String oldValue,
						String newValue) -> reloadTheme(oldValue, newValue));
	}

	/**
	 * Reload theme.
	 *
	 * @param oldValue the old value
	 * @param newValue the new value
	 */
	private void reloadTheme(String oldValue, String newValue) {
		this.primaryStage.getScene().getStylesheets().remove(
				getClass().getResource("/textures/" + oldValue.toLowerCase() + ".css").toExternalForm());
		this.primaryStage.getScene().getStylesheets().add(
				getClass().getResource("/textures/" + newValue.toLowerCase() + ".css").toExternalForm());

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
		themeChooser.setDisable(true);
		startButton.setDisable(false);
		aiPort.setDisable(false);
		aiServer.setDisable(false);
		serverPort.setDisable(true);
		if (!Soundeffects.isMuted()){
		Soundeffects.SELECT.play();
		}
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
		themeChooser.setDisable(false);
		startButton.setDisable(false);
		serverPort.setDisable(true);
		aiPort.setDisable(true);
		aiServer.setDisable(true);
		if (!Soundeffects.isMuted()){
		Soundeffects.SELECT.play();
		}

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
		themeChooser.setDisable(true);
		serverPort.setDisable(false);
		aiPort.setDisable(true);
		aiServer.setDisable(true);
		if (!Soundeffects.isMuted()){
		Soundeffects.SELECT.play();
		}
		if (stopServer.disabledProperty().getValue()){
			startButton.setDisable(false);
		} else {
			startButton.setDisable(true);
		}

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
		gc.disconnectServer();
		stopServer.setDisable(true);
		startClient.setDisable(false);
		startAI.setDisable(false);
		startButton.setDisable(false);
		if (!Soundeffects.isMuted()){
		Soundeffects.SELECT.play();
		}

		// playButtonSound();
	}

	/**
	 * Handle start button.
	 *
	 * @param event
	 *            the event
	 */
	@FXML
	public
	void handleStartButton(ActionEvent event) {
		RadioButton rb = (RadioButton) startMode.getSelectedToggle();
		if (!Soundeffects.isMuted()){
		Soundeffects.SELECT.play();
		}
		switch (rb.getText()) {
		case "Client":
			Thread clientThread = new Thread(new Runnable() {

				@Override
				public void run() {
					setClientController(new ClientController(new Stage(), themeChooser.getValue().toLowerCase()));
				}
			});
			clientThread.run();
			break;
		case "Server":
			//TODO minimize to tray
			int port = serverPort.getText().equals("") ? 8080 : Integer.parseInt(serverPort.getText());
			gc = new ServerController(port);
			stopServer.setDisable(false);
			serverPort.setDisable(true);
			startButton.setDisable(true);
			InetAddress IP;
			try {
				IP = InetAddress.getLocalHost();
				serverIP.setText(IP.getHostAddress() + ":" + port);
			} catch (UnknownHostException e) {
				logger.trace(e);
			}
			break;
		case "AI":
			//TODO minimize to tray
			primaryStage.hide();
			String server = !aiServer.getText().equals("") ? aiServer.getText() : "localhost";
			int aip = !aiPort.getText().equals("") ? Integer.parseInt(aiPort.getText()) : 8080;
			pa2 = new AdvancedAI(server, aip);
			break;
		default:
			throw new IllegalArgumentException("Mode non existant");
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
