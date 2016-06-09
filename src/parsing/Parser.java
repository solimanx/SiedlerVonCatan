package parsing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Parser {
    @SuppressWarnings("unchecked")
    private static Logger logger = LogManager.getLogger(Parser.class.getName());

    public <T> T parseString(String string) {

        Gson gson = new GsonBuilder().create();
        Response response = gson.fromJson(string, Response.class);

        if (response.pHello != null) {
            return (T) response.pHello;
        }

        if (response.pWelcome != null) {
            return (T) response.pWelcome;
        }

        //

        if (response.pClientReady != null) {
            return (T) response.pClientReady;
        }

        if (response.pError != null) {
            return (T) response.pError;
        }

        if (response.pGameStarted != null) {
            return (T) response.pGameStarted;
        }

        if (response.pPlayerProfile != null) {
            return (T) response.pPlayerProfile;
        }

        //

        if (response.pBuild != null) {
            return (T) response.pBuild;
        }

        if (response.pDRResult != null) {
            return (T) response.pDRResult;
        }

        if (response.pRObtain != null) {
            return (T) response.pRObtain;
        }

        if (response.pSUpdate != null) {
            return (T) response.pSUpdate;
        }

        // ...

        if (response.pChatReceive != null) {
            return (T) response.pChatReceive;
        }

        if (response.pChatSend != null) {
            return (T) response.pChatSend;
        }

        if (response.pServerConfirmation != null) {
            return (T) response.pServerConfirmation;
        }

        // ..

        if (response.pBuildRequest != null) {
            return (T) response.pBuildRequest;
        }

        if (response.pDiceRollRequest != null) {
            return (T) response.pDiceRollRequest;
        }

        if (response.pEndTurn != null) {
            return (T) response.pEndTurn;
        }
        //in Protocol 0.2
        if (response.pVictory != null) {
            return (T) response.pVictory;
        }

        if (response.pCosts != null) {
            return (T) response.pCosts;
        }

        if (response.pRobberMovement != null) {
            return (T) response.pRobberMovement;
        }

        if (response.pRobberLoss != null) {
            return (T) response.pRobberLoss;

        }
        if (response.pHarbourRequest != null) {
            return (T) response.pHarbourRequest;

        }
        if (response.pTradeRequest != null) {
            return (T) response.pTradeRequest;

        }
        if (response.pTradeIsRequested != null) {
            return (T) response.pTradeIsRequested;

        }
        if (response.pTradeAccept != null) {
            return (T) response.pTradeAccept;

        }

        if (response.pTradeConfirm != null) {
            return (T) response.pTradeConfirm;

        }
        if (response.pTradeComplete != null) {
            return (T) response.pTradeComplete;

        }
        if (response.pTradeIsCompleted != null) {
            return (T) response.pTradeIsCompleted;

        }
        if (response.pTradeCancel != null) {
            return (T) response.pTradeCancel;

        }

        if (response.pTradeIsCanceled != null) {
            return (T) response.pTradeIsCanceled;

        }
        //in Protocol 0.3
        if (response.pBuyDevCards != null) {
            return (T) response.pBuyDevCards;
        }
        if (response.pDevelopmentCard != null) {
            return (T) response.pDevelopmentCard;
        }
        if (response.pBuyDevCards != null) {
            return (T) response.pBuyDevCards;
        }
        if (response.pInvention != null) {
            return (T) response.pInvention;
        }
        if (response.pMonopolyCard != null) {
            return (T) response.pMonopolyCard;
        }
        if (response.pRoadBuildCard != null) {
            return (T) response.pRoadBuildCard;
        }
        if (response.pInventionCardInfo != null) {
            return (T) response.pInventionCardInfo;
        }

        if (response.pLongestRoad != null) {
            return (T) response.pLongestRoad;
        }
        if (response.pMonopolyCardInfo != null) {
            return (T) response.pMonopolyCardInfo;
        }

        if (response.pplayKnightCard != null) {
            return (T) response.pplayKnightCard;
        }

        if (response.pRoadBuildingCardInfo != null) {
            return (T) response.pRoadBuildingCardInfo;
        }

        logger.info("CANNOT READ INPUT");
        System.out.println("CANNOT READ INPUT");
        return null;

    }

    public String createString(Object fromObject) {
        Gson gson = new GsonBuilder().create();
        String response = gson.toJson(fromObject, fromObject.getClass());
        return response;
    }
}
