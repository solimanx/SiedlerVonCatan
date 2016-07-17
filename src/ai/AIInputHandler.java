/*
 *
 */
package ai;

import java.util.ArrayList;

import enums.CardType;
import enums.PlayerState;
import model.board.Corner;
import model.board.Edge;
import model.board.Field;
import model.card.InventionCard;
import model.card.KnightCard;
import model.card.MonopolyCard;
import model.card.StreetBuildingCard;
import network.client.io.ClientInputHandler;
import parsing.Parser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ai.agents.OpponentAgent;
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
import service.HexService;
import service.ProtocolToModel;

// TODO: Auto-generated Javadoc
/**
 * Handling all input for the AI
 */
public class AIInputHandler extends ClientInputHandler {
	private AdvancedAI ai;
	private static Logger logger = LogManager.getLogger(AIInputHandler.class.getSimpleName());
	private Parser parser = new Parser();

	private ArrayList<Integer> opponentID = new ArrayList<Integer>();

	/**
	 * Instantiates a new AI input handler.
	 *
	 * @param advAI
	 *            the adv AI
	 */
	protected AIInputHandler(AdvancedAI advAI) {
		super(null);
		ai = advAI;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * network.client.client.ClientInputHandler#sendToParser(java.lang.String)
	 */
	public void sendToParser(String line) {
		Object object = parser.parseString(line);
		handle(object);

	}

	/**
	 * What to do after receiving hello from server.
	 *
	 * @param hello
	 *            the hello
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
	 *
	 * @param welcome
	 *            the welcome
	 */
	@Override
	protected void handle(ProtocolWelcome welcome) {
		ai.setID(welcome.getPlayerID());
		ai.getOutput().respondProfile(ai.getColorCounter());

	}

	/**
	 * Update board in the AI, after receiving the board json.
	 *
	 * @param gameStarted
	 *            the game started
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

		// ID MATCHING

		ai.getGl().getBoard().deletePlayers();
		ai.getGl().getBoard().insertPlayers(opponentID.size());
		for (int i = 0; i < opponentID.size(); i++) {
			ai.getGl().getBoard().getPlayer(i).setID(opponentID.get(i));
		}
		OpponentAgent oA = new OpponentAgent(ai.getGl().getBoard().getPlayerModels());
		ai.setOpponentAgent(oA);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see network.client.client.ClientInputHandler#handle(protocol.messaging.
	 * ProtocolChatReceiveMessage)
	 */
	@Override
	protected void handle(ProtocolChatReceiveMessage chatReceiveMessage) {
		if (ai.getMe() != null && ai.getMe().getPlayerState() == PlayerState.TRADING_OR_BUILDING && chatReceiveMessage.getSender()  == null) {
			ai.getOutput().respondEndTurn(); // if error then end turn
			ai.updateCards();
		}
		// Chatbot?

	}

	/**
	 * After receiving a {"Serverantwort":} JSON.
	 *
	 * @param serverResponse
	 *            the server response
	 */
	protected void handle(ProtocolServerResponse serverResponse) {
		// if game hasn't started, start it
		if (serverResponse.getServerResponse().equals("OK")) {
			if (!ai.isStarted()) {
				ai.getOutput().respondStartGame();
			}
		}
		// if color taken, try other colors
		else if (serverResponse.getServerResponse().equals("Farbe bereits vergeben")) {
			ai.setColorCounter(ai.getColorCounter() + 1);
			ai.getOutput().respondProfile(ai.getColorCounter());
		}
		// TODO: Fix OK Message
		else if (ai.getMe() != null && ai.getMe().getPlayerState() == PlayerState.TRADING_OR_BUILDING) {
			ai.getOutput().respondEndTurn(); // if error then end turn
			ai.updateCards();
		}

	}

	/**
	 * After receiving a {"Bauvorgang":} JSON.
	 *
	 * @param build
	 *            the build
	 */
	protected void handle(ProtocolBuild build) {
		ProtocolBuilding building = build.getBuilding();
		int playerID = building.getPlayerID();
		int[] coords;

		if (building.getType().equals("Dorf")) {
			coords = ProtocolToModel.getCornerCoordinates(building.getID());
			ai.updateVillage(coords[0], coords[1], coords[2], playerID);
			if (playerID == ai.getID()) {
				ai.getMe().decreaseAmountVillages();
				ai.getResourceAgent().add(ai.getGl().getBoard().getCornerAt(coords[0], coords[1], coords[2]));
				if (ai.getMe().getPlayerState() == PlayerState.TRADING_OR_BUILDING) {
					ai.actuate();
				}
			}

			// ai.getOpponentAgent().CostsEnemy(playerID,
			// DefaultSettings.VILLAGE_BUILD_COST);

		} else if (building.getType().equals("Straße")) {
			coords = ProtocolToModel.getEdgeCoordinates(building.getID());
			ai.updateRoad(coords[0], coords[1], coords[2], playerID);
			if (playerID == ai.getID()) {
				ai.getMe().decreaseAmountStreets();
				ai.getResourceAgent().add(ai.getGl().getBoard().getEdgeAt(coords[0], coords[1], coords[2]));
				ai.getResourceAgent()
						.addToOwnStreetSet(ai.getGl().getBoard().getEdgeAt(coords[0], coords[1], coords[2]));
				if (ai.devCardActionCounter == 2) {
					ai.devCardActionCounter--;
					// wait for second street
				} else if (ai.devCardActionCounter == 1) {
					ai.devCardActionCounter--;
					ai.currentDevCard = null;
					ai.actuate();
				} else if (ai.getMe().getPlayerState() == PlayerState.TRADING_OR_BUILDING) {
					ai.actuate();
				}
			}

			// ai.getOpponentAgent().CostsEnemy(playerID,
			// DefaultSettings.STREET_BUILD_COST);

		} else if (building.getType().equals("Stadt")) {
			coords = ProtocolToModel.getCornerCoordinates(building.getID());
			ai.updateCity(coords[0], coords[1], coords[2], playerID);
			if (playerID == ai.getID()) {
				ai.getMe().decreaseAmountCities();
				ai.getMe().increaseAmountVillages();
				if (ai.getMe().getPlayerState() == PlayerState.TRADING_OR_BUILDING) {
					ai.actuate();
				}
			}

			// ai.getOpponentAgent().CostsEnemy(playerID,
			// DefaultSettings.CITY_BUILD_COST);
		} else {
			logger.warn("Throws new IllegalArgumentException \"Building type not defined\"");
		}

	}

	/**
	 * After receiving a {"Würfelwurf":...} JSON
	 *
	 * @param diceRollResult
	 *            the dice roll result
	 */
	protected void handle(ProtocolDiceRollResult diceRollResult) {
		// Nothing useful can be done with this information.
	}

	/**
	 * After receiving a {"Ertrag": ...} JSON
	 *
	 * @param resourceObtain
	 *            the resource obtain
	 */
	protected void handle(ProtocolResourceObtain resourceObtain) {
		// Get ID and resources
		int ID = resourceObtain.getPlayerID();
		int[] gain;
		gain = ProtocolToModel.convertResources(resourceObtain.getResource());

		// if it's me
		if (ID == ai.getID()) {
			ai.getMe().incrementResources(gain);
			if (ai.tradeWaitForBuilding != null) {
				ai.checkIncomingTrade();
			}
			// after monopoly & invention
			else if (ai.currentDevCard != CardType.STREET && ai.devCardActionCounter > 0) {
				ai.devCardActionCounter--;
				ai.currentDevCard = null;
				ai.actuate();
			}
		}
		// if it isn't me

		else {
			ai.getOpponentAgent().ressourceObtainEnemy(ID, gain);

		}

	}

	/**
	 * After receiving a {"Statusupdate": ...} JSON
	 *
	 * @param statusUpdate
	 *            the status update
	 */
	@Override
	protected void handle(ProtocolStatusUpdate statusUpdate) {
		// THE ID
		int pID = statusUpdate.getPlayer().getPlayerID();
		// THE STATUS
		PlayerState ps = statusUpdate.getPlayer().getStatus();

		// if it's me
		if (pID == ai.getID()) {
			if (ai.getMe() != null) {
				logger.debug(
						"Wood: " + ai.getMe().getResourceAmountOf(0) + ", CLay: " + ai.getMe().getResourceAmountOf(1)
								+ ", ORE: " + ai.getMe().getResourceAmountOf(2) + ", SHEEP: "
								+ ai.getMe().getResourceAmountOf(3) + ", CORN: " + ai.getMe().getResourceAmountOf(4));
				ai.getMe().setPlayerState(ps);
			}
			switch (ps) {
			// and i'm waiting for game to start
			case WAITING_FOR_GAMESTART:
				ai.setStarted(true);
				break;
			// if it's me and i have to build initial villages
			case BUILDING_VILLAGE:
				ai.initialVillage();
				break;
			// if it's me and i have to build initial roads
			case BUILDING_STREET:
				ai.initialRoad();
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
				ai.looseToBandit();
				break;
			case TRADING_OR_BUILDING:
				ai.actuate();
				// ai.getOutput().respondEndTurn();
				break;
			default:// do nothing

			}

		} else {
			switch (ps) {
			case WAITING_FOR_GAMESTART:
				if (!opponentID.contains(pID)) {
					opponentID.add(pID);
				}
				break;
			default:
				break;
			}
		}

	}

	/**
	 * After receiving a {Räuber versetzt} JSON.
	 *
	 * @param robberMovement
	 *            the robber movement
	 */
	@Override
	protected void handle(ProtocolRobberMovement robberMovement) {
		ai.updateRobber(ProtocolToModel.getProtocolOneID(robberMovement.getLocationID()));

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * network.client.client.ClientInputHandler#handle(protocol.configuration.
	 * ProtocolVictory)
	 */
	@Override
	protected void handle(ProtocolVictory victory) {
		// Disconnect?

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see network.client.client.ClientInputHandler#handle(protocol.
	 * serverinstructions.ProtocolCosts)
	 */
	@Override
	protected void handle(ProtocolCosts costs) {
		// Get ID and resources
		int ID = costs.getPlayerID();
		int[] loss;
		loss = ProtocolToModel.convertResources(costs.getResource());
		// if it's me
		if (ID == ai.getID()) {
			ai.getMe().decrementResources(loss);
		}
		// if it isn't me
		else {
			ai.getOpponentAgent().costsEnemy(ID, loss);

		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see network.client.client.ClientInputHandler#handle(protocol.
	 * serverinstructions.trade.ProtocolTradePreview)
	 */
	@Override
	protected void handle(ProtocolTradePreview tradePreview) {
		// TODO redirect to ai -> trade agent

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see network.client.client.ClientInputHandler#handle(protocol.
	 * serverinstructions.trade.ProtocolTradeConfirmation)
	 */
	@Override
	protected void handle(ProtocolTradeConfirmation tradeConfirmation) {
		// TODO redirect to ai -> trade agent

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see network.client.client.ClientInputHandler#handle(protocol.
	 * serverinstructions.trade.ProtocolTradeCompletion)
	 */
	@Override
	protected void handle(ProtocolTradeCompletion tradeIsCompleted) {
		// TODO redirect to ai -> trade agent

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see network.client.client.ClientInputHandler#handle(protocol.
	 * serverinstructions.trade.ProtocolTradeCancellation)
	 */
	@Override
	protected void handle(ProtocolTradeCancellation tradeIsCanceled) {
		// TODO redirect to ai -> trade agent

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see network.client.client.ClientInputHandler#handle(protocol.
	 * serverinstructions.ProtocolLargestArmy)
	 */
	@Override
	protected void handle(ProtocolLargestArmy largestArmy) {
		// TODO if self, nothing else redirect to ai -> opponent agent
		if (largestArmy.getPlayerID() == ai.getID()) {
			ai.getMe().setHasLargestArmy(true);
		} else {
			ai.getMe().setHasLargestArmy(false);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see network.client.client.ClientInputHandler#handle(protocol.
	 * serverinstructions.ProtocolLongestRoad)
	 */
	@Override
	protected void handle(ProtocolLongestRoad longestRoad) {
		// TODO if self, nothing else redirect to ai -> opponent agent
		if (longestRoad.getPlayerID() == ai.getID()) {
			ai.getMe().setHasLongestRoad(true);
		} else {
			ai.getMe().setHasLongestRoad(false);
			// TODO Mark opponent
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * network.client.client.ClientInputHandler#handle(protocol.dualinstructions
	 * .ProtocolPlayInventionCard)
	 */
	@Override
	protected void handle(ProtocolPlayInventionCard inventionCardInfo) {
		// TODO

		// Get ID and resources
		int ID = inventionCardInfo.getPlayerID();
		// opponent agent
		// if it's me
		if (ID == ai.getID()) {
			ai.getMe().decrementPlayerDevCard(new InventionCard());
		}
		// if it isn't me
		else {
			ai.getOpponentAgent().devCardPlayed(CardType.INVENTION, ID);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * network.client.client.ClientInputHandler#handle(protocol.dualinstructions
	 * .ProtocolPlayMonopolyCard)
	 */
	@Override
	protected void handle(ProtocolPlayMonopolyCard monopolyCardInfo) {
		// TODO

		// Get ID and resources
		int ID = monopolyCardInfo.getPlayerID();
		if (ID == ai.getID()) {
			ai.getMe().decrementPlayerDevCard(new MonopolyCard());
		}
		// if it isn't me
		else {
			// OpponentAgent
			ai.getOpponentAgent().devCardPlayed(CardType.MONOPOLY, ID);
			// if it's me
			// massages as costs and obtain -> not necessary
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * network.client.client.ClientInputHandler#handle(protocol.dualinstructions
	 * .ProtocolPlayKnightCard)
	 */
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
			if (ai.getMe().getPlayerState() == PlayerState.TRADING_OR_BUILDING) {
				ai.actuate(); // actuate after played a card
			}

		}
		// if it isn't me
		else {
			// OpponentAgent
			ai.getOpponentAgent().devCardPlayed(CardType.KNIGHT, ID);

		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * network.client.client.ClientInputHandler#handle(protocol.dualinstructions
	 * .ProtocolPlayRoadCard)
	 */
	@Override
	protected void handle(ProtocolPlayRoadCard roadBuildingCardInfo) {
		// TODO

		// Get ID and resources
		int ID = roadBuildingCardInfo.getPlayerID();

		// if it's me
		if (ID == ai.getID()) {
			ai.getMe().decrementPlayerDevCard(new StreetBuildingCard());

			int[] coords = ProtocolToModel.getEdgeCoordinates(roadBuildingCardInfo.getRoadID1());
			ai.getResourceAgent().add(ai.getGl().getBoard().getEdgeAt(coords[0], coords[1], coords[2]));
			ai.getResourceAgent().addToOwnStreetSet(ai.getGl().getBoard().getEdgeAt(coords[0], coords[1], coords[2]));

			if (roadBuildingCardInfo.getRoadID2() != null) {
				int[] coords2 = ProtocolToModel.getEdgeCoordinates(roadBuildingCardInfo.getRoadID2());
				ai.getResourceAgent().add(ai.getGl().getBoard().getEdgeAt(coords2[0], coords2[1], coords2[2]));
				ai.getResourceAgent()
						.addToOwnStreetSet(ai.getGl().getBoard().getEdgeAt(coords2[0], coords2[1], coords2[2]));
				ai.getMe().decreaseAmountStreets();
			}
			ai.getMe().decreaseAmountStreets();

			if (ai.getMe().getPlayerState() == PlayerState.TRADING_OR_BUILDING) {
				ai.actuate(); // actuate after played a card
			}
		}
		// if it isn't me
		else {
			// OpponentAgent
			ai.getOpponentAgent().devCardPlayed(CardType.STREET, ID);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see network.client.client.ClientInputHandler#handle(protocol.
	 * serverinstructions.ProtocolBoughtDevelopmentCard)
	 */
	@Override
	protected void handle(ProtocolBoughtDevelopmentCard boughtDevelopmentCard) {
		// Get ID and resources
		int ID = boughtDevelopmentCard.getPlayerID();
		CardType ct = boughtDevelopmentCard.getDevelopmentCard();

		ai.getCardAgent().increaseAmountOfBoughtDevCards();
		// if it's me
		if (ID == ai.getID()) {
			// ai.getMe().incrementPlayerDevCard(ProtocolToModel.getDevCard(ct));
			// this will be done later (after end Turn)
			ai.boughtDevCard = ct;
			if (ai.getMe().getPlayerState() == PlayerState.TRADING_OR_BUILDING) {
				ai.actuate();
			}
		}
		// if it isn't me
		else {
			// OpponentAgent
			ai.getOpponentAgent().boughtDevCard(ID);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * network.client.client.ClientInputHandler#handle(protocol.configuration.
	 * ProtocolError)
	 */
	@Override
	protected void handle(ProtocolError error) {
		ai.setColorCounter(ai.getColorCounter() + 1);
		ai.getOutput().respondProfile(ai.getColorCounter());
	}

}
