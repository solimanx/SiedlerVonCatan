package network.client.client;

import model.objects.Field;
import network.InputHandler;
import network.ProtocolToModel;
import network.client.controller.ClientController;
import protocol.clientinstructions.ProtocolBuildRequest;
import protocol.clientinstructions.ProtocolDiceRollRequest;
import protocol.clientinstructions.ProtocolEndTurn;
import protocol.clientinstructions.ProtocolHarbourRequest;
import protocol.clientinstructions.ProtocolRobberLoss;
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
import protocol3.object.ProtocolInventionCard;
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
		int size = DefaultSettings.BOARD_SIZE;
		int radius = DefaultSettings.BOARD_RADIUS;
		ProtocolBoard pBoard = gameStarted.getBoard();
		Field[][] fields = new Field[size][size];
		for (int i = 0; i < pBoard.getAmountFields(); i++) {
			ProtocolField pField = pBoard.getProtocolField(i);
			int[] axCoord = ProtocolToModel.getFieldCoordinates(pField.getFieldID());
			fields[axCoord[0] + radius][axCoord[1] + radius] = new Field(pField.getFieldID(),
					ProtocolToModel.getResourceType(pField.getFieldType()), pField.getDiceIndex());
		}
		// TODO:
		// networkController.initBoard(int amountPlayers,
		// Field[][] serverFields, Edge[][][] edges, Corner[][][] corners,Field
		// bandit)

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
		clientController.serverConfirmation(server_response);
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
		int result = diceRollResult.getRoll();
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
		clientController.endTurn();
		// unnecessary Method in ClientInputHandler
	}

	@Override
	protected void handle(String string) {
		clientController.serverConfirmation(string);
	}

	protected void handle(ProtocolRobberLoss losses) {
		// TODO

	}

	protected void handle(ProtocolTradeIsRequested tradeIsRequested) {

		int player_id = tradeIsRequested.getPlayer_id();
		int trade_id = tradeIsRequested.getTrade_id();
		ProtocolResource offer = tradeIsRequested.getOffer();
		ProtocolResource withdrawal = tradeIsRequested.getWithdrawal();
		// networkController.tradeIsRequested(player_id,trade_id,offer,withdrawal);
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

	protected void handle(ProtocolTradeIsCompleted tradeIsCompleted) {

		int player_id = tradeIsCompleted.getPlayer_id();
		int tradePartner_id = tradeIsCompleted.getTradePartner_id();
		// networkController.tradeIsCompleted(player_id,tradePartner_id);
	}

	@Override
	protected void handle(ProtocolVictory victory) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolCosts costs) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolRobberMovement robberMovement) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolHarbourRequest harbourRequest) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolTradeRequest tradeRequest) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolTradeAccept tradeAccept) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolTradeComplete tradeComplete) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolTradeCancel tradeCancel) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolBiggestKnightProwess biggestKnightProwess) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolInventionCardInfo inventionCard) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolLongestRoad longestRoad) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolMonopolyCardInfo protocolMonopolyCardInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolPlayKnightCard protocolPlayKnightCard) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolRoadBuildingCardInfo protocolRoadBuildingCardInfo) {
		// TODO Auto-generated method stub

	}
}
