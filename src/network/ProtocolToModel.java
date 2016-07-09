package network;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import enums.CardType;
import enums.CornerStatus;
import enums.HarbourStatus;
import enums.ResourceType;
import model.Board;
import model.HexService;
import model.Index;
import model.objects.DevCards.DevelopmentCard;
import model.objects.DevCards.InventionCard;
import model.objects.DevCards.KnightCard;
import model.objects.DevCards.MonopolyCard;
import model.objects.DevCards.StreetBuildingCard;
import model.objects.DevCards.UnknownCard;
import model.objects.DevCards.VictoryPointCard;
import protocol.object.ProtocolDevCard;
import protocol.object.ProtocolResource;
import settings.DefaultSettings;

// TODO: Auto-generated Javadoc
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
	private static Logger logger = LogManager.getLogger(ProtocolToModel.class.getSimpleName());
	private static Map<Index, String> hm1 = new HashMap<Index, String>();
	private static Map<String, Index> hm2 = new HashMap<String, Index>();

	/**
	 * Gets the field coordinates.
	 *
	 * @param fieldID
	 *            the field ID
	 * @return the field coordinates
	 */
	public static int[] getFieldCoordinates(String fieldID) {
		// Get the field ID through the Board's HashMap
		return Board.getStringToCoordMap().get(fieldID);
	}

	/**
	 * Get Corner axial coordinates + direction through a three-character ID
	 * <p>
	 * Example EFJ -> (0,0,0)
	 * </p>
	 * .
	 *
	 * @param location
	 *            3-character-string
	 * @return the corner coordinates
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
			int[] result = HexService.getCornerCoordinates(a, b, c);
			if (result.length == 3)
				return result;
			else {
				throw new IllegalArgumentException("Result isn't 3 characters");
			}
		}
	}

	/**
	 * Get Corner axial coordinates + direction through a 2-character ID
	 * <p>
	 * Example EJ -> (0,0,0)
	 * </p>
	 * .
	 *
	 * @param location
	 *            3-character-string
	 * @return the edge coordinates
	 */
	public static int[] getEdgeCoordinates(String location) {
		if (location.length() != 2)
			throw new IllegalArgumentException("EdgeID has to be 2 characters");

		else {
			// Get each individual Field coordinate
			String a = location.substring(0, 1);
			String b = location.substring(1, 2);
			// Calculate their common edge through HexService
			int[] result = HexService.getEdgeCoordinates(a, b);
			return result;
		}
	}

	/**
	 * Convert a land type to enum.ResourceType
	 *
	 * @param resourceString
	 *            the resource string
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

	/**
	 * Inits the protocol to model.
	 */
	public static void initProtocolToModel() {
		int r = DefaultSettings.BOARD_RADIUS;

		// Starting indices.
		char outerFieldsBegin = 'a';
		char innerFieldsBegin = 'A';

		for (int i = r; i >= -r; i--) {
			for (int j = -r; j <= r; j++) {
				if (Math.abs(i + j) <= r) {
					int comparison = HexService.sumAbsCubeXYZ(HexService.specialConvertAxialToCube(new int[] { i, j }));
					if (comparison == r * 2) {
						hm1.put(new Index(j, i), String.valueOf(outerFieldsBegin));
						hm2.put(String.valueOf(outerFieldsBegin), new Index(j, i));
						outerFieldsBegin++;
					} else if (comparison < r * 2) {
						hm1.put(new Index(j, i), String.valueOf(innerFieldsBegin));
						hm2.put(String.valueOf(innerFieldsBegin), new Index(j, i));
						innerFieldsBegin++;
					}
				}

			}

		}
	}

	/**
	 * Gets the protocol one index.
	 *
	 * @param s
	 *            the s
	 * @return the protocol one index
	 */
	public static Index getProtocolOneIndex(String s) {
		return hm2.get(s);
	}

	/**
	 * Gets the protocol one ID.
	 *
	 * @param i
	 *            the i
	 * @return the protocol one ID
	 */
	public static String getProtocolOneID(Index i) {
		return hm1.get(i);
	}

	/**
	 * Gets the player state.
	 *
	 * @param state
	 *            the state
	 * @return the player state
	 */
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
			logger.error("Error in ProtocolToModel.getPlayerState");
			return null;
		}
	}

	/**
	 * Convert protocol resource to own resource array.
	 *
	 * @param resources
	 *            the resources
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
	 * Convert Building type to match CornerStatus.
	 *
	 * @param pBuildingType
	 *            the building type
	 * @return the corner type
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
			logger.error("Error in ProtocolToModel.getCornerType");
			return null;
		}
	}

	/**
	 * Gets the harbour type.
	 *
	 * @param pHarbourType
	 *            the harbour type
	 * @return the harbour type
	 */
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

	/**
	 * Convert resources.
	 *
	 * @param wood
	 *            the wood
	 * @param clay
	 *            the clay
	 * @param ore
	 *            the ore
	 * @param wool
	 *            the wool
	 * @param corn
	 *            the corn
	 * @return the int[]
	 */
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

	/**
	 * Convert resources.
	 *
	 * @param resource
	 *            the resource
	 * @return the int[]
	 */
	public static int[] convertResources(ProtocolResource resource) {
		if (resource.getUnknown() == null) {
			int result[] = new int[5];
			result[0] = resource.getWood() == null ? 0 : resource.getWood().intValue();
			result[1] = resource.getClay() == null ? 0 : resource.getClay().intValue();
			result[2] = resource.getOre() == null ? 0 : resource.getOre().intValue();
			result[3] = resource.getWool() == null ? 0 : resource.getWool().intValue();
			result[4] = resource.getCorn() == null ? 0 : resource.getCorn().intValue();
			// Array of length 5
			return result;
		} else if (resource.getUnknown() != null) {
			// array of length 1 (hiddenResource)
			return new int[] { resource.getUnknown().intValue() };
		} else {
			throw new IllegalArgumentException("Error at convertResources");
		}
	}

	/**
	 * Intended for use by ClientInputHandler after buying a card only.
	 *
	 * @param pdv
	 *            the pdv
	 * @return the card type
	 */
	public static DevelopmentCard getCardType(ProtocolDevCard pdv) {
		if (pdv.getInvention() != null) {
			return new InventionCard();
		}
		if (pdv.getKnight() != null) {
			return new KnightCard();
		}
		if (pdv.getMonopoly() != null) {
			return new MonopolyCard();
		}
		if (pdv.getRoadbuild() != null) {
			return new StreetBuildingCard();
		}
		if (pdv.getVictoryPoint() != null) {
			return new VictoryPointCard();
		}
		if (pdv.getUnknown() != null) {
			return null;// TODO
		} else {
			throw new IllegalArgumentException("ProtocolDevCard is all null");
		}

	}

	/**
	 * Gets the corner coordinates.
	 *
	 * @param id
	 *            the id
	 * @return the corner coordinates
	 */
	public static int[] getCornerCoordinates(Index[] id) {
		String a = getProtocolOneID(id[0]) != null ? getProtocolOneID(id[0]) : "";
		String b = getProtocolOneID(id[1]) != null ? getProtocolOneID(id[1]) : "";
		String c = getProtocolOneID(id[2]) != null ? getProtocolOneID(id[2]) : "";
		return getCornerCoordinates(a + b + c);
	}

	/**
	 * Gets the edge coordinates.
	 *
	 * @param id
	 *            the id
	 * @return the edge coordinates
	 */
	public static int[] getEdgeCoordinates(Index[] id) {
		String a = getProtocolOneID(id[0]) != null ? getProtocolOneID(id[0]) : "";
		String b = getProtocolOneID(id[1]) != null ? getProtocolOneID(id[1]) : "";
		return getEdgeCoordinates(a + b);
	}

	/**
	 * Gets the corner ID index.
	 *
	 * @param id
	 *            the id
	 * @return the corner ID index
	 */
	public static String getCornerIDIndex(Index[] id) {
		String id0 = getProtocolOneID(id[0]);
		String id1 = getProtocolOneID(id[1]);
		String id2 = getProtocolOneID(id[2]);
		return id0 + id1 + id2;
	}

	/**
	 * Gets the edge ID index.
	 *
	 * @param id
	 *            the id
	 * @return the edge ID index
	 */
	public static String getEdgeIDIndex(Index[] id) {
		String id0 = getProtocolOneID(id[0]);
		String id1 = getProtocolOneID(id[1]);
		return id0 + id1;
	}

	/**
	 * Gets the dev card.
	 *
	 * @param ct
	 *            the ct
	 * @return the dev card
	 */
	public static DevelopmentCard getDevCard(CardType ct) {
		switch (ct) {
		case INVENTION:
			return new InventionCard();
		case KNIGHT:
			return new KnightCard();
		case MONOPOLY:
			return new MonopolyCard();
		case STREET:
			return new StreetBuildingCard();
		case VICTORYPOINT:
			return new VictoryPointCard();
		case UNKNOWN:
			return new UnknownCard();
		}

		throw new IllegalArgumentException("CardType doesn't exit");
	}

	/**
	 * Gets the resource from index.
	 *
	 * @param c the c
	 * @return the resource from index
	 */
	public static ResourceType getResourceFromIndex(int c) {
		switch (c) {
		case 0:
			return ResourceType.WOOD;
		case 1:
			return ResourceType.CLAY;
		case 2:
			return ResourceType.ORE;
		case 3:
			return ResourceType.SHEEP;
		case 4:
			return ResourceType.CORN;
		default:
			throw new IllegalArgumentException("Resource " + c + " doesn't exist");
		}
	}

}
