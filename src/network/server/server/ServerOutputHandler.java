package network.server.server;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import enums.CardType;
import enums.Color;
import enums.PlayerState;
import enums.ResourceType;
import model.Board;
import model.objects.Field;
import network.ModelToProtocol;
import parsing.Parser;
import parsing.Response;
import protocol.configuration.ProtocolError;
import protocol.configuration.ProtocolGameStarted;
import protocol.configuration.ProtocolVictory;
import protocol.connection.ProtocolHello;
import protocol.connection.ProtocolWelcome;
import protocol.messaging.ProtocolChatReceiveMessage;
import protocol.messaging.ProtocolServerResponse;
import protocol.object.ProtocolBoard;
import protocol.object.ProtocolBuilding;
import protocol.object.ProtocolField;
import protocol.object.ProtocolHarbour;
import protocol.object.ProtocolPlayer;
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
import protocol.serverinstructions.trade.ProtocolTradePreview;
import protocol3.serverinstructions.ProtocolBiggestKnightProwess;
import protocol3.serverinstructions.ProtocolBoughtDevelopmentCard;
import protocol3.serverinstructions.ProtocolInventionCardInfo;
import protocol3.serverinstructions.ProtocolLongestRoad;
import protocol3.serverinstructions.ProtocolMonopolyCardInfo;
import protocol3.serverinstructions.ProtocolPlayKnightCard;
import protocol3.serverinstructions.ProtocolRoadBuildingCardInfo;

//import static org.apache.logging.log4j.FormatterLoggerManualExample.logger;

public class ServerOutputHandler {
    private Server server;
    private Parser parser;

    public ServerOutputHandler(Server server) {
        this.server = server;
        this.parser = server.getServerInputHandler().getParser();
    }

    private static Logger logger = LogManager.getLogger(ServerOutputHandler.class.getName());

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
     * Create ProtocolHello JSON and broadcast it as a String through server
     *
     * @param serverVersion
     * @param protocolVersion
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

