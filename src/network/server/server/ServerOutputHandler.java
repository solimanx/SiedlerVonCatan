package network.server.server;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import enums.CardType;
import enums.Color;
import enums.PlayerState;
import enums.ResourceType;
import model.Board;
import model.Index;
import model.objects.Corner;
import model.objects.Edge;
import model.objects.Field;
import model.objects.DevCards.DevelopmentCard;
import network.ModelToProtocol;
import network.ProtocolToModel;
import parsing.Parser;
import parsing.Response;
import protocol.configuration.ProtocolError;
import protocol.configuration.ProtocolGameStarted;
import protocol.configuration.ProtocolVictory;
import protocol.connection.ProtocolHello;
import protocol.connection.ProtocolWelcome;
import protocol.dualinstructions.ProtocolPlayInventionCard;
import protocol.dualinstructions.ProtocolPlayKnightCard;
import protocol.dualinstructions.ProtocolPlayMonopolyCard;
import protocol.dualinstructions.ProtocolPlayRoadCard;
import protocol.messaging.ProtocolChatReceiveMessage;
import protocol.messaging.ProtocolServerResponse;
import protocol.object.ProtocolBoard;
import protocol.object.ProtocolBuilding;
import protocol.object.ProtocolDevCard;
import protocol.object.ProtocolField;
import protocol.object.ProtocolHarbour;
import protocol.object.ProtocolPlayer;
import protocol.object.ProtocolResource;
import protocol.serverinstructions.ProtocolBoughtDevelopmentCard;
import protocol.serverinstructions.ProtocolBuild;
import protocol.serverinstructions.ProtocolCosts;
import protocol.serverinstructions.ProtocolDiceRollResult;
import protocol.serverinstructions.ProtocolLargestArmy;
import protocol.serverinstructions.ProtocolLongestRoad;
import protocol.serverinstructions.ProtocolResourceObtain;
import protocol.serverinstructions.ProtocolRobberMovement;
import protocol.serverinstructions.ProtocolStatusUpdate;
import protocol.serverinstructions.trade.ProtocolTradeCancellation;
import protocol.serverinstructions.trade.ProtocolTradeCompletion;
import protocol.serverinstructions.trade.ProtocolTradeConfirmation;
import protocol.serverinstructions.trade.ProtocolTradePreview;

// TODO: Auto-generated Javadoc
//import static org.apache.logging.log4j.FormatterLoggerManualExample.logger;

public class ServerOutputHandler {
	protected Server server;
	protected Parser parser;
	protected static Logger logger = LogManager.getLogger(ServerOutputHandler.class.getSimpleName());

	/**
	 * Instantiates a new server output handler.
	 *
	 * @param server the server
	 */
	public ServerOutputHandler(Server server) {
		this.server = server;
		this.parser = new Parser();
	}


