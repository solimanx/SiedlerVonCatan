package network.server.server;

import enums.Color;
import network.InputHandler;
import network.ProtocolToModel;
import network.client.controller.ClientNetworkController;
import network.server.controller.ServerNetworkController;
import protocol.clientinstructions.ProtocolBuildRequest;
import protocol.clientinstructions.ProtocolDiceRollRequest;
import protocol.clientinstructions.ProtocolEndTurn;
import protocol.configuration.ProtocolClientReady;
import protocol.configuration.ProtocolError;
import protocol.configuration.ProtocolGameStarted;
import protocol.configuration.ProtocolPlayerProfile;
import protocol.connection.ProtocolHello;
import protocol.connection.ProtocolWelcome;
import protocol.messaging.ProtocolChatReceiveMessage;
import protocol.messaging.ProtocolChatSendMessage;
import protocol.messaging.ProtocolServerConfirmation;
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

    //unnecessary Method in ServerInputHandler
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
        // ClientNetworkController.sendPlayerProfile(name,color);
        //TODO

    }

    @Override
    protected void handle(ProtocolChatReceiveMessage chatReceiveMessage) {
        String s = chatReceiveMessage.getMessage();
        int playerId = chatReceiveMessage.getSender();
        serverNetworkController.chatReceiveMessage(playerId, s);
        /*ChatRecieveMessage, (Nachricht wird vom Server verteilt) needs to be handled only in ServerOutputHandler and
        in ClientInputHandler. unnecessary Method in ServerInputHandler
         */
    }

    protected void handle(ProtocolChatSendMessage chatSendMessage) {
        String s = chatSendMessage.getMessage();
        serverNetworkController.chatSendMessage(s, this.currentThreadID);
    }

    @Override
    protected void handle(ProtocolServerConfirmation serverConfirmation) {
        // TODO Auto-generated method stub

    }

    //
    @Override
    protected void handle(ProtocolBuild build) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void handle(ProtocolDiceRollResult diceRollResult) {
        // unnecessary Method in ServerInputHandler

    }

    @Override
    protected void handle(ProtocolResourceObtain resourceObtain) {
        //unnecessary Method in ServerInputHandler
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


}
