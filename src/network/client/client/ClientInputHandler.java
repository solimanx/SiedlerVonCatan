package network.client.client;

import network.client.controller.ClientNetworkController;
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
import protocol.serverinstructions.ProtocolStatusUpdate;

public class ClientInputHandler {
	private Parser parser;
	private ClientNetworkController networkController;

	public ClientInputHandler(ClientNetworkController nc) {
		this.networkController = nc;
		this.parser = new Parser();
	}

	/**
	 * sends JSON formatted string to parser and initiates handling of parsed
	 * object
	 *
	 * @param s
	 */
	public void sendToParser(String s) {
		Object object = parser.parseString(s);
		System.out.println(object.getClass());
		handle(object);
		// handle(object.getClass().cast(object));
	}

	/**
	 * takes parsed object and redirects to its proper method depending on
	 * objects "real" class. ( object will be casted to "real" class )
	 *
	 * @param o
	 */
	private void handle(Object o) {
		switch(o.getClass().getSimpleName()){
		case "ProtocolHello":handle((ProtocolHello) o);break;
		case "ProtocolWelcome":handle((ProtocolWelcome) o );break;
		case "ProtocolClientReady":handle((ProtocolClientReady) o );break;
		case "ProtocolError":handle((ProtocolError) o );break;
		case "ProtocolGameStarted":handle((ProtocolGameStarted) o );break;
		case "ProtocolPlayerProfile":handle ((ProtocolPlayerProfile) o );break;
		case "ProtocolChatReceiveMessage":handle((ProtocolChatReceiveMessage) o);break;
        case "ProtocolChatSendMessage":handle((ProtocolChatSendMessage) o);break;
        case "ProtocolServerConfirmation":handle((ProtocolServerConfirmation) o );break;
        case "ProtocolBuild":handle((ProtocolBuild) o );break;
        case "ProtocolDiceRollResult":handle((ProtocolDiceRollResult) o );break;
        case "ProtocolResourceObtain":handle ((ProtocolResourceObtain) o );break;
        case "ProtocolStatusUpdate":handle ((ProtocolStatusUpdate) o );break;
        case "ProtocolBuildRequest":handle ((ProtocolBuildRequest) o );break;
        case "ProtocolDiceRollRequest":handle ((ProtocolDiceRollRequest) o );break;
        case "ProtocolEndTurn":handle ((ProtocolEndTurn) o );break;
        case "ProtocolChatReceiveMessage":handle((ProtocolChatReceiveMessage) o);break;
		default: System.out.println("Class not found");
		}
		/*
		 Class cl = o.getClass();
		if (cl.equals(ProtocolHello.class)) {
			ProtocolHello ph = (ProtocolHello) o;
			handle(ph);
		} else if (cl.equals(ProtocolWelcome.class)) {
			ProtocolWelcome pw = (ProtocolWelcome) o;
			handle(pw);
		} else if (cl.equals(ProtocolClientReady.class)) {
			ProtocolClientReady pw = (ProtocolClientReady) o;
			handle(pw);
		} else if (cl.equals(ProtocolGameStarted.class)) {
			ProtocolGameStarted pw = (ProtocolGameStarted) o;
			handle(pw);
		} else if (cl.equals(ProtocolError.class)) {
			ProtocolError pw = (ProtocolError) o;
			handle(pw);
		} else if (cl.equals(ProtocolPlayerProfile.class)) {
			ProtocolPlayerProfile pw = (ProtocolPlayerProfile) o;
			handle(pw);
		} else if (cl.equals(ProtocolChatReceiveMessage.class)) {
			ProtocolChatReceiveMessage pw = (ProtocolChatReceiveMessage) o;
			handle(pw);
		} else if (cl.equals(ProtocolChatSendMessage.class)) {
			ProtocolChatSendMessage pw = (ProtocolChatSendMessage) o;
			handle(pw);
		} else if (cl.equals(ProtocolServerConfirmation.class)) {
			ProtocolServerConfirmation pw = (ProtocolServerConfirmation) o;
			handle(pw);
		} else if (cl.equals(ProtocolBuild.class)) {
			ProtocolBuild pw = (ProtocolBuild) o;
			handle(pw);
		} else if (cl.equals(ProtocolDiceRollResult.class)) {
			ProtocolDiceRollResult pw = (ProtocolDiceRollResult) o;
			handle(pw);
		} else if (cl.equals(ProtocolResourceObtain.class)) {
			ProtocolResourceObtain pw = (ProtocolResourceObtain) o;
			handle(pw);
		} else if (cl.equals(ProtocolStatusUpdate.class)) {
			ProtocolStatusUpdate pw = (ProtocolStatusUpdate) o;
			handle(pw);
		} else if (cl.equals(ProtocolDiceRollResult.class)) {
			ProtocolDiceRollResult pw = (ProtocolDiceRollResult) o;
			handle(pw);
		} else if (cl.equals(ProtocolBuildRequest.class)) {
			ProtocolBuildRequest pw = (ProtocolBuildRequest) o;
			handle(pw);
		} else if (cl.equals(ProtocolDiceRollRequest.class)) {
			ProtocolDiceRollRequest pw = (ProtocolDiceRollRequest) o;
			handle(pw);
		} else if (cl.equals(ProtocolEndTurn.class)) {
			ProtocolEndTurn pw = (ProtocolEndTurn) o;
			handle(pw);
		}*/
	}

	/**
	 * Acknowledge the JSON object and pass it through the network controller
	 * for checks
	 * 
	 * @param hello
	 */
	private void handle(ProtocolHello hello) {
		System.out.println("Hello gelesen!");
		networkController.serverHello(hello.getVersion(), hello.getProtocol());
	}

	private void handle(ProtocolWelcome welcome) {
		System.out.println("Willkommen gelesen!");
		networkController.welcome(welcome.getPlayer_id());
	}

	private void handle(ProtocolClientReady clientReady) {

	}

	private void handle(ProtocolGameStarted gameStarted) {

	}

	private void handle(ProtocolError error) {

	}

	private void handle(ProtocolPlayerProfile playerProfile) {

	}

	//

	private void handle(ProtocolChatReceiveMessage chatReceiveMessage) {
		String s = chatReceiveMessage.getMessage();
		int playerId = chatReceiveMessage.getSender();
		networkController.chatReceiveMessage(playerId, s);
	}

	private void handle(ProtocolChatSendMessage chatSendMessage) {

	}

	private void handle(ProtocolServerConfirmation serverConfirmation) {

	}

	//

	private void handle(ProtocolBuild build) {

	}

	private void handle(ProtocolDiceRollResult diceRollResult) {

	}

	private void handle(ProtocolResourceObtain resourceObtain) {

	}

	private void handle(ProtocolStatusUpdate statusUpdate) {

	}

	//

	private void handle(ProtocolBuildRequest buildRequest) {

	}

	private void handle(ProtocolDiceRollRequest diceRollRequest) {

	}

	private void handle(ProtocolEndTurn endTurn) {

	}

}
