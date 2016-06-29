package network;

import model.Board;
import model.HexService;
import network.server.server.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestPTM {
	private static Logger logger = LogManager.getLogger(TestPTM.class.getSimpleName());

	/**
	 * The main method.
	 *
	 * @param Args the arguments
	 */
	public static void main(String[] Args) {
		Board board = new Board();
		System.out.println(ModelToProtocol.getEdgeID(0, 0, 0));
		logger.debug(ModelToProtocol.getEdgeID(0, 0, 0));
	}
}