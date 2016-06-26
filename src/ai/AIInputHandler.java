package ai;

import java.util.ArrayList;

import enums.CardType;
import enums.PlayerState;
import model.HexService;
import model.objects.Corner;
import model.objects.Edge;
import model.objects.Field;
import model.objects.DevCards.InventionCard;
import model.objects.DevCards.KnightCard;
import model.objects.DevCards.MonopolyCard;
import model.objects.DevCards.StreetBuildingCard;
import network.ProtocolToModel;
import network.client.client.ClientInputHandler;
import parsing.Parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import protocol.configuration.*;
import protocol.connection.*;
import protocol.dualinstructions.ProtocolPlayInventionCard;
import protocol.dualinstructions.ProtocolPlayKnightCard;
import protocol.dualinstructions.ProtocolPlayMonopolyCard;
import protocol.dualinstructions.ProtocolPlayRoadCard;
import protocol.messaging.*;
import protocol.object.ProtocolBoard;
import protocol.object.ProtocolBuilding;
import protocol.object.ProtocolField;
import protocol.object.ProtocolHarbour;
import protocol.serverinstructions.*;
import protocol.serverinstructions.trade.*;

/**
 * Handling all input for the AI
 */
public class AIInputHandler extends ClientInputHandler {
	private PrimitiveAI ai;
	private static Logger logger = LogManager.getLogger(AIInputHandler.class.getSimpleName());
	private Parser parser = new Parser();

	protected AIInputHandler(PrimitiveAI primitiveAI) {
		super(null);
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
			logger.warn("Throws new IllegalArgumentException \"Protocol version mismatch\"");
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
			fields[i].setFieldID(ProtocolToModel.getProtocolOneID(pField.getFieldID()));
			fields[i].setDiceIndex(pField.getDiceIndex());
			fields[i].setResourceType(ProtocolToModel.getResourceType(pField.getFieldType()));
		}
		ArrayList<Edge> streets = new ArrayList<Edge>();
		Corner[] corners = new Corner[pBoard.getAmountBuildings()];
		for (int i = 0; i < corners.length; i++) {
			ProtocolBuilding pBuild = pBoard.getProtocolBuilding(i);
			if (!pBuild.getType().equals("Straße")) {
				corners[i] = new Corner();
				corners[i].setCornerID(ProtocolToModel.getCornerIDIndex(pBuild.getID()));
				corners[i].setOwnerID(pBuild.getPlayerID());
				corners[i].setStatus(ProtocolToModel.getCornerType(pBuild.getType()));
			} else {
				Edge e = new Edge();
				streets.add(e);
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
		String banditLocation = ProtocolToModel.getProtocolOneID(pBoard.getRobber_location());
		ai.updateBoard(fields, corners, streets, harbourCorners, banditLocation);

	}

	@Override
	protected void handle(ProtocolChatReceiveMessage chatReceiveMessage) {
		// Chatbot?

	}

	/**
	 * After receiving a {"Serverantwort":} JSON
	 */
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
	protected void handle(ProtocolBuild build) {
		ProtocolBuilding building = build.getBuilding();
		int playerID = building.getPlayerID();
		int[] coords;

		if (building.getType().equals("Dorf")) {
			coords = ProtocolToModel.getCornerCoordinates(building.getID());
			if (playerID == ai.getID()) {
				ai.getMe().decreaseAmountVillages();
				ai.getResourceAgent().add(ai.getGl().getBoard().getCornerAt(coords[0], coords[1], coords[2]));
			}
			ai.updateVillage(coords[0], coords[1], coords[2], playerID);

		} else if (building.getType().equals("Straße")) {
			coords = ProtocolToModel.getEdgeCoordinates(building.getID());
			if (playerID == ai.getID()) {
				ai.getMe().decreaseAmountStreets();
				ai.getResourceAgent().add(ai.getGl().getBoard().getEdgeAt(coords[0], coords[1], coords[2]));
			}

			ai.updateRoad(coords[0], coords[1], coords[2], playerID);

		} else if (building.getType().equals("Stadt")) {
			if (playerID == ai.getID()) {
				ai.getMe().decreaseAmountCities();
				ai.getMe().increaseAmountVillages();
			}
			coords = ProtocolToModel.getCornerCoordinates(building.getID());
		} else {
			logger.warn("Throws new IllegalArgumentException \"Building type not defined\"");
		}

	}

	/**
	 * After receiving a {"Würfelwurf":...} JSON
	 */
	protected void handle(ProtocolDiceRollResult diceRollResult) {
		// Nothing useful can be done with this information.
	}

	/**
	 * After receiving a {"Ertrag": ...} JSON
	 */
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
				// TODO change
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
		ai.updateRobber(ProtocolToModel.getProtocolOneID(robberMovement.getLocationID()));

	}

