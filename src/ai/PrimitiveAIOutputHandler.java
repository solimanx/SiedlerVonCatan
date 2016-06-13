package ai;

import java.io.IOException;

import application.Main;
import enums.Color;
import network.ModelToProtocol;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parsing.Parser;
import parsing.Response;
import protocol.clientinstructions.ProtocolBuildRequest;
import protocol.clientinstructions.ProtocolDiceRollRequest;
import protocol.clientinstructions.ProtocolEndTurn;
import protocol.clientinstructions.ProtocolRobberLoss;
import protocol.clientinstructions.ProtocolRobberMovementRequest;
import protocol.configuration.ProtocolClientReady;
import protocol.configuration.ProtocolPlayerProfile;
import protocol.connection.ProtocolHello;
import protocol.messaging.ProtocolChatSendMessage;
import protocol.object.ProtocolResource;

public class PrimitiveAIOutputHandler {
    private static Logger logger = LogManager.getLogger(PrimitiveAIOutputHandler.class.getName());
    private PrimitiveAI ai;
    private Parser parser;

    public PrimitiveAIOutputHandler(PrimitiveAI primitiveAI) {
        ai = primitiveAI;
        parser = new Parser();
    }

    /**
     * Say hello to server.
     *
     * @param version
     */
    protected void respondHello(String version) {
        ProtocolHello ph = new ProtocolHello(version, null);
        Response r = new Response();
        r.pHello = ph;
        try {
            ai.write(parser.createString(r));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.throwing(Level.DEBUG, e);
            e.printStackTrace();
        }

    }

    /**
     * Tell the server it's ready to begin the game
     */
    protected void respondStartGame() {
        ProtocolClientReady pcr = new ProtocolClientReady();
        Response r = new Response();
        r.pClientReady = pcr;
        try {
            ai.write(parser.createString(r));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.catching(Level.DEBUG, e);
            e.printStackTrace();
        }
    }

    /**
     * Goes on a loop to find the first non taken color
     *
     * @param colorCounter
     */
    public void respondProfile(int colorCounter) {
        ProtocolPlayerProfile ppp;
        switch (colorCounter) {
            case 0:
                ppp = new ProtocolPlayerProfile("BlueBro", Color.BLUE);
                break;
            case 1:
                ppp = new ProtocolPlayerProfile("RedBro", Color.RED);
                break;
            case 2:
                ppp = new ProtocolPlayerProfile("Orange Destroyer", Color.ORANGE);
                break;
            case 3:
                ppp = new ProtocolPlayerProfile("Walter White", Color.WHITE);
                break;
            default:
                ppp = null;
        }
        Response r = new Response();
        r.pPlayerProfile = ppp;
        try {
            ai.write(parser.createString(r));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.catching(Level.DEBUG, e);
            e.printStackTrace();
        }

    }

    /**
     * Building the first (and second) village of the initial round
     *
     * @param j
     * @param i
     * @param k
     */
    public void requestBuildInitialVillage(int j, int i, int k) {
        String location = ModelToProtocol.getCornerID(j, i, k);
        ProtocolBuildRequest pbr = new ProtocolBuildRequest("Dorf", location);

        Response r = new Response();
        r.pBuildRequest = pbr;
        try {
            ai.write(parser.createString(r));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.catching(Level.DEBUG, e);
            e.printStackTrace();
        }

    }

    /**
     * Building the first (and second) road of the initial round
     *
     * @param x
     * @param y
     * @param dir
     */
    public void requestBuildInitialRoad(int x, int y, int dir) {
        String location = ModelToProtocol.getEdgeID(x, y, dir);
        ProtocolBuildRequest pbr = new ProtocolBuildRequest("Straße", location);

        Response r = new Response();
        r.pBuildRequest = pbr;
        try {
            ai.write(parser.createString(r));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.catching(Level.DEBUG, e);
            e.printStackTrace();
        }

    }

    /**
     * Roll the dice
     */
    public void respondDiceRoll() {
        ProtocolDiceRollRequest pdrr = new ProtocolDiceRollRequest();
        Response r = new Response();

        r.pDiceRollRequest = pdrr;
        try {
            ai.write(parser.createString(r));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.catching(Level.DEBUG, e);
            e.printStackTrace();
        }
    }

    /**
     * Send a message to others
     *
     * @param message random message
     */
    public void respondWithMessage(String message) {
        ProtocolChatSendMessage pcsm = new ProtocolChatSendMessage(message);
        Response r = new Response();

        r.pChatSend = pcsm;
        try {
            ai.write(parser.createString(r));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.catching(Level.DEBUG, e);
            e.printStackTrace();
        }

    }

    /**
     * End my turn
     */
    public void respondEndTurn() {
        ProtocolEndTurn pet = new ProtocolEndTurn();
        Response r = new Response();

        r.pEndTurn = pet;
        try {
            ai.write(parser.createString(r));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.catching(Level.DEBUG, e);
            e.printStackTrace();
        }

    }

    /**
     * Lose resources to robber
     *
     * @param losses
     */
    public void respondRobberLoss(int[] losses) {
        ProtocolResource loss = ModelToProtocol.convertToProtocolResource(losses);
        ProtocolRobberLoss prl = new ProtocolRobberLoss(loss);
        Response r = new Response();

        r.pRobberLoss = prl;
        try {
            ai.write(parser.createString(r));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.catching(Level.DEBUG, e);
            e.printStackTrace();
        }

    }

    /**
     * Move robber to a new position
     *
     * @param newRobber
     */
    public void respondMoveRobber(String newRobber) {
        ProtocolRobberMovementRequest prmr = new ProtocolRobberMovementRequest(newRobber, null);
        Response r = new Response();

        r.pRobberMoveRequest = prmr;
        try {
            ai.write(parser.createString(r));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.catching(Level.DEBUG, e);
            e.printStackTrace();
        }

    }

}
