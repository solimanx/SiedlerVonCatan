package network;

import model.HexService;
import protocol.object.ProtocolResource;

public final class ProtocolToModel {

	public static int[] getFieldCoordinates(String field_id) {
		return HexService.getFieldCoordinates(field_id);
	}

	public static int getPlayerId(int thredId){
		//TODO
	}



	public static int[] getCornerCoordinates(String location){
		//TODO
		//return [x][y][dir]


	}

	public static int[] getEdgeCoordinates(String location){
		//TODO
		//return [x][y][dir]



	}

	public static enums.PlayerState getPlayerState(String state){
		//TODO
	}



	public static int[] getResources(ProtocolResource resources){
		//TODO
	}

}