    public void initBoard(int amountPlayers, Board board) {

        ProtocolField[] pfArray = new ProtocolField[board.getStringToCoordMap().size()];
        int counter = 0;

        for (String key : board.getStringToCoordMap().keySet()) {
            int coords[] = board.getStringToCoordMap().get(key);
            Field f = board.getFieldAt(coords[0], coords[1]);
            pfArray[counter] = new ProtocolField(f.getFieldID(),
                    ModelToProtocol.resourceToString.get(f.getResourceType()), f.getDiceIndex());
            counter++;

        }

        ProtocolBuilding[] pBuildingsArray = {};
        ProtocolHarbour[] pHarbourArray = {};
        ProtocolBoard pb = new ProtocolBoard(pfArray, pBuildingsArray, pHarbourArray, board.getBandit());
        ProtocolGameStarted pgs = new ProtocolGameStarted(pb);
        Response r = new Response();
        r.pGameStarted = pgs;
        try {
            server.broadcast(parser.createString(r));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

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

    public void resourceObtain(int playerID, int[] resources) {
    	ProtocolResource pResource;
    	if (resources.length == 1){
    		pResource = new ProtocolResource(null, null, null, null, null, resources[0]);
    	} else {
    		pResource = new ProtocolResource(resources[0], resources[1], resources[4], resources[2], resources[3],
                null);
    	}      
        ProtocolResourceObtain po = new ProtocolResourceObtain(playerID, pResource);
        Response r = new Response();
        r.pRObtain = po;
        try {
            server.broadcast(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }
    }

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

    public void statusUpdate(int playerID, Color color, String name, PlayerState status, int victoryPoints,
                             int[] resources, Integer sendToPlayerID) {

        // Build costs: {WOOD, CLAY, ORE, SHEEP, CORN}
        ProtocolResource pResource;
        if (resources.length == 1) {
            pResource = new ProtocolResource(null, null, null, null, null, resources[0]);
        } else {
            pResource = new ProtocolResource(resources[0], resources[1], resources[4], resources[2], resources[3],
                    null);
        }
        ProtocolPlayer pPlayer = new ProtocolPlayer(playerID, color, name, status, victoryPoints, pResource);
        ProtocolStatusUpdate ps = new ProtocolStatusUpdate(pPlayer);
        Response r = new Response();
        r.pSUpdate = ps;
        try {
            if (sendToPlayerID != null) {
                System.out.println("Send " + status + " to " + sendToPlayerID);
                server.sendToClient(parser.createString(r), sendToPlayerID);
            } else {
                server.broadcast((parser.createString(r)));
            }
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

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

    public void costs(int playerID, int[] costs) {
        ProtocolResource pr;
        if (costs.length > 1) {
            pr = new ProtocolResource(costs[0], costs[1], costs[4], costs[2], costs[3], 0);
        } else {
            pr = new ProtocolResource(0, 0, 0, 0, 0, costs[0]);
        }
        ProtocolCosts pc = new ProtocolCosts(playerID, pr);
        Response r = new Response();
        r.pCosts = pc;
        try {
            server.sendToClient(parser.createString(r), playerID);
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }
    }

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

    public void robberMovement(int player_id, String location_id, Integer victim_id) {
        ProtocolRobberMovement pm = new ProtocolRobberMovement(player_id, location_id, victim_id);

        Response r = new Response();
        r.pRobberMovement = pm;
        try {
            server.sendToClient(parser.createString(r), player_id);
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void tradePreview(int player_id, int trade_id, int[] offer, int[] demand) {
        ProtocolResource proff;
        if (offer.length > 1) {
            proff = new ProtocolResource(offer[0], offer[1], offer[4], offer[2], offer[3], 0);
        } else {
            proff = new ProtocolResource(0, 0, 0, 0, 0, offer[0]);
        }
        ProtocolResource prw;
        if (demand.length > 1) {
            prw = new ProtocolResource(demand[0], demand[1], demand[4], demand[2], demand[3], 0);
        } else {
            prw = new ProtocolResource(0, 0, 0, 0, 0, demand[0]);
        }

        ProtocolTradePreview ptp = new ProtocolTradePreview(player_id, trade_id, proff, prw);
        Response r = new Response();
        r.pTradePreview = ptp;
        try {
            server.broadcast(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void tradeConfirmation(int player_id, int trade_id) {
        ProtocolTradeConfirmation pc = new ProtocolTradeConfirmation(player_id, trade_id);
        Response r = new Response();
        r.pTradeConfirm = pc;
        try {
            server.broadcast(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }
    }

    public void tradeIsCanceled(int player_id, int trade_id) {
        ProtocolTradeIsCanceled ptic = new ProtocolTradeIsCanceled(player_id, trade_id);
        Response r = new Response();
        r.pTradeIsCanceled = ptic;
        try {
            server.broadcast(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }
    }

    public void tradeIsCompleted(int player_id, int tradePartner_id) {
        ProtocolTradeIsCompleted ptico = new ProtocolTradeIsCompleted(player_id, tradePartner_id);
        Response r = new Response();
        r.pTradeIsCompleted = ptico;
        try {
            server.broadcast(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void buildVillage(int x, int y, int dir, int playerID) {
        String location = ModelToProtocol.getCornerID(x, y, dir);
        ProtocolBuilding pb = new ProtocolBuilding(playerID, "Dorf", location);
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

    public void buildStreet(int x, int y, int dir, int playerID) {
        String location = ModelToProtocol.getEdgeID(x, y, dir);
        ProtocolBuilding pb = new ProtocolBuilding(playerID, "Straße", location);
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

    public void buildCity(int x, int y, int dir, int playerID) {
        String location = ModelToProtocol.getCornerID(x, y, dir);
        ProtocolBuilding pb = new ProtocolBuilding(playerID, "Stadt", location);
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

    public void biggestKnightProwess(int player_id) {
        ProtocolBiggestKnightProwess pbkp = new ProtocolBiggestKnightProwess(player_id);
        Response r = new Response();
        r.pBiggestKnightProwess = pbkp;
        try {
            server.broadcast(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void inventionCardInfo(int[] resource, Integer player_id) {
        ProtocolResource pr;
        if (resource.length > 1) {
            pr = new ProtocolResource(resource[0], resource[1], resource[2], resource[3], resource[4], 0);
        } else {
            pr = new ProtocolResource(0, 0, 0, 0, 0, resource[0]);
        }
        ProtocolInventionCardInfo pici = new ProtocolInventionCardInfo(pr, player_id);
        Response r = new Response();
        r.pInventionCardInfo = pici;
        try {
            server.broadcast(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }
    }


    public void longestRoad(int player_id) {
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

    public void monopolyCardInfo(ResourceType resourceType, Integer player_id) {

        ProtocolMonopolyCardInfo pmci = new ProtocolMonopolyCardInfo(resourceType, player_id);
        Response r = new Response();
        r.pMonopolyCardInfo = pmci;
        try {
            server.broadcast(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void playKnight() {
        // This should be deleted
    }

    public void playKnightCard(String road1_id, int target, Integer player_id) {
        ProtocolPlayKnightCard ppkc = new ProtocolPlayKnightCard(road1_id, target, player_id);
        Response r = new Response();
        r.pplayKnightCard = ppkc;
        try {
            server.broadcast(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }
    }

    public void roadBuildingCardInfo(String road1_id, String road2_id, Integer player_id) {
        ProtocolRoadBuildingCardInfo prbci = new ProtocolRoadBuildingCardInfo(road1_id, road2_id, player_id);
        Response r = new Response();
        r.pRoadBuildingCardInfo = prbci;
        try {
            server.broadcast(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }


    }

    public void serverConfirm(String server_response, int threadPlayerID) {
        ProtocolServerResponse psc = new ProtocolServerResponse(server_response);
        try {
            server.sendToClient(parser.createString(psc), threadPlayerID);
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }
    }

    public void boughtDevelopmentCard(int player_id, CardType developmentCard) {

        ProtocolBoughtDevelopmentCard pbdc = new ProtocolBoughtDevelopmentCard(player_id, developmentCard);
        Response r = new Response();
        r.pBoughtDevelopmentCard = pbdc;
        try {
            server.broadcast(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }
    }

}