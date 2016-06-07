package network.server.server;

import enums.Color;
import network.InputHandler;
import network.ProtocolToModel;
import network.client.controller.ClientNetworkController;
import network.server.controller.ServerController;
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
import protocol.object.ProtocolResource;
import protocol.serverinstructions.*;
import protocol.serverinstructions.trade.ProtocolTradeConfirmation;
import protocol.serverinstructions.trade.ProtocolTradeIsCanceled;
import protocol.serverinstructions.trade.ProtocolTradeIsCompleted;
import protocol.serverinstructions.trade.ProtocolTradeIsRequested;
import protocol3.object.ProtocolInventionCard;
import protocol3.severinstructions.*;

public class ServerInputHandler extends InputHandler {
	private ServerController serverController;
	private int currentThreadID;

	public ServerInputHandler(ServerController serverController) {
		super();
		this.serverController = serverController;
	}

	public ServerController getGameController() {
		return serverController;
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
		serverController.welcome(serverController.getPlayerModelId(currentThreadID));

	}

	@Override
	protected void handle(ProtocolWelcome welcome) {
		System.out.println("Welcome gelesen!");
	}

	// unnecessary Method in ServerInputHandler
	@Override
	protected void handle(ProtocolClientReady clientReady) {
		serverController.clientReady(serverController.getPlayerModelId(currentThreadID));

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
		serverController.statusUpdate(color, name, currentThreadID);

	}

	@Override
	protected void handle(ProtocolChatReceiveMessage chatReceiveMessage) {
		String s = chatReceiveMessage.getMessage();
		int playerId = chatReceiveMessage.getSender();
		serverController.chatReceiveMessage(playerId, s);
		/*
		 * ChatRecieveMessage, (Nachricht wird vom Server verteilt) needs to be
		 * handled only in ServerOutputHandler and in ClientInputHandler.
		 * unnecessary Method in ServerInputHandler
		 */
	}

	protected void handle(ProtocolChatSendMessage chatSendMessage) {
		String s = chatSendMessage.getMessage();
		serverController.chatSendMessage(s, this.currentThreadID);
	}

	@Override
	protected void handle(ProtocolServerConfirmation serverConfirmation) {
		String server_response = serverConfirmation.getServer_response();
		serverController.serverConfirmation(server_response);
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
			serverController.requestBuildStreet(loc[0], loc[1], loc[2],
					serverController.getPlayerModelId(this.currentThreadID));
		}
		if (buildRequest.getBuilding() == "Dorf") {
			int[] loc = ProtocolToModel.getCornerCoordinates(buildRequest.getLocation());
			serverController.requestBuildVillage(loc[0], loc[1], loc[2],
					serverController.getPlayerModelId(this.currentThreadID));
		}
		if (buildRequest.getBuilding() == "Stadt") {
			int[] loc = ProtocolToModel.getCornerCoordinates(buildRequest.getLocation());
			serverController.requestBuildCity(loc[0], loc[1], loc[2],
					serverController.getPlayerModelId(this.currentThreadID));
		}
	}

	@Override
	protected void handle(ProtocolDiceRollRequest diceRollRequest) {
		serverController.diceRollRequest(serverController.getPlayerModelId(this.currentThreadID));

	}

	@Override
	protected void handle(ProtocolEndTurn endTurn) {
		System.out.println("Der Zug wurde beendet");

	}

	@Override
	protected void handle(ProtocolVictory victory) {

	}

	@Override
	protected void handle(ProtocolCosts costs) {

	}

	@Override
	protected void handle(ProtocolRobberMovement robberMovement) {

	}

	@Override
	protected void handle(ProtocolRobberLoss robberLoss) {

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

	@Override
	protected void handle(ProtocolTradeConfirmation tradeConfirmation) {

	}

	protected void handle(ProtocolTradeRequest tradeRequest) {

		ProtocolResource offer = tradeRequest.getOffer();
		ProtocolResource withdrawal = tradeRequest.getWithdrawal();
		// gameController.tradeRequest(offer,withdrawal);

	}

	@Override
	protected void handle(ProtocolTradeIsRequested tradeIsRequested) {

	}

	protected void handle(ProtocolTradeCancel tradeCancel) {

		int trade_id = tradeCancel.getTrade_id();
		// gameController.tradeCancel(trade_id);
	}

	@Override
	protected void handle(ProtocolTradeIsCanceled tradeIsCanceled) {

	}

	@Override
	protected void handle(ProtocolBiggestKnightProwess biggestKnightProwess) {

	}

	@Override
	protected void handle(ProtocolInventionCard inventionCard) {

	}

	@Override
	protected void handle(ProtocolLongestRoad longestRoad) {

	}

	@Override
	protected void handle(ProtocolMonopolyCardInfo protocolMonopolyCardInfo) {

	}

	@Override
	protected void handle(ProtocolPlayKnightCard protocolPlayKnightCard) {

	}

	@Override
	protected void handle(ProtocolRoadBuildingCardInfo protocolRoadBuildingCardInfo) {

	}

	protected void handle(ProtocolTradeComplete tradeComplete) {

		int trade_id = tradeComplete.getTrade_id();
		int tradePartner_id = tradeComplete.getTradePartner_id();
		// gameController.tradeComplete(trade_id,tradePartner_id;)

	}

	@Override
	protected void handle(ProtocolTradeIsCompleted tradeIsCompleted) {

	}

}
