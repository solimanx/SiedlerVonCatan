package parsing;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Parser {
	Response response;
	Gson gson = new GsonBuilder().create();
	HashMap<String,Object> map;

	public Parser() {
		map = new HashMap<String, Object>();
	}

	@SuppressWarnings("unchecked")
	public <T> T parseString(String string) {

		response = gson.fromJson(string, Response.class);

		//TODO perhaps reference to variables so it doesn't have to refresh map everytime
		refreshMap();
	 
		for ( HashMap.Entry<String, Object> entry : map.entrySet() ) {
			if(entry.getValue()!=null){
				return (T) entry.getValue();
			}
		   
		}
		return null;
	}

	private void refreshMap() {
		map.put("ProtocolHello", response.pHello);
		map.put("ProtocolWelcome", response.pWelcome);
		map.put("ProtocolClientReady", response.pClientReady);
		map.put("ProtocolError", response.pError);
		map.put("ProtocolGameStarted", response.pGameStarted);
		map.put("ProtocolPlayerProfile", response.pPlayerProfile);
		map.put("ProtocolVictory", response.pVictory);
		map.put("ProtocolBuild", response.pBuild);
		map.put("ProtocolDiceRollResult", response.pDRResult);
		map.put("ProtocolResourceObtain", response.pRObtain);
		map.put("Protocolstatusupdate", response.pSUpdate);
		map.put("ProtocolCosts",response.pCosts);
		map.put("ProtocolRobberMovement", response.pRobberMovement);
		map.put("ProtocolChatReceiveMessage", response.pChatReceive);
		map.put("ProtocolChatSendMessage", response.pChatSend);
		map.put("ProtocolServerConfirmation", response.pServerConfirmation);
		map.put("ProtocolBuildRequest", response.pBuildRequest);
		map.put("ProtocolDiceRollRequest", response.pDiceRollRequest);
		map.put("ProtocolEndTurn", response.pEndTurn);
		map.put("ProtocolRobberLoss", response.pRobberLoss);
		map.put("ProtocolRobberMovementRequest", response.pRobberMoveRequest);
		map.put("ProtocolHarbourRequest", response.pHarbourRequest);
		map.put("ProtocolTradeRequest", response.pTradeRequest);
		map.put("ProtocolTradePreview",response.pTradePreview);
		map.put("ProtocolTradeAccept", response.pTradeAccept);
		map.put("ProtocolTradeConfirm", response.pTradeConfirm);

	}

	public String createString(Object fromObject) {
		String response = gson.toJson(fromObject, fromObject.getClass());
		return response;
	}
}
