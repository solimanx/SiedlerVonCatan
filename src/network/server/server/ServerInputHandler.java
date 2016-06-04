package network.server.server;

import enums.Color;
import network.InputHandler;
import network.ModelToProtocol;
import network.ProtocolToModel;
import network.client.client.Client;
import network.client.controller.ClientNetworkController;
import network.server.controller.ServerNetworkController;
import parsing.Response;
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

public class ServerInputHandler extends InputHandler {
	private ServerNetworkController networkController;
	private int currentThreadID;

	public ServerInputHandler(ServerNetworkController nc) {
		super();
		this.networkController = nc;
	}

	/**
	 * sends JSON formatted string to parser and initiates handling of parsed
	 * object
	 *
	 * @param s
	 */
	public void sendToParser(String s, int threadID) {
		// speichert die threadID, falls sie in handle(Protocol...) gebraucht
		// wird.
		this.currentThreadID = threadID;
		Object object = parser.parseString(s);

		System.out.println(object.getClass());
		handle(object);
		// handle(object.getClass().cast(object));
	}

	@Override
	protected void handle(ProtocolHello hello) {
		System.out.println("SERVER: Hello gelesen!");
		networkController.welcome(networkController.getPlayerModelId(currentThreadID));

	}

	@Override
	protected void handle(ProtocolWelcome welcome) {
		System.out.println("Welcome gelesen!");
	}

	@Override
	protected void handle(ProtocolClientReady clientReady) {
		networkController.clientReady(networkController.getPlayerModelId(currentThreadID));



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
		String name= playerProfile.getName();
		Color color=playerProfile.getColor();
	//ClientNetworkController.sendPlayerProfile(name,color);


	}

	//
	@Override
	protected void handle(ProtocolChatReceiveMessage chatReceiveMessage) {
		String s = chatReceiveMessage.getMessage();
		int playerId = chatReceiveMessage.getSender();
		networkController.chatReceiveMessage(playerId, s);
	}

	protected void handle(ProtocolChatSendMessage chatSendMessage) {
		String s = chatSendMessage.getMessage();
		networkController.chatSendMessage(s, this.currentThreadID);
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

	}

	//

	@Override
	protected void handle(ProtocolBuildRequest buildRequest) {
		if (buildRequest.getBuilding() == "Straße") {
			int[] loc = ProtocolToModel.getEdgeCoordinates(buildRequest.getLocation());
			networkController.requestBuildStreet(loc[0], loc[1], loc[2],
					networkController.getPlayerModelId(this.currentThreadID));
		}
		if (buildRequest.getBuilding() == "Dorf") {
			int[] loc = ProtocolToModel.getCornerCoordinates(buildRequest.getLocation());
			networkController.requestBuildVillage(loc[0], loc[1], loc[2],
					networkController.getPlayerModelId(this.currentThreadID));
		}
		if (buildRequest.getBuilding() == "Stadt") {
			int[] loc = ProtocolToModel.getCornerCoordinates(buildRequest.getLocation());
			networkController.requestBuildCity(loc[0], loc[1], loc[2],
					networkController.getPlayerModelId(this.currentThreadID));
		}
	}

	@Override
	protected void handle(ProtocolDiceRollRequest diceRollRequest) {
		networkController.diceRollRequest(networkController.getPlayerModelId(this.currentThreadID));

	}

	@Override
	protected void handle(ProtocolEndTurn endTurn) {
		System.out.println("Der Zug wurde beendet");

	}

}
