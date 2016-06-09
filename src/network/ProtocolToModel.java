package network;

import enums.ResourceType;
import model.Board;
import model.HexService;
import protocol.object.ProtocolResource;

public final class ProtocolToModel {
	static HexService HS;

	public static int[] getFieldCoordinates(String field_id) {
		return Board.getStringToCoordMap().get(field_id);
	}

	public static int[] getCornerCoordinates(String location) {
		int[] a = Board.getStringToCoordMap().get(location.substring(0, 1));
		int[] b = Board.getStringToCoordMap().get(location.substring(1, 2));
		int[] c = Board.getStringToCoordMap().get(location.substring(2, 3));
		int[] result = HS.getCornerCoordinates(a[0], a[1], b[0], b[1], c[0], c[1]);
		return result;
	}

	public static int[] getEdgeCoordinates(String location) {
		int[] a = Board.getStringToCoordMap().get(location.substring(0, 1));
		int[] b = Board.getStringToCoordMap().get(location.substring(1, 2));
		int[] result = HS.getEdgeCoordinates(a[0], a[1], b[0], b[1]);
		return result;
	}

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
	//ENUMS ARE ALREADY SERIALIZED....
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

}
