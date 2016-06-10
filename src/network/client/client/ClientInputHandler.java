package network.client.client;

import java.util.ArrayList;

import enums.ResourceType;
import model.objects.Edge;
import model.HexService;
import model.objects.Corner;
import model.objects.Field;
import network.InputHandler;
import network.ProtocolToModel;
import network.client.controller.ClientController;
import protocol.clientinstructions.*;
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
import protocol.messaging.ProtocolServerConfirmation;
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
import protocol.serverinstructions.trade.ProtocolTradeIsRequested;
import protocol3.clientinstructions.ProtocolBuyDevelopmentCards;
import protocol3.clientinstructions.ProtocolDevelopmentCards;
import protocol3.object.ProtocolInventionCard;
import protocol3.object.ProtocolMonopolyCard;
import protocol3.object.ProtocolRoadBuildingCard;
import protocol3.severinstructions.*;
import settings.DefaultSettings;

public class ClientInputHandler extends InputHandler {
    private ClientController clientController;

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
        clientController.welcome(welcome.getPlayer_id());
    }

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
            if (!pBuild.getBuilding().equals("Straße")) {
                corners[i] = new Corner();
                corners[i].setCornerID(pBuild.getId());
                corners[i].setOwnerID(pBuild.getPlayer_id());
                corners[i].setStatus(ProtocolToModel.getCornerType(pBuild.getBuilding()));
            } else {
                Edge e = new Edge();
                streets.add(e);
                e.setEdgeID(pBuild.getId());
                e.setOwnedByPlayer(pBuild.getPlayer_id());
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
        clientController.error(error.getNotice());

    }

    @Override
    protected void handle(ProtocolPlayerProfile playerProfile) {
        // unnecessary Method in ClientInputHandler

    }

    @Override
    protected void handle(ProtocolChatReceiveMessage chatReceiveMessage) {
        String s = chatReceiveMessage.getMessage();
        int playerId = chatReceiveMessage.getSender();
        clientController.chatReceiveMessage(playerId, s);
    }

    @Override
    protected void handle(ProtocolChatSendMessage chatSendMessage) {
        // unnecessary Method in ClientInputHandler

    }

    @Override
    protected void handle(ProtocolServerConfirmation serverConfirmation) {
        String server_response = serverConfirmation.getServer_response();
        clientController.receiveServerConfirmation(server_response);
    }

    //
    @Override
    protected void handle(ProtocolBuild build) {
        ProtocolBuilding building = build.getBuilding();
        // networkController.build(building);

    }

    @Override
    protected void handle(ProtocolDiceRollResult diceRollResult) {
        int playerID = diceRollResult.getPlayer();
        int[] result = diceRollResult.getRoll();
        clientController.diceRollResult(playerID, result);
    }

    @Override
    protected void handle(ProtocolResourceObtain resourceObtain) {
        // int playerID = ProtocolResourceObtain.getPlayer();
        // int[]
        // resources=ProtocolToModel.getResources(resourceObtain.getPlayer().getResources());
        // networkController.resourceObtain(playerID, resources);

    }

    @Override
    protected void handle(ProtocolStatusUpdate statusUpdate) {
        ProtocolPlayer pPlayer = statusUpdate.getPlayer();
        int threadID =  pPlayer.getPlayerID();
        enums.Color color = pPlayer.getColor();
        String name = pPlayer.getName();
        enums.PlayerState status = pPlayer.getStatus();
        int victoryPoints = pPlayer.getVictoryPoints();
        ProtocolResource pRes = pPlayer.getResources();
        if (pRes.getUnknown() != null){
        	int[] resources = {pRes.getUnknown()};
        	clientController.statusUpdate(threadID, color, name, status, victoryPoints, resources);

        } else{
        	int[] resources = {pRes.getWood(),pRes.getClay(),pRes.getOre(),pRes.getWool(),pRes.getCorn()};
        	clientController.statusUpdate(threadID, color, name, status, victoryPoints, resources);
        }
    }

    //
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
        clientController.endTurn();
        // unnecessary Method in ClientInputHandler
    }

    @Override
    protected void handle(String string) {

        clientController.receiveServerConfirmation(string);
    }

    protected void handle(ProtocolRobberLoss losses) {
        // Unnecessary Method

    }

    @Override
    protected void handle(ProtocolRobberMovementRequest robberMovementRequest) {
        // Unnecessary Method
    }

    protected void handle(ProtocolTradeIsRequested tradeIsRequested) {
        int player_id = tradeIsRequested.getPlayer_id();
        int trade_id = tradeIsRequested.getTrade_id();
        ProtocolResource proff = tradeIsRequested.getOffer();
        ProtocolResource prw = tradeIsRequested.getWithdrawal();
        // networkController.tradeIsRequested((player_id, trade_id, proff,prw));
    }

    protected void handle(ProtocolTradeConfirmation tradeConfirmation) {

        int player_id = tradeConfirmation.getPlayer_id();
        int trade_id = tradeConfirmation.getTrade_id();
        // networkController.tradeConfirmation(player_id,trade_id);
    }

    protected void handle(ProtocolTradeIsCanceled tradeIsCanceled) {

        int player_id = tradeIsCanceled.getPlayer_id();
        int trade_id = tradeIsCanceled.getTrade_id();
        // networkController.tradeIsCanceled(player_id,trade_id);
    }

    @Override
    protected void handle(ProtocolBuyDevelopmentCards buyDevelopmentCards) {
        // unnecessary Method
    }

    @Override
    protected void handle(ProtocolDevelopmentCards developmentCards) {
        //TODO
    }

    @Override
    protected void handle(ProtocolInventionCard inventionCard) {
        //TODO
    }

    @Override
    protected void handle(ProtocolMonopolyCard monopolyCard) {
        //TODO
    }

    @Override
    protected void handle(ProtocolRoadBuildingCard roadBuildingCard) {
        // TODO
    }

    protected void handle(ProtocolTradeIsCompleted tradeIsCompleted) {

        int player_id = tradeIsCompleted.getPlayer_id();
        int tradePartner_id = tradeIsCompleted.getTradePartner_id();
        // networkController.tradeIsCompleted(player_id,tradePartner_id);
    }

    @Override
    protected void handle(ProtocolVictory victory) {
        String message = victory.getMessage();
        int winner_id = victory.getWinner_id();
        //networkController.victory(message,winner_id);

    }

    @Override
    protected void handle(ProtocolCosts costs) {
        int playerID = costs.getPlayer();
        ProtocolResource pr = costs.getResource();
        // networkController.costs(playerID,pr)
    }

    @Override
    protected void handle(ProtocolRobberMovement robberMovement) {

        int playerID = robberMovement.getPlayer_id();
        String locationID = robberMovement.getLocation_id();
        int victimID = robberMovement.getVictim_id();
        // networkController.robberMovement(playerID,locationID,victimID);

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
        // unnecessary Method

    }

    @Override
    protected void handle(ProtocolTradeCancel tradeCancel) {
        // unnecessary Method

    }

    @Override
    protected void handle(ProtocolBiggestKnightProwess biggestKnightProwess) {
        int player_id = biggestKnightProwess.getPlayer_id();
        // networkController.biggestKnightProwess(player_id);

    }

    @Override
    protected void handle(ProtocolInventionCardInfo inventionCardInfo) {
        ProtocolResource resource = inventionCardInfo.getResource();
        int player_id = inventionCardInfo.getPlayer_id();
        // networkController.inventionCardInfo(resource,player_id);
    }

    @Override
    protected void handle(ProtocolLongestRoad longestRoad) {
        int playerID = longestRoad.getPlayer_id();
        // networkController.longestRoad(playerID);

    }

    @Override
    protected void handle(ProtocolMonopolyCardInfo monopolyCardInfo) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void handle(ProtocolPlayKnightCard playKnightCard) {
        String road1_id = playKnightCard.getRoad1_id();
        int target = playKnightCard.getTarget();
        int player_id = playKnightCard.getPlayer_id();

        // networkController.playKnightCard(road1_id,target,player_id)

    }

    @Override
    protected void handle(ProtocolRoadBuildingCardInfo roadBuildingCardInfo) {
        String road1_id = roadBuildingCardInfo.getRoad1_id();
        String road2_idx = roadBuildingCardInfo.getRoad2_id();
        int player_id = roadBuildingCardInfo.getPlayer_id();
        // networkController.roadBuildingCardInfo(road1_id,road2_id,player_id)
    }

    @Override
    protected void handle(ProtocolBoughtDevelopmentCard boughtDevelopmentCard) {
        int playerID = boughtDevelopmentCard.getPlayer_id();
        ProtocolDevelopmentCards developmentCards = boughtDevelopmentCard.getDevelopmentCards();
        // networkController.boughtDevelopmentCard(playerID,developmentCards;

    }

    @Override
    protected void handle(ProtocolSpecialCaseLongestRoad specialCaseLongestRoad) {
      // networController.specialCaseLongestRoad();
    }

}
