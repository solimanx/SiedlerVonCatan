package protocol.object;

import com.google.gson.annotations.SerializedName;

/**
 * <b>4.6 Rohstoffe</b>
 * <p>
 * Contains amount of resources.
 * </p>
 * 
 * @version 0.1
 * 
 */
public class ProtocolResource {
	@SerializedName("Holz")
	int wood;

	@SerializedName("Lehm")
	int clay;

	@SerializedName("Wolle")
	int wool;

	@SerializedName("Getreide")
	int corn;

	@SerializedName("Erz")
	int ore;

	@SerializedName("Unbekannt")
	int unknown;

	public ProtocolResource(int wood, int clay, int wool, int corn, int ore, int unknown) {
		this.wood = wood;
		this.clay = clay;
		this.wool = wool;
		this.corn = corn;
		this.ore = ore;
		this.unknown = unknown;
	}

	public int getWood() {
		return wood;
	}

	public int getClay() {
		return clay;
	}

	public int getWool() {
		return wool;
	}

	public int getCorn() {
		return corn;
	}

	public int getOre() {
		return ore;
	}

	public int getUnknown() {
		return unknown;
	}
	// TODO special adapter

}
