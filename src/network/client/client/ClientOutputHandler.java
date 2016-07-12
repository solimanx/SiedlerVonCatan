package network.client.client;

import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import enums.CheatCode;
import enums.ResourceType;
import model.Index;
import network.ModelToProtocol;
import network.ProtocolToModel;
import parsing.Parser;
import parsing.Response;
import protocol.cheats.ProtocolCheat;
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

// TODO: Auto-generated Javadoc
public class ClientOutputHandler {

	private Client client;
	private Parser parser;

	/**
	 * Instantiates a new client output handler.
	 *
	 * @param client the client
	 */
	public ClientOutputHandler(Client client) {
		this.client = client;
		this.parser = new Parser();
	}

	private static Logger logger = LogManager.getLogger(ClientOutputHandler.class.getSimpleName());

	/**
	 * If the connection can be established, send "Hello" back to server.
	 *
	 * @param clientVersion the client version
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
	 * Send ready.
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
	 * Send chat message.
	 *
	 * @param s the s
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
	 * Send dice roll request.
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
	 * Send end turn.
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
	 * Send bandit request.
	 *
	 * @param x the x
	 * @param y the y
	 * @param victim_id the victim id
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
	 * Send player profile.
	 *
	 * @param name the name
	 * @param color the color
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
	 * Send robber loss.
	 *
	 * @param losses the losses
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
	 * Request harbour trade.
	 *
	 * @param offer the offer
	 * @param demand the demand
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
	 * Accept trade.
	 *
	 * @param tradeID the trade ID
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

	/**
	 * Decline trade.
	 *
	 * @param tradeID the trade ID
	 */
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
	 * Request trade.
	 *
	 * @param offer the offer
	 * @param demand the demand
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
	 * Cancel trade.
	 *
	 * @param tradeID the trade ID
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
	 * Trade complete.
	 *
	 * @param tradeID the trade ID
	 * @param tradePartnerID the trade partner ID
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
	 * Buy development card.
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
	 * Request build village.
	 *
	 * @param x the x
	 * @param y the y
	 * @param dir the dir
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
	 * Request build street.
	 *
	 * @param x the x
	 * @param y the y
	 * @param dir the dir
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
	 * Request build city.
	 *
	 * @param x the x
	 * @param y the y
	 * @param dir the dir
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
	 * Play invention card.
	 *
	 * @param resource the resource
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
	 * Play monopoly card.
	 *
	 * @param resource the resource
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
	 * Play road card.
	 *
	 * @param road1_id the road 1 id
	 * @param road2_id the road 2 id
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

	/**
	 * Play knight card.
	 *
	 * @param x the x
	 * @param y the y
	 * @param victimID the victim ID
	 */
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

	/**
	 * Send cheat code.
	 *
	 * @param value the value
	 */
	public void sendCheatCode(String value) {
		CheatCode cc = CheatCode.fromString(value);
		ProtocolCheat pc = new ProtocolCheat(cc);
		Response r = new Response();
		r.pCheat = pc;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			logger.trace(Level.ERROR, e);
		}

	}

	/**
	 * Send cheat.
	 *
	 * @param string the string
	 */
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