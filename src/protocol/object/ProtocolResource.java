package protocol.object;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * <b>Rohstoffe (Resources)</b>
 * <p>
 * Contains amount of resources.
 * </p>
 * 
 */

@Since(0.1)
public class ProtocolResource {

	@SerializedName("Holz")
	private int wood;

	@SerializedName("Lehm")
	private int clay;

	@SerializedName("Wolle")
	private int wool;

	@SerializedName("Getreide")
	private int corn;

	@SerializedName("Erz")
	private int ore;

	@SerializedName("Unbekannt")
	private int unknown;

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
