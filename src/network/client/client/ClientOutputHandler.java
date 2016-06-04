package network.client.client;

import java.io.IOException;
import parsing.Parser;
import parsing.Response;
import protocol.clientinstructions.ProtocolBuildRequest;
import protocol.clientinstructions.ProtocolDiceRollRequest;
import protocol.clientinstructions.ProtocolEndTurn;
import protocol.clientinstructions.ProtocolRobberMovementRequest;
import protocol.configuration.ProtocolClientReady;
import protocol.connection.ProtocolHello;
import protocol.messaging.ProtocolChatSendMessage;

public class ClientOutputHandler {

	private Client client;
	private Parser parser;

	public ClientOutputHandler(Client client) {
		this.client = client;
		this.parser = new Parser();
	}

	public void handleBuildRequest(int x, int y, int dir, int playerId, String string) {
		//TODO fix
	    String location = x + "," + y + "," + dir;
		ProtocolBuildRequest pbr = new ProtocolBuildRequest(string, location);
		Response r = new Response();
		r.pBuildRequest = pbr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			//TODO logging
			e.printStackTrace();
		}
		
		
		

	}

	/**
	 * If the connection can be established, send "Hello" back to server.
	 */
	public void clientHello(String clientVersion) {
		ProtocolHello ph = new ProtocolHello(clientVersion,null);
		Response r = new Response();
		r.pHello = ph;
		try {
			System.out.println("CLIENT HELLO OUTPUT: " + parser.createString(r));
			client.write(parser.createString(r));
		} catch (IOException e) {
			// TODO logging
			e.printStackTrace();
		}

	}

	public void clientReady() {
		ProtocolClientReady pcr = new ProtocolClientReady();
		Response r = new Response();
		r.pClientReady = pcr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e){
            // TODO logging
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
			// TODO logging
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
			// TODO logging
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
			// TODO logging
			e.printStackTrace();
		}

	}

	public void requestSetBandit(int playerid, int x, int y, int victim_id) {
		//TODO fix
		String location = x + "," + y;
		ProtocolRobberMovementRequest prmr = new ProtocolRobberMovementRequest(playerid, location, victim_id);
		Response r = new Response();
		r.pRobberMoveRequest = prmr;
		try {
			client.write(parser.createString(r));
		} catch (IOException e) {
			// TODO logging
			e.printStackTrace();
		}
		
		
		
		

	}

}
