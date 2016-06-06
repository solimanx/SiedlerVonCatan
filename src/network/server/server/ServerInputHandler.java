package network.server.server;

import enums.Color;
import network.InputHandler;
import network.ProtocolToModel;
import network.client.controller.ClientNetworkController;
import network.server.controller.ServerNetworkController;
import protocol.clientinstructions.*;
import protocol.clientinstructions.trade.ProtocolTradeAccept;
import protocol.clientinstructions.trade.ProtocolTradeCancel;
import protocol.clientinstructions.trade.ProtocolTradeRequest;
import protocol.configuration.ProtocolClientReady;
import protocol.configuration.ProtocolError;
import protocol.configuration.ProtocolGameStarted;
import protocol.configuration.ProtocolPlayerProfile;
import protocol.connection.ProtocolHello;
import protocol.connection.ProtocolWelcome;
import protocol.messaging.ProtocolChatReceiveMessage;
import protocol.messaging.ProtocolChatSendMessage;
import protocol.messaging.ProtocolServerConfirmation;
import protocol.object.ProtocolResource;
import protocol.serverinstructions.ProtocolBuild;
import protocol.serverinstructions.ProtocolDiceRollResult;
import protocol.serverinstructions.ProtocolResourceObtain;
import protocol.serverinstructions.ProtocolStatusUpdate;

public class ServerInputHandler extends InputHandler {
    private ServerNetworkController serverNetworkController;
    private int currentThreadID;

    public ServerInputHandler(ServerNetworkController nc) {
        super();
        this.serverNetworkController = nc;
    }

    public ServerNetworkController getServerNetworkController() {
        return serverNetworkController;
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
        serverNetworkController.welcome(serverNetworkController.getPlayerModelId(currentThreadID));

    }

    @Override
    protected void handle(ProtocolWelcome welcome) {
        System.out.println("Welcome gelesen!");
    }

    // unnecessary Method in ServerInputHandler
    @Override
    protected void handle(ProtocolClientReady clientReady) {
        serverNetworkController.clientReady(serverNetworkController.getPlayerModelId(currentThreadID));

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
        ClientNetworkController clientNetworkController = new ClientNetworkController();
        clientNetworkController.sendPlayerProfile(color, name);


    }

    @Override
    protected void handle(ProtocolChatReceiveMessage chatReceiveMessage) {
        String s = chatReceiveMessage.getMessage();
        int playerId = chatReceiveMessage.getSender();
        serverNetworkController.chatReceiveMessage(playerId, s);
        /*
         * ChatRecieveMessage, (Nachricht wird vom Server verteilt) needs to be
		 * handled only in ServerOutputHandler and in ClientInputHandler.
		 * unnecessary Method in ServerInputHandler
		 */
    }

    protected void handle(ProtocolChatSendMessage chatSendMessage) {
        String s = chatSendMessage.getMessage();
        serverNetworkController.chatSendMessage(s, this.currentThreadID);
    }

    @Override
    protected void handle(ProtocolServerConfirmation serverConfirmation) {
        String server_response = serverConfirmation.getServer_response();
        serverNetworkController.serverConfirmation(server_response);
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
            serverNetworkController.requestBuildStreet(loc[0], loc[1], loc[2],
                    serverNetworkController.getPlayerModelId(this.currentThreadID));
        }
        if (buildRequest.getBuilding() == "Dorf") {
            int[] loc = ProtocolToModel.getCornerCoordinates(buildRequest.getLocation());
            serverNetworkController.requestBuildVillage(loc[0], loc[1], loc[2],
                    serverNetworkController.getPlayerModelId(this.currentThreadID));
        }
        if (buildRequest.getBuilding() == "Stadt") {
            int[] loc = ProtocolToModel.getCornerCoordinates(buildRequest.getLocation());
            serverNetworkController.requestBuildCity(loc[0], loc[1], loc[2],
                    serverNetworkController.getPlayerModelId(this.currentThreadID));
        }
    }

    @Override
    protected void handle(ProtocolDiceRollRequest diceRollRequest) {
        serverNetworkController.diceRollRequest(serverNetworkController.getPlayerModelId(this.currentThreadID));

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

        Integer player_id = robberMovementRequest.getPlayer_id();
        String location_id = robberMovementRequest.getLocation_id();
        int victim_id = robberMovementRequest.getVictim_id();
        // serverNetworkController.robberMovementRequest(player_id,location_id,victim_id);

    }

    protected void handle(ProtocolHarbourRequest harbourRequest) {

        ProtocolResource offer = harbourRequest.getOffer();
        ProtocolResource withdrawal = harbourRequest.getWithdrawal();
        // serverNetworkController.harbourRequest(offer,withdrawal);
    }

    protected void handle(ProtocolTradeAccept tradeAccept) {

        int trade_id = tradeAccept.getTrade_id();
        // serverNetworkController.tradeAccept(trade_id);
    }

    protected void handle(ProtocolTradeRequest tradeRequest) {

        ProtocolResource offer = tradeRequest.getOffer();
        ProtocolResource withdrawal = tradeRequest.getWithdrawal();
        // serverNetworkController.tradeRequest(offer,withdrawal);

    }

    protected void handle(ProtocolTradeCancel tradeCancel) {

        int trade_id = tradeCancel.getTrade_id();
        //    serverNetworkController.tradeCancel(trade_id);
    }

}
