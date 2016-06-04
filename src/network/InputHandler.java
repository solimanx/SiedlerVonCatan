package network;

import parsing.Parser;
import protocol.clientinstructions.ProtocolBuildRequest;
import protocol.clientinstructions.ProtocolDiceRollRequest;
import protocol.clientinstructions.ProtocolEndTurn;
import protocol.configuration.ProtocolClientReady;
import protocol.configuration.ProtocolError;
import protocol.configuration.ProtocolGameStarted;
import protocol.configuration.ProtocolPlayerProfile;
import protocol.connection.ProtocolHello;
import protocol.connection.ProtocolWelcome;
import protocol.messaging.ProtocolChatReceiveMessage;
import protocol.messaging.ProtocolChatSendMessage;
import protocol.messaging.ProtocolServerConfirmation;
import protocol.serverinstructions.ProtocolBuild;
import protocol.serverinstructions.ProtocolDiceRollResult;
import protocol.serverinstructions.ProtocolResourceObtain;
import protocol.serverinstructions.ProtocolRobberMovement;
import protocol.serverinstructions.ProtocolStatusUpdate;

public abstract class InputHandler {
	protected Parser parser;

	public InputHandler() {
		parser = new Parser();
	}

	// public void sendToParser();

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
		default:
			System.out.println("Class not found");
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

	protected abstract void handle(ProtocolBuild build);

	protected abstract void handle(ProtocolDiceRollResult diceRollResult);

	protected abstract void handle(ProtocolResourceObtain resourceObtain);

	protected abstract void handle(ProtocolStatusUpdate statusUpdate);

	protected abstract void handle(ProtocolBuildRequest buildRequest);

	protected abstract void handle(ProtocolDiceRollRequest diceRollRequest);

	protected abstract void handle(ProtocolEndTurn endTurn);

}
