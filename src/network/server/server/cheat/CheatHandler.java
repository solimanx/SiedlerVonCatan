package network.server.server.cheat;

import java.io.IOException;

import enums.CardType;
import enums.CheatCode;
import network.server.server.Server;
import network.server.server.ServerOutputHandler;
import parsing.Response;
import protocol.serverinstructions.ProtocolBoughtDevelopmentCard;

public class CheatHandler extends ServerOutputHandler{
	
	
	
	public CheatHandler(Server server) {
		super(server);
	}

	public void handle(Integer id, CheatCode cc){
		switch(cc){
		case DICEROLL_10: rollTenCheat(id); break;
		case DICEROLL_11: rollElevenCheat(id); break;
		case DICEROLL_12: rollTwelveCheat(id);break;
		case DICEROLL_9: rollNineCheat(id);break;
		case DICEROLL_8: rollEightCheat(id);break;
		case DICEROLL_7: rollSevenCheat(id);break;
		case DICEROLL_6: rollSixCheat(id);break;
		case DICEROLL_5: rollFiveCheat(id);break;
		case DICEROLL_4: rollFourCheat(id);break;
		case DICEROLL_3: rollThreeCheat(id);break;
		case DICEROLL_2: rollTwoCheat(id);break;
		case INCREASE_ELEMENTFIVE: increaseElementFive(id);break;
		case INCREASE_ELEMENTFOUR: increaseElementFour(id);break;
		case INCREASE_ELEMENTONE: increaseElementOne(id);break;
		case INCREASE_ELEMENTTHREE: increaseElementThree(id);break;
		case INCREASE_ELEMENTTWO: increaseElementTwo(id);break;
		case INSTANT_WIN: instantWin(id);break;
		case INVENTION_CARD: drawInventionCard(id);break;
		case KNIGHT_CARD: drawKnightCard(id);break;
		case MONOPOLY_CARD: drawMonopolyCard(id);break;
		case OTHER_HAND: showOtherHand(id);break;
		case STREET_BUILD_CARD: streetBuildCard(id);break;
		default: throw new IllegalArgumentException("Cheat doesn't exist");
	
		}}

	private void streetBuildCard(Integer threadID) {
		int modelID = server.getServerInputHandler().getServerController().getThreadPlayerIdMap().get(threadID);
		ProtocolBoughtDevelopmentCard pbdc = new ProtocolBoughtDevelopmentCard(threadID, CardType.STREET);
		Response r = new Response();
		r.pBoughtDevelopmentCard = pbdc;
		try {
			server.sendToClient(parser.createString(r),modelID);
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}
		
	}

	private static void showOtherHand(Integer id) {
		// TODO Auto-generated method stub
		
	}

	private static void drawMonopolyCard(Integer id) {
		// TODO Auto-generated method stub
		
	}

	private static void drawKnightCard(Integer id) {
		// TODO Auto-generated method stub
		
	}

	private static void drawInventionCard(Integer id) {
		// TODO Auto-generated method stub
		
	}

	private static void instantWin(Integer id) {
		// TODO Auto-generated method stub
		
	}

	private static void increaseElementTwo(Integer id) {
		// TODO Auto-generated method stub
		
	}

	private static void increaseElementThree(Integer id) {
		// TODO Auto-generated method stub
		
	}

	private static void increaseElementOne(Integer id) {
		// TODO Auto-generated method stub
		
	}

	private static void increaseElementFour(Integer id) {
		// TODO Auto-generated method stub
		
	}

	private static void increaseElementFive(Integer id) {
		// TODO Auto-generated method stub
		
	}

	private static void rollTwoCheat(Integer id) {
		// TODO Auto-generated method stub
		
	}

	private static void rollThreeCheat(Integer id) {
		// TODO Auto-generated method stub
		
	}

	private static void rollFourCheat(Integer id) {
		// TODO Auto-generated method stub
		
	}

	private static void rollFiveCheat(Integer id) {
		// TODO Auto-generated method stub
		
	}

	private static void rollSixCheat(Integer id) {
		// TODO Auto-generated method stub
		
	}

	private static void rollSevenCheat(Integer id) {
		// TODO Auto-generated method stub
		
	}

	private static void rollEightCheat(Integer id) {
		// TODO Auto-generated method stub
		
	}

	private static void rollNineCheat(Integer id) {
		// TODO Auto-generated method stub
		
	}

	private static void rollTwelveCheat(Integer id) {
		// TODO Auto-generated method stub
		
	}

	private static void rollElevenCheat(Integer id) {
		// TODO Auto-generated method stub
		
	}

	private static void rollTenCheat(Integer id) {
		// TODO Auto-generated method stub
		
	}
}
