package application;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import network.ModelToProtocol;
import network.client.controller.ClientController;
import network.server.controller.ServerController;

public class Main extends Application {

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(Main.class.getName());

	private ServerController gc;
	private ClientController fc;

	// true for server, false for client
	private boolean mode = false;

	@Override
	public void start(Stage primaryStage) {
		// Logger logger = Logger.getLogger("MyLog");
		//
		// FileHandler fileHandler = new FileHandler("Log.txt");
		// fileHandler = new FileHandler("MyLogFile.log", true);
		//
		// logger.addHandler(fileHandler);
		// logger.setLevel(Level.ALL);
		//
		// SimpleFormatter formatter = new SimpleFormatter();
		// fileHandler.setFormatter(formatter);
		//
		// logger.log(Level.INFO, "Our first log");
		ModelToProtocol.initModelToProtocol();
		if (mode) {
			// server = new Server();
			setGameController(new ServerController());
		} else {
			setFlowController(new ClientController(primaryStage));
		}

	}

	@Override
	public void init() throws Exception {
		Parameters p = getParameters();
		List<String> raw = p.getRaw();
		for (String string : raw) {
			if (string.equals("server")) {
				mode = true;
			}
		}
	}

	public static void main(String[] args) {

		launch(args);

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
	public void setGameController(ServerController gc) {
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
	public void setFlowController(ClientController fc) {
		this.fc = fc;
	}
}
