package network.client.client;

import enums.Color;
import network.InputHandler;
import network.ProtocolToModel;
import network.client.controller.ClientNetworkController;
import network.client.controller.FlowController;
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
import protocol.object.ProtocolBuilding;
import protocol.serverinstructions.ProtocolBuild;
import protocol.serverinstructions.ProtocolDiceRollResult;
import protocol.serverinstructions.ProtocolResourceObtain;
import protocol.serverinstructions.ProtocolStatusUpdate;

public class ClientInputHandler extends InputHandler {
    private ClientNetworkController networkController;

    public ClientInputHandler(ClientNetworkController nc) {
        super();
        this.networkController = nc;
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
        System.out.println("Hello gelesen!");
        networkController.serverHello(hello.getVersion(), hello.getProtocol());
    }

    @Override
    protected void handle(ProtocolWelcome welcome) {
        System.out.println("Willkommen gelesen!");
        networkController.welcome(welcome.getPlayer_id());
    }

    @Override
    protected void handle(ProtocolClientReady clientReady) {
        // unnecessary Method in ClientInputHandler

    }

    @Override
    protected void handle(ProtocolGameStarted gameStarted) {

      //  networkController.gameStarted(fields, edges, corners, bandit);

    }

    @Override
    protected void handle(ProtocolError error) {
        System.out.println("Meldung wird geschickt");
        networkController.error(error.getNotice());

    }

    @Override
    protected void handle(ProtocolPlayerProfile playerProfile) {
    //  unnecessary Method in ClientInputHandler

    }


    @Override
    protected void handle(ProtocolChatReceiveMessage chatReceiveMessage) {
        String s = chatReceiveMessage.getMessage();
        int playerId = chatReceiveMessage.getSender();
        networkController.chatReceiveMessage(playerId, s);
    }

    @Override
    protected void handle(ProtocolChatSendMessage chatSendMessage) {
        // unnecessary Method in ClientInputHandler

    }

    @Override
    protected void handle(ProtocolServerConfirmation serverConfirmation) {
        String server_response=serverConfirmation.getServer_response();
        networkController.serverConfirmation(server_response);
    }

    //
    @Override
    protected void handle(ProtocolBuild build) {
        ProtocolBuilding building=build.getBuilding();
        //networkController.build(building);

    }

    @Override
    protected void handle(ProtocolDiceRollResult diceRollResult) {
        int playerID = diceRollResult.getPlayer();
        int result = diceRollResult.getRoll();
        networkController.diceRollResult(playerID, result);
    }

    @Override
    protected void handle(ProtocolResourceObtain resourceObtain) {
//        TODO

    }

    @Override
    protected void handle(ProtocolStatusUpdate statusUpdate) {
        // TODO Auto-generated method stub
        // int playerId =
        // ProtocolToModel.getPlayerId(statusUpdate.getPlayer().getPlayer_id());
        enums.Color color = statusUpdate.getPlayer().getColor();
        String name = statusUpdate.getPlayer().getName();
        enums.PlayerState status = ProtocolToModel.getPlayerState(statusUpdate.getPlayer().getStatus());
        int victoryPoints = statusUpdate.getPlayer().getVictory_points();
        // int[] resources =
        // ProtocolToModel.getResources(statusUpdate.getPlayer().getResources());
        // networkController.statusUpdate(playerId, color, name, status,
        // victoryPoints, resources);

    }

    //
    @Override
    protected void handle(ProtocolBuildRequest buildRequest) {
        // unnecessary Method in CLientInputHaendler

    }

    @Override
    protected void handle(ProtocolDiceRollRequest diceRollRequest) {
        // unnecessary Method in ClientInputHandler

    }

    @Override
    protected void handle(ProtocolEndTurn endTurn) {
        System.out.println("Der Zug wurde beendet");
        networkController.endTurn();
        //unnecessary Method in ClientInputHandler
    }

}
