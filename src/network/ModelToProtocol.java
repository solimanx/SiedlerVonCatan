package network;

import java.io.ObjectInputStream.GetField;
import java.util.HashMap;
import model.Board;

import enums.ResourceType;
import protocol.object.ProtocolResource;

public final class ModelToProtocol {

	public static HashMap<ResourceType, String> resourceToString = new HashMap<ResourceType, String>();

	public static void initModelToProtocol() {
		fillHashMap();
	}

	public static void fillHashMap() {
		resourceToString.put(ResourceType.SHEEP, "Weideland");
		resourceToString.put(ResourceType.WOOD, "Wald");
		resourceToString.put(ResourceType.SEA, "Meer");
		resourceToString.put(ResourceType.ORE, "Gebirge");
		resourceToString.put(ResourceType.CLAY, "Hügelland");
		resourceToString.put(ResourceType.CORN, "Ackerland");
		resourceToString.put(ResourceType.NOTHING, "Wüste");
	}

	@Deprecated
	public static int getPlayerId(int threadID) {
		// refer to ServerNetworkController
		return 0;
	}

	public static ProtocolResource getResources(int[] resources) {
		// TODO
		Integer wood = 0;
		Integer clay = 0;
		Integer wool = 0;
		Integer corn = 0;
		Integer ore = 0;
		Integer unknown;
		for (int i = 0; i < resources.length; i++) {
			if (resources[i] == 0) {
				wood++;
				continue;
			}
			if (resources[i] == 1) {
				clay++;
				continue;
			}
			if (resources[i] == 2) {
				ore++;
				continue;
			}
			if (resources[i] == 3) {
				wool++;
				continue;
			}
			if (resources[i] == 4) {
				corn++;
				continue;
			}
		}
		unknown = wood + clay + wool + corn + ore;
		ProtocolResource pr = new ProtocolResource(wood, clay, wool, corn, ore, unknown);
		return pr;
	}

	public static String getCornerID(int x, int y, int dir) {
		String result = "";
		switch (dir){
		case 0:
			result = result + getFieldID(x, y-1);
			result = result + getFieldID(x+1, y-1);
			result = result + getFieldID(x, y);
			return result;
		case 1:
			result = result + getFieldID(x, y);
			result = result + getFieldID(x-1, y+1);
			result = result + getFieldID(x, y+1);
			return result;
		default: throw new IllegalArgumentException("Illegal dir in getCornerID");
		}
	}

	public static String getEdgeID(int x, int y, int dir) {
		String result = "";
		switch (dir){
		case 0:
			result = result + getFieldID(x, y-1);
			result = result + getFieldID(x, y);
		case 1:
			result = result + getFieldID(x+1, y-1);
			result = result + getFieldID(x, y);
		case 2:
			result = result + getFieldID(x, y);
			result = result + getFieldID(x+1, y);
		default: throw new IllegalArgumentException("Illegal dir in getEdgeID");
		}
	}

	public static String getFieldID(int x, int y) {
		return Board.getCoordToStringMap().get(new int[]{x,y});
	}
}