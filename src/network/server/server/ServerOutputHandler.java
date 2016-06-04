package network.server.server;

import java.io.IOException;
import java.util.logging.Level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import enums.Color;
import enums.PlayerState;
import model.Corner;
import model.Edge;
import model.Field;
import network.ModelToProtocol;
import network.client.client.ClientOutputHandler;
import network.server.controller.ServerNetworkController;
import parsing.Parser;
import parsing.Response;
import protocol.configuration.ProtocolError;
import protocol.configuration.ProtocolGameStarted;
import protocol.connection.ProtocolHello;
import protocol.connection.ProtocolWelcome;
import protocol.messaging.ProtocolChatReceiveMessage;
import protocol.messaging.ProtocolChatSendMessage;
import protocol.object.ProtocolBuilding;
import protocol.object.ProtocolPlayer;
import protocol.serverinstructions.ProtocolBuild;
import protocol.serverinstructions.ProtocolDiceRollResult;
import protocol.serverinstructions.ProtocolResourceObtain;
import protocol.serverinstructions.ProtocolStatusUpdate;

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
	
    private static Logger logger = LogManager.getLogger(ServerOutputHandler.class.getName());

	public void statusUpdate(ProtocolPlayer player ) {
		ProtocolStatusUpdate ps = new ProtocolStatusUpdate(player);
		Response r = new Response();
		r.pSUpdate = ps;
		try {
			server.broadcast((parser.createString(r)));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}
		
		

	}

	public void buildBuilding( ProtocolBuilding building) {
		ProtocolBuild pb = new ProtocolBuild(building);
		Response r = new Response();
		r.pBuild = pb;
		try {
			server.broadcast((parser.createString(r)));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}
		
		

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
			logger.error("Threw a Input/Output Exception ", e);
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
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	public void initBoard(int amountPlayers, Field[][] fields, Edge[][][] edges, Corner[][][] corners, Field bandit) {
		
		// TODO Auto-generated method stub
	}

	public void error(String s) {
		ProtocolError pe = new ProtocolError(s);
		Response r = new Response();
		r.pError = pe;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
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
			logger.error("Threw a Input/Output Exception ", e);
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
			logger.error("Threw a Input/Output Exception ", e);
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
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}
}