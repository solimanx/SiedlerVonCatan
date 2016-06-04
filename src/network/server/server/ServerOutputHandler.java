package network.server.server;

import java.io.IOException;
import java.util.logging.Level;

import com.sun.javafx.logging.Logger;
import enums.Color;
import enums.PlayerState;
import model.Corner;
import model.Edge;
import model.Field;
import network.ModelToProtocol;
import network.server.controller.ServerNetworkController;
import parsing.Parser;
import parsing.Response;
import protocol.configuration.ProtocolError;
import protocol.configuration.ProtocolGameStarted;
import protocol.connection.ProtocolHello;
import protocol.connection.ProtocolWelcome;
import protocol.messaging.ProtocolChatReceiveMessage;
import protocol.messaging.ProtocolChatSendMessage;
import protocol.serverinstructions.ProtocolDiceRollResult;
import protocol.serverinstructions.ProtocolResourceObtain;

//import static org.apache.logging.log4j.FormatterLoggerManualExample.logger;

public class ServerOutputHandler {
	private Server server;
	private Parser parser;
	private ServerNetworkController networkController;

	public ServerOutputHandler(Server server, ServerNetworkController serverNetworkController) {
		this.server = server;
		this.networkController = serverNetworkController;
		this.parser = new Parser();
	}

	public void statusUpdate(int playerId, Color color, String name, PlayerState status, int victoryPoints,
			int[] resources) {
		// TODO Auto-generated method stub

	}

	public void buildBuilding(int x, int y, int dir, int playerId, String string) {
		// TODO Auto-generated method stub

	}

	/**
	 * Create ProtocolHello JSON and broadcast it as a String through server
	 *
	 * @param serverVersion
	 * @param protocolVersion
	 */
	public void hello(String serverVersion, String protocolVersion, int thread_id) {
		ProtocolHello ph = new ProtocolHello(serverVersion, protocolVersion);
		Response r = new Response();
		r.pHello = ph;
		try {
			server.sendToClient(parser.createString(r), thread_id);
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void chatReceiveMessage(int threadID, String message) {
		ProtocolChatReceiveMessage pcrm = new ProtocolChatReceiveMessage(threadID, message);
		Response r = new Response();
		r.pChatReceive = pcrm;
		try {
			server.broadcast((parser.createString(r)));
		} catch (IOException e) {
			// TODO logging
			e.printStackTrace();
		}

	}

	public void initBoard(int amountPlayers, Field[][] fields, Edge[][][] edges, Corner[][][] corners, Field bandit) {
		
		ProtocolGameStarted pgs = new ProtocolGameStarted(board)
	}

	public void error(String s) {
		ProtocolError pe = new ProtocolError(s);
		Response r = new Response();
		r.pError = pe;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void diceRollResult(int playerID, int result) {
		ProtocolDiceRollResult dr = new ProtocolDiceRollResult(ModelToProtocol.getPlayerId(playerID), result);
		Response r = new Response();
		r.pDRResult = dr;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void resourceObtain(int playerID, int[] resources) {
		ProtocolResourceObtain po = new ProtocolResourceObtain(playerID, ModelToProtocol.getResources(resources));
		Response r = new Response();
		r.pRObtain = po;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void welcome(int player_id) {
		ProtocolWelcome pw = new ProtocolWelcome(player_id);

		Response r = new Response();
		r.pWelcome = pw;
		try {
			server.sendToClient(parser.createString(r), networkController.getThreadID(player_id));
		} catch (IOException e) {
			// TODO logging
			e.printStackTrace();
		}

	}
}