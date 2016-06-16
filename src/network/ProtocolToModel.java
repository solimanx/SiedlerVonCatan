package network;

import enums.CornerStatus;
import enums.HarbourStatus;
import enums.ResourceType;
import model.Board;
import model.HexService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import protocol.object.ProtocolResource;

public final class ProtocolToModel {

	/**
	 * Get Field axial coordinates through a one character ID
	 * <p>
	 * Example J -> (0,0)
	 * </p>
	 *
	 * @param fieldID
	 * @return
	 */
	private static Logger logger = LogManager.getLogger(ProtocolToModel.class.getName());
	public static int[] getFieldCoordinates(String fieldID) {
		// Get the field ID through the Board's HashMap
		return Board.getStringToCoordMap().get(fieldID);
	}

	/**
	 * Get Corner axial coordinates + direction through a three-character ID
	 * <p>
	 * Example EFJ -> (0,0,0)
	 * </p>
	 *
	 * @param location
	 *            3-character-string
	 * @return
	 */
	public static int[] getCornerCoordinates(String location) {
		if (location.length() != 3)
			throw new IllegalArgumentException("CornerID has to be 3 characters");
		else {
			// Get each individual Field coordinate
			String a = location.substring(0, 1);
			String b = location.substring(1, 2);
			String c = location.substring(2, 3);
			// Calculate their common corner through HexService
			int[] result = HexService.getCornerCoordinates(a,b,c);
			if(result.length == 3)
			return result;
			else{
				throw new IllegalArgumentException("Result isn't 3 characters");}
		}
	}

	/**
	 * Get Corner axial coordinates + direction through a 2-character ID
	 * <p>
	 * Example EJ -> (0,0,0)
	 * </p>
	 *
	 * @param location
	 *            3-character-string
	 * @return
	 */
	public static int[] getEdgeCoordinates(String location) {
		if (location.length() != 2)
			throw new IllegalArgumentException("EdgeID has to be 2 characters");

		else {
			// Get each individual Field coordinate
			String a = location.substring(0, 1);
			String b = location.substring(1, 2);
			// Calculate their common edge through HexService
			int[] result = HexService.getEdgeCoordinates(a,b);
			return result;
		}
	}

	/**
	 * Convert a land type to enum.ResourceType
	 *
	 * @param resourceString
	 * @return Resource Type
	 */
	public static enums.ResourceType getResourceType(String resourceString) {
		switch (resourceString) {
		case "Weideland":
			return ResourceType.SHEEP;
		case "Wald":
			return ResourceType.WOOD;
		case "Hügelland":
			return ResourceType.CLAY;
		case "Ackerland":
			return ResourceType.CORN;
		case "Gebirge":
			return ResourceType.ORE;
		case "Wüste":
			return ResourceType.NOTHING;
		case "Meer":
			return ResourceType.SEA;
		default:
			return ResourceType.UNKNOWN;
		}
	}

	@Deprecated
	// ENUMS ARE ALREADY SERIALIZED....
	public static enums.PlayerState getPlayerState(String state) {
		switch (state) {
		case "Spiel starten":
			return enums.PlayerState.GAME_STARTING;
		case "Wartet auf Spielbeginn":
			return enums.PlayerState.WAITING_FOR_GAMESTART;
		case "Dorf bauen":
			return enums.PlayerState.BUILDING_VILLAGE;
		case "Straße bauen":
			return enums.PlayerState.BUILDING_STREET;
		case "Würfeln":
			return enums.PlayerState.DICEROLLING;
		case "Karte wegen Räuber abgeben":
			return enums.PlayerState.DISPENSE_CARDS_ROBBER_LOSS;
		case "Räuber versetzen":
			return enums.PlayerState.MOVE_ROBBER;
		case "Handeln oder Bauen":
			return enums.PlayerState.TRADING_OR_BUILDING;
		case "Warten":
			return enums.PlayerState.WAITING;
		case "Verbindung verloren":
			return enums.PlayerState.CONNECTION_LOST;
		default:
			System.out.println("Error in ProtocolToModel.getPlayerState");
			logger.error("Error in ProtocolToModel.getPlayerState");
			return null;
		}
	}

