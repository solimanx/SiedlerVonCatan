package network.server.server;

import enums.Color;
import network.InputHandler;
import network.ProtocolToModel;
import network.server.controller.ServerController;
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
        serverController.playerProfileUpdate(color, name, currentThreadID);

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
        serverController.chatSendMessage(s, currentThreadID);
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
        if (buildRequest.getBuilding().equals("Straße")) {
            int[] loc = ProtocolToModel.getEdgeCoordinates(buildRequest.getLocation());
            serverController.requestBuildStreet(loc[0], loc[1], loc[2], currentThreadID);
        }
        if (buildRequest.getBuilding().equals("Dorf")) {
            int[] loc = ProtocolToModel.getCornerCoordinates(buildRequest.getLocation());
            serverController.requestBuildVillage(loc[0], loc[1], loc[2], currentThreadID);
        }
        if (buildRequest.getBuilding().equals("Stadt")) {
            int[] loc = ProtocolToModel.getCornerCoordinates(buildRequest.getLocation());
            serverController.requestBuildCity(loc[0], loc[1], loc[2], currentThreadID);
        }
    }

    @Override
    protected void handle(ProtocolDiceRollRequest diceRollRequest) {
        serverController.diceRollRequest(currentThreadID);

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

        String location_id = robberMovementRequest.getLocation_id();
        int[] coords = ProtocolToModel.getFieldCoordinates(location_id);
        int victim_id = robberMovementRequest.getVictim_id();
        //TODO: Was wenn victim_id nicht vorhanden?
        serverController.robberMovementRequest(coords[0], coords[1], victim_id, currentThreadID);


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

    @Override
    protected void handle(ProtocolVictory victory) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void handle(ProtocolCosts costs) {
        // Unnecessary Method

    }

    @Override
    protected void handle(ProtocolRobberMovement robberMovement) {

        //unnecessary Method

        }



    @Override
    protected void handle(ProtocolRobberLoss robberLoss) {
        ProtocolResource prl = robberLoss.getLosses();
        //networkController.robberLoss(prl);

    }

    @Override
    protected void handle(ProtocolTradeIsRequested tradeIsRequested) {
        // Unnecessary Method

    }

    @Override
    protected void handle(ProtocolTradeConfirmation tradeConfirmation) {
        // Unnecessary Method

    }

    @Override
    protected void handle(ProtocolTradeIsCompleted tradeIsCompleted) {
        // Unnecessary Method

    }

    @Override
    protected void handle(ProtocolTradeIsCanceled tradeIsCanceled) {
        // Unnecessary Method

    }

    @Override
    protected void handle(ProtocolBuyDevelopmentCards buyDevelopmentCards) {
        //TODO
    }

    @Override
    protected void handle(ProtocolDevelopmentCards developmentCards) {
        //TODO
    }

    @Override
    protected void handle(ProtocolBiggestKnightProwess biggestKnightProwess) {
        // Unnecessary Method

    }

    //@Override
    protected void handle(ProtocolInventionCard inventionCard) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void handle(ProtocolMonopolyCard monopolyCard) {
        //Unnecessary Method
    }

    @Override
    protected void handle(ProtocolRoadBuildingCard roadBuildingCard) {
        //TODO
    }

    @Override
    protected void handle(ProtocolLongestRoad longestRoad) {
        //Unnecessary Method

    }

    @Override
    protected void handle(ProtocolMonopolyCardInfo monopolyCardInfo) {
        // Unnecessary Method

    }

    @Override
    protected void handle(ProtocolPlayKnightCard playKnightCard) {
        // Unnecessary Method

    }

    @Override
    protected void handle(ProtocolRoadBuildingCardInfo roadBuildingCardInfo) {
        // Unnecessary Method

    }

    @Override
    protected void handle(ProtocolBoughtDevelopmentCard boughtDevelopmentCard) {

    }

    public ServerController getServerController() {

        return this.serverController;
    }

    public void hello(int threadID) {

        serverController.hello(threadID);
    }

    @Override
    protected void handle(ProtocolInventionCardInfo inventionCardInfo) {
        // Unnecessary Method

    }

    @Override
    protected void handle(ProtocolWelcome welcome) {
        // Unnecessary Method

    }

    protected void handleRoadBuild(String road1_id, String road2_id, int player_id) {
        // TODO Auto-generated method stub


    }

}
