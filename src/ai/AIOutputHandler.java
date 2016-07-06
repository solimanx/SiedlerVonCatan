/*
 *
 */
package ai;

import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import enums.Color;
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
import protocol.clientinstructions.ProtocolRobberLoss;
import protocol.clientinstructions.ProtocolRobberMovementRequest;
import protocol.clientinstructions.trade.ProtocolTradeRequest;
import protocol.configuration.ProtocolClientReady;
import protocol.configuration.ProtocolPlayerProfile;
import protocol.connection.ProtocolHello;
import protocol.dualinstructions.ProtocolPlayKnightCard;
import protocol.dualinstructions.ProtocolPlayMonopolyCard;
import protocol.dualinstructions.ProtocolPlayRoadCard;
import protocol.messaging.ProtocolChatSendMessage;
import protocol.object.ProtocolResource;

// TODO: Auto-generated Javadoc
public class AIOutputHandler {
	private static Logger logger = LogManager.getLogger(AIOutputHandler.class.getSimpleName());
	private PrimitiveAI ai;
	private Parser parser;

	/**
	 * Instantiates a new AI output handler.
	 *
	 * @param primitiveAI
	 *            the primitive AI
	 */
	public AIOutputHandler(PrimitiveAI primitiveAI) {
		ai = primitiveAI;
		parser = new Parser();
	}

