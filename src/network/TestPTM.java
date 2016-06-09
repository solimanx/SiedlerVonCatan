package network;

import model.Board;
import model.HexService;
import network.server.server.Server;

public class TestPTM {

	public static void main(String[] Args) {
		Board board = new Board();
		System.out.println(ModelToProtocol.getEdgeID(0, 0, 0));
	}
}