package application;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ai.AdvancedAI;
import ai.PrimitiveAI;
import debugging.DebugClient;
import javafx.application.Application;
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

	// 0 for client, 1 for server , 2 for AI, 3 for debug/test mode
	private int mode = 0;

	@Override
	public void start(Stage primaryStage) {

		ModelToProtocol.initModelToProtocol();
		ProtocolToModel.initProtocolToModel();
		switch (mode) {
		case 0:
			setClientController(new ClientController(primaryStage));
			break;
		case 1:
			setServerController(new ServerController());
			break;
		case 2:
			pa = new PrimitiveAI();
			pa.commence();
			break;
		case 3:
			dc = new DebugClient(primaryStage);
			break;
		case 4:
			pa = new AdvancedAI();
			pa.commence();
		}

	}

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
