package network.client.client;

import java.io.IOException;

import enums.ResourceType;
import org.apache.logging.log4j.Level;
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
import protocol.dualinstructions.ProtocolPlayInventionCard;
import protocol.dualinstructions.ProtocolPlayKnightCard;
import protocol.dualinstructions.ProtocolPlayMonopolyCard;
import protocol.dualinstructions.ProtocolPlayRoadCard;
import protocol.messaging.ProtocolChatSendMessage;
import protocol.object.ProtocolResource;
import protocol.serverinstructions.ProtocolRobberMovement;

public class ClientOutputHandler {

	private Client client;
	private Parser parser;

	/**
	 * @param client
	 */
	public ClientOutputHandler(Client client) {
		this.client = client;
		this.parser = new Parser();
	}

	private static Logger logger = LogManager.getLogger(ClientOutputHandler.class.getName());

	/**
	 * If the connection can be established, send "Hello" back to server.
	 */
	/**
	 * @param clientVersion
	 */
	public void sendHello(String clientVersion) {
		ProtocolHello ph = new ProtocolHello(clientVersion, null);
		Response r = new Response();
		r.pHello = ph;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 *
	 */
	public void sendReady() {
		ProtocolClientReady pcr = new ProtocolClientReady();
		Response r = new Response();
		r.pClientReady = pcr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * @param s
	 */
	public void sendChatMessage(String s) {
		ProtocolChatSendMessage pcsm = new ProtocolChatSendMessage(s);
		Response r = new Response();
		r.pChatSend = pcsm;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 *
	 */
	public void sendDiceRollRequest() {
		ProtocolDiceRollRequest pdrr = new ProtocolDiceRollRequest();
		Response r = new Response();
		r.pDiceRollRequest = pdrr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();

		}
	}

	/**
	 *
	 */
	public void sendEndTurn() {

		ProtocolEndTurn pcet = new ProtocolEndTurn();
		Response r = new Response();
		r.pEndTurn = pcet;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * @param x
	 * @param y
	 * @param victim_id
	 */
	public void sendBanditRequest(int x, int y, int victim_id) {
		String location = ModelToProtocol.getFieldID(x, y);

		ProtocolRobberMovementRequest prmr = new ProtocolRobberMovementRequest(location, victim_id);
		Response r = new Response();
		r.pRobberMoveRequest = prmr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * @param name
	 * @param color
	 */
	public void sendPlayerProfile(String name, enums.Color color) {

		ProtocolPlayerProfile pcr = new ProtocolPlayerProfile(name, color);
		Response r = new Response();
		r.pPlayerProfile = pcr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * @param losses
	 */
	public void sendRobberLoss(int[] losses) {
		ProtocolResource prL = ModelToProtocol.convertToProtocolResource(losses);
		ProtocolRobberLoss prlosses = new ProtocolRobberLoss(prL);
		Response r = new Response();
		r.pRobberLoss = prlosses;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}


	/**
	 * @param offer
	 * @param demand
	 */
	public void requestHarbourTrade(int[] offer, int[] demand) {
		ProtocolResource prOff = ModelToProtocol.convertToProtocolResource(offer);
		ProtocolResource prDem = ModelToProtocol.convertToProtocolResource(demand);

		ProtocolHarbourRequest phr = new ProtocolHarbourRequest(prOff, prDem);
		Response r = new Response();
		r.pHarbourRequest = phr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * @param tradeID
	 */
	public void acceptTrade(int tradeID) {
		ProtocolTradeAccept pta = new ProtocolTradeAccept(tradeID);
		Response r = new Response();
		r.pTradeAccept = pta;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}
	}

	/**
	 * @param offer
	 * @param demand
	 */
	public void requestTrade(int[] offer, int[] demand) {
		ProtocolResource prOff = ModelToProtocol.convertToProtocolResource(offer);
		ProtocolResource prDem = ModelToProtocol.convertToProtocolResource(demand);
		ProtocolTradeRequest ptr = new ProtocolTradeRequest(prOff, prDem);
		Response r = new Response();
		r.pTradeRequest = ptr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}
	}

	/**
	 * @param tradeID
	 */
	public void cancelTrade(int tradeID) {
		ProtocolTradeCancel ptc = new ProtocolTradeCancel(tradeID);
		Response r = new Response();
		r.pTradeCancel = ptc;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();

		}

	}

	/**
	 * @param tradeID
	 * @param tradePartnerID
	 */
	public void tradeComplete(int tradeID, int tradePartnerID) {
		ProtocolTradeComplete ptco = new ProtocolTradeComplete(tradeID, tradePartnerID);
		Response r = new Response();
		r.pTradeComplete = ptco;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();

		}
	}

	/**
	 *
	 */
	public void buyDevelopmentCard() {
		ProtocolBuyDevCard pbdc = new ProtocolBuyDevCard();
		Response r = new Response();
		r.pBuyDevCards = pbdc;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();

		}
	}

	/**
	 * @param x
	 * @param y
	 * @param dir
	 */
	public void requestBuildVillage(int x, int y, int dir) {
		String location = ModelToProtocol.getCornerID(x, y, dir);
		ProtocolBuildRequest pbr = new ProtocolBuildRequest("Dorf", location);

		Response r = new Response();
		r.pBuildRequest = pbr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * @param x
	 * @param y
	 * @param dir
	 */
	public void requestBuildStreet(int x, int y, int dir) {
		String location = ModelToProtocol.getEdgeID(x, y, dir);
		ProtocolBuildRequest pbr = new ProtocolBuildRequest("Straße", location);

		Response r = new Response();
		r.pBuildRequest = pbr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * @param x
	 * @param y
	 * @param dir
	 */
	public void requestBuildCity(int x, int y, int dir) {
		String location = ModelToProtocol.getCornerID(x, y, dir);
		ProtocolBuildRequest pbr = new ProtocolBuildRequest("Stadt", location);

		Response r = new Response();
		r.pBuildRequest = pbr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * @param resource
	 */
	public void handleMonopoly(int[] resource) {
		// Resource type goes in
		// TODO
		ProtocolPlayMonopolyCard pmc = new ProtocolPlayMonopolyCard(null);
		Response r = new Response();
		r.pPlayMonopolyCard = pmc;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * @param resource
	 */
	public void playInventionCard(int[] resource) {
		ProtocolResource pr = ModelToProtocol.convertToProtocolResource(resource);
		ProtocolPlayInventionCard pici = new ProtocolPlayInventionCard(pr);
		Response r = new Response();
		r.pPlayInventionCard = pici;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * @param resource
	 */
	public void monopolyCardInfo(ResourceType resource) {

		ProtocolPlayMonopolyCard pmci = new ProtocolPlayMonopolyCard(resource);
		Response r = new Response();
		r.pPlayMonopolyCard = pmci;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * @param road1_id
	 * @param road2_id
	 */
	public void roadBuildingCardInfo(String road1_id, String road2_id) {
		ProtocolPlayRoadCard prbci = new ProtocolPlayRoadCard(road1_id, road2_id);
		Response r = new Response();
		r.pPlayRoadCard = prbci;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * @param road1_id
	 * @param target
	 */
	public void playKnightCard(String road1_id, int target) {
		ProtocolPlayKnightCard ppkc = new ProtocolPlayKnightCard(road1_id, target);
		Response r = new Response();
		r.pPlayKnightCard = ppkc;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}
	}

}