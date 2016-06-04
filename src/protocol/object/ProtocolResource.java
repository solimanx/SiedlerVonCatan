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
	private Integer wood;

	@SerializedName("Lehm")
	private Integer clay;

	@SerializedName("Wolle")
	private Integer wool;

	@SerializedName("Getreide")
	private Integer corn;

	@SerializedName("Erz")
	private Integer ore;

	@SerializedName("Unbekannt")
	private Integer unknown;

	public ProtocolResource(Integer wood, Integer clay, Integer wool, Integer corn, Integer ore, Integer unknown) {
		this.wood = wood;
		this.clay = clay;
		this.wool = wool;
		this.corn = corn;
		this.ore = ore;
		this.unknown = unknown;
	}

	public Integer getWood() {
		return wood;
	}

	public Integer getClay() {
		return clay;
	}

	public Integer getWool() {
		return wool;
	}

	public Integer getCorn() {
		return corn;
	}

	public Integer getOre() {
		return ore;
	}

	public Integer getUnknown() {
		return unknown;
	}
	// TODO special adapter

}
