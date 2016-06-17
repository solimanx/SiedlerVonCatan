package network;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler;

import enums.CardType;
import enums.ResourceType;
import model.Board;
import model.Index;
import model.objects.PlayerModel;
import model.objects.DevCards.DevelopmentCard;
import protocol.object.ProtocolDevCard;
import protocol.object.ProtocolResource;

public final class ModelToProtocol {

	private static Logger logger = LogManager.getLogger(PlayerModel.class.getName());

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

	// TODO what ?
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
		switch (dir) {
		case 0:
			result = result + getFieldID(x, y - 1);
			result = result + getFieldID(x + 1, y - 1);
			result = result + getFieldID(x, y);
			return result;
		case 1:
			result = result + getFieldID(x, y);
			result = result + getFieldID(x - 1, y + 1);
			result = result + getFieldID(x, y + 1);
			return result;
		default:
			throw new IllegalArgumentException("Illegal dir in getCornerID");
		}
	}

	public static String getEdgeID(int x, int y, int dir) {
		String result = "";
		switch (dir) {
		case 0:
			result = result + getFieldID(x, y - 1);
			result = result + getFieldID(x, y);
			return result;
		case 1:
			result = result + getFieldID(x + 1, y - 1);
			result = result + getFieldID(x, y);
			return result;
		case 2:
			result = result + getFieldID(x, y);
			result = result + getFieldID(x + 1, y);
			return result;
		default:
			throw new IllegalArgumentException("Illegal dir in getEdgeID");
		}
	}

	public static String getFieldID(int x, int y) {
		Index index = new Index(x, y);
		return Board.getCoordToStringMap().get(index);
	}

	/**
	 * CONVERTS NON UNKNOWN RESOURCE ARRAYS TO PROTOCOLRESOURCE
	 *
	 * @param resources
	 * @return
	 */
	// Amount of Landscape Resource Cards: {WOOD, CLAY, ORE, SHEEP, CORN,
	public static ProtocolResource convertToProtocolResource(int[] resources) {
		// CONVERTS NON UNKNOWN RESOURCE ARRAYS TO PROTOCOLRESOURCE
		Integer wood = resources[0] == 0 ? null : resources[0];
		Integer clay = resources[1] == 0 ? null : resources[1];
		Integer ore = resources[2] == 0 ? null : resources[2];
		Integer wool = resources[3] == 0 ? null : resources[3];
		Integer corn = resources[4] == 0 ? null : resources[4];
		return (new ProtocolResource(wood, clay, wool, corn, ore, null));
	}

	public static ProtocolDevCard devCardToProtocolDevCard(DevelopmentCard devCard) {
		switch (devCard.getName()) {
		case "Knight Card":
			return new ProtocolDevCard(1, null, null, null, null, null);
		case "Victory Card":
			return new ProtocolDevCard(null, null, null, null, 1, null);
		case "Invention Card":
			return new ProtocolDevCard(null, null, null, null, 1, null);
		case "Monopoly Card":
			return new ProtocolDevCard(null, null, 1, null, null, null);
		case "Street Building Card":
			return new ProtocolDevCard(null, 1, null, null, null, null);
		default:
			logger.info(devCard.getName());
			throw new IllegalArgumentException("Invalid Development Card object");
		}
	}

	public static Index[] convertToEdgeIndex(String locationString) {
		if (locationString.length() == 2) {
			String field1 = locationString.substring(0, 1);
			String field2 = locationString.substring(1, 2);
			Index a = ProtocolToModel.getProtocolOneIndex(field1);
			Index b = ProtocolToModel.getProtocolOneIndex(field2);

			return new Index[] { a, b };
		} else {
			throw new IllegalArgumentException("Edge has to be two characters long");
		}
	}

	public static Index[] convertCornerIndex(String locationString) {
		if(locationString.length() == 3){
			String field1 = locationString.substring(0,1);
			String field2 = locationString.substring(1,2);
			String field3 = locationString.substring(2,3);
			Index a = ProtocolToModel.getProtocolOneIndex(field1);
			Index b = ProtocolToModel.getProtocolOneIndex(field2);
			Index c = ProtocolToModel.getProtocolOneIndex(field3);

			return new Index[] {a,b,c};
		}
		else{
			throw new IllegalArgumentException("Corner has to be 3 characters long");
		}
	}

	public static Index getFieldIndex(String field) {
		return ProtocolToModel.getProtocolOneIndex(field);
	}
}