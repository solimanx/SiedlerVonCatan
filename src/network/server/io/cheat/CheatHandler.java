package network.server.io.cheat;

import java.io.IOException;

import audio.Soundeffects;
import enums.CardType;
import enums.CheatCode;
import enums.PlayerState;
import model.board.PlayerModel;
import network.server.Server;
import network.server.io.ServerOutputHandler;
import parsing.Response;
import protocol.cheats.ProtocolLongestTurn;
import protocol.configuration.ProtocolVictory;
import protocol.messaging.ProtocolChatReceiveMessage;
import protocol.object.ProtocolResource;
import protocol.serverinstructions.ProtocolBoughtDevelopmentCard;
import protocol.serverinstructions.ProtocolDiceRollResult;
import protocol.serverinstructions.ProtocolResourceObtain;

// TODO: Auto-generated Javadoc
// TODO GENERATE VS DECK PROBLEM
public class CheatHandler extends ServerOutputHandler {

	/**
	 * Instantiates a new cheat handler.
	 *
	 * @param server
	 *            the server
	 */
	public CheatHandler(Server server) {
		super(server);
	}

	/**
	 * Handle.
	 *
	 * @param threadID
	 *            the thread ID
	 * @param cc
	 *            the cc
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
		case VICTORY:
			drawVictoryCard(threadID);
			break;
		case ACTIVATE_LT:
			activateLongestTurn(threadID);
			break;
		case DEACTIVATE_LT:
			deactivateLongestTurn(threadID);
			break;
		case SOUNDTRACK_ONE:
			playSoundtrackOne(threadID);
			break;
		case SOUNDTRACK_TWO:
			playSoundtrackTwo(threadID);
		    break;
			
		default:
			throw new IllegalArgumentException("Cheat doesn't exist");

		}
	}

	private void playSoundtrackTwo(Integer threadID) {
		Soundeffects.playSoundtrack2();
		
	}

	private void playSoundtrackOne(Integer threadID) {
		Soundeffects.playSoundtrack1();
		
	}

	private void deactivateLongestTurn(Integer threadID) {
		server.getServerInputHandler().getServerController().setLongestTurnEnabled(false);
		
	}

	private void activateLongestTurn(Integer threadID) {
		server.getServerInputHandler().getServerController().setLongestTurnEnabled(true);

	}
	
	public void longestTurn(Integer threadID){
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		ProtocolLongestTurn plt = new ProtocolLongestTurn(threadID);
		Response r = new Response();
		r.pLongestTurn = plt;
		try {
			server.sendToClient(parser.createString(r), modelID);
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
		}

	}

	private void drawVictoryCard(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		ProtocolBoughtDevelopmentCard pbdc = new ProtocolBoughtDevelopmentCard(threadID, CardType.VICTORYPOINT);
		Response r = new Response();
		r.pBoughtDevelopmentCard = pbdc;
		try {
			server.sendToClient(parser.createString(r), modelID);
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
		}

		
	}

	/**
	 * Street build card.
	 *
	 * @param threadID
	 *            the thread ID
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
	 * @param threadID
	 *            the thread ID
	 */
	// TODO NEEDS TESTING
	private void showOtherHand(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		int resources[][] = new int[server.getConnectedPlayers() - 1][5];
		String names[] = new String[server.getConnectedPlayers() - 1];
		for (int i = 0; i < server.getConnectedPlayers(); i++) {

			// if other players
			if (i != modelID) {
				// get their resource count
				resources[i] = server.getServerInputHandler().getServerController().getBoard().getPlayer(i)
						.getResources();
				names[i] = server.getServerInputHandler().getServerController().getBoard().getPlayer(i).getName();
			}
		}
		// send all of the information to the cheat code inserter.
		for (int i = 0; i < server.getConnectedPlayers() - 1; i++) {
			String msg = "Player " + names[i] + " has: " + "Wood: " + resources[i][0] + ", Clay: " + resources[i][1]
					+ ", Ore: " + resources[i][2] + ", Sheep: " + resources[i][3] + ", Corn: " + resources[i][4];

			ProtocolChatReceiveMessage pcrm = new ProtocolChatReceiveMessage(threadID, msg);
			Response r = new Response();
			r.pChatReceive = pcrm;
			try {
				server.sendToClient((parser.createString(r)), modelID);
			} catch (IOException e) {
				logger.error(e);
			}
		}

	}

	/**
	 * Draw monopoly card.
	 *
	 * @param threadID
	 *            the thread ID
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
	 * @param threadID
	 *            the thread ID
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
	 * @param threadID
	 *            the thread ID
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
	 * @param threadID
	 *            the thread ID
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
	 * @param threadID
	 *            the thread ID
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
	 * @param threadID
	 *            the thread ID
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
	 * @param threadID
	 *            the thread ID
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
	 * @param threadID
	 *            the thread ID
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
	 * @param threadID
	 *            the thread ID
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
	 * CHeck if player is in dice roll state.
	 *
	 * @param modelID the model ID
	 * @return true, if successful
	 */
	private boolean playerRollStateCheck(Integer modelID) {
		if (server.getServerInputHandler().getServerController().getBoard().getPlayer(modelID).getPlayerState()
				.equals(PlayerState.DICEROLLING)) {
			return true;
		}
		return false;
	}

