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
import protocol3.serverinstructions.*;

/**
 * Handling all input for the AI
 */
public class PrimitiveAIInputHandler extends InputHandler {
	PrimitiveAI ai;

	protected PrimitiveAIInputHandler(PrimitiveAI primitiveAI) {
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
		if (ai.getProtocol().equals(hello.getProtocol())) {
			ai.getOutput().respondHello(ai.getVersion());

		} else {
			throw new IllegalArgumentException("Protocol version mismatch");
		}

	}

	/**
	 * Initializing ID after receiving welcome from server. Attempt to create a
	 * profile.
	 */
	@Override
	protected void handle(ProtocolWelcome welcome) {
		ai.setID(welcome.getPlayerID());
		ai.getOutput().respondProfile(ai.getColorCounter());

	}

	/**
	 * Update board in the AI, after receiving the board json.
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
		ai.updateBoard(fields, corners, streets, harbourCorners, banditLocation);

	}

	@Override
	protected void handle(ProtocolChatReceiveMessage chatReceiveMessage) {
		// TODO Chatbot?

	}

	/**
	 * After receiving a {"Serverantwort":} JSON
	 */
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

	/**
	 * After receiving a {"Bauvorgang":} JSON
	 */
	@Override
	protected void handle(ProtocolBuild build) {
		ProtocolBuilding building = build.getBuilding();
		int playerID = building.getPlayerID();
		int[] coords;

		if (building.getType().equals("Dorf")) {
			coords = ProtocolToModel.getCornerCoordinates(building.getID());
			ai.updateVillage(coords[0], coords[1], coords[2], playerID);
		} else if (building.getType().equals("Straße")) {
			coords = ProtocolToModel.getEdgeCoordinates(building.getID());
			ai.updateRoad(coords[0], coords[1], coords[2], playerID);

		} else if (building.getType().equals("Stadt")) {
			coords = ProtocolToModel.getCornerCoordinates(building.getID());
		} else
			throw new IllegalArgumentException("Building type not defined");

	}

	/**
	 * After receiving a {"Würfelwurf":...} JSON
	 */
	@Override
	protected void handle(ProtocolDiceRollResult diceRollResult) {
		// TODO Store this.

	}

	/**
	 * After receiving a {"Ertrag": ...} JSON
	 */
	@Override
	protected void handle(ProtocolResourceObtain resourceObtain) {
		// Get ID and resources
		int ID = resourceObtain.getPlayerID();
		int[] gain;

		// if it's me
		if (ID == ai.getID()) {
			gain = ProtocolToModel.convertResources(resourceObtain.getResource());
			ai.getMe().incrementResources(gain);
		}
		// if it isn't me
		else {
			// TODO Store for strategy
		}

	}

	/**
	 * After receiving a {"Statusupdate": ...} JSON
	 */
	@Override
	protected void handle(ProtocolStatusUpdate statusUpdate) {
		// THE ID
		int pID = statusUpdate.getPlayer().getPlayerID();
		// THE STATUS
		PlayerState ps = statusUpdate.getPlayer().getStatus();

		// if it's me
		if (pID == ai.getID()) {

			switch (ps) {
			// and i'm waiting for game to start
			case WAITING_FOR_GAMESTART:
				ai.setStarted(true);
				break;
			// if it's me and i have to build initial villages
			case BUILDING_VILLAGE:
				if (ai.getSecondVillageLocation() == null) {
					ai.initialVillage();
				}
				break;
			// if it's me and i have to build initial roads
			case BUILDING_STREET:
				if (ai.getSecondRoadLocation() == null) {
					ai.initialRoad();
				}
				break;
			// if it's me and i have to roll dice
			case DICEROLLING:
				ai.getOutput().respondDiceRoll();
				break;
			// if it's me and I have to move robber
			case MOVE_ROBBER:
				ai.moveRobber();
				break;
			case DISPENSE_CARDS_ROBBER_LOSS:
				ai.loseToBandit();
				break;
			case TRADING_OR_BUILDING:
				ai.getOutput().respondEndTurn();
				break;
			default:// do nothing

			}

		}

	}

	/**
	 * After receiving a {Räuber versetzt} JSON
	 */
	@Override
	protected void handle(ProtocolRobberMovement robberMovement) {
		ai.updateRobber(robberMovement.getLocationID());

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
	protected void handle(ProtocolTradePreview tradePreview) {
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

	@Override
	protected void handle(ProtocolError error) {
		// Preferably the AI will try to avoid all errors, which makes this
		// method useless.
	}

	@Deprecated
	@Override
	protected void handle(ProtocolPlayerProfile playerProfile) {
		// Server doesn't output this JSON.

	}

	@Override
	@Deprecated
	protected void handle(ProtocolClientReady clientReady) {
		// Server doesn't output this JSON.
	}

	// ================================================================================
	// UNUSED/DEPRECATED
	// ================================================================================

	@Deprecated
	@Override
	protected void handle(ProtocolChatSendMessage chatSendMessage) {
		// Server doesn't output this JSON.

	}

	@Deprecated
	@Override
	protected void handle(String string) {
		// Was intended for server response
	}

	@Deprecated
	@Override
	protected void handle(ProtocolBuildRequest buildRequest) {
		// Server doesn't output this JSON.
	}

	@Deprecated
	@Override
	protected void handle(ProtocolDiceRollRequest diceRollRequest) {
		// Server doesn't output this JSON.
	}

	@Deprecated
	@Override
	protected void handle(ProtocolEndTurn endTurn) {
		// Server doesn't output this JSON.

	}

}
