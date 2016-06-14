package network.client.client;

import java.util.ArrayList;

import enums.CardType;
import enums.ResourceType;
import model.HexService;
import model.objects.Corner;
import model.objects.Edge;
import model.objects.Field;
import network.InputHandler;
import network.ProtocolToModel;
import network.client.controller.ClientController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import protocol.clientinstructions.ProtocolBuildRequest;
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
import protocol.messaging.ProtocolChatReceiveMessage;
import protocol.messaging.ProtocolChatSendMessage;
import protocol.messaging.ProtocolServerResponse;
import protocol.object.ProtocolBoard;
import protocol.object.ProtocolBuilding;
import protocol.object.ProtocolField;
import protocol.object.ProtocolHarbour;
import protocol.object.ProtocolPlayer;
import protocol.object.ProtocolResource;
import protocol.serverinstructions.ProtocolBuild;
import protocol.serverinstructions.ProtocolCosts;
import protocol.serverinstructions.ProtocolDiceRollResult;
import protocol.serverinstructions.ProtocolResourceObtain;
import protocol.serverinstructions.ProtocolRobberMovement;
import protocol.serverinstructions.ProtocolStatusUpdate;
import protocol.serverinstructions.trade.ProtocolTradeConfirmation;
import protocol.serverinstructions.trade.ProtocolTradeIsCanceled;
import protocol.serverinstructions.trade.ProtocolTradeIsCompleted;
import protocol.serverinstructions.trade.ProtocolTradePreview;
import protocol3.clientinstructions.ProtocolBuyDevelopmentCards;
import protocol3.clientinstructions.ProtocolDevelopmentCards;
import protocol3.object.ProtocolInventionCard;
import protocol3.object.ProtocolMonopolyCard;
import protocol3.object.ProtocolRoadBuildingCard;
import protocol3.serverinstructions.ProtocolBiggestKnightProwess;
import protocol3.serverinstructions.ProtocolBoughtDevelopmentCard;
import protocol3.serverinstructions.ProtocolInventionCardInfo;
import protocol3.serverinstructions.ProtocolLongestRoad;
import protocol3.serverinstructions.ProtocolMonopolyCardInfo;
import protocol3.serverinstructions.ProtocolPlayKnightCard;
import protocol3.serverinstructions.ProtocolRoadBuildingCardInfo;

public class ClientInputHandler extends InputHandler {
	private static Logger logger = LogManager.getLogger(ClientInputHandler.class.getName());
	private ClientController clientController;

	/**
	 * @param clientController
	 */
	public ClientInputHandler(ClientController clientController) {
		super();
		this.clientController = clientController;
	}

	/**
	 * sends JSON formatted string to parser and initiates handling of parsed
	 * object
	 *
	 * @param s
	 */
	/**
	 * @param s
	 */
	public void sendToParser(String s) {
		Object object = parser.parseString(s);
		handle(object);
	}

	@Override
	protected void handle(ProtocolHello hello) {
		clientController.serverHello(hello.getVersion(), hello.getProtocol());
	}
	
	@Override
	protected void handle(ProtocolWelcome welcome) {
		clientController.welcome(welcome.getPlayerID());
	}

	@Deprecated
	@Override
	protected void handle(ProtocolClientReady clientReady) {
		// unnecessary Method in ClientInputHandler

	}

	@Override
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

	@Override
	protected void handle(ProtocolError error) {
		System.out.println("Meldung wird geschickt");
		logger.info("Meldung wird geschickt");
		clientController.error(error.getNotice());

	}

	@Override
	protected void handle(ProtocolPlayerProfile playerProfile) {
		// unnecessary Method in ClientInputHandler

	}

	@Override
	protected void handle(ProtocolChatReceiveMessage chatReceiveMessage) {
		String s = chatReceiveMessage.getMessage();
		Integer playerId = chatReceiveMessage.getSender();
		clientController.chatReceiveMessage(playerId, s);
	}

	@Override
	protected void handle(ProtocolChatSendMessage chatSendMessage) {
		// unnecessary Method in ClientInputHandler

	}

