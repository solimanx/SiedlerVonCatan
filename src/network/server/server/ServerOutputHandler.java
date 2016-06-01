package network.server.server;

import java.io.IOException;

import enums.Color;
import enums.PlayerState;
import model.Corner;
import model.Edge;
import model.Field;
import parsing.Parser;
import protocol.connection.ProtocolHello;

public class ServerOutputHandler {
	private Server server;
	private Parser parser;

	public ServerOutputHandler(Server server) {
		this.server = server;
		this.parser = new Parser();
	}

	public void statusUpdate(int playerId, Color color, String name, PlayerState status, int victoryPoints,
			int[] resources) {
		// TODO Auto-generated method stub

	}

	public void buildBuilding(int x, int y, int dir, int playerId, String string) {
		// TODO Auto-generated method stub

	}

	public void hello(String serverVersion, String protocolVersion) {
		ProtocolHello ph = new ProtocolHello(serverVersion,protocolVersion);
		try {
			server.broadcast(parser.createString(ph));
		} catch (IOException e) {
			// TODO logging
			e.printStackTrace();
		}


	}

	public void initBoard(int amountPlayers, Field[][] fields, Edge[][][] edges, Corner[][][] corners, Field bandit) {
		// TODO Auto-generated method stub

	}

	public void error(int playerId, String s) {
		// TODO Auto-generated method stub

	}

	public void chatReceiveMessage(int i, String s) {
		// TODO Auto-generated method stub

	}

	public void diceRollResult(int i, int result) {
		// TODO Auto-generated method stub

	}

	public void resourceObtain(int i, int[] resources) {
		// TODO Auto-generated method stub

	}

	public void welcome(int playerId) {
		// TODO Auto-generated method stub

	}


}
