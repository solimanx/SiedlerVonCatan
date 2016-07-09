package network.server.server.cheat;

import java.io.IOException;

import enums.CardType;
import enums.CheatCode;
import network.ModelToProtocol;
import network.server.server.Server;
import network.server.server.ServerOutputHandler;
import parsing.Response;
import protocol.configuration.ProtocolVictory;
import protocol.object.ProtocolResource;
import protocol.serverinstructions.ProtocolBoughtDevelopmentCard;
import protocol.serverinstructions.ProtocolDiceRollResult;
import protocol.serverinstructions.ProtocolResourceObtain;

// TODO: Auto-generated Javadoc
public class CheatHandler extends ServerOutputHandler {

	/**
	 * Instantiates a new cheat handler.
	 *
	 * @param server the server
	 */
	public CheatHandler(Server server) {
		super(server);
	}

	/**
	 * Handle.
	 *
	 * @param threadID the thread ID
	 * @param cc the cc
	 */
	public void handle(Integer threadID, CheatCode cc) {
		switch (cc) {
		case DICEROLL_10:
			rollTenCheat(threadID);
			break;
		case DICEROLL_11:
			rollElevenCheat(threadID);
			break;
		case DICEROLL_12:
			rollTwelveCheat(threadID);
			break;
		case DICEROLL_9:
			rollNineCheat(threadID);
			break;
		case DICEROLL_8:
			rollEightCheat(threadID);
			break;
		case DICEROLL_7:
			rollSevenCheat(threadID);
			break;
		case DICEROLL_6:
			rollSixCheat(threadID);
			break;
		case DICEROLL_5:
			rollFiveCheat(threadID);
			break;
		case DICEROLL_4:
			rollFourCheat(threadID);
			break;
		case DICEROLL_3:
			rollThreeCheat(threadID);
			break;
		case DICEROLL_2:
			rollTwoCheat(threadID);
			break;
		case INCREASE_ELEMENTFIVE:
			increaseElementFive(threadID);
			break;
		case INCREASE_ELEMENTFOUR:
			increaseElementFour(threadID);
			break;
		case INCREASE_ELEMENTONE:
			increaseElementOne(threadID);
			break;
		case INCREASE_ELEMENTTHREE:
			increaseElementThree(threadID);
			break;
		case INCREASE_ELEMENTTWO:
			increaseElementTwo(threadID);
			break;
		case INSTANT_WIN:
			instantWin(threadID);
			break;
		case INVENTION_CARD:
			drawInventionCard(threadID);
			break;
		case KNIGHT_CARD:
			drawKnightCard(threadID);
			break;
		case MONOPOLY_CARD:
			drawMonopolyCard(threadID);
			break;
		case OTHER_HAND:
			showOtherHand(threadID);
			break;
		case STREET_BUILD_CARD:
			streetBuildCard(threadID);
			break;
		default:
			throw new IllegalArgumentException("Cheat doesn't exist");

		}
	}

	/**
	 * Street build card.
	 *
	 * @param threadID the thread ID
	 */
	private void streetBuildCard(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		ProtocolBoughtDevelopmentCard pbdc = new ProtocolBoughtDevelopmentCard(threadID, CardType.STREET);
		Response r = new Response();
		r.pBoughtDevelopmentCard = pbdc;
		try {
			server.sendToClient(parser.createString(r), modelID);
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
		}

	}

	/**
	 * Show other hand.
	 *
	 * @param threadID the thread ID
	 */
	private void showOtherHand(Integer threadID) {
		// TODO Auto-generated method stub

	}

	/**
	 * Draw monopoly card.
	 *
	 * @param threadID the thread ID
	 */
	private void drawMonopolyCard(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		ProtocolBoughtDevelopmentCard pbdc = new ProtocolBoughtDevelopmentCard(threadID, CardType.MONOPOLY);
		Response r = new Response();
		r.pBoughtDevelopmentCard = pbdc;
		try {
			server.sendToClient(parser.createString(r), modelID);
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
		}

	}

