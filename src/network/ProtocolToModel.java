package network;

import model.HexService;
import protocol.object.ProtocolResource;

public final class ProtocolToModel {

	public static int[] getFieldCoordinates(String field_id) {
		return HexService.getFieldCoordinates(field_id);
	}


	public static int[] getCornerCoordinates(String location) {
		// TODO
		// return [x][y][dir]
		return null;

	}

	public static int[] getEdgeCoordinates(String location) {
		// TODO
		// return [x][y][dir]
		return null;

	}

	public static enums.PlayerState getPlayerState(String state) {
		switch (state) {
		case "Spiel startet":
			return enums.PlayerState.GAME_SARTING;
		case "Warte auf Spilebeginn":
			return enums.PlayerState.WAITING_FOR_GAMESTART;
		case "Dorf bauen":
			return enums.PlayerState.BUILDING_VILLAGE;
		case "Straße bauen":
			return enums.PlayerState.BUILDING_STREET;
		case "Würfeln":
			return enums.PlayerState.DICEROLLING;
		case "Karte wegen Räuber abgeben":
			return enums.PlayerState.DISPENDE_CARDS_ROBBER_LOSS;
		case "Räuber versezen":
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
