package network.server.server;

import enums.Color;
import network.InputHandler;
import network.ProtocolToModel;
import network.client.controller.ClientNetworkController;
import network.server.controller.GameController;
import protocol.clientinstructions.*;
import protocol.clientinstructions.trade.ProtocolTradeAccept;
import protocol.clientinstructions.trade.ProtocolTradeCancel;
import protocol.clientinstructions.trade.ProtocolTradeComplete;
import protocol.clientinstructions.trade.ProtocolTradeRequest;
import protocol.configuration.ProtocolClientReady;
import protocol.configuration.ProtocolError;
import protocol.configuration.ProtocolGameStarted;
import protocol.configuration.ProtocolPlayerProfile;
import protocol.connection.ProtocolHello;
import protocol.connection.ProtocolWelcome;
import protocol.messaging.ProtocolChatReceiveMessage;
import protocol.messaging.ProtocolChatSendMessage;
import protocol.messaging.ProtocolServerConfirmation;
import protocol.object.ProtocolResource;
import protocol.serverinstructions.ProtocolBuild;
import protocol.serverinstructions.ProtocolDiceRollResult;
import protocol.serverinstructions.ProtocolResourceObtain;
import protocol.serverinstructions.ProtocolStatusUpdate;

public class ServerInputHandler extends InputHandler {
	private GameController gameController;
	private int currentThreadID;

	public ServerInputHandler(GameController gameController) {
		super();
		this.gameController = gameController;
	}

	public GameController getGameController() {
		return gameController;
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

		handle(object);
	}

	@Override
	protected void handle(ProtocolHello hello) {
		System.out.println("SERVER: Hello gelesen!");
		gameController.welcome(gameController.getPlayerModelId(currentThreadID));

	}

	@Override
	protected void handle(ProtocolWelcome welcome) {
		System.out.println("Welcome gelesen!");
	}

	// unnecessary Method in ServerInputHandler
	@Override
	protected void handle(ProtocolClientReady clientReady) {
		gameController.clientReady(gameController.getPlayerModelId(currentThreadID));

	}

	@Override
	protected void handle(ProtocolGameStarted gameStarted) {
		// unnecessary Method in ServerInputHandler

	}

	@Override
	protected void handle(ProtocolError error) {
		// unnecessary Method in ServerInputHandler

	}

	@Override
	protected void handle(ProtocolPlayerProfile playerProfile) {
		String name = playerProfile.getName();
		Color color = playerProfile.getColor();
		gameController.statusUpdate(color, name, currentThreadID);

	}

	@Override
	protected void handle(ProtocolChatReceiveMessage chatReceiveMessage) {
		String s = chatReceiveMessage.getMessage();
		int playerId = chatReceiveMessage.getSender();
		gameController.chatReceiveMessage(playerId, s);
		/*
		 * ChatRecieveMessage, (Nachricht wird vom Server verteilt) needs to be
		 * handled only in ServerOutputHandler and in ClientInputHandler.
		 * unnecessary Method in ServerInputHandler
		 */
	}

	protected void handle(ProtocolChatSendMessage chatSendMessage) {
		String s = chatSendMessage.getMessage();
		gameController.chatSendMessage(s, this.currentThreadID);
	}

	@Override
	protected void handle(ProtocolServerConfirmation serverConfirmation) {
		String server_response = serverConfirmation.getServer_response();
		gameController.serverConfirmation(server_response);
		// Unnecessary Method in ServerInputHadler
	}

	//
	@Override
	protected void handle(ProtocolBuild build) {
		// unnecessary Method in ServerInputHandler

	}

	@Override
	protected void handle(ProtocolDiceRollResult diceRollResult) {
		// unnecessary Method in ServerInputHandler

	}

	@Override
	protected void handle(ProtocolResourceObtain resourceObtain) {
		// unnecessary Method in ServerInputHandler
	}

	@Override
	protected void handle(ProtocolStatusUpdate statusUpdate) {
		// unnecessary Method in ServerInputHandler

	}

	//

	@Override
	protected void handle(ProtocolBuildRequest buildRequest) {
		if (buildRequest.getBuilding() == "Straße") {
			int[] loc = ProtocolToModel.getEdgeCoordinates(buildRequest.getLocation());
			gameController.requestBuildStreet(loc[0], loc[1], loc[2],
					gameController.getPlayerModelId(this.currentThreadID));
		}
		if (buildRequest.getBuilding() == "Dorf") {
			int[] loc = ProtocolToModel.getCornerCoordinates(buildRequest.getLocation());
			gameController.requestBuildVillage(loc[0], loc[1], loc[2],
					gameController.getPlayerModelId(this.currentThreadID));
		}
		if (buildRequest.getBuilding() == "Stadt") {
			int[] loc = ProtocolToModel.getCornerCoordinates(buildRequest.getLocation());
			gameController.requestBuildCity(loc[0], loc[1], loc[2],
					gameController.getPlayerModelId(this.currentThreadID));
		}
	}

	@Override
	protected void handle(ProtocolDiceRollRequest diceRollRequest) {
		gameController.diceRollRequest(gameController.getPlayerModelId(this.currentThreadID));

	}

	@Override
	protected void handle(ProtocolEndTurn endTurn) {
		System.out.println("Der Zug wurde beendet");

	}

	@Override
	protected void handle(String string) {
		// TODO Auto-generated method stub

	}

	protected void handle(ProtocolRobberMovementRequest robberMovementRequest) {

		Integer player_id = robberMovementRequest.getPlayer_id();
		String location_id = robberMovementRequest.getLocation_id();
		int victim_id = robberMovementRequest.getVictim_id();
		// gameController.robberMovementRequest(player_id,location_id,victim_id);

	}

	protected void handle(ProtocolHarbourRequest harbourRequest) {

		ProtocolResource offer = harbourRequest.getOffer();
		ProtocolResource withdrawal = harbourRequest.getWithdrawal();
		// gameController.harbourRequest(offer,withdrawal);
	}

	protected void handle(ProtocolTradeAccept tradeAccept) {

		int trade_id = tradeAccept.getTrade_id();
		// gameController.tradeAccept(trade_id);
	}

	protected void handle(ProtocolTradeRequest tradeRequest) {

		ProtocolResource offer = tradeRequest.getOffer();
		ProtocolResource withdrawal = tradeRequest.getWithdrawal();
		// gameController.tradeRequest(offer,withdrawal);

	}

	protected void handle(ProtocolTradeCancel tradeCancel) {

		int trade_id = tradeCancel.getTrade_id();
		// gameController.tradeCancel(trade_id);
	}

	protected void handle(ProtocolTradeComplete tradeComplete) {

		int trade_id = tradeComplete.getTrade_id();
		int tradePartner_id = tradeComplete.getTradePartner_id();
		// gameController.tradeComplete(trade_id,tradePartner_id;)

	}

}
