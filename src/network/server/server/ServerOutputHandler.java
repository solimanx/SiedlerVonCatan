package network.server.server;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import enums.Color;
import enums.PlayerState;
import model.Board;
import model.objects.Field;
import network.ModelToProtocol;
import parsing.Parser;
import parsing.Response;
import protocol.serverinstructions.ProtocolCosts;
import protocol.serverinstructions.trade.ProtocolTradeIsCanceled;
import protocol.serverinstructions.trade.ProtocolTradeIsCompleted;
import protocol.serverinstructions.trade.ProtocolTradeIsRequested;
import protocol.configuration.ProtocolError;
import protocol.configuration.ProtocolGameStarted;
import protocol.connection.ProtocolHello;
import protocol.connection.ProtocolWelcome;
import protocol.messaging.ProtocolChatReceiveMessage;
import protocol.object.ProtocolBoard;
import protocol.object.ProtocolBuilding;
import protocol.object.ProtocolField;
import protocol.object.ProtocolHarbour;
import protocol.object.ProtocolResource;
import protocol.serverinstructions.ProtocolBuild;
import protocol.serverinstructions.ProtocolDiceRollResult;
import protocol.serverinstructions.ProtocolResourceObtain;
import protocol.serverinstructions.trade.ProtocolTradeConfirmation;
import protocol3.object.ProtocolInventionCard;
import protocol3.object.ProtocolMonopolyCard;
import protocol3.object.ProtocolRoadBuildingCard;
import protocol3.severinstructions.*;

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
            server.sendToClient(parser.createString(r), player_id);
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

    public void gameStarted(Board board) {
        //TODO: Board in ProtocolBoard umwandeln

		/*ProtocolGameStarted pb = new ProtocolGameStarted(board);
        Response r = new Response();
		r.pGameStarted = pb;
		try {
			server.broadcast(parser.createString(r));
		} catch (IOException e) {
			logger.error("Threw a Input/Output Exception ", e);
			e.printStackTrace();
		}*/
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

    public void costs(ProtocolCosts costs) {
        //TODO
    }

    public void protocolTradeIsRequested(int player_id, int trade_id, ProtocolResource offer,
                                         ProtocolResource withdrawal) {
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
        // TODO Auto-generated method stub

    }

    public void buildStreet(int x, int y, int dir, int playerID) {
        // TODO Auto-generated method stub

    }

    public void buildCity(int x, int y, int dir, int playerID) {
        // TODO Auto-generated method stub

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

    public void inventionCardInfo(ProtocolInventionCard invention) {
        ProtocolInventionCardInfo pici = new ProtocolInventionCardInfo(invention);
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

    public void monopolyCardInfo(ProtocolMonopolyCard monopoly) {
        //TODO
    }

    public void playDevelopmentCards() {
        //TODO
    }

    public void playKnightCard(String road1_id, int target) {
        ProtocolPlayKnightCard ppkc = new ProtocolPlayKnightCard(road1_id, target);
        Response r = new Response();
        r.pplayKnightCard = ppkc;
        try {
            server.broadcast(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }
    }

    public void roadBuildingCardInfo(ProtocolRoadBuildingCard road) {
        ProtocolRoadBuildingCardInfo prbci = new ProtocolRoadBuildingCardInfo(road);
        Response r = new Response();
        r.pRoadBuildingCardInfo = prbci;
        try {
            server.broadcast(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void specialCaseLongestRoad() {
        //ProtocolSpecialCaseLongestRoad psclr = new ProtocolSpecialCaseLongestRoad();
        //   Response r = new Response();
         //r.pSpecialCaseLongestRoad=psclr;
        // try {
        //   server.broadcast(parser.createString(r));
        //} catch (IOException e) {
        //   logger.error("Threw a Input/Output Exception ", e);
        //  e.printStackTrace();
        //}
        //}
    }
}