	@Override
	protected void handle(ProtocolVictory victory) {
		// Disconnect?

	}

	@Override
	protected void handle(ProtocolCosts costs) {
		// TODO redirect to ai - > resource agent / opponent agent

	}

	@Override
	protected void handle(ProtocolTradePreview tradePreview) {
		// TODO redirect to ai -> trade agent

	}

	@Override
	protected void handle(ProtocolTradeConfirmation tradeConfirmation) {
		// TODO redirect to ai -> trade agent

	}

	@Override
	protected void handle(ProtocolTradeCompletion tradeIsCompleted) {
		// TODO redirect to ai -> trade agent

	}

	@Override
	protected void handle(ProtocolTradeCancellation tradeIsCanceled) {
		// TODO redirect to ai -> trade agent

	}

	@Override
	protected void handle(ProtocolLargestArmy biggestKnightProwess) {
		// TODO if self, nothing else redirect to ai -> opponent agent

	}

	@Override
	protected void handle(ProtocolLongestRoad longestRoad) {
		// TODO if self, nothing else redirect to ai -> opponent agent

	}

	@Override
	protected void handle(ProtocolPlayInventionCard inventionCardInfo) {
		// TODO

		// Get ID and resources
		int ID = inventionCardInfo.getPlayerID();
		// if it's me
		if (ID == ai.getID()) {
			ai.getMe().decrementPlayerDevCard(new InventionCard());
		}
		// if it isn't me
		else {
			// TODO Store for strategy
		}

	}

	@Override
	protected void handle(ProtocolPlayMonopolyCard monopolyCardInfo) {
		// TODO

		// Get ID and resources
		int ID = monopolyCardInfo.getPlayerID();
		// if it's me
		if (ID == ai.getID()) {
			ai.getMe().decrementPlayerDevCard(new MonopolyCard());
		}
		// if it isn't me
		else {
			// TODO Store for strategy
		}

	}

	@Override
	protected void handle(ProtocolPlayKnightCard playKnightCard) {
		// TODO

		// Get ID and resources
		int ID = playKnightCard.getPlayerID();
		ai.updateRobber(ProtocolToModel.getProtocolOneID(playKnightCard.getLocationID()));

		// if it's me
		if (ID == ai.getID()) {
			ai.getMe().decrementPlayerDevCard(new KnightCard());
			ai.getMe().incrementPlayedKnightCards();
		}
		// if it isn't me
		else {
			// TODO Store for strategy
		}

	}

	@Override
	protected void handle(ProtocolPlayRoadCard roadBuildingCardInfo) {
		// TODO

		// Get ID and resources
		int ID = roadBuildingCardInfo.getPlayerID();
		// if it's me
		if (ID == ai.getID()) {
			ai.getMe().decrementPlayerDevCard(new StreetBuildingCard());
		}
		// if it isn't me
		else {
			// TODO Store for strategy
		}

	}

	@Override
	protected void handle(ProtocolBoughtDevelopmentCard boughtDevelopmentCard) {
		// Get ID and resources
		int ID = boughtDevelopmentCard.getPlayerID();
		CardType ct = boughtDevelopmentCard.getDevelopmentCard();

		// if it's me
		if (ID == ai.getID()) {
			ai.getMe().incrementPlayerDevCard(ProtocolToModel.getDevCard(ct));
			;
		}
		// if it isn't me
		else {
			// TODO Store for strategy
		}

	}

	@Override
	protected void handle(ProtocolError error) {
			ai.setColorCounter(ai.getColorCounter() + 1);
			ai.getOutput().respondProfile(ai.getColorCounter());
	}

}
