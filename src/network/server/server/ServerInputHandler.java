package network.server.server;

import enums.Color;
import enums.ResourceType;
import network.InputHandler;
import network.ProtocolToModel;
import network.server.controller.ServerController;
import protocol.clientinstructions.ProtocolBuildRequest;
import protocol.clientinstructions.ProtocolBuyDevCard;
import protocol.clientinstructions.ProtocolDiceRollRequest;
import protocol.clientinstructions.ProtocolEndTurn;
import protocol.clientinstructions.ProtocolHarbourRequest;
import protocol.clientinstructions.ProtocolRobberLoss;
import protocol.clientinstructions.ProtocolRobberMovementRequest;
import protocol.clientinstructions.trade.ProtocolTradeAccept;
import protocol.clientinstructions.trade.ProtocolTradeCancel;
import protocol.clientinstructions.trade.ProtocolTradeComplete;
import protocol.clientinstructions.trade.ProtocolTradeRequest;
import protocol.configuration.ProtocolClientReady;
import protocol.configuration.ProtocolError;
import protocol.configuration.ProtocolGameStarted;
import protocol.configuration.ProtocolPlayerProfile;
import protocol.configuration.ProtocolVictory;
import protocol.connection.ProtocolHello;
import protocol.connection.ProtocolWelcome;
import protocol.dualinstructions.ProtocolPlayInventionCard;
import protocol.dualinstructions.ProtocolPlayKnightCard;
import protocol.dualinstructions.ProtocolPlayMonopolyCard;
import protocol.dualinstructions.ProtocolPlayRoadCard;
import protocol.messaging.ProtocolChatReceiveMessage;
import protocol.messaging.ProtocolChatSendMessage;
import protocol.messaging.ProtocolServerResponse;
import protocol.object.ProtocolDevCard;
import protocol.object.ProtocolResource;
import protocol.serverinstructions.ProtocolBoughtDevelopmentCard;
import protocol.serverinstructions.ProtocolBuild;
import protocol.serverinstructions.ProtocolCosts;
import protocol.serverinstructions.ProtocolDiceRollResult;
import protocol.serverinstructions.ProtocolLargestArmy;
import protocol.serverinstructions.ProtocolLongestRoad;
import protocol.serverinstructions.ProtocolResourceObtain;
import protocol.serverinstructions.ProtocolRobberMovement;
import protocol.serverinstructions.ProtocolStatusUpdate;
import protocol.serverinstructions.trade.ProtocolTradeConfirmation;
import protocol.serverinstructions.trade.ProtocolTradeCancellation;
import protocol.serverinstructions.trade.ProtocolTradeCompletion;
import protocol.serverinstructions.trade.ProtocolTradePreview;

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
		serverController.receiveHello(currentThreadID);

	}

	@Override
	protected void handle(ProtocolClientReady clientReady) {
		serverController.clientReady(currentThreadID);

	}

	@Override
	protected void handle(ProtocolPlayerProfile playerProfile) {
		String name = playerProfile.getName();
		Color color = playerProfile.getColor();
		serverController.playerProfileUpdate(color, name, currentThreadID);

	}

	protected void handle(ProtocolChatSendMessage chatSendMessage) {
		String s = chatSendMessage.getMessage();
		serverController.chatSendMessage(s, currentThreadID);
	}

	//

	@Override
	protected void handle(ProtocolBuildRequest buildRequest) {
		if (buildRequest.getBuildingType().equals("Straße")) {
			int[] loc = ProtocolToModel.getEdgeCoordinates(buildRequest.getLocationID());
			serverController.requestBuildStreet(loc[0], loc[1], loc[2], currentThreadID);
		}
		if (buildRequest.getBuildingType().equals("Dorf")) {
			int[] loc = ProtocolToModel.getCornerCoordinates(buildRequest.getLocationID());
			serverController.requestBuildVillage(loc[0], loc[1], loc[2], currentThreadID);
		}
		if (buildRequest.getBuildingType().equals("Stadt")) {
			int[] loc = ProtocolToModel.getCornerCoordinates(buildRequest.getLocationID());
			serverController.requestBuildCity(loc[0], loc[1], loc[2], currentThreadID);
		}
	}

	@Override
	protected void handle(ProtocolDiceRollRequest diceRollRequest) {
		serverController.diceRollRequest(currentThreadID);

	}

	@Override
	protected void handle(ProtocolEndTurn endTurn) {
		serverController.endTurn(currentThreadID);
	}

	protected void handle(ProtocolRobberMovementRequest robberMovementRequest) {
		String location_id = robberMovementRequest.getLocationID();
		int[] coords = ProtocolToModel.getFieldCoordinates(location_id);
		Integer victim_id = robberMovementRequest.getVictimID();
		serverController.robberMovementRequest(coords[0], coords[1], victim_id, currentThreadID);

	}

	protected void handle(ProtocolHarbourRequest harbourRequest) {

		ProtocolResource offer = harbourRequest.getOffer();
		ProtocolResource withdrawal = harbourRequest.getWithdrawal();
		// gameController.harbourRequest(offer,withdrawal);
	}

	protected void handle(ProtocolTradeAccept tradeAccept) {
		int tradeID = tradeAccept.getTradeID();
		serverController.acceptTrade(currentThreadID, tradeID);
	}

	protected void handle(ProtocolTradeRequest tradeRequest) {
		ProtocolResource offer = tradeRequest.getOffer();
		ProtocolResource demand = tradeRequest.getDemand();
		serverController.clientOffersTrade(currentThreadID, ProtocolToModel.convertResources(offer),
				ProtocolToModel.convertResources(demand));
	}

	protected void handle(ProtocolTradeCancel tradeCancel) {
		int tradeID = tradeCancel.getTradeID();
		serverController.cancelTrade(currentThreadID, tradeID);
	}

	protected void handle(ProtocolTradeComplete tradeComplete) {
		int tradeID = tradeComplete.getTradeID();
		int tradePartnerID = tradeComplete.getTradePartnerID();
		serverController.fulfillTrade(currentThreadID, tradeID, tradePartnerID);
	}

	@Override
	protected void handle(ProtocolRobberLoss robberLoss) {
		ProtocolResource prl = robberLoss.getLosses();
		serverController.robberLoss(currentThreadID, ProtocolToModel.convertResources(prl));
	}

	public ServerController getServerController() {

		return this.serverController;
	}

	public void hello(int threadID) {

		serverController.hello(threadID);
	}

	// Unnecessary Methods

	@Override
	protected void handle(ProtocolWelcome welcome) {
		// Unnecessary Method

	}

	@Override
	protected void handle(ProtocolServerResponse serverConfirmation) {
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

	@Override
	protected void handle(ProtocolTradePreview tradePreview) {
		// Unnecessary Method

	}

	@Override
	protected void handle(ProtocolTradeConfirmation tradeConfirmation) {
		// Unnecessary Method

	}

	@Override
	protected void handle(ProtocolTradeCompletion tradeIsCompleted) {
		// Unnecessary Method

	}

	@Override
	protected void handle(ProtocolTradeCancellation tradeIsCanceled) {
		// Unnecessary Method

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
	protected void handle(ProtocolVictory victory) {
		// Unnecessary Method

	}

	@Override
	protected void handle(ProtocolCosts costs) {
		// Unnecessary Method

	}

	@Override
	protected void handle(ProtocolRobberMovement robberMovement) {

		// unnecessary Method

	}

	@Override
	protected void handle(ProtocolChatReceiveMessage chatReceiveMessage) {
		/*
		 * ChatRecieveMessage, (Nachricht wird vom Server verteilt) needs to be
		 * handled only in ServerOutputHandler and in ClientInputHandler.
		 * unnecessary Method in ServerInputHandler
		 */
	}

	@Override
	protected void handle(String string) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolBuyDevCard buyDevelopmentCards) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolDevCard developmentCards) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolLargestArmy biggestKnightProwess) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolPlayInventionCard inventionCardInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolLongestRoad longestRoad) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolPlayMonopolyCard monopolyCardInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolPlayKnightCard playKnightCard) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolPlayRoadCard roadBuildingCardInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolBoughtDevelopmentCard boughtDevelopmentCard) {
		// TODO Auto-generated method stub

	}

}
