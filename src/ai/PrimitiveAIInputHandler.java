package ai;

import java.util.ArrayList;

import enums.PlayerState;
import model.HexService;
import model.objects.Corner;
import model.objects.Edge;
import model.objects.Field;
import network.InputHandler;
import network.ProtocolToModel;
import protocol.clientinstructions.*;
import protocol.clientinstructions.trade.*;
import protocol.configuration.*;
import protocol.connection.*;
import protocol.messaging.*;
import protocol.object.ProtocolBoard;
import protocol.object.ProtocolBuilding;
import protocol.object.ProtocolField;
import protocol.object.ProtocolHarbour;
import protocol.serverinstructions.*;
import protocol.serverinstructions.trade.*;
import protocol3.clientinstructions.*;
import protocol3.object.*;
import protocol3.severinstructions.*;

public class PrimitiveAIInputHandler extends InputHandler {
	PrimitiveAI ai;

	public PrimitiveAIInputHandler(PrimitiveAI primitiveAI) {
		ai = primitiveAI;

	}

	public void sendToParser(String line) {
		Object object = parser.parseString(line);
		handle(object);

	}

	/**
	 * What to do after receiving hello from server
	 */
	@Override
	protected void handle(ProtocolHello hello) {
		if (ai.getPROTOCOL().equals(hello.getProtocol())) {
			ai.getOutput().respondHello(ai.getVERSION());

		} else {
			throw new IllegalArgumentException("Protocol version mismatch");
		}

	}

	@Override
	protected void handle(ProtocolWelcome welcome) {
		ai.setID(welcome.getPlayer_id());
		ai.getOutput().respondProfile(ai.getColorCounter());
		// ai.getOutput().respondStartGame();

	}

	@Override
	@Deprecated
	protected void handle(ProtocolClientReady clientReady) {
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

		// Times 2 for each harbour there are two corners.
		Corner[] harbourCorners = new Corner[pBoard.getAmountHarbours() * 2];
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
		ai.initBoard(fields, corners, streets, harbourCorners, banditLocation);

	}

	@Override
	protected void handle(ProtocolError error) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void handle(ProtocolPlayerProfile playerProfile) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolChatReceiveMessage chatReceiveMessage) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolChatSendMessage chatSendMessage) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolServerResponse serverResponse) {
		// if game hasn't started, start it
		if (serverResponse.getServerResponse().equals("OK") && !ai.isStarted()) {
			ai.getOutput().respondStartGame();
		}
		// if color taken, try other colors
		else if (serverResponse.getServerResponse().equals("Farbe bereits vergeben")) {
			ai.setColorCounter(ai.getColorCounter() + 1);
			ai.getOutput().respondProfile(ai.getColorCounter());
		}

	}

	@Override
	protected void handle(String string) {

	}

	@Override
	protected void handle(ProtocolBuild build) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolDiceRollResult diceRollResult) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolResourceObtain resourceObtain) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolStatusUpdate statusUpdate) {
		// if it's me and i'm waiting for game to start
		if (statusUpdate.getPlayer().getPlayerID() == ai.getID()) {
			if (statusUpdate.getPlayer().getStatus().equals(PlayerState.WAITING_FOR_GAMESTART)) {
				ai.setStarted(true);
			}
			// if it's me and i have to build a village
			else if (statusUpdate.getPlayer().getStatus().equals(PlayerState.BUILDING_VILLAGE)) {
				// TODO TO BE CONTINUED
			}

		}

	}

	@Override
	protected void handle(ProtocolBuildRequest buildRequest) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolDiceRollRequest diceRollRequest) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolEndTurn endTurn) {
		// TODO Auto-generated method stub

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
	protected void handle(ProtocolRobberLoss robberLoss) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolRobberMovementRequest robberMovementRequest) {
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
	protected void handle(ProtocolTradeIsRequested tradeIsRequested) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolTradeAccept tradeAccept) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolTradeConfirmation tradeConfirmation) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolTradeComplete tradeComplete) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolTradeIsCompleted tradeIsCompleted) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolTradeCancel tradeCancel) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolTradeIsCanceled tradeIsCanceled) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolBuyDevelopmentCards buyDevelopmentCards) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolDevelopmentCards developmentCards) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolInventionCard inventionCard) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolMonopolyCard monopolyCard) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolRoadBuildingCard roadBuildingCard) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolBiggestKnightProwess biggestKnightProwess) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolInventionCardInfo inventionCardInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolLongestRoad longestRoad) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolMonopolyCardInfo monopolyCardInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolPlayKnightCard playKnightCard) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolRoadBuildingCardInfo roadBuildingCardInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle(ProtocolBoughtDevelopmentCard boughtDevelopmentCard) {
		// TODO Auto-generated method stub

	}

}
