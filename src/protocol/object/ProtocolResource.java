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

	/**
	 * Instantiates a new protocol resource.
	 *
	 * @param wood the wood
	 * @param clay the clay
	 * @param wool the wool
	 * @param corn the corn
	 * @param ore the ore
	 * @param unknown the unknown
	 */
	public ProtocolResource(Integer wood, Integer clay, Integer wool, Integer corn, Integer ore, Integer unknown) {
		this.wood = wood;
		this.clay = clay;
		this.wool = wool;
		this.corn = corn;
		this.ore = ore;
		this.unknown = unknown;
	}

	/**
	 * Gets the wood.
	 *
	 * @return the wood
	 */
	public Integer getWood() {
		return wood;
	}

	/**
	 * Gets the clay.
	 *
	 * @return the clay
	 */
	public Integer getClay() {
		return clay;
	}

	/**
	 * Gets the wool.
	 *
	 * @return the wool
	 */
	public Integer getWool() {
		return wool;
	}

	/**
	 * Gets the corn.
	 *
	 * @return the corn
	 */
	public Integer getCorn() {
		return corn;
	}

	/**
	 * Gets the ore.
	 *
	 * @return the ore
	 */
	public Integer getOre() {
		return ore;
	}

	/**
	 * Gets the unknown.
	 *
	 * @return the unknown
	 */
	public Integer getUnknown() {
		return unknown;
	}
	// TODO special adapter

}
