package network.client.client;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import network.ModelToProtocol;
import parsing.Parser;
import parsing.Response;
import protocol.clientinstructions.*;
import protocol.clientinstructions.trade.ProtocolTradeAccept;
import protocol.clientinstructions.trade.ProtocolTradeCancel;
import protocol.clientinstructions.trade.ProtocolTradeComplete;
import protocol.clientinstructions.trade.ProtocolTradeRequest;
import protocol.configuration.ProtocolClientReady;
import protocol.configuration.ProtocolPlayerProfile;
import protocol.connection.ProtocolHello;
import protocol.messaging.ProtocolChatSendMessage;
import protocol.object.ProtocolResource;
import protocol3.clientinstructions.ProtocolBuyDevelopmentCards;

public class ClientOutputHandler {

	private Client client;
	private Parser parser;

	public ClientOutputHandler(Client client) {
		this.client = client;
		this.parser = new Parser();
	}

	private static Logger logger = LogManager.getLogger(ClientOutputHandler.class.getName());

	/**
	 * If the connection can be established, send "Hello" back to server.
	 */
	public void clientHello(String clientVersion) {
		ProtocolHello ph = new ProtocolHello(clientVersion, null);
		Response r = new Response();
		r.pHello = ph;
		try {
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

	public void requestSetBandit(int x, int y, int victim_id) {
		String location = ModelToProtocol.getFieldID(x,y);

		ProtocolRobberMovementRequest prmr = new ProtocolRobberMovementRequest(location, victim_id);
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

	public void handleHarbourRequest(ProtocolResource offer, ProtocolResource withdrawal) {
		ProtocolHarbourRequest phr = new ProtocolHarbourRequest(offer, withdrawal);
		Response r = new Response();
		r.pHarbourRequest = phr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	public void handleTradeAccept(int trade_id) {
		ProtocolTradeAccept pta = new ProtocolTradeAccept(trade_id);
		Response r = new Response();
		r.pTradeAccept = pta;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}
	}

	public void handleTradeRequest(ProtocolResource offer, ProtocolResource withdrawal) {
		ProtocolTradeRequest ptr = new ProtocolTradeRequest(offer, withdrawal);
		Response r = new Response();
		r.pTradeRequest = ptr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}
	}

	public void handleTradeCancel(int trade_id) {
		ProtocolTradeCancel ptc = new ProtocolTradeCancel(trade_id);
		Response r = new Response();
		r.pTradeCancel = ptc;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();

		}

	}

	public void handleTradeComplete(int trade_id, int tradePartner_id) {
		ProtocolTradeComplete ptco = new ProtocolTradeComplete(trade_id, tradePartner_id);
		Response r = new Response();
		r.pTradeComplete = ptco;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();

		}
	}
	
	public void handleBuyDevelopmentCards() {
		ProtocolBuyDevelopmentCards pbdc = new ProtocolBuyDevelopmentCards();
		Response r = new Response();
		r.pBuyDevCards = pbdc;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();

		
		}
	}

	public void requestBuildVillage(int x, int y, int dir) {
		String location = ModelToProtocol.getCornerID(x,y,dir);
		ProtocolBuildRequest pbr = new ProtocolBuildRequest("Dorf", location);
		
		Response r = new Response();
		r.pBuildRequest = pbr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}
		
	}

	public void requestBuildStreet(int x, int y, int dir) {
		String location = ModelToProtocol.getEdgeID(x,y,dir);
		ProtocolBuildRequest pbr = new ProtocolBuildRequest("Straße", location);
		
		Response r = new Response();
		r.pBuildRequest = pbr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}
		
	}

	public void requestBuildCity(int x, int y, int dir) {
		String location = ModelToProtocol.getCornerID(x,y,dir);
		ProtocolBuildRequest pbr = new ProtocolBuildRequest("Stadt", location);
		
		Response r = new Response();
		r.pBuildRequest = pbr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}
		
	}

}