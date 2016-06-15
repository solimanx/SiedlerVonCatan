package network.client.client;

import java.util.ArrayList;

import enums.CardType;
import enums.ResourceType;
import model.HexService;
import model.objects.Corner;
import model.objects.Edge;
import model.objects.Field;
import network.ProtocolToModel;
import network.client.controller.ClientController;
import parsing.Parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import protocol.object.ProtocolBoard;
import protocol.object.ProtocolBuilding;
import protocol.object.ProtocolDevCard;
import protocol.object.ProtocolField;
import protocol.object.ProtocolHarbour;
import protocol.object.ProtocolPlayer;
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

public class ClientInputHandler {
	private static Logger logger = LogManager.getLogger(ClientInputHandler.class.getName());
	private ClientController clientController;
	private Parser parser;

	/**
	 * @param clientController
	 */
	public ClientInputHandler(ClientController clientController) {
		this.clientController = clientController;
		parser = new Parser();
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
			System.out.println("Class not found");
			logger.info("Class not found");
		}

	}

	/**
	 * sends JSON formatted string to parser and initiates handling of parsed
	 * object
	 *
	 * @param s
	 */
	public void sendToParser(String s) {
		Object object = parser.parseString(s);
		handle(object);
	}

	// Paragraph 5

	protected void handle(ProtocolHello hello) {
		clientController.serverHello(hello.getVersion(), hello.getProtocol());
	}

	protected void handle(ProtocolWelcome welcome) {
		clientController.welcome(welcome.getPlayerID());
	}

	// Paragraph 7
	protected void handle(ProtocolServerResponse serverConfirmation) {
		String server_response = serverConfirmation.getServerResponse();
		clientController.receiveServerConfirmation(server_response);
	}

	protected void handle(ProtocolChatReceiveMessage chatReceiveMessage) {
		String s = chatReceiveMessage.getMessage();
		Integer playerId = chatReceiveMessage.getSender();
		clientController.chatReceiveMessage(playerId, s);
	}

	// Paragraph 8

	protected void handle(ProtocolError error) {
		logger.info("Meldung wird geschickt");
		clientController.error(error.getNotice());

	}

	protected void handle(ProtocolGameStarted gameStarted) {
		// ProtocolBoard object retrieved (Karte: ...}
		ProtocolBoard pBoard = gameStarted.getBoard();
		Field[] fields = new Field[pBoard.getAmountFields()];
		for (int i = 0; i < pBoard.getAmountFields(); i++) {
			ProtocolField pField = pBoard.getProtocolField(i);
			fields[i] = new Field();
			fields[i].setDiceIndex(pField.getDiceIndex());
			fields[i].setFieldID(pField.getFieldID());
			fields[i].setResourceType(ProtocolToModel.getResourceType(pField.getFieldType()));
		}
		ArrayList<Edge> streets = new ArrayList<Edge>();
		Corner[] corners = new Corner[pBoard.getAmountBuildings()];
		for (int i = 0; i < corners.length; i++) {
			ProtocolBuilding pBuild = pBoard.getProtocolBuilding(i);
			if (!pBuild.getType().equals("Straße")) {
				corners[i] = new Corner();
				corners[i].setCornerID(pBuild.getID());
				corners[i].setOwnerID(pBuild.getPlayerID());
				corners[i].setStatus(ProtocolToModel.getCornerType(pBuild.getType()));
			} else {
				Edge e = new Edge();
				streets.add(e);
				e.setEdgeID(pBuild.getID());
				e.setOwnedByPlayer(pBuild.getPlayerID());
				e.setHasStreet(true);
			}

		}

		Corner[] harbourCorners = new Corner[pBoard.getAmountHarbours() * 2]; // *2
		// wegen
		// Harbours
		for (int i = 0; i < pBoard.getAmountHarbours(); i++) {
			ProtocolHarbour pHarb = pBoard.getHarbours(i);
			Corner c1 = new Corner();
			Corner c2 = new Corner();
			harbourCorners[2 * i] = c1;
			harbourCorners[2 * i + 1] = c2;
			String[] coords = HexService.getCornerFromEdge(pHarb.getID());
			c1.setCornerID(coords[0]);
			c2.setCornerID(coords[1]);

			c1.setHarbourStatus(pHarb.getType());
			c2.setHarbourStatus(pHarb.getType());
		}
		String banditLocation = pBoard.getRobber_location();
		clientController.initBoard(fields, corners, streets, harbourCorners, banditLocation);

	}

	protected void handle(ProtocolVictory victory) {
		String message = victory.getMessage();
		int winnerID = victory.getWinnerID();
		clientController.victory(message, winnerID);

	}

	protected void handle(ProtocolStatusUpdate statusUpdate) {
		// get player object
		ProtocolPlayer pPlayer = statusUpdate.getPlayer();
		// get ID which is "32" or "42" etc.
		int threadID = pPlayer.getPlayerID();
		// get color
		enums.Color color = pPlayer.getColor();
		// get name
		String name = pPlayer.getName();
		// status
		enums.PlayerState status = pPlayer.getStatus();
		// victory points
		int victoryPoints = pPlayer.getVictoryPoints();
		// and resources
		ProtocolResource pRes = pPlayer.getResources();

		int[] resources = ProtocolToModel.convertResources(pRes);
		clientController.statusUpdate(threadID, color, name, status, victoryPoints, resources);

	}

	protected void handle(ProtocolDiceRollResult diceRollResult) {
		int playerID = diceRollResult.getPlayerID();
		int[] result = diceRollResult.getRoll();
		clientController.diceRollResult(playerID, result);
	}

	protected void handle(ProtocolResourceObtain resourceObtain) {
		int playerID = resourceObtain.getPlayerID();
		ProtocolResource pr = resourceObtain.getResource();
		// Amount of Landscape Resource Cards: {WOOD, CLAY, ORE, SHEEP, CORN}
		int[] result = ProtocolToModel.convertResources(pr);
		clientController.resourceObtain(playerID, result);

	}

	protected void handle(ProtocolCosts costs) {
		int playerID = costs.getPlayerID();
		ProtocolResource pr = costs.getResource();
		// Amount of Landscape Resource Cards: {WOOD, CLAY, ORE, SHEEP, CORN}
		int[] resources = ProtocolToModel.convertResources(pr);
		clientController.costs(playerID, resources);

	}

	protected void handle(ProtocolRobberMovement robberMovement) {

		int playerID = robberMovement.getPlayerID();
		String locationID = robberMovement.getLocationID();
		int victimID = robberMovement.getVictimID();
		clientController.robberMovement(locationID);

	}

	protected void handle(ProtocolBuild build) {
		ProtocolBuilding building = build.getBuilding();
		int playerID = building.getPlayerID();
		int[] coords;
		if (building.getType().equals("Dorf")) {
			coords = ProtocolToModel.getCornerCoordinates(building.getID());
			clientController.buildVillage(coords[0], coords[1], coords[2], playerID);
		} else if (building.getType().equals("Straße")) {
			coords = ProtocolToModel.getEdgeCoordinates(building.getID());
			clientController.buildStreet(coords[0], coords[1], coords[2], playerID);

		} else if (building.getType().equals("Stadt")) {
			coords = ProtocolToModel.getCornerCoordinates(building.getID());
			clientController.buildCity(coords[0], coords[1], coords[2], playerID);
		} else {
			logger.warn("Throws new IllegalArgumentException,\"Building type not defined\" ");
			throw new IllegalArgumentException("Building type not defined");
		}

	}

	protected void handle(ProtocolBoughtDevelopmentCard boughtDevelopmentCard) {
		// TODO Auto-generated method stub

	}

	protected void handle(ProtocolLongestRoad longestRoad) {
		// TODO TWO CASES

	}

	protected void handle(ProtocolLargestArmy largestArmy) {
		// TODO Auto-generated method stub

	}

	protected void handle(ProtocolTradePreview tradePreview) {
		int playerID = tradePreview.getPlayerID();
		int tradeID = tradePreview.getTradeID();

		// FIXED
		int[] offer = ProtocolToModel.convertResources(tradePreview.getOffer());
		int[] demand = ProtocolToModel.convertResources(tradePreview.getWithdrawal());
		clientController.receiveTrade(playerID, tradeID, offer, demand);
	}

	protected void handle(ProtocolTradeConfirmation tradeConfirmation) {
		int playerID = tradeConfirmation.getPlayerID();
		int tradeID = tradeConfirmation.getTradeID();
		clientController.tradeAccepted(playerID, tradeID);
	}

	protected void handle(ProtocolTradeCompletion tradeIsCompleted) {

		int playerID = tradeIsCompleted.getPlayerID();
		int tradePartnerID = tradeIsCompleted.getTradePartnerID();
		clientController.tradeFulfilled(playerID, tradePartnerID);
	}

	protected void handle(ProtocolTradeCancellation tradeIsCanceled) {
		int playerID = tradeIsCanceled.getPlayerID();
		int tradeID = tradeIsCanceled.getTradeID();
		clientController.tradeCancelled(playerID, tradeID);
	}

	protected void handle(ProtocolBuyDevCard buyDevelopmentCards) {
		// TODO Auto-generated method stub

	}

	protected void handle(ProtocolDevCard developmentCards) {
		// TODO Auto-generated method stub

	}

	protected void handle(ProtocolPlayInventionCard inventionCardInfo) {
		// TODO Auto-generated method stub

	}

	protected void handle(ProtocolPlayMonopolyCard monopolyCardInfo) {
		// TODO Auto-generated method stub

	}

	protected void handle(ProtocolPlayKnightCard playKnightCard) {
		// TODO Auto-generated method stub

	}

	protected void handle(ProtocolPlayRoadCard roadBuildingCardInfo) {
		// TODO Auto-generated method stub

	}

	protected void handle(String string) {
		clientController.receiveServerConfirmation(string);
	}

}
