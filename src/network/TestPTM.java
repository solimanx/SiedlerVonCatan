package network;

import model.Board;
import model.HexService;
import network.server.controller.ServerNetworkController;
import network.server.server.Server;

public class TestPTM {
	
	
	public static void main(String[] Args){
		int[] i = ProtocolToModel.getCornerCoordinates("ABE");
		for(int j = 0; j<i.length; j++){
			System.out.println(i[j]);
		}
	}
}
