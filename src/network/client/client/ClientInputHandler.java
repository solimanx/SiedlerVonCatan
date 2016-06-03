package network.client.client;

import network.InputHandler;
import network.ProtocolToModel;
import network.client.controller.ClientNetworkController;
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

public class ClientInputHandler extends InputHandler {
	private ClientNetworkController networkController;

	public ClientInputHandler(ClientNetworkController nc) {
		super();
		this.networkController = nc;
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

	@Override
	protected void handle(ProtocolHello hello) {
		System.out.println("Hello gelesen!");
		networkController.serverHello(hello.getVersion(), hello.getProtocol());
	}

	@Override
	protected void handle(ProtocolWelcome welcome) {
		System.out.println("Willkommen gelesen!");
		networkController.welcome(welcome.getPlayer_id());
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
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolPlayerProfile playerProfile) {
		// TODO Auto-generated method stub

	}

	//
	@Override
	protected void handle(ProtocolChatReceiveMessage chatReceiveMessage) {
		String s = chatReceiveMessage.getMessage();
		int playerId = chatReceiveMessage.getSender();
		networkController.chatReceiveMessage(playerId, s);
	}

	@Override
	protected void handle(ProtocolChatSendMessage chatSendMessage) {
		// Unneeded

	}

	@Override
	protected void handle(ProtocolServerConfirmation serverConfirmation) {
		// TODO Auto-generated method stub
	}

	//
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
		int playerId = ProtocolToModel.getPlayerId(statusUpdate.getPlayer().getPlayer_id());
		enums.Color color = statusUpdate.getPlayer().getColor();
		String name = statusUpdate.getPlayer().getName();
		enums.PlayerState status = ProtocolToModel.getPlayerState(statusUpdate.getPlayer().getStatus());
		int victoryPoints = statusUpdate.getPlayer().getVictory_points();
		int[] resources = ProtocolToModel.getResources(statusUpdate.getPlayer().getResources());
		networkController.statusUpdate(playerId, color, name, status, victoryPoints, resources);
		
	}

	//
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

}
