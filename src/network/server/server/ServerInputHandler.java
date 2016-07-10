package network.server.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import enums.Color;
import network.ProtocolToModel;
import network.server.controller.ServerController;
import network.server.server.cheat.CheatHandler;
import parsing.Parser;
import protocol.cheats.ProtocolCheat;
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
import settings.DefaultSettings;

// TODO: Auto-generated Javadoc
public class ServerInputHandler {
	private static Logger logger = LogManager.getLogger(ServerInputHandler.class.getSimpleName());
	protected Parser parser;
	private ServerController serverController;
	private int currentThreadID;

	/**
	 * Instantiates a new server input handler.
	 *
	 * @param serverController
	 *            the server controller
	 */
	public ServerInputHandler(ServerController serverController) {
		parser = new Parser();
		this.serverController = serverController;
	}

	/**
	 * Handle.
	 *
	 * @param o
	 *            the o
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
		case "ProtocolServerResponse":
			handle((ProtocolServerResponse) o);
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
		case "ProtocolRobberMovementRequest": // 0.2
			handle((ProtocolRobberMovementRequest) o);
			break;
		case "String":
			handle((String) o);// handle(new ProtocolServerResponse((String)
								// o));
			break;
		case "ProtocolVictory":
			handle((ProtocolVictory) o);
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
		case "ProtocolTradeAccept":
			handle((ProtocolTradeAccept) o);
			break;
		case "ProtocolTradeComplete":
			handle((ProtocolTradeComplete) o);
			break;
		case "ProtocolTradeCancel":
			handle((ProtocolTradeCancel) o);
			break;
		case "ProtocolBuyDevCard":
			handle((ProtocolBuyDevCard) o);
			break;
		case "ProtocolPlayInventionCard":
			handle((ProtocolPlayInventionCard) o);
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
		case "ProtocolCheat":
			handle((ProtocolCheat) o);
			break;
		default:
			handle((String) o);
			logger.info("Class not found");
		}

	}

	/**
	 * Gets the game controller.
	 *
	 * @return the game controller
	 */
	public ServerController getGameController() {
		return serverController;
	}

	/**
	 * sends JSON formatted string to parser and initiates handling of parsed
	 * object.
	 *
	 * @param s
	 *            the s
	 * @param threadID
	 *            the thread ID
	 */
	public void sendToParser(String s, int threadID) {
		// speichert die threadID, falls sie in handle(Protocol...) gebraucht
		// wird.
		this.currentThreadID = threadID;
		Object object = parser.parseString(s);

		handle(object);
	}

	/**
	 * Handle.
	 *
	 * @param o
	 *            the o
	 */
	protected void handle(String o) {
		serverController.sendInvalidJSON(currentThreadID);
	}

	/**
	 * Hello.
	 *
	 * @param threadID
	 *            the thread ID
	 */
	protected void hello(int threadID) {
		serverController.hello(threadID);
	}

	/**
	 * Handle.
	 *
	 * @param hello
	 *            the hello
	 */
	protected void handle(ProtocolHello hello) {
		
			serverController.receiveHello(currentThreadID,hello.getVersion());

	}

	/**
	 * Handle.
	 *
	 * @param chatSendMessage
	 *            the chat send message
	 */
	protected void handle(ProtocolChatSendMessage chatSendMessage) {
		String s = chatSendMessage.getMessage();
		serverController.chatSendMessage(s, currentThreadID);
	}

	/**
	 * Handle.
	 *
	 * @param clientReady
	 *            the client ready
	 */
	protected void handle(ProtocolClientReady clientReady) {
		serverController.clientReady(currentThreadID);

	}

	/**
	 * Handle.
	 *
	 * @param playerProfile
	 *            the player profile
	 */
	protected void handle(ProtocolPlayerProfile playerProfile) {
		String name = playerProfile.getName();
		Color color = playerProfile.getColor();
		serverController.playerProfileUpdate(color, name, currentThreadID);

	}

	/**
	 * Handle.
	 *
	 * @param diceRollRequest
	 *            the dice roll request
	 */
	protected void handle(ProtocolDiceRollRequest diceRollRequest) {
		serverController.diceRollRequest(currentThreadID);

	}

	/**
	 * Handle.
	 *
	 * @param robberLoss
	 *            the robber loss
	 */
	protected void handle(ProtocolRobberLoss robberLoss) {
		ProtocolResource prl = robberLoss.getLosses();
		serverController.robberLoss(currentThreadID, ProtocolToModel.convertResources(prl));
	}

	/**
	 * Handle.
	 *
	 * @param robberMovementRequest
	 *            the robber movement request
	 */
	protected void handle(ProtocolRobberMovementRequest robberMovementRequest) {
		String location_id = ProtocolToModel.getProtocolOneID(robberMovementRequest.getLocationID());
		int[] coords = ProtocolToModel.getFieldCoordinates(location_id);
		Integer victim_id = robberMovementRequest.getVictimID();
		serverController.robberMovementRequest(coords[0], coords[1], victim_id, currentThreadID);

	}