	@Override
	protected void handle(ProtocolServerResponse serverConfirmation) {
		String server_response = serverConfirmation.getServerResponse();
		clientController.receiveServerConfirmation(server_response);
	}

	@Override
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

	@Override
	protected void handle(ProtocolDiceRollResult diceRollResult) {
		int playerID = diceRollResult.getPlayerID();
		int[] result = diceRollResult.getRoll();
		clientController.diceRollResult(playerID, result);
	}

	@Override
	protected void handle(ProtocolResourceObtain resourceObtain) {
		int playerID = resourceObtain.getPlayerID();
		ProtocolResource pr = resourceObtain.getResource();
		// Amount of Landscape Resource Cards: {WOOD, CLAY, ORE, SHEEP, CORN,
		int[] result = ProtocolToModel.convertResources(pr);
		clientController.resourceObtain(playerID, result);

	}

	@Override
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
		// check if it's self or another will be done in clientcontroller not
		// handler
		// if (pRes.getUnknown() == null && pRes.getClay() == null) {
		// int[] empty = {0, 0, 0, 0, 0};
		// clientController.statusUpdate(threadID, color, name, status,
		// victoryPoints, empty);
		// } else {
		// if (pRes.getUnknown() != null) {
		// int[] resources = {pRes.getUnknown()};
		// clientController.statusUpdate(threadID, color, name, status,
		// victoryPoints, resources);
		//
		// } else {
		// will be length 5 or 1
		int[] resources = ProtocolToModel.convertResources(pRes);
		clientController.statusUpdate(threadID, color, name, status, victoryPoints, resources);
		// }
		// }
	}

	@Override
	protected void handle(ProtocolBuildRequest buildRequest) {
		// unnecessary Method in ClientInputHandler

	}

	@Override
	protected void handle(ProtocolDiceRollRequest diceRollRequest) {
		// unnecessary Method in ClientInputHandler

	}

	@Override
	protected void handle(ProtocolEndTurn endTurn) {

		System.out.println("Der Zug wurde beendet");
		logger.info("Der Zug wurde beendet");
		clientController.endTurn();
		// unnecessary Method in ClientInputHandler
	}

	protected void handle(ProtocolRobberLoss losses) {
		// Unnecessary Method

	}

	@Override
	protected void handle(ProtocolRobberMovementRequest robberMovementRequest) {
		// Unnecessary Method
	}

	protected void handle(ProtocolTradePreview tradePreview) {
		int playerID = tradePreview.getTradeID();
		int tradeID = tradePreview.getTradeID();
		
		//hier läuft was schief, wenn ProtocolTradePreview geparst wird. denn dann ist unbekannt = 0 und nicht null!!!
		// Protocol to Model.convertResources funktioniert dann nicht.
		int[] offer = ProtocolToModel.convertResources(tradePreview.getOffer());
		int[] demand = ProtocolToModel.convertResources(tradePreview.getWithdrawal());
		clientController.receiveTrade(playerID, tradeID, offer, demand);
	}

	protected void handle(ProtocolTradeConfirmation tradeConfirmation) {
		int playerID = tradeConfirmation.getPlayerID();
		int tradeID = tradeConfirmation.getTradeID();
		clientController.tradeAccepted(playerID, tradeID);
	}

	protected void handle(ProtocolTradeIsCanceled tradeIsCanceled) {
		int playerID = tradeIsCanceled.getPlayerID();
		int tradeID = tradeIsCanceled.getTradeID();
		clientController.tradeCancelled(playerID, tradeID);
	}

	protected void handle(ProtocolTradeIsCompleted tradeIsCompleted) {
	
		int playerID = tradeIsCompleted.getPlayerID();
		int tradePartnerID = tradeIsCompleted.getTradePartnerID();
		clientController.tradeFulfilled(playerID, tradePartnerID);
	}

	@Override
	protected void handle(ProtocolBuyDevelopmentCards buyDevelopmentCards) {
		// unnecessary Method
	}

	@Override
	protected void handle(ProtocolDevelopmentCards developmentCards) {
		// unnecessary Method
	}

	@Override
	protected void handle(ProtocolInventionCard inventionCard) {
		// unnecessary Method
	}

	@Override
	protected void handle(ProtocolMonopolyCard monopolyCard) {
		// unnecessary Method
	}

	@Override
	protected void handle(ProtocolRoadBuildingCard roadBuildingCard) {
		// unnecessary Method
	}
	
	@Override
	protected void handle(ProtocolVictory victory) {
		String message = victory.getMessage();
		int winnerID = victory.getWinnerID();
		clientController.victory(message, winnerID);

	}

	@Override
	protected void handle(ProtocolCosts costs) {
		int playerID = costs.getPlayerID();
		ProtocolResource pr = costs.getResource();
		int[] resources = ProtocolToModel.convertResources(pr);
		clientController.costs(playerID, resources);

	}

	@Override
	protected void handle(ProtocolRobberMovement robberMovement) {

		int playerID = robberMovement.getPlayerID();
		String locationID = robberMovement.getLocationID();
		int victimID = robberMovement.getVictimID();
		clientController.robberMovement(locationID);

	}

	@Override
	protected void handle(ProtocolHarbourRequest harbourRequest) {
		// unnecessary Method

	}

	@Override
	protected void handle(ProtocolTradeRequest tradeRequest) {
		// unnecessary Method

	}

	@Override
	protected void handle(ProtocolTradeAccept tradeAccept) {
		// unnecessary Method

	}

	@Override
	protected void handle(ProtocolTradeComplete tradeComplete) {
		int playerID = tradeComplete.getTradeID();
		int partnerID = tradeComplete.getTradePartnerID();
		clientController.tradeFulfilled(playerID, partnerID);
	}

	@Override
	protected void handle(ProtocolTradeCancel tradeCancel) {
		// unnecessary Method

	}

	@Override
	protected void handle(ProtocolBiggestKnightProwess biggestKnightProwess) {
		int playerID = biggestKnightProwess.getPlayer_id();
		// networkController.biggestKnightProwess(player_id);

	}

	@Override
	protected void handle(ProtocolInventionCardInfo inventionCardInfo) {
		ProtocolResource resource = inventionCardInfo.getResource();
		int playerID = inventionCardInfo.getPlayer_id();
		// networkController.inventionCardInfo(resource,player_id);
	}

	@Override
	protected void handle(ProtocolLongestRoad longestRoad) {
		int playerID = longestRoad.getPlayer_id();
		// networkController.longestRoad(player_id);

	}

	@Override
	protected void handle(ProtocolMonopolyCardInfo monopolyCardInfo) {
		ResourceType resourceType = monopolyCardInfo.getResourceType();
		Integer playerID = monopolyCardInfo.getPlayer_id();
		// networkController.monopolyCardInfo(resourceType,player_id)

	}

	@Override
	protected void handle(ProtocolPlayKnightCard playKnightCard) {
		String road1_id = playKnightCard.getRoad1_id();
		int target = playKnightCard.getTarget();
		Integer playerID = playKnightCard.getPlayer_id();

		// networkController.playKnightCard(road1_id,target,player_id)

	}

	@Override
	protected void handle(ProtocolRoadBuildingCardInfo roadBuildingCardInfo) {
		String road1_id = roadBuildingCardInfo.getRoad1_id();
		String road2_idx = roadBuildingCardInfo.getRoad2_id();
		Integer playerID = roadBuildingCardInfo.getPlayer_id();
		// networkController.roadBuildingCardInfo(road1_id,road2_id,player_id)
	}

	@Override
	protected void handle(ProtocolBoughtDevelopmentCard boughtDevelopmentCard) {
		int playerID = boughtDevelopmentCard.getPlayer_id();
		CardType developmentCard = boughtDevelopmentCard.getDevelopmentCard();
		// networkController.boughtDevelopmentCard(playerID,developmentCards;

	}

	@Override
	protected void handle(String string) {
	
		clientController.receiveServerConfirmation(string);
	}

}
