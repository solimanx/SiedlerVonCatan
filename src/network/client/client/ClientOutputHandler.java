package network.client.client;

import parsing.Parser;
import protocol.connection.ProtocolHello;

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

	public void clientHello(String clientVersion) {
		ProtocolHello ph = new ProtocolHello(clientVersion,null);
		parser.createString(ph);

	}

	public void clientReady() {
		// TODO Auto-generated method stub

	}

	public void chatSendMessage(String s) {
		// TODO Auto-generated method stub

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
