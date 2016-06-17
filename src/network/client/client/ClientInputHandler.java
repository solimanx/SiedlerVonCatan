package network.client.client;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import enums.ResourceType;
import model.HexService;
import model.objects.Corner;
import model.objects.Edge;
import model.objects.Field;
import model.objects.DevCards.DevelopmentCard;
import network.ProtocolToModel;
import network.client.controller.ClientController;
import parsing.Parser;
import protocol.configuration.ProtocolError;
import protocol.configuration.ProtocolGameStarted;
import protocol.configuration.ProtocolVictory;
import protocol.connection.ProtocolHello;
import protocol.connection.ProtocolWelcome;
import protocol.dualinstructions.ProtocolPlayInventionCard;
import protocol.dualinstructions.ProtocolPlayKnightCard;
import protocol.dualinstructions.ProtocolPlayMonopolyCard;
import protocol.dualinstructions.ProtocolPlayRoadCard;
import protocol.messaging.ProtocolChatReceiveMessage;
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
import protocol.serverinstructions.trade.ProtocolTradeCancellation;
import protocol.serverinstructions.trade.ProtocolTradeCompletion;
import protocol.serverinstructions.trade.ProtocolTradeConfirmation;
import protocol.serverinstructions.trade.ProtocolTradePreview;

/**
 * Relays information to Client controller.
 */
public class ClientInputHandler {
	private static Logger logger = LogManager.getLogger(ClientInputHandler.class.getName());
	private ClientController clientController;
	private Parser parser;

	/**
	 * Default constructor for ClientInputHandler
	 * 
	 * @param clientController
	 */
	public ClientInputHandler(ClientController clientController) {
		this.clientController = clientController;
		parser = new Parser();
	}

	/**
	 * Deciding which Protocol handling is going to be used
	 * 
	 * @param o
	 */
	protected void handle(Object o) {
		switch (o.getClass().getSimpleName()) {

		// Paragraph 5
		case "ProtocolHello":
			handle((ProtocolHello) o);
			break;
		case "ProtocolWelcome":
			handle((ProtocolWelcome) o);
			break;

		// Paragraph 7
		case "ProtocolServerResponse":
			handle((ProtocolServerResponse) o);
			break;
		case "String":
			handle(new ProtocolServerResponse((String) o));
			break;
		case "ProtocolChatReceiveMessage":
			handle((ProtocolChatReceiveMessage) o);
			break;

		// Paragraph 8
		case "ProtocolError":
			handle((ProtocolError) o);
			break;
		case "ProtocolGameStarted":
			handle((ProtocolGameStarted) o);
			break;
		case "ProtocolVictory":
			handle((ProtocolVictory) o);
			break;

		// Paragraph 9
		case "ProtocolStatusUpdate":
			handle((ProtocolStatusUpdate) o);
			break;
		case "ProtocolDiceRollResult":
			handle((ProtocolDiceRollResult) o);
			break;
		case "ProtocolResourceObtain":
			handle((ProtocolResourceObtain) o);
			break;
		case "ProtocolCosts":
			handle((ProtocolCosts) o);
			break;
		case "ProtocolBuild":
			handle((ProtocolBuild) o);
			break;
		case "ProtocolRobberMovement":
			handle((ProtocolRobberMovement) o);
			break;
		case "ProtocolBoughtDevelopmentCard":
			handle((ProtocolBoughtDevelopmentCard) o);
			break;
		case "ProtocolLongestRoad":
			handle((ProtocolLongestRoad) o);
			break;
		case "ProtocolLargestArmy":
			handle((ProtocolLargestArmy) o);
			break;

		// Paragraph 11
		case "ProtocolTradePreview":
			handle((ProtocolTradePreview) o);
			break;
		case "ProtocolTradeConfirmation":
			handle((ProtocolTradeConfirmation) o);
			break;
		case "ProtocolTradeCompletion":
			handle((ProtocolTradeCompletion) o);
			break;
		case "ProtocolTradeCancellation":
			handle((ProtocolTradeCancellation) o);
			break;

		// Paragraph 12
		case "ProtocolPlayKnightCard":
			handle((ProtocolPlayKnightCard) o);
			break;
		case "ProtocolPlayRoadCard":
			handle((ProtocolPlayRoadCard) o);
			break;
		case "ProtocolPlayInventionCard":
			handle((ProtocolPlayInventionCard) o);
			break;
		case "ProtocolPlayMonopolyCard":
			handle((ProtocolPlayMonopolyCard) o);
			break;

		default:
			System.out.println("Class not found");
			logger.warn("Class not found");
		}

	}

