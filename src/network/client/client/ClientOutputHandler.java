package network.client.client;

import java.io.IOException;

import parsing.Parser;
import parsing.Response;
import protocol.clientinstructions.ProtocolEndTurn;
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
		// TODO Auto-generated method stub

	}

	/**
	 * If the connection can be established, send "Hello" back to server.
	 */
	public void clientHello(String clientVersion) {
		ProtocolHello ph = new ProtocolHello(clientVersion,null);
		try {
			client.write(parser.createString(ph));
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
		// TODO Auto-generated method stub

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

	public void requestSetBandit(int x, int y, int stealFromPlayerId) {
		// TODO Auto-generated method stub

	}

}