	/**
	 * Draw knight card.
	 *
	 * @param threadID the thread ID
	 */
	private void drawKnightCard(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		ProtocolBoughtDevelopmentCard pbdc = new ProtocolBoughtDevelopmentCard(threadID, CardType.KNIGHT);
		Response r = new Response();
		r.pBoughtDevelopmentCard = pbdc;
		try {
			server.sendToClient(parser.createString(r), modelID);
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
		}

	}

	/**
	 * Draw invention card.
	 *
	 * @param threadID the thread ID
	 */
	private void drawInventionCard(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		ProtocolBoughtDevelopmentCard pbdc = new ProtocolBoughtDevelopmentCard(threadID, CardType.INVENTION);
		Response r = new Response();
		r.pBoughtDevelopmentCard = pbdc;
		try {
			server.sendToClient(parser.createString(r), modelID);
			// TODO tell others?
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
		}
	}

	/**
	 * Instant win.
	 *
	 * @param threadID the thread ID
	 */
	private void instantWin(Integer threadID) {
		ProtocolVictory pv = new ProtocolVictory("You have won!", threadID);
		Response r = new Response();
		r.pVictory = pv;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
		}

	}

	/**
	 * Increase element one.
	 *
	 * @param threadID the thread ID
	 */
	// Amount of Landscape Resource Cards: {WOOD, CLAY, ORE, SHEEP, CORN}
	private void increaseElementOne(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		ProtocolResource pResource = new ProtocolResource(1, null, null, null, null, null);
		ProtocolResourceObtain po = new ProtocolResourceObtain(threadID, pResource);
		Response r = new Response();
		r.pRObtain = po;
		try {
			server.sendToClient(parser.createString(r), modelID);
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
		}
	}

	/**
	 * Increase element two.
	 *
	 * @param threadID the thread ID
	 */
	private void increaseElementTwo(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		ProtocolResource pResource = new ProtocolResource(null, 1, null, null, null, null);
		ProtocolResourceObtain po = new ProtocolResourceObtain(threadID, pResource);
		Response r = new Response();
		r.pRObtain = po;
		try {
			server.sendToClient(parser.createString(r), modelID);
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
		}

	}

	/**
	 * Increase element three.
	 *
	 * @param threadID the thread ID
	 */
	private void increaseElementThree(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		ProtocolResource pResource = new ProtocolResource(null, null, 1, null, null, null);
		ProtocolResourceObtain po = new ProtocolResourceObtain(threadID, pResource);
		Response r = new Response();
		r.pRObtain = po;
		try {
			server.sendToClient(parser.createString(r), modelID);
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
		}
	}

	/**
	 * Increase element four.
	 *
	 * @param threadID the thread ID
	 */
	private void increaseElementFour(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		ProtocolResource pResource = new ProtocolResource(null, null, null, 1, null, null);
		ProtocolResourceObtain po = new ProtocolResourceObtain(threadID, pResource);
		Response r = new Response();
		r.pRObtain = po;
		try {
			server.sendToClient(parser.createString(r), modelID);
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
		}

	}

	/**
	 * Increase element five.
	 *
	 * @param threadID the thread ID
	 */
	private void increaseElementFive(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		ProtocolResource pResource = new ProtocolResource(null, null, null, null, 1, null);
		ProtocolResourceObtain po = new ProtocolResourceObtain(threadID, pResource);
		Response r = new Response();
		r.pRObtain = po;
		try {
			server.sendToClient(parser.createString(r), modelID);
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
		}

	}

	/**
	 * Roll two cheat.
	 *
	 * @param threadID the thread ID
	 */
	private void rollTwoCheat(Integer threadID) {
		int[] result = { 1, 1 };
		ProtocolDiceRollResult dr = new ProtocolDiceRollResult(threadID, result);
		Response r = new Response();
		r.pDRResult = dr;
		try {
			server.broadcast(parser.createString(r));
			// TODO change status from roll dice to trading/building
		} catch (IOException e) {
			logger.error(e);
		}

	}

	/**
	 * Roll three cheat.
	 *
	 * @param threadID the thread ID
	 */
	private void rollThreeCheat(Integer threadID) {
		int[] result = { 1, 2 };
		ProtocolDiceRollResult dr = new ProtocolDiceRollResult(threadID, result);
		Response r = new Response();
		r.pDRResult = dr;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error(e);
		}

	}

	/**
	 * Roll four cheat.
	 *
	 * @param threadID the thread ID
	 */
	private void rollFourCheat(Integer threadID) {
		int[] result = { 2, 2 };
		ProtocolDiceRollResult dr = new ProtocolDiceRollResult(threadID, result);
		Response r = new Response();
		r.pDRResult = dr;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error(e);
		}

	}

	/**
	 * Roll five cheat.
	 *
	 * @param threadID the thread ID
	 */
	private void rollFiveCheat(Integer threadID) {
		int[] result = { 2, 3 };
		ProtocolDiceRollResult dr = new ProtocolDiceRollResult(threadID, result);
		Response r = new Response();
		r.pDRResult = dr;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error(e);
		}

	}

	/**
	 * Roll six cheat.
	 *
	 * @param threadID the thread ID
	 */
	private void rollSixCheat(Integer threadID) {
		int[] result = { 3, 3 };
		ProtocolDiceRollResult dr = new ProtocolDiceRollResult(threadID, result);
		Response r = new Response();
		r.pDRResult = dr;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error(e);
		}

	}

	/**
	 * Roll seven cheat.
	 *
	 * @param threadID the thread ID
	 */
	private void rollSevenCheat(Integer threadID) {
		int[] result = { 4, 3 };
		ProtocolDiceRollResult dr = new ProtocolDiceRollResult(threadID, result);
		Response r = new Response();
		r.pDRResult = dr;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error(e);
		}

	}

	/**
	 * Roll eight cheat.
	 *
	 * @param threadID the thread ID
	 */
	private void rollEightCheat(Integer threadID) {
		int[] result = { 4, 4 };
		ProtocolDiceRollResult dr = new ProtocolDiceRollResult(threadID, result);
		Response r = new Response();
		r.pDRResult = dr;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error(e);
		}

	}

	/**
	 * Roll nine cheat.
	 *
	 * @param threadID the thread ID
	 */
	private void rollNineCheat(Integer threadID) {
		int[] result = { 4, 5 };
		ProtocolDiceRollResult dr = new ProtocolDiceRollResult(threadID, result);
		Response r = new Response();
		r.pDRResult = dr;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error(e);
		}

	}

	/**
	 * Roll ten cheat.
	 *
	 * @param threadID the thread ID
	 */
	private void rollTenCheat(Integer threadID) {
		int[] result = { 4, 6 };
		ProtocolDiceRollResult dr = new ProtocolDiceRollResult(threadID, result);
		Response r = new Response();
		r.pDRResult = dr;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error(e);
		}

	}

	/**
	 * Roll eleven cheat.
	 *
	 * @param threadID the thread ID
	 */
	private void rollElevenCheat(Integer threadID) {
		int[] result = { 5, 6 };
		ProtocolDiceRollResult dr = new ProtocolDiceRollResult(threadID, result);
		Response r = new Response();
		r.pDRResult = dr;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error(e);
		}

	}

	/**
	 * Roll twelve cheat.
	 *
	 * @param threadID the thread ID
	 */
	private void rollTwelveCheat(Integer threadID) {
		int[] result = { 6, 6 };
		ProtocolDiceRollResult dr = new ProtocolDiceRollResult(threadID, result);
		Response r = new Response();
		r.pDRResult = dr;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error(e);
		}

	}

}
