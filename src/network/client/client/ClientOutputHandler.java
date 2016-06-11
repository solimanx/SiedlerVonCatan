package network.client.client;

import java.io.IOException;

import enums.ResourceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import network.ModelToProtocol;
import parsing.Parser;
import parsing.Response;
import protocol.clientinstructions.*;
import protocol.clientinstructions.trade.ProtocolTradeAccept;
import protocol.clientinstructions.trade.ProtocolTradeCancel;
import protocol.clientinstructions.trade.ProtocolTradeComplete;
import protocol.clientinstructions.trade.ProtocolTradeRequest;
import protocol.configuration.ProtocolClientReady;
import protocol.configuration.ProtocolPlayerProfile;
import protocol.connection.ProtocolHello;
import protocol.messaging.ProtocolChatSendMessage;
import protocol.object.ProtocolResource;
import protocol.serverinstructions.ProtocolRobberMovement;
import protocol3.clientinstructions.ProtocolBuyDevelopmentCards;
import protocol3.clientinstructions.ProtocolDevelopmentCards;
import protocol3.object.ProtocolInventionCard;
import protocol3.object.ProtocolMonopolyCard;
import protocol3.object.ProtocolRoadBuildingCard;
import protocol3.severinstructions.ProtocolInventionCardInfo;
import protocol3.severinstructions.ProtocolMonopolyCardInfo;
import protocol3.severinstructions.ProtocolPlayKnightCard;
import protocol3.severinstructions.ProtocolRoadBuildingCardInfo;

public class ClientOutputHandler {

    private Client client;
    private Parser parser;

    public ClientOutputHandler(Client client) {
        this.client = client;
        this.parser = new Parser();
    }

    private static Logger logger = LogManager.getLogger(ClientOutputHandler.class.getName());