	/**
	 * Dice roll consequences depending on number.
	 *
	 * @param i the i
	 * @param modelID the model ID
	 */
	private void diceCheats(int i, int modelID) {
		if (i == 7) {
			// server.getServerInputHandler().getServerController().
			PlayerModel pM = server.getServerInputHandler().getServerController().getBoard().getPlayer(modelID);
			PlayerModel currPM;
			for (int j = 0; j < server.getServerInputHandler().getServerController().getAmountPlayers(); j++) {
				currPM = server.getServerInputHandler().getServerController().getBoard().getPlayer(i);
				if (currPM.sumResources() > 7) {
					currPM.setPlayerState(PlayerState.DISPENSE_CARDS_ROBBER_LOSS);
					server.getServerInputHandler().getServerController().statusUpdate(j);
					server.getServerInputHandler().getServerController().setRobberLossCounter(
							server.getServerInputHandler().getServerController().getRobberLossCounter() + 1);
				}
			}
			if (server.getServerInputHandler().getServerController().getRobberLossCounter() == 0) {
				pM.setPlayerState(PlayerState.MOVE_ROBBER);
				server.getServerInputHandler().getServerController().statusUpdate(modelID);
			} else {
				if (pM.getPlayerState() != PlayerState.DISPENSE_CARDS_ROBBER_LOSS) {
					pM.setPlayerState(PlayerState.WAITING);
					server.getServerInputHandler().getServerController().statusUpdate(modelID);
				}
			}
		} else {
			server.getServerInputHandler().getServerController().gainBoardResources(i);
			server.getServerInputHandler().getServerController().getBoard().getPlayer(modelID)
					.setPlayerState(PlayerState.TRADING_OR_BUILDING);
		}
		server.getServerInputHandler().getServerController().statusUpdate(modelID);
	}

	/**
	 * Roll two cheat.
	 *
	 * @param threadID
	 *            the thread ID
	 */
	private void rollTwoCheat(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		if (playerRollStateCheck(modelID)) {
			diceCheats(2, modelID);
			int[] result = { 1, 1 };
			ProtocolDiceRollResult dr = new ProtocolDiceRollResult(threadID, result);
			Response r = new Response();
			r.pDRResult = dr;
			try {
				server.broadcast(parser.createString(r));
			} catch (IOException e) {
				logger.error(e);
			}
		} else {

			server.getServerInputHandler().getServerController().serverResponse(modelID,
					"Diese Aktion ist derzeit nicht zulässig");

		}

	}

	/**
	 * Roll three cheat.
	 *
	 * @param threadID
	 *            the thread ID
	 */
	private void rollThreeCheat(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		if (playerRollStateCheck(modelID)) {
			diceCheats(3, modelID);
			int[] result = { 2, 1 };
			ProtocolDiceRollResult dr = new ProtocolDiceRollResult(threadID, result);
			Response r = new Response();
			r.pDRResult = dr;
			try {
				server.broadcast(parser.createString(r));
			} catch (IOException e) {
				logger.error(e);
			}
		} else {

			server.getServerInputHandler().getServerController().serverResponse(modelID,
					"Diese Aktion ist derzeit nicht zulässig");

		}

	}

	/**
	 * Roll four cheat.
	 *
	 * @param threadID
	 *            the thread ID
	 */
	private void rollFourCheat(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		if (playerRollStateCheck(modelID)) {
			diceCheats(4, modelID);
			int[] result = { 2, 2 };
			ProtocolDiceRollResult dr = new ProtocolDiceRollResult(threadID, result);
			Response r = new Response();
			r.pDRResult = dr;
			try {
				server.broadcast(parser.createString(r));
			} catch (IOException e) {
				logger.error(e);
			}
		} else {

			server.getServerInputHandler().getServerController().serverResponse(modelID,
					"Diese Aktion ist derzeit nicht zulässig");

		}

	}

	/**
	 * Roll five cheat.
	 *
	 * @param threadID
	 *            the thread ID
	 */
	private void rollFiveCheat(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		if (playerRollStateCheck(modelID)) {
			diceCheats(5, modelID);
			int[] result = { 3, 2 };
			ProtocolDiceRollResult dr = new ProtocolDiceRollResult(threadID, result);
			Response r = new Response();
			r.pDRResult = dr;
			try {
				server.broadcast(parser.createString(r));
			} catch (IOException e) {
				logger.error(e);
			}
		} else {

			server.getServerInputHandler().getServerController().serverResponse(modelID,
					"Diese Aktion ist derzeit nicht zulässig");

		}

	}

