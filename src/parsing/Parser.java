package parsing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Parser {
	public Object parseString(String string){
    
		Gson gson = new GsonBuilder().serializeNulls().create();
		Response response = gson.fromJson(string , Response.class);

		
		if (response.pHello != null){
		    return response.pHello;
		}
		
		if (response.pWelcome != null){
		    return response.pWelcome;
		}
		
		//
		
		if (response.pClientReady != null){
		    return response.pClientReady;
		}
		
		if (response.pError != null){
		    return response.pError;
		}
		
		if (response.pGameStarted != null){
		    return response.pGameStarted;
		}
		
		if (response.pPlayerProfile != null){
		    return response.pPlayerProfile;
		}
		
		//
		
		if (response.pBuild != null){
		    return response.pBuild;
		}
		
		if (response.pDRResult != null){
		    return response.pDRResult;
		}
		
		if (response.pRObtain != null){
		    return response.pRObtain;
		}
		
		if (response.pSUpdate != null){
		    return response.pSUpdate;
		}
		
		//...
		
		if (response.pChatReceive != null){
		    return response.pChatReceive;
		}
		
		if (response.pChatSend != null){
		    return response.pChatSend;
		}
		
		if (response.pServerConfirmation != null){
		    return response.pServerConfirmation;
		}
		
		// ..
		
		if (response.pHello != null){
		    return response.pBuildRequest;
		}
		
		if (response.pDiceRollRequest != null){
		    return response.pDiceRollRequest;
		}
		
		if (response.pEndTurn != null){
		    return response.pEndTurn;
		}
		
	}

}