    /**
     * If the connection can be established, send "Hello" back to server.
     */
    public void clientHello(String clientVersion) {
        ProtocolHello ph = new ProtocolHello(clientVersion, null);
        Response r = new Response();
        r.pHello = ph;
        try {
            client.write(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void clientReady() {
        ProtocolClientReady pcr = new ProtocolClientReady();
        Response r = new Response();
        r.pClientReady = pcr;
        try {
            client.write(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void chatSendMessage(String s) {
        ProtocolChatSendMessage pcsm = new ProtocolChatSendMessage(s);
        Response r = new Response();
        r.pChatSend = pcsm;
        try {
            client.write(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void diceRollRequest() {
        ProtocolDiceRollRequest pdrr = new ProtocolDiceRollRequest();
        Response r = new Response();
        r.pDiceRollRequest = pdrr;
        try {
            client.write(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();

        }
    }

    public void endTurn() {

        ProtocolEndTurn pcet = new ProtocolEndTurn();
        Response r = new Response();
        r.pEndTurn = pcet;
        try {
            client.write(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void requestSetBandit(int x, int y, int victim_id) {
        String location = ModelToProtocol.getFieldID(x, y);

        ProtocolRobberMovementRequest prmr = new ProtocolRobberMovementRequest(location, victim_id);
        Response r = new Response();
        r.pRobberMoveRequest = prmr;
        try {
            client.write(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void sendPlayerProfile(String name, enums.Color color) {

        ProtocolPlayerProfile pcr = new ProtocolPlayerProfile(name, color);
        Response r = new Response();
        r.pPlayerProfile = pcr;
        try {
            client.write(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void robberLoss(int[] losses) {
        ProtocolResource prl;
        if (losses.length > 1) {
            prl = new ProtocolResource(losses[0], losses[1], losses[3], losses[4], losses[5], 0);
        } else {
            prl = new ProtocolResource(0, 0, 0, 0, 0, losses[0]);
        }
        ProtocolRobberLoss prlosses = new ProtocolRobberLoss(prl);
        Response r = new Response();
        r.pRobberLoss = prlosses;
        try {
            client.write(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void robberMovementRequest(String location_id, int victim_id) {
        ProtocolRobberMovementRequest prmr = new ProtocolRobberMovementRequest(location_id, victim_id);
        Response r = new Response();
        r.pRobberMoveRequest = prmr;
        try {
            client.write(parser.createString(r));

        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void handleHarbourRequest(int[] offer, int[] withdrawal) {
        ProtocolResource proff;
        if (offer.length > 1) {
            proff = new ProtocolResource(offer[0], offer[1], offer[3], offer[4], offer[5], 0);
        } else {
            proff = new ProtocolResource(0, 0, 0, 0, 0, offer[0]);
        }
        ProtocolResource prw;
        if (withdrawal.length > 1) {
            prw = new ProtocolResource(withdrawal[0], withdrawal[1], withdrawal[2], withdrawal[3], withdrawal[4], 0);
        } else {
            prw = new ProtocolResource(0, 0, 0, 0, 0, withdrawal[0]);
        }
        ProtocolHarbourRequest phr = new ProtocolHarbourRequest(proff, prw);
        Response r = new Response();
        r.pHarbourRequest = phr;
        try {
            client.write(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void handleTradeAccept(int trade_id) {
        ProtocolTradeAccept pta = new ProtocolTradeAccept(trade_id);
        Response r = new Response();
        r.pTradeAccept = pta;
        try {
            client.write(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }
    }

    public void handleTradeRequest(int[] offer, int[] withdrawal) {
        ProtocolResource proff;
        if (offer.length > 1) {
            proff = new ProtocolResource(offer[0], offer[1], offer[3], offer[4], offer[5], 0);
        } else {
            proff = new ProtocolResource(0, 0, 0, 0, 0, offer[0]);
        }
        ProtocolResource prw;
        if (withdrawal.length > 1) {
            prw = new ProtocolResource(withdrawal[0], withdrawal[1], withdrawal[2], withdrawal[3], withdrawal[4], 0);
        } else {
            prw = new ProtocolResource(0, 0, 0, 0, 0, withdrawal[0]);
        }
        ProtocolTradeRequest ptr = new ProtocolTradeRequest(proff, prw);
        Response r = new Response();
        r.pTradeRequest = ptr;
        try {
            client.write(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }
    }

    public void handleTradeCancel(int trade_id) {
        ProtocolTradeCancel ptc = new ProtocolTradeCancel(trade_id);
        Response r = new Response();
        r.pTradeCancel = ptc;
        try {
            client.write(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();

        }

    }

    public void handleTradeComplete(int trade_id, int tradePartner_id) {
        ProtocolTradeComplete ptco = new ProtocolTradeComplete(trade_id, tradePartner_id);
        Response r = new Response();
        r.pTradeComplete = ptco;
        try {
            client.write(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();

        }
    }

    public void handleBuyDevelopmentCards() {
        ProtocolBuyDevelopmentCards pbdc = new ProtocolBuyDevelopmentCards();
        Response r = new Response();
        r.pBuyDevCards = pbdc;
        try {
            client.write(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();

        }
    }

    public void requestBuildVillage(int x, int y, int dir) {
        String location = ModelToProtocol.getCornerID(x, y, dir);
        ProtocolBuildRequest pbr = new ProtocolBuildRequest("Dorf", location);

        Response r = new Response();
        r.pBuildRequest = pbr;
        try {
            client.write(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void requestBuildStreet(int x, int y, int dir) {
        String location = ModelToProtocol.getEdgeID(x, y, dir);
        ProtocolBuildRequest pbr = new ProtocolBuildRequest("Straße", location);

        Response r = new Response();
        r.pBuildRequest = pbr;
        try {
            client.write(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void requestBuildCity(int x, int y, int dir) {
        String location = ModelToProtocol.getCornerID(x, y, dir);
        ProtocolBuildRequest pbr = new ProtocolBuildRequest("Stadt", location);

        Response r = new Response();
        r.pBuildRequest = pbr;
        try {
            client.write(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void handleMonopoly(int[] resource) {
        ProtocolResource pr;
        if (resource.length > 0) {
            pr = new ProtocolResource(resource[0], resource[1], resource[3], resource[4], resource[5], 0);
        } else {
            pr = new ProtocolResource(0, 0, 0, 0, 0, resource[0]);
        }
        ProtocolMonopolyCard pmc = new ProtocolMonopolyCard(pr);
        Response r = new Response();
        r.pMonopolyCard = pmc;
        try {
            client.write(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void inventionCardInfo(int[] resource) {
        ProtocolResource pr;
        if (resource.length > 0) {
            pr = new ProtocolResource(resource[0], resource[1], resource[3], resource[4], resource[5], 0);
        } else {
            pr = new ProtocolResource(0, 0, 0, 0, 0, resource[0]);
        }
        ProtocolInventionCardInfo pici = new ProtocolInventionCardInfo(pr);
        Response r = new Response();
        r.pInventionCardInfo = pici;
        try {
            client.write(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void monopolyCardInfo(ResourceType resource) {

        ProtocolMonopolyCardInfo pmci = new ProtocolMonopolyCardInfo(resource);
        Response r = new Response();
        r.pMonopolyCardInfo = pmci;
        try {
            client.write(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void roadBuildingCardInfo(String road1_id, String road2_id) {
        ProtocolRoadBuildingCardInfo prbci = new ProtocolRoadBuildingCardInfo(road1_id, road2_id);
        Response r = new Response();
        r.pRoadBuildingCardInfo = prbci;
        try {
            client.write(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }

    }

    public void playKnightCard(String road1_id, int target) {
        ProtocolPlayKnightCard ppkc = new ProtocolPlayKnightCard(road1_id, target);
        Response r = new Response();
        r.pplayKnightCard = ppkc;
        try {
            client.write(parser.createString(r));
        } catch (IOException e) {
            logger.error("Threw a Input/Output Exception ", e);
            e.printStackTrace();
        }
    }

}