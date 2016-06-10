package network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parsing.Parser;
import protocol.clientinstructions.*;
import protocol.clientinstructions.trade.ProtocolTradeAccept;
import protocol.clientinstructions.trade.ProtocolTradeCancel;
import protocol.clientinstructions.trade.ProtocolTradeComplete;
import protocol.clientinstructions.trade.ProtocolTradeRequest;
import protocol.configuration.*;
import protocol.connection.ProtocolHello;
import protocol.connection.ProtocolWelcome;
import protocol.messaging.ProtocolChatReceiveMessage;
import protocol.messaging.ProtocolChatSendMessage;
import protocol.messaging.ProtocolServerConfirmation;
import protocol.serverinstructions.*;
import protocol.serverinstructions.trade.ProtocolTradeConfirmation;
import protocol.serverinstructions.trade.ProtocolTradeIsCanceled;
import protocol.serverinstructions.trade.ProtocolTradeIsCompleted;
import protocol.serverinstructions.trade.ProtocolTradeIsRequested;
import protocol3.clientinstructions.ProtocolBuyDevelopmentCards;
import protocol3.clientinstructions.ProtocolDevelopmentCards;
import protocol3.object.ProtocolInventionCard;
import protocol3.object.ProtocolMonopolyCard;
import protocol3.object.ProtocolRoadBuildingCard;
import protocol3.severinstructions.*;

public abstract class InputHandler {
	private static Logger logger = LogManager.getLogger(InputHandler.class.getName());
	protected Parser parser;

	public InputHandler() {
		parser = new Parser();
	}

	// public void sendToParser();

	public Parser getParser() {
		return parser;
	}

	/**
	 * takes parsed object and redirects to its proper method depending on
	 * objects "real" class. ( object will be casted to "real" class )
	 *
	 * @param o
	 */
	protected void handle(Object o) {
		switch (o.getClass().getSimpleName()) {
		case "ProtocolHello":
			handle((ProtocolHello) o);
			break;
		case "ProtocolWelcome":
			handle((ProtocolWelcome) o);
			break;
		case "ProtocolClientReady":
			handle((ProtocolClientReady) o);
			break;
		case "ProtocolError":
			handle((ProtocolError) o);
			break;
		case "ProtocolGameStarted":
			handle((ProtocolGameStarted) o);
			break;
		case "ProtocolPlayerProfile":
			handle((ProtocolPlayerProfile) o);
			break;
		case "ProtocolChatReceiveMessage":
			handle((ProtocolChatReceiveMessage) o);
			break;
		case "ProtocolChatSendMessage":
			handle((ProtocolChatSendMessage) o);
			break;
		case "ProtocolServerConfirmation":
			handle((ProtocolServerConfirmation) o);
			break;
		case "ProtocolBuild":
			handle((ProtocolBuild) o);
			break;
		case "ProtocolDiceRollResult":
			handle((ProtocolDiceRollResult) o);
			break;
		case "ProtocolResourceObtain":
			handle((ProtocolResourceObtain) o);
			break;
		case "ProtocolStatusUpdate":
			handle((ProtocolStatusUpdate) o);
			break;
		case "ProtocolBuildRequest":
			handle((ProtocolBuildRequest) o);
			break;
		case "ProtocolDiceRollRequest":
			handle((ProtocolDiceRollRequest) o);
			break;
		case "ProtocolEndTurn":
			handle((ProtocolEndTurn) o);
			break;
		case "ProtocolRobberMovement": // 0.2
			handle((ProtocolRobberMovement) o);
			break;
		case "ProtocolRobberMovementRequest": // 0.2
			handle((ProtocolRobberMovementRequest) o);
			break;

		case "String":
			handle((String) o);
			break;
		case "ProtocolVictory":
			handle((ProtocolVictory) o);
			break;
		case "ProtocolCosts":
			handle((ProtocolCosts) o);
			break;
		case "ProtocolRobberLoss":
			handle((ProtocolRobberLoss) o);
			break;
		case "ProtocolHarbourRequest":
			handle((ProtocolHarbourRequest) o);
			break;
		case "ProtocolTradeRequest":
			handle((ProtocolTradeRequest) o);
			break;
		case "ProtocolTradeIsRequested":
			handle((ProtocolTradeIsRequested) o);
			break;
		case "ProtocolTradeAccept":
			handle((ProtocolTradeAccept) o);
			break;
		case "ProtocolTradeConfirmation":
			handle((ProtocolTradeConfirmation) o);
			break;
		case "ProtocolTradeComplete":
			handle((ProtocolTradeComplete) o);
			break;
		case "ProtocolTradeIsCompleted":
			handle((ProtocolTradeIsCompleted) o);
			break;
		case "ProtocolTradeCancel":
			handle((ProtocolTradeCancel) o);
			break;
		case "ProtocolTradeIsCanceled":
			handle((ProtocolTradeIsCanceled) o);
			break;
		case "Entwicklungskarte kaufen":
			handle((ProtocolBuyDevelopmentCards) o);
			break;
		case "Entwicklungskarte":
			handle((ProtocolDevelopmentCards) o);
			break;
		case "Erfindungskarte":
			handle((ProtocolInventionCard) o);
			break;
		case "Monopolkarte":
			handle((ProtocolMonopolyCard) o);
			break;
		case "Strassenbaukarte":
			handle((ProtocolRoadBuildingCard) o);
			break;
		case "ProtocolBiggestKnightProwess":
			handle((ProtocolBiggestKnightProwess) o);
			break;
		case "ProtocolInventionCardInfo":
			handle((ProtocolInventionCardInfo) o);
			break;
		case "ProtocolLongestRoad":
			handle((ProtocolLongestRoad) o);
			break;
		case "ProtocolMonopolyCardInfo":
			handle((ProtocolMonopolyCardInfo) o);
			break;
		case "ProtocolPlayKnightCard":
			handle((ProtocolPlayKnightCard) o);
			break;
		case "ProtocolRoadBuildingCardInfo":
			handle((ProtocolRoadBuildingCardInfo) o);
			break;
		case "ProtocolBoughtDevelopmentCard":
			handle((ProtocolBoughtDevelopmentCard) o);
			break;
		case "ProtocolSpecialCaseLongestRoad":
			handle((ProtocolSpecialCaseLongestRoad)o);
			break;

		default:
			System.out.println("Class not found");
			logger.info("Class not found");
		}

	}

