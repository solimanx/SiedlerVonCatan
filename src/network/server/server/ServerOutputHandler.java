package network.server.server;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import enums.Color;
import enums.PlayerState;
import model.Board;
import model.Corner;
import model.Edge;
import model.Field;
import network.ModelToProtocol;
import network.server.controller.ServerNetworkController;
import parsing.Parser;
import parsing.Response;
import protocol.clientinstructions.trade.ProtocolTradeIsRequested;
import protocol.configuration.ProtocolError;
import protocol.configuration.ProtocolGameStarted;
import protocol.connection.ProtocolHello;
import protocol.connection.ProtocolWelcome;
import protocol.messaging.ProtocolChatReceiveMessage;
import protocol.object.ProtocolBoard;
import protocol.object.ProtocolBuilding;
import protocol.object.ProtocolField;
import protocol.object.ProtocolHarbour;
import protocol.object.ProtocolBuilding;
import protocol.object.ProtocolPlayer;
import protocol.object.ProtocolResource;
import protocol.serverinstructions.ProtocolBuild;
import protocol.serverinstructions.ProtocolDiceRollResult;
import protocol.serverinstructions.ProtocolResourceObtain;
import protocol.serverinstructions.ProtocolStatusUpdate;
import protocol.serverinstructions.trade.ProtocolTradeConfirmation;
import protocol.serverinstructions.trade.ProtocolTradePreview;

//import static org.apache.logging.log4j.FormatterLoggerManualExample.logger;

public class ServerOutputHandler {
    private Server server;
    private Parser parser;
    private ServerNetworkController networkController;

    public ServerOutputHandler(Server server) {
        this.server = server;
        this.networkController = server.getServerInputHandler().getServerNetworkController();
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

        ProtocolBuilding[] pBuildingsArray = null;
        ProtocolHarbour[] pHarbourArray = null;
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

    public void error(String s) {
        ProtocolError pe = new ProtocolError(s);
        Response r = new Response();
        r.pError = pe;

        try {
            server.broadcast(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }
    }

    public void diceRollResult(int playerID, int result) {
        ProtocolDiceRollResult dr = new ProtocolDiceRollResult(ModelToProtocol.getPlayerId(playerID), result);
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
        ProtocolResourceObtain po = new ProtocolResourceObtain(playerID, ModelToProtocol.getResources(resources));
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
            server.sendToClient(parser.createString(r), networkController.getThreadID(player_id));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void statusUpdate(int playerID, Color color, String name, PlayerState status, int victoryPoints,
                             int[] resources) {
        // Build costs: {WOOD, CLAY, ORE, SHEEP, CORN}
        //
        // ProtocolResource pResourceToPlayer = new
        // ProtocolResource(resources[0], resources[1], resources[3],
        // resources[4], resources[2], null);
        // ProtocolPlayer player = new ProtocolPlayer(playerID, color, name,
        // status, victoryPoints, pResourceToPlayer);
        // ProtocolStatusUpdate ps = new ProtocolStatusUpdate(player);
        // Response r = new Response();
        // r.pSUpdate = ps;
        // try {
        // server.broadcast((parser.createString(r)));
        // } catch (IOException e) {
        // logger.error("Threw a Input/Output Exception ", e);
        // e.printStackTrace();
        // }
        //

    }

    public void gameStarted(ProtocolBoard board) {
        ProtocolGameStarted pb = new ProtocolGameStarted(board);
        Response r = new Response();
        r.pGameStarted = pb;
        try {
            server.broadcast(parser.createString(r));
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

    public void ProtocolTradeIsRequested(int player_id, int trade_id, ProtocolResource offer, ProtocolResource withdrawal) {
        ProtocolTradeIsRequested ptis = new ProtocolTradeIsRequested(player_id, trade_id, offer, withdrawal);
        Response r = new Response();
        r.pTradeIsRequested = ptis;
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


}