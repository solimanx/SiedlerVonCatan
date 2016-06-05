package network.client.client;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parsing.Parser;
import parsing.Response;
import protocol.clientinstructions.ProtocolBuildRequest;
import protocol.clientinstructions.ProtocolDiceRollRequest;
import protocol.clientinstructions.ProtocolEndTurn;
import protocol.clientinstructions.ProtocolRobberMovementRequest;
import protocol.configuration.ProtocolClientReady;
import protocol.configuration.ProtocolPlayerProfile;
import protocol.connection.ProtocolHello;
import protocol.messaging.ProtocolChatSendMessage;

public class ClientOutputHandler {

	private Client client;
	private Parser parser;

	public ClientOutputHandler(Client client) {
		this.client = client;
		this.parser = new Parser();
	}

	private static Logger logger = LogManager.getLogger(ClientOutputHandler.class.getName());

	public void handleBuildRequest(String building_type, String location_id) {
		ProtocolBuildRequest pbr = new ProtocolBuildRequest(building_type, location_id);
		Response r = new Response();
		r.pBuildRequest = pbr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	/**
	 * If the connection can be established, send "Hello" back to server.
	 */
	public void clientHello(String clientVersion) {
		ProtocolHello ph = new ProtocolHello(clientVersion, null);
		Response r = new Response();
		r.pHello = ph;
		try {
			System.out.println("CLIENT HELLO OUTPUT: " + parser.createString(r));
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	public void clientReady() {
		ProtocolClientReady pcr = new ProtocolClientReady();
		Response r = new Response();
		r.pClientReady = pcr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	public void chatSendMessage(String s) {
		ProtocolChatSendMessage pcsm = new ProtocolChatSendMessage(s);
		Response r = new Response();
		r.pChatSend = pcsm;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	public void diceRollRequest() {
		ProtocolDiceRollRequest pdrr = new ProtocolDiceRollRequest();
		Response r = new Response();
		r.pDiceRollRequest = pdrr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();

		}
	}

	public void endTurn() {

		ProtocolEndTurn pcet = new ProtocolEndTurn();
		Response r = new Response();
		r.pEndTurn = pcet;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	public void requestSetBandit(int player_Id, String location_id, int victim_id) {

		ProtocolRobberMovementRequest prmr = new ProtocolRobberMovementRequest(player_Id, location_id, victim_id);
		Response r = new Response();
		r.pRobberMoveRequest = prmr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	public void sendPlayerProfile(String name, enums.Color color) {

		ProtocolPlayerProfile pcr = new ProtocolPlayerProfile(name, color);
		Response r = new Response();
		r.pPlayerProfile = pcr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

}