	/**
	 * Sends JSON formatted string to parser and initiates handling of parsed
	 * object.
	 *
	 * @param s
	 */
	public void sendToParser(String s) {
		Object object = parser.parseString(s);
		handle(object);
	}

	// Paragraph 5

	/**
	 * Transition to Controller on receiving : Hello
	 *
	 * @param hello
	 */
	protected void handle(ProtocolHello hello) {
		clientController.receiveHello(hello.getVersion(), hello.getProtocol());
	}

	/**
	 * Transition to Controller on receiving : Welcome
	 *
	 * @param welcome
	 */
	protected void handle(ProtocolWelcome welcome) {
		clientController.receiveWelcome(welcome.getPlayerID());
	}

	// Paragraph 7

	/**
	 * Transition to Controller on receiving : ServerResponse
	 *
	 * @param serverConfirmation
	 */
	protected void handle(ProtocolServerResponse serverConfirmation) {
		String server_response = serverConfirmation.getServerResponse();
		clientController.receiveServerConfirmation(server_response);
	}

	/**
	 * Transition to Controller on receiving : Chat Message
	 *
	 * @param chatReceiveMessage
	 */
	protected void handle(ProtocolChatReceiveMessage chatReceiveMessage) {
		String s = chatReceiveMessage.getMessage();
		Integer playerId = chatReceiveMessage.getSender();
		clientController.receiveChatMessage(playerId, s);
	}

	// Paragraph 8

	/**
	 * Transition to Controller on receiving : Error Message
	 *
	 * @param error
	 */
	protected void handle(ProtocolError error) {
		logger.debug("Meldung wird geschickt");
		clientController.receiveError(error.getNotice());

	}