	protected abstract void handle(ProtocolHello hello);

	protected abstract void handle(ProtocolWelcome welcome);

	protected abstract void handle(ProtocolClientReady clientReady);

	protected abstract void handle(ProtocolGameStarted gameStarted);

	protected abstract void handle(ProtocolError error);

	protected abstract void handle(ProtocolPlayerProfile playerProfile);

	protected abstract void handle(ProtocolChatReceiveMessage chatReceiveMessage);

	protected abstract void handle(ProtocolChatSendMessage chatSendMessage);

	protected abstract void handle(ProtocolServerConfirmation serverConfirmation);

	protected abstract void handle(String string);

	protected abstract void handle(ProtocolBuild build);

	protected abstract void handle(ProtocolDiceRollResult diceRollResult);

	protected abstract void handle(ProtocolResourceObtain resourceObtain);

	protected abstract void handle(ProtocolStatusUpdate statusUpdate);

	protected abstract void handle(ProtocolBuildRequest buildRequest);

	protected abstract void handle(ProtocolDiceRollRequest diceRollRequest);

	protected abstract void handle(ProtocolEndTurn endTurn);

	protected abstract void handle(ProtocolVictory victory);

	protected abstract void handle(ProtocolCosts costs);

	protected abstract void handle(ProtocolRobberMovement robberMovement);

	protected abstract void handle(ProtocolRobberLoss robberLoss);

	protected abstract void handle(ProtocolRobberMovementRequest robberMovementRequest);

	protected abstract void handle(ProtocolHarbourRequest harbourRequest);

	protected abstract void handle(ProtocolTradeRequest tradeRequest);

	protected abstract void handle(ProtocolTradeIsRequested tradeIsRequested);

	protected abstract void handle(ProtocolTradeAccept tradeAccept);

	protected abstract void handle(ProtocolTradeConfirmation tradeConfirmation);

	protected abstract void handle(ProtocolTradeComplete tradeComplete);

	protected abstract void handle(ProtocolTradeIsCompleted tradeIsCompleted);

	protected abstract void handle(ProtocolTradeCancel tradeCancel);

	protected abstract void handle(ProtocolTradeIsCanceled tradeIsCanceled);

	protected abstract void handle(ProtocolBuyDevelopmentCards buyDevelopmentCards);

	protected abstract void handle(ProtocolDevelopmentCards developmentCards);

	protected abstract void handle(ProtocolInventionCard inventionCard);

	protected abstract void handle(ProtocolMonopolyCard monopolyCard);

	protected abstract void handle(ProtocolRoadBuildingCard roadBuildingCard);

	protected abstract void handle(ProtocolBiggestKnightProwess biggestKnightProwess);

	protected abstract void handle(ProtocolInventionCardInfo inventionCardInfo);

	protected abstract void handle(ProtocolLongestRoad longestRoad);

	protected abstract void handle(ProtocolMonopolyCardInfo monopolyCardInfo);

	protected abstract void handle(ProtocolPlayKnightCard playKnightCard);

	protected abstract void handle(ProtocolRoadBuildingCardInfo roadBuildingCardInfo);

	protected abstract void handle(ProtocolBoughtDevelopmentCard boughtDevelopmentCard);

	protected abstract void handle(ProtocolSpecialCaseLongestRoad specialCaseLongestRoad);

}
