package application;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import network.ModelToProtocol;
import network.client.controller.FlowController;
import network.server.controller.GameController;

public class Main extends Application {

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(Main.class.getName());

	private GameController gc;
	private FlowController fc;

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
			setGameController(new GameController(primaryStage));
		} else {
			setFlowController(new FlowController(primaryStage));
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
	public GameController getGameController() {
		return gc;
	}

	/**
	 * @param gc
	 *            the gc to set
	 */
	public void setGameController(GameController gc) {
		this.gc = gc;
	}

	/**
	 * @return the fc
	 */
	public FlowController getFlowController() {
		return fc;
	}

	/**
	 * @param fc
	 *            the fc to set
	 */
	public void setFlowController(FlowController fc) {
		this.fc = fc;
	}
}