	/**
	 * Transition to Controller on receiving : Game Started
	 *
	 * @param gameStarted
	 */

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
		clientController.initializeBoard(fields, corners, streets, harbourCorners, banditLocation);

	}

	protected void handle(ProtocolVictory victory) {
		String message = victory.getMessage();
		int winnerID = victory.getWinnerID();
		clientController.victory(message, winnerID);

	}

	// Paragraph 9

	/**
	 * Transition to Controller on receiving : Status Update
	 *
	 * @param statusUpdate
	 */
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
		clientController.receiveStatusUpdate(threadID, color, name, status, victoryPoints, resources);

	}

	/**
	 * Transition to Controller on receiving : Dice Roll result
	 *
	 * @param diceRollResult
	 */
	protected void handle(ProtocolDiceRollResult diceRollResult) {
		int playerID = diceRollResult.getPlayerID();
		int[] result = diceRollResult.getRoll();
		clientController.receiveDiceRollResult(playerID, result);
	}

	/**
	 * Transition to Controller on receiving : Resource Obtain
	 *
	 * @param resourceObtain
	 */
	protected void handle(ProtocolResourceObtain resourceObtain) {
		int playerID = resourceObtain.getPlayerID();
		ProtocolResource pr = resourceObtain.getResource();
		// Amount of Landscape Resource Cards: {WOOD, CLAY, ORE, SHEEP, CORN}
		int[] result = ProtocolToModel.convertResources(pr);
		clientController.resourceObtain(playerID, result);

	}

	/**
	 * Transition to Controller on receiving : Resource Costs
	 *
	 * @param costs
	 */
	protected void handle(ProtocolCosts costs) {
		int playerID = costs.getPlayerID();
		ProtocolResource pr = costs.getResource();
		// Amount of Landscape Resource Cards: {WOOD, CLAY, ORE, SHEEP, CORN}
		int[] resources = ProtocolToModel.convertResources(pr);
		clientController.costs(playerID, resources);

	}

	/**
	 * Transition to Controller on receiving : Robber New Position
	 *
	 * @param robberMovement
	 */
	protected void handle(ProtocolRobberMovement robberMovement) {
		// TODO smth with information
		int playerID = robberMovement.getPlayerID();
		String locationID = robberMovement.getLocationID();
		// TODO smth with information
		int victimID = robberMovement.getVictimID();
		clientController.robberMove(locationID);

	}

	/**
	 * Transition to Controller on receiving : Build process
	 *
	 * @param build
	 */
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

	// Paragraph 9.10

	/**
	 * Transition to Controller on receiving : Buying DevCard
	 *
	 * @param boughtDevelopmentCard
	 */
	protected void handle(ProtocolBoughtDevelopmentCard boughtDevelopmentCard) {
		ProtocolDevCard pdv = boughtDevelopmentCard.getDevelopmentCard();
		DevelopmentCard ct = ProtocolToModel.getCardType(pdv);
		int playerID = boughtDevelopmentCard.getPlayerID();
		clientController.addToDeck(playerID, ct);

	}

	/**
	 * Transition to Controller on receiving : Longest Road Card
	 *
	 * @param longestRoad
	 */
	protected void handle(ProtocolLongestRoad longestRoad) {
		Integer playerID = longestRoad.getPlayerID();
		clientController.longestRoad(playerID);

	}

	/**
	 * Transition to Controller on receiving : Largest Army Card
	 *
	 * @param largestArmy
	 */
	protected void handle(ProtocolLargestArmy largestArmy) {
		int playerID = largestArmy.getPlayerID();
		clientController.largestArmy(playerID);

	}

	// Paragraph 11

	/**
	 * Transition to Controller on receiving : Trade Preview
	 *
	 * @param tradePreview
	 */
	protected void handle(ProtocolTradePreview tradePreview) {
		int playerID = tradePreview.getPlayerID();
		int tradeID = tradePreview.getTradeID();

		int[] offer = ProtocolToModel.convertResources(tradePreview.getOffer());
		int[] demand = ProtocolToModel.convertResources(tradePreview.getWithdrawal());
		clientController.receiveTrade(playerID, tradeID, offer, demand);

	}

	/**
	 * Transition to Controller on receiving : Trade Confirmation
	 *
	 * @param tradeConfirmation
	 */
	protected void handle(ProtocolTradeConfirmation tradeConfirmation) {
		int playerID = tradeConfirmation.getPlayerID();
		int tradeID = tradeConfirmation.getTradeID();
		clientController.tradeAccepted(playerID, tradeID);
	}

	/**
	 * Transition to Controller on receiving : Trade Completion
	 *
	 * @param tradeIsCompleted
	 */
	protected void handle(ProtocolTradeCompletion tradeIsCompleted) {

		int playerID = tradeIsCompleted.getPlayerID();
		int tradePartnerID = tradeIsCompleted.getTradePartnerID();
		clientController.tradeFulfilled(playerID, tradePartnerID);
	}

	/**
	 * Transition to Controller on receiving : Trade Cancelation
	 *
	 * @param tradeIsCanceled
	 */
	protected void handle(ProtocolTradeCancellation tradeIsCanceled) {
		int playerID = tradeIsCanceled.getPlayerID();
		int tradeID = tradeIsCanceled.getTradeID();
		clientController.tradeCancelled(playerID, tradeID);
	}

	// Paragraph 12

	/**
	 * Transition to Controller on receiving : Knight Card
	 *
	 * @param knightCardInfo
	 */
	protected void handle(ProtocolPlayKnightCard knightCardInfo) {
		if (knightCardInfo.getPlayerID() != null) {
			// TODO smth with information
			int playerID = knightCardInfo.getPlayerID();
			String locationID = knightCardInfo.getLocationID();
			// TODO smth with information
			int victimID = knightCardInfo.getVictimID();
			clientController.robberMove(locationID);

		}

	}

	/**
	 * Transition to Controller on receiving : Road Card
	 *
	 * @param roadBuildingCardInfo
	 */
	protected void handle(ProtocolPlayRoadCard roadBuildingCardInfo) {
		if(roadBuildingCardInfo.getPlayerID()!=null){
			int playerID = roadBuildingCardInfo.getPlayerID();
			String locationID1 = roadBuildingCardInfo.getRoadID1();
			String locationID2 = roadBuildingCardInfo.getRoadID2();
			clientController.receiveRoadCard(playerID, locationID1, locationID2);
			
			
		}

	}

	/**
	 * Transition to Controller on receiving : Monopoly Card
	 *
	 * @param monopolyCardInfo
	 */

	protected void handle(ProtocolPlayMonopolyCard monopolyCardInfo) {
		if(monopolyCardInfo.getPlayerID()!=null){
			int playerID = monopolyCardInfo.getPlayerID();
			ResourceType rt = monopolyCardInfo.getResourceType();
			clientController.receiveMonopolyCard(playerID, rt);
		}

	}

	/**
	 * Transition to Controller on receiving : Invention Card
	 *
	 * @param inventionCardInfo
	 */
	protected void handle(ProtocolPlayInventionCard inventionCardInfo) {
		if(inventionCardInfo.getPlayerID()!=null){
			int playerID = inventionCardInfo.getPlayerID();
			int[] resource = ProtocolToModel.convertResources(inventionCardInfo.getResource());
			clientController.receiveInventionCard(playerID, resource);
		}

	}

	// Custom

	/**
	 * Transition to Controller on receiving non defined String
	 *
	 * @param string
	 */
	protected void handle(String string) {
		clientController.receiveServerConfirmation(string);
	}

}
