package network;

import java.util.HashMap;

import enums.ResourceType;
import protocol.object.ProtocolResource;

public final class ModelToProtocol {

	public static HashMap<ResourceType, String> resourceToString = new HashMap<ResourceType, String>();

	public static void initModelToProtocol(){
		fillHashMap();
	}

	public static void fillHashMap(){
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
		return null;
	}
}