	/**
	 * Builds the building.
	 *
	 * @param building the building
	 */
	public void buildBuilding(ProtocolBuilding building) {
		ProtocolBuild pb = new ProtocolBuild(building);
		Response r = new Response();
		r.pBuild = pb;
		try {
			server.broadcast((parser.createString(r)));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	/**
	 * Create ProtocolHello JSON and broadcast it as a String through server.
	 *
	 * @param serverVersion the server version
	 * @param protocolVersion the protocol version
	 * @param thread_id the thread id
	 */
	public void hello(String serverVersion, String protocolVersion, int thread_id) {
		ProtocolHello ph = new ProtocolHello(serverVersion, protocolVersion);
		Response r = new Response();
		r.pHello = ph;
		try {
			server.sendToClient(parser.createString(r), thread_id);
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	/**
	 * Chat receive message.
	 *
	 * @param threadID the thread ID
	 * @param message the message
	 */
	public void chatReceiveMessage(int threadID, String message) {
		ProtocolChatReceiveMessage pcrm = new ProtocolChatReceiveMessage(threadID, message);
		Response r = new Response();
		r.pChatReceive = pcrm;
		try {
			server.broadcast((parser.createString(r)));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	/**
	 * Inits the board.
	 *
	 * @param amountPlayers the amount players
	 * @param board the board
	 */
	public void initBoard(int amountPlayers, Board board) {

		ProtocolField[] pfArray = new ProtocolField[board.getStringToCoordMap().size()];
		int counter = 0;

		for (String key : board.getStringToCoordMap().keySet()) {
			int coords[] = board.getStringToCoordMap().get(key);
			Field f = board.getFieldAt(coords[0], coords[1]);
			pfArray[counter] = new ProtocolField(ProtocolToModel.getProtocolOneIndex(f.getFieldID()),
					ModelToProtocol.resourceToString.get(f.getResourceType()), f.getDiceIndex());
			counter++;

		}

		ProtocolBuilding[] pBuildingsArray = {};
		ProtocolHarbour[] pHarbourArray = new ProtocolHarbour[board.getHarbourCorners().length/2];
		Corner[] hC = board.getHarbourCorners();
		int[] coords;
		Edge[] c1Edges;
		Edge[] c2Edges;
		Edge schnitt = null;
		for (int i = 0;i < hC.length ;i+=2){
			coords = ProtocolToModel.getCornerCoordinates(hC[i].getCornerID());
			c1Edges = board.getProjectingEdges(coords[0],coords[1],coords[2]);
			
			coords = ProtocolToModel.getCornerCoordinates(hC[i+1].getCornerID());
			c2Edges = board.getProjectingEdges(coords[0],coords[1],coords[2]);
			for (int j = 0;j <c1Edges.length;j++){
				for (int k = 0;k <c2Edges.length;k++){
					if (c1Edges[j] != null && c2Edges[k] != null){
					if (c1Edges[j].equals(c2Edges[k])){
						schnitt = c1Edges[j];
					}
					}
				}
			}
			Index[] location = ModelToProtocol.convertToEdgeIndex(schnitt.getEdgeID());
			pHarbourArray[i/2] = new ProtocolHarbour(location,hC[i].getHarbourStatus());
		}
		ProtocolBoard pb = new ProtocolBoard(pfArray, pBuildingsArray, pHarbourArray,
				ProtocolToModel.getProtocolOneIndex(board.getBandit()));
		ProtocolGameStarted pgs = new ProtocolGameStarted(pb);
		Response r = new Response();
		r.pGameStarted = pgs;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Error.
	 *
	 * @param s the s
	 * @param threadPlayerID the thread player ID
	 */
	public void error(String s, int threadPlayerID) {
		ProtocolError pe = new ProtocolError(s);
		Response r = new Response();
		r.pError = pe;

		try {
			server.sendToClient(parser.createString(r), threadPlayerID);
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}
	}

	/**
	 * Dice roll result.
	 *
	 * @param playerID the player ID
	 * @param result the result
	 */
	public void diceRollResult(int playerID, int[] result) {
		ProtocolDiceRollResult dr = new ProtocolDiceRollResult(playerID, result);
		Response r = new Response();
		r.pDRResult = dr;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}
	}

	/**
	 * Resource obtain.
	 *
	 * @param playerID the player ID
	 * @param resources the resources
	 * @param sendToClient the send to client
	 */
	public void resourceObtain(int playerID, int[] resources,int sendToClient) {
		ProtocolResource pResource;
		if (resources.length != 5) {
			pResource = new ProtocolResource(null, null, null, null, null, resources[0]);
		} else {
			pResource = ModelToProtocol.convertToProtocolResource(resources);
		}
		ProtocolResourceObtain po = new ProtocolResourceObtain(playerID, pResource);
		Response r = new Response();
		r.pRObtain = po;
		try {
			server.sendToClient(parser.createString(r),sendToClient);
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}
	}

	/**
	 * Welcome.
	 *
	 * @param player_id the player id
	 */
	public void welcome(int player_id) {
		ProtocolWelcome pw = new ProtocolWelcome(player_id);

		Response r = new Response();
		r.pWelcome = pw;
		try {
			server.sendToClient(parser.createString(r), player_id);
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	/**
	 * Status update.
	 *
	 * @param playerID the player ID
	 * @param color the color
	 * @param name the name
	 * @param status the status
	 * @param victoryPoints the victory points
	 * @param resources the resources
	 * @param knightAmount the knight amount
	 * @param devCards the dev cards
	 * @param longestTR the longest TR
	 * @param biggestKP the biggest KP
	 * @param sendToPlayerID the send to player ID
	 */
	public void statusUpdate(int playerID, Color color, String name, PlayerState status, int victoryPoints,
			int[] resources,int knightAmount, int[] devCards,boolean longestTR, boolean biggestKP, Integer sendToPlayerID) {
		ProtocolResource pResource;
		if (resources.length != 5) {
			pResource = new ProtocolResource(null, null, null, null, null, resources[0]);
		} else {
			pResource = ModelToProtocol.convertToProtocolResource(resources);
		}
		ProtocolDevCard pDevCard;
		if (devCards.length != 5){
			pDevCard = new ProtocolDevCard(null,null,null,null,null,devCards[0]);
		} else {
			pDevCard = new ProtocolDevCard(devCards[0],devCards[1],devCards[2],devCards[3],devCards[4],null);
		}
		ProtocolPlayer pPlayer = new ProtocolPlayer(playerID, color, name, status, victoryPoints, pResource, knightAmount, pDevCard, biggestKP,longestTR ); //TODO die 1.0 neue attribute
		ProtocolStatusUpdate ps = new ProtocolStatusUpdate(pPlayer);
		Response r = new Response();
		r.pSUpdate = ps;
		try {
			if (sendToPlayerID != null) {
				logger.info("Send " + status + " to " + sendToPlayerID);
				server.sendToClient(parser.createString(r), sendToPlayerID);
			} else {
				server.broadcast((parser.createString(r)));
			}
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	/**
	 * Builds the.
	 *
	 * @param building the building
	 */
	public void build(ProtocolBuilding building) {
		ProtocolBuild pb = new ProtocolBuild(building);
		Response r = new Response();
		r.pBuild = pb;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}
	}

	/**
	 * Costs.
	 *
	 * @param playerID the player ID
	 * @param costs the costs
	 * @param sendToPlayer the send to player
	 */
	public void costs(int playerID, int[] costs,int sendToPlayer) {
		ProtocolResource pResource;
		if (costs.length != 5) {
			pResource = new ProtocolResource(null, null, null, null, null, costs[0]);
		} else {
			pResource = ModelToProtocol.convertToProtocolResource(costs);
		}
		ProtocolCosts pc = new ProtocolCosts(playerID, pResource);
		Response r = new Response();
		r.pCosts = pc;
		try {
			server.sendToClient(parser.createString(r), sendToPlayer);			
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}
	}

	/**
	 * Victory.
	 *
	 * @param message the message
	 * @param winner_id the winner id
	 */
	public void victory(String message, int winner_id) {
		ProtocolVictory pv = new ProtocolVictory(message, winner_id);
		Response r = new Response();
		r.pVictory = pv;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	/**
	 * Robber movement.
	 *
	 * @param player_id the player id
	 * @param location_id the location id
	 * @param victim_id the victim id
	 */
	public void robberMovement(int player_id, String location_id, Integer victim_id) {
		Index location = ProtocolToModel.getProtocolOneIndex(location_id);
		ProtocolRobberMovement pm = new ProtocolRobberMovement(player_id, location, victim_id);

		Response r = new Response();
		r.pRobberMovement = pm;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	/**
	 * Trade preview.
	 *
	 * @param playerID the player ID
	 * @param tradeID the trade ID
	 * @param offer the offer
	 * @param demand the demand
	 */
	public void tradePreview(int playerID, int tradeID, int[] offer, int[] demand) {
		ProtocolResource pOff = ModelToProtocol.convertToProtocolResource(offer);
		ProtocolResource pDem = ModelToProtocol.convertToProtocolResource(demand);
		ProtocolTradePreview ptp = new ProtocolTradePreview(playerID, tradeID, pOff, pDem);
		Response r = new Response();
		r.pTradePreview = ptp;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	/**
	 * Trade confirmation.
	 *
	 * @param player_id the player id
	 * @param trade_id the trade id
	 * @param accepted the accepted
	 */
	public void tradeConfirmation(int player_id, int trade_id, boolean accepted) {
		ProtocolTradeConfirmation pc = new ProtocolTradeConfirmation(player_id, trade_id, accepted);
		Response r = new Response();
		r.pTradeConfirm = pc;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}
	}

	/**
	 * Trade is canceled.
	 *
	 * @param player_id the player id
	 * @param trade_id the trade id
	 */
	public void tradeIsCanceled(int player_id, int trade_id) {
		ProtocolTradeCancellation ptic = new ProtocolTradeCancellation(player_id, trade_id);
		Response r = new Response();
		r.pTradeIsCanceled = ptic;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}
	}

	/**
	 * Trade is completed.
	 *
	 * @param player_id the player id
	 * @param tradePartner_id the trade partner id
	 */
	public void tradeIsCompleted(int player_id, int tradePartner_id) {
		ProtocolTradeCompletion ptico = new ProtocolTradeCompletion(player_id, tradePartner_id);
		Response r = new Response();
		r.pTradeIsCompleted = ptico;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	/**
	 * Builds the village.
	 *
	 * @param x the x
	 * @param y the y
	 * @param dir the dir
	 * @param playerID the player ID
	 */
	public void buildVillage(int x, int y, int dir, int playerID) {
		String location = ModelToProtocol.getCornerID(x, y, dir);
		Index[] locationIndex = ModelToProtocol.convertCornerIndex(location);
		ProtocolBuilding pb = new ProtocolBuilding(playerID, "Dorf", locationIndex);
		ProtocolBuild pbu = new ProtocolBuild(pb);
		Response r = new Response();
		r.pBuild = pbu;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	/**
	 * Builds the street.
	 *
	 * @param x the x
	 * @param y the y
	 * @param dir the dir
	 * @param playerID the player ID
	 */
	public void buildStreet(int x, int y, int dir, int playerID) {
		String location = ModelToProtocol.getEdgeID(x, y, dir);
		Index[] locationIndex = ModelToProtocol.convertToEdgeIndex(location);
		ProtocolBuilding pb = new ProtocolBuilding(playerID, "Straße", locationIndex);
		ProtocolBuild pbu = new ProtocolBuild(pb);
		Response r = new Response();
		r.pBuild = pbu;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	/**
	 * Builds the city.
	 *
	 * @param x the x
	 * @param y the y
	 * @param dir the dir
	 * @param playerID the player ID
	 */
	public void buildCity(int x, int y, int dir, int playerID) {
		String location = ModelToProtocol.getCornerID(x, y, dir);
		Index[] locationIndex = ModelToProtocol.convertCornerIndex(location);
		ProtocolBuilding pb = new ProtocolBuilding(playerID, "Stadt", locationIndex);
		ProtocolBuild pbu = new ProtocolBuild(pb);
		Response r = new Response();
		r.pBuild = pbu;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	/**
	 * Biggest knight prowess.
	 *
	 * @param player_id the player id
	 */
	public void biggestKnightProwess(int player_id) {
		ProtocolLargestArmy pbkp = new ProtocolLargestArmy(player_id);
		Response r = new Response();
		r.pLargestArmy = pbkp;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	/**
	 * Longest road.
	 *
	 * @param player_id the player id
	 */
	public void longestRoad(Integer player_id) {
		ProtocolLongestRoad plr = new ProtocolLongestRoad(player_id);
		Response r = new Response();
		r.pLongestRoad = plr;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	/**
	 * Invention card played.
	 *
	 * @param resource the resource
	 * @param player_id the player id
	 */
	public void inventionCardPlayed(int[] resource, Integer player_id) {
		ProtocolResource pr = ModelToProtocol.getResources(resource);
		ProtocolPlayInventionCard pici = new ProtocolPlayInventionCard(player_id, pr);
		Response r = new Response();
		r.pPlayInventionCard = pici;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}
	}

	/**
	 * Monopoly card played.
	 *
	 * @param resourceType the resource type
	 * @param threadID the thread ID
	 */
	public void monopolyCardPlayed(ResourceType resourceType, int threadID) {

		ProtocolPlayMonopolyCard pmci = new ProtocolPlayMonopolyCard(threadID, resourceType);
		Response r = new Response();
		r.pPlayMonopolyCard = pmci;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	/**
	 * Knight card played.
	 *
	 * @param threadID the thread ID
	 * @param location the location
	 * @param victimID the victim ID
	 */
	public void knightCardPlayed(int threadID, String location, Integer victimID) {
		Index locationIndex = ProtocolToModel.getProtocolOneIndex(location);
		ProtocolPlayKnightCard ppkc = new ProtocolPlayKnightCard(threadID, locationIndex, victimID);
		Response r = new Response();
		r.pPlayKnightCard = ppkc;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}
	}

	/**
	 * Road building card played.
	 *
	 * @param threadID the thread ID
	 * @param x1 the x 1
	 * @param y1 the y 1
	 * @param dir1 the dir 1
	 * @param x2 the x 2
	 * @param y2 the y 2
	 * @param dir2 the dir 2
	 */
	public void roadBuildingCardPlayed(int threadID, int x1, int y1, int dir1, int x2, int y2, int dir2) {
		String location1 = ModelToProtocol.getEdgeID(x1, y1, dir1);
		String location2 = ModelToProtocol.getEdgeID(x2, y2, dir2);
		Index[] location1Index = ModelToProtocol.convertToEdgeIndex(location1);
		Index[] location2Index = ModelToProtocol.convertToEdgeIndex(location2);
		ProtocolPlayRoadCard prbci = new ProtocolPlayRoadCard(threadID, location1Index, location2Index);
		Response r = new Response();
		r.pPlayRoadCard = prbci;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}

	}

	/**
	 * Server confirm.
	 *
	 * @param server_response the server response
	 * @param threadPlayerID the thread player ID
	 */
	public void serverConfirm(String server_response, int threadPlayerID) {
		ProtocolServerResponse psc = new ProtocolServerResponse(server_response);
		try {
			server.sendToClient(parser.createString(psc), threadPlayerID);
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}
	}

	/**
	 * Bought development card.
	 *
	 * @param player_id the player id
	 * @param devCard the dev card
	 * @param sendToID the send to ID
	 */
	public void boughtDevelopmentCard(int player_id, DevelopmentCard devCard,int sendToID) {
		CardType pdc;
		if (devCard == null){
			pdc = CardType.UNKNOWN;
		} else {
			pdc = ModelToProtocol.devCardToCardType(devCard);
		}	
		ProtocolBoughtDevelopmentCard pbdc = new ProtocolBoughtDevelopmentCard(player_id, pdc);
		Response r = new Response();
		r.pBoughtDevelopmentCard = pbdc;
		try {
			server.sendToClient(parser.createString(r),sendToID);
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}
	}

}