	/**
	 * Say hello to server.
	 *
	 * @param version
	 *            the version
	 */
	protected void respondHello(String version) {
		ProtocolHello ph = new ProtocolHello(version, null);
		Response r = new Response();
		r.pHello = ph;
		try {
			ai.write(parser.createString(r));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * Tell the server it's ready to begin the game.
	 */
	protected void respondStartGame() {
		ProtocolClientReady pcr = new ProtocolClientReady();
		Response r = new Response();
		r.pClientReady = pcr;
		try {
			ai.write(parser.createString(r));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}
	}

	/**
	 * Goes on a loop to find the first non taken color.
	 *
	 * @param colorCounter
	 *            the color counter
	 */
	public void respondProfile(int colorCounter) {
		ProtocolPlayerProfile ppp;
		switch (colorCounter) {
		case 0:
			ppp = new ProtocolPlayerProfile("BlueBro", Color.BLUE);
			break;
		case 1:
			ppp = new ProtocolPlayerProfile("RedBro", Color.RED);
			break;
		case 2:
			ppp = new ProtocolPlayerProfile("Orange Destroyer", Color.ORANGE);
			break;
		case 3:
			ppp = new ProtocolPlayerProfile("Walter White", Color.WHITE);
			break;
		default:
			ppp = null;
		}
		Response r = new Response();
		r.pPlayerProfile = ppp;
		try {
			ai.write(parser.createString(r));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * Building the first (and second) village of the initial round.
	 *
	 * @param j
	 *            the j
	 * @param i
	 *            the i
	 * @param k
	 *            the k
	 */
	public void requestBuildVillage(int j, int i, int k) {
		String location = ModelToProtocol.getCornerID(j, i, k);
		Index[] locationIndex = ModelToProtocol.convertCornerIndex(location);
		ProtocolBuildRequest pbr = new ProtocolBuildRequest("Dorf", locationIndex);

		Response r = new Response();
		r.pBuildRequest = pbr;
		try {
			ai.write(parser.createString(r));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * Building the first (and second) road of the initial round.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param dir
	 *            the dir
	 */

	public void requestBuildRoad(int x, int y, int dir) {
		String location = ModelToProtocol.getEdgeID(x, y, dir);
		Index[] locationIndex = ModelToProtocol.convertToEdgeIndex(location);
		ProtocolBuildRequest pbr = new ProtocolBuildRequest("Straße", locationIndex);

		Response r = new Response();
		r.pBuildRequest = pbr;
		try {
			ai.write(parser.createString(r));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * Roll the dice.
	 */
	public void respondDiceRoll() {
		ProtocolDiceRollRequest pdrr = new ProtocolDiceRollRequest();
		Response r = new Response();

		r.pDiceRollRequest = pdrr;
		try {
			ai.write(parser.createString(r));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}
	}

	/**
	 * Send a message to others.
	 *
	 * @param message
	 *            random message
	 */
	public void respondWithMessage(String message) {
		ProtocolChatSendMessage pcsm = new ProtocolChatSendMessage(message);
		Response r = new Response();

		r.pChatSend = pcsm;
		try {
			ai.write(parser.createString(r));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	/**
	 * End my turn.
	 */
	public void respondEndTurn() {
		ai.updateCards();
		ProtocolEndTurn pet = new ProtocolEndTurn();
		Response r = new Response();

		r.pEndTurn = pet;
		try {
			ai.write(parser.createString(r));
		} catch (IOException e) {
			logger.trace(Level.ERROR, e);
		}

	}

	/**
	 * Lose resources to robber.
	 *
	 * @param losses
	 *            the losses
	 */
	public void respondRobberLoss(int[] losses) {
		ProtocolResource loss = ModelToProtocol.convertToProtocolResource(losses);
		ProtocolRobberLoss prl = new ProtocolRobberLoss(loss);
		Response r = new Response();

		r.pRobberLoss = prl;
		try {
			ai.write(parser.createString(r));
		} catch (IOException e) {
			logger.trace(Level.ERROR, e);
		}

	}

	/**
	 * Move robber to a new position.
	 *
	 * @param newRobber
	 *            the new robber
	 * @param target
	 *            the target
	 */
	public void respondMoveRobber(String newRobber, Integer target) {
		Index newRobberIndex = ProtocolToModel.getProtocolOneIndex(newRobber);
		ProtocolRobberMovementRequest prmr = new ProtocolRobberMovementRequest(newRobberIndex, target);
		Response r = new Response();

		r.pRobberMoveRequest = prmr;
		try {
			ai.write(parser.createString(r));
		} catch (IOException e) {
			logger.trace(Level.ERROR, e);
		}

	}

	/**
	 * Request buy card.
	 */
	public void requestBuyCard() {
		ProtocolBuyDevCard pbdc = new ProtocolBuyDevCard();
		Response r = new Response();
		r.pBuyDevCards = pbdc;
		try {
			ai.write(parser.createString(r));
		} catch (IOException e) {
			logger.trace(Level.ERROR, e);

		}

	}

	/**
	 * Request build city.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param dir
	 *            the dir
	 */
	public void requestBuildCity(int x, int y, int dir) {
		String locationString = ModelToProtocol.getCornerID(x, y, dir);
		Index[] location = ModelToProtocol.convertCornerIndex(locationString);
		ProtocolBuildRequest pbr = new ProtocolBuildRequest("Stadt", location);

		Response r = new Response();
		r.pBuildRequest = pbr;
		try {
			ai.write(parser.createString(r));
		} catch (IOException e) {
			logger.trace(Level.ERROR, e);
		}

	}

	/**
	 * Respond knight card.
	 *
	 * @param newRobber
	 *            the new robber
	 * @param target
	 *            the target
	 */
	public void respondKnightCard(String newRobber, Integer target) {
		Index i = ProtocolToModel.getProtocolOneIndex(newRobber);
		ProtocolPlayKnightCard pkc = new ProtocolPlayKnightCard(i, target);
		Response r = new Response();
		r.pPlayKnightCard = pkc;
		try {
			ai.write(parser.createString(r));
		} catch (IOException e) {
			logger.trace(Level.ERROR, e);
		}

	}

	/**
	 * Request trade.
	 *
	 * @param offer
	 *            the offer
	 * @param demand
	 *            the demand
	 */
	public void requestTrade(int[] offer, int[] demand) {
		ProtocolResource prOff = ModelToProtocol.convertToProtocolResource(offer);
		ProtocolResource prDem = ModelToProtocol.convertToProtocolResource(demand);
		ProtocolTradeRequest ptr = new ProtocolTradeRequest(prOff, prDem);
		Response r = new Response();
		r.pTradeRequest = ptr;
		try {
			ai.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw an Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}
	}

	public void requestPlayStreetCard(int[] coords1, int[] coords2) {
		Index[] road1 = ModelToProtocol
				.convertToEdgeIndex(ModelToProtocol.getEdgeID(coords1[0], coords1[1], coords1[2]));
		Index[] road2 = coords2 == null ? null
				: ModelToProtocol.convertToEdgeIndex(ModelToProtocol.getEdgeID(coords2[0], coords2[1], coords2[2]));

		ProtocolPlayRoadCard prbci = new ProtocolPlayRoadCard(road1, road2);
		Response r = new Response();
		r.pPlayRoadCard = prbci;
		try {
			ai.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw an Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}

	public void requestPlayMonopolyCard(ResourceType rt) {
		ProtocolPlayMonopolyCard pmci = new ProtocolPlayMonopolyCard(rt);
		Response r = new Response();
		r.pPlayMonopolyCard = pmci;
		try {
			ai.write(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw an Input/Output Exception ", e);
			logger.catching(Level.ERROR, e);
			e.printStackTrace();
		}

	}
}
