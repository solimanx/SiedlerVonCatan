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

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.connection.ProtocolHello)
     */
    @Override
    protected void handle(ProtocolHello hello) {
        clientController.serverHello(hello.getVersion(), hello.getProtocol());
    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.connection.ProtocolWelcome)
     */
    @Override
    protected void handle(ProtocolWelcome welcome) {
        clientController.welcome(welcome.getPlayerID());
    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.configuration.ProtocolClientReady)
     */
    @Deprecated
    @Override
    protected void handle(ProtocolClientReady clientReady) {
        // unnecessary Method in ClientInputHandler

    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.configuration.ProtocolGameStarted)
     */
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

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.configuration.ProtocolError)
     */
    @Override
    protected void handle(ProtocolError error) {
        System.out.println("Meldung wird geschickt");
        logger.info("Meldung wird geschickt");
        clientController.error(error.getNotice());

    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.configuration.ProtocolPlayerProfile)
     */
    @Override
    protected void handle(ProtocolPlayerProfile playerProfile) {
        // unnecessary Method in ClientInputHandler

    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.messaging.ProtocolChatReceiveMessage)
     */
    @Override
    protected void handle(ProtocolChatReceiveMessage chatReceiveMessage) {
        String s = chatReceiveMessage.getMessage();
        Integer playerId = chatReceiveMessage.getSender();
        clientController.chatReceiveMessage(playerId, s);
    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.messaging.ProtocolChatSendMessage)
     */
    @Override
    protected void handle(ProtocolChatSendMessage chatSendMessage) {
        // unnecessary Method in ClientInputHandler

    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.messaging.ProtocolServerResponse)
     */
    @Override
    protected void handle(ProtocolServerResponse serverConfirmation) {
        String server_response = serverConfirmation.getServerResponse();
        clientController.receiveServerConfirmation(server_response);
    }

    //
    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.serverinstructions.ProtocolBuild)
     */
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

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.serverinstructions.ProtocolDiceRollResult)
     */
    @Override
    protected void handle(ProtocolDiceRollResult diceRollResult) {
        int playerID = diceRollResult.getPlayerID();
        int[] result = diceRollResult.getRoll();
        clientController.diceRollResult(playerID, result);
    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.serverinstructions.ProtocolResourceObtain)
     */
    @Override
    protected void handle(ProtocolResourceObtain resourceObtain) {
        int playerID = resourceObtain.getPlayerID();
        ProtocolResource pr = resourceObtain.getResource();
        // Amount of Landscape Resource Cards: {WOOD, CLAY, ORE, SHEEP, CORN,
        int[] result = ProtocolToModel.convertResources(pr);
        clientController.resourceObtain(playerID, result);

    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.serverinstructions.ProtocolStatusUpdate)
     */
    @Override
    protected void handle(ProtocolStatusUpdate statusUpdate) {
        //get player object
        ProtocolPlayer pPlayer = statusUpdate.getPlayer();
        //get ID which is "32" or "42" etc.
        int threadID = pPlayer.getPlayerID();
        //get color
        enums.Color color = pPlayer.getColor();
        // get name
        String name = pPlayer.getName();
        // status
        enums.PlayerState status = pPlayer.getStatus();
        // victory points
        int victoryPoints = pPlayer.getVictoryPoints();
        // and resources
        ProtocolResource pRes = pPlayer.getResources();
        // check if it's self or another will be done in clientcontroller not handler
//        if (pRes.getUnknown() == null && pRes.getClay() == null) {
//            int[] empty = {0, 0, 0, 0, 0};
//            clientController.statusUpdate(threadID, color, name, status, victoryPoints, empty);
//        } else {
//            if (pRes.getUnknown() != null) {
//                int[] resources = {pRes.getUnknown()};
//                clientController.statusUpdate(threadID, color, name, status, victoryPoints, resources);
//
//            } else {
        //will be length 5 or 1
        int[] resources = ProtocolToModel.convertResources(pRes);
        clientController.statusUpdate(threadID, color, name, status, victoryPoints, resources);
        // }
        // }
    }

    //
    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.clientinstructions.ProtocolBuildRequest)
     */
    @Override
    protected void handle(ProtocolBuildRequest buildRequest) {
        // unnecessary Method in ClientInputHandler

    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.clientinstructions.ProtocolDiceRollRequest)
     */
    @Override
    protected void handle(ProtocolDiceRollRequest diceRollRequest) {
        // unnecessary Method in ClientInputHandler

    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.clientinstructions.ProtocolEndTurn)
     */
    @Override
    protected void handle(ProtocolEndTurn endTurn) {

        System.out.println("Der Zug wurde beendet");
        logger.info("Der Zug wurde beendet");
        clientController.endTurn();
        // unnecessary Method in ClientInputHandler
    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(java.lang.String)
     */
    @Override
    protected void handle(String string) {

        clientController.receiveServerConfirmation(string);
    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.clientinstructions.ProtocolRobberLoss)
     */
    protected void handle(ProtocolRobberLoss losses) {
        // Unnecessary Method

    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.clientinstructions.ProtocolRobberMovementRequest)
     */
    @Override
    protected void handle(ProtocolRobberMovementRequest robberMovementRequest) {
        // Unnecessary Method
    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.serverinstructions.trade.ProtocolTradeIsRequested)
     */
    protected void handle(ProtocolTradePreview tradePreview) {
        int playerID = tradePreview.getPlayerID();
        int trade_id = tradePreview.getTradeID();
        ProtocolResource proff = tradePreview.getOffer();
        ProtocolResource prw = tradePreview.getWithdrawal();
        // networkController.tradeIsRequested((player_id, trade_id, proff,prw));
    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.serverinstructions.trade.ProtocolTradeConfirmation)
     */
    protected void handle(ProtocolTradeConfirmation tradeConfirmation) {

        int playerID = tradeConfirmation.getPlayerID();
        int trade_id = tradeConfirmation.getTradeID();
        // networkController.tradeConfirmation(player_id,trade_id);
    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.serverinstructions.trade.ProtocolTradeIsCanceled)
     */
    protected void handle(ProtocolTradeIsCanceled tradeIsCanceled) {

        int playerID = tradeIsCanceled.getPlayerID();
        int trade_id = tradeIsCanceled.getTradeID();
        // networkController.tradeIsCanceled(player_id,trade_id);
    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol3.clientinstructions.ProtocolBuyDevelopmentCards)
     */
    @Override
    protected void handle(ProtocolBuyDevelopmentCards buyDevelopmentCards) {
        // unnecessary Method
    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol3.clientinstructions.ProtocolDevelopmentCards)
     */
    @Override
    protected void handle(ProtocolDevelopmentCards developmentCards) {
        // unnecessary Method
    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol3.object.ProtocolInventionCard)
     */
    @Override
    protected void handle(ProtocolInventionCard inventionCard) {
        // unnecessary Method
    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol3.object.ProtocolMonopolyCard)
     */
    @Override
    protected void handle(ProtocolMonopolyCard monopolyCard) {
        // unnecessary Method
    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol3.object.ProtocolRoadBuildingCard)
     */
    @Override
    protected void handle(ProtocolRoadBuildingCard roadBuildingCard) {
        // unnecessary Method
    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.serverinstructions.trade.ProtocolTradeIsCompleted)
     */
    protected void handle(ProtocolTradeIsCompleted tradeIsCompleted) {

        int playerID = tradeIsCompleted.getPlayerID();
        int tradePartner_id = tradeIsCompleted.getTradePartnerID();
        // networkController.tradeIsCompleted(player_id,tradePartner_id);
    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.configuration.ProtocolVictory)
     */
    @Override
    protected void handle(ProtocolVictory victory) {
        String message = victory.getMessage();
        int winner_id = victory.getWinnerID();
        // networkController.victory(message,winner_id);

    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.serverinstructions.ProtocolCosts)
     */
    @Override
    protected void handle(ProtocolCosts costs) {
        int playerID = costs.getPlayerID();
        ProtocolResource pr = costs.getResource();
        if (pr.getUnknown() != null){
        	int[] resources = {pr.getWood(),pr.getClay(),pr.getOre(),pr.getWool(),pr.getCorn()};
        	clientController.costs(playerID,resources);
        }
        
    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.serverinstructions.ProtocolRobberMovement)
     */
    @Override
    protected void handle(ProtocolRobberMovement robberMovement) {

        int playerID = robberMovement.getPlayerID();
        String locationID = robberMovement.getLocationID();
        int victimID = robberMovement.getVictimID();
        clientController.robberMovement(locationID);

    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.clientinstructions.ProtocolHarbourRequest)
     */
    @Override
    protected void handle(ProtocolHarbourRequest harbourRequest) {
        // unnecessary Method

    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.clientinstructions.trade.ProtocolTradeRequest)
     */
    @Override
    protected void handle(ProtocolTradeRequest tradeRequest) {
        // unnecessary Method

    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.clientinstructions.trade.ProtocolTradeAccept)
     */
    @Override
    protected void handle(ProtocolTradeAccept tradeAccept) {
        // unnecessary Method

    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.clientinstructions.trade.ProtocolTradeComplete)
     */
    @Override
    protected void handle(ProtocolTradeComplete tradeComplete) {
        // unnecessary Method

    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol.clientinstructions.trade.ProtocolTradeCancel)
     */
    @Override
    protected void handle(ProtocolTradeCancel tradeCancel) {
        // unnecessary Method

    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol3.severinstructions.ProtocolBiggestKnightProwess)
     */
    @Override
    protected void handle(ProtocolBiggestKnightProwess biggestKnightProwess) {
        int playerID = biggestKnightProwess.getPlayer_id();
        // networkController.biggestKnightProwess(player_id);

    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol3.severinstructions.ProtocolInventionCardInfo)
     */
    @Override
    protected void handle(ProtocolInventionCardInfo inventionCardInfo) {
        ProtocolResource resource = inventionCardInfo.getResource();
        int playerID = inventionCardInfo.getPlayer_id();
        // networkController.inventionCardInfo(resource,player_id);
    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol3.severinstructions.ProtocolLongestRoad)
     */
    @Override
    protected void handle(ProtocolLongestRoad longestRoad) {
        int playerID = longestRoad.getPlayer_id();
        // networkController.longestRoad(player_id);

    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol3.severinstructions.ProtocolMonopolyCardInfo)
     */
    @Override
    protected void handle(ProtocolMonopolyCardInfo monopolyCardInfo) {
        ResourceType resourceType = monopolyCardInfo.getResourceType();
        Integer playerID = monopolyCardInfo.getPlayer_id();
        //networkController.monopolyCardInfo(resourceType,player_id)

    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol3.severinstructions.ProtocolPlayKnightCard)
     */
    @Override
    protected void handle(ProtocolPlayKnightCard playKnightCard) {
        String road1_id = playKnightCard.getRoad1_id();
        int target = playKnightCard.getTarget();
        Integer playerID = playKnightCard.getPlayer_id();

        // networkController.playKnightCard(road1_id,target,player_id)

    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol3.severinstructions.ProtocolRoadBuildingCardInfo)
     */
    @Override
    protected void handle(ProtocolRoadBuildingCardInfo roadBuildingCardInfo) {
        String road1_id = roadBuildingCardInfo.getRoad1_id();
        String road2_idx = roadBuildingCardInfo.getRoad2_id();
        Integer playerID = roadBuildingCardInfo.getPlayer_id();
        // networkController.roadBuildingCardInfo(road1_id,road2_id,player_id)
    }

    /* (non-Javadoc)
     * @see network.InputHandler#handle(protocol3.severinstructions.ProtocolBoughtDevelopmentCard)
     */
    @Override
    protected void handle(ProtocolBoughtDevelopmentCard boughtDevelopmentCard) {
        int playerID = boughtDevelopmentCard.getPlayer_id();
        CardType developmentCard = boughtDevelopmentCard.getDevelopmentCard();
        // networkController.boughtDevelopmentCard(playerID,developmentCards;

    }


}
