package network.client.client;

import org.apache.logging.log4j.core.net.Protocol;

import model.objects.Corner;
import model.objects.Edge;
import model.objects.Field;
import network.InputHandler;
import network.ProtocolToModel;
import network.client.controller.ClientNetworkController;
import protocol.clientinstructions.ProtocolBuildRequest;
import protocol.clientinstructions.ProtocolDiceRollRequest;
import protocol.clientinstructions.ProtocolEndTurn;
import protocol.clientinstructions.ProtocolRobberLoss;
import protocol.configuration.ProtocolClientReady;
import protocol.configuration.ProtocolError;
import protocol.configuration.ProtocolGameStarted;
import protocol.configuration.ProtocolPlayerProfile;
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
import protocol.serverinstructions.ProtocolDiceRollResult;
import protocol.serverinstructions.ProtocolResourceObtain;
import protocol.serverinstructions.ProtocolStatusUpdate;
import protocol.serverinstructions.trade.ProtocolTradeConfirmation;
import protocol.serverinstructions.trade.ProtocolTradeIsCanceled;
import protocol.serverinstructions.trade.ProtocolTradeIsCompleted;
import protocol.serverinstructions.trade.ProtocolTradeIsRequested;
import settings.DefaultSettings;

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
		networkController.serverHello(hello.getVersion(), hello.getProtocol());
	}

	@Override
	protected void handle(ProtocolWelcome welcome) {
		networkController.welcome(welcome.getPlayer_id());
	}

	@Override
	protected void handle(ProtocolClientReady clientReady) {
		// unnecessary Method in ClientInputHandler

	}

	@Override
	protected void handle(ProtocolGameStarted gameStarted) {
		ProtocolBoard pBoard = gameStarted.getBoard();
		Field[][] fields = new Field[DefaultSettings.BOARD_SIZE][DefaultSettings.BOARD_SIZE];
		for (int i = 0; i < pBoard.getAmountFields(); i++) {
			int[] axCoordinates = ProtocolToModel.getFieldCoordinates(pBoard.getProtocolField(i).getFieldID());
			fields[axCoordinates[0] + DefaultSettings.BOARD_RADIUS][axCoordinates[1]
					+ DefaultSettings.BOARD_RADIUS] = new Field(pBoard.getProtocolField(i).getFieldID(),
							ProtocolToModel.getResourceType(pBoard.getProtocolField(i).getFieldType()),
							pBoard.getProtocolField(i).getDiceIndex());
		}
		// TODO:
		// networkController.flowController.initBoard(int amountPlayers,
		// Field[][] serverFields, Edge[][][] edges, Corner[][][] corners,Field
		// bandit)

	}

	@Override
	protected void handle(ProtocolError error) {
		System.out.println("Meldung wird geschickt");
		networkController.error(error.getNotice());

	}

	@Override
	protected void handle(ProtocolPlayerProfile playerProfile) {
		// unnecessary Method in ClientInputHandler

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
		String server_response = serverConfirmation.getServer_response();
		networkController.serverConfirmation(server_response);
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
		networkController.diceRollResult(playerID, result);
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
		networkController.endTurn();
		// unnecessary Method in ClientInputHandler
	}

	@Override
	protected void handle(String string) {
		networkController.serverConfirmation(string);
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
}