	/**
	 * Handle.
	 *
	 * @param endTurn
	 *            the end turn
	 */
	protected void handle(ProtocolEndTurn endTurn) {
		serverController.endTurn(currentThreadID);
	}

	/**
	 * Handle.
	 *
	 * @param buildRequest
	 *            the build request
	 */
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

	/**
	 * Handle.
	 *
	 * @param buyDevelopmentCards
	 *            the buy development cards
	 */
	protected void handle(ProtocolBuyDevCard buyDevelopmentCards) {
		serverController.requestBuyDevCard(currentThreadID);
	}

	/**
	 * Handle.
	 *
	 * @param harbourRequest
	 *            the harbour request
	 */
	protected void handle(ProtocolHarbourRequest harbourRequest) {
		ProtocolResource rOf = harbourRequest.getOffer();
		ProtocolResource rDe = harbourRequest.getWithdrawal();
		int[] offer = ProtocolToModel.convertResources(rOf);
		int[] demand = ProtocolToModel.convertResources(rDe);
		serverController.requestSeaTrade(currentThreadID, offer, demand);
	}

	/**
	 * Handle.
	 *
	 * @param tradeRequest
	 *            the trade request
	 */
	protected void handle(ProtocolTradeRequest tradeRequest) {
		ProtocolResource offer = tradeRequest.getOffer();
		ProtocolResource demand = tradeRequest.getDemand();
		serverController.clientOffersTrade(currentThreadID, ProtocolToModel.convertResources(offer),
				ProtocolToModel.convertResources(demand));
	}

	/**
	 * Handle.
	 *
	 * @param tradeAccept
	 *            the trade accept
	 */
	protected void handle(ProtocolTradeAccept tradeAccept) {
		int tradeID = tradeAccept.getTradeID();
		boolean acceptFlag = tradeAccept.getAccepted();
		serverController.acceptTrade(currentThreadID, tradeID, acceptFlag);
	}

	/**
	 * Handle.
	 *
	 * @param tradeComplete
	 *            the trade complete
	 */
	protected void handle(ProtocolTradeComplete tradeComplete) {
		int tradeID = tradeComplete.getTradeID();
		int tradePartnerID = tradeComplete.getTradePartnerID();
		serverController.fulfillTrade(currentThreadID, tradeID, tradePartnerID);
	}

	/**
	 * Handle.
	 *
	 * @param tradeCancel
	 *            the trade cancel
	 */
	protected void handle(ProtocolTradeCancel tradeCancel) {
		int tradeID = tradeCancel.getTradeID();
		serverController.cancelTrade(currentThreadID, tradeID);
	}

	/**
	 * Handle.
	 *
	 * @param playInventionCard
	 *            the play invention card
	 */
	protected void handle(ProtocolPlayInventionCard playInventionCard) {
		int[] resources = ProtocolToModel.convertResources(playInventionCard.getResource());
		serverController.playInventionCard(currentThreadID, resources);

	}

	/**
	 * Handle.
	 *
	 * @param monopolyCard
	 *            the monopoly card
	 */
	protected void handle(ProtocolPlayMonopolyCard monopolyCard) {
		serverController.playMonopolyCard(currentThreadID, monopolyCard.getResourceType());
	}

	/**
	 * Handle.
	 *
	 * @param knightCard
	 *            the knight card
	 */
	protected void handle(ProtocolPlayKnightCard knightCard) {
		String fieldID = ProtocolToModel.getProtocolOneID(knightCard.getLocationID());
		int coords[] = ProtocolToModel.getFieldCoordinates(fieldID);
		serverController.playKnightCard(currentThreadID, coords[0], coords[1], knightCard.getVictimID());

	}

	/**
	 * Handle.
	 *
	 * @param roadBuildingCard
	 *            the road building card
	 */
	protected void handle(ProtocolPlayRoadCard roadBuildingCard) {
		int coords1[] = ProtocolToModel.getEdgeCoordinates(roadBuildingCard.getRoadID1());
		int coords2[] = ProtocolToModel.getEdgeCoordinates(roadBuildingCard.getRoadID2());
		serverController.playStreetCard(currentThreadID, coords1[0], coords1[1], coords1[2], coords2[0], coords1[1],
				coords2[2]);

	}

	/**
	 * Handle.
	 *
	 * @param cheat
	 *            the cheat
	 */
	protected void handle(ProtocolCheat cheat) {
		if (cheat.getCheatCode() == null) {
			serverController.serverResponse(currentThreadID, "Unzul�ssige Cheatcode");
		} else {
			CheatHandler ch = new CheatHandler(serverController.getServer());
			ch.handle(currentThreadID, cheat.getCheatCode());
		}
	}

	/**
	 * Gets the server controller.
	 *
	 * @return the server controller
	 */
	public ServerController getServerController() {

		return this.serverController;
	}

	/**
	 * Lost connection.
	 *
	 * @param threadID
	 *            the thread ID
	 */
	public void lostConnection(int threadID) {
		serverController.connectionLost(threadID);
		// TODO Auto-generated method stub

	}

}