	/**
	 * Roll six cheat.
	 *
	 * @param threadID
	 *            the thread ID
	 */
	private void rollSixCheat(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		if (playerRollStateCheck(modelID)) {
			diceCheats(6, modelID);
			int[] result = { 4, 2 };
			ProtocolDiceRollResult dr = new ProtocolDiceRollResult(threadID, result);
			Response r = new Response();
			r.pDRResult = dr;
			try {
				server.broadcast(parser.createString(r));
			} catch (IOException e) {
				logger.error(e);
			}
		} else {

			server.getServerInputHandler().getServerController().serverResponse(modelID,
					"Diese Aktion ist derzeit nicht zulässig");

		}

	}

	/**
	 * Roll seven cheat.
	 *
	 * @param threadID
	 *            the thread ID
	 */
	private void rollSevenCheat(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		if (playerRollStateCheck(modelID)) {
			diceCheats(7, modelID);
			int[] result = { 4, 3 };
			ProtocolDiceRollResult dr = new ProtocolDiceRollResult(threadID, result);
			Response r = new Response();
			r.pDRResult = dr;
			try {
				server.broadcast(parser.createString(r));
			} catch (IOException e) {
				logger.error(e);
			}
		} else {

			server.getServerInputHandler().getServerController().serverResponse(modelID,
					"Diese Aktion ist derzeit nicht zulässig");

		}

	}

	/**
	 * Roll eight cheat.
	 *
	 * @param threadID
	 *            the thread ID
	 */
	private void rollEightCheat(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		if (playerRollStateCheck(modelID)) {
			diceCheats(8, modelID);
			int[] result = { 4, 4 };
			ProtocolDiceRollResult dr = new ProtocolDiceRollResult(threadID, result);
			Response r = new Response();
			r.pDRResult = dr;
			try {
				server.broadcast(parser.createString(r));
			} catch (IOException e) {
				logger.error(e);
			}
		} else {

			server.getServerInputHandler().getServerController().serverResponse(modelID,
					"Diese Aktion ist derzeit nicht zulässig");

		}

	}

	/**
	 * Roll nine cheat.
	 *
	 * @param threadID
	 *            the thread ID
	 */
	private void rollNineCheat(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		if (playerRollStateCheck(modelID)) {
			diceCheats(9, modelID);
			int[] result = { 4, 5 };
			ProtocolDiceRollResult dr = new ProtocolDiceRollResult(threadID, result);
			Response r = new Response();
			r.pDRResult = dr;
			try {
				server.broadcast(parser.createString(r));
			} catch (IOException e) {
				logger.error(e);
			}
		} else {

			server.getServerInputHandler().getServerController().serverResponse(modelID,
					"Diese Aktion ist derzeit nicht zulässig");

		}

	}

	/**
	 * Roll ten cheat.
	 *
	 * @param threadID
	 *            the thread ID
	 */
	private void rollTenCheat(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		if (playerRollStateCheck(modelID)) {
			diceCheats(10, modelID);
			int[] result = { 5, 5 };
			ProtocolDiceRollResult dr = new ProtocolDiceRollResult(threadID, result);
			Response r = new Response();
			r.pDRResult = dr;
			try {
				server.broadcast(parser.createString(r));
			} catch (IOException e) {
				logger.error(e);
			}
		} else {

			server.getServerInputHandler().getServerController().serverResponse(modelID,
					"Diese Aktion ist derzeit nicht zulässig");

		}

	}

	/**
	 * Roll eleven cheat.
	 *
	 * @param threadID
	 *            the thread ID
	 */
	private void rollElevenCheat(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		if (playerRollStateCheck(modelID)) {
			diceCheats(11, modelID);
			int[] result = { 5, 6 };
			ProtocolDiceRollResult dr = new ProtocolDiceRollResult(threadID, result);
			Response r = new Response();
			r.pDRResult = dr;
			try {
				server.broadcast(parser.createString(r));
			} catch (IOException e) {
				logger.error(e);
			}
		} else {

			server.getServerInputHandler().getServerController().serverResponse(modelID,
					"Diese Aktion ist derzeit nicht zulässig");

		}

	}

	/**
	 * Roll twelve cheat.
	 *
	 * @param threadID
	 *            the thread ID
	 */
	private void rollTwelveCheat(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		if (playerRollStateCheck(modelID)) {
			diceCheats(12, modelID);
			int[] result = { 6, 6 };
			ProtocolDiceRollResult dr = new ProtocolDiceRollResult(threadID, result);
			Response r = new Response();
			r.pDRResult = dr;
			try {
				server.broadcast(parser.createString(r));
			} catch (IOException e) {
				logger.error(e);
			}
		} else {

			server.getServerInputHandler().getServerController().serverResponse(modelID,
					"Diese Aktion ist derzeit nicht zulässig");

		}

	}

}
