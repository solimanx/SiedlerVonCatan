package ai;

import java.io.IOException;

import network.InputHandler;
import protocol.clientinstructions.*;
import protocol.clientinstructions.trade.*;
import protocol.configuration.*;
import protocol.connection.*;
import protocol.messaging.*;
import protocol.serverinstructions.*;
import protocol.serverinstructions.trade.*;
import protocol3.clientinstructions.*;
import protocol3.object.*;
import protocol3.severinstructions.*;

public class PrimitiveAIInputHandler extends InputHandler {
	PrimitiveAI ai;

	public PrimitiveAIInputHandler(PrimitiveAI primitiveAI) {
		ai = primitiveAI;
	}

	public void sendToParser(String line) {
		Object object = parser.parseString(line);
		handle(object);

	}

	/**
	 * What to do after receiving hello from server
	 */
	@Override
	protected void handle(ProtocolHello hello) {
		System.out.println(ai.getPROTOCOL().equals(hello.getProtocol()));

		if (ai.getPROTOCOL().equals(hello.getProtocol())) {
			ai.getOutput().respondHello(ai.getVERSION());

		} else {
			throw new IllegalArgumentException("Protocol version mismatch");
		}

	}

	@Override
	protected void handle(ProtocolWelcome welcome) {
		ai.setID(welcome.getPlayer_id());
		ai.getOutput().respondProfile(ai.getColorCounter());
		//ai.getOutput().respondStartGame();

	}

	@Override
	protected void handle(ProtocolClientReady clientReady) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolGameStarted gameStarted) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolError error) {
		ai.setColorCounter(ai.getColorCounter()+1);
		ai.getOutput().respondProfile(ai.getColorCounter());

	}

	@Override
	protected void handle(ProtocolPlayerProfile playerProfile) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolChatReceiveMessage chatReceiveMessage) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolChatSendMessage chatSendMessage) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolServerConfirmation serverConfirmation) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(String string) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolBuild build) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolDiceRollResult diceRollResult) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolResourceObtain resourceObtain) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolStatusUpdate statusUpdate) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolBuildRequest buildRequest) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolDiceRollRequest diceRollRequest) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolEndTurn endTurn) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolVictory victory) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolCosts costs) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolRobberMovement robberMovement) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolRobberLoss robberLoss) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolRobberMovementRequest robberMovementRequest) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolHarbourRequest harbourRequest) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolTradeRequest tradeRequest) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolTradeIsRequested tradeIsRequested) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolTradeAccept tradeAccept) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolTradeConfirmation tradeConfirmation) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolTradeComplete tradeComplete) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolTradeIsCompleted tradeIsCompleted) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolTradeCancel tradeCancel) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolTradeIsCanceled tradeIsCanceled) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolBuyDevelopmentCards buyDevelopmentCards) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolDevelopmentCards developmentCards) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolInventionCard inventionCard) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolMonopolyCard monopolyCard) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolRoadBuildingCard roadBuildingCard) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolBiggestKnightProwess biggestKnightProwess) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolInventionCardInfo inventionCardInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolLongestRoad longestRoad) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolMonopolyCardInfo monopolyCardInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolPlayKnightCard playKnightCard) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolRoadBuildingCardInfo roadBuildingCardInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolBoughtDevelopmentCard boughtDevelopmentCard) {
		// TODO Auto-generated method stub

	}

}