	/**
	 * Convert protocol resource to own resource array.
	 *
	 * @param resources
	 * @return ResourceArray
	 */
	public static enums.ResourceType[] getResources(ProtocolResource resources) {
		// TODO
		int wood = resources.getWood();
		int clay = resources.getClay();
		int wool = resources.getWool();
		int corn = resources.getCorn();
		int ore = resources.getOre();
		int unknown = resources.getUnknown();
		int amount = wood + clay + wool + corn + ore + unknown;
		enums.ResourceType[] result = new enums.ResourceType[amount];
		for (int i = 0; i < amount; i++) {
			if (wood != 0) {
				result[i] = enums.ResourceType.WOOD;
				wood--;
				continue;
			}
			if (clay != 0) {
				result[i] = enums.ResourceType.CLAY;
				clay--;
				continue;

			}
			if (wool != 0) {
				result[i] = enums.ResourceType.SHEEP;
				wool--;
				continue;
			}
			if (corn != 0) {
				result[i] = enums.ResourceType.CORN;
				corn--;
				continue;
			}
			if (ore != 0) {
				result[i] = enums.ResourceType.ORE;
				ore--;
				continue;
			}
			if (unknown != 0) {
				result[i] = enums.ResourceType.UNKNOWN;
				unknown--;
				continue;
			}
		}
		return result;
	}

	/**
	 * Convert Building type to match CornerStatus
	 *
	 * @param pBuildingType
	 * @return
	 */
	/*
	 * Warning: Works only for Village and City
	 */
	public static enums.CornerStatus getCornerType(String pBuildingType) {
		switch (pBuildingType) {
		case "Dorf":
			return CornerStatus.VILLAGE;
		case "Stadt":
			return CornerStatus.CITY;
		default:
			System.out.println("Error in ProtocolToModel.getCornerType");
			logger.error("Error in ProtocolToModel.getCornerType");
			return null;
		}
	}

	@Deprecated
	// ENUMS ARE ALREADY SERIALIZED....
	public static HarbourStatus getHarbourType(String pHarbourType) {
		switch (pHarbourType) {
		case "Holz Hafen":
			return HarbourStatus.WOOD;
		case "Lehm Hafen":
			return HarbourStatus.CLAY;
		case "Erz Hafen":
			return HarbourStatus.ORE;
		case "Wolle Hafen":
			return HarbourStatus.SHEEP;
		case "Getreide Hafen":
			return HarbourStatus.CORN;
		case "Hafen":
			return HarbourStatus.THREE_TO_ONE;
		default:
			throw new IllegalArgumentException("Wrong Harbour Types");
		}
	}

	// Amount of Landscape Resource Cards: {WOOD, CLAY, ORE, SHEEP, CORN,
	@Deprecated
	public static int[] convertResources(Integer wood, Integer clay, Integer ore, Integer wool, Integer corn) {
		int result[] = new int[5];
		result[0] = wood == null ? 0 : wood.intValue();
		result[1] = clay == null ? 0 : clay.intValue();
		result[2] = ore == null ? 0 : ore.intValue();
		result[3] = wool == null ? 0 : wool.intValue();
		result[4] = corn == null ? 0 : corn.intValue();
		return result;
	}

	public static int[] convertResources(ProtocolResource resource) {
		if (resource.getUnknown() == null) {
			int result[] = new int[5];
			result[0] = resource.getWood() == null ? 0 : resource.getWood().intValue();
			result[1] = resource.getClay() == null ? 0 : resource.getClay().intValue();
			result[2] = resource.getOre() == null ? 0 : resource.getOre().intValue();
			result[3] = resource.getWool() == null ? 0 : resource.getWool().intValue();
			result[4] = resource.getCorn() == null ? 0 : resource.getCorn().intValue();
			//Array of length 5
			return result;
		}
		else if (resource.getUnknown() != null){
			//array of length 1 (hiddenResource)
			return new int[]{resource.getUnknown().intValue()};
		}
		else{
			throw new IllegalArgumentException("Error at convertResources");
		}
	}

}
