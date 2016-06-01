package network.client.client;

import java.io.IOException;

import parsing.Parser;
import parsing.Response;
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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	public void requestSetBandit(int x, int y, int stealFromPlayerId) {
		// TODO Auto-generated method stub

	}

}
