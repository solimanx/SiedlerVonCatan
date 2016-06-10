package network;

import enums.CornerStatus;
import enums.HarbourStatus;
import enums.ResourceType;
import model.Board;
import model.HexService;
import protocol.object.ProtocolResource;

public final class ProtocolToModel {

	/**
	 * Get Field axial coordinates through a one character ID
	 * <p>
	 * Example J -> (0,0)
	 * </p>
	 * 
	 * @param field_id
	 * @return
	 */
	public static int[] getFieldCoordinates(String field_id) {
		// Get the field ID through the Board's HashMap
		return Board.getStringToCoordMap().get(field_id);
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
			int[] a = getFieldCoordinates(location.substring(0, 1));
			int[] b = getFieldCoordinates(location.substring(1, 2));
			int[] c = getFieldCoordinates(location.substring(2, 3));
			// Calculate their common corner through HexService
			int[] result = HexService.getCornerCoordinates(a[0], a[1], b[0], b[1], c[0], c[1]);
			return result;
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
			int[] a = getFieldCoordinates(location.substring(0, 1));
			int[] b = getFieldCoordinates(location.substring(1, 2));
			// Calculate their common edge through HexService
			int[] result = HexService.getEdgeCoordinates(a[0], a[1], b[0], b[1]);
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

}
