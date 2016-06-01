package server.server;

import enums.Color;
import enums.PlayerState;
import model.Corner;
import model.Edge;
import model.Field;

public class ServerOutputHandler {
	private Server server;

	public ServerOutputHandler(Server server) {
		this.server = server;
	}

	public void statusUpdate(int playerId, Color color, String name, PlayerState status, int victoryPoints,
			int[] resources) {
		// TODO Auto-generated method stub
		
	}

	public void buildBuilding(int x, int y, int dir, int playerId, String string) {
		// TODO Auto-generated method stub
		
	}

	public void hello(double serverVersion, double protocolVersion) {
		// TODO Auto-generated method stub
		
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

	public void hello(double serverVersion, double protocolVersion) {
		// TODO Auto-generated method stub
		
	}

}
