package network.client.client;

import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.net.Protocol;

import enums.ResourceType;
import model.Index;
import network.ModelToProtocol;
import network.ProtocolToModel;
import parsing.Parser;
import parsing.Response;
import protocol.clientinstructions.ProtocolBuildRequest;
import protocol.clientinstructions.ProtocolBuyDevCard;
import protocol.clientinstructions.ProtocolDiceRollRequest;
import protocol.clientinstructions.ProtocolEndTurn;
import protocol.clientinstructions.ProtocolHarbourRequest;
import protocol.clientinstructions.ProtocolRobberLoss;
import protocol.clientinstructions.ProtocolRobberMovementRequest;
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

	private static Logger logger = LogManager.getLogger(ClientOutputHandler.class.getSimpleName());

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
			logger.error("Threw an Input/Output Exception ", e);
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
			logger.error("Threw an Input/Output Exception ", e);
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
			logger.error("Threw an Input/Output Exception ", e);
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
			logger.error("Threw an Input/Output Exception ", e);
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
			logger.error("Threw an Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * @param x
	 * @param y
	 * @param victim_id
	 */
	public void sendBanditRequest(int x, int y, Integer victim_id) {
		Index i = ProtocolToModel.getProtocolOneIndex(ModelToProtocol.getFieldID(x, y));
		ProtocolRobberMovementRequest prmr = new ProtocolRobberMovementRequest(i, victim_id);
		Response r = new Response();
		r.pRobberMoveRequest = prmr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw an Input/Output Exception ", e);
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
			logger.error("Threw an Input/Output Exception ", e);
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
			logger.error("Threw an Input/Output Exception ", e);
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
			logger.error("Threw an Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * @param tradeID
	 */
	public void acceptTrade(int tradeID) {
		ProtocolTradeAccept pta = new ProtocolTradeAccept(tradeID, true);
		Response r = new Response();
		r.pTradeAccept = pta;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw an Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}
	}

	// TODO
	public void declineTrade(int tradeID) {
		ProtocolTradeAccept pta = new ProtocolTradeAccept(tradeID, false);
		Response r = new Response();
		r.pTradeAccept = pta;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw an Input/Output Exception ", e);
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
			logger.error("Threw an Input/Output Exception ", e);
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
			logger.error("Threw an Input/Output Exception ", e);
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
			logger.error("Threw an Input/Output Exception ", e);
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
			logger.error("Threw an Input/Output Exception ", e);
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
		String locationString = ModelToProtocol.getCornerID(x, y, dir);
		Index[] location = ModelToProtocol.convertCornerIndex(locationString);
		ProtocolBuildRequest pbr = new ProtocolBuildRequest("Dorf", location);

		Response r = new Response();
		r.pBuildRequest = pbr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw an Input/Output Exception ", e);
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
		String locationString = ModelToProtocol.getEdgeID(x, y, dir);
		Index[] location = ModelToProtocol.convertToEdgeIndex(locationString);
		ProtocolBuildRequest pbr = new ProtocolBuildRequest("Straße", location);

		Response r = new Response();
		r.pBuildRequest = pbr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw an Input/Output Exception ", e);
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
		String locationString = ModelToProtocol.getCornerID(x, y, dir);
		Index[] location = ModelToProtocol.convertCornerIndex(locationString);
		ProtocolBuildRequest pbr = new ProtocolBuildRequest("Stadt", location);

		Response r = new Response();
		r.pBuildRequest = pbr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw an Input/Output Exception ", e);
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
			logger.error("Threw an Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * @param resource
	 */
	public void playMonopolyCard(ResourceType resource) {

		ProtocolPlayMonopolyCard pmci = new ProtocolPlayMonopolyCard(resource);
		Response r = new Response();
		r.pPlayMonopolyCard = pmci;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw an Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * @param road1_id
	 * @param road2_id
	 */
	public void playRoadCard(String road1_id, String road2_id) {
		Index[] road1 = ModelToProtocol.convertToEdgeIndex(road1_id);
		Index[] road2 = ModelToProtocol.convertToEdgeIndex(road2_id);

		ProtocolPlayRoadCard prbci = new ProtocolPlayRoadCard(road1, road2);
		Response r = new Response();
		r.pPlayRoadCard = prbci;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw an Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	public void playKnightCard(int x, int y, Integer victimID) {
		Index i = ProtocolToModel.getProtocolOneIndex(ModelToProtocol.getFieldID(x, y));
		ProtocolPlayKnightCard pkc = new ProtocolPlayKnightCard(i, victimID);
		Response r = new Response();
		r.pPlayKnightCard = pkc;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.trace(Level.ERROR, e);
		}

	}

	public void sendCheat(String string) {
		try {
			client.write(string);
		} catch (IOException e) {
			logger.error("Threw an Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}
	}

}