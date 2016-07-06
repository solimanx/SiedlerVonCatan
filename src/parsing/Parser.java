package parsing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// TODO: Auto-generated Javadoc
public class Parser {
	private static Logger logger = LogManager.getLogger(Parser.class.getSimpleName());

	/**
	 * Parses the string.
	 *
	 * @param <T> the generic type
	 * @param string the string
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public <T> T parseString(String string) {

		Gson gson = new GsonBuilder().create();
		try{
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
		// in Protocol 0.2
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
		if (response.pRobberMoveRequest != null) {
			return (T) response.pRobberMoveRequest;
		}
		if (response.pHarbourRequest != null) {
			return (T) response.pHarbourRequest;

		}
		if (response.pTradeRequest != null) {
			return (T) response.pTradeRequest;

		}
		if (response.pTradePreview != null) {
			return (T) response.pTradePreview;

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
		// in Protocol 0.3
		if (response.pBuyDevCards != null) {
			return (T) response.pBuyDevCards;
		}
		if (response.pBoughtDevelopmentCard != null) {
			return (T) response.pBoughtDevelopmentCard;
		}

		if (response.pPlayKnightCard != null) {
			return (T) response.pPlayKnightCard;
		}

		if (response.pPlayRoadCard != null) {
			return (T) response.pPlayRoadCard;
		}

		if (response.pPlayInventionCard != null) {
			return (T) response.pPlayInventionCard;
		}
		if (response.pPlayMonopolyCard != null) {
			return (T) response.pPlayMonopolyCard;
		}

		if (response.pLargestArmy != null) {
			return (T) response.pLargestArmy;
		}

		if (response.pLongestRoad != null) {
			return (T) response.pLongestRoad;
		}

		logger.warn("CANNOT READ INPUT");
		return null;
		}
		catch(com.google.gson.JsonSyntaxException je){
			return (T) string;
		}
	}

	/**
	 * Creates the string.
	 *
	 * @param fromObject the from object
	 * @return the string
	 */
	public String createString(Object fromObject) {
		Gson gson = new GsonBuilder().create();
		String response = gson.toJson(fromObject, fromObject.getClass());
		return response;
	}
}
