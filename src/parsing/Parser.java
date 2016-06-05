package parsing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Parser {
	@SuppressWarnings("unchecked")
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

		// TODO logging
		System.out.println("CANNOT READ INPUT");
		return null;

	}

	public String createString(Object fromObject) {
		Gson gson = new GsonBuilder().create();
		String response = gson.toJson(fromObject, fromObject.getClass());
		return response;
	}
}
