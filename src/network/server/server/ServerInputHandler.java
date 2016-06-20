package network.server.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import enums.Color;
import enums.ResourceType;
import network.ProtocolToModel;
import network.server.controller.ServerController;
import parsing.Parser;
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
import protocol.serverinstructions.trade.ProtocolTradeCancellation;
import protocol.serverinstructions.trade.ProtocolTradeCompletion;
import protocol.serverinstructions.trade.ProtocolTradeConfirmation;
import protocol.serverinstructions.trade.ProtocolTradePreview;

public class ServerInputHandler {
	private static Logger logger = LogManager.getLogger(ServerInputHandler.class.getSimpleName());
	protected Parser parser;
	private ServerController serverController;
	private int currentThreadID;

	public ServerInputHandler(ServerController serverController) {
		parser = new Parser();
		this.serverController = serverController;
	}

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
		case "ProtocolServerResponse":
			handle((ProtocolServerResponse) o);
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
			handle(new ProtocolServerResponse((String) o));
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
		case "ProtocolTradePreview":
			handle((ProtocolTradePreview) o);
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
		case "ProtocolTradeCompletion":
			handle((ProtocolTradeCompletion) o);
			break;
		case "ProtocolTradeCancel":
			handle((ProtocolTradeCancel) o);
			break;
		case "ProtocolTradeCancellation":
			handle((ProtocolTradeCancellation) o);
			break;
		case "ProtocolBuyDevCard":
			handle((ProtocolBuyDevCard) o);
			break;
		case "ProtocolLargestArmy":
			handle((ProtocolLargestArmy) o);
			break;
		case "ProtocolPlayInventionCard":
			handle((ProtocolPlayInventionCard) o);
			break;
		case "ProtocolLongestRoad":
			handle((ProtocolLongestRoad) o);
			break;
		case "ProtocolPlayMonopolyCard":
			handle((ProtocolPlayMonopolyCard) o);
			break;
		case "ProtocolPlayKnightCard":
			handle((ProtocolPlayKnightCard) o);
			break;
		case "ProtocolPlayRoadCard":
			handle((ProtocolPlayRoadCard) o);
			break;
		case "ProtocolBoughtDevelopmentCard":
			handle((ProtocolBoughtDevelopmentCard) o);
			break;

		default:
			logger.info("Class not found");
		}

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

	protected void hello(int threadID) {
		serverController.hello(threadID);
	}

	protected void handle(ProtocolHello hello) {
		logger.debug("SERVER: Hello gelesen!");
		serverController.receiveHello(currentThreadID);

	}

	protected void handle(ProtocolChatSendMessage chatSendMessage) {
		String s = chatSendMessage.getMessage();
		serverController.chatSendMessage(s, currentThreadID);
	}

	protected void handle(ProtocolClientReady clientReady) {
		serverController.clientReady(currentThreadID);

	}

	protected void handle(ProtocolPlayerProfile playerProfile) {
		String name = playerProfile.getName();
		Color color = playerProfile.getColor();
		serverController.playerProfileUpdate(color, name, currentThreadID);

	}

	protected void handle(ProtocolDiceRollRequest diceRollRequest) {
		serverController.diceRollRequest(currentThreadID);

	}

	protected void handle(ProtocolRobberLoss robberLoss) {
		ProtocolResource prl = robberLoss.getLosses();
		serverController.robberLoss(currentThreadID, ProtocolToModel.convertResources(prl));
	}

	protected void handle(ProtocolRobberMovementRequest robberMovementRequest) {
		String location_id = ProtocolToModel.getProtocolOneID(robberMovementRequest.getLocationID());
		int[] coords = ProtocolToModel.getFieldCoordinates(location_id);
		Integer victim_id = robberMovementRequest.getVictimID();
		serverController.robberMovementRequest(coords[0], coords[1], victim_id, currentThreadID);

	}

	protected void handle(ProtocolEndTurn endTurn) {
		serverController.endTurn(currentThreadID);
	}

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

	protected void handle(ProtocolBuyDevCard buyDevelopmentCards) {
		serverController.requestBuyDevCard(currentThreadID);
	}

	protected void handle(ProtocolHarbourRequest harbourRequest) {
		ProtocolResource rOf = harbourRequest.getOffer();
		ProtocolResource rDe = harbourRequest.getWithdrawal();
		int[] offer = ProtocolToModel.convertResources(rOf);
		int[] demand = ProtocolToModel.convertResources(rDe);
		serverController.requestSeaTrade(currentThreadID, offer, demand);
	}

	protected void handle(ProtocolTradeRequest tradeRequest) {
		ProtocolResource offer = tradeRequest.getOffer();
		ProtocolResource demand = tradeRequest.getDemand();
		serverController.clientOffersTrade(currentThreadID, ProtocolToModel.convertResources(offer),
				ProtocolToModel.convertResources(demand));
	}

	protected void handle(ProtocolTradeAccept tradeAccept) {
		int tradeID = tradeAccept.getTradeID();
		boolean acceptFlag = tradeAccept.getAccepted();
		serverController.acceptTrade(currentThreadID, tradeID, acceptFlag);
	}

	protected void handle(ProtocolTradeComplete tradeComplete) {
		int tradeID = tradeComplete.getTradeID();
		int tradePartnerID = tradeComplete.getTradePartnerID();
		serverController.fulfillTrade(currentThreadID, tradeID, tradePartnerID);
	}

	protected void handle(ProtocolTradeCancel tradeCancel) {
		int tradeID = tradeCancel.getTradeID();
		serverController.cancelTrade(currentThreadID, tradeID);
	}

	protected void handle(ProtocolPlayInventionCard playInventionCard) {
		int[] resources = ProtocolToModel.convertResources(playInventionCard.getResource());
		serverController.playInventionCard(currentThreadID, resources);

	}

	protected void handle(ProtocolPlayMonopolyCard monopolyCard) {
		serverController.playMonopolyCard(currentThreadID, monopolyCard.getResourceType());
	}

	protected void handle(ProtocolPlayKnightCard knightCard) {
		String fieldID = ProtocolToModel.getProtocolOneID(knightCard.getLocationID());
		int coords[] = ProtocolToModel.getFieldCoordinates(fieldID);
		serverController.playKnightCard(currentThreadID, coords[0], coords[1], knightCard.getVictimID());

	}

	protected void handle(ProtocolPlayRoadCard roadBuildingCard) {
		int coords1[] = ProtocolToModel.getEdgeCoordinates(roadBuildingCard.getRoadID1());
		int coords2[] = ProtocolToModel.getEdgeCoordinates(roadBuildingCard.getRoadID2());
		serverController.playStreetCard(currentThreadID, coords1[0], coords1[1], coords1[2], coords2[0], coords1[1], coords2[2]);

	}

	public ServerController getServerController() {

		return this.serverController;
	}

}
